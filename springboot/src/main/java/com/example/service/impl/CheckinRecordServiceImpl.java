package com.example.service.impl;

import com.example.entity.CheckinRecord;
import com.example.mapper.CheckinRecordMapper;
import com.example.service.CheckinRecordService;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * 入住信息记录服务实现类
 */
@Service
public class CheckinRecordServiceImpl implements CheckinRecordService {

    @Resource
    private CheckinRecordMapper checkinRecordMapper;

    @Override
    @Transactional
    public void add(CheckinRecord checkinRecord) {
        checkinRecordMapper.insert(checkinRecord);
    }

    @Override
    @Transactional
    public void deleteById(Integer id) {
        checkinRecordMapper.deleteById(id);
    }

    @Override
    @Transactional
    public void deleteBatch(List<Integer> ids) {
        checkinRecordMapper.deleteBatch(ids);
    }

    @Override
    @Transactional
    public void updateById(CheckinRecord checkinRecord) {
        checkinRecordMapper.updateById(checkinRecord);
    }

    @Override
    public CheckinRecord selectById(Integer id) {
        return checkinRecordMapper.selectById(id);
    }

    @Override
    public List<CheckinRecord> selectAll(CheckinRecord checkinRecord) {
        return checkinRecordMapper.selectAll(checkinRecord);
    }

    @Override
    public PageInfo<CheckinRecord> selectPage(CheckinRecord checkinRecord, Integer pageNum, Integer pageSize) {
        PageHelper.startPage(pageNum, pageSize);
        List<CheckinRecord> list = checkinRecordMapper.selectPage(checkinRecord);
        return new PageInfo<>(list);
    }
} 