# Docker Online Contester
# Version:  0.1
FROM fedora:latest
MAINTAINER              Andrey Grigoryev <i107th@gmail.com>
ENV JAVA_VERSION 8u66
ENV BUILD_VERSION b17

#Oracle Java installation:
# Upgrading system
RUN dnf -y upgrade 
RUN dnf -y install wget tar procps-ng less curl sed gpg

# Downloading Java
RUN wget --no-cookies --no-check-certificate --header "Cookie: oraclelicense=accept-securebackup-cookie" "http://download.oracle.com/otn-pub/java/jdk/$JAVA_VERSION-$BUILD_VERSION/jdk-$JAVA_VERSION-linux-x64.rpm" -O /tmp/jdk-8-linux-x64.rpm

RUN dnf -y install /tmp/jdk-8-linux-x64.rpm

RUN alternatives --install /usr/bin/java java /usr/java/latest/bin/java 200000
RUN alternatives --install /usr/bin/javaws javaws /usr/java/latest/bin/javaws 200000
RUN alternatives --install /usr/bin/javac javac /usr/java/latest/bin/javac 200000

ENV JAVA_HOME /usr/java/latest
