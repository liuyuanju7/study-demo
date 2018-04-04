package demo.http.httpclient;

import org.apache.http.*;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.entity.BufferedHttpEntity;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicHeaderElementIterator;
import org.apache.http.message.BasicHttpResponse;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.junit.Test;

import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;


/**
 * Created by liuyuanju1 on 2017/12/11.
 */
public class HttpClientDemo {
    /**
     * 1.1执行请求
     * HttpClient最基本的功能是执行HTTP方法，一个 HTTP 方法的执行包含一个或多个 HTTP 请求/HTTP 相应的交换，
     * 通常由 HttpClient的内部来处理。使用者被要求提供一个Request对象来执行，
     * HttpClient就会把请求传送给目标服务器并返回一个相对应的response对象，如果执行不成功，将会抛出一个异常。
     * 显然，HttpClient API 的主要切入点就是定义描述上述契约的HttpClient接口。
     */

    @Test  //一个简单请求实例
    public void testHttp() throws IOException {
        CloseableHttpClient httpClient = HttpClients.createDefault();
        HttpGet httpGet = new HttpGet("http://www.baidu.com");
        CloseableHttpResponse response = null;
        try {
            response = httpClient.execute(httpGet);
        } catch (IOException e) {
            e.printStackTrace();
        }finally {
            response.close();
        }
    }

    /**
     * 1.1.1. HTTP 请求（Request）
     * 所有 HTTP 请求都有一个请求起始行，这个起始行由方法名，请求 URI 和 HTTP 协议版本组成。
     * HttpClient很好地支持了HTTP/1.1规范中所有的HTTP方法：GET，HEAD， POST，PUT， DELETE， TRACE 和 OPTIONS。
     * 每个方法都有一个特别的类：HttpGet，HttpHead， HttpPost，HttpPut，HttpDelete，HttpTrace和HttpOptions。
     * URI是统一资源标识符的缩写，用来标识与请求相符合的资源。
     * HTTP 请求URI包含了一个协议名称，主机名，可选端口，资源路径，可选的参数，可选的片段。
     */

    /**
     * HttpClient提供了URIBuilder工具类来简化创建、修改请求 URIs。
     * @throws URISyntaxException
     */
    @Test
    public void testUri() throws URISyntaxException {
        URI uri = new URIBuilder()
                .setScheme("http")
                .setHost("www.google.com")
                .setPath("/search")
                .setParameter("q","httpclient")
                .setParameter("btnG","Google search")
                .setParameter("aq","f")
                .setParameter("oq","")
                .build();
        HttpGet httpGet = new HttpGet(uri);
        System.out.println(httpGet.getURI());

    }


    /**
     * 1.1.2.HTTP 响应（Response）
     * HTTP 相应是服务器接收并解析请求信息后返回给客户端的信息，
     * 它的起始行包含了一个协议版本，一个状态码和描述状态的短语。
     */
    @Test
    public void testResponse(){
        HttpResponse response = new BasicHttpResponse(HttpVersion.HTTP_1_1, HttpStatus.SC_OK,"ok");
        System.out.println(response.getProtocolVersion());
        System.out.println(response.getStatusLine().getStatusCode());
        System.out.println(response.getStatusLine().toString());
    }

    /**
     * 1.1.3.处理报文首部（Headers）
     *一个HTTP报文包含了许多描述报文的首部，比如内容长度，内容类型等。
     * HttpClient提供了一些方法来取出，添加，移除，枚举首部。
     */

