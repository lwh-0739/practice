package l.w.h.mytispluspractice.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * <p>
 * 订单详情
 * </p>
 *
 * @author lwh
 * @since 2021/12/17 16:26
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ApiModel("订单详情")
@TableName("`order`")
public class Order implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty("数据库自增标识")
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    @ApiModelProperty("订单唯一编码")
    private String orderUid;

    @ApiModelProperty("用户标识")
    private String memberUid;

    @ApiModelProperty("用户账号")
    private String memberName;

    @ApiModelProperty("创建时间")
    private LocalDateTime createTime;

    @ApiModelProperty("支付时间")
    private LocalDateTime paymentTime;

    @ApiModelProperty("发货时间")
    private LocalDateTime deliveryTime;

    @ApiModelProperty("确认收货时间")
    private LocalDateTime receiveTime;

    @ApiModelProperty("评价时间")
    private LocalDateTime commentTime;

    @ApiModelProperty("更新时间")
    private LocalDateTime modifyTime;

    @ApiModelProperty("订单总金额；单位：分")
    private Integer totalAmount;

    @ApiModelProperty("订单实际支付金额；单位：分")
    private Integer payAmount;

    @ApiModelProperty("运费；单位：分")
    private Integer freightAmount;

    @ApiModelProperty("优惠券标识列表；格式：uid,uid")
    private String couponUidList;

    @ApiModelProperty("优惠金额（总）；单位：分")
    private Integer couponAmount;

    @ApiModelProperty("管理员是否后台改价；0：未改价（默认）；1：改价")
    private Integer adminModifyPrice;

    @ApiModelProperty("订单类型；0：正常订单（默认）；1：定向卡订单；2：秒杀订单")
    private Integer orderType;

    @ApiModelProperty("支付方式；0：未支付；1：支付宝支付；2：微信支付；3：余额支付；4：定向卡支付；5：存储卡；6：信用卡")
    private Integer payType;

    @ApiModelProperty("定向卡标识；若pay_type=4，此值必填")
    private String directionalCardUid;

    @ApiModelProperty("0：PC端；1：APP端；2：小程序；3：线下门店；4：其他")
    private Integer sourceType;

    @ApiModelProperty("销售员（线下），目前不启用")
    private String salesman;

    @ApiModelProperty("订单状态；0：待付款；1：待发货；2：待收货；3：已完成；4：已关闭；5：售后中")
    private Integer status;

    @ApiModelProperty("支付状态；0：待付款；1：已支付；2：部分退款；3：全部退款")
    private Integer payStatus;

    @ApiModelProperty("总退款金额（增量修改）；单位：分")
    private Integer refund;

    @ApiModelProperty("第三方支付订单号；即非余额、定向卡支付时")
    private String payOrderNo;

    @ApiModelProperty("删除状态；0：未删除；-1：已删除")
    private Integer deleteStatus;

    @ApiModelProperty("是否评论；0：未评论；1：已评论")
    private Integer comment;

    @ApiModelProperty("用户备注")
    private String note;

    @ApiModelProperty("管理员备注")
    private String adminNote;

    @ApiModelProperty("自动确认时间（天）")
    private Integer autoConfirmDay;

    @ApiModelProperty("组织标识")
    private String organizationUid;

    @ApiModelProperty("门店标识")
    private String shopUid;

    @ApiModelProperty("小程序标识")
    private String appUid;

    @ApiModelProperty("收货人")
    private String receiverName;

    @ApiModelProperty("收货人电话")
    private String receiverPhone;

    @ApiModelProperty("收货地址；格式：省份/直辖市 + , + 城市 + , + 区 + , + 详细地址")
    private String receiverAddress;

    @ApiModelProperty("预留字段1")
    private String reservedOne;

    @ApiModelProperty("预留字段2")
    private String reservedTwo;

    @ApiModelProperty("预留字段3")
    private String reservedThree;

}
