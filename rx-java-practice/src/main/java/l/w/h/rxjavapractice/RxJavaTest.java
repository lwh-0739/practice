package l.w.h.rxjavapractice;

import com.alibaba.fastjson.JSON;
import io.reactivex.Flowable;
import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import l.w.h.rxjavapractice.help.SourceScore;
import l.w.h.rxjavapractice.help.Student;
import org.junit.Test;

import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author lwh
 * @since 2022/1/26 16:10
 **/
public class RxJavaTest {

    private AtomicInteger atomicInteger = new AtomicInteger(0);

    private List<Student> studentList;

    private Random random = new Random();

    @Test
    public void test(){
        // createTest();
        // fromIterableTest();
        // countTotalScore();
        // countStudentAvg();
        // findMaxScoreEachStudent();
        // flowableTest();
    }

    /**
     * 支持背压
     */
    private void flowableTest(){
        Disposable disposable = Flowable.just("test")
                .map(s -> "map:  " + s)
                .subscribe(System.out::println);
        System.out.println(JSON.toJSONString(disposable));
    }

    private void findMaxScoreEachStudent(){
        initData();
        final boolean[] flag = {false};
        Observable.fromIterable(studentList)
                .toMap(student -> student.getClassUid() + "-" + student.getName(), student -> {
                    AtomicInteger max = new AtomicInteger();
                    Observable.fromIterable(student.getSourceScoreList())
                            .map(SourceScore::getScore)
                            .sorted(Comparator.comparingInt(i -> -i))
                            .subscribe(i -> {
                                boolean innerFlag = flag[0];
                                if (!innerFlag) {
                                    flag[0] = true;
                                    max.set(i);
                                    // System.out.println(i);
                                }
                            });
                    flag[0] = false;
                    return max.get();
                })
                .toObservable()
                .map(Map::entrySet)
                .flatMap(Observable::fromIterable)
                .sorted(Comparator.comparing(Map.Entry::getKey))
                .subscribe(System.out::println);
    }

    private void countStudentAvg(){
        initData();
        Disposable subscribe = Observable.fromIterable(studentList)
                .toMap(student -> student.getClassUid() + "-" + student.getName(), student -> {
                    int i = Observable.fromIterable(student.getSourceScoreList())
                            .map(SourceScore::getScore)
                            .reduce(Integer::sum).blockingGet();
                    // System.out.println(i);
                    return i / student.getSourceScoreList().size();
                })
                .subscribe(System.out::println);
        System.out.println(JSON.toJSONString(subscribe));
    }

    private void countTotalScore(){
        initData();
        Integer integer = Observable.fromIterable(studentList)
                .map(Student::getSourceScoreList)
                .flatMap(Observable::fromIterable)
                .map(SourceScore::getScore)
                .reduce(Integer::sum)
                .blockingGet();
        // .subscribe(System.out::println);
        System.out.println(integer);
    }

    private void fromIterableTest(){
        initData();
        Disposable subscribe = Observable.fromIterable(studentList)
                .subscribeOn(Schedulers.newThread())
                .subscribe(o -> System.out.println(JSON.toJSONString(o)));
        try {
            Thread.sleep(3000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println(JSON.toJSONString(subscribe));
    }

    private void createTest(){
        Observable.<String>create(emitter -> {
            emitter.onNext("test1");
            emitter.onNext("test2");
            emitter.onNext("test3");
            // emitter.onError(new Throwable("测试"));
            emitter.onComplete();
        })
                // .subscribeOn(Schedulers.newThread())
                .flatMap(str -> {
                    System.out.println("floatMap1：" + str + " " + Thread.currentThread());
                    return Observable.just(str);
                })
                // .observeOn(Schedulers.newThread())
                .flatMap(str -> {
                    System.out.println("floatMap2：" + str + " " + Thread.currentThread());
                    return Observable.just("floatMap2：" + str);
                })
                // .observeOn(Schedulers.newThread())
                .flatMap(str -> {
                    System.out.println("floatMap3：" + str + " " + Thread.currentThread());
                    return Observable.just("floatMap3：" + str);
                })
                // .map(str -> "map：" + str)
                // .map(str -> "observeon：" + str)
                // .observeOn(Schedulers.newThread())
                .subscribe(new Observer<String>() {
                    @Override
                    public void onSubscribe(Disposable disposable) {
                        System.out.println("onSubscribe。。。" + Thread.currentThread());
                    }

                    @Override
                    public void onNext(String s) {
                        System.out.println("onNext。。。" + s + " " + Thread.currentThread());
                    }

                    @Override
                    public void onError(Throwable throwable) {
                        System.out.println("onError。。。" + throwable.getMessage() + " " + Thread.currentThread());
                    }

                    @Override
                    public void onComplete() {
                        System.out.println("onComplete。。。 " + Thread.currentThread());
                    }
                });
        System.out.println("结束。。。" + Thread.currentThread());
        try {
            Thread.sleep(3000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    /**
     * 初始化数据
     */
    private void initData(){
        studentList = new ArrayList<>();
        initStudent("班级1");
        initStudent("班级2");
    }

    private void initStudent(String clazzUid){
        for (int i = 1; i < 11; i++) {
            Student student = new Student();
            student.setClassUid(clazzUid);
            student.setUid(atomicInteger.get() + "");
            student.setName("学生" + i);
            student.setAge(random.nextInt(26));
            student.setSourceScoreList(initSourceScore());
            studentList.add(student);
        }
    }

    private List<SourceScore> initSourceScore(){
        List<SourceScore> sourceScoreList = new ArrayList<>();
        for (int i = 1; i < 15; i++) {
            SourceScore sourceScore = new SourceScore();
            sourceScore.setUid(atomicInteger.get() + "");
            sourceScore.setName("课程" + i);
            sourceScore.setScore(random.nextInt(100));
            sourceScoreList.add(sourceScore);
        }
        return sourceScoreList;
    }

}
