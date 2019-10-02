package cn.kgc.itrip.auth.controller;

import cn.kgc.itrip.auth.service.IUserService;
import cn.kgc.itrip.beans.common.ServerResponse;
import cn.kgc.itrip.beans.vo.ItripUserVO;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/api")
public class UserController
{

    @Autowired
    private IUserService iUserService;

    /**
     * 手机注册 发送短信
     */
    @RequestMapping(value = "/registerbyphone",method = RequestMethod.POST)
    @ResponseBody
    public ServerResponse registerbyphone(@RequestBody ItripUserVO itripUserVO)throws Exception
    {
        return iUserService.registerbyphone(itripUserVO);
    }


    /**
     * 激活操作
     */
    @RequestMapping(value = "/validatephone",method = RequestMethod.PUT)
    @ResponseBody
    public ServerResponse validatephone(@RequestParam("user") String user,@RequestParam("code") String code)throws Exception
    {
        return iUserService.validatephone(user,code);
    }

    /**
     * 邮箱注册
     */
    @RequestMapping(value = "/doregister",method = RequestMethod.POST )
    @ResponseBody
    public ServerResponse doregister(@RequestBody ItripUserVO itripUserVO)throws Exception
    {
        return  iUserService.doregister(itripUserVO);
    }

    /**
     * 邮箱验证，是否存在该账户
     */
    @RequestMapping(value = "/ckusr",method = RequestMethod.GET)
    @ResponseBody
    public ServerResponse ckusr(@RequestParam("name") String name) throws Exception
    {
        if(StringUtils.isBlank(name))
        {
            return ServerResponse.createByErrorMessage("参数丢失");
        }
        boolean b = iUserService.isuserCode(name);
        if(b)
        {
            return ServerResponse.createBySuccess();
        }else
        {
            return ServerResponse.createByErrorMessage("用户名存在");
        }
    }


    /**
     * 邮箱激活
     */
    @RequestMapping(value = "/activate",method = RequestMethod.PUT)
    @ResponseBody
    public ServerResponse activate(@RequestParam("user") String user,@RequestParam("code") String code) throws Exception
    {
        return iUserService.validatephone(user,code);
    }
}
