package cn.kgc.itrip.beans.pojo;
import java.io.Serializable;
import java.util.Date;
import lombok.Data;
/***
*   标签字典表
*/
@Data
public class ItripLabelDic implements Serializable {
        /**
          *主键
          */
        private Long id;
        /**
          *key值
          */
        private String name;
        /**
          *value值
          */
        private String value;
        /**
          *描述
          */
        private String description;
        /**
          *父级标签id(1级标签则为0)
          */
        private Long parentId;
        /**
          *标签图片地址
          */
        private String pic;
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
