# Debug trên Docker - Port 5005

## 🚀 Quick Start

### Cách 1: Dùng Script (Khuyến nghị) ⭐

```bash
# Start Docker với debug mode ở port 5005
./scripts/debug-docker.sh
```

Script sẽ:
- ✅ Tự động build và start Docker container
- ✅ Enable debug mode ở port 5005
- ✅ Expose port 5005 để kết nối từ IDE
- ✅ Start PostgreSQL nếu chưa chạy

### Cách 2: Dùng Docker Compose

```bash
# Set environment variable để enable debug
export DEBUG_ENABLED=true
export DEBUG_PORT=5005

# Start services
docker-compose up --build
```

### Cách 3: Custom Debug Port

```bash
# Dùng port khác (ví dụ: 5006)
DEBUG_PORT=5006 ./scripts/debug-docker.sh
```

---

## 🔧 Cấu hình

### Environment Variables

Thêm vào `.env` hoặc export:

```bash
# Enable debug mode
DEBUG_ENABLED=true

# Debug port (default: 5005)
DEBUG_PORT=5005
```

### docker-compose.yml

Đã được cấu hình sẵn:
- Port mapping: `5005:5005` (host:container)
- Environment variable: `DEBUG_ENABLED`
- Debug port sẽ được expose khi `DEBUG_ENABLED=true`

---

## 💻 Kết nối Debugger từ IDE

### IntelliJ IDEA

#### Bước 1: Tạo Remote Debug Configuration

1. **Run** → **Edit Configurations...**
2. Click **+** → **Remote JVM Debug**
3. Cấu hình:
   - **Name**: `Docker Debug (Port 5005)`
   - **Host**: `localhost`
   - **Port**: `5005`
   - **Debugger mode**: `Attach to remote JVM`
4. Click **OK**

#### Bước 2: Start Docker với Debug

```bash
./scripts/debug-docker.sh
```

#### Bước 3: Kết nối Debugger

1. Đặt breakpoint trong code
2. **Run** → **Debug 'Docker Debug (Port 5005)'**
3. Hoặc click icon 🐛 bên cạnh run configuration

### VS Code

#### Bước 1: Tạo Launch Configuration

Tạo hoặc cập nhật `.vscode/launch.json`:

```json
{
    "version": "0.2.0",
    "configurations": [
        {
            "type": "java",
            "name": "Attach to Docker (Port 5005)",
            "request": "attach",
            "hostName": "localhost",
            "port": 5005,
            "projectName": "spring-boot-backend"
        }
    ]
}
```

#### Bước 2: Start Docker với Debug

```bash
./scripts/debug-docker.sh
```

#### Bước 3: Kết nối Debugger

1. Đặt breakpoint trong code
2. Press `F5` hoặc **Run and Debug** → **Attach to Docker (Port 5005)**

---

## ✅ Verify Debug Port

Sau khi start Docker, kiểm tra:

```bash
# Xem logs - phải có dòng "Listening for transport dt_socket"
docker-compose logs backend | grep -i "debug\|jdwp\|5005"

# Kiểm tra port 5005 đang listen
lsof -i :5005

# Hoặc test connection
nc -zv localhost 5005
```

Log sẽ hiển thị:
```
Listening for transport dt_socket at address: 5005
```

---

## 🐛 Troubleshooting

### Lỗi: "Port 5005 already in use"

```bash
# Tìm process đang dùng port 5005
lsof -i :5005

# Kill process (thay PID bằng process ID)
kill -9 <PID>
```

### Lỗi: "Cannot connect to debugger"

1. **Kiểm tra Docker container đang chạy:**
   ```bash
   docker-compose ps backend
   ```

2. **Kiểm tra debug mode đã enable:**
   ```bash
   docker-compose logs backend | grep -i "jdwp\|debug"
   ```

3. **Kiểm tra port mapping:**
   ```bash
   docker-compose ps backend
   # Phải thấy: 0.0.0.0:5005->5005/tcp
   ```

4. **Kiểm tra firewall:**
   - macOS: System Preferences → Security & Privacy → Firewall
   - Đảm bảo không block port 5005

### Debugger không dừng ở breakpoint

1. **Kiểm tra source code mapping:**
   - Đảm bảo source code trong IDE match với code trong Docker
   - Rebuild Docker image nếu đã thay đổi code:
     ```bash
     docker-compose build --no-cache backend
     ```

2. **Kiểm tra debug mode đã enable:**
   - Xem logs có dòng "Listening for transport dt_socket"
   - Verify `DEBUG_ENABLED=true` trong environment

3. **Restart Docker container:**
   ```bash
   docker-compose restart backend
   ```

### Application không start trong Docker

1. **Kiểm tra logs:**
   ```bash
   docker-compose logs backend
   ```

2. **Kiểm tra PostgreSQL:**
   ```bash
   docker-compose ps postgres
   ```

3. **Rebuild image:**
   ```bash
   docker-compose build --no-cache backend
   docker-compose up backend
   ```

---

## 📝 Commands Reference

### Start với Debug

```bash
# Dùng script (khuyến nghị)
./scripts/debug-docker.sh

# Hoặc manual
export DEBUG_ENABLED=true
docker-compose up --build
```

### Stop

```bash
# Stop backend
docker-compose stop backend

# Stop tất cả
docker-compose down
```

### View Logs

```bash
# Xem logs real-time
docker-compose logs -f backend

# Xem logs với filter
docker-compose logs backend | grep -i "error\|exception"
```

### Rebuild

```bash
# Rebuild image
docker-compose build --no-cache backend

# Rebuild và start
docker-compose up --build
```

### Check Status

```bash
# Xem container status
docker-compose ps

# Xem port mapping
docker-compose ps backend
```

---

## 🎯 Best Practices

1. **Dùng script** `debug-docker.sh` để đảm bảo cấu hình đúng
2. **Đặt breakpoint** trước khi kết nối debugger
3. **Rebuild image** sau khi thay đổi code:
   ```bash
   docker-compose build --no-cache backend
   ```
4. **Check logs** nếu có vấn đề:
   ```bash
   docker-compose logs backend
   ```
5. **Disable debug** trong production:
   - Set `DEBUG_ENABLED=false` hoặc không set
   - Debug port sẽ không được expose

---

## 🔄 So sánh: Local Debug vs Docker Debug

| Feature | Local Debug | Docker Debug |
|---------|-------------|--------------|
| **Port** | 5005 | 5005 |
| **Start** | `./scripts/debug-local.sh` | `./scripts/debug-docker.sh` |
| **Environment** | Dev (PostgreSQL local) | Prod (PostgreSQL Docker) |
| **Rebuild** | Không cần | Cần rebuild image |
| **Performance** | Nhanh hơn | Chậm hơn (Docker overhead) |
| **Use Case** | Development | Production-like debugging |

---

## 📚 Tham khảo

- [Spring Boot Debugging](https://docs.spring.io/spring-boot/docs/current/reference/html/features.html#features.developing-applications.debugging)
- [Docker Remote Debugging](https://www.baeldung.com/spring-boot-debugging)
- [IntelliJ Remote Debugging](https://www.jetbrains.com/help/idea/tutorial-remote-debug.html)
- [VS Code Java Debugging](https://code.visualstudio.com/docs/java/java-debugging)

