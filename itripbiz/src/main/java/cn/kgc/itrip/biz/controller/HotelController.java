package cn.kgc.itrip.biz.controller;

import cn.kgc.itrip.beans.common.ServerResponse;
import cn.kgc.itrip.biz.service.IHotelService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping("/api/hotel")
public class HotelController
{
    @Autowired
    private IHotelService iHotelService;


    /**
     * 根据城市id 查询商圈
     * @param cityId
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "/querytradearea/{cityId}",method = RequestMethod.GET)
    @ResponseBody
    public ServerResponse querytradearea(@PathVariable Integer cityId) throws Exception
    {
        return iHotelService.queryTradeList(cityId);
    }
    /**
     * 查询酒店图片 [type==0]
     */
    @RequestMapping(value = "/getimg/{targetId}",method = RequestMethod.GET)
    @ResponseBody
    public ServerResponse getimg(@PathVariable Long targetId) throws Exception
    {
        return iHotelService.queryImageByTargerId(targetId,null);
    }

    /**
     * 根据酒店id查询酒店特色，商圈，酒店名称
     */
    @RequestMapping(value = "/getvideodesc/{hotelId}",method = RequestMethod.GET)
    @ResponseBody
    public ServerResponse getVideoDesc(@PathVariable Long hotelId) throws Exception
    {
        return iHotelService.queryHotelByHotelId(hotelId);
    }

    /**
     * 根据酒店id查询酒店设施
     */
    @RequestMapping(value = "/queryhotelfacilities/{id}",method = RequestMethod.GET)
    @ResponseBody
    public ServerResponse queryHotelFacilities(@PathVariable Long id) throws Exception
    {
        return iHotelService.queryHotelfacilitiesByHotelId(id);
    }

    /**
     * 查询酒店特色列表
     */
    @RequestMapping(value = "/queryhotelfeature",method = RequestMethod.GET)
    @ResponseBody
    public ServerResponse queryHotelFeature() throws Exception
    {
        return iHotelService.queryhotelfeature();
    }

    /**
     * 查询酒店特色列表
     */
    @RequestMapping(value = "/queryhotcity/{type}",method = RequestMethod.GET)
    @ResponseBody
    public ServerResponse queryhotcity(@PathVariable Integer type) throws Exception
    {
        return iHotelService.queryhotcity(type);
    }

    /**
     * 查询酒店的特色和介绍
     */
    @RequestMapping(value = "/queryhoteldetails/{id}",method = RequestMethod.GET)
    @ResponseBody
    public ServerResponse queryhoteldetails(@PathVariable Long id) throws Exception
    {
        return iHotelService.queryhoteldetails(id);
    }

    /**
     * 根据酒店id查询酒店政策
     */
    @RequestMapping(value = "/queryhotelpolicy/{id}",method = RequestMethod.GET)
    @ResponseBody
    public ServerResponse queryhotelpolicy(@PathVariable Long id) throws Exception
    {
        return iHotelService.queryhotelpolicy(id);
    }

}
