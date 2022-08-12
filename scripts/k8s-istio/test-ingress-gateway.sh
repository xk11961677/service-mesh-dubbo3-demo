#!/bin/bash
for i in {1..500}
 do
    curl -s -k 'GET' -b 'stuff' 'http://172.18.30.246/dubbo-demo-consumer/hello';
    echo "\r\n"
    sleep 1;  
 done
echo "脚本执行结束"