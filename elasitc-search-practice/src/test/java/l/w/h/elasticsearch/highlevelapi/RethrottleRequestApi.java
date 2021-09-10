package l.w.h.elasticsearch.highlevelapi;

import org.elasticsearch.action.admin.cluster.node.tasks.list.ListTasksResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.client.RethrottleRequest;
import org.elasticsearch.tasks.TaskId;

import java.io.IOException;

/**
 * @author lwh
 * @date 2021/8/16 14:36
 * 任务限流API
 **/
public class RethrottleRequestApi {

    private RestHighLevelClient client;

    public RethrottleRequestApi(RestHighLevelClient client){
        this.client = client;
    }

    /**
     * 执行
     */
    public void execute() throws IOException {
        rethrottleResponse(create());
    }

    /**
     * 创建
     */
    private RethrottleRequest create(){
        // 完全禁用限流，即请求无限制
        // RethrottleRequest request = new RethrottleRequest(TaskId.EMPTY_TASK_ID);
        // 限流，每秒最多只可100个子请求
        return new RethrottleRequest(TaskId.EMPTY_TASK_ID,100.0f);
    }

    /**
     * 响应
     */
    private void rethrottleResponse(RethrottleRequest request) throws IOException {
        ListTasksResponse response = client.reindexRethrottle(request, RequestOptions.DEFAULT);
        response.getPerNodeTasks().forEach((nodeId,taskList) -> System.out.println(nodeId + " ：" + taskList.toString()));
    }

}
