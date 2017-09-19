FROM tomcat:8.0
EXPOSE 8080
RUN rm -fr /usr/local/tomcat/webapps/ROOT
COPY target/ /usr/local/tomcat/webapps/ROOT