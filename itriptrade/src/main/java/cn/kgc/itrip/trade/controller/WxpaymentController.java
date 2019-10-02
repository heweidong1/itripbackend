package cn.kgc.itrip.trade.controller;

import cn.kgc.itrip.beans.common.ServerResponse;
import cn.kgc.itrip.beans.pojo.ItripHotelOrder;
import cn.kgc.itrip.trade.config.WXPayConfig;
import cn.kgc.itrip.trade.service.ItripOrderService;
import cn.kgc.itrip.utils.EmptyUtils;
import com.github.wxpay.sdk.WXPay;
import com.github.wxpay.sdk.WXPayConstants;
import com.github.wxpay.sdk.WXPayUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Map;

@Controller
@RequestMapping("/api/wxpay")
public class WxpaymentController
{

    @Autowired
    private ItripOrderService itripOrderService;

    @Autowired
    private WXPayConfig wxPayConfig;

    /**
     * 微信支付 前端页面微信支付 调用此方法
     * 此方法 对微信服务器 发送请求 微信服务返回一个Map【xml封装】
     * 将该Map传返回前端页面 前端页面通过字符串生成二维码
     * @param orderNo
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "/createqccode/{orderNo}",method = RequestMethod.GET,produces = "application/json")
    @ResponseBody
    public ServerResponse createQcCode(@PathVariable String orderNo )throws Exception
    {
        WXPay wxpay = new WXPay(wxPayConfig);
        ItripHotelOrder order = itripOrderService.getItripHotelOrderByOrderNo(orderNo);
        if(EmptyUtils.isEmpty(order)||EmptyUtils.isNotEmpty(order.getTradeNo()))
        {
            return ServerResponse.createByErrorMessage("订单状态异常");
        }
        Map<String, String> data = new HashMap<>();
        data.put("body", "爱旅行项目订单支付");
        data.put("out_trade_no",orderNo);
        data.put("device_info", "");
        data.put("fee_type", "CNY");
        //设定支付金额为1分钱，((int)order.getPayAmount()*100)
        data.put("total_fee", "1");
        data.put("spbill_create_ip", "123.12.12.123");
        data.put("notify_url", wxPayConfig.getNotifyUrl());
        // 此处指定为扫码支付
        data.put("trade_type", "NATIVE");
        //产品id
        data.put("product_id", order.getRoomId().toString());

        //发送
        Map<String, String> stringStringMap = wxpay.unifiedOrder(data);
        //判断状态码
        if(stringStringMap.get("return_code").equals(WXPayConstants.SUCCESS))
        {
            //判断业务结果
            if(stringStringMap.get("result_code").equals(WXPayConstants.SUCCESS))
            {
                //返回页面生成二维码
                Map<String,Object> result=new HashMap<>();

                result.put("hotelName",order.getHotelName());
                result.put("roomId",order.getRoomId());
                result.put("count",order.getCount());
                result.put("payAmount",order.getPayAmount());
                //二维码
                result.put("codeUrl",stringStringMap.get("code_url"));

                return ServerResponse.createBySuccess(result);
            }else
            {
                return ServerResponse.createByErrorMessage(stringStringMap.get("err_code_des"));
            }
        }else
        {
            return ServerResponse.createByErrorMessage(stringStringMap.get("return_msg"));
        }
    }

    /**
     * 微信后台调用 返回结果 回调
     */
    @RequestMapping(value = "/notify",method = RequestMethod.POST)
    @ResponseBody
    public void paymentCallback(HttpServletRequest request, HttpServletResponse response)throws Exception
    {
        WXPay wxpay = new WXPay(wxPayConfig);
        //先从请求中取数据
        InputStream inputStream = request.getInputStream();
        BufferedReader in  = new BufferedReader(new InputStreamReader(inputStream,"UTF-8"));
        StringBuilder builder = new StringBuilder();
        String s = null;
        while ((s=in.readLine())!=null)
        {
            builder.append(s);
        }
        in.close();
        inputStream.close();

        //将xml格式的相应结果  转为 Map 格式
        Map<String, String> stringStringMap = WXPayUtil.xmlToMap(builder.toString());
        //验签
        //WXPay wxPay = new WXPay(wxPayConfig);
        //返回的结果会和自己的key 比较  验证是不是微信后台发送的请求
        boolean flag = wxpay.isResponseSignatureValid(stringStringMap);
        if(flag)
        {
            //判断状态
            String returnCode = stringStringMap.get("return_code");
            if(WXPayConstants.SUCCESS.equals(returnCode))
            {
                //判断业务结果
                String resultCode = stringStringMap.get("result_code");
                if(WXPayConstants.SUCCESS.equals(resultCode))
                {
                    //经行业务逻辑
                    //获取订单号
                    String outTradeNo = stringStringMap.get("out_trade_no");
                    //根据订单号查询出具体订单
                    ItripHotelOrder order = itripOrderService.getItripHotelOrderByOrderNo(outTradeNo);

                    //判断订单   和  tradeNO  空不空
                    if(EmptyUtils.isNotEmpty(order)&&order.getOrderStatus()==0)
                    {
                        //((int)(order.getPayAmount().doubleValue()*100))
                        if(Integer.parseInt(stringStringMap.get("total_fee"))==((int)(0.01D*100)))
                        {
                            String transactionId = stringStringMap.get("transaction_id");
                            order.setTradeNo(transactionId);
                            order.setPayType(2);
                            order.setOrderStatus(2);
                            itripOrderService.updateOrder(order);
                            System.out.println("=================交易完成"+transactionId);
                            //返回结果  标识处理完毕
                            Map<String,String> reply = new HashMap<>();
                            reply.put("return_code","SUCCESS");
                            reply.put("return_msg","OK");
                            response.setContentType("application/xml;charset=utf-8");
                            PrintWriter out = response.getWriter();
                            out.print(WXPayUtil.mapToXml(reply));
                            out.flush();
                            out.close();
                        }
                    }
                }
            }
        }
    }



    /**
     * 返回订单信息
     * @param orderNo
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "/queryorderstatus/{orderNo}",method = RequestMethod.GET,
            produces = "application/json")
    @ResponseBody
    public ServerResponse queryorderstatus(@PathVariable String orderNo)throws Exception {
        ItripHotelOrder order = itripOrderService.getItripHotelOrderByOrderNo(orderNo);

        if (EmptyUtils.isEmpty(orderNo)) {
            return ServerResponse.createByErrorMessage("订单号不能为空");
        }
        if (EmptyUtils.isEmpty(order)) {
            return ServerResponse.createByErrorMessage("订单号无效");
        }

        return ServerResponse.createBySuccess(order);
    }

}
