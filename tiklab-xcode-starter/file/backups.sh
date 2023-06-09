!/bin/bash
host=$host
port=$port
userName=$userName
password=$password
dbName=$dbName
schemaName=$schemaName
backupsUrl=$backupsUrl
backupsCodeUrl=$backupsCodeUrl
sourceFilePath=$sourceFilePath
reduceName=$reduceName
length=${length}

mysql_bin="/Users/limingliang/postgreSQL/bin"
psql="${mysql_bin}/psql"
pg_dump="${mysql_bin}/pg_dump"
current_time=$(date +%s)

PGPASSWORD=${password} ${pg_dump} -U ${userName} -h ${host} -p ${port} -d ${dbName} -n ${schemaName}>${backupsUrl}/${dbName}.sql


while read line ;  do
cp -r ${line} ${backupsCodeUrl}
done <<< ${sourceFilePath}

tar -zcvf ${reduceName}.tar.gz  --strip-components=${length} ${backupsUrl}

rm -rf ${backupsUrl}


#create db




