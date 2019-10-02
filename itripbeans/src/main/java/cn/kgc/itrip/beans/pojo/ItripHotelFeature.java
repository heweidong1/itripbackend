package cn.kgc.itrip.beans.pojo;
import java.io.Serializable;
import java.util.Date;
import lombok.Data;
/***
*   
*/
@Data
public class ItripHotelFeature implements Serializable {
        /**
          *
          */
        private Long id;
        /**
          *
          */
        private Long hotelId;
        /**
          *
          */
        private Long featureId;
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
