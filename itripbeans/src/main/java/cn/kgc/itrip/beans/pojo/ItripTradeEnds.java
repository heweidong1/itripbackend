package cn.kgc.itrip.beans.pojo;
import java.io.Serializable;
import java.util.Date;
import lombok.Data;
/***
*   订单支付完成后，系统需对该订单进行后续处理，如减库存等。处理完成后，删除此表中的订单记录
*/
@Data
public class ItripTradeEnds implements Serializable {
        /**
          *订单ID
          */
        private Long id;
        /**
          *订单编号(注意非支付宝交易编号tradeNo)
          */
        private String orderNo;
        /**
          *标识字段(默认0：未处理；1：处理中)
          */
        private Integer flag;
}
