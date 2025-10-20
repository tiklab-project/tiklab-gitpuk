#!/bin/sh

DIRS=$(dirname "$PWD")

APP_MAIN="io.tiklab.gitpuk.starter.GitPukApplication"
APPLY=gitpuk

DATA_HOME=""
YAML=${DIRS}/conf/application.yaml

create_user(){
   useradd -m -s /bin/bash postgres
}

create_user

create_home(){

  data_home=$(awk -F': ' '/DATA_HOME:/ {print $2}' "${YAML}")

  # 创建目录及其上级目录(如果不存在)
  mkdir -p "${data_home}"

  DATA_HOME=${data_home}
  # 检查目录是否创建成功
  if [ -d "${data_home}" ]; then
    echo "DATA_HOME: ${data_home}"
  else
    echo "================================================================================================================"
    echo "DATA_HOME：${data_home} initialized Failed!"
    echo "请更改文件${YAML}中的DATA_HOME字段，配置应用可以访问的地址！"
    echo "${APP_MAIN} start [failed]"
    echo "================================================================================================================"
    exit 1
  fi

}

JDK_VERSION=jdk-16.0.2
valid_jdk(){
  if [ -d "${DIRS}/embbed/${JDK_VERSION}" ]; then
      JAVA_HOME="${DIRS}/embbed/${JDK_VERSION}"
  else
      echo "Unable to find embbed jdk!"
      exit 1;
  fi
}

PGSQL_VERSION=pgsql-10.23
valid_postgresql(){
  if [ -d "${DIRS}/embbed/${PGSQL_VERSION}/bin" ]; then
      #echo "user embbed postgresql exist"
      rm -rf ${DIRS}/embbed/${PGSQL_VERSION}/${PGSQL_VERSION}.tar.gz
  else
      #echo "unzip postgresql file ....."
      tar -xzf "${DIRS}/embbed/${PGSQL_VERSION}/${PGSQL_VERSION}.tar.gz" -C "${DIRS}/embbed"
      #echo "unzip postgresql success!"
      rm -rf ${DIRS}/embbed/${PGSQL_VERSION}/${PGSQL_VERSION}.tar.gz
  fi
}

APP_HOME=${DIRS}
export APP_HOME

create_home

enableApply(){

      APPLYDIR="$PWD"

      serverName=enable-${APPLY}.service

      applyserver=/etc/systemd/system/${serverName}

      if [ ! -e "${applyserver}" ]; then
cat << EOF >  ${applyserver}
[Unit]
Description=Start Tiklab Apply
After=network.target remote-fs.target nss-lookup.target

[Service]
EOF

echo Environment=\"DIR=${APPLYDIR}\" >> ${applyserver}

cat << EOF >> ${applyserver}
ExecStart=/bin/bash -c 'cd "\$DIR"; sh startup.sh'
Type=forking

[Install]
WantedBy=multi-user.target
EOF

  touch ${applyserver}
  chmod 644 ${applyserver}
  systemctl enable ${serverName}

  else
cat << EOF >  ${applyserver}
[Unit]
Description=Start Tiklab Apply
After=network.target remote-fs.target nss-lookup.target

[Service]
EOF

echo Environment=\"DIR=${APPLYDIR}\" >> ${applyserver}
cat << EOF >> ${applyserver}
ExecStart=/bin/bash -c 'cd "\$DIR"; sh startup.sh'
Type=forking

[Install]
WantedBy=multi-user.target
EOF
fi

}

enableApply

JAVA_OPTS=""
add_javaOpts(){
  APP_CONFIG=${DIRS}/conf
  APP_LOG=${DIRS}/logs

  JAVA_OPTS="$JAVA_OPTS -server -Xms1024m -Xmx1024m -XX:+UseG1GC -XX:MaxGCPauseMillis=200 -Xloggc:$APP_LOG/gc.log -Dfile.encoding=UTF-8"
  JAVA_OPTS="$JAVA_OPTS -DlogPath=$APP_LOG -Duser.timezone=GMT+08"
  JAVA_OPTS="$JAVA_OPTS -Dconf.config=file:${APP_CONFIG}"
  JAVA_OPTS="$JAVA_OPTS --add-opens java.base/java.lang=ALL-UNNAMED --add-opens java.sql/java.sql=ALL-UNNAMED  -classpath"
}

