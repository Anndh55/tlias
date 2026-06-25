package com.example.hello.service;

import com.example.hello.common.PageResult;
import com.example.hello.dto.ClazzQuery;
import com.example.hello.entity.Clazz;

import java.util.List;

public interface ClazzService {

    PageResult<Clazz> page(ClazzQuery query);

    Clazz findById(Integer id);

    List<Clazz> findAll();

    void add(Clazz clazz);

    void update(Clazz clazz);

    void deleteById(Integer id);
}
