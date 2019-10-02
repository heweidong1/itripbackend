package cn.kgc.itrip.beans.pojo;
import java.io.Serializable;
import java.util.Date;
import lombok.Data;
/***
*   实时库存表
*/
@Data
public class ItripHotelTempStore implements Serializable {
        /**
          *
          */
        private Long id;
        /**
          *
          */
        private Integer hotelId;
        /**
          *商品id
          */
        private Long roomId;
        /**
          *记录时间
          */
        private Date recordDate;
        /**
          *库存
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
