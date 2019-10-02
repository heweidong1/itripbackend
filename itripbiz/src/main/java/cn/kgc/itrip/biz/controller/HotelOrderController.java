package cn.kgc.itrip.biz.controller;

import cn.kgc.itrip.beans.common.ServerResponse;
import cn.kgc.itrip.beans.vo.ItripAddHotelOrderVO;
import cn.kgc.itrip.beans.vo.ItripModifyHotelOrderVO;
import cn.kgc.itrip.beans.vo.ItripSearchOrderVO;
import cn.kgc.itrip.beans.vo.ValidateRoomStoreVO;
import cn.kgc.itrip.biz.service.HotelOrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

@Controller
@RequestMapping("/api/hotelorder")
public class HotelOrderController
{
    @Autowired
    private HotelOrderService hotelOrderService;

    /**
     * 获取订单列表
     * @param itripSearchOrderVO
     * @param request
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "/getpersonalorderlist", method = RequestMethod.POST, produces = "application/json")
    @ResponseBody
    public ServerResponse getPersonalOrderList(@RequestBody ItripSearchOrderVO itripSearchOrderVO,
                                               HttpServletRequest request)throws Exception
    {
        return hotelOrderService.getpersonalorderlist(itripSearchOrderVO, request);
    }


    /**
     * 根据订单id查看个人订单详情
     * @param orderId
     * @param request
     * @return
     */
    @RequestMapping(value = "/getpersonalorderinfo/{orderId}", method = RequestMethod.GET, produces = "application/json")
    @ResponseBody
    public ServerResponse getPersonalOrderInfo( @PathVariable Long orderId, HttpServletRequest request)throws Exception
    {
        return hotelOrderService.getPersonalOrderInfo(orderId,request);
    }


    /**
     * 根据订单id查询房型的详情
     */
    @RequestMapping(value = "/getpersonalorderroominfo/{orderId}", method = RequestMethod.GET, produces = "application/json")
    @ResponseBody
    public ServerResponse getPersonalOrderRoomInfo(@PathVariable Long orderId, HttpServletRequest request)throws Exception
    {
        return hotelOrderService.getPersonalOrderRoomInfo(orderId,request);
    }


    /**
     * 以消费
     */
    @Scheduled(cron = "0 0 0/2 * * ?")
    public void cancelOrderToComment() throws Exception
    {
        hotelOrderService.cancelOrderToComment();
    }

    /**
     * 已取消
     */
    @Scheduled(cron = "0 0/10 * * * ?")
    public void cancelOrderTimeOut()throws Exception
    {
        hotelOrderService.cancelOrderTimeOut();
    }


    /**
     * 根据用户得入住时间查询真实库存
     * @param validateRoomStoreVO
     * @param request
     * @return
     * @throws Exception
     */

    @RequestMapping(value = "/getpreorderinfo",method = RequestMethod.POST,produces = "application/json")
    @ResponseBody
    public ServerResponse getPreorDerInfo(@RequestBody ValidateRoomStoreVO validateRoomStoreVO, HttpServletRequest request)throws  Exception
    {
        return hotelOrderService.getPreorDerInfo(validateRoomStoreVO, request);
    }


    /**
     * 修改订房日期验证是否有房
     */
    @RequestMapping(value = "/validateroomstore",method = RequestMethod.POST,produces = "application/json")
    @ResponseBody
    public ServerResponse validateRoomStore(@RequestBody ValidateRoomStoreVO validateRoomStoreVO, HttpServletRequest request)throws  Exception
    {
        return hotelOrderService.validateRoomStore(validateRoomStoreVO, request);
    }



    /**
     * 生成订单
     * @param itripAddHotelOrderVO
     * @param request
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "/addhotelorder",method = RequestMethod.POST,produces = "application/json")
    @ResponseBody
    public ServerResponse addHotelOrder(@RequestBody ItripAddHotelOrderVO itripAddHotelOrderVO, HttpServletRequest request)throws Exception
    {
        return hotelOrderService.addHotelOrder(itripAddHotelOrderVO, request);
    }


    /**
     * 修改订单状态
     * @param itripModifyHotelOrderVO
     * @param request
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "/updateorderstatusandpaytype",method = RequestMethod.POST,produces = "application/json")
    @ResponseBody
    public ServerResponse updateOrderStatusAndPayType(@RequestBody ItripModifyHotelOrderVO itripModifyHotelOrderVO, HttpServletRequest request)throws Exception
    {
        return hotelOrderService.updateOrderStatusAndPayType(itripModifyHotelOrderVO, request);
    }
}
