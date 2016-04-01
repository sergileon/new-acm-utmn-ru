# Docker Online Contester
# Version:  0.1
FROM java 
MAINTAINER              Andrey Grigoryev <i107th@gmail.com>

#INSTALL UI
RUN dnf -y install unzip
RUN dnf -y install httpd
RUN dnf -y install mariadb-server mariadb
RUN dnf -y install php-mysql php-devel php-gd php-pecl-memcache php-pspell php-snmp php-xmlrpc php-xml
RUN dnf -y install php
RUN setsebool httpd_can_network_connect 1
RUN mkdir /home/my/
RUN mkdir /home/my/temp/
RUN mkdir /home/my/acm/
COPY tyumen_acm.zip /var/www/html/
RUN unzip /var/www/html/tyumen_acm.zip -x -d /var/www/html/

#INSTALL PROBLEMSET
COPY archive.zip /home/my/acm/
RUN unzip home/my/acm/archive.zip -x -d /home/my/acm/

#RUN UI
RUN systemctl start httpd
RUN systemctl start mariadb

#INSTALL API
COPY reverse-proxy.conf /etc/httpd/conf.d/
RUN systemctl restart httpd
#ADD http://apache-mirror.rbc.ru/pub/apache/tomcat/tomcat-7/v7.0.68/bin/apache-tomcat-7.0.68.zip /home/my/
COPY apache-tomcat-7.0.68.zip /home/my/
RUN unzip /home/my/apache-tomcat-7.0.68.zip -x -d /home/my/

#INSTALL BACK END
yum groupinstall 'Development Tools'

EXPOSE 80

CMD ["/home/my/apache-tomcat-7.0.68/bin/catalina.sh","start"]