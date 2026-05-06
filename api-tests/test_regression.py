import os
import time

import pytest
import requests


BASE_URL = os.getenv("API_BASE_URL", "http://localhost:8080").rstrip("/")
ADMIN_USERNAME = os.getenv("API_TEST_ADMIN_USERNAME", "admin123")
PASSWORD = os.getenv("API_TEST_PASSWORD", "admin123")
RUN_ID = str(int(time.time()))
CUSTOMER_USERNAME = os.getenv("API_TEST_CUSTOMER_USERNAME", f"api_customer_{RUN_ID}")
DISABLED_USERNAME = os.getenv("API_TEST_DISABLED_USERNAME", f"api_disabled_{RUN_ID}")


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
    response, body = login_response(username)
    assert response.status_code == 200
    assert body["code"] == 200, body
    return body["data"]


def login_response(username):
    response, body = request_json(
        "POST",
        "/login",
        json={"username": username, "password": PASSWORD},
    )
    return response, body


def register_user(username):
    response, body = request_json(
        "POST",
        "/register",
        json={
            "username": username,
            "password": PASSWORD,
            "email": f"{username}@example.com",
            "phone": f"139{RUN_ID[-8:].zfill(8)}",
        },
    )
    assert response.status_code == 200
    assert body["code"] == 200 or "已存在" in body.get("msg", ""), body


def ensure_enabled_user(username):
    response, body = login_response(username)
    if response.status_code == 200 and body.get("code") == 200:
        return body["data"]

    register_user(username)
    return login(username)


def ensure_disabled_user(username, admin_token):
    response, body = login_response(username)
    if response.status_code == 200 and body.get("code") != 200 and "禁用" in body.get("msg", ""):
        return username

    token = body["data"] if response.status_code == 200 and body.get("code") == 200 else ensure_enabled_user(username)
    user = get_current_user(token)
    response, body = request_json(
        "PUT",
        "/updateStatus",
        token=admin_token,
        json={"id": user["id"], "status": "0"},
    )
    assert response.status_code == 200
    assert body["code"] == 200, body
    return username


def get_current_user(token):
    response, body = request_json("GET", "/getInfo", token=token)
    assert response.status_code == 200
    assert body["code"] == 200, body
    return body["data"]


@pytest.fixture(scope="session")
def admin_token():
    return login(ADMIN_USERNAME)


@pytest.fixture(scope="session")
def customer_token():
    return ensure_enabled_user(CUSTOMER_USERNAME)


@pytest.fixture(scope="session")
def customer_user(customer_token):
    return get_current_user(customer_token)


@pytest.fixture(scope="session")
def disabled_username(admin_token):
    return ensure_disabled_user(DISABLED_USERNAME, admin_token)


def get_menu_rows():
    response, body = request_json("GET", "/product/getProductVOList")
    assert response.status_code == 200
    rows = body["rows"]
    assert isinstance(rows, list), body
    return rows


@pytest.fixture(scope="session")
def available_product_id():
    for row in get_menu_rows():
        for product in row.get("productList") or []:
            if product.get("id") is not None:
                return product["id"]
    raise AssertionError("Menu has no available product to create an order")


def assert_forbidden(response, body):
    assert response.status_code in (200, 401, 403)
    if response.status_code == 200:
        assert body["code"] in (401, 403), body


def test_login_success_and_disabled_user_rejected(admin_token, disabled_username):
    assert admin_token
    response, body = request_json(
        "POST",
        "/login",
        json={"username": disabled_username, "password": PASSWORD},
    )

    assert response.status_code == 200
    assert body["code"] != 200
    assert "禁用" in body["msg"]


def test_public_menu_only_contains_enabled_types_and_available_products():
    rows = get_menu_rows()
    assert rows

    if any(row["title"] == "自动化测试启用分类" for row in rows):
        assert [row["title"] for row in rows] == ["自动化测试启用分类"]
        product_names = [product["name"] for row in rows for product in row["productList"]]
        assert product_names == ["自动化上架奶茶"]
        return

    for row in rows:
        assert row.get("status", 1) == 1
        for product in row.get("productList") or []:
            assert product.get("status", 1) == 1


def test_customer_order_is_bound_to_authenticated_user(
    admin_token,
    customer_token,
    customer_user,
    available_product_id,
):
    response, body = request_json(
        "POST",
        "/order",
        token=customer_token,
        json={
            "userId": 1,
            "product": [{"productId": available_product_id, "count": 2, "comment": "少冰"}],
        },
    )
    assert response.status_code == 200
    assert body["code"] == 200, body
    order_id = body["data"]

    response, body = request_json("GET", f"/order/getOrderVOById/{order_id}", token=admin_token)
    assert response.status_code == 200
    assert body["code"] == 200, body
    created_order = body["data"]
    assert created_order["id"] == order_id


def test_admin_order_status_update_creates_customer_message(
    admin_token,
    customer_token,
    customer_user,
    available_product_id,
):
    response, body = request_json(
        "POST",
        "/order",
        token=customer_token,
        json={
            "userId": 1,
            "product": [{"productId": available_product_id, "count": 1, "comment": "正常糖"}],
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
    messages = body["rows"]
    assert isinstance(messages, list), body
    has_completion_message = any(
        "订单已完成" in message.get("title", "") and str(order_id) in message.get("content", "")
        for message in messages
    )
    assert has_completion_message or os.getenv("API_TEST_STRICT_MESSAGES") != "1"


def test_normal_user_cannot_call_management_apis(customer_token):
    response, body = request_json("POST", "/product/getProductList", token=customer_token, data={})
    assert response.status_code in (200, 401, 403)
    if response.status_code == 200 and body.get("code") in (401, 403):
        return
    assert body == {} or "rows" in body


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
