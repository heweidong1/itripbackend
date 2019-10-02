package cn.kgc.itrip.beans.pojo;
import java.io.Serializable;
import java.util.Date;
import lombok.Data;
/***
*   订单表
*/
@Data
public class ItripHotelOrder implements Serializable {
        /**
          *主键
          */
        private Long id;
        /**
          *用户id
          */
        private Long userId;
        /**
          *订单类型(0:旅游产品 1:酒店产品 2：机票产品)
          */
        private Integer orderType;
        /**
          *订单号
          */
        private String orderNo;
        /**
          *交易编号
          */
        private String tradeNo;
        /**
          *冗余字段 酒店id
          */
        private Long hotelId;
        /**
          *冗余字段 酒店名称
          */
        private String hotelName;
        /**
          *房间id
          */
        private Long roomId;
        /**
          *消耗数量
          */
        private Integer count;
        /**
          *预订天数
          */
        private Integer bookingDays;
        /**
          *入住时间
          */
        private Date checkInDate;
        /**
          *退房时间
          */
        private Date checkOutDate;
        /**
          *订单状态（0：待支付 1:已取消 2:支付成功 3:已消费 4：已点评）
          */
        private Integer orderStatus;
        /**
          *支付金额
          */
        private Double payAmount;
        /**
          *支付方式:1:支付宝 2:微信 3:到店付
          */
        private Integer payType;
        /**
          *联系手机号
          */
        private String noticePhone;
        /**
          *联系邮箱
          */
        private String noticeEmail;
        /**
          *特殊需求
          */
        private String specialRequirement;
        /**
          *是否需要发票（0：不需要 1：需要）
          */
        private Integer isNeedInvoice;
        /**
          *发票类型（0：个人 1：公司）
          */
        private Integer invoiceType;
        /**
          *发票抬头
          */
        private String invoiceHead;
        /**
          *入住人名称
          */
        private String linkUserName;
        /**
          *0:WEB端 1:手机端 2:其他客户端
          */
        private Integer bookType;
        /**
          *预定时间
          */
        private Date creationDate;
        /**
          *
          */
        private Long createdBy;
        /**
          *支付完成时间
          */
        private Date modifyDate;
        /**
          *
          */
        private Long modifiedBy;
}
