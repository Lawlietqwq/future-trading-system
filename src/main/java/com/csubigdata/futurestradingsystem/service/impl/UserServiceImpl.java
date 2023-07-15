package com.csubigdata.futurestradingsystem.service.impl;

import com.csubigdata.futurestradingsystem.common.CommonException;
import com.csubigdata.futurestradingsystem.common.ResultTypeEnum;
import com.csubigdata.futurestradingsystem.dao.UserMapper;
import com.csubigdata.futurestradingsystem.entity.User;
import com.csubigdata.futurestradingsystem.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserMapper userMapper;
    
    @Override
    public User getUserByUsername(String username){
        User user = userMapper.getByUsername(username);
        return user;
    }


    @Override
    @Transactional
    public void insertUser(User user){
        if (userMapper.getByUsername(user.getUsername()) != null)
            CommonException.fail(ResultTypeEnum.USER_EXIST);
        boolean success =  userMapper.insert(user) > 0;
        if (!success){
            CommonException.fail(ResultTypeEnum.REGISTER_FAIL);
        }
    }

    @Override
    public int insertUserSelective(User user) {
        return 0;
    }

    @Override
    public List<String> getAllCompany() {
        return userMapper.getAllCompany();
    }
}
