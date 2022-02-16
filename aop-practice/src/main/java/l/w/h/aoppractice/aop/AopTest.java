package l.w.h.aoppractice.aop;

import com.alibaba.fastjson.JSON;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

/**
 * @author lwh
 * @since 2022/2/16 11:28
 **/
@Aspect
@Component
public class AopTest {

    private Logger logger = LoggerFactory.getLogger(AopTest.class);

    @Pointcut("execution(public * l.w.h.aoppractice..*.*(..))")
    public void test(){}

    /**
     * 通知执行顺序：
     *      @Around: joinPoint.proceed()之前的代码 -> @Before -> 处理代码 -> @AfterReturning/@AfterThrowing -> @After -> @Around: joinPoint.proceed()之后的代码（若出现异常，改代码不执行）
     * 参数使用：
     *      均可使用：JoinPoint，获取参数、签名等信息，@Around中可使用：ProceedingJoinPoint 获取相关信息
     */

    @Before("test()")
    public void doBefore(JoinPoint joinPoint){
        logger.error("before：执行..." + JSON.toJSONString(joinPoint.getArgs()));
        // logger.error("before：执行..." + joinPoint.getClass());
    }

    @Around("test()")
    public Object doAround(ProceedingJoinPoint joinPoint) throws Throwable {
        logger.error("around；执行前..." + JSON.toJSONString(joinPoint.getArgs()));
        Object proceed = joinPoint.proceed();
        logger.error("around；执行后...");
        return proceed;
    }

    @After("test()")
    public void doAfter(JoinPoint joinPoint){
        logger.error("after：执行..." + JSON.toJSONString(joinPoint.getArgs()));
        // logger.error("after：执行..." + joinPoint.getClass());
    }

    @AfterReturning("test()")
    public void doAfterReturn(JoinPoint joinPoint){
        logger.error("afterReturn：执行..." + JSON.toJSONString(joinPoint.getArgs()));
        // logger.error("afterReturn：执行..." + joinPoint.getClass());
    }

    @AfterThrowing("test()")
    public void doAfterThrowing(JoinPoint joinPoint){
        logger.error("afterThrowing：执行..." + JSON.toJSONString(joinPoint.getArgs()));
        // logger.error("afterThrowing：执行..." + joinPoint.getClass());
    }

}
