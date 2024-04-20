package com.github.marschall.lockingdatasource;

import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.ConnectionBuilder;
import java.sql.SQLException;
import java.sql.SQLFeatureNotSupportedException;
import java.sql.ShardingKey;
import java.sql.ShardingKeyBuilder;
import java.util.Objects;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import java.util.logging.Logger;

import javax.sql.DataSource;

/**
 * A {@link DataSource} where every call to {@link #getConnection()} and {@link #getConnection(String, String)}
 * goes through a global lock.
 * <p>
 */
public final class LockingDataSource implements DataSource {

  private final DataSource delegate;

  private static final Lock LOCK;

  static {
    LOCK = new ReentrantLock();
  }

  public LockingDataSource(DataSource delegate) {
    Objects.requireNonNull(delegate, "delegate");
    this.delegate = delegate;
  }

  public <T> T unwrap(Class<T> iface) throws SQLException {
    return delegate.unwrap(iface);
  }

  public boolean isWrapperFor(Class<?> iface) throws SQLException {
    return delegate.isWrapperFor(iface);
  }

  public Connection getConnection() throws SQLException {
    LOCK.lock();
    try {
      return delegate.getConnection();
    } finally {
      LOCK.unlock();
    }
  }

  public Connection getConnection(String username, String password) throws SQLException {
    LOCK.lock();
    try {
      return delegate.getConnection(username, password);
    } finally {
      LOCK.unlock();
    }
  }

  public Logger getParentLogger() throws SQLFeatureNotSupportedException {
    return delegate.getParentLogger();
  }

  public PrintWriter getLogWriter() throws SQLException {
    return delegate.getLogWriter();
  }

  public void setLogWriter(PrintWriter out) throws SQLException {
    delegate.setLogWriter(out);
  }

  public void setLoginTimeout(int seconds) throws SQLException {
    delegate.setLoginTimeout(seconds);
  }

  public int getLoginTimeout() throws SQLException {
    return delegate.getLoginTimeout();
  }

  public ConnectionBuilder createConnectionBuilder() throws SQLException {
    return new LockingConnectionBuilder(delegate.createConnectionBuilder());
  }

  public ShardingKeyBuilder createShardingKeyBuilder() throws SQLException {
    return delegate.createShardingKeyBuilder();
  }

  static final class LockingConnectionBuilder implements ConnectionBuilder {

    private final ConnectionBuilder delegate;

    LockingConnectionBuilder(ConnectionBuilder connectionBuilder) {
      this.delegate = connectionBuilder;
    }

    public ConnectionBuilder user(String username) {
      delegate.user(username);
      return this;
    }

    public ConnectionBuilder password(String password) {
      delegate.password(password);
      return this;
    }

    public ConnectionBuilder shardingKey(ShardingKey shardingKey) {
      delegate.shardingKey(shardingKey);
      return this;
    }

    public ConnectionBuilder superShardingKey(ShardingKey superShardingKey) {
      delegate.superShardingKey(superShardingKey);
      return this;
    }

    public Connection build() throws SQLException {
      LOCK.lock();
      try {
        return delegate.build();
      } finally {
        LOCK.unlock();
      }
    }

  }

}
