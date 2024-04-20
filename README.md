Locking DataSource [![Maven Central](https://maven-badges.herokuapp.com/maven-central/com.github.marschall/locking-datasource/badge.svg)](https://maven-badges.herokuapp.com/maven-central/com.github.marschall/locking-datasource) [![javadoc](https://javadoc.io/badge2/com.github.marschall/locking-datasource/javadoc.svg)](https://javadoc.io/doc/com.github.marschall/locking-datasource) 
==================

A DataSource where every call to `#getConnection()` goes through a global lock.

Use this when `DataSource#getConnection()` or `Driver#connect(String, Properties)` is not thread safe for your JDBC driver and your connection pool creates connections in the calling thread.

```xml
<dependency>
  <groupId>com.github.marschall</groupId>
  <artifactId>locking-datasource</artifactId>
  <version>1.0.0</version>
</dependency>
```

