package com.mumuwest.mumumike.controller;

import com.mumuwest.mumumike.pojo.AjaxResult;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.util.*;

/**
 * 微信支付控制器
 * 注意：这是一个简化版本，实际生产环境需要完整实现微信支付流程
 */
@RestController
@RequestMapping("/wx/pay")
public class WxPayController {

    // 微信支付配置，需要在application.properties中配置
    @Value("${wx.pay.appid:}")
    private String appId;

    @Value("${wx.pay.mchid:}")
    private String mchId;

    @Value("${wx.pay.key:}")
    private String apiKey;

    @Value("${wx.pay.notifyUrl:}")
    private String notifyUrl;

    /**
     * 创建支付订单
     * @param params 支付参数
     * @return 支付参数
     */
    @PostMapping("/create")
    public AjaxResult createPayOrder(@RequestBody Map<String, Object> params) {
        try {
            String orderId = String.valueOf(params.get("orderId"));
            Integer totalFee = (Integer) params.get("totalFee"); // 单位：分
            String body = (String) params.getOrDefault("body", "木木奶茶-订单支付");

            // 检查配置
            if (appId == null || appId.isEmpty() || mchId == null || mchId.isEmpty()) {
                // 开发模式：返回模拟支付参数
                return AjaxResult.success("开发模式，请到收银台支付", createMockPayParams());
            }

            // 生成支付参数
            Map<String, String> payParams = createPayParams(orderId, totalFee, body);
            
            return AjaxResult.success(payParams);

        } catch (Exception e) {
            e.printStackTrace();
            return AjaxResult.error("创建支付订单失败：" + e.getMessage());
        }
    }

    /**
     * 支付回调通知
     * @param xmlData 微信回调的XML数据
     * @return 响应结果
     */
    @PostMapping("/notify")
    public String payNotify(@RequestBody String xmlData) {
        try {
            // 解析XML数据
            Map<String, String> notifyData = parseXml(xmlData);
            
            // 验证签名
            if (!verifySign(notifyData)) {
                return "<xml><return_code><![CDATA[FAIL]]></return_code><return_msg><![CDATA[签名验证失败]]></return_msg></xml>";
            }

            String resultCode = notifyData.get("result_code");
            String outTradeNo = notifyData.get("out_trade_no");

            if ("SUCCESS".equals(resultCode)) {
                // 支付成功，更新订单状态
                // TODO: 调用订单服务更新订单支付状态
                System.out.println("订单支付成功: " + outTradeNo);
            }

            return "<xml><return_code><![CDATA[SUCCESS]]></return_code><return_msg><![CDATA[OK]]></return_msg></xml>";
            
        } catch (Exception e) {
            e.printStackTrace();
            return "<xml><return_code><![CDATA[FAIL]]></return_code><return_msg><![CDATA[处理失败]]></return_msg></xml>";
        }
    }

    /**
     * 查询支付状态
     * @param orderId 订单ID
     * @return 支付状态
     */
    @GetMapping("/query/{orderId}")
    public AjaxResult queryPayStatus(@PathVariable String orderId) {
        try {
            // TODO: 实现查询微信支付订单状态
            // 这里返回模拟数据
            Map<String, Object> result = new HashMap<>();
            result.put("orderId", orderId);
            result.put("payStatus", "NOTPAY"); // NOTPAY-未支付, SUCCESS-已支付, CLOSED-已关闭
            
            return AjaxResult.success(result);
            
        } catch (Exception e) {
            return AjaxResult.error("查询支付状态失败");
        }
    }

