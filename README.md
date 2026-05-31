# Car Rental Backend

## Run From IntelliJ

Start MySQL and Redis first:

```powershell
docker compose up -d mysql redis
```

Then run `com.carrental.CarRentalApplication` from IntelliJ.

On this machine, MySQL is installed as the Windows service `MYSQL80`. If the app fails with `Communications link failure`, start MySQL in an Administrator PowerShell:

```powershell
Start-Service -Name MYSQL80
```

You can verify the port with:

```powershell
Test-NetConnection localhost -Port 3306
```

Or run the guarded local script:

```powershell
.\scripts\run-local.ps1
```

If you are using a locally installed MySQL server instead of Docker, run:

```sql
CREATE DATABASE IF NOT EXISTS car_rental;
CREATE USER IF NOT EXISTS 'car_rental'@'localhost' IDENTIFIED BY 'car_rental';
GRANT ALL PRIVILEGES ON car_rental.* TO 'car_rental'@'localhost';
FLUSH PRIVILEGES;
```

Or run the setup script and enter your MySQL root password when prompted:

```powershell
.\scripts\setup-mysql-user.ps1
```

The default app connection is:

```text
jdbc:mysql://localhost:3306/car_rental?useSSL=false&allowPublicKeyRetrieval=true&serverTimezone=UTC
```

## Run Everything With Docker

```powershell
docker compose -f docker-compose.yml -f docker-compose.api.yml up --build
```

Swagger UI:

```text
http://localhost:8080/swagger-ui.html
```
