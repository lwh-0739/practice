package l.w.h.elasticsearch.highlevelapi;

import com.alibaba.fastjson.JSON;
import l.w.h.elasticsearch.ElasticSearchHighLevelTest;
import org.elasticsearch.ElasticsearchException;
import org.elasticsearch.action.DocWriteRequest;
import org.elasticsearch.action.DocWriteResponse;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.action.support.WriteRequest;
import org.elasticsearch.action.support.replication.ReplicationResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.Requests;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.common.unit.TimeValue;
import org.elasticsearch.common.xcontent.XContentBuilder;
import org.elasticsearch.common.xcontent.XContentFactory;
import org.elasticsearch.common.xcontent.XContentType;
import org.elasticsearch.index.VersionType;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * @author lwh
 * @date 2021/8/10 16:23
 **/
public class IndexApi {

    private RestHighLevelClient restHighLevelClient;

    public IndexApi(RestHighLevelClient restHighLevelClient){
        this.restHighLevelClient = restHighLevelClient;
    }

    /**
     * JSON字符串的方式添加文档
     */
    public void addDocumentByJsonString() throws IOException {
        String jsonString = JSON.toJSONString(
                ElasticSearchHighLevelTest.People
                        .builder()
                        .name("张三")
                        .age(15)
                        .sex("男")
                        .build()
        );
        IndexRequest request = new IndexRequest(ElasticSearchHighLevelTest.INDEX_TEST_NAME);
        request.id("1").source(jsonString, XContentType.JSON);
        IndexResponse response = restHighLevelClient.index(
                request,
                RequestOptions.DEFAULT
        );
        System.out.println(response.getResult());
    }

    /**
     * Map的方式添加文档
     */
    public void addDocumentByMap() throws IOException {
        Map<String,Object> map = new HashMap<>(4);
        map.put("name","王五");
        map.put("age",18);
        map.put("sex","男");
        IndexRequest request = new IndexRequest(ElasticSearchHighLevelTest.INDEX_TEST_NAME);
        request.id("2").source(map);
        IndexResponse response = restHighLevelClient.index(request, RequestOptions.DEFAULT);
        System.out.println(response.getResult());
    }

    /**
     * XContentBuilder的方式添加文档
     */
    public void addDocumentByXContentBuilder() throws IOException {
        XContentBuilder xContentBuilder = XContentFactory.jsonBuilder();
        xContentBuilder.startObject();
        xContentBuilder.field("name","李四");
        xContentBuilder.field("age",20);
        xContentBuilder.field("sex","女");
        xContentBuilder.endObject();
        IndexRequest request = new IndexRequest(ElasticSearchHighLevelTest.INDEX_TEST_NAME);
        request.id("3").source(xContentBuilder);
        IndexResponse response = restHighLevelClient.index(request, RequestOptions.DEFAULT);
        System.out.println(response.getResult());
    }

    /**
     * key-pairs的方式添加文档
     */
    public void addDocumentByKeyPairs() throws IOException {
        IndexRequest request = new IndexRequest(ElasticSearchHighLevelTest.INDEX_TEST_NAME);
        request.id("4").source("name","赵六","age",23,"sex","女");
        IndexResponse response = restHighLevelClient.index(request, RequestOptions.DEFAULT);
        System.out.println(response.getResult());
    }

    /**
     * 可选参数设置
     */
    public void optionalArguments(){
        IndexRequest request = Requests.indexRequest(ElasticSearchHighLevelTest.INDEX_TEST_NAME);
        request.routing("routing");
        // 超时
        request.timeout("1s");
        request.timeout(TimeValue.timeValueSeconds(1));
        //刷新策略 默认：WriteRequest.RefreshPolicy.NONE，不刷新，详情查看：RefreshPolicy类
        request.setRefreshPolicy(WriteRequest.RefreshPolicy.WAIT_UNTIL);
        request.setRefreshPolicy("wait_for");
        // 版本
        request.version(2);
        request.versionType(VersionType.EXTERNAL);
        // 操作类型
        request.opType(DocWriteRequest.OpType.CREATE);
        request.opType("create");
        // 设置在索引文档之前要执行的摄取管道
        request.setPipeline("pipeline");
    }

    /**
     * 客户端执行请求
     */
    private void indexExecution(){
        //同步执行
        // restHighLevelClient.index();
        //异步执行
        // restHighLevelClient.indexAsync(null, RequestOptions.DEFAULT, new ActionListener<IndexResponse>() {
        //     @Override
        //     public void onResponse(IndexResponse indexResponse) {
        //
        //     }
        //
        //     @Override
        //     public void onFailure(Exception e) {
        //
        //     }
        // });
    }

    /**
     * indexResponse 对象
     */
    private void indexResponse() {
        IndexRequest request = new IndexRequest(ElasticSearchHighLevelTest.INDEX_TEST_NAME);
        request.id("4").source("name","赵六","age",23,"sex","女");
        IndexResponse response = null;
        try {
            response = restHighLevelClient.index(request, RequestOptions.DEFAULT);
            DocWriteResponse.Result result = response.getResult();
            ReplicationResponse.ShardInfo shardInfo = response.getShardInfo();
            if (shardInfo.getFailed() > 0){
                for (ReplicationResponse.ShardInfo.Failure failure : shardInfo.getFailures()) {
                    System.out.println(failure.reason());
                }
            }
        } catch (ElasticsearchException | IOException e) {
            e.printStackTrace();
        }
    }

}
