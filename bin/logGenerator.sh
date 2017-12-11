#!/usr/bin/env bash
while :; do
    echo $((i++))" 正在生成测试的日志...  OK!" $( head -100 /dev/urandom | cksum | cut -f1 -d" ") >> test.log ;
    # $( cat /proc/sys/kernel/random/uuid)
    sleep 1;
done