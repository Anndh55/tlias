package com.example.hello.mapper;

import com.example.hello.dto.ClazzQuery;
import com.example.hello.entity.Clazz;

import java.util.List;

public interface ClazzMapper {

    List<Clazz> list(ClazzQuery query);

    Clazz findById(Integer id);

    int insert(Clazz clazz);

    int update(Clazz clazz);

    int deleteById(Integer id);
}
