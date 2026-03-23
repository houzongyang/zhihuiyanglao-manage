package com.example.service.impl;

import com.example.entity.CareAssignment;
import com.example.mapper.CareAssignmentMapper;
import com.example.service.CareAssignmentService;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 护理安排服务实现类
 */
@Service
public class CareAssignmentServiceImpl implements CareAssignmentService {

    @Resource
    private CareAssignmentMapper careAssignmentMapper;

    @Override
    public void add(CareAssignment careAssignment) {
        careAssignmentMapper.insert(careAssignment);
    }

    @Override
    public void deleteById(Integer id) {
        careAssignmentMapper.deleteById(id);
    }

    @Override
    public void deleteBatch(List<Integer> ids) {
        careAssignmentMapper.deleteBatch(ids);
    }

    @Override
    public void updateById(CareAssignment careAssignment) {
        careAssignmentMapper.updateById(careAssignment);
    }

    @Override
    public CareAssignment selectById(Integer id) {
        return careAssignmentMapper.selectById(id);
    }

    @Override
    public List<CareAssignment> selectAll(CareAssignment careAssignment) {
        return careAssignmentMapper.selectAll(careAssignment);
    }

    @Override
    public PageInfo<CareAssignment> selectPage(CareAssignment careAssignment, Integer pageNum, Integer pageSize) {
        PageHelper.startPage(pageNum, pageSize);
        List<CareAssignment> list = careAssignmentMapper.selectPage(careAssignment);
        return PageInfo.of(list);
    }
}