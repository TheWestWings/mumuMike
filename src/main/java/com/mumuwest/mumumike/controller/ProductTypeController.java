package com.mumuwest.mumumike.controller;

import com.mumuwest.mumumike.annotation.Role;
import com.mumuwest.mumumike.pojo.AjaxResult;
import com.mumuwest.mumumike.pojo.Product;
import com.mumuwest.mumumike.pojo.ProductType;
import com.mumuwest.mumumike.pojo.TableDataInfo;
import com.mumuwest.mumumike.service.ProductTypeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/productType")
public class ProductTypeController {

    @Autowired
    public ProductTypeService productTypeService;

    /**
     * 获取产品类型列表
     * @param productType
     * @return
     */
    @Role(role = {0, 1})
    @PostMapping("/getProductTypeList")
    public TableDataInfo getProductTypeList(ProductType productType) {
        return new TableDataInfo(productTypeService.getProductTypeList(productType), productTypeService.getProductTypeList(productType).size());
    }

    /**
     * 根据id获取产品类型
     * @param id
     * @return
     */
    @Role(role = {0, 1})
    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable("id") Integer id){
        return AjaxResult.success(productTypeService.getProductTypeById(id));
    }

    /**
     * 新建产品类型
     * @param productType
     * @return
     */
    @Role(role = {0, 1})
    @PostMapping
    public AjaxResult insertProductType(@RequestBody ProductType productType) {
        return AjaxResult.success(productTypeService.insertProductType(productType));
    }

    /**
     * 更新产品类型
     * @param productType
     * @return
     */
    @Role(role = {0, 1})
    @PutMapping
    public AjaxResult updateProductType(@RequestBody ProductType productType) {
        return AjaxResult.success(productTypeService.updateProductType(productType));
    }

    /**
     * 删除产品类型
     * @param id
     * @return
     */
    @Role(role = {0, 1})
    @DeleteMapping("/{id}")
    public AjaxResult deleteProductType(@PathVariable("id") Integer id) {
        ProductType productType = new ProductType();
        productType.setId(id);
        return AjaxResult.success(productTypeService.deleteProductType(productType));
    }

    /**
     * 修改产品类型状态
     * @param productType
     * @return
     */
    @PutMapping("/updateProductTypeStatus")
    public AjaxResult updateProductTypeStatus(@RequestBody ProductType productType) {
        ProductType productTypeUpdate = new ProductType();
        productTypeUpdate.setId(productType.getId());
        productTypeUpdate.setStatus(productType.getStatus());
        return AjaxResult.success(productTypeService.updateProductType(productTypeUpdate));
    }

}
