package com.godtrue.spring.mybatis.dao.impl;

import com.godtrue.spring.mybatis.dao.UserDao;
import com.godtrue.spring.mybatis.domain.User;
import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

/**
 * @description：
 * @author：qianyingjie1
 * @create：2019-11-13
 */
public class UserDaoImpl implements UserDao{

  private static final String NS = "com.godtrue.spring.mybatis.dao.UserDao.";

  @Autowired
  private SqlSessionTemplate sqlSession;

  @Override
  public User getUser(String userId) {
    return sqlSession.selectOne(NS + "getUser",userId);
  }

  @Override
  public List<User> getUsers() {
    return sqlSession.selectList(NS + "getUsers");
  }
}
