package cn.kgc.itrip.biz.service.Impl;

import cn.kgc.itrip.beans.common.ResponseCode;
import cn.kgc.itrip.beans.common.ServerResponse;
import cn.kgc.itrip.beans.pojo.ItripAreaDic;
import cn.kgc.itrip.beans.pojo.ItripHotel;
import cn.kgc.itrip.beans.pojo.ItripLabelDic;
import cn.kgc.itrip.beans.vo.*;
import cn.kgc.itrip.biz.service.IHotelService;
import cn.kgc.itrip.mapper.itripAreaDic.ItripAreaDicMapper;

import cn.kgc.itrip.mapper.itripHotel.ItripHotelMapper;
import cn.kgc.itrip.mapper.itripImage.ItripImageMapper;
import cn.kgc.itrip.mapper.itripLabelDic.ItripLabelDicMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@Slf4j
public class HotelServiceImpl implements IHotelService {

    @Autowired
    private ItripAreaDicMapper itripAreaDicMapper;

    @Autowired
    private ItripImageMapper itripImageMapper;

    @Autowired
    private ItripHotelMapper itripHotelMapper;

    @Autowired
    private ItripLabelDicMapper itripLabelDicMapper;


    @Override
    public ServerResponse<List<ItripTradeAreaVO>> queryTradeList(Integer cityId) throws Exception {

        if(cityId==null||cityId==0)
        {
            return ServerResponse.createByErrorMessage(ResponseCode.ILLEGAL_ARGUMENT.getDesc());
        }
        List<ItripTradeAreaVO> itripTradeAreaVOS = itripAreaDicMapper.queryTradeArea(cityId);
        return ServerResponse.createBySuccess(itripTradeAreaVOS);
    }

    @Override
    public ServerResponse<List<ItripHotelImageVO>> queryImageByTargerId(Long targetId, String type)throws Exception
    {
        if(StringUtils.isEmpty(type))
        {
            type="0";
        }
        List<ItripHotelImageVO> itripHotelImageVOS = itripImageMapper.queryHotelImage(targetId, type);
        return ServerResponse.createBySuccess(itripHotelImageVOS);

    }

    @Override
    public ServerResponse queryHotelByHotelId(Long hotelId) throws Exception
    {
        HotelVideoDescVO hotelVideoDescVO=new HotelVideoDescVO();
        if(hotelId==null)
        {
            return ServerResponse.createByErrorMessage("酒店id不能为空");
        }
        try
        {
            //获取特色
            List<ItripAreaDic> itripAreaDics = itripAreaDicMapper.queryItripHotelFeatureByHotelId(hotelId);
            List<String> featureLsit = new ArrayList<>();
            for(ItripAreaDic trade:itripAreaDics)
            {
                featureLsit.add(trade.getName());
            }
            hotelVideoDescVO.setHotelFeatureList(featureLsit);

            //获取商圈
            List<ItripTradeAreaVO> itripTradeAreaVOS1 = itripAreaDicMapper.queryTradeAreaByHotelId(hotelId);
            List<String> areaNameLsit = new ArrayList<>();
            for(ItripTradeAreaVO trade:itripTradeAreaVOS1)
            {
                areaNameLsit.add(trade.getName());
            }
            hotelVideoDescVO.setTradingAreaNameList(areaNameLsit);

            //h获取酒店名字
            ItripHotel itripHotel = itripAreaDicMapper.queryItripHotelByHotelId(hotelId);
            hotelVideoDescVO.setHotelName(itripHotel.getHotelName());

            return ServerResponse.createBySuccess(hotelVideoDescVO);
        }catch (Exception e)
        {
            e.printStackTrace();
            return ServerResponse.createByErrorMessage("获取酒店信息失败");
        }

    }


    @Override
    public ServerResponse queryHotelfacilitiesByHotelId(Long HotelId)throws Exception
    {
        ItripHotel itripHotel = itripHotelMapper.queryHotelfacilitiesByHotelId(HotelId);
        if(itripHotel==null)
        {
            return ServerResponse.createByErrorMessage("获取失败");
        }
        return ServerResponse.createBySuccess(itripHotel.getFacilities());
    }

    /**
     * 查询酒店特色
     * @return
     * @throws Exception
     */
    @Override
    public ServerResponse queryhotelfeature() throws Exception {
        Map<String,Object> param = new HashMap<>();
        param.put("parentId",16);
        List<ItripLabelDic> itripLabelDicListByMap = itripLabelDicMapper.getItripLabelDicListByMap(param);
        return ServerResponse.createBySuccess(itripLabelDicListByMap);
    }

    @Override
    public ServerResponse queryhotcity(Integer type)throws Exception
    {
        Map<String,Object> param = new HashMap<>();
        param.put("isChina",type);
        param.put("isHot",1);
        List<ItripAreaDic> itripAreaDicListByMap = itripAreaDicMapper.getItripAreaDicListByMap(param);
        if(itripAreaDicListByMap==null)
        {
            return ServerResponse.createByErrorMessage("获取失败");
        }
        return ServerResponse.createBySuccess(itripAreaDicListByMap);
    }

    /**
     * 根据酒店id查询特色和介绍
     */
    @Override
    public ServerResponse queryhoteldetails(Long id)throws Exception
    {
        List<ItripAreaDic> itripAreaDics = itripAreaDicMapper.queryItripHotelFeatureByHotelId(id);
        List<ItripSearchDetailsHotelVO> ouput = new ArrayList<>();

        //封装特色
        for(ItripAreaDic itripAreaDic:itripAreaDics)
        {
            ItripSearchDetailsHotelVO itripSearchDetailsHotelVO=new ItripSearchDetailsHotelVO();
            BeanUtils.copyProperties(itripAreaDic,itripSearchDetailsHotelVO);
            ouput.add(itripSearchDetailsHotelVO);
        }
        //封装介绍
        ItripHotel itripHotelById = itripHotelMapper.getItripHotelById(id);
        ItripSearchDetailsHotelVO itripSearchDetailsHotelVO=new ItripSearchDetailsHotelVO();
        itripSearchDetailsHotelVO.setName("酒店介绍");
        itripSearchDetailsHotelVO.setDescription(itripHotelById.getDetails());
        ouput.add(itripSearchDetailsHotelVO);
        return ServerResponse.createBySuccess(ouput);
    }

    /**
     * 根据酒店id查询酒店政策
     */
    @Override
    public ServerResponse queryhotelpolicy(Long id)throws Exception
    {
        if(id==null)
        {
            return ServerResponse.createByErrorMessage("酒店id不能为空");
        }

        ItripHotel itripHotelById = itripHotelMapper.getItripHotelById(id);
        if(itripHotelById==null)
        {
            return ServerResponse.createByErrorMessage("获取失败");
        }
        ItripSearchFacilitiesHotelVO itripSearchFacilitiesHotelVO=new ItripSearchFacilitiesHotelVO();
        itripSearchFacilitiesHotelVO.setFacilities(itripHotelById.getHotelPolicy());
        return ServerResponse.createBySuccess(itripSearchFacilitiesHotelVO);
    }
}
