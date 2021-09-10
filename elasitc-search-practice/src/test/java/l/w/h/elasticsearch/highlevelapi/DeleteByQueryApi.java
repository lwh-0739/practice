package l.w.h.elasticsearch.highlevelapi;

import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.index.query.MatchQueryBuilder;
import org.elasticsearch.index.reindex.BulkByScrollResponse;
import org.elasticsearch.index.reindex.DeleteByQueryRequest;
import org.elasticsearch.index.reindex.UpdateByQueryRequest;

import java.io.IOException;

/**
 * @author lwh
 * @date 2021/8/16 13:14
 * 详情：{@link UpdateByQueryApi}
 **/
public class DeleteByQueryApi {

    private RestHighLevelClient client;

    public DeleteByQueryApi(RestHighLevelClient client){
        this.client = client;
    }

    /**
     * 执行
     */
    public void execute() throws IOException {
        deleteByQueryRequest(create());
    }

    /**
     * 创建
     */
    private DeleteByQueryRequest create(){
        DeleteByQueryRequest request = new DeleteByQueryRequest("test");
        return request
                .setQuery(
                        new MatchQueryBuilder("event.original","2099")
                );
    }

    /**
     * 可选参数
     * @see UpdateByQueryApi#optionalArguments(UpdateByQueryRequest)
     */
    private DeleteByQueryRequest optionalArguments(DeleteByQueryRequest request){
        return request;
    }

    /**
     * 响应
     */
    private void deleteByQueryRequest(DeleteByQueryRequest request) throws IOException {
        BulkByScrollResponse response = client.deleteByQuery(request, RequestOptions.DEFAULT);
        System.out.println(response.getStatus().getDeleted());
    }

}
