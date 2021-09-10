package l.w.h.elasticsearch.highlevelapi;

import l.w.h.elasticsearch.ElasticSearchHighLevelTest;
import org.elasticsearch.action.get.GetRequest;
import org.elasticsearch.action.get.GetResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.Requests;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.client.core.GetSourceRequest;
import org.elasticsearch.client.core.GetSourceResponse;

import java.io.IOException;

/**
 * @author lwh
 * @date 2021/8/11 9:40
 **/
public class GetApi {

    private RestHighLevelClient client;

    public GetApi(RestHighLevelClient client){
        this.client = client;
    }

    /**
     * 获取指定索引、id的文档信息
     */
    public void get() throws IOException {
        GetResponse response = client.get(
                Requests.getRequest(
                        ElasticSearchHighLevelTest.INDEX_TEST_NAME
                ).id("1")
                , RequestOptions.DEFAULT
        );
        System.out.println(response.getSourceAsString());
        System.out.println(response.getField("name"));
        System.out.println(response.getPrimaryTerm());
        System.out.println(response.getFields());
        System.out.println(response.getSeqNo());
        response.forEach(System.out::println);
    }

    /**
     * 可选参数（*）
     * 补充：{@link IndexApi#optionalArguments()}
     */
    public void optionalArguments() throws IOException {
        GetRequest request = Requests.getRequest(ElasticSearchHighLevelTest.INDEX_TEST_NAME).id("1");
        // 设置不返回命中数据，默认情况下，返回
        // request.fetchSourceContext(FetchSourceContext.DO_NOT_FETCH_SOURCE);
        // 设置返回字段
        // String[] includes = new String[]{"name","age"};
        // String[] excludes = Strings.EMPTY_ARRAY;
        // request.fetchSourceContext(
        //         new FetchSourceContext(Boolean.TRUE,includes,excludes)
        // );
        // 设置返回排除字段
        // String[] includes = Strings.EMPTY_ARRAY;
        // String[] excludes = new String[]{"name","age"};
        // request.fetchSourceContext(
        //         new FetchSourceContext(Boolean.TRUE,includes,excludes)
        // );
        // 配置返回的指定存储字段（需要在单独存储，即Mapping中字段属性"store": true，意为：除了在 _source 中存一份，也会存储在一个跟 _source 平级的独立的field中）
        // request.storedFields("name");
        // 设置分片路由：
        //      分片路由规则：shard_num = hash(_routing) % num_primary_shards
        //      _routing字段的取值，默认是_id字段，可自定义，如下：
        // request.routing("routing");
        // 设置优先选择满足条件的节点，详情查看：Preference类
        // Preference.LOCAL.toString() 等价于 Preference.LOCAL.name()
        // request.preference(Preference.LOCAL.toString());
        // request.preference(Preference.LOCAL.name());
        // 设置实时性，默认：true
        // request.realtime(false);
        // 在检索文档之前，执行刷新操作；默认：false
        // request.refresh(true);
        // 版本、版本类型：暂不知具体用途
        // request.version(2);
        // request.versionType(VersionType.EXTERNAL);
        GetResponse response = client.get(
                request
                , RequestOptions.DEFAULT
        );
        System.out.println(response.getSourceAsString());
        // System.out.println(response.getField("name"));
    }

    /**
     * 异常
     */
    private void getExecution(){
        //同步执行
        // client.index();
        //异步执行
        // client.indexAsync(null, RequestOptions.DEFAULT, new ActionListener<IndexResponse>() {
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
     * GetResponse 对象
     */
    public void getResponse() throws IOException {
        GetRequest request = Requests.getRequest(ElasticSearchHighLevelTest.INDEX_TEST_NAME).id("2");
        GetResponse response = client.get(
                request
                , RequestOptions.DEFAULT
        );
        System.out.println(response.getSourceAsString());
        response.getSourceAsMap().forEach((key,obj) -> System.out.println(key + ":" + obj));
        System.out.println(response.isExists());
        System.out.println(response.isSourceEmpty());
        System.out.println(response.isFragment());
    }

    /**
     * GetSourceRequest 请求
     */
    public void getSourceRequest() throws IOException {
        GetSourceRequest request = new GetSourceRequest(ElasticSearchHighLevelTest.INDEX_TEST_NAME, "1");
        GetSourceResponse source = client.getSource(request, RequestOptions.DEFAULT);
        source.getSource().forEach((key,obj) -> System.out.println(key + ":" + obj));
        System.out.println(source.toString());
    }

    /**
     * 判断指定id的文档是否存在
     */
    public void existRequest() throws IOException {
        System.out.println(client.exists(
                Requests.getRequest(ElasticSearchHighLevelTest.INDEX_TEST_NAME).id("1")
                , RequestOptions.DEFAULT
        ));
        System.out.println(client.existsSource(
                new GetSourceRequest(ElasticSearchHighLevelTest.INDEX_TEST_NAME, "1")
                , RequestOptions.DEFAULT
        ));
    }

}
