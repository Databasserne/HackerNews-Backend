FROM tomcat:8.0
EXPOSE 8080
RUN rm -fr /usr/local/tomcat/webapps/ROOT
COPY target/backend-1.0.0.war /usr/local/tomcat/webapps/ROOT.war