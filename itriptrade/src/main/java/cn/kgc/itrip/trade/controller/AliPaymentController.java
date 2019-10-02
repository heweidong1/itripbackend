package cn.kgc.itrip.trade.controller;

import cn.kgc.itrip.beans.pojo.ItripHotelOrder;
import cn.kgc.itrip.trade.config.AlipayConfig;
import cn.kgc.itrip.trade.service.ItripOrderService;
import cn.kgc.itrip.utils.EmptyUtils;
import com.alipay.api.AlipayClient;
import com.alipay.api.DefaultAlipayClient;
import com.alipay.api.internal.util.AlipaySignature;
import com.alipay.api.request.AlipayTradePagePayRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

@Controller
@RequestMapping("/api")
public class AliPaymentController
{

    @Autowired
    private ItripOrderService itripOrderService;

    @Autowired
    private AlipayConfig alipayConfig;

    /**
     * 跳转支付页面
     * @param orderNo
     * @param model
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "/prepay/{orderNo}",method = RequestMethod.GET)
    public String prepay(@PathVariable("orderNo") String orderNo, Model model)throws Exception
    {

        //获取订单
        ItripHotelOrder itripHotelOrderByOrderNo = itripOrderService.getItripHotelOrderByOrderNo(orderNo);

        if(itripHotelOrderByOrderNo==null)
        {
            return "/notfound.jsp";
        }
        model.addAttribute("hotelName",itripHotelOrderByOrderNo.getHotelName());
        model.addAttribute("roomId",itripHotelOrderByOrderNo.getRoomId());
        model.addAttribute("count",itripHotelOrderByOrderNo.getCount());
        model.addAttribute("payAmount",itripHotelOrderByOrderNo.getPayAmount());
        model.addAttribute("orderNo",orderNo);

        return "/pay.jsp";
    }



    /**
     * 支付功能实现 会提交到ali服务器  相当于一个表单
     * 设计思路 传递订单号  其他信息 比如金额等比较敏感的信息，可以来自数据库，安全性会更好些
     */
    @RequestMapping(value = "/pay",method = RequestMethod.POST,produces = "application/xml",
            consumes = "application/x-www-form-urlencoded")
    public void pay(@RequestParam String WIDout_trade_no,
                    @RequestParam String WIDsubject,
                    @RequestParam String WIDtotal_amount, HttpServletResponse response)throws Exception
    {

        /**
         * WIDout_trade_no   订单编号
         * WIDsubject  订单名称
         * WIDtotal_amount  订单金额
         */


        //获得初始化的AlipayClient  获取支付连接
        AlipayClient alipayClient = new DefaultAlipayClient(
                alipayConfig.getUrl(), alipayConfig.getAppID(),
                alipayConfig.getRsaPrivateKey(),
                "json", alipayConfig.getCharset(),
                alipayConfig.getAlipayPublicKey(),
                alipayConfig.getSignType());


        //设置请求参数
        AlipayTradePagePayRequest alipayRequest = new AlipayTradePagePayRequest();
        alipayRequest.setReturnUrl(alipayConfig.getReturnUrl());
        //aili 回调接口
        alipayRequest.setNotifyUrl(alipayConfig.getNotifyUrl());

        // 放置参数
        alipayRequest.setBizContent("{\"out_trade_no\":\""+ WIDout_trade_no +"\","
                + "\"total_amount\":\""+ WIDtotal_amount +"\","
                + "\"subject\":\""+ WIDsubject +"\","
                //+ "\"body\":\""+ body +"\","
                + "\"product_code\":\"FAST_INSTANT_TRADE_PAY\"}");

        //请求 会得到一个 form 表单 <form action=""> </form> document.forms[0].submit()
        String body = alipayClient.pageExecute(alipayRequest).getBody();

        //输出
        response.setContentType("text/html;charset=utf-8");
        PrintWriter writer = response.getWriter();
        writer.println(body);

        writer.flush();
        writer.close();

    }

    /**
     * 自动调用此方法 想客户展示提交成功
     * @param request
     * @param response
     * @throws Exception
     */

