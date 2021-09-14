package l.w.h.elasticsearch.highlevelapi;

import l.w.h.elasticsearch.ElasticSearchHighLevelTest;
import org.elasticsearch.action.DocWriteResponse;
import org.elasticsearch.action.delete.DeleteResponse;
import org.elasticsearch.action.support.replication.ReplicationResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.Requests;
import org.elasticsearch.client.RestHighLevelClient;

import java.io.IOException;

/**
 * @author lwh
 * @date 2021/8/11 14:40
 **/
public class DeleteApi {

    private RestHighLevelClient client;

    public DeleteApi(RestHighLevelClient client){
        this.client = client;
    }

    /**
     * 删除指定索引、id的文档信息
     */
    public void delete() throws IOException {
        DeleteResponse response = client.delete(
                Requests.deleteRequest(ElasticSearchHighLevelTest.INDEX_TEST_NAME).id("1")
                , RequestOptions.DEFAULT
        );
        if (DocWriteResponse.Result.NOT_FOUND.equals(response.getResult())){
            System.out.println("文档未找到！");
        }
        System.out.println(response.getVersion());
        if (response.getShardInfo().getFailed() > 0){
            for (ReplicationResponse.ShardInfo.Failure failure : response.getShardInfo().getFailures()) {
                System.out.println(failure.reason());
            }
        }
    }

    /**
     * 可选参数（*）
     * 详情参考：{@link GetApi#optionalArguments()}
     */
    public void optionalArguments() {

    }

}
