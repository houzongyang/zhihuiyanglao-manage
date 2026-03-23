package com.example.service.impl;

import com.example.entity.CarePlan;
import com.example.mapper.CarePlanMapper;
import com.example.service.CarePlanService;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 护理计划服务实现类
 */
@Service
public class CarePlanServiceImpl implements CarePlanService {

    @Resource
    private CarePlanMapper carePlanMapper;

    @Override
    public void add(CarePlan carePlan) {
        carePlanMapper.insert(carePlan);
    }

    @Override
    public void deleteById(Integer id) {
        carePlanMapper.deleteById(id);
    }

    @Override
    public void deleteBatch(List<Integer> ids) {
        carePlanMapper.deleteBatch(ids);
    }

    @Override
    public void updateById(CarePlan carePlan) {
        carePlanMapper.updateById(carePlan);
    }

    @Override
    public CarePlan selectById(Integer id) {
        return carePlanMapper.selectById(id);
    }

    @Override
    public List<CarePlan> selectAll(CarePlan carePlan) {
        return carePlanMapper.selectAll(carePlan);
    }

    @Override
    public PageInfo<CarePlan> selectPage(CarePlan carePlan, Integer pageNum, Integer pageSize) {
        PageHelper.startPage(pageNum, pageSize);
        List<CarePlan> list = carePlanMapper.selectPage(carePlan);
        return PageInfo.of(list);
    }
}