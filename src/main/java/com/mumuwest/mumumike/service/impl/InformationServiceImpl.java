package com.mumuwest.mumumike.service.impl;

import com.mumuwest.mumumike.mapper.InformationMapper;
import com.mumuwest.mumumike.pojo.Information;
import com.mumuwest.mumumike.service.InformationService;
import com.mumuwest.mumumike.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
public class InformationServiceImpl implements InformationService {

    @Autowired
    private InformationMapper informationMapper;


    @Override
    public int createInformation(Information information) {
        // 设置创建时间
        information.setCreateTime(new Date());
        return informationMapper.createInformation(information);
    }

    @Override
    public int updateInformation(Information information) {
        return informationMapper.updateInformation(information);
    }

    @Override
    public int deleteInformation(Integer id) {
        return informationMapper.deleteInformation(id);
    }

    @Override
    public Information getInformationById(Integer Id) {
        return informationMapper.getInformationById(Id);
    }

    @Override
    public Information getInformationListByUserId(Integer userId) {
        return informationMapper.getInformationListByUserId(userId);
    }

    @Override
    public List<Information> getInformationList(Information information) {
        return informationMapper.getInformationList(information);
    }
}
