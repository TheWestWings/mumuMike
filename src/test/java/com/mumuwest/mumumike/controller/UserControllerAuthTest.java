package com.mumuwest.mumumike.controller;

import com.mumuwest.mumumike.pojo.AjaxResult;
import org.junit.jupiter.api.Test;
import org.springframework.mock.web.MockHttpServletRequest;

import static org.assertj.core.api.Assertions.assertThat;

class UserControllerAuthTest {

    @Test
    void getInfoReturnsUnauthorizedWhenAuthorizationHeaderIsMissing() {
        UserController controller = new UserController();

        AjaxResult result = controller.getInfo(new MockHttpServletRequest());

        assertThat(result.get("code")).isEqualTo(401);
        assertThat(result.get("msg")).isEqualTo("未登录或token无效");
    }

    @Test
    void getInfoReturnsUnauthorizedWhenAuthorizationHeaderIsNotBearerToken() {
        UserController controller = new UserController();
        MockHttpServletRequest request = new MockHttpServletRequest();
        request.addHeader("Authorization", "Token abc");

        AjaxResult result = controller.getInfo(request);

        assertThat(result.get("code")).isEqualTo(401);
        assertThat(result.get("msg")).isEqualTo("未登录或token无效");
    }
}
