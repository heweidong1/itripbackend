package cn.kgc.itrip.beans.pojo;
import java.io.Serializable;
import java.util.Date;
import lombok.Data;
/***
*   图片表
*/
@Data
public class ItripImage implements Serializable {
        /**
          *主键
          */
        private Long id;
        /**
          *图片类型(0:酒店图片1:房间图片2:评论图片)
          */
        private String type;
        /**
          *关联id
          */
        private Long targetId;
        /**
          *图片s上传顺序位置
          */
        private Integer position;
        /**
          *图片地址
          */
        private String imgUrl;
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