    @Test
    public void testHeaders(){
        HttpResponse response = new BasicHttpResponse(HttpVersion.HTTP_1_1, HttpStatus.SC_OK,"ok");
        response.addHeader("Set-Cookie","c1=a; path=/; domain=localhost");
        response.addHeader("Set-Cookie","c2=b; path=\"/\",c3=c; domain=\"localhost\"");
        Header h1 = response.getFirstHeader("Set-Cookie");
        System.out.println(h1);
        Header h2 = response.getLastHeader("Set-Cookie");
        System.out.println(h2);
        Header[] headers = response.getHeaders("Set-Cookie");
        System.out.println(headers.length);

        //获得所有指定类型首部最有效的方式是使用HeaderIterator接口
        HeaderIterator it = response.headerIterator("Set-Cookie");
        while(it.hasNext()){
            System.out.println(it.next());
        }
        System.out.println("-------------");
        //HttpClient也提供了其他便利的方法吧HTTP报文转化为单个的HTTP元素
        HeaderElementIterator elemIt = new BasicHeaderElementIterator(response.headerIterator("Set-Cookie"));
        while(elemIt.hasNext()){
            HeaderElement element = elemIt.nextElement();
            System.out.println(element.getName() + " = " + element.getValue());
            NameValuePair[] params = element.getParameters();
            for(int i = 0; i < params.length; i++){
                System.out.println(" " + params[i]);
            }
        }
    }

    /**
     * 1.1.4.HTTP实体（HTTP Entity）
     HTTP报文能够携带与请求或相应相关联的内容实体。实体存在于某些请求、响应中，它门是可选的。
     使用实体的请求被称为内含实体请求【译者：原文为entity enclosing requests，我把它翻译为 内含实体请求】。
     HTTP规范定义了两种内含实体请求，POST和PUT。而响应总是内含实体。
     但有些响应不符合这一规则，比如，对HEAD方法的响应和状态为204 No Content, 304 Not Modified, 205 Reset Content的响应。
     依据实体的内容来源，HttpClient区分出三种实体：
     流式实体（streamed）：内容来源于一个流，或者在运行中产生。特别的，这个类别包括从响应中接收到的实体。流式实体不可重复。
     自包含实体（self-contained）：在内存中的内容或者通过独立的连接/其他实体获得的内容。自包含实体是可重复的。这类实体大部分是HTTP内含实体请求。
     包装实体（wrapping）：从另外一个实体中获取内容。
     */

    /**
     * 1.1.4.1 可重复实体
     一个实体能够被重复，意味着它的内容能够被读取多次。它仅可能是自包含实体（像ByteArrayEntity或StringEntity）
     */

    /**
     * 1.1.4.2 使用HTTP实体

     由于一个实体能够表现为二进制和字符内容，它支持二进制编码（为了支持后者，即字符内容）。
     实体将会在一些情况下被创建：当执行一个含有内容的请求时或者当请求成功，响应体作为结果返回给客户端时。
     为了读取实体的内容，可以通过HttpEntity#getContent() 方法取出输入流，返回一个java.io.InputStream，
     或者提供一个输出流给HttpEntity#writeTo(OutputStream) 方法，它将会返回写入给定流的所有内容。
     当实体内部含有信息时，使用HttpEntity#getContentType()和HttpEntity#getContentLength()方法将会读取一些基本的元数据，
     比如Content-Type和Content-Length这样的首部（如果他们可用的话），由于Content-Type首部能够包含文本MIME类型（像 text/plain 或text/html），
     它也包含了与MIME类型相对应的字符编码，HttpEntity#getContentEncoding()方法被用来读取这些字符编码。
     如果对应的首部不存在，则Content-Length的返回值为-1，Content-Type返回值为NULL。
     如果Content-Type是可用的，一个Header类的对象将会返回。
     当我们构建一个具有可用信息的实体时，元数据将会被实体构建器提供。
     【译者：我理解的是，如下面例子中的长度，并没有声明，但是可用，是构建器生成的】
     */
    @Test
    public void testEntity() throws IOException {
        StringEntity entity = new StringEntity("important message",
                ContentType.create("text/plain","Utf-8"));
        System.out.println(entity.getContentType());
        System.out.println(entity.getContentLength());
        System.out.println(EntityUtils.toString(entity));
        System.out.println(EntityUtils.toByteArray(entity).length);
    }

