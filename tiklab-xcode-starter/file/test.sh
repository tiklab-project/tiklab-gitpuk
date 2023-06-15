!/bin/bash


mysql_bin="/Users/limingliang/postgreSQL/bin"
psql="${mysql_bin}/psql"


#PGPASSWORD=darth2020 ${psql} -U postgres -h 172.10.1.10 -p 5432 -d tiklab_xcode -c "SELECT table_name FROM information_schema.tables WHERE table_schema='public'"




PGPASSWORD=darth2020 ${psql} -U postgres -h 172.10.1.10 -p 5432 -c "SELECT pg_terminate_backend(pid) FROM pg_stat_activity WHERE datname='tiklab_xcode';"


PGPASSWORD=darth2020 ${psql} -U postgres -h 172.10.1.10 -p 5432 -c  "DROP DATABASE tiklab_xcode;"

PGPASSWORD=darth2020 ${psql} -U postgres -h 172.10.1.10 -p 5432 -c "CREATE DATABASE tiklab_xcode;"