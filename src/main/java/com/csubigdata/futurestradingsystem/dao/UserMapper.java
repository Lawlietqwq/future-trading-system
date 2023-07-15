package com.csubigdata.futurestradingsystem.dao;

import com.csubigdata.futurestradingsystem.entity.User;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserMapper {
    User getByUsername(String username);

    int insert(User user);

    String getById(Integer uid);

    List<String> getAllCompany();

}