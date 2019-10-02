package cn.kgc.itrip.beans.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;

@Data
@ApiModel(value = "ItripSearchUserLinkUserVO",description = "查询常用联系人")
public class ItripSearchUserLinkUserVO
{
    @ApiModelProperty("[必填] 常用刚联系人姓名")
    private String linkUserName;
    @ApiModelProperty("[必填，注：接收数字类型 酒店ID")
    private Long hotelId;
    @ApiModelProperty("[必填，注：接收数字类型 房间ID")
    private Long roomId;
    @ApiModelProperty("[必填，注：接收日期类型 入住时间")
    private Date checkInDate;
    @ApiModelProperty("[必填，注：接收日期类型 退房时间")
    private Date checkOutDate;
    @ApiModelProperty("[必填，默认请传1")
    private Integer count;


}
