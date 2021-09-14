package l.w.h.elasticsearch.highlevelapi;

import l.w.h.elasticsearch.ElasticSearchHighLevelTest;
import org.elasticsearch.action.support.ActiveShardCount;
import org.elasticsearch.action.support.replication.ReplicationResponse;
import org.elasticsearch.action.update.UpdateRequest;
import org.elasticsearch.action.update.UpdateResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.index.get.GetResult;
import org.elasticsearch.script.Script;
import org.elasticsearch.script.ScriptType;

import java.io.IOException;
import java.util.Collections;
import java.util.Map;

/**
 * @author lwh
 * @date 2021/8/11 15:05
 **/
public class UpdateApi {

    private RestHighLevelClient client;

    public UpdateApi(RestHighLevelClient client){
        this.client = client;
    }

    /**
     * 更新
     */
    public void updateByScript() throws IOException {
        UpdateRequest request = new UpdateRequest(ElasticSearchHighLevelTest.INDEX_TEST_NAME,"2");
        //painless脚本更新
        Map<String,Object> parameters = Collections.singletonMap("count",4);
        // ScriptType.INLINE：即时编译脚本
        Script script = new Script(ScriptType.INLINE, Script.DEFAULT_SCRIPT_LANG, "ctx._source.age += params.count", parameters);
        // ScriptType.STORED：执行已存储的脚本
        // 执行脚本时，要先存储脚本，如下：
        // POST _scripts/increment-age
        // {
        //     "script":{
        //             "lang": "painless",
        //             "source": "ctx._source.age += params.count"
        //      }
        // }
        // Script script = new Script(ScriptType.STORED,null,"increment-age",parameters);
        request.script(script);
        UpdateResponse response = client.update(
                request
                , RequestOptions.DEFAULT
        );
        System.out.println(response.getResult());
        System.out.println(response.status());
        System.out.println(response.getShardId().toString());
        System.out.println(response.getGetResult());
    }

    /**
     * 局部更新
     */
    public void update() throws IOException {
        UpdateRequest request = new UpdateRequest(ElasticSearchHighLevelTest.INDEX_TEST_NAME, "7");
        // JSON String方式更新
        // String jsonString = JSON.toJSONString(
        //         ElasticSearchHighLevelTest
        //                 .People
        //                 .builder()
        //                 .name("王五")
        //                 .age(30)
        //                 .build()
        // );
        // request.doc(jsonString, XContentType.JSON);
        // Map方式更新
        // Map<String,Object> parameterMap = new HashMap<>(4);
        // parameterMap.put("name","王五1");
        // parameterMap.put("age",20);
        // request.doc(parameterMap);
        // XContentBuilder方式更新
        // XContentBuilder xContentBuilder = XContentFactory.jsonBuilder();
        // xContentBuilder.startObject();
        // {
        //     xContentBuilder.field("name","王五2");
        //     xContentBuilder.field("age","25");
        // }
        // xContentBuilder.endObject();
        // request.doc(xContentBuilder);
        // key-pairs方式更新
        request.doc("name","陈七","age",21);
        request.docAsUpsert(true);
        request.fetchSource(Boolean.TRUE);
        // request.doc("name","王五3","age",20);
        // request.upsert("name","王二麻子","age",23,"sex","男");
        UpdateResponse response = client.update(
                request
                , RequestOptions.DEFAULT
        );
        System.out.println(response.getResult());
        // getResult只有当设置fetchSource时，才会将已更新的文档返回（全部字段）
        GetResult getResult = response.getGetResult();
        if (getResult.isExists()){
            System.out.println(getResult.sourceAsString());
        }else {
        //    处理响应中没有文档源的情况，这是默认情况
        }
        ReplicationResponse.ShardInfo shardInfo = response.getShardInfo();
        if (shardInfo.getTotal() != shardInfo.getSuccessful()){
        //    处理成功的分片数少于总分片数的情况
        }
        if (shardInfo.getFailed() > 0){
            for (ReplicationResponse.ShardInfo.Failure failure : shardInfo.getFailures()) {
                // 处理错误
                System.out.println(failure.reason());
            }
        }
    }

    /**
     * 创建或更新
     * 若文档存在，则更新，否则，创建
     * 注：request.upsert()前要先执行request.doc()/request.script()
     * 方式与局部更新相同，共有四种，详情参照：{@link #update()}
     */
    public void upsert() throws IOException {
        UpdateRequest request = new UpdateRequest(ElasticSearchHighLevelTest.INDEX_TEST_NAME,"6");
        request.doc("name","郑六");
        request.upsert("name","郑六-upsert","age",40,"sex","男");
        UpdateResponse response = client.update(request, RequestOptions.DEFAULT);
        System.out.println(response.getResult());
    }

    /**
     * 可选参数（*）
     * 公共参数详情参考：{@link GetApi#optionalArguments()}
     */
    public void optionalArgument(){
        UpdateRequest request = new UpdateRequest(ElasticSearchHighLevelTest.INDEX_TEST_NAME, "1");
        // 重试次数：当要更新的文档在更新操作的获取与索引阶段之间被另一操作修改时，要进行重试的次数
        request.retryOnConflict(3);
        // 启用资源检索，默认：false，参照：fetchSourceContext
        request.fetchSource(true);
        // 更新时使用乐观锁，与version类似，防止并发时数据出错
        request.setIfSeqNo(23);
        request.setIfPrimaryTerm(1);
        // 检测这个更新是否是NOOP，默认：true
        request.detectNoop(false);
        // 指示不管要更新文档是否存在，脚本都会执行，若文档不存在，则进行创建；默认：false
        request.scriptedUpsert(true);
        // 如果文档不存在，则用作upsert文档使用；默认：false
        // 例：   request.doc("name","陈七","age",21,"sex","男");
        //        request.docAsUpsert(true);
        //      等价：
        //        request.doc("name","陈七");
        //        request.upsert("name","陈七","age",21,"sex","男");
        request.docAsUpsert(true);
        // 设置更新操作之前必须处于活动状态的分片副本的数量
        request.waitForActiveShards(2);
        request.waitForActiveShards(ActiveShardCount.ALL);
    }

}
