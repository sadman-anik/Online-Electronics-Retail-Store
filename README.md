# JDBC Resource Setup (Important)

Before running the application, you must create BOTH:

- JDBC Connection Pool
- JDBC Resource

The application expects this JNDI resource:

```text
jdbc/RetailStoreDS
```

If the JDBC Resource is missing, deployment will fail with errors like:

```text
javax.naming.NameNotFoundException: RetailStoreDS not found
```

Create the JDBC Resource in GlassFish Admin Console:

```text
Resources → JDBC → JDBC Resources → New
```

Use:

| Field | Value |
|---|---|
| JNDI Name | jdbc/RetailStoreDS |
| Pool Name | RetailStorePool |

Save the resource before deploying the application.