    /**
     * 创建支付参数（调用微信统一下单接口）
     */
    private Map<String, String> createPayParams(String orderId, Integer totalFee, String body) throws Exception {
        // 生成预支付订单
        String nonceStr = generateNonceStr();
        String timeStamp = String.valueOf(System.currentTimeMillis() / 1000);

        // 构建统一下单请求参数
        SortedMap<String, String> unifiedOrderParams = new TreeMap<>();
        unifiedOrderParams.put("appid", appId);
        unifiedOrderParams.put("mch_id", mchId);
        unifiedOrderParams.put("nonce_str", nonceStr);
        unifiedOrderParams.put("body", body);
        unifiedOrderParams.put("out_trade_no", orderId + "_" + System.currentTimeMillis());
        unifiedOrderParams.put("total_fee", String.valueOf(totalFee));
        unifiedOrderParams.put("spbill_create_ip", "127.0.0.1");
        unifiedOrderParams.put("notify_url", notifyUrl);
        unifiedOrderParams.put("trade_type", "JSAPI");
        // 注意：实际需要传入用户的openid
        // unifiedOrderParams.put("openid", openid);

        // 生成签名
        String sign = generateSign(unifiedOrderParams);
        unifiedOrderParams.put("sign", sign);

        // TODO: 调用微信统一下单接口获取prepay_id
        // 这里返回模拟数据
        String prepayId = "wx_prepay_" + System.currentTimeMillis();

        // 构建小程序支付参数
        SortedMap<String, String> payParams = new TreeMap<>();
        payParams.put("appId", appId);
        payParams.put("timeStamp", timeStamp);
        payParams.put("nonceStr", nonceStr);
        payParams.put("package", "prepay_id=" + prepayId);
        payParams.put("signType", "MD5");

        // 生成支付签名
        String paySign = generateSign(payParams);

        Map<String, String> result = new HashMap<>();
        result.put("timeStamp", timeStamp);
        result.put("nonceStr", nonceStr);
        result.put("package", "prepay_id=" + prepayId);
        result.put("signType", "MD5");
        result.put("paySign", paySign);

        return result;
    }

    /**
     * 创建模拟支付参数（开发测试用）
     */
    private Map<String, String> createMockPayParams() {
        Map<String, String> result = new HashMap<>();
        result.put("timeStamp", String.valueOf(System.currentTimeMillis() / 1000));
        result.put("nonceStr", generateNonceStr());
        result.put("package", "prepay_id=mock_prepay_" + System.currentTimeMillis());
        result.put("signType", "MD5");
        result.put("paySign", "mock_sign_" + System.currentTimeMillis());
        return result;
    }

    /**
     * 生成随机字符串
     */
    private String generateNonceStr() {
        return UUID.randomUUID().toString().replace("-", "").substring(0, 32);
    }

    /**
     * 生成签名
     */
    private String generateSign(SortedMap<String, String> params) throws Exception {
        StringBuilder sb = new StringBuilder();
        for (Map.Entry<String, String> entry : params.entrySet()) {
            if (entry.getValue() != null && !entry.getValue().isEmpty() && !"sign".equals(entry.getKey())) {
                sb.append(entry.getKey()).append("=").append(entry.getValue()).append("&");
            }
        }
        sb.append("key=").append(apiKey);

        MessageDigest md = MessageDigest.getInstance("MD5");
        byte[] digest = md.digest(sb.toString().getBytes(StandardCharsets.UTF_8));
        
        StringBuilder hexString = new StringBuilder();
        for (byte b : digest) {
            String hex = Integer.toHexString(0xff & b);
            if (hex.length() == 1) hexString.append('0');
            hexString.append(hex);
        }
        return hexString.toString().toUpperCase();
    }

    /**
     * 验证签名
     */
    private boolean verifySign(Map<String, String> params) {
        try {
            String sign = params.get("sign");
            if (sign == null) return false;
            
            SortedMap<String, String> sortedParams = new TreeMap<>(params);
            String calculatedSign = generateSign(sortedParams);
            
            return sign.equals(calculatedSign);
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * 解析XML（简化版本）
     */
    private Map<String, String> parseXml(String xml) {
        Map<String, String> result = new HashMap<>();
        // 简化处理，实际应该使用XML解析库
        // 这里仅作示例
        return result;
    }
}
