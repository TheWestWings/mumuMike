package com.mumuwest.mumumike.controller;

import com.mumuwest.mumumike.pojo.Information;
import com.mumuwest.mumumike.pojo.TableDataInfo;
import com.mumuwest.mumumike.service.InformationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/information")
public class InformationController {

    @Autowired
    private InformationService informationService;

    // 添加信息
    @PostMapping
    public int createInformation(@RequestBody Information information) {
        return informationService.createInformation(information);
    }

    @DeleteMapping("/{id}")
    public int deleteInformation(@PathVariable("id") Integer id) {
        return informationService.deleteInformation(id);
    }

    @PutMapping
    public int updateInformation(@RequestBody Information information) {
        return informationService.updateInformation(information);
    }

    @GetMapping("/{id}")
    public Information getInformationById(@PathVariable("id") Integer id) {
        return informationService.getInformationById(id);
    }

    @PostMapping("/getList")
    public TableDataInfo getInformationList(@RequestBody Information information) {
        List<Information> informationList = informationService.getInformationList(information);
        return new TableDataInfo(informationList, informationList.size());
    }
}
