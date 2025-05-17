package com.mumuwest.mumumike.controller;

import com.mumuwest.mumumike.pojo.AjaxResult;
import com.mumuwest.mumumike.pojo.Product;
import com.mumuwest.mumumike.pojo.ProductType;
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

    @PostMapping("/getProductList")
    public TableDataInfo getProductList(Product product) {
        List<Product> productList = productService.getProductList(product);
        return new TableDataInfo(productList, productList.size());
    }

    @GetMapping("/{id}")
    public AjaxResult getProductById(@PathVariable("id") Integer id) {
        return AjaxResult.success(productService.getProductById(id));
    }

    @PostMapping
    public AjaxResult insertProduct(@RequestParam("name") String name,
                                    @RequestParam("description") String description,
                                    @RequestParam("price") Integer price,
                                    @RequestParam("productTypeId") Integer productTypeId,
                                    @RequestParam("image") MultipartFile image
                                    ) throws IOException {
        Product product = new Product();
        product.setName(name);
        product.setDescription(description);
        product.setPrice(price);
        product.setProductTypeId(productTypeId);

        if(image != null) {
            String imagePath = FileStorageUtil.storeFile(image);
            product.setPictureUrl(imagePath);
        }
        return AjaxResult.success(productService.insertProduct(product));
    }

    @PutMapping
    public AjaxResult updateProduct(@RequestParam("name") String name,
                                    @RequestParam("description") String description,
                                    @RequestParam("price") Integer price,
                                    @RequestParam("productTypeId") Integer productTypeId,
                                    @RequestParam("image") MultipartFile image) throws IOException {
        Product product = new Product();
        product.setName(name);
        product.setDescription(description);
        product.setPrice(price);
        product.setProductTypeId(productTypeId);

        if(image != null) {
            String imagePath = FileStorageUtil.storeFile(image);
            product.setPictureUrl(imagePath);
        }
        return AjaxResult.success(productService.updateProduct(product));
    }

    @DeleteMapping("/{id}")
    public AjaxResult deleteProduct(@PathVariable("id") Integer id) {
        return AjaxResult.success(productService.deleteProduct(id));
    }

    @GetMapping("/getProductVOList")
    public TableDataInfo getProductVOList() {
        List<ProductVO> productVOList = productService.getProductVOList();
        return new TableDataInfo(productVOList, productVOList.size());
    }


}
