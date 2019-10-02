package cn.kgc.itrip.beans.pojo;
import java.io.Serializable;
import java.util.Date;
import lombok.Data;
/***
*   
*/
@Data
public class ItripUserLinkUser implements Serializable {
        /**
          *主键
          */
        private Long id;
        /**
          *常用联系人姓名
          */
        private String linkUserName;
        /**
          *证件号码
          */
        private String linkIdCard;
        /**
          *常用联系人电话
          */
        private String linkPhone;
        /**
          *用户id
          */
        private Integer userId;
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
        /**
          *证件类型：(0-身份证，1-护照，2-学生证，3-军人证，4-驾驶证，5-旅行证)
          */
        private Integer linkIdCardType;
}
