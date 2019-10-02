package cn.kgc.itrip.biz.service.Impl;

import cn.kgc.itrip.beans.common.ServerResponse;
import cn.kgc.itrip.beans.pojo.*;
import cn.kgc.itrip.beans.vo.*;
import cn.kgc.itrip.biz.service.SystemCommentService;
import cn.kgc.itrip.mapper.itripComment.ItripCommentMapper;
import cn.kgc.itrip.mapper.itripHotel.ItripHotelMapper;
import cn.kgc.itrip.mapper.itripHotelOrder.ItripHotelOrderMapper;
import cn.kgc.itrip.mapper.itripImage.ItripImageMapper;
import cn.kgc.itrip.mapper.itripLabelDic.ItripLabelDicMapper;
import cn.kgc.itrip.utils.EmptyUtils;
import cn.kgc.itrip.utils.Page;
import cn.kgc.itrip.utils.RedisApi;
import cn.kgc.itrip.utils.SystemConfig;
import com.alibaba.fastjson.JSONObject;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.multipart.commons.CommonsMultipartResolver;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.util.*;

@Service
public class SystemCommentServiceImpl implements SystemCommentService
{
    @Autowired
    private ItripLabelDicMapper itripLabelDicMapper;

    @Autowired
    private ItripCommentMapper itripCommentMapper;

    @Autowired
    private ItripImageMapper itripImageMapper;

    @Autowired
    private ItripHotelOrderMapper itripHotelOrderMapper;

    @Autowired
    private ItripHotelMapper itripHotelMapper;

    @Autowired
    private RedisApi redisApi;

    @Autowired
    private SystemConfig systemConfig;

    @Override
    public ServerResponse getTravelType() throws Exception {
        Map<String,Object> param = new HashMap<>();
        param.put("parentId",107);
        List<ItripLabelDic> itripLabelDicListByMap = itripLabelDicMapper.getItripLabelDicListByMap(param);
        return ServerResponse.createBySuccess(itripLabelDicListByMap);
    }


    //图片上传
    @Override
    public ServerResponse upLoad(HttpServletRequest request)throws Exception
    {

        List<String> hasError = new ArrayList<>();

        List<String> ouput = new ArrayList<>();

        //获取图片解析器
        CommonsMultipartResolver commonsMultipartResolver =
                new CommonsMultipartResolver(request.getSession().getServletContext());
        //判断请求中是否带有文件上传
        if(commonsMultipartResolver.isMultipart(request))
        {
            //处理文件上传   因为上传了文件延迟
            MultipartHttpServletRequest multipartRequest=(MultipartHttpServletRequest)request;
            //获取token
            String token = multipartRequest.getHeader("token");
            if(redisApi.exists(token))
            {
                //获取文件的总数量
                int size = multipartRequest.getMultiFileMap().size();
                if(size>=1&&size<=4)
                {
                    //处理文件上传操作

                    //获取所有的文件   依次遍历
                    Iterator<String> fileNames = multipartRequest.getFileNames();

                    //根据token过去用户信息
                    ItripUser itripUser = JSONObject.parseObject(redisApi.get(token), ItripUser.class);
                    MultipartFile file =null;
                    while (fileNames.hasNext())
                    {
                        //进行文件上传
                        try
                        {
                            //获取文件
                            file= multipartRequest.getFile(fileNames.next());

                            //根据具体文件 获取具体的文件名字
                            String originalFilename = file.getOriginalFilename();

                            //验证文件是否满足条件
                            String suffix = originalFilename.substring(originalFilename.lastIndexOf(".")).toLowerCase();
                            if(!originalFilename.equals(" ") && (suffix.equals(".jpg")||suffix.equals(".jpeg")||suffix.equals(".png")))
                            {
                                //保存文件：文件名字不能使用原来的名字，自己生成【用户的id-系统时间毫秒数-随机数】
                                //设置新名字
                                String fileName = itripUser.getId()+"-"+System.currentTimeMillis()+"-"+
                                        (int)(Math.random()*10000000)+suffix;

                                //获取完成的保存路径【保存图片的位置】
                                String fullPath = systemConfig.getFileUploadPathString()+
                                        File.separator+fileName;

                                //文件保存
                                file.transferTo(new File(fullPath));

                                //返回页面，图片的访问路径
                                ouput.add(systemConfig.getVisitImgUrlString()+fileName);
                            }else
                            {
                                hasError.add(originalFilename+"上传失败,类型不允许");
                            }
                        }catch (Exception e)
                        {
                            e.printStackTrace();
                            hasError.add(file+"文件上传失败，请重新上传");
                        }
                    }
                    if(hasError.isEmpty())
                    {
                        return ServerResponse.createBySuccess(ouput);
                    }else
                    {
                        return ServerResponse.createBySuccess(hasError.toString(),ouput);
                    }
                }else
                {
                    return ServerResponse.createByErrorMessage("文件数量不对");
                }
            }else
            {
                return ServerResponse.createByErrorMessage("token失效，请重新登录");
            }
        }else
        {
            return ServerResponse.createByErrorMessage("请求的内容不是上传文件的类型");
        }
    }

