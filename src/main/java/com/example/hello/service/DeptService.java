package com.example.hello.service;

import com.example.hello.entity.Dept;

import java.util.List;

public interface DeptService {

    List<Dept> findAll();

    Dept findById(Integer id);

    int insert(Dept dept);

    int update(Dept dept);

    int deleteById(Integer id);
}