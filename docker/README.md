# Environment Setup with Docker

There are issues with running Docker on Windows 10.<br/>
If you are running Windows 10 follow steps 1-5.<br/>
For other platforms follow [installation instructions](https://docs.docker.com/engine/installation/) and skip to step 7.

1.	[Download](https://www.virtualbox.org/wiki/Downloads) and install Oracle VirtualBox VM ;

2.	[Download](http://isoredirect.centos.org/centos/7/isos/x86_64/CentOS-7-x86_64-Minimal-1511.iso) CentOS image;

3.	Create CentOS image with VirtualBox: 
	1.	Create Virtual Machine: enter Oracle VirtualBox
	2.	Click Create and enter name and choose OS Type and its version:
	3.	Point volume of RAM (It is recommended to use 2Gb):
	4.	Create new hard drive.
	5.	Then choose just created VM and Launch it:
	6.	Choose CentOS Image:
	7.	Install CentOS operation system;
	8.	Shutdown the system;

4.	Install Network with Internet connection (only if your base computer have internet connection): 
	1.	Open preferences and change Network adapter setup:
	2.	Run command: vi /etc/sysconfig/network-scripts/ifcfg-enp0s3 and change ONBOOT=yes;
	3.	Reboot machine with command: reboot
	4.	Test it using command: 

5.	Install docker:
	1.	Run following commands and choose y whenever:
		1.	yum update;
		2.	curl -sSL https://get.docker.com/ | sh
		3.	service docker start
		4.	chkconfig docker on
		5.	docker run hello-world
	2.	Last message shows that you correctly install docker;

6. Build images
	docker build --tag="java" /home/my/java/
	docker build --tag="online-contester-ui" /home/my/oc-ui/
	docker build --tag="online-contester-db" /home/my/oc-db/
	docker build --tag="online-contester-api" /home/my/oc-api/
	
	docker run -d -p 3306:3306 --name='oc-db-node' online-contester-db
	docker run -d -p 80:80 --name='oc-ui-node' --link oc-db-node:oc-db-node online-contester-ui