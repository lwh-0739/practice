package l.w.h.hutool;

import org.ansj.domain.Result;
import org.ansj.domain.Term;
import org.ansj.library.StopLibrary;
import org.ansj.splitWord.analysis.ToAnalysis;

import java.io.IOException;
import java.util.Iterator;

/**
 * @author lwh
 * @date 2021/4/22 15:47
 **/
public class Tokenizer {

    public static void main(String[] args) throws IOException {


        //自动根据用户引入的分词库的jar来自动选择使用的引擎
        // TokenizerEngine engine = TokenizerUtil.createEngine();
        // String text = "《奔跑吧兄弟第二季》20150619 钟汉良湿身露囧态 井柏然李晨超强对决";
        // Result result = engine.parse(text);
        // String resultStr = CollUtil.join(result, ",",null,null);
        // System.out.println(resultStr);

        // StopRecognition stopRecognition = new StopRecognition();
        // stopRecognition.insertStopWords(" ","《","》");
        // Result parse = ToAnalysis.parse("《奔跑吧兄弟第二季》20150619 钟汉良湿身露囧态 井柏然李晨超强对决").recognition(stopRecognition);

        // MyStaticValue.putLibrary(StopLibrary.DEFAULT, "library/stop.dic");
        // Result result = ToAnalysis.parse("《奔跑吧兄弟第二季》20150619 钟汉良湿身露囧态 井柏然李晨超强对决").recognition(StopLibrary.get());
        Result result = ToAnalysis.parse("-rising star----------timber-----mv").recognition(StopLibrary.get()).recognition(new RecognitionImpl());
        // Result result = ToAnalysis.parse("《奔跑吧兄弟第二季》20150619 钟汉良湿身露囧态 井柏然李晨超强对决").recognition(StopLibrary.get()).recognition(new RecognitionImpl());
        System.out.println(result);
        System.out.println(result.toStringWithOutNature(","));



        // HanLP.Config.CoreStopWordDictionaryPath = "D:\\code\\practice\\hutool-practice\\src\\main\\resources\\data\\dictionary\\test.txt";
        // List<Term> segment = HanLP.segment("《奔跑吧兄弟第二季》20150619 钟汉良湿身露囧态 井柏然李晨超强对决");
        // System.out.println(segment);
        // CoreStopWordDictionary.load("D:\\code\\practice\\hutool-practice\\src\\main\\resources\\data\\dictionary\\test.txt",true);
        // CoreStopWordDictionary.reload();
        // CoreStopWordDictionary.apply(segment);
        // System.out.println(segment);
    }

    private static String resultToString(Result result){
        StringBuilder stringBuilder = new StringBuilder();
        Iterator<Term> iterator = result.iterator();
        Term term;
        while (iterator.hasNext()){
            term = iterator.next();
            stringBuilder.append(term).append(",");
        }
        if (stringBuilder.length() != 0){
            stringBuilder.deleteCharAt(stringBuilder.length() - 1);
        }
        return stringBuilder.toString();
    }

}
