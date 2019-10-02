package cn.kgc.itrip.search.servcice.Impl;

import cn.kgc.itrip.beans.common.ServerResponse;
import cn.kgc.itrip.beans.vo.SearchHotCityVO;
import cn.kgc.itrip.beans.vo.SearchHotelVO;
import cn.kgc.itrip.search.dao.BaseDao;
import cn.kgc.itrip.search.model.Hotel;
import cn.kgc.itrip.search.servcice.ISearchService;
import cn.kgc.itrip.utils.EmptyUtils;
import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.client.solrj.SolrServerException;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;

@Service
public class SeathServiceImpl implements ISearchService
{

    @Autowired
    private BaseDao baseDao;

    @Override
    public ServerResponse searchItripHotelListByHotCity(SearchHotCityVO searchHotCityVO) {
        SolrQuery solrQuery=new SolrQuery("*:*");
        if(searchHotCityVO.getCityId()!=null)
        {
            solrQuery.setFilterQueries("cityId:"+searchHotCityVO.getCityId());
        }
        try {
            return baseDao.qieryData(solrQuery,null,null, Hotel.class);
        } catch (IOException | SolrServerException e) {
            e.printStackTrace();
            return ServerResponse.createByError();
        }
    }
    @Override
    public ServerResponse searchItripHotelPage(SearchHotelVO searchHotelVO)
    {

        if(searchHotelVO.getPageNo()==null||searchHotelVO.getPageNo()==0)
        {
            searchHotelVO.setPageNo(1);
        }

        if(searchHotelVO.getPageSize()==null||searchHotelVO.getPageSize()==0)
        {
            searchHotelVO.setPageSize(5);
        }
        //开始组装查询
        SolrQuery solrQuery = new SolrQuery("*:*");

        //查询目的地
        if(searchHotelVO.getDestination()!=null)
        {
            solrQuery.addFilterQuery("destination:"+searchHotelVO.getDestination());
        }else
        {
            solrQuery.addFilterQuery("destination:北京");
        }
        //在目的地后面查询关键字
        if(EmptyUtils.isNotEmpty(searchHotelVO.getKeywords()))
        {
            solrQuery.addFilterQuery("keyword:"+searchHotelVO.getKeywords());
        }

        //查询酒店的级别
        if(searchHotelVO.getHotelLevel() !=null && searchHotelVO.getHotelLevel()!=0)
        {
            solrQuery.addFilterQuery("hotelLevel:"+searchHotelVO.getHotelLevel());
        }

        //查询商圈
        if(EmptyUtils.isNotEmpty(searchHotelVO.getTradeAreaIds()))
        {
            solrQuery.addFilterQuery("tradingAreaIds:"+searchHotelVO.getTradeAreaIds());
        }

        //查询最低价和最高价
        if(EmptyUtils.isNotEmpty(searchHotelVO.getMaxPrice()))
        {
            solrQuery.addFilterQuery("minPrice:[ * TO "+searchHotelVO.getMaxPrice()+" ] ");
        }
        if(EmptyUtils.isNotEmpty(searchHotelVO.getMinPrice()))
        {
            solrQuery.addFilterQuery("minPrice:[ "+searchHotelVO.getMinPrice()+" TO * ]");
        }

        //查询酒店特色
        if(EmptyUtils.isNotEmpty(searchHotelVO.getFeatureIds()))
        {
            String[]featureIds = searchHotelVO.getFeatureIds().split(",");
            StringBuffer fqQuery = new StringBuffer();
            for(int i =0;i<featureIds.length;i++)
            {
                if(i==0)
                {
                    fqQuery.append("featureIds:*,"+featureIds[i]+",*");
                }else
                {
                    fqQuery.append(" OR featureIds:*,"+featureIds[i]+",*");
                }
            }

            solrQuery.addFilterQuery(fqQuery.toString());
        }
        if(EmptyUtils.isNotEmpty(searchHotelVO.getAscSort()))
        {
            solrQuery.addSort(searchHotelVO.getAscSort(), SolrQuery.ORDER.asc);
        }
        if(EmptyUtils.isNotEmpty(searchHotelVO.getDescSort()))
        {
            solrQuery.addSort(searchHotelVO.getAscSort(), SolrQuery.ORDER.desc);
        }
        if(EmptyUtils.isNotEmpty(searchHotelVO.getTradeAreaIds()))
        {
            String[]tradeAreaIds = searchHotelVO.getTradeAreaIds().split(",");
            StringBuffer fqQuery = new StringBuffer();
            for(int i =0;i<tradeAreaIds.length;i++)
            {
                if(i==0)
                {
                    fqQuery.append("tradingAreaIds:*,"+tradeAreaIds[i]+",*");
                }else
                {
                    fqQuery.append(" OR tradingAreaIds:*,"+tradeAreaIds[i]+",*");
                }
            }
            solrQuery.addFilterQuery(fqQuery.toString());
        }
        try {
            return baseDao.qieryData(solrQuery,searchHotelVO.getPageNo(),searchHotelVO.getPageSize(), Hotel.class);
        } catch (IOException | SolrServerException e) {
            e.printStackTrace();
            return ServerResponse.createByError();
        }
    }
}
