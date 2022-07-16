package l.w.h.mytispluspractice;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import l.w.h.mytispluspractice.entity.Order;
import l.w.h.mytispluspractice.mapper.OrderMapper;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import javax.annotation.Resource;

/**
 * @author lwh
 * @since 2021/12/17 16:32
 **/

@SpringBootTest
public class MybatisPlusApplicationTest {

    @Resource
    private OrderMapper orderMapper;

    @Test
    public void test(){
        // Order order = Order
        //         .builder()
        //         .id(2L)
        //         .memberUid("test-2")
        //         .memberName("测试2")
        //         .orderUid("1226338932483489")
        //         .totalAmount(1000)
        //         .payAmount(1000)
        //         .orderType(1)
        //         .payType(1)
        //         .autoConfirmDay(15)
        //         .organizationUid("zhgtv")
        //         .shopUid("zhgtv")
        //         .build();
        // System.out.println(orderMapper.insert(order));
        // Order order = orderMapper.selectById(1L);
        // if (null != order){
        //     System.out.println(order);
        // }else {
        //     System.out.println("订单不存在！");
        // }
        // order.setPayType(2);
        // order.setOrderType(null);
        // orderMapper.updateById(order);
        // System.out.println(orderMapper.update(order, Wrappers.<Order>update().eq("organization_uid", "zhgtv")));

        // Page<Order> orderPage = orderMapper.selectPage(new Page<>(1, 2), Wrappers.<Order>query().orderBy(true,false,"create_time"));
        // System.out.println(JSON.toJSONString(orderPage));

        Order order = orderMapper.selectById(1);
//        orderMapper.update(order, Wrappers.<Order>lambdaUpdate().eq(Order::getId,1).setSql("pay_amount = pay_amount + 100"));
    }

}
