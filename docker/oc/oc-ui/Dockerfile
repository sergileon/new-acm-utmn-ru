# Docker Online Contester
# Version:  0.1
FROM java 
MAINTAINER              Andrey Grigoryev <i107th@gmail.com>

#INSTALL UI
RUN dnf -y install unzip
RUN dnf -y install httpd
RUN dnf -y install php-mysql php-devel php-gd php-pecl-memcache php-pspell php-snmp php-xmlrpc php-xml
RUN dnf -y install php
#RUN setsebool httpd_can_network_connect 1
RUN mkdir /home/my/
RUN mkdir /home/my/temp/
RUN mkdir /home/my/acm/
COPY tyumen_acm.zip /var/www/html/
RUN unzip /var/www/html/tyumen_acm.zip -x -d /var/www/html/
COPY reverse-proxy.conf /etc/httpd/conf.d/
RUN systemctl enable httpd

EXPOSE 80

CMD ["/usr/sbin/init"]