package cn.kgc.itrip.biz.controller;

import cn.kgc.itrip.beans.common.ServerResponse;
import cn.kgc.itrip.beans.vo.ItripAddUserLinkUserVO;
import cn.kgc.itrip.beans.vo.ItripModifyUserLinkUserVO;
import cn.kgc.itrip.beans.vo.ItripSearchUserLinkUserVO;
import cn.kgc.itrip.biz.service.LinkUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

@Controller
@RequestMapping("/api/userinfo")
public class UserInfoController
{

    @Autowired
    private LinkUserService linkUserService;



    @RequestMapping(value = "/adduserlinkuser",method = RequestMethod.POST )
    @ResponseBody
    public ServerResponse queryhotelpolicy(@RequestBody ItripAddUserLinkUserVO itripAddUserLinkUserVO, HttpServletRequest request) throws Exception
    {
        return linkUserService.addLinkUser(itripAddUserLinkUserVO,request.getHeader("token"));

    }


    @RequestMapping(value = "/deluserlinkuser",method = RequestMethod.GET)
    @ResponseBody
    public ServerResponse deluserlinkuser(Long[]ids, HttpServletRequest request) throws Exception
    {
        return linkUserService.linkDel(ids,request.getHeader("token"));
    }

    /**
     * 查询常用联系人接口
     */
    @RequestMapping(value = "/queryuserlinkuser",method = RequestMethod.POST,produces = "application/json")
    @ResponseBody
    public ServerResponse queryuserlinkuser(@RequestBody ItripSearchUserLinkUserVO itripSearchUserLinkUserVO, HttpServletRequest request) throws Exception
    {
        return linkUserService.queryLinkUser(itripSearchUserLinkUserVO,request.getHeader("token"));
    }

    /**
     * 修改常用联系人
     * @param itripModifyUserLinkUserVO
     * @param request
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "/modifyuserlinkuser",method = RequestMethod.POST)
    @ResponseBody
    public ServerResponse modifyuserlinkuser(@RequestBody ItripModifyUserLinkUserVO itripModifyUserLinkUserVO, HttpServletRequest request) throws Exception
    {
        return linkUserService.updateLinkUser(itripModifyUserLinkUserVO,request.getHeader("token"));
    }
}
