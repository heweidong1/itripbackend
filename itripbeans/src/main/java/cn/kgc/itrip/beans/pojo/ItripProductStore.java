package cn.kgc.itrip.beans.pojo;
import java.io.Serializable;
import java.util.Date;
import lombok.Data;
/***
*   原始库存表
*/
@Data
public class ItripProductStore implements Serializable {
        /**
          *
          */
        private Long id;
        /**
          *商品类型(0:旅游产品 1:酒店产品 2:机票产品)
          */
        private Integer productType;
        /**
          *商品id
          */
        private Long productId;
        /**
          *商品库存
          */
        private Integer store;
        /**
          *
          */
        private Date creationDate;
        /**
          *
          */
        private Long createdBy;
        /**
          *
          */
        private Date modifyDate;
        /**
          *
          */
        private Long modifiedBy;
}
