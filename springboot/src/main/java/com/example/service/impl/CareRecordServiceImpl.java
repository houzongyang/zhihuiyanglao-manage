package com.example.service.impl;

import com.example.entity.CareRecord;
import com.example.mapper.CareRecordMapper;
import com.example.service.CareRecordService;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 护理记录服务实现类
 */
@Service
public class CareRecordServiceImpl implements CareRecordService {

    @Resource
    private CareRecordMapper careRecordMapper;

    @Override
    public void add(CareRecord careRecord) {
        careRecordMapper.insert(careRecord);
    }

    @Override
    public void deleteById(Integer id) {
        careRecordMapper.deleteById(id);
    }

    @Override
    public void deleteBatch(List<Integer> ids) {
        careRecordMapper.deleteBatch(ids);
    }

    @Override
    public void updateById(CareRecord careRecord) {
        careRecordMapper.updateById(careRecord);
    }

    @Override
    public CareRecord selectById(Integer id) {
        return careRecordMapper.selectById(id);
    }

    @Override
    public List<CareRecord> selectAll(CareRecord careRecord) {
        return careRecordMapper.selectAll(careRecord);
    }

    @Override
    public PageInfo<CareRecord> selectPage(CareRecord careRecord, Integer pageNum, Integer pageSize) {
        PageHelper.startPage(pageNum, pageSize);
        List<CareRecord> list = careRecordMapper.selectPage(careRecord);
        return PageInfo.of(list);
    }
}