TOMCAT_HOME="/e/Programy/apache-tomcat-8.0.14"
WAR_DIR=`PWD`"/target"

cp "$WAR_DIR/nlp-service.war" "$TOMCAT_HOME/webapps/nlp.war"
cd "$TOMCAT_HOME/bin"
"$TOMCAT_HOME/bin/startup.sh"