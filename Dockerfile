FROM jetty
EXPOSE 8080
COPY target/backend-1.0.0.war /usr/lib/jetty/webapps/ROOT.war