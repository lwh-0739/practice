package l.w.h.elasticsearch.highlevelapi;

import l.w.h.elasticsearch.ElasticSearchHighLevelTest;
import lombok.extern.slf4j.Slf4j;
import org.elasticsearch.action.DocWriteResponse;
import org.elasticsearch.action.bulk.BackoffPolicy;
import org.elasticsearch.action.bulk.BulkProcessor;
import org.elasticsearch.action.bulk.BulkRequest;
import org.elasticsearch.action.bulk.BulkResponse;
import org.elasticsearch.action.delete.DeleteRequest;
import org.elasticsearch.action.delete.DeleteResponse;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.action.update.UpdateRequest;
import org.elasticsearch.action.update.UpdateResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.Requests;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.common.unit.ByteSizeUnit;
import org.elasticsearch.common.unit.ByteSizeValue;
import org.elasticsearch.common.unit.TimeValue;
import org.elasticsearch.common.xcontent.XContentType;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

/**
 * 批量处理
 * 仅支持JSON、SMILE格式的文档
 * @author lwh
 * @date 2021/8/12 14:23
 **/
@Slf4j
public class BulkApi {

    private RestHighLevelClient client;

    public BulkApi(RestHighLevelClient client){
        this.client = client;
    }

    /**
     * 创建批量处理请求（至少存在一个请求，多个请求时，类型可相同，也可不同）
     * 支持：一个顺序的{@link IndexRequest}s, {@link DeleteRequest}s and {@link UpdateRequest}s
     * @return BulkRequest
     */
    public BulkRequest createBulkRequest(){
        // BulkRequest request = new BulkRequest();
        // 可设置全局索引（仅在创建时），子请求可自定义进行覆盖
        BulkRequest request = new BulkRequest(ElasticSearchHighLevelTest.INDEX_TEST_NAME);
        request.add(
                Requests.indexRequest(
                        ElasticSearchHighLevelTest.INDEX_TEST_NAME)
                        .id("8")
                        .source("name","王八","age",28,"sex","男")
        );
        request.add(
                new UpdateRequest(
                        ElasticSearchHighLevelTest.INDEX_TEST_NAME,"2")
                        .doc("age",29)
        );
        request.add(
                Requests.deleteRequest(ElasticSearchHighLevelTest.INDEX_TEST_NAME).id("1")
        );
        return request;
    }

    /**
     * 执行
     */
    public void execute() throws IOException {
        bulkResponse(optional(createBulkRequest()));
    }

    /**
     * 可选参数（*）
     * 参考：
     *      {@link GetApi#optionalArguments()}
     *      {@link IndexApi#optionalArguments()}
     *      {@link UpdateApi#optionalArgument()}
     */
    public BulkRequest optional(BulkRequest request) throws IOException {
        // 设置指定管道
        // Pipeline需提前创建，并设置相关的处理程序
        // 例：PUT _ingest/pipeline/pipeline_test
        // {
        //   "description" : "describe pipeline",
        //   "processors" : [
        //     {
        //       "set" : {
        //         "field": "name",
        //         "value": "-pipeline-test"
        //       }
        //     }
        //   ]
        // }
        // 必须元素：
        //      description：描述
        //      processors：处理程序数组
        // 注意事项：see pipeline方法
        request.pipeline("pipeline_test");
        bulkResponse(request);
        return request;
    }

    /**
     * 批量响应
     */
    public void bulkResponse(BulkRequest request) throws IOException {
        BulkResponse responses = client.bulk(request, RequestOptions.DEFAULT);
        // 遍历子任务结果
        responses.forEach(itemResponse -> {
            DocWriteResponse response = itemResponse.getResponse();
            switch (itemResponse.getOpType()) {
                case INDEX:
                case CREATE:
                    IndexResponse indexResponse = (IndexResponse) response;
                    System.out.println(indexResponse.getResult());
                    break;
                case UPDATE:
                    UpdateResponse updateResponse = (UpdateResponse) response;
                    System.out.println(updateResponse.getResult());
                    break;
                case DELETE:
                    DeleteResponse deleteResponse = (DeleteResponse) response;
                    System.out.println(deleteResponse.getResult());
            }
        });
        // 快速判断子任务执行是否存在异常
        if (responses.hasFailures()) {
            // 遍历结果，检索响应的错误
            responses.forEach(itemResponse -> {
                if (itemResponse.isFailed()){
                    System.out.println(itemResponse.getFailure());
                }
            });
        }
    }

