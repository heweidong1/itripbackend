package cn.kgc.itrip.mapper.itripUser;

import cn.kgc.itrip.beans.pojo.ItripUser;
import org.apache.ibatis.annotations.Param;
import java.util.List;
import java.util.Map;

public interface ItripUserMapper {

	public ItripUser getItripUserById(@Param(value = "id") Long id)throws Exception;

	public List<ItripUser>	getItripUserListByMap(Map<String,Object> param)throws Exception;

	public Integer getItripUserCountByMap(Map<String,Object> param)throws Exception;

	public Integer insertItripUser(ItripUser itripUser)throws Exception;

	public Integer updateItripUser(ItripUser itripUser)throws Exception;

	public Integer deleteItripUserById(@Param(value = "id") Long id)throws Exception;

	/**
	 * 根据用户名字查询用户信息 是否存在
	 * @param userCode
	 * @return
	 * @throws Exception
	 */
	Integer queryUserByUserCode(String userCode)throws Exception;

	/**
	 * 根据用户名和密码来确定唯一用户
	 * @param userCode 用户名
	 * @param password 密码
	 * @return 返回
	 * @throws Exception
	 */
	ItripUser queryUserBuUserCodeAndPassword(String userCode,String password)throws  Exception;

	/**
	 * 根据userCode 查询用户
	 */
	ItripUser queryUserIsExistsByUserCode(String usercode)throws Exception;


	/**
	 * 根据usercode删除记录
	 *
	 */
	Integer deleUserByCode(String usercode)throws Exception;
}
