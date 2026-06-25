package com.example.hello.mapper;

import com.example.hello.dto.EmpQuery;
import com.example.hello.entity.Emp;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface EmpMapper {

    List<Emp> list(EmpQuery query);

    Emp findById(Integer id);

    List<Emp> findAll();

    int insert(Emp emp);

    int update(Emp emp);

    int deleteByIds(@Param("ids") List<Integer> ids);
}
