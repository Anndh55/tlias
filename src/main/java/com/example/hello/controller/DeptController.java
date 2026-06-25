package com.example.hello.controller;

import com.example.hello.common.Result;
import com.example.hello.entity.Dept;
import com.example.hello.service.DeptService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/depts")
public class DeptController {

    private final DeptService deptService;

    public DeptController(DeptService deptService) {
        this.deptService = deptService;
    }

    @GetMapping
    public Result<List<Dept>> findAll() {
        return Result.success(deptService.findAll());
    }

    @GetMapping("/{id}")
    public Result<Dept> findById(@PathVariable Integer id) {
        return Result.success(deptService.findById(id));
    }

    @PostMapping
    public Result<Void> insert(@RequestBody Dept dept) {
        deptService.insert(dept);
        return Result.success();
    }

    @PutMapping
    public Result<Void> update(@RequestBody Dept dept) {
        deptService.update(dept);
        return Result.success();
    }

    @DeleteMapping("/{id}")
    public Result<Void> deleteById(@PathVariable Integer id) {
        deptService.deleteById(id);
        return Result.success();
    }
}