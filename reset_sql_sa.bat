@echo off
echo Dang khoi phuc va dat lai mat khau cho tai khoan 'sa'...
sqlcmd -S localhost -E -Q "ALTER LOGIN sa WITH PASSWORD='Admin@123'; ALTER LOGIN sa ENABLE;"
echo.
echo Da hoan tat! Bay gio ban co the chay lai Spring Boot.
