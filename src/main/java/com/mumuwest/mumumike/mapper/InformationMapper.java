package com.mumuwest.mumumike.mapper;

import com.mumuwest.mumumike.pojo.Information;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface InformationMapper {

    /**
     * 添加信息
     * @param information
     * @return
     */
    public int createInformation(Information information);

    /**
     * 更新信息
     * @param information
     * @return
     */
    public int updateInformation(Information information);

    /**
     * 删除信息
     * @param id
     * @return
     */
    public int deleteInformation(Integer id);

    /**
     * 根据Id查询信息
     * @param Id
     * @return
     */
    public Information getInformationById(Integer Id);

    /**
     * 根据userId查询信息列表
     * @param userId
     * @return
     */
    public Information getInformationListByUserId(Integer userId);

    /**
     * 查询留言列表
     * @param information
     * @return
     */
    public List<Information> getInformationList(Information information);

}
