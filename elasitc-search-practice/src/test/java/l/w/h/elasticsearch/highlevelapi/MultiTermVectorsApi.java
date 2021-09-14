package l.w.h.elasticsearch.highlevelapi;

import l.w.h.elasticsearch.ElasticSearchHighLevelTest;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.client.core.MultiTermVectorsRequest;
import org.elasticsearch.client.core.MultiTermVectorsResponse;
import org.elasticsearch.client.core.TermVectorsRequest;
import org.elasticsearch.client.core.TermVectorsResponse;

import java.io.IOException;
import java.util.List;

/**
 * @author lwh
 * @date 2021/8/16 15:32
 **/
public class MultiTermVectorsApi {

    private RestHighLevelClient client;

    public MultiTermVectorsApi(RestHighLevelClient client){
        this.client = client;
    }

    /**
     * 执行
     */
    public void execute() throws IOException {
        multiTermVectorsResponse(create());
    }

    /**
     * 创建
     */
    private MultiTermVectorsRequest create(){
        // 方式一：先创建空的MultiTermVectorsRequest，然后，逐一添加TermVectorsRequest
        // MultiTermVectorsRequest request = new MultiTermVectorsRequest();
        // TermVectorsRequest termVectorsRequest1 = new TermVectorsRequest(ElasticSearchHighLevelTest.INDEX_TEST_NAME,"4");
        // termVectorsRequest1.setFields("name");
        // request.add(termVectorsRequest1);
        // TermVectorsRequest termVectorsRequest2 = new TermVectorsRequest(ElasticSearchHighLevelTest.INDEX_TEST_NAME,"5");
        // termVectorsRequest2.setFields("sex");
        // request.add(termVectorsRequest2);
        // return request;

        // 方式二：根据模板创建；仅当请求共享相同的索引、类型，以及其他相同的设置时使用
        // "test"：可为任意值，具体值取自ids
        TermVectorsRequest termVectorsRequest = new TermVectorsRequest(ElasticSearchHighLevelTest.INDEX_TEST_NAME,"test");
        termVectorsRequest.setFields("name");
        String[] ids = new String[]{"2","3"};
        return new MultiTermVectorsRequest(ids,termVectorsRequest);
    }

    /**
     * 响应
     */
    private void multiTermVectorsResponse(MultiTermVectorsRequest request) throws IOException {
        MultiTermVectorsResponse response = client.mtermvectors(request, RequestOptions.DEFAULT);
        List<TermVectorsResponse> termVectorsResponses;
        if (null != (termVectorsResponses = response.getTermVectorsResponses()) && !termVectorsResponses.isEmpty()){
            termVectorsResponses.forEach(termVectorsResponse -> {
                termVectorsResponse.getTermVectorsList().forEach(termVector -> {
                    TermVectorsResponse.TermVector.FieldStatistics fieldStatistics = termVector.getFieldStatistics();
                    System.out.println(fieldStatistics.getDocCount());
                });
            });
        }
    }

}
