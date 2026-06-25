package com.example.hello.mapper;

import com.example.hello.dto.StudentQuery;
import com.example.hello.entity.Student;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface StudentMapper {

    List<Student> list(StudentQuery query);

    Student findById(Integer id);

    int insert(Student student);

    int update(Student student);

    int deleteByIds(@Param("ids") List<Integer> ids);
}
