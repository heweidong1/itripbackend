package cn.kgc.itrip.trade.service;

import cn.kgc.itrip.beans.pojo.ItripHotelOrder;

public interface ItripOrderService
{
    public ItripHotelOrder getItripHotelOrderByOrderNo(String orderNo)throws Exception;

    public void updateOrder(ItripHotelOrder itripHotelOrder) throws Exception;
}
