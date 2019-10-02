package cn.kgc.itrip.auth.controller;

import cn.kgc.itrip.auth.service.TokenService;
import cn.kgc.itrip.beans.common.ServerResponse;
import cn.kgc.itrip.beans.vo.ItripTokenVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;

@Controller
@RequestMapping("/api")
public class TokenController
{
    @Autowired
    private TokenService itripTokenVo;

    @RequestMapping(value = "retoken",method = RequestMethod.POST)
    @ResponseBody
    public ServerResponse reToken(HttpServletRequest request)throws Exception
    {
        String token = request.getHeader("token");
        String header = request.getHeader("user-agent");

        ServerResponse<ItripTokenVo> itripTokenVoServerResponse = itripTokenVo.replaceToken(header, token);
        return itripTokenVoServerResponse;
    }
}
