# 郑子龙-API回归测试台词

## 角色

API 回归负责人：客户侧 API 回归测试

## 预计时长

3-4 分钟

## 台词

大家好，我是郑子龙，负责客户侧 API 回归测试。

我的部分不是单独测某一个类，而是用 pytest 和 requests 模拟真实用户流程。这里看 `api-tests/test_regression.py`，它覆盖正常用户登录、禁用用户登录失败、公开菜单过滤、顾客下单、管理员处理订单、消息提醒生成，以及普通用户不能调用管理接口。

这部分和前面的 JUnit 不一样。JUnit 更偏代码级，能快速定位某个服务或控制器的问题；API 回归更接近用户视角，验证多个接口串起来之后，主业务流程还能不能走通。

在 GitHub Actions 里，`api-regression` 会自动启动临时 MySQL，导入 `test-data/schema.sql` 和 `test-data/seed.sql`，再启动后端执行同一套 pytest 脚本。所以它不依赖我们本机数据库，老师复查时也可以复现。

最后，如果现场需要运行 API 回归测试，并且本地后端已经启动在 `http://localhost:8080`，我会执行：

```bash
python3 -m pip install -r api-tests/requirements.txt
API_BASE_URL=http://localhost:8080 python3 -m pytest api-tests -v --junitxml=api-tests/report.xml
```
