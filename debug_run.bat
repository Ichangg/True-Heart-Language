@echo off
echo Dang chay Spring Boot o che do Debug de tim ra loi that su...
call mvnw.cmd clean spring-boot:run -e -X > debug_run_log.txt 2>&1
echo Da ghi log vao file debug_run_log.txt
