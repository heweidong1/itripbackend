package cn.kgc.itrip.beans.vo;

import lombok.Data;

@Data
public class ItripTokenVo
{
    private Long expTime;
    private Long gentTime;
    private String token;

}
