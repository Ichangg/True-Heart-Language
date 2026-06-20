@echo off
echo Dang chay Maven o che do Debug de tim loi that su...
call mvnw.cmd clean compile -e -X > debug_log.txt 2>&1
echo Da ghi log vao file debug_log.txt
