package com.example.hello.controller;

import com.example.hello.common.PageResult;
import com.example.hello.common.Result;
import com.example.hello.dto.ClazzQuery;
import com.example.hello.entity.Clazz;
import com.example.hello.service.ClazzService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/clazzs")
public class ClazzController {

    private final ClazzService clazzService;

    public ClazzController(ClazzService clazzService) {
        this.clazzService = clazzService;
    }

    @GetMapping
    public Result<PageResult<Clazz>> page(ClazzQuery query) {
        return Result.success(clazzService.page(query));
    }

    @GetMapping("/list")
    public Result<List<Clazz>> findAll() {
        return Result.success(clazzService.findAll());
    }

    @GetMapping("/{id}")
    public Result<Clazz> findById(@PathVariable Integer id) {
        return Result.success(clazzService.findById(id));
    }

    @PostMapping
    public Result<Void> add(@RequestBody Clazz clazz) {
        clazzService.add(clazz);
        return Result.success();
    }

    @PutMapping
    public Result<Void> update(@RequestBody Clazz clazz) {
        clazzService.update(clazz);
        return Result.success();
    }

    @DeleteMapping("/{id}")
    public Result<Void> deleteById(@PathVariable Integer id) {
        clazzService.deleteById(id);
        return Result.success();
    }
}
