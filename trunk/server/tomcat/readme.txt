
tomcat配置调整如下：
    1、目前端口号必须为8090（程序中暂时写死了）
	修改conf/server.xml将8080端口修改为8090
    2、将nextbi的应用上下文配置为"/"，需进行如下的调整：
	修改conf/server.xml文件host节点改为
	 <Host name="localhost"  appBase="deploy"
            unpackWARs="false" autoDeploy="false">

            <Valve className="org.apache.catalina.valves.AccessLogValve" directory="logs"  
               prefix="localhost_access_log." suffix=".txt"
               pattern="%h %l %u %t &quot;%r&quot; %s %b" resolveHosts="false"/>

	    <Context docBase="../webapps/nextbi" path="/" reloadable="false"/>
         </Host>
    3、设置jvm的内存
	在bin/catalina.bat中，设置jvm大小（特别是PermSiz的大小）
	  set JAVA_OPTS=%JAVA_OPTS% -Xms512m -Xmx1536m -XX:PermSize=64M -XX:MaxPermSize=128m
