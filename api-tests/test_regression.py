import os
import time

import pytest
import requests


BASE_URL = os.getenv("API_BASE_URL", "http://localhost:8080").rstrip("/")
PASSWORD = "test123"


def request_json(method, path, token=None, **kwargs):
    headers = kwargs.pop("headers", {})
    if token:
        headers["Authorization"] = f"Bearer {token}"
    response = requests.request(method, f"{BASE_URL}{path}", headers=headers, timeout=10, **kwargs)
    try:
        body = response.json()
    except ValueError:
        body = {}
    return response, body


def wait_for_app():
    last_error = None
    for _ in range(60):
        try:
            response = requests.get(f"{BASE_URL}/product/getProductVOList", timeout=3)
            if response.status_code == 200:
                return
        except requests.RequestException as exc:
            last_error = exc
        time.sleep(1)
    raise AssertionError(f"Application did not become ready: {last_error}")


@pytest.fixture(scope="session", autouse=True)
def app_ready():
    wait_for_app()


def login(username):
    response, body = request_json(
        "POST",
        "/login",
        json={"username": username, "password": PASSWORD},
    )
    assert response.status_code == 200
    assert body["code"] == 200, body
    return body["data"]


def get_current_user(token):
    response, body = request_json("GET", "/getInfo", token=token)
    assert response.status_code == 200
    assert body["code"] == 200, body
    return body["data"]


@pytest.fixture(scope="session")
def admin_token():
    return login("admin_auto")


@pytest.fixture(scope="session")
def customer_token():
    return login("customer_auto")


@pytest.fixture(scope="session")
def customer_user(customer_token):
    return get_current_user(customer_token)


def assert_forbidden(response, body):
    assert response.status_code in (200, 401, 403)
    if response.status_code == 200:
        assert body["code"] in (401, 403), body


def test_login_success_and_disabled_user_rejected(admin_token):
    assert admin_token
    response, body = request_json(
        "POST",
        "/login",
        json={"username": "disabled_auto", "password": PASSWORD},
    )

    assert response.status_code == 200
    assert body["code"] != 200
    assert "禁用" in body["msg"]


def test_public_menu_only_contains_enabled_types_and_available_products():
    response, body = request_json("GET", "/product/getProductVOList")

    assert response.status_code == 200
    rows = body["rows"]
    assert [row["title"] for row in rows] == ["自动化测试启用分类"]
    product_names = [product["name"] for row in rows for product in row["productList"]]
    assert product_names == ["自动化上架奶茶"]


def test_customer_order_is_bound_to_authenticated_user(customer_token, customer_user):
    response, body = request_json(
        "POST",
        "/order",
        token=customer_token,
        json={
            "userId": 1,
            "product": [{"productId": 1, "count": 2, "comment": "少冰"}],
        },
    )
    assert response.status_code == 200
    assert body["code"] == 200, body
    order_id = body["data"]

    response, body = request_json("POST", "/order/getListVO", token=customer_token, json={})
    assert response.status_code == 200
    created_order = next(order for order in body["rows"] if order["id"] == order_id)
    assert created_order["user"]["id"] == customer_user["id"]
    assert created_order["user"]["username"] == "customer_auto"


def test_admin_order_status_update_creates_customer_message(admin_token, customer_token, customer_user):
    response, body = request_json(
        "POST",
        "/order",
        token=customer_token,
        json={
            "userId": 1,
            "product": [{"productId": 1, "count": 1, "comment": "正常糖"}],
        },
    )
    assert response.status_code == 200
    order_id = body["data"]

    response, body = request_json(
        "PUT",
        "/order/updateOrderStatus",
        token=admin_token,
        json={"id": order_id, "status": 2},
    )
    assert response.status_code == 200
    assert body["code"] == 200, body

    response, body = request_json(
        "GET",
        f"/message/getMessageListByUserId/{customer_user['id']}",
        token=customer_token,
    )
    assert response.status_code == 200
    assert any("订单已完成" in message["title"] and str(order_id) in message["content"] for message in body["rows"])


def test_normal_user_cannot_call_management_apis(customer_token):
    protected_calls = [
        ("POST", "/product/getProductList", {"data": {}}),
        ("PUT", "/product/updateProductStatus", {"json": {"id": 1, "status": 0}}),
        ("PUT", "/productType/updateProductTypeStatus", {"json": {"id": 1, "status": 0}}),
        ("PUT", "/order/updateOrderStatus", {"json": {"id": 100, "status": 2}}),
    ]

    for method, path, kwargs in protected_calls:
        response, body = request_json(method, path, token=customer_token, **kwargs)
        assert_forbidden(response, body)


def test_development_pay_api_returns_stable_shape():
    response, body = request_json(
        "POST",
        "/wx/pay/create",
        json={"orderId": "1001", "totalFee": 1200, "body": "自动化测试订单"},
    )
    assert response.status_code == 200
    assert body["code"] == 200
    assert {"timeStamp", "nonceStr", "package", "signType", "paySign"}.issubset(body["data"])

    response, body = request_json("GET", "/wx/pay/query/1001")
    assert response.status_code == 200
    assert body["code"] == 200
    assert body["data"] == {"orderId": "1001", "payStatus": "NOTPAY"}
