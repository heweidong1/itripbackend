package cn.kgc.itrip.biz.service.Impl;

import cn.kgc.itrip.beans.common.ServerResponse;
import cn.kgc.itrip.beans.pojo.ItripHotelRoom;
import cn.kgc.itrip.beans.pojo.ItripLabelDic;
import cn.kgc.itrip.beans.vo.ItripLabelDicVO;
import cn.kgc.itrip.beans.vo.SearchHotelRoomVO;
import cn.kgc.itrip.biz.service.ItripHotelRoomService;
import cn.kgc.itrip.mapper.itripHotelRoom.ItripHotelRoomMapper;
import cn.kgc.itrip.mapper.itripLabelDic.ItripLabelDicMapper;
import cn.kgc.itrip.utils.DateUtil;
import cn.kgc.itrip.utils.EmptyUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class itripHotelRoomServiceImpl implements ItripHotelRoomService
{

    @Autowired
    private ItripHotelRoomMapper itripHotelRoomMapper;

    @Autowired
    private ItripLabelDicMapper itripLabelDicMapper;


    @Override
    public ServerResponse queryHotelRoomByHotel(SearchHotelRoomVO searchHotelRoomVO) throws Exception {
        if(searchHotelRoomVO.getHotelId()==null||searchHotelRoomVO.getHotelId()==0)
        {
            return ServerResponse.createByErrorMessage("酒店id不能为空");
        }
        if(searchHotelRoomVO.getStartDate()==null||searchHotelRoomVO.getEndDate()==null)
        {
            return ServerResponse.createByErrorMessage("入住时间不能为空");
        }
        if(searchHotelRoomVO.getStartDate().getTime()>searchHotelRoomVO.getEndDate().getTime())
        {
            return ServerResponse.createByErrorMessage("入住时间不能大于退房时间");
        }

        Map<String,Object> param=new HashMap<>();
        param.put("hotelId",searchHotelRoomVO.getHotelId());
        param.put("roomBedTypeId",searchHotelRoomVO.getRoomBedTypeId());
        param.put("isHavingBreakfast",searchHotelRoomVO.getIsHavingBreakfast());
        param.put("isBook",searchHotelRoomVO.getIsBook());
        param.put("isCancel",searchHotelRoomVO.getIsCancel());
        param.put("isTimelyResponse",searchHotelRoomVO.getIsTimelyResponse());
        param.put("payType",
                searchHotelRoomVO.getPayType()==null||
                        searchHotelRoomVO.getPayType()==3?null:searchHotelRoomVO.getPayType());

        //得到日历得区间 用户选择得日期区间
        List<Date> dateList =
                DateUtil.getBetweenDates(searchHotelRoomVO.getStartDate(),
                        searchHotelRoomVO.getEndDate());

        param.put("timelist",dateList);

        List<ItripHotelRoom> itripHotelRoomListByMap = itripHotelRoomMapper.getItripHotelRoomListByMap(param);
        List<List<ItripHotelRoom>> ouput = new ArrayList<>();

        if(EmptyUtils.isNotEmpty(itripHotelRoomListByMap))
        {
            for(ItripHotelRoom room:itripHotelRoomListByMap)
            {
                List<ItripHotelRoom> list = new ArrayList<>();
                list.add(room);
                ouput.add(list);
            }
        }
        return ServerResponse.createBySuccess(ouput);
    }

    @Override
    public ServerResponse queryHotelRoombed() throws Exception
    {
        Map<String,Object> param = new HashMap<>();
        List<ItripLabelDicVO> ouput = new ArrayList<>();
        param.put("parentId",1);
        List<ItripLabelDic> itripLabelDicListByMap = itripLabelDicMapper.getItripLabelDicListByMap(param);
        for(ItripLabelDic dic:itripLabelDicListByMap)
        {
            ItripLabelDicVO vo = new ItripLabelDicVO();
            BeanUtils.copyProperties(dic,vo);
            ouput.add(vo);
        }
        return ServerResponse.createBySuccess(ouput);
    }
}
