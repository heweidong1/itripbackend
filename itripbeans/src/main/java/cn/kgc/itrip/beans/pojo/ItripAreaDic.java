package cn.kgc.itrip.beans.pojo;
import java.io.Serializable;
import java.util.Date;
import lombok.Data;
/***
*   区域字典表
*/
@Data
public class ItripAreaDic implements Serializable {
        /**
          *主键
          */
        private Long id;
        /**
          *区域名称
          */
        private String name;
        /**
          *区域编号
          */
        private String areaNo;
        /**
          *父级区域
          */
        private Long parent;
        /**
          *0:未激活 1:已激活
          */
        private Integer isActivated;
        /**
          *是否是商圈(0:不是 1:是)
          */
        private Integer isTradingArea;
        /**
          *(0:不是 1：是)
          */
        private Integer isHot;
        /**
          *区域级别(0:国家级 1:省级 2:市级 3:县/区)
          */
        private Integer level;
        /**
          *1:国内 2：国外
          */
        private Integer isChina;
        /**
          *
          */
        private String pinyin;
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

        private String description;
}