    //图片删除
    @Override
    public ServerResponse delpic(String imgName, HttpServletRequest request)throws Exception
    {
        String token = request.getHeader("token");
        if(redisApi.exists(token))
        {
            //获取文件在服务器上的物理路径
            String fullpath = systemConfig.getFileUploadPathString()+File.separator+imgName;
            File file = new File(fullpath);
            if(file.exists())
            {
                //删除
                file.delete();
                return ServerResponse.createBySuccess("删除成功");
            }else
            {
                return ServerResponse.createByErrorMessage("删除失败，文件不存在");
            }
        }else
        {
            return ServerResponse.createByErrorMessage("token失效，重新登录");
        }
    }

    //添加评论
    @Override
    public  ServerResponse add(ItripAddCommentVO itripAddCommentVO, HttpServletRequest request)throws Exception
    {
        //判断token
        String token = request.getHeader("token");
        if(redisApi.exists(token))
        {
            //封装数据
            ItripComment itripComment=new ItripComment();
            BeanUtils.copyProperties(itripAddCommentVO,itripComment);
            ItripUser user = JSONObject.parseObject(redisApi.get(token), ItripUser.class);
            itripComment.setCreationDate(new Date());
            itripComment.setCreatedBy(user.getId());
            itripComment.setUserId(user.getId());


            List<ItripImage> itripImages = new ArrayList<>();
            if(itripAddCommentVO.getIsHavingImg()==1)
            {
                //说明本次上传有图片
                int i =0;
                //封装图片信息
                for(ItripImage imag :itripAddCommentVO.getItripImages())
                {
                    imag.setCreationDate(itripComment.getCreationDate());
                    imag.setPosition(++i);
                    imag.setCreatedBy(itripComment.getCreatedBy());
                    imag.setType("2");
                    itripImages.add(imag);
                }
            }
            //计算综合得分 ： （设施 +卫生+服务+位置）/ 4
            int sum = itripComment.getFacilitiesScore()+itripComment.getHygieneScore()+
                    itripComment.getPositionScore()+itripComment.getServiceScore();
            itripComment.setScore(sum/4);
            //添加评论信息
            itripCommentMapper.insertItripComment(itripComment);
            //保存图片
            Long id = itripComment.getId();
            if(EmptyUtils.isNotEmpty(itripImages))
            {
                for(ItripImage imag : itripImages)
                {
                    imag.setTargetId(id);
                    itripImageMapper.insertItripImage(imag);
                }
            }
            //修改订单状态
            //修改订单的状态  改为已评论
            ItripHotelOrder order = new ItripHotelOrder();
            order.setOrderStatus(4);
            order.setModifyDate(itripComment.getCreationDate());
            order.setModifiedBy(itripComment.getCreatedBy());
            order.setId(itripComment.getOrderId());
            itripHotelOrderMapper.updateItripHotelOrder(order);

            return ServerResponse.createBySuccess();

        }else
        {
            return ServerResponse.createByErrorMessage("token失效，请重新登录");
        }
    }

