package com.example.hello.controller;

import com.example.hello.common.PageResult;
import com.example.hello.common.Result;
import com.example.hello.dto.StudentQuery;
import com.example.hello.entity.Student;
import com.example.hello.service.StudentService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/students")
public class StudentController {

    private final StudentService studentService;

    public StudentController(StudentService studentService) {
        this.studentService = studentService;
    }

    @GetMapping
    public Result<PageResult<Student>> page(StudentQuery query) {
        return Result.success(studentService.page(query));
    }

    @GetMapping("/list")
    public Result<List<Student>> findAll() {
        return Result.success(studentService.findAll());
    }

    @GetMapping("/{id}")
    public Result<Student> findById(@PathVariable Integer id) {
        return Result.success(studentService.findById(id));
    }

    @PostMapping
    public Result<Void> add(@RequestBody Student student) {
        studentService.add(student);
        return Result.success();
    }

    @PutMapping
    public Result<Void> update(@RequestBody Student student) {
        studentService.update(student);
        return Result.success();
    }

    @DeleteMapping
    public Result<Void> delete(@RequestParam String ids) {
        studentService.deleteByIds(ids);
        return Result.success();
    }
}