    @RequestMapping(value = "/return",method = RequestMethod.GET)
    public void callback(HttpServletRequest request, HttpServletResponse response)throws Exception
    {
        //获取支付宝GET过来反馈信息
        //params 封装提交过来的数据
        Map<String,String> params = new HashMap<String,String>();
        Map<String,String[]> requestParams = request.getParameterMap();
        for (Iterator<String> iter = requestParams.keySet().iterator(); iter.hasNext();) {
            String name = (String) iter.next();
            String[] values = (String[]) requestParams.get(name);
            String valueStr = "";
            for (int i = 0; i < values.length; i++) {
                valueStr = (i == values.length - 1) ? valueStr + values[i]
                        : valueStr + values[i] + ",";
            }
            //乱码解决，这段代码在出现乱码时使用
            valueStr = new String(valueStr.getBytes("ISO-8859-1"), "utf-8");
            params.put(name, valueStr);
        }

        //验证签名
        boolean signVerified = AlipaySignature.rsaCheckV1(params,
                alipayConfig.getAlipayPublicKey(),
                alipayConfig.getCharset(),
                //调用SDK验证签名
                alipayConfig.getSignType());

        if(signVerified)
        {
            //获取数据
            //商户订单号
            String out_trade_no = new String(request.getParameter("out_trade_no").getBytes("ISO-8859-1"), "UTF-8");

            //支付宝交易号
            String trade_no = new String(request.getParameter("trade_no").getBytes("ISO-8859-1"), "UTF-8");

            //获取appID
            String appId = params.get("app_id");
            ItripHotelOrder itripHotelOrderByOrderNo = itripOrderService.getItripHotelOrderByOrderNo(out_trade_no);
            if(EmptyUtils.isNotEmpty(itripHotelOrderByOrderNo))
            {
                if(alipayConfig.getAppID().equals(appId))
                {
                    //判断订单是否已经修改 通过是否已经被支付
                    if(itripHotelOrderByOrderNo.getOrderStatus()==0)
                    {
                        //此处代表支付成功 修改订单
                        itripHotelOrderByOrderNo.setOrderStatus(2);
                        itripHotelOrderByOrderNo.setPayType(1);
                        itripHotelOrderByOrderNo.setTradeNo(trade_no);
                        itripOrderService.updateOrder(itripHotelOrderByOrderNo);
                    }
                    response.sendRedirect(String.format(alipayConfig.getPaymentSuccessUrl(),
                            out_trade_no,itripHotelOrderByOrderNo.getId().toString()));
                    return;
                }
            }
        }
        response.sendRedirect(alipayConfig.getPaymentFailureUrl());
    }

    /**
     * 支付宝自动调用接口 返回真正的处理结果
     */
    @RequestMapping(value = "/notify",method = RequestMethod.POST)
    public void processOrderNotify(HttpServletRequest request,HttpServletResponse response)throws Exception {
        //获取支付宝POST过来反馈信息
        Map<String, String> params = new HashMap<String, String>();
        Map<String, String[]> requestParams = request.getParameterMap();
        for (Iterator<String> iter = requestParams.keySet().iterator(); iter.hasNext(); ) {
            String name = (String) iter.next();
            String[] values = (String[]) requestParams.get(name);
            String valueStr = "";
            for (int i = 0; i < values.length; i++) {
                valueStr = (i == values.length - 1) ? valueStr + values[i]
                        : valueStr + values[i] + ",";
            }
            params.put(name, valueStr);
        }

        //调用SDK验证签名
        boolean signVerified = AlipaySignature.rsaCheckV1(params,
                alipayConfig.getAlipayPublicKey(),
                alipayConfig.getCharset(),
                alipayConfig.getSignType());
        String receipt = null;
        if(signVerified)
        {
            //商户订单号
            String out_trade_no = new String(request.getParameter("out_trade_no").getBytes("ISO-8859-1"),"UTF-8");

            //支付宝交易号
            String trade_no = new String(request.getParameter("trade_no").getBytes("ISO-8859-1"),"UTF-8");

            //交易状态
            String trade_status = new String(request.getParameter("trade_status").getBytes("ISO-8859-1"),"UTF-8");

            String appId = params.get("app_id");

            ItripHotelOrder order = itripOrderService.getItripHotelOrderByOrderNo(out_trade_no);

            if(EmptyUtils.isNotEmpty(order))
            {
                if (alipayConfig.getAppID().equals(appId))
                {
                    //代表交易完成
                    if (trade_status.equals("TRADE_FINISHED")) {
                    if(order.getOrderStatus()==0)
                    {
                        //处理订单
                        order.setPayType(1);
                        order.setOrderStatus(2);
                        order.setTradeNo(trade_no);
                        itripOrderService.updateOrder(order);
                    }
                    //交易成功
                } else if (trade_status.equals("TRADE_SUCCESS"))
                    {
                        if(order.getOrderStatus()==0)
                        {
                            //处理订单
                            order.setPayType(1);
                            order.setOrderStatus(2);
                            order.setTradeNo(trade_no);
                            itripOrderService.updateOrder(order);
                        }
                    }
                    receipt="success";
                }
            }
        }else
        {
            receipt="fail";
        }
        response.setContentType("text/html;charset=utf-8");
        PrintWriter writer = response.getWriter();
        writer.print(receipt);
        writer.flush();
        writer.close();
    }
}
