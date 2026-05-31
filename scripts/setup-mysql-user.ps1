param(
    [string]$RootUser = "root",
    [string]$AppDatabase = "car_rental",
    [string]$AppUser = "car_rental",
    [string]$AppPassword = "car_rental"
)

$mysql = Get-Command mysql -ErrorAction SilentlyContinue
if (-not $mysql) {
    $defaultMysql = "C:\Program Files\MySQL\MySQL Server 8.0\bin\mysql.exe"
    if (Test-Path $defaultMysql) {
        $mysqlPath = $defaultMysql
    } else {
        Write-Host "mysql.exe was not found. Add MySQL Server bin folder to PATH or install MySQL client tools."
        exit 1
    }
} else {
    $mysqlPath = $mysql.Source
}

$sql = @"
CREATE DATABASE IF NOT EXISTS $AppDatabase;
CREATE USER IF NOT EXISTS '$AppUser'@'localhost' IDENTIFIED BY '$AppPassword';
ALTER USER '$AppUser'@'localhost' IDENTIFIED BY '$AppPassword';
GRANT ALL PRIVILEGES ON $AppDatabase.* TO '$AppUser'@'localhost';
CREATE USER IF NOT EXISTS '$AppUser'@'127.0.0.1' IDENTIFIED BY '$AppPassword';
ALTER USER '$AppUser'@'127.0.0.1' IDENTIFIED BY '$AppPassword';
GRANT ALL PRIVILEGES ON $AppDatabase.* TO '$AppUser'@'127.0.0.1';
FLUSH PRIVILEGES;
"@

Write-Host "Enter the MySQL password for '$RootUser' when prompted."
$sql | & $mysqlPath -u $RootUser -p --protocol=TCP --host=localhost
$exitCode = $LASTEXITCODE

if ($exitCode -ne 0) {
    Write-Host "MySQL setup failed. Check the root/admin username and password."
    exit $exitCode
}

Write-Host "MySQL database and app user are ready:"
Write-Host "  database: $AppDatabase"
Write-Host "  username: $AppUser"
Write-Host "  password: $AppPassword"
