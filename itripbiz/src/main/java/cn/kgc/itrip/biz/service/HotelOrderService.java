package cn.kgc.itrip.biz.service;

import cn.kgc.itrip.beans.common.ServerResponse;
import cn.kgc.itrip.beans.vo.ItripAddHotelOrderVO;
import cn.kgc.itrip.beans.vo.ItripModifyHotelOrderVO;
import cn.kgc.itrip.beans.vo.ItripSearchOrderVO;
import cn.kgc.itrip.beans.vo.ValidateRoomStoreVO;
import org.springframework.web.bind.annotation.RequestBody;

import javax.servlet.http.HttpServletRequest;

public interface HotelOrderService
{
    public ServerResponse getpersonalorderlist(ItripSearchOrderVO itripSearchOrderVO, HttpServletRequest request) throws Exception;

    public  ServerResponse getPersonalOrderInfo(Long orderId, HttpServletRequest request)throws Exception;

    public ServerResponse getPersonalOrderRoomInfo(Long orderId, HttpServletRequest request);


    public void cancelOrderToComment() throws Exception;

    public Integer cancelOrderTimeOut() throws Exception;

    public ServerResponse getPreorDerInfo(ValidateRoomStoreVO validateRoomStoreVO, HttpServletRequest request) throws Exception;

    public ServerResponse validateRoomStore(ValidateRoomStoreVO validateRoomStoreVO, HttpServletRequest request) throws Exception;

    public ServerResponse addHotelOrder(ItripAddHotelOrderVO itripAddHotelOrderVO, HttpServletRequest request)throws Exception;

    public ServerResponse updateOrderStatusAndPayType(ItripModifyHotelOrderVO itripModifyHotelOrderVO, HttpServletRequest request)throws Exception;
}
