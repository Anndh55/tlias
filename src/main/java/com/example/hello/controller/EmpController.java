package com.example.hello.controller;

import com.example.hello.common.PageResult;
import com.example.hello.common.Result;
import com.example.hello.dto.EmpQuery;
import com.example.hello.entity.Emp;
import com.example.hello.service.EmpService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/emps")
public class EmpController {

    private final EmpService empService;

    public EmpController(EmpService empService) {
        this.empService = empService;
    }

    @GetMapping
    public Result<PageResult<Emp>> page(EmpQuery query) {
        return Result.success(empService.page(query));
    }

    @GetMapping("/list")

    public Result<List<Emp>> findAll() {
        return Result.success(empService.findAll());
    }

    @GetMapping("/{id}")
    public Result<Emp> findById(@PathVariable Integer id) {
        return Result.success(empService.findById(id));
    }

    @PostMapping
    public Result<Void> add(@RequestBody Emp emp) {
        empService.add(emp);
        return Result.success();
    }

    @PutMapping
    public Result<Void> update(@RequestBody Emp emp) {
        empService.update(emp);
        return Result.success();
    }

    @DeleteMapping
    public Result<Void> delete(@RequestParam String ids) {
        empService.deleteByIds(ids);
        return Result.success();
    }
}
