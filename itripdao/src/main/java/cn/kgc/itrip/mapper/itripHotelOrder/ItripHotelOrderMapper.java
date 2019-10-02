package cn.kgc.itrip.mapper.itripHotelOrder;

import cn.kgc.itrip.beans.pojo.ItripHotelOrder;
import cn.kgc.itrip.beans.pojo.ItripHotelTempStore;
import cn.kgc.itrip.beans.vo.OrderRoomDetailVO;
import org.apache.ibatis.annotations.Param;
import java.util.List;
import java.util.Map;

public interface ItripHotelOrderMapper {

	public ItripHotelOrder getItripHotelOrderById(@Param(value = "id") Long id)throws Exception;

	public List<ItripHotelOrder>	getItripHotelOrderListByMap(Map<String,Object> param)throws Exception;

	public Integer getItripHotelOrderCountByMap(Map<String,Object> param)throws Exception;

	public Integer insertItripHotelOrder(ItripHotelOrder itripHotelOrder)throws Exception;

	public Integer updateItripHotelOrder(ItripHotelOrder itripHotelOrder)throws Exception;

	public Integer deleteItripHotelOrderById(@Param(value = "id") Long id)throws Exception;

	public OrderRoomDetailVO getItripHotelOrderRoomInfoById(@Param(value = "orderId")Long orderId);

	public Integer changeOrderToComment()throws Exception;

	public Integer cancleOrderTimeOut();

	//存储过程
	public void flushStore(Map<String,Object> param)throws Exception;

	public List<ItripHotelTempStore> queryRoomStroe(Map<String,Object> param)throws Exception;

	public Integer getPatTypeByHotelId(@Param(value = "hotelId") Long hotelId)throws Exception;
}
