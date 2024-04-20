Locking DataSource
==================

A DataSource where every call to `#getConnection()` goes through a global lock.

Use this when `DataSource#getConnection()` or `Driver#connect(String, Properties)` is not thread safe for your JDBC driver and your connection pool creates connections in the calling thread.
