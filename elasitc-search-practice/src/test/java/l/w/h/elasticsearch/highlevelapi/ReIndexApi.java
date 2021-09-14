package l.w.h.elasticsearch.highlevelapi;

import org.elasticsearch.action.bulk.BulkRequest;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.client.tasks.TaskSubmissionResponse;
import org.elasticsearch.index.reindex.BulkByScrollResponse;
import org.elasticsearch.index.reindex.ReindexRequest;

import java.io.IOException;

/**
 * @author lwh
 * @date 2021/8/13 11:24
 **/
public class ReIndexApi {

    private RestHighLevelClient client;

    public ReIndexApi(RestHighLevelClient client){
        this.client = client;
    }

    /**
     * 执行
     */
    public void execute() throws IOException {
        reindexResponse(optionalArguments(create()));
    }

    /**
     * 创建
     */
    private ReindexRequest create(){
        ReindexRequest request = new ReindexRequest();
        request.setSourceIndices("ik_test","test");
        request.setDestIndex("reindex_test");
        return request;
    }

    /**
     * 可选参数（*）
     */
    ReindexRequest optionalArguments(ReindexRequest request){
        // 设置目标索引的版本类型；默认：VersionType.INTERNAL
        // 版本类型的区别：
        //      VersionType.INTERNAL：会将源索引中数据以id为标识重新索引至目标索引中，忽略版本冲突
        //              若目标索引中不存在指定id的文档，则进行创建；若存在，则直接将源索引的指定文档覆盖目标索引中的文档
        //      VersionType.EXTERNAL：引中数据以id为标识重新索引至目标索引中，有可能会出现版本冲突，
        //              若目标索引中不存在指定id的文档，则进行创建；若存在，则比较源索引与目标索引中指定id的文档的版本，若前者不大于后者，则会版本冲突，不会进行更新操作
        // request.setDestVersionType(VersionType.EXTERNAL);

        // 设置版本冲突时_reindex进程处理方式：
        //      abort：终止_reindex进程，记录异常
        //      proceed：不终止_reindex进程，忽略异常；类似于，for循环的continue
        // request.setConflicts("proceed");

        // 设置操作类型；可用值：index、create；默认：index
        //      index：没有则创建，否则进行更新（版本号增加）
        //      create：没有则创建，否则版本异常；且只支持VersionType.INTERNAL
        // request.setDestOpType(DocWriteRequest.OpType.CREATE.name());
        // request.setDestOpType(DocWriteRequest.OpType.CREATE.toString());

        // 设置查询源索引的查询条件，仅对查询出的文档移至目标索引
        // 第一个例子，无效，查询不到；后两个均可查询出文档
        // request.setSourceQuery(new TermQueryBuilder("content","女人对护肤品比对男票好？网友神怼"));
        // request.setSourceQuery(new TermQueryBuilder("content","女人"));
        // request.setSourceQuery(new TermQueryBuilder("content","护肤品"));

        // 设置最大出力文档数量；默认：-1，意为无限制，处理全部文档
        // request.setMaxDocs(2);

        // 设置一次批量处理的文档数
        // 当索引中文档数据很大，需要分多次处理，此时设置每次处理的文档数量
        // request.setSourceBatchSize(2);

        // 设置指定管道，可对源索引中数据进行处理后，再将其放入目标索引中
        // 注：需提前创建自定义pipeline，再在这里引用
        /** @see BulkApi#optional(BulkRequest) 中pipeline可选参数  */
        // request.setDestPipeline("pipeline-test");

        // 允许使用脚本的方式数源索引中数据（与元数据）进行处理后，再放入目标索引中，与指定pipeline功能类似
        //
        /** @see UpdateApi#updateByScript() 获取更多信息 */
        // request.setScript(
        //         new Script(
        //                 ScriptType.INLINE,"painless","ctx._source.content += '-reindex-test'", new HashMap<>(2)
        //         )
        // );

        // 支持Elasticsearch集群（新集群；新集群需配置：reindex.remote.whitelist: 旧集群IP:9200）重新索引；集群迁移时使用
        // 注：当使用远程集群使用的query时，需在RemoteInfo对象中指定，而不是设置setSourceQuery。当同时设置RemoteInfo的query与setSourceQuery时，则在请求期间导致验证错误。
        // 原因：远程的Elasticsearch可能不理解现在查询构建器构建的查询。远程集群支持可以追溯到Elasticsearch 0.90，从那时起，查询语言就发生了变化。
        // 当达到旧版本时，使用JSON手工编写查询更安全。
        // 以下实例是对单一集群进行的处理（将IP设为旧集群IP即可）
        // request.setRemoteInfo(
        //         new RemoteInfo(
        //                 HttpHost.DEFAULT_SCHEME_NAME
        //                 ,"192.168.2.133"
        //                 ,9200
        //                 ,null
        //                 ,new BytesArray(new MatchQueryBuilder("content","女人").toString())
        //                 ,"elastic"
        //                 ,"123456"
        //                 ,new HashMap<>(2)
        //                 , TimeValue.timeValueMillis(100)
        //                 ,TimeValue.timeValueSeconds(100)
        //                 )
        // );

        // 设置将任务划分的分片数；默认：1，任务不分片为子任务
        // 0相当于Rest API的"auto"切片参数
        // request.setSlices(2);

        // 设置SearchRequest的滚动超时时间
        // request.setScroll(TimeValue.timeValueMinutes(10));

        // 设置分片对每个批量请求可用的超时时间；默认：1分钟
        // request.setTimeout(TimeValue.timeValueMinutes(2));
        // 设置：在请求结束后，是否对写入的索引执行刷新操作；默认：false
        // request.setRefresh(true);

        return request;
    }

    /**
     * 提交重新索引任务
     */
    private void submitReindexTask() throws IOException {
        TaskSubmissionResponse taskSubmissionResponse = client.submitReindexTask(
                create().setRefresh(true)
                , RequestOptions.DEFAULT
        );
        // 仅返回任务标识
        System.out.println(taskSubmissionResponse.getTask());
    }

    /**
     * 响应
     */
    private void reindexResponse(ReindexRequest request) throws IOException {
        BulkByScrollResponse response = client.reindex(request, RequestOptions.DEFAULT);
        System.out.println(response.isTimedOut());
        System.out.println(response.getStatus());
        System.out.println(response.getBatches());
        System.out.println(response.getCreated());
        System.out.println(response.getUpdated());
        System.out.println(response.getDeleted());
        System.out.println(response.getVersionConflicts());
        response.getBulkFailures().forEach(failure -> {
            System.out.println(failure.getStatus());
            System.out.println(failure.getMessage());
            System.out.println(failure.getCause().getMessage());
        });
    }

}
