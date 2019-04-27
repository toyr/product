package demo.msa.product.controller;

import demo.msa.product.model.Product;
import demo.msa.product.request.ProductRequest;
import demo.msa.product.response.ProductResponse;
import demo.msa.product.service.ProductService;
import io.swagger.annotations.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * @author unisk1123
 * @Description
 * @create 2019/4/27
 */
@RestController
public class ProductController {

    @Autowired
    private ProductService productService;

    @ApiOperation("根据产品ID查询产品")
    @ApiImplicitParams({
            @ApiImplicitParam(value = "产品ID", paramType = "path", dataType = "long", name = "id", required = true)
    })
    @ApiResponses({
            @ApiResponse(code = 200, message = "成功"),
            @ApiResponse(code = 400, message = "错误请求"),
            @ApiResponse(code = 600, message = "无效的产品ID")
    })
    @GetMapping("/product/{id}")
    public Product getProductById(@PathVariable("id") long id) {
        return productService.getProductById(id);
    }

    @PostMapping("product")
    public Product createProduct(@RequestBody ProductRequest productRequest) {
        String name = productRequest.getName();
        int price = productRequest.getPrice();
        return productService.createProduct(name, price);
    }

    @PutMapping("product/{id}")
    public Product updateProduct(@PathVariable("id") long id, @RequestBody Map<String, Object> fieldMap) {
        return productService.updateProduct(id, fieldMap);
    }

    @DeleteMapping("/product/{id}")
    public Product deleteProductById(@PathVariable("id") long id) {
        Product product = productService.getProductById(id);
        return productService.deleteProductById(id) ? product : null;
    }

    @GetMapping("/product")
    public ProductResponse getAllProducts() {
        List<Product> productList = productService.getProductList();
        ProductResponse response = new ProductResponse();
        response.setProductList(productList);
        response.setTotal(productList.size());
        return response;
    }
}
