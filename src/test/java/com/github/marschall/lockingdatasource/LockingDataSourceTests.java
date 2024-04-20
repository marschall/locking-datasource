package com.github.marschall.lockingdatasource;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.h2.jdbcx.JdbcDataSource;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.jdbc.core.JdbcOperations;
import org.springframework.jdbc.core.JdbcTemplate;

class LockingDataSourceTests {

  private JdbcOperations jdbcTemplate;

  @BeforeEach
  public void setUp() {
    JdbcDataSource h2DataSource = new JdbcDataSource();
    h2DataSource.setURL("jdbc:h2:mem:");
    this.jdbcTemplate = new JdbcTemplate(new LockingDataSource(h2DataSource));
  }

  @Test
  void select() {
    Integer result = this.jdbcTemplate.queryForObject("SELECT 1 FROM dual", Integer.class);
    assertEquals(Integer.valueOf(1), result);
  }

}