    /**
     * 1.1.5.确保释放低级别的资源
     为了确保正确的释放系统资源，你必须关掉与实体相关的的内容流，还必须关掉响应本身。
     关闭内容流和关闭响应的不同点是：前者将会通过消费实体内容保持潜在的连接，而后者迅速的关闭并丢弃连接。
     请注意，一旦实体被HttpEntity#writeTo(OutputStream)方法成功地写入时，也需要确保正确地释放系统资源。
     如果方法获得通过HttpEntity#getContent()，它也需要在一个finally子句中关闭流。
     当使用实体时，你可以使用EntityUtils#consume(HttpEntity)方法来确保实体内容完全被消费并且使潜在的流关闭。
     某些情况，整个响应内容的仅仅一小部分需要被取出，会使消费其他剩余内容的性能代价和连接可重用性代价太高，
     这时可以通过关闭响应来终止内容流。
     【译者：例子中可以看出，实体输入流仅仅读取了两个字节，就关闭了响应，也就是按需读取，而不是读取全部响应】
     这样，连接将不会被重用，它分配的所有级别的资源将会被解除。
     */

    @Test
    public void testStream() throws IOException {
        CloseableHttpClient httpClient = HttpClients.createDefault();
        HttpGet httpGet = new HttpGet("http://www.google.com/");
        CloseableHttpResponse response = httpClient.execute(httpGet);

        try{
            HttpEntity entity = response.getEntity();
            if(entity != null){
                InputStream inputStream = entity.getContent();
                try{
                    //做相应处理
                    int byteOne = inputStream.read();
                    int byteTwo = inputStream.read();
                    System.out.println(byteOne + "   " + byteTwo);
                }finally {
                    inputStream.close();
                }
            }
        }finally {
            response.close();
        }
    }

    /**
     * 1.1.6.消费实体内容

     为了消费实体内容，推荐的方式是使用HttpEntity#getContent()或者 HttpEntity#writeTo(OutputStream)方法。
     HttpClient也提供了一个EntityUtils类，它有几个静态方法更容易的从实体中读取内容或信息。
     取代了直接读取java.io.InputStream，你可以通过这个类的方法取出全部内容体并放入一个String 中或者byte数组中。
     可是，强烈不建议使用EntityUtils，除非响应实体来自于信任的HTTP服务器并且知道它的长度。
     */

    @Test
    public void testConsumeEntity() throws IOException {
        CloseableHttpClient httpClient = HttpClients.createDefault();
        HttpGet httpGet = new HttpGet("https://www.google.com/");
        CloseableHttpResponse response = httpClient.execute(httpGet);

        try{
            HttpEntity entity = response.getEntity();
            if(null != entity){
                long len = entity.getContentLength();
                if(len != -1 && len < 2048){
                    System.out.println(EntityUtils.toString(entity));
                }else {
                    // stream content out
                }
            }
        }finally {
            response.close();
        }
    }

    /**
     * 在某些情况下，多次读取实体内容是必要的。在这种情况下，实体内容必须以一些方式缓冲，内存或者硬盘中。
     * 为了达到这个目的，最简单的方法是把原始的实体用BufferedHttpEntity类包装起来。
     * 这将会使原始实体的内容读入一个in-memory缓冲区。所有方式的实体包装都是代表最原始的那个实体。
     */
    @Test
    public void testMemory() throws IOException{
        CloseableHttpClient httpClient = HttpClients.createDefault();
        HttpGet httpGet = new HttpGet("https://www.google.com/");
        CloseableHttpResponse response = httpClient.execute(httpGet);
        try{
            HttpEntity entity = response.getEntity();
            if(entity != null){
                entity = new BufferedHttpEntity(entity);
            }
        }finally {
            response.close();
        }
    }

    /**
     * 1.1.7.1 HTML表单

     许多应用需要模仿一个登陆HTML表单的过程，比如：为了登陆一个web应用或者提交输入的数据。HttpClient提供了UrlEncodedFormEntity类来简化这个过程。
     */

    @Test
    public void testHtmlForm(){
        List<NameValuePair> formparams = new ArrayList<NameValuePair>();
        formparams.add(new BasicNameValuePair("param1","value1"));
        formparams.add(new BasicNameValuePair("param2","value2"));
        UrlEncodedFormEntity entity = new UrlEncodedFormEntity(formparams,Consts.UTF_8);
        HttpPost httpPost = new HttpPost("url");
        httpPost.setEntity(entity);

    }
}

