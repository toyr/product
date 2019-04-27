package demo.msa.product.service;

import demo.msa.product.model.Product;
import org.springframework.stereotype.Service;
import org.springframework.util.ReflectionUtils;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author unisk1123
 * @Description
 * @create 2019/4/27
 */
@Service
public class ProductService {

    private final static List<Product> PRODUCT_LIST = new ArrayList<Product>();
    private final static AtomicInteger idGenerator = new AtomicInteger(1);
    static {
        PRODUCT_LIST.add(new Product(generateId(), "MacBook", 10000, getCurrentTime()));
        PRODUCT_LIST.add(new Product(generateId(), "MacBook Air", 7000, getCurrentTime()));
        PRODUCT_LIST.add(new Product(generateId(), "MacBook Pro", 12000, getCurrentTime()));
    }

    public static int generateId() {
        return idGenerator.getAndIncrement();
    }

    public static long getCurrentTime() {
        return System.currentTimeMillis();
    }

    public Product getProductById(long id) {
        for (Product product : PRODUCT_LIST) {
            if (product.getId() == id) {
                return product;
            }
        }
        return null;
    }

    public Product createProduct(String name, int price) {
        Product product = new Product(generateId(), name, price, getCurrentTime());
        boolean result = PRODUCT_LIST.add(product);
        if (result) {
            return product;
        }
        return null;
    }

    public Product updateProduct(long id, Map<String, Object> fieldMap) {
        Product product = getProductById(id);
        if (product != null) {
            for (Map.Entry<String, Object> fieldEntry : fieldMap.entrySet()) {
                Object fieldValue = fieldEntry.getValue();
                if (fieldValue != null) {
                    String fieldName = fieldEntry.getKey();
                    Field field = ReflectionUtils.findField(Product.class, fieldName);
                    ReflectionUtils.makeAccessible(field);
                    ReflectionUtils.setField(field, product, fieldValue);
                }
            }
            return product;
        }
        return null;
    }

    public boolean deleteProductById(long id) {
        Iterator<Product> iterator = PRODUCT_LIST.iterator();
        while (iterator.hasNext()) {
            Product product = iterator.next();
            if (product.getId() == id) {
                iterator.remove();
                return true;
            }
        }
        return false;
    }

    public List<Product> getProductList() {
        return PRODUCT_LIST;
    }
}
