# Deploy on Sealos DevBox

## DevBox 部署步骤

后端服务在 DevBox 上是一个独立的容器（端口 8080，内网地址 `http://tlias.ns-jnrdqssh:8080`）。

### 1. 克隆仓库

进入 DevBox 后端服务的 workspace，**不要使用 Spring Boot 模板**（或用空白模板）：

```bash
git clone https://github.com/Anndh55/tlias.git /tmp/tlias
cp -r /tmp/tlias/* ./
cp -r /tmp/tlias/.* ./ 2>/dev/null
rm -rf /tmp/tlias
```

如果已经用了模板且 clone 到了子目录 `tlias/`，先清理再移出来：

```bash
# 清理模板文件
rm -f CLAUDE.md README.md entrypoint.sh mvnw mvnw.cmd pom.xml
rm -rf src target

# 把 tlias/ 内容移到根目录
mv tlias/* ./
mv tlias/.* ./ 2>/dev/null
rmdir tlias
```

### 2. 确认结构

```bash
ls -la
# 应该看到: pom.xml  src/  entrypoint.sh  .git/  ...
```

### 3. 启动

```bash
bash entrypoint.sh
```

后端运行在 `8080` 端口。数据库自动连接内网 MySQL（`zxyf-mysql.ns-jnrdqssh.svc:3306`）。

---

## 本地开发

```bash
mvn spring-boot:run
# 启动在 http://localhost:8080
```
