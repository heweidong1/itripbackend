package cn.kgc.itrip.auth.service;

import cn.kgc.itrip.beans.common.ServerResponse;
import cn.kgc.itrip.beans.vo.ItripUserVO;

public interface IUserService
{
    public ServerResponse userLogin(String userCode, String password, String userAgent) throws Exception ;

    public ServerResponse registerbyphone(ItripUserVO itripUserVO)throws Exception;

    public ServerResponse validatephone(String user,String code)throws Exception;

    public ServerResponse doregister(ItripUserVO itripUserVO)throws Exception;

    public boolean isuserCode(String usercode) throws Exception;
}
