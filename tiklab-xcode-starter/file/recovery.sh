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
#mysql_bin=$pgsqlUrl
psql="${mysql_bin}/psql"


#PGPASSWORD=${password} ${psql} -U ${userName} -h ${host} -p ${port} -c "SELECT pg_terminate_backend(pid) FROM pg_stat_activity WHERE datname='${dbName}';"

echo PGPASSWORD=${password} ${psql} -U ${userName} -d ${dbName} -h ${host} -p ${port} -c "DROP SCHEMA IF EXISTS ${schemaName} CASCADE;"

PGPASSWORD=${password} ${psql} -U ${userName} -d ${dbName} -h ${host} -p ${port} -c "DROP SCHEMA IF EXISTS ${schemaName} CASCADE;"
PGPASSWORD=${password} ${psql} -U ${userName} -h ${host} -p ${port} -d ${dbName} -c "CREATE schema  ${schemaName};"


PGPASSWORD=${password} ${psql} -U ${userName} -h ${host} -p ${port} -d ${dbName}  -n ${schemaName}<${backupsSqlUrl}${dbName}.sql;


#tar -zxvf ${reduceUrl} -C ${prePath}
#cp -r ${backupsCodeUrl}/* ${sourceFilePath}

#rm -rf ${backupsSqlUrl}

#create db





