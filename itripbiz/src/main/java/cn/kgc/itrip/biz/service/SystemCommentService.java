package cn.kgc.itrip.biz.service;

import cn.kgc.itrip.beans.common.ServerResponse;
import cn.kgc.itrip.beans.vo.ItripAddCommentVO;
import cn.kgc.itrip.beans.vo.ItripSearchCommentVO;

import javax.servlet.http.HttpServletRequest;

public interface SystemCommentService
{
    public ServerResponse getTravelType() throws Exception;

    public ServerResponse upLoad(HttpServletRequest request)throws Exception;

    public ServerResponse delpic( String imgName, HttpServletRequest request)throws Exception;

    public  ServerResponse add(ItripAddCommentVO itripAddCommentVO, HttpServletRequest request)throws Exception;

    public ServerResponse getCommentList(ItripSearchCommentVO itripSearchCommentVO)throws Exception;

    public ServerResponse getHotelDesc(Long hotelId)throws Exception;

    public ServerResponse gethotelscore(Long hotelId)throws Exception;

    public ServerResponse getCount(Long hotelId) throws Exception;
}
