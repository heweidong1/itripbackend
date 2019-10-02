package cn.kgc.itrip.biz.controller;

import cn.kgc.itrip.beans.common.ServerResponse;
import cn.kgc.itrip.beans.vo.SearchHotelRoomVO;
import cn.kgc.itrip.biz.service.IHotelService;
import cn.kgc.itrip.biz.service.ItripHotelRoomService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

@Controller
@RequestMapping("/api/hotelroom")
public class HotelRoomController
{

   @Autowired
   private ItripHotelRoomService itripHotelRoomService;

    @Autowired
    private IHotelService iHotelService;

    @RequestMapping(value = "/queryhotelroombyhotel",method = RequestMethod.POST,produces = "application/json")
    @ResponseBody
    public ServerResponse queryHotelRoomByHotel(@RequestBody SearchHotelRoomVO searchHotelRoomVO)throws Exception
    {
        return  itripHotelRoomService.queryHotelRoomByHotel(searchHotelRoomVO);
    }

    @RequestMapping(value = "/queryhotelroombed",method = RequestMethod.GET,produces = "application/json")
    @ResponseBody
    public ServerResponse queryHotelRoombed()throws Exception {
        return itripHotelRoomService.queryHotelRoombed();
    }

    /**
     * 查询酒店图片 [type==0]
     */
    @RequestMapping(value = "/getimg/{targetId}",method = RequestMethod.GET)
    @ResponseBody
    public ServerResponse getimg(@PathVariable Long targetId) throws Exception
    {
        return iHotelService.queryImageByTargerId(targetId,"1");
    }



}
