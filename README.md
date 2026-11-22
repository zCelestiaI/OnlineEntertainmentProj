# Online Entertainment – Java Web

An academic video-entertainment web application with user-facing video browsing, account management, favorites, an administrator dashboard, and a community-chat demonstration.

**Status:** source-code portfolio project. This is **not yet a public, production-ready video-upload platform**. Videos are currently managed by the administrator; user-created channels, direct video uploads and video storage/transcoding are not implemented in this version.

## Features implemented

- Registration, login/logout, user profile, password-management pages and session-based Admin/User authorization.
- Search and pagination on the public video listing; view video details and record view counts.
- Like/unlike videos and browse a user's favorites.
- Admin CRUD for videos and user accounts.
- Reports for favorites, users who liked videos, and recorded shares.
- WebSocket community-chat prototype: `ChatController`, `JsonChatServerEndpoint` and `json-chat.js` are included in the source tree. It is a prototype rather than a hardened production chat system.
- Share-history recording. **Email sending is not implemented yet**: `EmailUtil.send()` prints a message in the console.

## Tech stack

| Component | Technology |
| --- | --- |
| Server-side | Java 11, Jakarta Servlet 6, JSP/JSTL |
| Persistence | Hibernate 6.4 / JPA |
| Database | Microsoft SQL Server |
| Build & packaging | Maven, WAR |
| Web server | Apache Tomcat 10.1 |
| Frontend | Bootstrap, JavaScript |
| Real-time prototype | Jakarta WebSocket, Jackson |

This application is **not Spring Boot**.

## Source layout

```text
src/main/
├── java/com/example/java4main/
│   ├── controller/     # Servlet controllers and administration
│   ├── dao/            # Data access
│   ├── dto/            # Transfer objects
│   ├── entity/         # User, Video, Favorite, Share
│   ├── filter/         # Login and role checks
│   ├── service/        # Business operations
│   ├── utils/          # JPA, session, cookie, email placeholder
│   └── websocket/      # Community-chat prototype
├── resources/META-INF/persistence.xml
└── webapp/             # JSP views, JavaScript, WEB-INF
```

## Running locally without IntelliJ

Requirements: **JDK 11+**, **Tomcat 10.1**, **SQL Server** with database `PolyOE`, and the four tables mapped by the entities.

1. Prepare a SQL Server or Azure SQL database and run [`database/schema.sql`](database/schema.sql). This creates the four mapped tables (without seed accounts). Hibernate uses `hbm2ddl.auto=validate`, so the schema must exist first.
2. Set environment variables (PowerShell example):

```powershell
$env:DB_URL = 'jdbc:sqlserver://localhost:1433;databaseName=PolyOE;encrypt=true;trustServerCertificate=true'
$env:DB_USER = 'your-db-user'
$env:DB_PASSWORD = 'your-db-password'
```

3. Package the application:

```powershell
.\mvnw.cmd clean package
```

4. Copy `target/Java4Main-1.0-SNAPSHOT.war` into Tomcat's `webapps/` directory. Start Tomcat and visit `http://localhost:8080/Java4Main-1.0-SNAPSHOT/home`.

## Hosting and security notes

- The app needs a database reachable by the remote Tomcat instance; `localhost` on a cloud host will **not** refer to your laptop.
- Credentials are read from `DB_URL`, `DB_USER`, and `DB_PASSWORD` in the current source. **Rotate any database password previously committed to Git**, as Git history retains old credentials.
- New passwords are salted and hashed using PBKDF2-HMAC-SHA256. Legacy plaintext passwords are upgraded after a successful login. Run [`database/migrate_password_column.sql`](database/migrate_password_column.sql) before updating an existing database. Additional security reviews (CSRF, WebSocket auth, rate limiting) are still needed before public signup.
- `ShareController` still references a localhost URL; replace this with the deployment base URL.
- The repository includes [`database/schema.sql`](database/schema.sql), [`Dockerfile`](Dockerfile) and [`render.yaml`](render.yaml). There is still **no verified live deployment URL**. Read [`DEPLOYMENT.md`](DEPLOYMENT.md) before deploying.
- The WebSocket chat is a prototype, with production authentication, rate limiting and moderation still needed.
- The project currently links to YouTube media/thumbnails and does **not** contain end-user video channels or file-upload/transcoding functionality.

## Portfolio

Repository author: [@zCelestiaI](https://github.com/zCelestiaI). This project was developed for Java Web coursework and demonstrates Servlet routes, role checks, Hibernate persistence, DAO/service structure, and administrative reports.
