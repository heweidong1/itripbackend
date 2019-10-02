package cn.kgc.itrip.beans.pojo;
import java.io.Serializable;
import java.util.Date;
import lombok.Data;
/***
*   酒店表
*/
@Data
public class ItripHotel implements Serializable {
        /**
          *主键
          */
        private Long id;
        /**
          *酒店名称
          */
        private String hotelName;
        /**
          *所在国家id
          */
        private Long countryId;
        /**
          *所在省份id
          */
        private Long provinceId;
        /**
          *所在城市id
          */
        private Long cityId;
        /**
          *酒店所在地址
          */
        private String address;
        /**
          *酒店介绍（保存附文本）
          */
        private String details;
        /**
          *酒店设施
          */
        private String facilities;
        /**
          *酒店政策
          */
        private String hotelPolicy;
        /**
          *酒店类型(1:国内酒店 2:国际酒店)
          */
        private Integer hotelType;
        /**
          *(1:经济酒店  2:二星级酒店  3:三星级 4:四星酒店 5星酒店)
          */
        private Integer hotelLevel;
        /**
          *是否是团购酒店
          */
        private Integer isGroupPurchase;
        /**
          *城市名称 冗余字段
          */
        private String redundantCityName;
        /**
          *省名称 冗余字段
          */
        private String redundantProvinceName;
        /**
          *国家名称 冗余字段
          */
        private String redundantCountryName;
        /**
          *酒店库存（冗余，每天开定时任务的时候更新）
          */
        private Integer redundantHotelStore;
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
