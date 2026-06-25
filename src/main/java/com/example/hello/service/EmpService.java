package com.example.hello.service;

import com.example.hello.common.PageResult;
import com.example.hello.dto.EmpQuery;
import com.example.hello.entity.Emp;

import java.util.List;

public interface EmpService {

    PageResult<Emp> page(EmpQuery query);

    Emp findById(Integer id);

    List<Emp> findAll();

    void add(Emp emp);

    void update(Emp emp);

    void deleteByIds(String ids);
}
