package cn.kgc.itrip.auth.controller;

import cn.kgc.itrip.auth.service.IUserService;
import cn.kgc.itrip.auth.service.TokenService;
import cn.kgc.itrip.beans.common.ServerResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;

@Controller
@RequestMapping("/api")
public class LoginController
{
    @Autowired
    private IUserService iUserService;

    @Autowired
    private TokenService tokenService;

    @RequestMapping(value = "/dologin",method = RequestMethod.POST)
    @ResponseBody
    public ServerResponse dologin(String name, String password, HttpServletRequest request) throws Exception
    {
        String agent= request.getHeader("user-agent");

        return iUserService.userLogin(name,password,agent);
    }

    @RequestMapping(value = "/logout",method = RequestMethod.GET)
    @ResponseBody
    public ServerResponse logout(HttpServletRequest request) throws Exception
    {
        String token = request.getHeader("token");
        return tokenService.delToken(token);
    }
}
