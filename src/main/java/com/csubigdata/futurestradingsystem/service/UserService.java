package com.csubigdata.futurestradingsystem.service;

import com.csubigdata.futurestradingsystem.dao.UserMapper;
import com.csubigdata.futurestradingsystem.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;


public interface UserService{

    public User getUserByUsername(String name);

    public void insertUser(User user);

    public int insertUserSelective(User user);

    public List<String> getAllCompany();

}
