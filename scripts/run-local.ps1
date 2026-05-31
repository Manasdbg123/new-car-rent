param(
    [string]$MysqlServiceName = "MYSQL80"
)

$mysql = Get-Service -Name $MysqlServiceName -ErrorAction SilentlyContinue
if ($mysql -and $mysql.Status -ne "Running") {
    Write-Host "MySQL service '$MysqlServiceName' is installed but stopped."
    Write-Host "Open PowerShell as Administrator and run:"
    Write-Host "  Start-Service -Name $MysqlServiceName"
    exit 1
}

$port = Test-NetConnection -ComputerName localhost -Port 3306 -WarningAction SilentlyContinue
if (-not $port.TcpTestSucceeded) {
    Write-Host "MySQL is not reachable on localhost:3306."
    Write-Host "Start MySQL first, then run the application again."
    Write-Host "If MySQL is installed as a Windows service, use an Administrator PowerShell:"
    Write-Host "  Start-Service -Name $MysqlServiceName"
    exit 1
}

.\mvnw.cmd spring-boot:run
