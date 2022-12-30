package com.harmonycloud.stuck.util.userCase;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.harmonycloud.stuck.bean.StudentDO;
import com.harmonycloud.stuck.mapper.StudentMapper;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;

@Service
public class UserService {

    private static Logger log = LogManager.getLogger(UserService.class);

    @Resource
    private StudentMapper studentMapper;


    @Transactional
    public void createUserWrong1(String name) {

        this.createUser(name);
        this.update(name);

        if (name.contains("test"))
            throw new RuntimeException("invalid username!");

    }

    @Transactional
    public void update(String name) {

        //更新学生姓名
        this.updateStudent(name);

    }



    public void createUser(String name) {

        StudentDO studentDO = new StudentDO();
        studentDO.setName(name);

        //新增学生
        studentMapper.insert(studentDO);

    }

    public void updateStudent(String name) {

        log.info("更新学生名称");
        StudentDO studentDO = new StudentDO();
        studentDO.setName(name + "new");
        studentMapper.update(studentDO, new QueryWrapper<StudentDO>().eq("name", "test"));

    }

}
