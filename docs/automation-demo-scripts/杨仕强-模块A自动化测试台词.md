# 杨仕强-模块A自动化测试台词

## 角色

模块 A 负责人：订单交易与支付闭环 JUnit 测试

## 预计时长

2-3 分钟

## 台词

大家好，我是杨仕强，负责模块 A，也就是订单交易与支付闭环的代码级测试。

这个模块重点覆盖三个风险：第一，创建订单时订单归属不能相信前端传入的 `userId`，必须以后端 token 解析出的用户为准；第二，普通用户不能调用订单状态管理接口；第三，微信支付目前是开发模式，需要保证接口返回结构稳定。

我这里展示三个测试：`OrderServiceImplTest` 验证订单明细会绑定数据库生成的订单 id；`OrderControllerSecurityTest` 验证订单状态接口的权限和订单归属；`WxPayControllerTest` 验证开发模式支付创建和查询接口返回稳定结构。

这些测试对应测试计划里的下单、订单详情、订单状态处理和支付接口校验，也对应代码评审中发现的订单归属和越权风险。

最后，如果现场需要运行模块 A 自动化测试，我会执行：

```bash
mvn -Dtest=OrderServiceImplTest,OrderControllerSecurityTest,WxPayControllerTest test
```
