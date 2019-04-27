package demo.msa.product.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import demo.msa.product.model.Product;

import java.util.List;

/**
 * @author unisk1123
 * @Description
 * @create 2019/4/27
 */
public class ProductResponse {

    @JsonProperty("item")
    private List<Product> productList;

    private int total;

    public List<Product> getProductList() {
        return productList;
    }

    public void setProductList(List<Product> productList) {
        this.productList = productList;
    }

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }
}
