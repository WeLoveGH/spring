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
package org.mybatis.spring;

import org.apache.ibatis.session.ExecutorType;
import org.apache.ibatis.session.SqlSession;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.DefaultTransactionDefinition;
import org.springframework.transaction.support.TransactionSynchronizationManager;

import java.sql.SQLException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.fail;

// tests basic usage and implementation only
// MapperFactoryBeanTest handles testing the transactional functions in SqlSessionTemplate
public class SqlSessionTemplateTest extends AbstractMyBatisSpringTest {

  /**
   * 声明 SqlSession，SqlSession 是操作 SQL 语句的核心接口
   */
  private static SqlSession sqlSessionTemplate;

  /**
   * 执行测试用例的测试方法之前，执行此方法，用于设置各种前置资源
   */
  @BeforeAll
  static void setupSqlTemplate() {
    sqlSessionTemplate = new SqlSessionTemplate(sqlSessionFactory);
  }

  /**
   * 执行测试用例的测试方法之后，执行此方法，用于释放各种前置资源
   */
  @AfterEach
  void tearDown() {
    try {
      connection.close();
    } catch (SQLException ignored) {
    }
  }

  @Test
  void testGetConnection() throws java.sql.SQLException {
    java.sql.Connection conn = sqlSessionTemplate.getConnection();

    // outside of an explicit tx, getConnection() will start a tx, get an open connection then
    // end the tx, which closes the connection
    assertThat(conn.isClosed()).isTrue();
  }

  @Test
  void testGetConnectionInTx() throws java.sql.SQLException {
    TransactionStatus status = null;

    try {
      status = txManager.getTransaction(new DefaultTransactionDefinition());

      java.sql.Connection conn = sqlSessionTemplate.getConnection();

      assertThat(conn.isClosed()).isFalse();

    } finally {
      // rollback required to close connection
      txManager.rollback(status);
    }
  }

  @Test
  void testCommit() {
    assertThrows(UnsupportedOperationException.class, sqlSessionTemplate::commit);
  }

  @Test
  void testClose() {
    assertThrows(UnsupportedOperationException.class, sqlSessionTemplate::close);
  }

  @Test
  void testRollback() {
    assertThrows(UnsupportedOperationException.class, sqlSessionTemplate::rollback);
  }

  @Test
  void testExecutorType() {
    SqlSessionTemplate template = new SqlSessionTemplate(sqlSessionFactory, ExecutorType.BATCH);
    assertThat(template.getExecutorType()).isEqualTo(ExecutorType.BATCH);

    DataSourceTransactionManager manager = new DataSourceTransactionManager(dataSource);

    TransactionStatus status = null;

    try {
      status = manager.getTransaction(new DefaultTransactionDefinition());

      // will synchronize the template with the current tx
      template.getConnection();

      SqlSessionHolder holder = (SqlSessionHolder) TransactionSynchronizationManager.getResource(sqlSessionFactory);

      assertThat(holder.getExecutorType()).isEqualTo(ExecutorType.BATCH);
    } finally {
      // rollback required to close connection
      txManager.rollback(status);
    }
  }

  @Test
  void testExceptionTranslationShouldThrowMyBatisSystemException() throws SQLException {
    try {
      sqlSessionTemplate.selectOne("undefined");
      fail("exception not thrown when expected");
    } catch (MyBatisSystemException mbse) {
      // success
    } catch (Throwable t) {
      fail("SqlSessionTemplate should translate MyBatis PersistenceExceptions");
    } finally {
      connection.close(); // the template do not open the connection so it do not close it
    }
  }

  @Test
  void testExceptionTranslationShouldThrowDataAccessException() {

    // this query must be the same as the query in TestMapper.xml
    connection.getPreparedStatementResultSetHandler().prepareThrowsSQLException("SELECT 'fail'");

    try {
      sqlSessionTemplate.selectOne("org.mybatis.spring.TestMapper.findFail");
      fail("exception not thrown when expected");
    } catch (MyBatisSystemException mbse) {
      fail("SqlSessionTemplate should translate SQLExceptions into DataAccessExceptions");
    } catch (DataAccessException dae) {
      // success
    } catch (Throwable t) {
      fail("SqlSessionTemplate should translate MyBatis PersistenceExceptions");
    }
  }

  @Test
  void testTemplateWithNoTxInsert() {

    sqlSessionTemplate.getMapper(TestMapper.class).insertTest("test1");
    assertCommitJdbc();
    assertCommitSession();

  }

  @Test
  void testTemplateWithNoTxSelect() {

    sqlSessionTemplate.getMapper(TestMapper.class).findTest();
    assertCommit();

  }

  @Test
  void testWithTxRequired() {
    DefaultTransactionDefinition txDef = new DefaultTransactionDefinition();
    txDef.setPropagationBehaviorName("PROPAGATION_REQUIRED");

    TransactionStatus status = txManager.getTransaction(txDef);

    sqlSessionTemplate.getMapper(TestMapper.class).findTest();

    txManager.commit(status);

    assertCommit();
    assertSingleConnection();
  }

}
