package com.example.hello.mapper;

import com.example.hello.entity.Dept;
// 程序包org.apache.ibatis.annotations不存在，使用MyBatis的Mapper注解需要添加mybatis依赖
// 临时方案：移除Mapper注解，改为在配置类中扫描或使用XML配置
// 或者添加依赖：org.mybatis:mybatis:3.5.x
// 修复：移除不存在的依赖，Param注解在MyBatis 3.5.0+中已内置于核心包，无需单独导入

import java.util.List;

// @Mapper 注解暂时注释掉，需要在配置类中手动扫描Mapper接口
// 原因：当前项目缺少mybatis-spring依赖，无法使用@Mapper注解
// 解决方案：在MyBatisConfig配置类中使用@MapperScan("com.example.hello.mapper")扫描
// @Mapper
public interface DeptMapper {

    List<Dept> findAll();

    Dept findById(Integer id);

    int insert(Dept dept);

    int update(Dept dept);

    int deleteById(Integer id);
}