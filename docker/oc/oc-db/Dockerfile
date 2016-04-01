# Docker Online Contester
# Version:  0.1
FROM java 
MAINTAINER              Andrey Grigoryev <i107th@gmail.com>

#INSTALL DB
RUN dnf -y install unzip
RUN dnf -y install mariadb-server mariadb
RUN systemctl enable mariadb

EXPOSE 3306

CMD ["/usr/sbin/init"]