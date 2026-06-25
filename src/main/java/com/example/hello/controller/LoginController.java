package com.example.hello.controller;

import com.example.hello.common.Result;
import com.example.hello.entity.Emp;
import com.example.hello.mapper.EmpMapper;
import com.example.hello.utils.JwtUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
public class LoginController {

    private final EmpMapper empMapper;

    public LoginController(EmpMapper empMapper) {
        this.empMapper = empMapper;
    }

    @PostMapping("/login")
    public Result<Map<String, Object>> login(@RequestBody Map<String, String> params) {
        String username = params.get("username");
        String password = params.get("password");

        Emp emp = empMapper.findByUsername(username);
        if (emp == null || !emp.getPassword().equals(password)) {
            return Result.fail("用户名或密码错误");
        }

        String token = JwtUtils.generateToken(emp);

        Map<String, Object> data = Map.of(
                "id", emp.getId(),
                "username", emp.getUsername(),
                "name", emp.getName(),
                "token", token
        );
        return Result.success(data);
    }
}
