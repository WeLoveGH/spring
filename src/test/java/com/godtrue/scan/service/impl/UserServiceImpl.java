/**
 * Copyright 2010-2019 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.godtrue.scan.service.impl;

import com.godtrue.scan.config.mapper.UserMapper;
import com.godtrue.scan.domain.User;
import com.godtrue.scan.service.UserService;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * 用户服务实现类，采用注解的方式注入服务bean
 */
@Transactional
public class UserServiceImpl implements UserService {

  private UserMapper userMapper;

  /**
   * 根据用户ID，获取用户信息
   *
   * @param userId
   * @return
   */
  @Override
  public User getUser(String userId) {
    return userMapper.getUser(userId);
  }

  /**
   * 获取所有的用户信息
   *
   * @return
   */
  @Override
  public List<User> getUsers() {
    return userMapper.getUsers();
  }

  public void setUserMapper(UserMapper userMapper) {
    this.userMapper = userMapper;
  }
}
