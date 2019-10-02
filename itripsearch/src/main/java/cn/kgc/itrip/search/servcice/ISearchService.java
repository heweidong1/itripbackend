package cn.kgc.itrip.search.servcice;

import cn.kgc.itrip.beans.common.ServerResponse;
import cn.kgc.itrip.beans.vo.SearchHotCityVO;
import cn.kgc.itrip.beans.vo.SearchHotelVO;
import org.apache.solr.client.solrj.SolrServerException;

import java.io.IOException;

public interface ISearchService
{
    /**
     * 根据热门城市查询酒店
     * @param searchHotCityVO
     * @return
     */
    ServerResponse searchItripHotelListByHotCity(SearchHotCityVO searchHotCityVO);

    ServerResponse searchItripHotelPage(SearchHotelVO searchHotelVO);
}
