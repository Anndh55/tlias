package com.example.hello.service.impl;

import com.example.hello.common.PageResult;
import com.example.hello.dto.EmpQuery;
import com.example.hello.entity.Emp;
import com.example.hello.entity.EmpExpr;
import com.example.hello.mapper.EmpExprMapper;
import com.example.hello.mapper.EmpMapper;
import com.example.hello.service.EmpService;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import java.time.LocalDateTime;

@Service
public class EmpServiceImpl implements EmpService {

    private final EmpMapper empMapper;
    private final EmpExprMapper empExprMapper;

    public EmpServiceImpl(EmpMapper empMapper, EmpExprMapper empExprMapper) {
        this.empMapper = empMapper;
        this.empExprMapper = empExprMapper;
    }

    @Override
    public PageResult<Emp> page(EmpQuery query) {
        PageHelper.startPage(query.getPage(), query.getPageSize());
        List<Emp> list = empMapper.list(query);
        PageInfo<Emp> pageInfo = new PageInfo<>(list);
        return new PageResult<>(pageInfo.getTotal(), pageInfo.getList());
    }

    @Override
    public Emp findById(Integer id) {
        Emp emp = empMapper.findById(id);
        if (emp != null) {
            emp.setExprList(empExprMapper.findByEmpId(id));
        }
        return emp;
    }

    @Override
    public List<Emp> findAll() {
        return empMapper.findAll();
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void add(Emp emp) {
        if (!StringUtils.hasText(emp.getPassword())) {
            emp.setPassword("123456");
        }
        empMapper.insert(emp);
        saveExprList(emp.getId(), emp.getExprList());
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void update(Emp emp) {
        emp.setUpdateTime(LocalDateTime.now());
        empMapper.update(emp);
        updateExprList(emp.getId(), emp.getExprList());
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteByIds(String ids) {
        List<Integer> idList = parseIds(ids);
        if (idList.isEmpty()) {
            return;
        }
        empExprMapper.deleteByEmpIds(idList);
        empMapper.deleteByIds(idList);
    }

    private void saveExprList(Integer empId, List<EmpExpr> exprList) {
        if (exprList == null || exprList.isEmpty()) {
            return;
        }
        exprList.forEach(expr -> {
            expr.setEmpId(empId);
            empExprMapper.insert(expr);
        });
    }

    private void updateExprList(Integer empId, List<EmpExpr> exprList) {
        if (exprList == null || exprList.isEmpty()) {
            empExprMapper.deleteByEmpId(empId);
            return;
        }

        List<Integer> keepIds = exprList.stream()
                .map(EmpExpr::getId)
                .filter(Objects::nonNull)
                .collect(Collectors.toList());

        empExprMapper.deleteByEmpIdExcludeIds(empId, keepIds);

        exprList.forEach(expr -> {
            expr.setEmpId(empId);
            if (expr.getId() != null) {
                empExprMapper.update(expr);
            } else {
                empExprMapper.insert(expr);
            }
        });
    }

    private List<Integer> parseIds(String ids) {
        if (!StringUtils.hasText(ids)) {
            return Collections.emptyList();
        }
        return Arrays.stream(ids.split(","))
                .map(String::trim)
                .filter(StringUtils::hasText)
                .map(Integer::valueOf)
                .collect(Collectors.toList());
    }
}
