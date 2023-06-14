!/bin/bash
host=$host
port=$port
userName=$userName
password=$password
dbName=$dbName
schemaName=$schemaName
prePath=$prePath
backupsSqlUrl=$backupsSqlUrl
backupsCodeUrl=$backupsCodeUrl
sourceFilePath=$sourceFilePath
reduceUrl=$reduceUrl

mysql_bin="/Users/limingliang/postgreSQL/bin"
psql="${mysql_bin}/psql"


PGPASSWORD=${password} ${psql} -U ${userName} -h ${host} -p ${port} -c "SELECT pg_terminate_backend(pid) FROM pg_stat_activity WHERE datname='${dbName}';"
PGPASSWORD=${password} ${psql} -U ${userName} -h ${host} -p ${port} -c  "DROP DATABASE ${dbName};"
PGPASSWORD=${password} ${psql} -U ${userName} -h ${host} -p ${port} -c "CREATE DATABASE ${dbName};"


tar -zxvf ${reduceUrl} -C ${prePath}

PGPASSWORD=${password} ${psql} -U ${userName} -h ${host} -p ${port} -d ${dbName}  -n ${schemaName}<${backupsSqlUrl}/${dbName}.sql;



cp -r ${backupsCodeUrl}/* ${sourceFilePath}

rm -rf ${backupsSqlUrl}

#create db





