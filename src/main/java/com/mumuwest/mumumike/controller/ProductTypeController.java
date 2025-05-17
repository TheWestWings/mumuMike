package com.mumuwest.mumumike.controller;

import com.mumuwest.mumumike.annotation.Role;
import com.mumuwest.mumumike.pojo.AjaxResult;
import com.mumuwest.mumumike.pojo.ProductType;
import com.mumuwest.mumumike.pojo.TableDataInfo;
import com.mumuwest.mumumike.service.ProductTypeService;
import org.apache.ibatis.annotations.Delete;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController("/productType")
public class ProductTypeController {
    @Autowired
    public ProductTypeService productTypeService;

    @Role(role = {0, 1})
    @PostMapping("/getProductTypeList")
    public TableDataInfo getProductTypeList(@RequestBody ProductType productType) {
        return new TableDataInfo(productTypeService.getProductTypeList(productType), productTypeService.getProductTypeList(productType).size());
    }

    @Role(role = {0, 1})
    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable("id") Integer id){
        return AjaxResult.success(productTypeService.getProductTypeById(id));
    }

    @Role(role = {0, 1})
    @PostMapping
    public AjaxResult insertProductType(@RequestBody ProductType productType) {
        return AjaxResult.success(productTypeService.insertProductType(productType));
    }

    @Role(role = {0, 1})
    @PutMapping
    public AjaxResult updateProductType(@RequestBody ProductType productType) {
        return AjaxResult.success(productTypeService.updateProductType(productType));
    }

    @DeleteMapping("/{id}")
    public AjaxResult deleteProductType(@PathVariable("id") Integer id) {
        ProductType productType = new ProductType();
        productType.setId(id);
        return AjaxResult.success(productTypeService.deleteProductType(productType));
    }


}
