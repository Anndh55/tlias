package com.example.hello.service.impl;

import com.example.hello.common.PageResult;
import com.example.hello.dto.ClazzQuery;
import com.example.hello.entity.Clazz;
import com.example.hello.mapper.ClazzMapper;
import com.example.hello.service.ClazzService;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.stereotype.Service;

import java.util.List;
import java.time.LocalDateTime;

@Service
public class ClazzServiceImpl implements ClazzService {

    private final ClazzMapper clazzMapper;

    public ClazzServiceImpl(ClazzMapper clazzMapper) {
        this.clazzMapper = clazzMapper;
    }

    @Override
    public PageResult<Clazz> page(ClazzQuery query) {
        PageHelper.startPage(query.getPage(), query.getPageSize());
        List<Clazz> list = clazzMapper.list(query);
        PageInfo<Clazz> pageInfo = new PageInfo<>(list);
        return new PageResult<>(pageInfo.getTotal(), pageInfo.getList());
    }

    @Override
    public Clazz findById(Integer id) {
        return clazzMapper.findById(id);
    }

    @Override
    public List<Clazz> findAll() {
        return clazzMapper.list(new ClazzQuery());
    }

    @Override
    public void add(Clazz clazz) {
        clazz.setCreateTime(LocalDateTime.now());
        clazz.setUpdateTime(LocalDateTime.now());
        clazzMapper.insert(clazz);
    }

    @Override
    public void update(Clazz clazz) {
        clazz.setUpdateTime(LocalDateTime.now());
        clazzMapper.update(clazz);
    }

    @Override
    public void deleteById(Integer id) {
        clazzMapper.deleteById(id);
    }
}
