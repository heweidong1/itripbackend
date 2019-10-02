package cn.kgc.itrip.search.controller;

import cn.kgc.itrip.beans.common.ServerResponse;
import cn.kgc.itrip.beans.vo.SearchHotCityVO;
import cn.kgc.itrip.beans.vo.SearchHotelVO;
import cn.kgc.itrip.search.servcice.ISearchService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping("/api/hotellist")
public class HotelListController
{

    @Autowired
    private ISearchService iSearchService;

    @RequestMapping(value = "/searchItripHotelListByHotCity",method = RequestMethod.POST,produces = "application/json")
    @ResponseBody
    public ServerResponse searchItripHotelListByHotCity(@RequestBody SearchHotCityVO searchHotCityVO)
    {
        return iSearchService.searchItripHotelListByHotCity(searchHotCityVO);
    }


    @RequestMapping(value = "/searchItripHotelPage",method = RequestMethod.POST,produces = "application/json")
    @ResponseBody
    public ServerResponse searchItripHotelPage(@RequestBody SearchHotelVO searchHotelVO)
    {
        return iSearchService.searchItripHotelPage(searchHotelVO);
    }
}
