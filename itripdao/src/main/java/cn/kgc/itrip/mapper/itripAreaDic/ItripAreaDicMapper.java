package cn.kgc.itrip.mapper.itripAreaDic;

import cn.kgc.itrip.beans.pojo.ItripAreaDic;
import cn.kgc.itrip.beans.pojo.ItripHotel;
import cn.kgc.itrip.beans.pojo.ItripHotelFeature;
import cn.kgc.itrip.beans.vo.ItripTradeAreaVO;
import org.apache.ibatis.annotations.Param;
import java.util.List;
import java.util.Map;

public interface ItripAreaDicMapper {

	public ItripAreaDic getItripAreaDicById(@Param(value = "id") Long id)throws Exception;

	public List<ItripAreaDic>	getItripAreaDicListByMap(Map<String,Object> param)throws Exception;

	public Integer getItripAreaDicCountByMap(Map<String,Object> param)throws Exception;

	public Integer insertItripAreaDic(ItripAreaDic itripAreaDic)throws Exception;

	public Integer updateItripAreaDic(ItripAreaDic itripAreaDic)throws Exception;

	public Integer deleteItripAreaDicById(@Param(value = "id") Long id)throws Exception;

	/**
	 * 根据城市id 来查询当地的商圈
	 * @param cityId 城市id
	 * @return 商圈
	 * @throws Exception
	 */
	public List<ItripTradeAreaVO> queryTradeArea(Integer cityId)throws Exception;

	/**
	 * 根据酒店id查询商圈
	 */
	public List<ItripTradeAreaVO> queryTradeAreaByHotelId(Long HotelId);


	/**
	 * 查修酒店特色
	 * @param HotelId
	 * @return
	 * @throws Exception
	 */
	public List<ItripAreaDic> queryItripHotelFeatureByHotelId(Long HotelId)throws Exception;

	/**
	 * 查询酒店的名字
	 */
	public ItripHotel queryItripHotelByHotelId(Long HotelId);


}
