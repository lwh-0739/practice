package l.w.h.elasticsearch.highlevelapi;

import org.elasticsearch.action.support.IndicesOptions;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.index.query.MatchPhraseQueryBuilder;
import org.elasticsearch.index.reindex.BulkByScrollResponse;
import org.elasticsearch.index.reindex.ReindexRequest;
import org.elasticsearch.index.reindex.UpdateByQueryRequest;
import org.elasticsearch.script.Script;

import java.io.IOException;
import java.util.Collections;

/**
 * @author lwh
 * @date 2021/8/16 9:16
 **/
public class UpdateByQueryApi {

    private RestHighLevelClient client;

    public UpdateByQueryApi(RestHighLevelClient client){
        this.client = client;
    }

    /**
     * 执行
     */
    public void execute() throws IOException {
        updateByQueryResponse(create());
    }

    /**
     * 创建
     * （只能根据Script进行更新）
     */
    private UpdateByQueryRequest create(){
        UpdateByQueryRequest request = new UpdateByQueryRequest("reindex_test");
        request.setQuery(
                new MatchPhraseQueryBuilder("content","女孩")
                // new MatchQueryBuilder("content","女孩")
        );
        request.setScript(
                new Script(
                        Script.DEFAULT_SCRIPT_TYPE
                        ,Script.DEFAULT_SCRIPT_LANG
                        ,"ctx._source.content += 'script-test'"
                        , Collections.emptyMap()
                )
        );
        return request;
    }

    /**
     * 可选参数（*）
     * 参照：{@link ReIndexApi#optionalArguments(ReindexRequest)}
     */
    UpdateByQueryRequest optionalArguments(UpdateByQueryRequest request){
        // 控制如何处理不可用的索引
        /**
         * 详情
         * @see IndicesOptions
         */
        request.setIndicesOptions(IndicesOptions.LENIENT_EXPAND_OPEN);
        return request;
    }

    /**
     * 响应
     */
    private void updateByQueryResponse(UpdateByQueryRequest request) throws IOException {
        BulkByScrollResponse response = client.updateByQuery(request, RequestOptions.DEFAULT);
        System.out.println(response.getStatus().getThrottled());
        System.out.println(response.getStatus());
    }

}
