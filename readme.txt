To tun REST web-service:
1. Run it at server from eclipse
2. go to the browser and check: http://localhost:8080/olymp/api/rest/serverstatus
3. TO test POST method use curl utility (you could download it from http://curl.haxx.se/download.html) and next command:
	curl -H "Content-Type: application/json" -X POST -d "{\"code\":\"123\"}" http://localhost:8080/olymp/api/rest/submit