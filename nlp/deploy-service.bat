set "TOMCAT_HOME=E:\Programy\apache-tomcat-8.0.14"
set "CATALINA_HOME=%TOMCAT_HOME%"
set "WAR_DIR=%cd%\target"

copy "%WAR_DIR%\nlp-service.war" "%TOMCAT_HOME%\webapps\nlp.war"
call "%TOMCAT_HOME%\bin\startup.bat"