package com.mumuwest.mumumike.controller;

import com.mumuwest.mumumike.pojo.AjaxResult;
import org.junit.jupiter.api.Test;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

class WxPayControllerTest {

    @Test
    void createPayOrderReturnsMockPayParamsInDevelopmentMode() {
        WxPayController controller = new WxPayController();
        ReflectionTestUtils.setField(controller, "appId", "");
        ReflectionTestUtils.setField(controller, "mchId", "");

        AjaxResult result = controller.createPayOrder(Map.of(
                "orderId", "1001",
                "totalFee", 2500,
                "body", "自动化测试订单"
        ));

        assertThat(result.get("code")).isEqualTo(200);
        assertThat(result.get("msg")).isEqualTo("开发模式，请到收银台支付");
        @SuppressWarnings("unchecked")
        Map<String, Object> data = (Map<String, Object>) result.get("data");
        assertThat(data)
                .containsKeys("timeStamp", "nonceStr", "package", "signType", "paySign");
    }

    @Test
    void queryPayStatusReturnsStableStatusShape() {
        WxPayController controller = new WxPayController();

        AjaxResult result = controller.queryPayStatus("1001");

        assertThat(result.get("code")).isEqualTo(200);
        @SuppressWarnings("unchecked")
        Map<String, Object> data = (Map<String, Object>) result.get("data");
        assertThat(data)
                .containsEntry("orderId", "1001")
                .containsEntry("payStatus", "NOTPAY");
    }
}
