Pre-steps: create database:

mysql -uroot < "C:\Users\Андрей\Documents\new-acm-utmn-ru\db_create.sql"

Change httpd.conf:

http://abhinavsingh.com/making-cross-sub-domain-ajax-xhr-requests-using-mod_proxy-and-iframes/

LoadModule proxy_module libexec/apache2/mod_proxy.so
LoadModule proxy_connect_module libexec/apache2/mod_proxy_connect.so
LoadModule proxy_http_module libexec/apache2/mod_proxy_http.so

To run REST web-service:
1. Run it at server from eclipse
2. go to the browser and check: http://localhost:8080/olymp/api/rest/serverstatus
3. TO test POST method use curl utility (you could download it from http://curl.haxx.se/download.html) and next command:

curl -i -X POST --header "Content-Type: text/plain" --data-binary @source.cpp http://localhost:8080/olymp/api/rest/submit/cpp/1/1
curl -i -X POST --header "Content-Type: text/plain" --data-binary @source.cs http://localhost:8080/olymp/api/rest/submit/cs/1/1/