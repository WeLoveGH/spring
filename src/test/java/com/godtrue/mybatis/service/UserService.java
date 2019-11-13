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
package com.godtrue.mybatis.service;

import com.godtrue.mybatis.domain.User;

import java.util.List;

/**
 * 用户服务
 */
public interface UserService {
  /**
   * 根据用户ID，获取用户信息
   * @param userId
   * @return
   */
  User getUser(String userId);

  /**
   * 获取所有的用户信息
   * @return
   */
  List<User> getUsers();
}