    /**
     * 通过提供一个功能性的类，简化Bulk Api的用法
     */
    public void bulkProcessor() throws InterruptedException {
        // 处理批量请求的监听器（批量请求以调用flush()方式分隔；或子请求数量达到预先设置好的数量，自动执行flush()方法），监听器以批量请求为单位进行处理，而非其内的子请求
        // 例：add request1,request2  -> 调用flush() -> add request3,request4
        // 此时：第一次批量请求：执行request1,request2；第二次批量请求：执行request3,request4
        BulkProcessor.Listener listener = new BulkProcessor.Listener() {
            @Override
            public void beforeBulk(long executionId, BulkRequest request) {
                // 输出批量请求次数以及该批量请求的操作数量
                log.debug("Executing bulk [{}] with {} requests", executionId, request.numberOfActions());
            }
            @Override
            public void afterBulk(long executionId, BulkRequest request,
                                  BulkResponse response) {
                if (response.hasFailures()) {
                    log.warn("Bulk [{}] executed with failures", executionId);
                } else {
                    log.debug("Bulk [{}] completed in {} milliseconds",
                            executionId, response.getTook().getMillis());
                }
            }
            @Override
            public void afterBulk(long executionId, BulkRequest request,
                                  Throwable failure) {
                log.error("Failed to execute bulk", failure);
            }
        };
        // 创建BulkProcessor（功能类，用以简化Bulk API的用法）
        BulkProcessor.Builder processorBuilder = BulkProcessor.builder(
                // 完成批量请求的操作
                (request, bulkListener) ->
                        client.bulkAsync(request, RequestOptions.DEFAULT, bulkListener)
                // 自定义的监听器
                ,listener
                // 处理器名称，用以鉴别调度线程，可为任意值
                , "bulk-processor-name");
        // 子请求数量大致设置值后自动刷新；默认：1000，可设置-1，禁用，即需要手动刷新
        processorBuilder.setBulkActions(500);
        // 子请求大小达到设置值后自动刷新；默认：5mb，可设置-1，禁用，即需要手动刷新
        processorBuilder.setBulkSize(new ByteSizeValue(1L, ByteSizeUnit.MB));
        // 设置允许指定的并发请求数；0：只允许执行单个请求；1：当累计新的批量请求时，允许执行一个并发请求；默认：1
        processorBuilder.setConcurrentRequests(0);
        // 设置每隔多少时间进行刷新；默认：未设置；设置该值时，可将bulkActions、bulkSize禁用（非强制）
        processorBuilder.setFlushInterval(TimeValue.timeValueSeconds(10L));
        // 设置bulk processor的重试相关配置
        // 设置一个常量后退策略，最初等待1秒，最多重试3次。
        // 更多选项请参阅BackoffPolicy.noBackoff()、BackoffPolicy.constantBackoff()和BackoffPolicy.exponentialBackoff()。
        processorBuilder.setBackoffPolicy(BackoffPolicy
                .constantBackoff(TimeValue.timeValueSeconds(1L), 3));
        BulkProcessor processor = processorBuilder.build();
        IndexRequest one = new IndexRequest("bulk_test").id("1")
                .source(XContentType.JSON, "title",
                        "In which order are my Elasticsearch queries executed?");
        IndexRequest two = new IndexRequest("bulk_test").id("2")
                .source(XContentType.JSON, "title",
                        "Current status and upcoming changes in Elasticsearch");
        IndexRequest three = new IndexRequest("bulk_test").id("3")
                .source(XContentType.JSON, "title",
                        "The Future of Federated Search in Elasticsearch");
        processor.add(one);
        processor.add(two);
        processor.add(three);
        // 手动刷新
        processor.flush();
        processor.add(one);
        processor.add(two);
        processor.add(three);
        // 关闭；
        //      立即关闭：close()；
        //      等待指定时间关闭：awaitClose()
        //          若在等待时间内，完成所有批量操作，则返回true，否则返回false
        processor.awaitClose(1, TimeUnit.MINUTES);
        // processor.close();
    }

}
