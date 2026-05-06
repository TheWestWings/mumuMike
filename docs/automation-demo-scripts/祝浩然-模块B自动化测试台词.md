# 祝浩然-模块B自动化测试台词

## 角色

模块 B 负责人：菜品与菜单管理 JUnit 测试

## 预计时长

2-3 分钟

## 台词

大家好，我是祝浩然，负责模块 B，也就是菜品与菜单管理的代码级测试。

这个模块重点覆盖三个风险：第一，商品和分类管理接口必须有角色权限；第二，商品编辑时图片应该是可选项，不能因为不换图片就无法编辑；第三，小程序公开菜单只能展示启用分类和上架商品。

我这里展示三个测试：`ProductControllerSecurityTest` 验证商品管理接口有权限注解；`ProductTypeControllerSecurityTest` 验证分类管理接口有权限注解；`ProductServiceImplTest` 验证菜单 VO 查询时只包含启用分类和上架商品。

这些测试对应测试计划里的后台新增商品、商品上下架、分类停用和菜单刷新，也能防止普通用户越权调用后台管理接口。

最后，如果现场需要运行模块 B 自动化测试，我会执行：

```bash
mvn -Dtest=ProductControllerSecurityTest,ProductTypeControllerSecurityTest,ProductServiceImplTest test
```
