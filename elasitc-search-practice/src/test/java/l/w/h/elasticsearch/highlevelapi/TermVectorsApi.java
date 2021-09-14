package l.w.h.elasticsearch.highlevelapi;

import l.w.h.elasticsearch.ElasticSearchHighLevelTest;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.client.core.TermVectorsRequest;
import org.elasticsearch.client.core.TermVectorsResponse;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * 词条API
 * @author lwh
 * @date 2021/8/12 10:53
 **/
public class TermVectorsApi {

    private RestHighLevelClient client;

    public TermVectorsApi(RestHighLevelClient client){
        this.client = client;
    }

    /**
     * 执行
     */
    public void execute() throws IOException {
        termVectorRequest(createTermVectorsRequest());
    }

    /**
     * 创建词条请求
     * @return TermVectorsRequest
     */
    private TermVectorsRequest createTermVectorsRequest(){
        // 指定文档（已存在）、指定字段的词条
        TermVectorsRequest request = new TermVectorsRequest(ElasticSearchHighLevelTest.INDEX_TEST_NAME, "7");
        request.setFields("name");
        // 只指定索引，不指定文档，对指定字段（字段需存在）传入的自定义值进行词条统计
        // XContentBuilder xContentBuilder = XContentFactory.jsonBuilder().startObject().field("name", "陈阿斯").endObject();
        // TermVectorsRequest request = new TermVectorsRequest(ElasticSearchHighLevelTest.INDEX_TEST_NAME, xContentBuilder);
        return request;
    }

    /**
     * 可选参数（*）
     */
    public void optionalArgument() throws IOException {
        TermVectorsRequest request = createTermVectorsRequest();
        // 字段统计信息；默认：true
        // request.setFieldStatistics(false);
        // 显示词条统计信息
        // request.setTermStatistics(true);
        // 词条分词位置
        // request.setPositions(false);
        // 词条分词偏移量
        // request.setOffsets(false);
        // 分词负载
        // request.setPayloads(false);

        // 基于tf-idf分数返回
        // Map<String, Integer> filterSettings = new HashMap<>();
        // filterSettings.put("max_num_terms", 3);
        // filterSettings.put("min_term_freq", 1);
        // filterSettings.put("max_term_freq", 10);
        // filterSettings.put("min_doc_freq", 1);
        // filterSettings.put("max_doc_freq", 100);
        // filterSettings.put("min_word_length", 1);
        // filterSettings.put("max_word_length", 10);
        // request.setFilterSettings(filterSettings);

        // 为每个字段指定分析器，而不使用字段本来的分析器
        // 以下实例：使用keyword分析器代替name字段本身的分析器
        // 结果影响：
        //      字段本省分析器：会对name字段的值进行分析，每个分词作为词条，如：陈七，分析之后会被拆为：陈、七
        //      keyword分析器：将name的值作为一个词条，如：陈七，分析之后还是：陈七
        Map<String, String> perFieldAnalyzer = new HashMap<>();
        perFieldAnalyzer.put("name", "keyword");
        request.setPerFieldAnalyzer(perFieldAnalyzer);

        termVectorRequest(request);
    }

    /**
     * 词条响应
     */
    private void termVectorRequest(TermVectorsRequest request) throws IOException {
        TermVectorsResponse response = client.termvectors(request, RequestOptions.DEFAULT);
        if (!response.getTermVectorsList().isEmpty()){
            // 词条信息
            response.getTermVectorsList().forEach(termVector -> {
                // 字段名称
                System.out.println(termVector.getFieldName());
                // 字段统计信息
                TermVectorsResponse.TermVector.FieldStatistics fieldStatistics = termVector.getFieldStatistics();
                // 字段文档数
                System.out.println(fieldStatistics.getDocCount());
                // 总词频
                System.out.println(fieldStatistics.getSumTotalTermFreq());
                // 逆文档频率
                System.out.println(fieldStatistics.getSumDocFreq());
                // 当前字段Terms
                termVector.getTerms().forEach(term -> {
                    // 词条名称
                    System.out.println(term.getTerm());
                    // 词频
                    System.out.println(term.getTermFreq());
                    // 逆文档频率
                    System.out.println(term.getDocFreq());
                    // 总词频
                    System.out.println(term.getTotalTermFreq());
                    // 得分
                    System.out.println(term.getScore());
                    // 词条分词
                    term.getTokens().forEach(token -> {
                        // 分词位置
                        System.out.println(token.getPosition());
                        // 分词开始偏移量
                        System.out.println(token.getStartOffset());
                        // 分词结束偏移量
                        System.out.println(token.getEndOffset());
                        // 分词负载
                        System.out.println(token.getPayload());
                    });
                });
            });
        }
    }

}
