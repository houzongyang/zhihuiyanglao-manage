#!/usr/bin/env python
# -*- coding: utf-8 -*-
"""
Cronbach's Alpha Calculator
---------------------------
用于计算问卷调查中的内部一致性系数(Cronbach's α)

使用方法:
1. 准备数据：每行代表一个受试者，每列代表一个问卷条目
2. 运行脚本：python cronbach_alpha_calculator.py
3. 输入数据或从CSV文件导入

示例数据格式 (AI模式满意度评分，5点Likert量表):
受试者ID, 可执行性, 清晰度, 安全性, 整体满意度
1, 4, 5, 4, 5
2, 5, 4, 5, 5
...

作者: 智能养老系统团队
日期: 2023
"""

import numpy as np
import pandas as pd
import sys

def calculate_cronbach_alpha(data):
    """
    计算Cronbach's Alpha系数
    
    参数:
    data: numpy数组，形状为(n_samples, n_items)
          每行代表一个受试者，每列代表一个问卷条目
    
    返回:
    alpha: Cronbach's Alpha系数
    """
    n_items = data.shape[1]
    
    # 计算每个条目的方差
    item_variances = np.var(data, axis=0, ddof=1)
    
    # 计算总分
    total_scores = np.sum(data, axis=1)
    
    # 计算总分方差
    total_variance = np.var(total_scores, ddof=1)
    
    # 计算Cronbach's Alpha
    alpha = (n_items / (n_items - 1)) * (1 - np.sum(item_variances) / total_variance)
    
    return alpha

def print_results(data, alpha):
    """打印详细的计算结果"""
    n_items = data.shape[1]
    item_variances = np.var(data, axis=0, ddof=1)
    total_scores = np.sum(data, axis=1)
    total_variance = np.var(total_scores, ddof=1)
    
    print("\n===== Cronbach's Alpha 计算结果 =====")
    print(f"样本数量: {data.shape[0]}")
    print(f"条目数量: {n_items}")
    print("\n各条目方差:")
    for i, var in enumerate(item_variances):
        print(f"  条目 {i+1}: {var:.4f}")
    print(f"\n条目方差之和: {np.sum(item_variances):.4f}")
    print(f"总分方差: {total_variance:.4f}")
    print(f"\nCronbach's Alpha: {alpha:.4f}")
    
    # 解释Alpha值
    if alpha >= 0.9:
        reliability = "极高"
    elif alpha >= 0.8:
        reliability = "良好"
    elif alpha >= 0.7:
        reliability = "可接受"
    elif alpha >= 0.6:
        reliability = "勉强可接受"
    else:
        reliability = "较低"
    
    print(f"\n内部一致性: {reliability}")
    print("================================")

def main():
    print("Cronbach's Alpha 计算器")
    print("------------------------")
    
    try:
        # 示例数据 - 可以替换为实际数据
        # 这里使用论文中的AI模式满意度评分示例数据
        example_data = np.array([
            [4, 5, 4, 5],  # 受试者1: 可执行性, 清晰度, 安全性, 整体满意度
            [5, 4, 5, 5],  # 受试者2
            [4, 5, 5, 5],  # 受试者3
            [5, 5, 4, 5],  # 受试者4
            [4, 4, 5, 4],  # 受试者5
            [5, 5, 5, 5],  # 受试者6
            [4, 4, 4, 4],  # 受试者7
            [5, 4, 5, 5],  # 受试者8
            [4, 5, 4, 4],  # 受试者9
            [5, 5, 4, 5],  # 受试者10
        ])
        
        # 计算并打印结果
        alpha = calculate_cronbach_alpha(example_data)
        print_results(example_data, alpha)
        
        print("\n传统模式满意度评分示例数据:")
        traditional_data = np.array([
            [3, 4, 3, 3],  # 受试者1: 可执行性, 清晰度, 安全性, 整体满意度
            [4, 3, 4, 4],  # 受试者2
            [3, 4, 4, 3],  # 受试者3
            [4, 3, 3, 4],  # 受试者4
            [3, 3, 4, 3],  # 受试者5
            [4, 4, 3, 4],  # 受试者6
            [3, 3, 3, 3],  # 受试者7
            [4, 3, 4, 4],  # 受试者8
            [3, 4, 3, 3],  # 受试者9
            [4, 3, 3, 3],  # 受试者10
        ])
        
        # 计算并打印结果
        trad_alpha = calculate_cronbach_alpha(traditional_data)
        print_results(traditional_data, trad_alpha)
        
        print("\n注意: 这些是示例数据，请替换为您的实际研究数据")
        print("您可以修改此脚本或将数据保存为CSV文件后导入")
        
    except Exception as e:
        print(f"计算过程中出错: {e}")
        return

if __name__ == "__main__":
    main()