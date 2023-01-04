package com.harmonycloud.stuck.util.userCase;

import com.harmonycloud.stuck.bean.StudentDO;
import com.harmonycloud.stuck.mapper.StudentMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;

@Service
public class UserRightService {

    @Resource
    private StudentMapper studentMapper;


    @Transactional
    public void createUserWrong(String name) {
        this.createUserPrivate(name);
        if (name.contains("test"))
            throw new RuntimeException("invalid username!");

    }

    public void createUserPrivate(String name) {

        StudentDO studentDO = new StudentDO();
        studentDO.setName(name);
        studentMapper.insert(studentDO);
    }

}