CLASSPATH=${DIRS}/conf
add_classpath(){
  #加载私有依赖
  for appJar in "${DIRS}"/lib/*.jar;
  do
     CLASSPATH="$CLASSPATH":"$appJar"
  done
}

PID=0
getPID(){
    javaps=`$JAVA_HOME/bin/jps -l | grep $APP_MAIN`
    if [ -n "$javaps" ]; then
        PID=`echo $javaps | awk '{print $1}'`
    else
        PID=0
    fi
}

startup(){
    getPID
    echo "================================================================================================================"
    if [ $PID -ne 0 ]; then
        echo "$APP_MAIN already started(PID=$PID)"
        echo "================================================================================================================"
        exit 0
    else
        echo -n "starting $APP_MAIN"
        if [ ! -d "$APP_LOG" ]; then
            mkdir "$APP_LOG"
        fi

        nohup $JAVA_HOME/bin/java $JAVA_OPTS $CLASSPATH $APP_MAIN > /dev/null 2>&1 &

        for i in $(seq 5); do
            echo -n "."
            sleep 1
        done

        getPID

        if [ $PID -ne 0 ]; then
            echo "(PID=$PID)[success]"
            find_address
        else
            echo "[failed]"
        fi
    fi
}

get_local_ip() {
    # 获取所有本机 IP（排除 127.0.0.1）
    ips=$(hostname -I 2>/dev/null | tr ' ' '\n' | grep -v "^127\.")

    # 优先匹配 192.168.*
    ip=$(echo "$ips" | grep "^192\.168\." | head -n 1)
    if [ -n "$ip" ]; then
        echo "$ip"
        return 0
    fi

    # 其次匹配 172.*
    ip=$(echo "$ips" | grep "^172\." | head -n 1)
    if [ -n "$ip" ]; then
        echo "$ip"
        return 0
    fi

    # 如果都没有，就返回第一个非 127 的地址
    echo "$ips" | head -n 1
}

find_address(){
  server_port=$(awk -F": *" '/^server:/ {
      inf=1
      next
  }
  inf && /^  port:/ {
      print $2
      exit
  }' "${YAML}")


  echo "The application is starting. Please wait ......"
  sleep 1


  echo "============================= Enter the following link in the browser to access ================================"
  echo "http://$(get_local_ip):${server_port}"
  echo "================================================================================================================"
}

copy_bin() {
  # shellcheck disable=SC3043
  local bin_name="$1"   # 二进制文件名
  # shellcheck disable=SC3043
  local src_path="${2}"   # 源文件路径
  # shellcheck disable=SC3043
  local dest_dir="/usr/bin"

  # 检查源文件是否存在
  if [ ! -f "$src_path" ]; then
    echo "❌ Source file not found: $src_path"
    return 1
  fi

  # 确保目标目录存在
  if [ ! -d "$dest_dir" ]; then
    echo "❌ Destination directory not found: $dest_dir"
    return 1
  fi

  # 复制并覆盖
  echo "📦 Installing $bin_name to $dest_dir..."
  sudo \cp -f "$src_path" "$dest_dir/$bin_name" && sudo chmod +x "$dest_dir/$bin_name"

  # 检查是否复制成功
  if [ -x "$dest_dir/$bin_name" ]; then
    echo "✅ $bin_name installed successfully at $dest_dir/$bin_name"
    return 0
  else
    echo "❌ Failed to install $bin_name"
    return 1
  fi
}


start(){

  valid_jdk
  add_javaOpts
  add_classpath

  # shellcheck disable=SC2038
  if command -v dos2unix > /dev/null 2>&1; then
      find "${DIRS}/" -name '*.sh' | xargs dos2unix > /dev/null 2>&1
  else
      echo "dos2unix skip update "
  fi

  startup


  copy_bin "${APPLY}" "${DIRS}/bin/${APPLY}"

}

start