    /**
     *  根据评论查询列表，并分页显示
     */
    @Override
    public ServerResponse getCommentList(ItripSearchCommentVO itripSearchCommentVO)throws Exception
    {
        if(itripSearchCommentVO.getIsOk()==-1)
        {
            itripSearchCommentVO.setIsOk(null);
        }
        if(itripSearchCommentVO.getIsHavingImg()==-1)
        {
            itripSearchCommentVO.setIsHavingImg(null);
        }
        Map<String,Object> param = new HashMap<>();
        param.put("hotelId",itripSearchCommentVO.getHotelId());
        param.put("isHavimgImg",itripSearchCommentVO.getIsHavingImg());
        param.put("isOk",itripSearchCommentVO.getIsOk());

        //获取总条数
        Integer count = itripCommentMapper.getItripCommentCountByMap(param);
        Page page = new Page(itripSearchCommentVO.getPageNo(),itripSearchCommentVO.getPageSize(),count);
        param.put("beginPos", page.getBeginPos());
        param.put("pageSize", page.getPageSize());
        List<ItripListCommentVO> itripCommentListByMapWithDetail = itripCommentMapper.getItripCommentListByMapWithDetail(param);
        page.setRows(itripCommentListByMapWithDetail);
        return ServerResponse.createBySuccess(page);
    }

    /**
     * 根据酒店id获取酒店相关信息（酒店名称、酒店星级）
     */
    @Override
    public ServerResponse getHotelDesc(Long hotelId)throws Exception
    {
        ItripHotelDescVO itripHotelDescVO=new ItripHotelDescVO();
        ItripHotel itripHotelById = itripHotelMapper.getItripHotelById(hotelId);
        itripHotelDescVO.setHotelId(itripHotelById.getId());
        itripHotelDescVO.setHotelLevel(itripHotelById.getHotelLevel());
        itripHotelDescVO.setHotelName(itripHotelById.getHotelName());

        return ServerResponse.createBySuccess(itripHotelDescVO);
    }

    /**
     * 查询酒店的各项平均分
     */
    @Override
    public ServerResponse gethotelscore(Long hotelId)throws Exception
    {
        ItripScoreCommentVO itripScoreCommentVO = new ItripScoreCommentVO();
        Map<String,Object> param =  new HashMap<>();
        param.put("hotelId",hotelId);
        List<ItripComment> commentListByMap = itripCommentMapper.getItripCommentListByMap(param);
        Integer PositionScore=0;
        Integer FacilitiesScore = 0;
        Integer ServiceScore=0;
        Integer HygieneScore=0;
        Integer Score =0;
        for(ItripComment comment:commentListByMap)
        {
            PositionScore+=comment.getPositionScore();
            FacilitiesScore+=comment.getFacilitiesScore();
            ServiceScore+=comment.getServiceScore();
            HygieneScore+=comment.getHygieneScore();
            Score+=comment.getScore();
        }
        itripScoreCommentVO.setAvgFacilitiesScore(FacilitiesScore/commentListByMap.size());
        itripScoreCommentVO.setAvgPositionScore(PositionScore/commentListByMap.size());
        itripScoreCommentVO.setAvgHygieneScore(HygieneScore/commentListByMap.size());
        itripScoreCommentVO.setAvgServiceScore(ServiceScore/commentListByMap.size());
        itripScoreCommentVO.setAvgScore(Score/commentListByMap.size());

        return ServerResponse.createBySuccess(itripScoreCommentVO);
    }

    /**
     * 获取各项评论的数量
     */
    @Override
    public ServerResponse getCount(Long hotelId) throws Exception {
        Map<String,Object> param = new HashMap<>();
        Map<String,Integer> output = new HashMap<String,Integer>();
        Integer count = 0;
        param.put("hotelId",hotelId);
        count = itripCommentMapper.getItripCommentCountByMap(param);
        output.put("allcomment",count);

        //获取有待改善的数量
        param.put("isOk","0");
        count = itripCommentMapper.getItripCommentCountByMap(param);
        output.put("improve",count);

        //获取值得推荐的数量
        param.put("isOk","1");
        count = itripCommentMapper.getItripCommentCountByMap(param);
        output.put("isok",count);

        //获取有图片的数量
        param.put("isHavingImg",1);
        param.put("isOk",null);
        count = itripCommentMapper.getItripCommentCountByMap(param);
        output.put("havingimg",count);

        return ServerResponse.createBySuccess(output);
    }

}
