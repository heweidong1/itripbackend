package cn.kgc.itrip.biz.service;

import cn.kgc.itrip.beans.common.ServerResponse;
import cn.kgc.itrip.beans.vo.HotelVideoDescVO;
import cn.kgc.itrip.beans.vo.ItripHotelImageVO;
import cn.kgc.itrip.beans.vo.ItripTradeAreaVO;

import java.util.List;

public interface IHotelService
{
    ServerResponse<List<ItripTradeAreaVO>> queryTradeList(Integer cityId)throws Exception;

    public ServerResponse<List<ItripHotelImageVO>> queryImageByTargerId(Long targetId, String type)throws Exception;

    public ServerResponse  queryHotelByHotelId(Long hotelId)throws Exception;

    public ServerResponse queryHotelfacilitiesByHotelId(Long HotelId)throws Exception;

    public ServerResponse queryhotelfeature() throws Exception;

    public ServerResponse queryhotcity(Integer type)throws Exception;

    public ServerResponse queryhoteldetails(Long id)throws Exception;

    public ServerResponse queryhotelpolicy(Long id)throws Exception;
}
