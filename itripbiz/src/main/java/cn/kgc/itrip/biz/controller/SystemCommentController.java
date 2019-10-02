package cn.kgc.itrip.biz.controller;

import cn.kgc.itrip.beans.common.ServerResponse;
import cn.kgc.itrip.beans.vo.ItripAddCommentVO;
import cn.kgc.itrip.beans.vo.ItripSearchCommentVO;
import cn.kgc.itrip.biz.service.IHotelService;
import cn.kgc.itrip.biz.service.SystemCommentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

@Controller
@RequestMapping("/api/comment")
public class SystemCommentController
{

    @Autowired
    private SystemCommentService systemCommentService;

    @Autowired
    private IHotelService iHotelService;

    /**
     * 查询出出游类型列表
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "/gettraveltype",method= RequestMethod.GET,produces = "application/json")
    @ResponseBody
    public ServerResponse getTravelType()throws Exception
    {
        return systemCommentService.getTravelType();
    }

    /**
     * 获取评论图片
     * @param targetId
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "/getimg/{targetId}" ,method = RequestMethod.GET,produces = "application/json")
    @ResponseBody
    public ServerResponse getImg(@PathVariable Long targetId)throws Exception
    {
        return iHotelService.queryImageByTargerId(targetId,"2");
    }


    /**
     * 图片上传
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "/upload" ,method = RequestMethod.POST,produces = "application/json")
    @ResponseBody
    public ServerResponse upload(HttpServletRequest request)throws Exception
    {
        return systemCommentService.upLoad(request);
    }

    /**
     * 图片删除
     */
    //删除已经保存的文件
    @RequestMapping(value = "/delpic",method = RequestMethod.POST,produces = "application/json")
    @ResponseBody
    public ServerResponse delpic(@RequestParam String imgName, HttpServletRequest request)throws Exception
    {
        return systemCommentService.delpic(imgName,request);
    }


    /**
     * 添加评论
     */
    @RequestMapping(value = "/add",method = RequestMethod.POST,produces = "application/json")
    @ResponseBody
    public ServerResponse addComment(@RequestBody ItripAddCommentVO itripAddCommentVO, HttpServletRequest request)throws Exception
    {
        return systemCommentService.add(itripAddCommentVO,request);
    }

    /**
     * 显示评论
     * @param itripSearchCommentVO
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "/getcommentlist" ,method = RequestMethod.POST,produces = "application/json")
    @ResponseBody
    public ServerResponse getCommentList(@RequestBody ItripSearchCommentVO itripSearchCommentVO)throws Exception
    {
        return systemCommentService.getCommentList(itripSearchCommentVO);
    }



    /**
     * 获取酒店相关信息 酒店名称 酒店星级
     */
    @RequestMapping(value = "/gethoteldesc/{hotelId}",method= RequestMethod.GET,produces = "application/json")
    @ResponseBody
    public ServerResponse getHotelDesc(@PathVariable Long hotelId)throws Exception
    {
        return systemCommentService.getHotelDesc(hotelId);
    }

    /**
     * 查询评价分
     * @param hotelId
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "/gethotelscore/{hotelId}",method= RequestMethod.GET,produces = "application/json")
    @ResponseBody
    public ServerResponse gethotelscore(@PathVariable Long hotelId)throws Exception
    {
        return systemCommentService.gethotelscore(hotelId);
    }

    /**
     * 根据酒店id查询各类评论数量
     */
    @RequestMapping(value = "/getcount/{hotelId}",method= RequestMethod.GET,produces = "application/json")
    @ResponseBody
    public ServerResponse getCommentCountByType(@PathVariable Long hotelId)throws Exception
    {
        return systemCommentService.getCount(hotelId);
    }
}
