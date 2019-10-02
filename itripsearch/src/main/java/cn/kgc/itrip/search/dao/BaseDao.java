package cn.kgc.itrip.search.dao;

import cn.kgc.itrip.beans.common.ServerResponse;
import cn.kgc.itrip.search.model.Hotel;
import cn.kgc.itrip.utils.Page;
import cn.kgc.itrip.utils.PropertiesUtil;
import org.apache.solr.client.solrj.SolrClient;
import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.client.solrj.impl.HttpSolrClient;
import org.apache.solr.client.solrj.impl.XMLResponseParser;
import org.apache.solr.client.solrj.response.QueryResponse;
import org.apache.solr.common.SolrDocumentList;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Component
public class BaseDao
{
    /**
     * solr连接对象
     */
    private HttpSolrClient solrClient;

    public BaseDao()
    {
        solrClient=new HttpSolrClient(PropertiesUtil.getProperty("solr.url"));
        solrClient.setConnectionTimeout(500);
        //设置格式样式
        solrClient.setParser(new XMLResponseParser());
    }
    public ServerResponse qieryData(SolrQuery solrQuery, Integer pageNo, Integer pageSize, Class clazz) throws IOException, SolrServerException {
        if(pageNo==null||pageSize==null)
        {
            //热门城市查询
            QueryResponse query = solrClient.query(solrQuery);
            List<Hotel> beans = query.getBeans(clazz);
            return ServerResponse.createBySuccess(beans);
        }
        List<Hotel> rows =new ArrayList<>();
        QueryResponse query = solrClient.query(solrQuery);
        List<Hotel> beans = query.getBeans(clazz);
        //返回文档对象得集合
        SolrDocumentList results = query.getResults();
        //获取查询出来得总条数
        long numFound = results.getNumFound();

        Page<Hotel>page=new Page(pageNo,pageSize,(int)numFound);


        for(int i = (pageNo-1)*pageSize;i<pageNo*pageSize;i++)
        {
            if(i<beans.size())
            {
                rows.add(beans.get(i));
            }else
            {
                break;
            }
        }
        page.setRows(rows);
        return ServerResponse.createBySuccess(page);
    }

}
