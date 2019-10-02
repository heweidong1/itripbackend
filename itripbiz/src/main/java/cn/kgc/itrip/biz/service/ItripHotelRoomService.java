package cn.kgc.itrip.biz.service;

import cn.kgc.itrip.beans.common.ServerResponse;
import cn.kgc.itrip.beans.vo.SearchHotelRoomVO;

public interface ItripHotelRoomService
{
    public ServerResponse queryHotelRoomByHotel(SearchHotelRoomVO searchHotelRoomVO) throws Exception;

    public ServerResponse queryHotelRoombed()throws Exception;
}
