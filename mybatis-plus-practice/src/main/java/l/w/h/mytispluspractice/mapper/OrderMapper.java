package l.w.h.mytispluspractice.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import l.w.h.mytispluspractice.entity.Order;
import org.apache.ibatis.annotations.Param;

/**
 * <p>
 * 订单详情 Mapper 接口
 * </p>
 *
 * @author lwh
 * @since 2021/12/17 16:26
 */
public interface OrderMapper extends BaseMapper<Order> {

    void insertSelective(Order order);

    Order findByOrderUid(@Param("organizationUid") String organizationUid,@Param("shopUid") String shopUid,@Param("orderUid") String orderUid);

}
