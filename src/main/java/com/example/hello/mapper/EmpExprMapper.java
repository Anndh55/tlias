package com.example.hello.mapper;

import com.example.hello.entity.EmpExpr;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface EmpExprMapper {

    List<EmpExpr> findByEmpId(Integer empId);

    int insert(EmpExpr empExpr);

    int update(EmpExpr empExpr);

    int deleteByEmpId(Integer empId);

    int deleteByEmpIds(@Param("empIds") List<Integer> empIds);

    int deleteByEmpIdExcludeIds(@Param("empId") Integer empId, @Param("ids") List<Integer> ids);
}
