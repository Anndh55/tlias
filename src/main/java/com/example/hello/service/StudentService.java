package com.example.hello.service;

import com.example.hello.common.PageResult;
import com.example.hello.dto.StudentQuery;
import com.example.hello.entity.Student;

import java.util.List;

public interface StudentService {

    PageResult<Student> page(StudentQuery query);

    Student findById(Integer id);

    List<Student> findAll();

    void add(Student student);

    void update(Student student);

    void deleteByIds(String ids);
}
