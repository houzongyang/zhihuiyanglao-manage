package com.example.controller;

import com.example.common.Result;
import com.example.common.enums.ResultCodeEnum;
import com.example.entity.BillManagement;
import com.example.service.BillManagementService;
import com.github.pagehelper.PageInfo;
import org.springframework.web.bind.annotation.*;

import jakarta.annotation.Resource;
import java.util.List;

/**
 * 费用管理前端操作接口
 **/
@RestController
@RequestMapping("/billManagement")
public class BillManagementController {

    @Resource
    private BillManagementService billManagementService;

    /**
     * 新增
     */
    @PostMapping("/add")
    public Result add(@RequestBody BillManagement billManagement) {
        billManagementService.add(billManagement);
        return Result.success();
    }

    /**
     * 批量新增账单
     */
    @PostMapping("/batchAdd")
    public Result batchAdd(@RequestBody List<BillManagement> billManagementList) {
        System.out.println("=== 批量添加账单调试信息 ===");
        System.out.println("接收到的账单数量: " + billManagementList.size());
        
        try {
            billManagementService.batchAdd(billManagementList);
            System.out.println("批量添加成功");
            System.out.println("===========================");
            return Result.success();
        } catch (Exception e) {
            System.out.println("批量添加失败: " + e.getMessage());
            e.printStackTrace();
            return Result.error(ResultCodeEnum.valueOf("批量添加失败: " + e.getMessage()));
        }
    }

    /**
     * 删除
     */
    @DeleteMapping("/delete/{id}")
    public Result deleteById(@PathVariable Integer id) {
        billManagementService.deleteById(id);
        return Result.success();
    }

    /**
     * 批量删除
     */
    @DeleteMapping("/delete/batch")
    public Result deleteBatch(@RequestBody List<Integer> ids) {
        billManagementService.deleteBatch(ids);
        return Result.success();
    }

    /**
     * 修改
     */
    @PutMapping("/update")
    public Result updateById(@RequestBody BillManagement billManagement) {
        billManagementService.updateById(billManagement);
        return Result.success();
    }

    /**
     * 根据ID查询
     */
    @GetMapping("/selectById/{id}")
    public Result selectById(@PathVariable Integer id) {
        BillManagement billManagement = billManagementService.selectById(id);
        return Result.success(billManagement);
    }

    /**
     * 查询所有
     */
    @GetMapping("/selectAll")
    public Result selectAll(BillManagement billManagement) {
        List<BillManagement> list = billManagementService.selectAll(billManagement);
        return Result.success(list);
    }

    /**
     * 分页查询
     */
    @GetMapping("/selectPage")
    public Result selectPage(BillManagement billManagement,
                             @RequestParam(defaultValue = "1") Integer pageNum,
                             @RequestParam(defaultValue = "10") Integer pageSize) {
        System.out.println("=== BillManagement selectPage 调试信息 ===");
        System.out.println("查询参数: " + billManagement);
        System.out.println("pageNum: " + pageNum + ", pageSize: " + pageSize);
        System.out.println("日期范围: startDate=" + billManagement.getStartDate() + ", endDate=" + billManagement.getEndDate());
        
        PageInfo<BillManagement> page = billManagementService.selectPage(billManagement, pageNum, pageSize);
        
        System.out.println("查询结果 - 总数: " + page.getTotal());
        System.out.println("查询结果 - 数据条数: " + page.getList().size());
        System.out.println("=======================================");
        
        return Result.success(page);
    }

    /**
     * 根据用户ID查询账单
     */
    @GetMapping("/selectByUserId/{userId}")
    public Result selectByUserId(@PathVariable Integer userId) {
        List<BillManagement> list = billManagementService.selectByUserId(userId);
        return Result.success(list);
    }

    /**
     * 根据账单类型查询
     */
    @GetMapping("/selectByBillType/{billType}")
    public Result selectByBillType(@PathVariable String billType) {
        List<BillManagement> list = billManagementService.selectByBillType(billType);
        return Result.success(list);
    }

    /**
     * 根据支付状态查询
     */
    @GetMapping("/selectByPaymentStatus/{paymentStatus}")
    public Result selectByPaymentStatus(@PathVariable String paymentStatus) {
        List<BillManagement> list = billManagementService.selectByPaymentStatus(paymentStatus);
        return Result.success(list);
    }

    /**
     * 根据用户ID和支付状态查询未支付账单
     */
    @GetMapping("/selectUnpaidByUserId/{userId}")
    public Result selectUnpaidByUserId(@PathVariable Integer userId) {
        List<BillManagement> list = billManagementService.selectByUserIdAndPaymentStatus(userId, "UNPAID");
        return Result.success(list);
    }

    /**
     * 支付账单（模拟支付）
     */
    @PutMapping("/pay/{id}")
    public Result payBill(@PathVariable Integer id) {
        billManagementService.payBill(id);
        return Result.success();
    }

    /**
     * 更新支付状态
     */
    @PutMapping("/updatePaymentStatus")
    public Result updatePaymentStatus(@RequestParam Integer id, @RequestParam String paymentStatus) {
        billManagementService.updatePaymentStatus(id, paymentStatus);
        return Result.success();
    }

    /**
     * 测试接口 - 直接查询所有数据
     */
    @GetMapping("/test")
    public Result test() {
        try {
            List<BillManagement> list = billManagementService.selectAll(new BillManagement());
            System.out.println("=== 测试接口调试信息 ===");
            System.out.println("直接查询所有数据，结果数量: " + list.size());
            for (int i = 0; i < Math.min(list.size(), 3); i++) {
                System.out.println("数据" + (i+1) + ": " + list.get(i));
            }
            System.out.println("=====================");
            return Result.success(list);
        } catch (Exception e) {
            System.out.println("查询失败: " + e.getMessage());
            e.printStackTrace();
            return Result.error(ResultCodeEnum.valueOf("查询失败: " + e.getMessage()));
        }
    }
}
