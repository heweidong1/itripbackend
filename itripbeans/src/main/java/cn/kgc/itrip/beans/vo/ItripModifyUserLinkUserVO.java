package cn.kgc.itrip.beans.vo;

import lombok.Data;

@Data
public class ItripModifyUserLinkUserVO
{
    private Long id;
    private String linkIdCard;
    private Integer linkIdCardType;
    private String linkPhone;
    private String linkUserName;
    private Integer userId;
}
