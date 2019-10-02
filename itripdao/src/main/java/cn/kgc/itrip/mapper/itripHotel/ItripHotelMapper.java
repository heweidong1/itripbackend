package cn.kgc.itrip.mapper.itripHotel;

import cn.kgc.itrip.beans.pojo.ItripHotel;
import org.apache.ibatis.annotations.Param;
import java.util.List;
import java.util.Map;

public interface ItripHotelMapper {

	public ItripHotel getItripHotelById(@Param(value = "id") Long id)throws Exception;

	public List<ItripHotel>	getItripHotelListByMap(Map<String,Object> param)throws Exception;

	public Integer getItripHotelCountByMap(Map<String,Object> param)throws Exception;

	public Integer insertItripHotel(ItripHotel itripHotel)throws Exception;

	public Integer updateItripHotel(ItripHotel itripHotel)throws Exception;

	public Integer deleteItripHotelById(@Param(value = "id") Long id)throws Exception;

	/**
	 * 查询酒店的设施
	 */
	public ItripHotel queryHotelfacilitiesByHotelId(Long HotelId)throws Exception;

}
