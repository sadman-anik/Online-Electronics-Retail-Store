# Retail Store Environment Setup Notes

## Technologies

| Technology | Version |
|---|---|
| Java JDK | 17 |
| Apache NetBeans | 25 |
| GlassFish | 7.x |
| MySQL | 8.x |
| MySQL Connector/J | 9.x |
| Jakarta EE | 10 |
| JSF | 4.x |
| EclipseLink | 4.x |
| FakeSMTP | 2.0 |

---

# EnterpriseApps Folder

Create this structure:

```text
$HOME/EnterpriseApps/
├── glassfish7
└── FakeSMTP
```

---

# MySQL

## Start

```bash
sudo /usr/local/mysql/support-files/mysql.server start
```

## Stop

```bash
sudo /usr/local/mysql/support-files/mysql.server stop
```

## Login

```bash
mysql -u root -p
```

## Create Database

```sql
CREATE DATABASE retailestoredb;
```

If the database already exists, use:

```sql
CREATE DATABASE IF NOT EXISTS retailestoredb;
```

---

# GlassFish

## Start

```bash
$HOME/EnterpriseApps/glassfish7/bin/asadmin start-domain
```

## Stop

```bash
$HOME/EnterpriseApps/glassfish7/bin/asadmin stop-domain
```

## Admin Console

```text
http://localhost:4848
```

---

# MySQL Connector/J

Copy connector into:

```text
$HOME/EnterpriseApps/glassfish7/glassfish/domains/domain1/lib
```

Example:

```bash
cp mysql-connector-j-9.3.0.jar \
$HOME/EnterpriseApps/glassfish7/glassfish/domains/domain1/lib/
```

Restart GlassFish after copying.

---

# JDBC Pool

## Pool Name

```text
RetailStorePool
```

## JDBC Resource

```text
jdbc/RetailStoreDS
```

## Datasource Classname

```text
com.mysql.cj.jdbc.MysqlDataSource
```

## Additional Properties

```text
serverName = localhost
portNumber = 3306
databaseName = retailestoredb
user = root
password = your_mysql_password
useSSL = false
allowPublicKeyRetrieval = true
```

Expected:

```text
Ping Succeeded
```

---

# FakeSMTP

## Run

```bash
cd $HOME/EnterpriseApps/FakeSMTP

java --add-exports java.desktop/com.apple.eawt=ALL-UNNAMED \
-jar fakeSMTP-2.0.jar
```

## Port

```text
2525
```

Click:

```text
Start Server
```

---

# Optional Scripts

Optional helper scripts are included:

```bash
./start-retailstore-env.sh
./stop-retailstore-env.sh
./restart-retailstore-env.sh
```

You can either:
- start services manually
OR
- use the scripts.

---

# Build and Run Project

## 1. Start Environment

Start:
- MySQL
- GlassFish
- FakeSMTP

---

## 2. Open Project

```text
File → Open Project
```

---

## 3. Build Project

```text
Right click project → Build
```

or:

```text
Right click project → Clean and Build
```

Expected:

```text
BUILD SUCCESSFUL
```

---

## 4. Run Project

```text
Right click project → Run
```

NetBeans will:
- deploy WAR to GlassFish
- create database tables
- open browser

---

# Application URL

```text
http://localhost:8080/
```

---

# Notes

Use:

```text
com.mysql.cj.jdbc.MysqlDataSource
```

NOT:

```text
com.mysql.jdbc.jdbc2.optional.MysqlDataSource
```

Keep FakeSMTP running during registration/recovery.
