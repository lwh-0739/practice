package l.w.h.elasticsearch;

import org.apache.commons.codec.binary.Base64;
import org.apache.http.Header;
import org.apache.http.HttpHost;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.impl.client.BasicCredentialsProvider;
import org.apache.http.impl.nio.reactor.IOReactorConfig;
import org.apache.http.message.BasicHeader;
import org.elasticsearch.client.Node;
import org.elasticsearch.client.Request;
import org.elasticsearch.client.Response;
import org.elasticsearch.client.RestClient;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpMethod;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.List;

/**
 * @author lwh
 * @date 2021/8/10 10:22
 **/
class ElasticSearchLowLevelTest {

    @Test
    void elasticSearchTest(){
        try {
            lowLevelClientCreateAndCloseAndAuth();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 创建restClient，并进行验证
     */
    private void lowLevelClientCreateAndCloseAndAuth() throws IOException {
        String auth = Base64.encodeBase64String("elastic:123456".getBytes(StandardCharsets.UTF_8));
        BasicCredentialsProvider basicCredentialsProvider = new BasicCredentialsProvider();
        basicCredentialsProvider.setCredentials(AuthScope.ANY,new UsernamePasswordCredentials("elastic","123456"));
        RestClient restClient = RestClient.builder(
                new HttpHost("192.168.2.133", 9200, HttpHost.DEFAULT_SCHEME_NAME)
        ).setFailureListener(new RestClient.FailureListener(){
            @Override
            public void onFailure(Node node) {
                super.onFailure(node);
                System.out.println("连接ElasticSearch服务器失败！");
            }
        })
                //设置异步客户端的线程数
                .setHttpClientConfigCallback(
                        httpClientBuilder ->
                                httpClientBuilder
                                        .setDefaultIOReactorConfig(
                                                IOReactorConfig.custom()
                                                        .setIoThreadCount(1)
                                                        .build()
                                        )
                        //基本认证
                        // .setDefaultCredentialsProvider(
                        //         basicCredentialsProvider
                        // )
                )
                //设置超时时间
                .setRequestConfigCallback(requestConfigBuilder -> requestConfigBuilder
                        .setConnectTimeout(5000)
                        .setSocketTimeout(60000))
                //基本认证
                .setDefaultHeaders(new Header[]{new BasicHeader("Authorization","Basic " + auth)})
                .build();
        Request request = new Request(HttpMethod.GET.toString(), "/");
        // 设置超时时间
        // request.setOptions(
        //         RequestOptions.DEFAULT.toBuilder().setRequestConfig(
        //                 RequestConfig
        //                         .custom()
        //                         .setConnectTimeout(5000)
        //                         .setSocketTimeout(60000)
        //                         .build()
        //         )
        // );


        Response response = restClient.performRequest(request);
        response.getEntity().writeTo(System.out);
        System.out.println(response.getEntity().getContent());
        System.out.println(response.getEntity().getContentEncoding());
        System.out.println(response.getStatusLine());
        System.out.println(response.getRequestLine());
        List<Node> nodes = restClient.getNodes();
        nodes.forEach(System.out::println);
        restClient.close();
    }



}
