package com.example.mapper;

import com.example.entity.BillManagement;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import java.util.List;

/**
 * 操作bill_management相关数据接口
 */
public interface BillManagementMapper {

    /**
     * 新增
     */
    int insert(BillManagement billManagement);

    /**
     * 批量新增
     */
    int batchInsert(@Param("list") List<BillManagement> billManagementList);

    /**
     * 删除
     */
    int deleteById(Integer id);

    /**
     * 修改
     */
    int updateById(BillManagement billManagement);

    /**
     * 根据ID查询
     */
    BillManagement selectById(Integer id);

    /**
     * 查询所有
     */
    List<BillManagement> selectAll(BillManagement billManagement);

    /**
     * 根据用户ID查询账单
     */
    @Select("select * from bill_management where user_id = #{userId} order by generated_date desc")
    List<BillManagement> selectByUserId(Integer userId);

    /**
     * 根据账单类型查询
     */
    @Select("select * from bill_management where bill_type = #{billType} order by generated_date desc")
    List<BillManagement> selectByBillType(String billType);

    /**
     * 根据支付状态查询
     */
    @Select("select * from bill_management where payment_status = #{paymentStatus} order by generated_date desc")
    List<BillManagement> selectByPaymentStatus(String paymentStatus);

    /**
     * 根据用户ID和支付状态查询
     */
    @Select("select * from bill_management where user_id = #{userId} and payment_status = #{paymentStatus} order by generated_date desc")
    List<BillManagement> selectByUserIdAndPaymentStatus(@Param("userId") Integer userId, @Param("paymentStatus") String paymentStatus);

    /**
     * 更新支付状态
     */
    @Update("update bill_management set payment_status = #{paymentStatus}, payment_date = now() where id = #{id}")
    int updatePaymentStatus(@Param("id") Integer id, @Param("paymentStatus") String paymentStatus);

    /**
     * 批量删除
     */
    int deleteBatch(@Param("ids") List<Integer> ids);
}
