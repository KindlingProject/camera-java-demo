package com.harmonycloud.stuck.util.userCase;

import com.harmonycloud.stuck.bean.StudentDO;
import com.harmonycloud.stuck.mapper.StudentMapper;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;

@Service
public class UserService2 {

    private static Logger log = LogManager.getLogger(UserService2.class);

    @Resource
    private StudentMapper studentMapper;



    public void createUserWrong1(String name) {
        this.createUserPrivate(name);

    }

    @Transactional(rollbackFor = Exception.class)
    public void createUserPrivate(String name) {

        StudentDO studentDO = new StudentDO();
        studentDO.setName(name);
        studentMapper.insert(studentDO);

//        if (name.contains("test"))
//            throw new RuntimeException("invalid username!");
    }

}
