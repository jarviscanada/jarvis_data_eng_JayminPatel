# Linux Cluster Monitoring Agent

# Introduction
The Linux Cluster Monitoring Agent (LCMA) is a resource management tool that consistently records a system's CPU, memory, and disk I/O consumption data. The LCMA can be deployed multiple nodes on a Linux cluster, connected through a network switch. Each node is equipped with a Linux-based operating system: CentOS 7, and shares a PostgreSQL relational database management system (RDBMS) with the other nodes in the cluster. Several bash scripts are constructed to set up the database instance through docker containers as well as collect and update the database with resource usage data over specified one-minute intervals. The following include some of the key technologies used in this implementation:

- Docker
- Bash
- PostgreSQL
- Crontab

# Quick Start

- Start a PostgreSQL instance using psql_docker.sh

```bash
# Get the latest PostgreSQL image
docker pull postgres

# Create PostgreSQL container / instance from image using psql_docker.sh
./scripts/psql_docker.sh create postgres password

# Start PostgreSQL container / instance using psql_docker.sh
./scripts/psql_docker.sh start
```

- Create tables using ddl.sql

```bash
# Connect to the PostgreSQL container / instance
psql -h localhost -U postgres -W

# Create host_agent database
postgres=# CREATE DATABASE host_agent;

# Create host_info and host_usage tables
psql -h localhost -U postgres -d host_agent -f ./sql/ddl.sql
```

- Insert hardware specifications data into the database using host_info.sh

```bash
./scripts/host_info.sh localhost 5432 host_agent postgres password
```

- Insert hardware usage data into the database using host_usage.sh

```bash
./scripts/host_usage.sh localhost 5432 host_agent postgres password
```

- Crontab setup

```bash
# Edit crontab tasks
crontab -e

# Add this to crontab
* * * * * bash <full_path>/linux_sql/scripts/host_usage.sh localhost 5432 host_agent postgres password > /tmp/host_usage.log

# List crontab jobs to check if the task is running
crontab -l
```

# Implementation

1. `psql_docker.sh` is used to creating, starting and stopping PostgreSQL docker containers
2. `ddl.sql` is used to create data tables in the `host_agent` database
3. `host_info.sh` and `host_usage.sh` is used to collect and insert hardware specifications data and hardware usage data respectively
4. `queries.sql` is equipped with useful queries relevant to resource management

## Scripts

- `psql_docker.sh` This script controls the docker container with postgres server. It is used for the creation of the server container and allows the user to start and stop it.

```bash
# Create a psql instance
./script/psql_docker.sh create [db_username] [db_password]

# Start the psql container
./script/psql_docker.sh start

# Stop the psql container
./script/psql_docker.sh stop
```

- `ddl.sql` is used to create data tables

```bash
psql -h [psql_host] -U [psql_user] -d [db_name] -f ./sql/ddl.sql
```

- `host_info.sh` This script parses the hardware specifications of the machine it is run on, provided by the lscpu command. Then, it inserts the data into the host_info table of the provided postgres server.

```bash
./scripts/host_info.sh [psql_host] [psql_port] [db_name] [psql_user] [psql_password]
```

- `host_usage.sh` This script parses the hardware usage data of the machine it is run on, provided by the vmstat and df commands. Then, it inserts the data into the host_info table of the provided postgres server.

```bash
./scripts/host_usage.sh [psql_host] [psql_port] [db_name] [psql_user] [psql_password]
```

- `crontab` is used to execute bash scripts over specified time intervals

```bash
# Edit crontab tasks
crontab -e

# Add this to crontab
* * * * * bash <full_path>/linux_sql/scripts/host_usage.sh [psql_host] [psql_port] [db_name] [psql_user] [psql_password] > /tmp/host_usage.log
```

- `queries.sql` is equipped with useful queries relevant to resource management
    - Group and display hosts by hardware information
    - Displays average memory usages over 5-minute intervals
    - Detects host failures, when less than 3 data points are collected within 5 minutes

```bash
psql -h [psql_host] -U [psql_user] -d [db_name] -f ./sql/queries.sql
```

## Database Modeling

- `host_info`

Column Name | Data Type | Description
---------- | ---------- | ----------
id | SERIAL | Unique identifier used as primary key
hostname | VARCHAR | Unique name of the host
cpu_number | INT | Number of CPUs
cpu_architecture | VARCHAR | CPU architecture
cpu_model | VARCHAR | CPU model
cpu_mhz | FLOAT | CPU processor speed (MHz)
L2_cache | INT | Level 2 cache (kB)
total_mem | INT | Total memory (kB)
timestamp | TIMESTAMP | Record entry time (UTC)

- `host_usage`

Column Name | Data Type | Description
---------- | ---------- | ----------
timestamp | TIMESTAMP | Record entry time (UTC)
host_id | SERIAL | Foreign key referencing `id` in `host_info`
memory_free | INT | Used memory (MB)
cpu_idle | INT | CPU idle time (%)
cpu_kernel | INT | Kernel processing time (%)
disk_io | INT | Number of disk I/O processes
disk_available | INT | Available disk space (MB)

# Test

All scripts are written to fail-fast, meaning if any of the inputs are not correct, the script will terminate. Each script in the developed project was tested manually by comparing the execution outcomes with the expected results:

1. The psql_docker.sh script was tested by verifying that the docker container with postgres sever was successfully created, started and stopped.

2. The ddl.sql script was tested by verifying that corresponding database and tables are created in postgres server.

3. The host_info.sh and host_usage.sh scripts were tested by running them and querying the corresponding tables to verify the insert operations. Similarly, the correct configuration of schedule crontab is verified by ensuring that a new entry is inserted every minute.

4. The queries.sql script was tested by generating synthetic data and running the queries on it.

# Deployment

- All source code is managed through GitHub
- PostgreSQL image can be retrieved from Docker Hub

# Improvements

- Develop an API that would enable to query the data without providing access to SQL.
- Severe resource consumption alerts.
- Add scripts for the common analytics features.