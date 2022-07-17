package com.imooc.miaosha.service.serviceImp;

import com.imooc.miaosha.dao.UserDao;
import com.imooc.miaosha.entity.MiaoshaUser;
import com.imooc.miaosha.entity.User;
import com.imooc.miaosha.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

/**
 * @Classname UserServiceImp
 * @Description TODO
 * @Date 2022/4/26 15:24
 * @Created by Eskii
 */
@Service("UserService")
public class UserServiceImp implements UserService {
    @Autowired
    UserDao userDao;

    @Override
    public User getById(int id) {
        User user1 = userDao.getById(id);
        return user1;
    }

    @Override
    @Transactional
    public boolean transactionTest() {
        User user1 = new User(1, "zhangsan");
        userDao.insert(user1);

        User user2 = new User(2, "zhangsan");
        userDao.insert(user2);

        User user3 = new User(3, "linwinwin");
        userDao.insert(user3);
        return true;
    }
}
