package com.mumuwest.mumumike.controller;

import com.mumuwest.mumumike.annotation.Role;
import com.mumuwest.mumumike.pojo.AjaxResult;
import com.mumuwest.mumumike.pojo.Product;
import com.mumuwest.mumumike.pojo.TableDataInfo;
import com.mumuwest.mumumike.pojo.VO.ProductVO;
import com.mumuwest.mumumike.service.ProductService;
import com.mumuwest.mumumike.utils.FileStorageUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/product")
public class ProductController {

    @Autowired
    public ProductService productService;

    /**
     * 获取产品列表
     * @param product
     * @return
     */
    @Role(role = {0, 1})
    @PostMapping("/getProductList")
    public TableDataInfo getProductList(Product product) {
        List<Product> productList = productService.getProductList(product);
        return new TableDataInfo(productList, productList.size());
    }

    /**
     * 根据id查询产品
     * @param id
     * @return
     */
    @GetMapping("/{id}")
    public AjaxResult getProductById(@PathVariable("id") Integer id) {
        return AjaxResult.success(productService.getProductById(id));
    }

    @Role(role = {0, 1})
    @PostMapping
    public AjaxResult insertProduct(@RequestParam("name") String name,
                                    @RequestParam("description") String description,
                                    @RequestParam("price") Integer price,
                                    @RequestParam("productTypeId") Integer productTypeId,
                                    @RequestParam(value = "image", required = false) MultipartFile image
                                    ) throws IOException {
        Product product = new Product();
        product.setName(name);
        product.setDescription(description);
        product.setPrice(price);
        product.setProductTypeId(productTypeId);

        if(image != null) {
            String imagePath = FileStorageUtil.storeFile(image);
            // 使用相对路径，前端根据baseUrl拼接
            String relativePath = imagePath.replace("./", "/");
            product.setPictureUrl(relativePath);
        }
        return AjaxResult.success(productService.insertProduct(product));
    }

    /**
     * 更新产品
     * @param name
     * @param description
     * @param price
     * @param productTypeId
     * @param image
     * @return
     */
    @Role(role = {0, 1})
    @PutMapping
    public AjaxResult updateProduct(@RequestParam("name") String name,
                                    @RequestParam("id") Integer id,
                                    @RequestParam("description") String description,
                                    @RequestParam("price") Integer price,
                                    @RequestParam("productTypeId") Integer productTypeId,
                                    @RequestParam(value = "image", required = false) MultipartFile image) throws IOException {
        Product product = new Product();
        product.setId(id);
        product.setName(name);
        product.setDescription(description);
        product.setPrice(price);
        product.setProductTypeId(productTypeId);

        if(image != null) {
            String imagePath = FileStorageUtil.storeFile(image);
            // 使用相对路径，前端根据baseUrl拼接
            String relativePath = imagePath.replace("./", "/");
            product.setPictureUrl(relativePath);
        }
        return AjaxResult.success(productService.updateProduct(product));
    }

    /**
     * 删除产品
     * @param id
     * @return
     */
    @Role(role = {0, 1})
    @DeleteMapping("/{id}")
    public AjaxResult deleteProduct(@PathVariable("id") Integer id) {
        return AjaxResult.success(productService.deleteProduct(id));
    }

    /**
     * 获取产品VO列表
     * @return
     */
    @GetMapping("/getProductVOList")
    public TableDataInfo getProductVOList() {
        List<ProductVO> productVOList = productService.getProductVOList();
        return new TableDataInfo(productVOList, productVOList.size());
    }

    @PutMapping("/updateProductStatus")
    @Role(role = {0, 1})
    public AjaxResult updateProductStatus(@RequestBody Product product) {
        Product productUpdate = new Product();
        productUpdate.setId(product.getId());
        productUpdate.setStatus(product.getStatus());
        return AjaxResult.success(productService.updateProduct(productUpdate));
    }


}
