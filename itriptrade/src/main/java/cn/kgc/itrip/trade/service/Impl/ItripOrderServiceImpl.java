package cn.kgc.itrip.trade.service.Impl;

import cn.kgc.itrip.beans.pojo.ItripHotelOrder;
import cn.kgc.itrip.mapper.itripHotelOrder.ItripHotelOrderMapper;
import cn.kgc.itrip.trade.service.ItripOrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class ItripOrderServiceImpl implements ItripOrderService
{

    @Autowired
    private ItripHotelOrderMapper itripHotelOrderMapper;

    @Override
    public ItripHotelOrder getItripHotelOrderByOrderNo(String orderNo)throws Exception
    {
        Map<String,Object> param = new HashMap<>();
        param.put("orderNo",orderNo);
        List<ItripHotelOrder> itripHotelOrderListByMap = itripHotelOrderMapper.getItripHotelOrderListByMap(param);
        if(itripHotelOrderListByMap!=null&&itripHotelOrderListByMap.size()==1)
        {
            return itripHotelOrderListByMap.get(0);
        }
        return null;
    }

    @Override
    public void updateOrder(ItripHotelOrder itripHotelOrder) throws Exception {
        itripHotelOrderMapper.updateItripHotelOrder(itripHotelOrder);
    }
}
