package l.w.h.elasticsearch;

import l.w.h.elasticsearch.highlevelapi.GetApi;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.apache.commons.codec.binary.Base64;
import org.apache.http.Header;
import org.apache.http.HttpHost;
import org.apache.http.message.BasicHeader;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.client.indices.CreateIndexRequest;
import org.elasticsearch.client.indices.CreateIndexResponse;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

/**
 * @author lwh
 * @date 2021/8/10 14:58
 **/
public class ElasticSearchHighLevelTest {

    public static final String INDEX_TEST_NAME = "elastic-search-client-test";

    @Test
    void elasticSearchTest(){
        RestHighLevelClient client = null;
        try {
            client = createClient();
            // IndexApi indexApi = new IndexApi(client);
            GetApi api = new GetApi(client);
            api.getResponse();
            // UpdateApi updateApi = new UpdateApi(client);
            // TermVectorsApi termVectorsApi = new TermVectorsApi(client);
            // BulkApi bulkApi = new BulkApi(client);
            // MultiGetApi multiGetApi = new MultiGetApi(client);
            // ReIndexApi reIndexApi = new ReIndexApi(client);
            // UpdateByQueryApi updateByQueryApi = new UpdateByQueryApi(client);
            // DeleteByQueryApi deleteByQueryApi = new DeleteByQueryApi(client);
            // MultiTermVectorsApi multiTermVectorsApi = new MultiTermVectorsApi(client);
            // multiTermVectorsApi.execute();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (null != client){
                closeClient(client);
            }
        }
    }

    /**
     * 创建索引
     */
    private void createIndex(RestHighLevelClient client) throws IOException {
        CreateIndexResponse response = client.indices().create(new CreateIndexRequest(INDEX_TEST_NAME), RequestOptions.DEFAULT);
        if (response.isAcknowledged()) {
            System.out.println("创建索引成功！");
        }else {
            System.out.println("创建索引失败！");
        }
    }

    /**
     * 创建客户端
     */
    private RestHighLevelClient createClient(){
        return new RestHighLevelClient(
                RestClient
                        .builder(
                                new HttpHost("192.168.2.133",9200,HttpHost.DEFAULT_SCHEME_NAME)
                        )
                .setDefaultHeaders(new Header[]{
                        new BasicHeader("Authorization","Basic " + Base64.encodeBase64String("elastic:123456".getBytes(StandardCharsets.UTF_8)))
                })
        );
    }

    /**
     * 关闭客户端
     */
    private void closeClient(RestHighLevelClient restHighLevelClient){
        try {
            restHighLevelClient.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Getter
    @Setter
    @Builder
    public static class People{

        private String name;

        private Integer age;

        private String sex;

    }

}
