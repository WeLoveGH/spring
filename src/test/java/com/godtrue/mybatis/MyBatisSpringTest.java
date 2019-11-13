package com.godtrue.mybatis;

import com.godtrue.mybatis.domain.User;
import com.godtrue.mybatis.service.UserService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;

import java.util.List;

/**
 * @description： 测试 MyBatis和Spring的结合示例
 * @author：qianyingjie1
 * @create：2019-11-13
 */

/**
 * 将应用的上下文配置信息，纳入到 Spring 的上下文环境之中
 */
@SpringJUnitConfig(locations = { "classpath:com/godtrue/mybatis/config/applicationContext/applicationContext.xml" })
public class MyBatisSpringTest {

  /**
   * 自动注入对应的服务
   */

  @Autowired
  private UserService userService;

  /**
   * 测试获取单个用户
   */
  @Test
  public void testGetUser(){
    User user = userService.getUser("u1");
    System.out.println("user = " + user);
  }

  /**
   * 测试获取多个用户
   */
  @Test
  public void testGetUsers(){
    List<User> userList = userService.getUsers();
    System.out.println("userList = " + userList);
  }
}
