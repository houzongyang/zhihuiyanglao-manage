package com.example.service;

import cn.hutool.core.util.ObjectUtil;
import com.example.common.Constants;
import com.example.common.enums.ResultCodeEnum;
import com.example.common.enums.RoleEnum;
import com.example.entity.Account;
import com.example.entity.Nurse;
import com.example.exception.CustomException;
import com.example.mapper.NurseMapper;
import com.example.utils.TokenUtils;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import jakarta.annotation.Resource;
import java.util.List;

/**
 * 护士业务处理
 **/
@Service
public class NurseService {

    @Resource
    private NurseMapper nurseMapper;

    /**
     * 新增
     */
    public void add(Nurse nurse) {
        Nurse dbNurse = nurseMapper.selectByUsername(nurse.getUsername());
        if (ObjectUtil.isNotNull(dbNurse)) {
            throw new CustomException(ResultCodeEnum.USER_EXIST_ERROR);
        }
        if (ObjectUtil.isEmpty(nurse.getPassword())) {
            nurse.setPassword(Constants.USER_DEFAULT_PASSWORD);
        }
        if (ObjectUtil.isEmpty(nurse.getName())) {
            nurse.setName(nurse.getUsername());
        }
        nurse.setRole(RoleEnum.NURSE.name());
        nurseMapper.insert(nurse);
    }

    /**
     * 删除
     */
    public void deleteById(Integer id) {
        nurseMapper.deleteById(id);
    }

    /**
     * 批量删除
     */
    public void deleteBatch(List<Integer> ids) {
        for (Integer id : ids) {
            nurseMapper.deleteById(id);
        }
    }

    /**
     * 修改
     */
    public void updateById(Nurse nurse) {
        nurseMapper.updateById(nurse);
    }

    /**
     * 根据ID查询
     */
    public Nurse selectById(Integer id) {
        return nurseMapper.selectById(id);
    }

    /**
     * 查询所有
     */
    public List<Nurse> selectAll(Nurse nurse) {
        return nurseMapper.selectAll(nurse);
    }

    /**
     * 分页查询
     */
    public PageInfo<Nurse> selectPage(Nurse nurse, Integer pageNum, Integer pageSize) {
        PageHelper.startPage(pageNum, pageSize);
        List<Nurse> list = nurseMapper.selectAll(nurse);
        return PageInfo.of(list);
    }

    /**
     * 登录
     */
    public Account login(Account account) {
        Account dbNurse = nurseMapper.selectByUsername(account.getUsername());
        if (ObjectUtil.isNull(dbNurse)) {
            throw new CustomException(ResultCodeEnum.USER_NOT_EXIST_ERROR);
        }
        if (!account.getPassword().equals(dbNurse.getPassword())) {
            throw new CustomException(ResultCodeEnum.USER_ACCOUNT_ERROR);
        }
        // 生成token
        String tokenData = dbNurse.getId() + "-" + RoleEnum.NURSE.name();
        String token = TokenUtils.createToken(tokenData, dbNurse.getPassword());
        dbNurse.setToken(token);
        return dbNurse;
    }

    /**
     * 注册
     */
    public void register(Account account) {
        Nurse nurse = new Nurse();
        BeanUtils.copyProperties(account, nurse);
        add(nurse);
    }

    /**
     * 修改密码
     */
    public void updatePassword(Account account) {
        Nurse dbNurse = nurseMapper.selectByUsername(account.getUsername());
        if (ObjectUtil.isNull(dbNurse)) {
            throw new CustomException(ResultCodeEnum.USER_NOT_EXIST_ERROR);
        }
        if (!account.getPassword().equals(dbNurse.getPassword())) {
            throw new CustomException(ResultCodeEnum.PARAM_PASSWORD_ERROR);
        }
        dbNurse.setPassword(account.getNewPassword());
        nurseMapper.updateById(dbNurse);
    }
}
