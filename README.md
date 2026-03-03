## 翰文在线教学管理系统（HanWenOnline）

前后端分离的在线教学管理系统示例项目。

## 项目结构

- `frontEnd/hanwen/`：前端（Vue 2 + Element UI）
- `BackEnd/`：后端（Spring Boot + Spring Cloud 微服务）
  - `gateway`：网关（Spring Cloud Gateway），端口 `8500`
  - `user-manage`：用户服务，端口 `8100`
  - `course-manage`：课程/班级服务，端口 `8200`
  - `resource-manage`：资源服务（含上传），端口 `8300`
  - `online-study`：在线学习服务（视频/文档等），端口 `8400`
  - `consumer`：聚合/消费端服务（前端主要调用它），端口 `8000`
- `BackEnd/hanwenonline.sql`：MySQL 初始化脚本（另有一份同名脚本在 `其它/` 目录）

## 技术栈

### 前端

- Vue `2.6.x`、Vue Router `3.x`、Vuex `3.x`
- Element UI `2.15.x`
- Axios
- Vue CLI `5.x`

### 后端

- Java `8`
- Spring Boot（项目内多处使用 `2.7.11`）
- Spring Cloud / Spring Cloud Alibaba（Nacos 注册发现）
- Spring Cloud Gateway
- Ribbon、Hystrix（部分服务启用）
- MyBatis
- MySQL（脚本基于 `8.0.15` 导出）

## 环境准备

- JDK 8
- Maven
- Node.js（建议 16+）与 npm
- MySQL 8.x
- Nacos（服务注册中心，默认地址 `localhost:8848`）

## 数据库初始化

1. 创建数据库并导入脚本：

```sql
-- 直接执行 BackEnd/hanwenonline.sql
CREATE DATABASE IF NOT EXISTS hanwenonline DEFAULT CHARSET utf8;
```

2. 后端默认数据库连接（各服务的 `application.yml` 中一致）：
   - **地址**：`jdbc:mysql://localhost:3306/hanwenonline`
   - **用户名**：`root`
   - **密码**：`123456`

3. 脚本中内置了示例用户数据：`user.password` 字段为 MD5（例如 `e10adc...` 对应明文 `123456`）。

## 后端启动

### 1) 启动 Nacos

保证 Nacos 已在本机启动并监听 `8848`（各服务配置为 `spring.cloud.nacos.discovery.server-addr: localhost:8848`）。

### 2) 启动各微服务（推荐启动顺序）

可使用 IDE 直接运行每个模块的 `main` 方法（位于 `src/main/java/.../*Application.java`）：

- `user-manage`：`com.yws.usermanage.UserManageApplication`（`8100`）
- `course-manage`：`com.yws.coursemanage.CourseApplication`（`8200`）
- `resource-manage`：`com.yws.resourcemanage.ResourceManageApplication`（`8300`）
- `online-study`：`com.yws.onlinestudymanage.OnlineStudyApplication`（`8400`）
- `consumer`：`com.yws.consumer.ConsumerApplication`（`8000`）
- `gateway`：`com.yws.gateway.GatewayApplication`（`8500`）

也可以在 `BackEnd/` 目录使用 Maven（示例）：

```bash
# 在 BackEnd/ 下执行
mvn -DskipTests clean package
```

> 说明：各模块为 `war` 打包方式；开发期建议直接在 IDE 运行。

## 前端启动

进入前端目录安装依赖并启动：

```bash
cd frontEnd/hanwen
npm install
npm run serve
```

默认会在本地起一个开发服务器（Vue CLI 默认端口通常为 `8080`，如端口占用会自动调整）。

## 前后端联调与接口说明

前端接口地址集中在 `frontEnd/hanwen/src/global.js`，默认：

- **网关**：`http://localhost:8500`
  - 用户相关：`/user/login`、`/user/regist`
  - 在线学习资源播放/查看：`/online-study/video-play`、`/online-study/pdf-view`
- **consumer 聚合服务**：`http://localhost:8000`
  - 课程：`/course/...`
  - 班级：`/classroom/...`
  - 学生：`/student/...`
  - 资源（部分）：`/resource/...`
  - 在线学习（部分）：`/online-study/...`
- **resource-manage 资源服务**：`http://localhost:8300`
  - 分片上传：`/resource/chunk-upload`

如需部署到服务器或修改域名/IP，直接修改 `frontEnd/hanwen/src/global.js` 中的 `HOSTURL`。

## 常见问题

### 资源上传失败/文件过大

`resource-manage` 配置了上传大小限制（见 `BackEnd/resource-manage/src/main/resources/application.yml`）：

- `spring.servlet.multipart.max-file-size: 1MB`

可按需要调大并重启服务。

### 网关只转发部分路径

当前 `gateway` 的路由配置仅启用了：

- `/user/**` -> `springcloud-user-manage-8100`
- `/online-study/**` -> `springcloud-online-study-8400`

如需通过网关转发更多服务，可在 `BackEnd/gateway/src/main/resources/application.yml` 增加/启用对应路由。

