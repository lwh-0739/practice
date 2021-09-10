package l.w.h.elasticsearch.highlevelapi;

import l.w.h.elasticsearch.ElasticSearchHighLevelTest;
import org.elasticsearch.ElasticsearchException;
import org.elasticsearch.action.get.GetResponse;
import org.elasticsearch.action.get.MultiGetItemResponse;
import org.elasticsearch.action.get.MultiGetRequest;
import org.elasticsearch.action.get.MultiGetResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.cluster.routing.Preference;
import org.elasticsearch.common.Strings;
import org.elasticsearch.search.fetch.subphase.FetchSourceContext;

import java.io.IOException;

/**
 * @author lwh
 * @date 2021/8/13 9:22
 **/
public class MultiGetApi {

    private RestHighLevelClient client;

    public MultiGetApi(RestHighLevelClient client){
        this.client = client;
    }

    /**
     * 执行
     */
    public void execute() throws IOException {
        multiResponse(optionalArgument());
    }

    /**
     * 创建MultiGetRequest
     */
    private MultiGetRequest create() throws IOException {
        MultiGetRequest request = new MultiGetRequest();
        return request.add(new MultiGetRequest.Item(ElasticSearchHighLevelTest.INDEX_TEST_NAME,"2"))
                .add(new MultiGetRequest.Item(ElasticSearchHighLevelTest.INDEX_TEST_NAME,"3"));
    }

    /**
     * 可选参数（*）：支持与GetRequest相同的可选参数，区别是：要设置在请求子项中
     * {@link GetApi#optionalArguments()}
     */
    private MultiGetRequest optionalArgument() {
        String[] includes = new String[]{"name","age"};
        String[] excludes = Strings.EMPTY_ARRAY;
        FetchSourceContext fetchSourceContext = new FetchSourceContext(true, includes, excludes);
        MultiGetRequest request = new MultiGetRequest();
        request.add(
                new MultiGetRequest
                        .Item(ElasticSearchHighLevelTest.INDEX_TEST_NAME,"2")
                // .fetchSourceContext(FetchSourceContext.DO_NOT_FETCH_SOURCE)
                .fetchSourceContext(fetchSourceContext)
                        // 获取指定存储字段，see GetApi#optionalArguments
                        .storedFields("name")
                // 分片路由，默认：_id
                // .routing("1")
                // 版本控制
                .version(2)
                // .versionType(VersionType.EXTERNAL)
        )
        .add(
                new MultiGetRequest
                        .Item(ElasticSearchHighLevelTest.INDEX_TEST_NAME,"3")
                // .fetchSourceContext(FetchSourceContext.DO_NOT_FETCH_SOURCE)
                .fetchSourceContext(fetchSourceContext)
                        // .storedFields("name")
        );
        // 偏好设置
        request.preference(Preference.PREFER_NODES.name());
        // 设置实时性，默认：true
        request.realtime(false);
        // 在检索文档之前，执行刷新操作；默认：false
        request.refresh(true);
        return request;
    }

    /**
     * 响应对象
     */
    private void multiResponse(MultiGetRequest request) throws IOException {
        MultiGetResponse responses = client.mget(request, RequestOptions.DEFAULT);
        for (MultiGetItemResponse response : responses.getResponses()) {
            if (response.isFailed()) {
                ElasticsearchException failure = (ElasticsearchException) response.getFailure().getFailure();
                System.out.println(failure.getMessage());
            }else {
                GetResponse getResponse = response.getResponse();
                System.out.println(getResponse.isExists());
                System.out.println(getResponse.getSourceAsString());
            }
        }
    }

}
