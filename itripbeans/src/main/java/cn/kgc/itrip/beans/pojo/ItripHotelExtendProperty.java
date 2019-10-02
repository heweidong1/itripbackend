package cn.kgc.itrip.beans.pojo;
import java.io.Serializable;
import java.util.Date;
import lombok.Data;
/***
*   
*/
@Data
public class ItripHotelExtendProperty implements Serializable {
        /**
          *
          */
        private Long id;
        /**
          *酒店id
          */
        private Long hotelId;
        /**
          *推广属性
          */
        private Long extendPropertyId;
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
