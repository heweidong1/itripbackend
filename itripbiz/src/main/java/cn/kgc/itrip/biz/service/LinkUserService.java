package cn.kgc.itrip.biz.service;

import cn.kgc.itrip.beans.common.ServerResponse;
import cn.kgc.itrip.beans.vo.ItripAddUserLinkUserVO;
import cn.kgc.itrip.beans.vo.ItripModifyUserLinkUserVO;
import cn.kgc.itrip.beans.vo.ItripSearchUserLinkUserVO;

public interface LinkUserService {
    public ServerResponse addLinkUser(ItripAddUserLinkUserVO linkUser, String token)throws Exception;
    public ServerResponse linkDel(Long[]ids, String token)throws Exception;
    public ServerResponse queryLinkUser(ItripSearchUserLinkUserVO itripSearchUserLinkUserVO, String token ) throws Exception;
    public ServerResponse updateLinkUser(ItripModifyUserLinkUserVO itripModifyUserLinkUserVO, String token)throws Exception;
}
