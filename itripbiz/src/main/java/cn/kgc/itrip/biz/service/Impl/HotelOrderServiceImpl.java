package cn.kgc.itrip.biz.service.Impl;

import cn.kgc.itrip.beans.common.ServerResponse;
import cn.kgc.itrip.beans.pojo.*;
import cn.kgc.itrip.beans.vo.*;
import cn.kgc.itrip.biz.service.HotelOrderService;
import cn.kgc.itrip.mapper.itripHotel.ItripHotelMapper;
import cn.kgc.itrip.mapper.itripHotelOrder.ItripHotelOrderMapper;
import cn.kgc.itrip.mapper.itripHotelRoom.ItripHotelRoomMapper;
import cn.kgc.itrip.mapper.itripOrderLinkUser.ItripOrderLinkUserMapper;
import cn.kgc.itrip.mapper.itripUserLinkUser.ItripUserLinkUserMapper;
import cn.kgc.itrip.utils.*;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;

import javax.servlet.http.HttpServletRequest;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class HotelOrderServiceImpl implements HotelOrderService
{

    @Autowired
    private RedisApi redisApi;

    @Autowired
    private ItripHotelOrderMapper itripHotelOrderMapper;

    @Autowired
    private ItripHotelRoomMapper itripHotelRoomMapper;

    @Autowired
    private SystemConfig systemConfig;

    @Autowired
    private ItripHotelMapper itripHotelMapper;

    @Autowired
    private ItripOrderLinkUserMapper itripOrderLinkUserMapper;

    /**
     * 查询订单列表
     * @param itripSearchOrderVO
     * @param request
     * @return
     * @throws Exception
     */
    @Override
    public ServerResponse getpersonalorderlist(ItripSearchOrderVO itripSearchOrderVO, HttpServletRequest request) throws Exception {
        String token = request.getHeader("token");
        Map<String,Object> param = new HashMap<>();
        ItripUser user = JSONObject.parseObject(redisApi.get(token), ItripUser.class);
        param.put("orderType", itripSearchOrderVO.getOrderType() == -1 ? null : itripSearchOrderVO.getOrderType());
        param.put("orderStatus", itripSearchOrderVO.getOrderStatus() == -1 ? null : itripSearchOrderVO.getOrderStatus());
        param.put("userId", user.getId());
        param.put("orderNo", itripSearchOrderVO.getOrderNo());
        param.put("linkUserName", itripSearchOrderVO.getLinkUserName());
        param.put("startDate", itripSearchOrderVO.getStartDate());
        param.put("endDate", itripSearchOrderVO.getEndDate());

        Integer count = itripHotelOrderMapper.getItripHotelOrderCountByMap(param);
        Page page =new Page(itripSearchOrderVO.getPageNo()==null? 1:itripSearchOrderVO.getPageNo(),itripSearchOrderVO.getPageSize()==null?6:itripSearchOrderVO.getPageSize(),count);
        param.put("beginPos", page.getBeginPos());
        param.put("pageSize", page.getPageSize());
        List<ItripHotelOrder> itripHotelOrderListByMap =
                itripHotelOrderMapper.getItripHotelOrderListByMap(param);
        page.setRows(itripHotelOrderListByMap);
        return ServerResponse.createBySuccess(page);
    }


    /**
     * 查询个人订单详情
     *
     */
    @Override
    public  ServerResponse getPersonalOrderInfo(Long orderId, HttpServletRequest request)throws Exception
    {
        String token = request.getHeader("token");
        if(!redisApi.exists(token))
        {
            return ServerResponse.createByErrorMessage("token失效，请重新登录");
        }
        //封装返回对象
        ItripPersonalHotelOrderVO itripPersonalHotelOrderVO = new ItripPersonalHotelOrderVO();

        //根据orderId查询订单
        ItripHotelOrder order = itripHotelOrderMapper.getItripHotelOrderById(orderId);

        if(order==null)
        {
            return ServerResponse.createByErrorMessage("没有该订单");
        }

        //封装订单id
        itripPersonalHotelOrderVO.setId(order.getId());
        //封装预定方式
        itripPersonalHotelOrderVO.setBookType(order.getBookType());
        //封装订单时间
        itripPersonalHotelOrderVO.setCreationDate(order.getCreationDate());
        //封装订单号
        itripPersonalHotelOrderVO.setOrderNo(order.getOrderNo());

        //查询预定房间信息、
        ItripHotelRoom itripHotelRoomById = itripHotelRoomMapper.getItripHotelRoomById(order.getRoomId());
        itripPersonalHotelOrderVO.setRoomPayType(itripHotelRoomById.getPayType());


        //封装订单状态
        //订单状态（0：待支付 1:已取消 2:支付成功 3:已消费 4:已点评）
        //{"1":"订单提交","2":"订单支付","3":"支付成功","4":"入住","5":"订单点评","6":"订单完成"}
        //{"1":"订单提交","2":"订单支付","3":"订单取消"}

        Integer orderStatus = order.getOrderStatus();
        itripPersonalHotelOrderVO.setOrderStatus(orderStatus);

        //订单取消
        if(orderStatus==1)
        {
            itripPersonalHotelOrderVO.setOrderProcess
                    (JSONArray.parse(systemConfig.getOrderProcessCancel()));
            itripPersonalHotelOrderVO.setProcessNode("3");
        }
        //待支付
        else if(orderStatus==0)
        {
            itripPersonalHotelOrderVO.setOrderProcess
                    (JSONArray.parse(systemConfig.getOrderProcessOK()));
            itripPersonalHotelOrderVO.setProcessNode("2");
        }
        //支付成功
        else if(orderStatus==2)
        {
            itripPersonalHotelOrderVO.setOrderProcess
                    (JSONArray.parse(systemConfig.getOrderProcessOK()));
            itripPersonalHotelOrderVO.setProcessNode("3");
        }
        //以消费
        else if(orderStatus==3)
        {
            itripPersonalHotelOrderVO.setOrderProcess
                    (JSONArray.parse(systemConfig.getOrderProcessOK()));
            itripPersonalHotelOrderVO.setProcessNode("5");
        }
        //已点评
        else if(orderStatus==4)
        {
            itripPersonalHotelOrderVO.setOrderProcess
                    (JSONArray.parse(systemConfig.getOrderProcessOK()));
            itripPersonalHotelOrderVO.setProcessNode("6");
        }else {
            itripPersonalHotelOrderVO.setOrderProcess(null);
            itripPersonalHotelOrderVO.setProcessNode(null);
        }

        itripPersonalHotelOrderVO.setPayAmount(order.getPayAmount());
        itripPersonalHotelOrderVO.setPayType(order.getPayType());
        itripPersonalHotelOrderVO.setNoticePhone(order.getNoticePhone());

        return ServerResponse.createBySuccess(itripPersonalHotelOrderVO);
    }

    /**
     * 查询房型详情
     */
    @Override
    public ServerResponse getPersonalOrderRoomInfo(Long orderId, HttpServletRequest request)
    {
        if(!redisApi.exists(request.getHeader("token")))
        {
            return ServerResponse.createByErrorMessage("token失效，请重新登录");
        }

        OrderRoomDetailVO itripHotelOrderRoomInfoById = itripHotelOrderMapper.getItripHotelOrderRoomInfoById(orderId);
        return ServerResponse.createBySuccess(itripHotelOrderRoomInfoById);
    }

    /**
     * 修改订单为以消费
     */
    @Override
    public void cancelOrderToComment() throws Exception
    {
        itripHotelOrderMapper.changeOrderToComment();
    }

    @Override
    public Integer cancelOrderTimeOut() throws Exception
    {
        return  itripHotelOrderMapper.cancleOrderTimeOut();
    }

    /**
     * 验证真实库存为多少
     */
    @Override
    public ServerResponse getPreorDerInfo(ValidateRoomStoreVO validateRoomStoreVO, HttpServletRequest request) throws Exception {
        if(!redisApi.exists(request.getHeader("token")))
        {
            return ServerResponse.createByErrorMessage("token失效，请重新登录");
        }
        if(validateRoomStoreVO.getCheckInDate()==null||validateRoomStoreVO.getCheckOutDate()==null)
        {
            return ServerResponse.createByErrorMessage("入住时间和退房时间不能为空");
        }
        if(validateRoomStoreVO.getCheckInDate().getTime()>validateRoomStoreVO.getCheckOutDate().getTime())
        {
            return ServerResponse.createByErrorMessage("入住时间不能晚于退房时间");
        }


        //封装返回的数据
        RoomStoreVO roomStoreVO=new RoomStoreVO();
        //获取酒店的名字
        ItripHotel itripHotelById = itripHotelMapper.getItripHotelById(validateRoomStoreVO.getHotelId());
        roomStoreVO.setHotelName(itripHotelById.getHotelName());

        //查询房间详情
        ItripHotelRoom itripHotelRoomById = itripHotelRoomMapper.getItripHotelRoomById(validateRoomStoreVO.getRoomId());
        roomStoreVO.setHotelId(itripHotelById.getId());
        roomStoreVO.setCheckInDate(validateRoomStoreVO.getCheckInDate());
        roomStoreVO.setCheckOutDate(validateRoomStoreVO.getCheckOutDate());
        roomStoreVO.setCount(1);
        roomStoreVO.setPrice(itripHotelRoomById.getRoomPrice());
        roomStoreVO.setRoomId(itripHotelRoomById.getId());
        //查询真实库存
        //封装数据
        Map<String,Object> parpam =new HashMap<>();
        parpam.put("startTime",validateRoomStoreVO.getCheckInDate());
        parpam.put("endTime",validateRoomStoreVO.getCheckOutDate());
        parpam.put("roomId",validateRoomStoreVO.getRoomId());
        parpam.put("hotelId",validateRoomStoreVO.getHotelId());
        //调用存储过程
        itripHotelOrderMapper.flushStore(parpam);

        //查询真实库存
        List<ItripHotelTempStore> itripHotelTempStores = itripHotelOrderMapper.queryRoomStroe(parpam);
        roomStoreVO.setStore(itripHotelTempStores.get(0).getStore());
        return ServerResponse.createBySuccess(roomStoreVO);
    }


    @Override
    public ServerResponse validateRoomStore(ValidateRoomStoreVO validateRoomStoreVO, HttpServletRequest request) throws Exception {
        ServerResponse preorDerInfo = this.getPreorDerInfo(validateRoomStoreVO, request);
        RoomStoreVO roomStoreVO =( RoomStoreVO)preorDerInfo.getData();
        boolean flag=  validateRoomStoreVO.getCount()<roomStoreVO.getStore();
        Map<String,Object> ouput = new HashMap<>();
        ouput.put("flag",flag);
        return ServerResponse.createBySuccess(ouput);
    }


    /**
     * 下订单
     * @param itripAddHotelOrderVO
     * @param request
     * @return
     * @throws Exception
     */

    @Override
    public ServerResponse addHotelOrder(ItripAddHotelOrderVO itripAddHotelOrderVO, HttpServletRequest request)throws Exception
    {
        String token =  request.getHeader("token");
        if(!redisApi.exists(token))
        {
            return ServerResponse.createByErrorMessage("token失效，请重新登录");
        }

        ValidateRoomStoreVO validateRoomStoreVO = new ValidateRoomStoreVO();
        validateRoomStoreVO.setCheckInDate(itripAddHotelOrderVO.getCheckInDate());
        validateRoomStoreVO.setCheckOutDate(itripAddHotelOrderVO.getCheckOutDate());
        validateRoomStoreVO.setCount(itripAddHotelOrderVO.getCount());
        validateRoomStoreVO.setHotelId(itripAddHotelOrderVO.getHotelId());
        validateRoomStoreVO.setRoomId(itripAddHotelOrderVO.getRoomId());

        ServerResponse serverResponse = this.validateRoomStore(validateRoomStoreVO, request);
        Map<String,Object> param = (Map<String, Object>) serverResponse.getData();
        boolean flag =(boolean) param.get("flag");
        if(!flag)
        {
            return ServerResponse.createByErrorMessage("库存不足");
        }


        //封装属性
        ItripHotelOrder itripHotelOrder=new ItripHotelOrder();
        BeanUtils.copyProperties(itripAddHotelOrderVO,itripHotelOrder);

        ItripUser itripUser = JSON.parseObject(redisApi.get(token), ItripUser.class);
        itripHotelOrder.setUserId(itripUser.getId());
        itripHotelOrder.setCreatedBy(itripUser.getId());
        itripHotelOrder.setOrderStatus(0);



        //相关联系人
        List<ItripUserLinkUser> linkUserList = itripAddHotelOrderVO.getLinkUser();
        if(EmptyUtils.isNotEmpty(linkUserList))
        {
            StringBuilder builder=new StringBuilder("");
            for(int i=0;i<linkUserList.size();i++)
            {
                builder.append(linkUserList.get(i).getLinkUserName());
                if(i!=linkUserList.size()-1)
                {
                    builder.append(",");
                }
            }
            itripHotelOrder.setLinkUserName(builder.toString());
        }

        //设置住店天数
        int days= DateUtil.getBetweenDates(itripAddHotelOrderVO.getCheckInDate(),
                itripAddHotelOrderVO.getCheckOutDate()).size()-1;
        itripHotelOrder.setBookingDays(days);

        //订单来自哪个平台
        if(token.startsWith("token:PC-"))
        {
            itripHotelOrder.setBookType(0);
        }else if(token.startsWith("token:MOBILE"))
        {
            itripHotelOrder.setBookType(1);
        }else
        {
            itripHotelOrder.setBookType(2);
        }
        //订单号
        StringBuilder md5 = new StringBuilder();
        md5.append(itripHotelOrder.getHotelId());
        md5.append(itripHotelOrder.getRoomId());
        md5.append(System.currentTimeMillis());
        md5.append(Math.random()*1000000);
        String md51 = MD5.getMd5(md5.toString(), 6);

        StringBuilder orderbuilder = new StringBuilder("");
        //获取机器码
        orderbuilder.append(systemConfig.getMachineCode());
        //获取日期
        orderbuilder.append(DateUtil.format(new Date(),"yyyyMMddHHmmss"));

        //获取订单号
        orderbuilder.append(md51);
        itripHotelOrder.setOrderNo(orderbuilder.toString());


        //订单类型
        itripHotelOrder.setOrderType(1);

        //订单金额
        //根据roomid查询房间的价格
        ItripHotelRoom itripHotelRoomById = itripHotelRoomMapper.getItripHotelRoomById(itripAddHotelOrderVO.getRoomId());
        Double roomPrice = itripHotelRoomById.getRoomPrice();

        Double roomprice=days*itripAddHotelOrderVO.getCount()*roomPrice;
        BigDecimal b = new BigDecimal(roomprice);
        roomprice = b.setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();

        itripHotelOrder.setPayAmount(roomprice);

        //添加订单
        itripHotelOrder.setCreationDate(new Date());
        itripHotelOrderMapper.insertItripHotelOrder(itripHotelOrder);

        //订单关联用户OrderLinkUser  添加到itrip_order_link_user 表中
        if(EmptyUtils.isNotEmpty(linkUserList))
        {
            ItripOrderLinkUser orderLinkUser=new ItripOrderLinkUser();
            for(ItripUserLinkUser linkUser:linkUserList)
            {
                BeanUtils.copyProperties(linkUser,orderLinkUser);
                //orderid为订单编号
                orderLinkUser.setOrderId(itripHotelOrder.getId());
                //LinkUserId 联系人自己得id
                orderLinkUser.setLinkUserId(linkUser.getId());
                orderLinkUser.setCreationDate(new Date());
                orderLinkUser.setCreatedBy(linkUser.getId());
                itripOrderLinkUserMapper.insertItripOrderLinkUser(orderLinkUser);
            }
        }

        Map<String,String> ouput = new HashMap<>();
        ouput.put("id",itripHotelOrder.getId().toString());
        ouput.put("orderNo",itripHotelOrder.getOrderNo());
        return  ServerResponse.createBySuccess(ouput);
        }


    /**
     * 线下支付   修改订单状态
     * @param itripModifyHotelOrderVO
     * @param request
     * @return
     * @throws Exception
     */
    @Override
    public ServerResponse updateOrderStatusAndPayType(ItripModifyHotelOrderVO itripModifyHotelOrderVO, HttpServletRequest request)throws Exception
    {
        String token =  request.getHeader("token");
        if(!redisApi.exists(token))
        {
            return ServerResponse.createByErrorMessage("token失效，请重新登录");
        }

        //验证是否支持线下支付服务
        Integer patTypeByHotelId = itripHotelOrderMapper.getPatTypeByHotelId(itripModifyHotelOrderVO.getId());
        if(!((2&patTypeByHotelId)!=0))
        {
            return ServerResponse.createByErrorMessage("对不起，此房间不支持线下支付");
        }
        ItripHotelOrder itripHotelOrder = new ItripHotelOrder();
        itripHotelOrder.setId(itripModifyHotelOrderVO.getId());
        //设置支付类型
        itripHotelOrder.setPayType(3);
        //支付成  2 已支付
        itripHotelOrder.setOrderStatus(2);
        ItripUser itripUser = JSON.parseObject(redisApi.get(request.getHeader("token")), ItripUser.class);
        //修改人信息
        itripHotelOrder.setModifiedBy(itripUser.getId());
        itripHotelOrder.setModifyDate(new Date());

        itripHotelOrderMapper.updateItripHotelOrder(itripHotelOrder);

        return ServerResponse.createBySuccess();
    }

}
