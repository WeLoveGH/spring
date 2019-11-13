package com.godtrue.mybatis.dao;

import com.godtrue.mybatis.domain.User;

import java.util.List;

/**
 * DAO 的接口声明类，通过配置可以直接和 Mapper 相对应起来，通过 namespace 来对应起来
 */
public interface UserDao {

  User getUser(String userId);

  List<User> getUsers();
}
