package com.harmonycloud.stuck.util.userCase;

import com.harmonycloud.stuck.bean.StudentDO;
import com.harmonycloud.stuck.mapper.StudentMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;

@Service
public class UserWrongService {


    @Resource
    private StudentMapper studentMapper;



    public void createUserWrong(String name) {

        this.createUser(name);

        if (name.contains("test"))
            throw new RuntimeException("invalid username!");

    }


    @Transactional
    public void createUser(String name) {

        StudentDO studentDO = new StudentDO();
        studentDO.setName(name);

        //新增学生
        studentMapper.insert(studentDO);

    }

}
