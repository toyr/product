package demo.msa.product.test;

import com.jayway.jsonpath.DocumentContext;
import com.jayway.jsonpath.JsonPath;
import demo.msa.product.model.Product;
import demo.msa.product.request.ProductRequest;
import demo.msa.product.response.ProductResponse;
import org.assertj.core.api.Assertions;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.MethodSorters;
import org.skyscreamer.jsonassert.JSONAssert;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpMethod;
import org.springframework.http.RequestEntity;
import org.springframework.test.context.junit4.SpringRunner;

import java.net.URI;
import java.util.HashMap;
import java.util.Map;

/**
 * @author unisk1123
 * @Description
 * @create 2019/4/27
 */
@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class ProductControllerTest {

    @Autowired
    private TestRestTemplate restTemplate;

    @Test
    public void test1_getProductById() throws Exception {
        Product product = restTemplate.getForObject("/product/1", Product.class);
        Assertions.assertThat(product.getId()).isEqualTo(1);
        Assertions.assertThat(product.getName()).isEqualTo("MacBook");
        Assertions.assertThat(product.getPrice()).isEqualTo(10000);
        String expected = "\"id\":1,\"name\":\"MacBook\",\"price\":10000";
        String actual = restTemplate.getForObject("/product/1", String.class);
        Assertions.assertThat(actual).contains(expected);

        // 使用jsonPath
        String json = restTemplate.getForObject("/product/1", String.class);
        DocumentContext ctx = JsonPath.parse(json);
        Assertions.assertThat(ctx.read("$.id")).isEqualTo(1);
        Assertions.assertThat(ctx.read("$.name")).isEqualTo("MacBook");
        Assertions.assertThat(ctx.read("$.price")).isEqualTo(10000);

        // 使用JSONassert
        String expected2 = "{\"id\":1,\"name\":\"MacBook\",\"price\":10000}";
        String actual2 = restTemplate.getForObject("/product/1", String.class);
        JSONAssert.assertEquals(expected2, actual2, false);

    }

    @Test
    public void test2_createProduct() throws Exception {

        ProductRequest request = new ProductRequest();
        request.setName("iMac");
        request.setPrice(8000);
        String expected = "{\"id\":4,\"name\":\"iMac\",\"price\":8000}";
        String actual = restTemplate.postForObject("/product", request, String.class);
        JSONAssert.assertEquals(expected, actual, false);
    }

    @Test
    public void test3_updateProduct() throws Exception {
        String expected = "{\"id\":4,\"name\":\"iMac\",\"price\":9000}";
        Map<String, Object> fieldMap = new HashMap<String, Object>();
        fieldMap.put("price", 9000);
        RequestEntity<Map<String, Object>> requestEntity =
                new RequestEntity<Map<String, Object>>(fieldMap, HttpMethod.PUT, new URI("/product/4"));
        String actual = restTemplate.exchange(requestEntity, String.class).getBody();
        JSONAssert.assertEquals(expected, actual, false);
        int price = JsonPath.parse(actual).read("$.price");
        Assertions.assertThat(price).isEqualTo(9000);


    }

    @Test
    public void test4_deleteProductById() throws Exception {
        String expected = "{\"id\":4,\"name\":\"iMac\",\"price\":9000}";
        String actual = restTemplate.exchange("/product/4", HttpMethod.DELETE, null, String.class).getBody();
        JSONAssert.assertEquals(expected, actual, false);

    }

    @Test
    public void test5_getProductList() throws Exception {

        ProductResponse productList = restTemplate.getForObject("/product", ProductResponse.class);
        Assertions.assertThat(productList).isNotNull();
        Assertions.assertThat(productList.getTotal()).isEqualTo(3);

    }

}
