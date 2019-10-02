package cn.kgc.itrip.search.model;

import lombok.Data;
import org.apache.solr.client.solrj.beans.Field;

import java.util.Date;

/**
 * @author 韩宏伟
 * 类名： Hotel
 * 创建时间： 2019/8/20 18:50
 */
@Data
public class Hotel {

    @Field
    private Long id;
    @Field
    private String hotelName;
    @Field
    private Long countryId;
    @Field
    private Long provinceId;
    @Field
    private Long cityId;
    @Field
    private String details;
    @Field
    private String facilities;
    @Field
    private String hotelPolicy;
    @Field
    private Integer hotelType;
    @Field
    private Integer hotelLevel;
    @Field
    private  Integer isGroupPurchase;
    @Field
    private String address;
    @Field
    private String redundantCityName;
    @Field
    private String redundantProvinceName;
    @Field
    private String redundantCountryName;
    @Field
    private Integer redundantHotelStore;
    @Field
    private Date creationDate;
    @Field
    private Long createBy;
    @Field
    private Date modifyDate;
    @Field
    private Long modifyBy;
    @Field
    private String imgUrl;
    @Field
    private Integer isOkCount;
    @Field
    private Integer commentCount;
    @Field
    private String extendPropertyNames;
    @Field
    private String extendPropertyIds;
    @Field
    private String extendPropertyPics;
    @Field
    private String tradingAreaIds;
    @Field
    private String tradingAreaNames;
    @Field
    private Double minPrice;
    @Field
    private Double maxPrice;
    @Field
    private String featureIds;
    @Field
    private String featureNames;
    @Field
    private Double avgScore;

}
