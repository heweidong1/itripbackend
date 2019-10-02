package cn.kgc.itrip.beans.pojo;
import java.io.Serializable;
import java.util.Date;
import lombok.Data;
/***
*   评论表
*/
@Data
public class ItripComment implements Serializable {
        /**
          *主键
          */
        private Long id;
        /**
          *如果产品是酒店的话 改字段保存酒店id
          */
        private Long hotelId;
        /**
          *商品id
          */
        private Long productId;
        /**
          *订单id
          */
        private Long orderId;
        /**
          *商品类型(0:旅游产品 1:酒店产品 2:机票产品)
          */
        private Integer productType;
        /**
          *评论内容
          */
        private String content;
        /**
          *用户编号
          */
        private Long userId;
        /**
          *是否包含图片(当用户上传评论时检测)0:无图片 1:有图片
          */
        private Integer isHavingImg;
        /**
          *位置评分
          */
        private Integer positionScore;
        /**
          *设施评分
          */
        private Integer facilitiesScore;
        /**
          *服务评分
          */
        private Integer serviceScore;
        /**
          *卫生评分
          */
        private Integer hygieneScore;
        /**
          *综合评分
          */
        private Integer score;
        /**
          *出游类型
          */
        private String tripMode;
        /**
          *是否满意（0：有待改善 1：值得推荐）
          */
        private Integer isOk;
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
