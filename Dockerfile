FROM tomcat:8.0
EXPOSE 8080

RUN echo oracle-java8-installer shared/accepted-oracle-license-v1-1 select true | debconf-set-selections &&   apt-get update &&   apt-get install -y software-properties-common &&  add-apt-repository -y ppa:webupd8team/java &&   apt-get update &&   apt-get install -y oracle-java8-installer  &&   rm -rf /var/lib/apt/lists/* &&   rm -rf /var/cache/oracle-jdk8-installer

RUN rm -fr /usr/local/tomcat/webapps/ROOT
COPY target/backend-1.0.0.war /usr/local/tomcat/webapps/ROOT.war
