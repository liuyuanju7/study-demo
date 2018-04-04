package demo.http.httpclient;

import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

import java.io.IOException;

/**
 * Created by liuyuanju1 on 2017/12/12.
 * 模拟访问 sina 首页
 */
public class LoginSinaDemo {
    public static void main(String[] args) throws IOException {
        CloseableHttpClient httpClient = HttpClients.createDefault();
        HttpGet httpGet = new HttpGet("http://www.sina.com.cn");
        //请求首部--可选的，User-Agent对于一些服务器必选，不加可能不会返回正确结果
        httpGet.setHeader("User-Agent", "Mozilla/5.0 (Windows NT 6.1; WOW64; rv:39.0) Gecko/20100101 Firefox/39.0");
        //执行请求
        CloseableHttpResponse response = httpClient.execute(httpGet);

        //获得起始行
        System.out.println(response.getStatusLine().toString() + "\n");
        //获得首部
        Header[] headers = response.getAllHeaders();
        for(Header h : headers){
            System.out.println(h.getName() + " : " + h.getValue());
        }
        //获取实体
        try{
            HttpEntity entity = response.getEntity();
            System.out.println(EntityUtils.toString(entity,"UTF-8"));
            EntityUtils.consume(entity);
        }finally {
            response.close();
        }
        httpClient.close();

    }
}
