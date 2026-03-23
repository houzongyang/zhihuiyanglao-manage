package com.example.service;

import cn.hutool.core.util.ObjectUtil;
import com.example.entity.BillManagement;
import com.example.mapper.BillManagementMapper;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.stereotype.Service;

import jakarta.annotation.Resource;
import java.time.LocalDateTime;
import java.util.List;

/**
 * 费用管理业务处理
 **/
@Service
public class BillManagementService {

    @Resource
    private BillManagementMapper billManagementMapper;

    /**
     * 新增
     */
    public void add(BillManagement billManagement) {
        if (ObjectUtil.isEmpty(billManagement.getPaymentStatus())) {
            billManagement.setPaymentStatus("UNPAID");
        }
        billManagement.setCreatedAt(LocalDateTime.now());
        billManagement.setUpdatedAt(LocalDateTime.now());
        billManagementMapper.insert(billManagement);
    }

    /**
     * 批量新增
     */
    public void batchAdd(List<BillManagement> billManagementList) {
        LocalDateTime now = LocalDateTime.now();
        for (BillManagement billManagement : billManagementList) {
            if (ObjectUtil.isEmpty(billManagement.getPaymentStatus())) {
                billManagement.setPaymentStatus("UNPAID");
            }
            billManagement.setCreatedAt(now);
            billManagement.setUpdatedAt(now);
        }
        billManagementMapper.batchInsert(billManagementList);
    }

    /**
     * 删除
     */
    public void deleteById(Integer id) {
        billManagementMapper.deleteById(id);
    }

    /**
     * 批量删除
     */
    public void deleteBatch(List<Integer> ids) {
        billManagementMapper.deleteBatch(ids);
    }

    /**
     * 修改
     */
    public void updateById(BillManagement billManagement) {
        billManagement.setUpdatedAt(LocalDateTime.now());
        billManagementMapper.updateById(billManagement);
    }

    /**
     * 根据ID查询
     */
    public BillManagement selectById(Integer id) {
        return billManagementMapper.selectById(id);
    }

    /**
     * 查询所有
     */
    public List<BillManagement> selectAll(BillManagement billManagement) {
        return billManagementMapper.selectAll(billManagement);
    }

    /**
     * 分页查询
     */
    public PageInfo<BillManagement> selectPage(BillManagement billManagement, Integer pageNum, Integer pageSize) {
        PageHelper.startPage(pageNum, pageSize);
        List<BillManagement> list = billManagementMapper.selectAll(billManagement);
        return PageInfo.of(list);
    }

    /**
     * 根据用户ID查询账单
     */
    public List<BillManagement> selectByUserId(Integer userId) {
        return billManagementMapper.selectByUserId(userId);
    }

    /**
     * 根据账单类型查询
     */
    public List<BillManagement> selectByBillType(String billType) {
        return billManagementMapper.selectByBillType(billType);
    }

    /**
     * 根据支付状态查询
     */
    public List<BillManagement> selectByPaymentStatus(String paymentStatus) {
        return billManagementMapper.selectByPaymentStatus(paymentStatus);
    }

    /**
     * 根据用户ID和支付状态查询
     */
    public List<BillManagement> selectByUserIdAndPaymentStatus(Integer userId, String paymentStatus) {
        return billManagementMapper.selectByUserIdAndPaymentStatus(userId, paymentStatus);
    }

    /**
     * 更新支付状态
     */
    public void updatePaymentStatus(Integer id, String paymentStatus) {
        billManagementMapper.updatePaymentStatus(id, paymentStatus);
    }

    /**
     * 支付账单（模拟支付）
     */
    public void payBill(Integer id) {
        BillManagement bill = billManagementMapper.selectById(id);
        if (bill != null && "UNPAID".equals(bill.getPaymentStatus())) {
            bill.setPaymentStatus("PAID");
            bill.setPaymentDate(LocalDateTime.now());
            bill.setUpdatedAt(LocalDateTime.now());
            billManagementMapper.updateById(bill);
        }
    }
}
