package demo.msa.product.request;

/**
 * @author unisk1123
 * @Description
 * @create 2019/4/27
 */
public class ProductRequest {

    private String name;
    private int price;

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
