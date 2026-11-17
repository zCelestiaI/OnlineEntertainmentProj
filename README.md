# Online Entertainment

A Java web application developed as an academic project. It provides a video-browsing experience for users and an administration area for managing videos, users, and engagement reports.

> **Status:** Source-code portfolio project. The `master` branch has **not** been verified as a production-ready, publicly hosted application. A SQL Server database and additional configuration are required to run it.

## Features

### User-facing features
- Browse active videos with search and pagination (six items per page in the main listing).
- Register, log in, log out, and manage user profile/password.
- View video details and track views.
- Like or unlike videos and view a personal favorites list.
- Submit video-sharing information, which is recorded for reporting.

### Administration
- Role-based access to `/admin/*` routes through an authentication/authorization filter.
- Manage video metadata, YouTube thumbnail URLs, visibility, and view counts.
- Manage user accounts and distinguish administrator/user roles.
- View reports on video favorites, users who liked videos, and video-sharing history.

## Technology stack

| Layer | Technology |
| --- | --- |
| Language | Java 11 (Maven compiler target) |
| Web backend | Jakarta Servlet 6.0, JSP, JSTL |
| Persistence | Hibernate ORM 6.4, Jakarta Persistence (JPA) |
| Database | Microsoft SQL Server, Microsoft JDBC Driver |
| Build | Maven Wrapper, Maven WAR Plugin |
| Deployment target | Apache Tomcat **10.1** (compatible with Servlet 6.0) |
| UI | JSP and Bootstrap (CDN referenced in views) |

This is a **traditional Java Servlet/JSP web application**, not a Spring Boot application.

## Repository structure

```text
src/main/
├── java/com/example/java4main/
│   ├── controller/       # Routes for users and administrators
│   ├── dao/              # Data access interfaces and implementations
│   ├── dto/              # Data transfer objects
│   ├── entity/           # JPA entity mappings
│   ├── filter/           # Authentication, admin access, encoding
│   ├── listener/         # Application lifecycle
│   ├── service/          # Business logic
│   └── utils/            # JPA, session, cookies, email stub
├── resources/META-INF/
│   └── persistence.xml   # Database/JPA configuration
└── webapp/
    ├── index.jsp
    ├── WEB-INF/web.xml
    └── views/
        ├── admin/
        ├── layout/
        └── user/
```

## Run locally without IntelliJ

### Prerequisites

- JDK **11 or later**
- Microsoft SQL Server
- Apache Tomcat **10.1**
- A SQL Server database named `PolyOE` with tables corresponding to the JPA entities (`Users`, `Video`, `Favorite`, `Share`)
- Maven is optional because the repository includes Maven Wrapper

> **Database setup:** The project currently uses `hibernate.hbm2ddl.auto=validate`, so it **will not generate missing tables**. This repository does not currently include a database schema/seed SQL script. Prepare the schema before starting the application.

### 1. Clone and build

```bash
git clone https://github.com/zCelestiaI/OnlineEntertainmentProj.git
cd OnlineEntertainmentProj
```

**Windows:**

```powershell
.\mvnw.cmd clean package
```

**macOS/Linux:**

```bash
./mvnw clean package
```

The compiled application should be available at:

```text
target/Java4Main-1.0-SNAPSHOT.war
```

### 2. Configure SQL Server locally

Check `src/main/resources/META-INF/persistence.xml`. The current version points to a SQL Server instance at `localhost:1433` and uses hardcoded database credentials.

**Do not reuse or publish the credentials currently committed in this repository.** Rotate the database password and move credentials to environment-based configuration before deploying the application publicly. Simply adding a file to `.gitignore` does not remove previously committed secrets from Git history.

The database must be reachable from the machine running Tomcat. A remote server's `localhost` is **not** your laptop.

### 3. Deploy the WAR to Tomcat 10.1

Copy the generated WAR to Tomcat's `webapps` directory, optionally renaming it to `online-entertainment.war`.

Start Tomcat using `bin/startup.bat` (Windows) or `bin/startup.sh` (Linux/macOS), then open:

```text
http://localhost:8080/online-entertainment/home
```

If you keep the original WAR filename, the context path will normally be `/Java4Main-1.0-SNAPSHOT` instead.

## Public deployment: work still required

Running locally without IntelliJ is different from making the app available at a public URL.

A typical deployment path is:

1. Fix credential handling, password storage, and other security issues before exposing the application on the internet.
2. Provision a **remote SQL Server-compatible database** and create the required tables/data.
3. Change JPA configuration to read DB URL, username, and password from environment variables.
4. Package the WAR into a Docker image with **Tomcat 10.1**.
5. Host the Docker web service on a provider such as Render, with a separate database service.
6. Update absolute local links in the app (for example, `localhost:8080` in sharing) to use the actual request/public URL.
7. Test routes, JSP assets, authentication, database access, and all interactive features after deployment.

**The repository does not yet include a ready-to-deploy Dockerfile or a live demo URL.** Do not assume that uploading this repository alone will produce a working website.

## Known limitations of this branch

- **Credentials:** `persistence.xml` currently contains hardcoded database connection details. Replace and rotate them before hosting.
- **Password security:** User passwords are compared directly in `UserService`; secure password hashing should be implemented before accepting real users.
- **Email:** `EmailUtil.send()` is currently a console-output placeholder, not a real email-delivery integration.
- **Sharing URL:** `ShareController` currently generates a `localhost:8080` link.
- **Database initialization:** No SQL schema/seed script is tracked, while Hibernate schema mode is `validate`.
- **Web assets:** A stylesheet under `/assets/css/style.css` is referenced in JSP, but the current tracked webapp tree does not contain that asset.
- **Community chat:** A WebSocket chat interface appears in some separate project screenshots, but a chat endpoint/implementation is not present in the currently tracked `master` source tree.

These notes distinguish existing code from features that need to be integrated or completed.

## Portfolio use

This repository demonstrates work with Java Servlet controllers, session-based authorization, DAO/service layers, JPA entity relationships, SQL Server, JSP views, and administrative reporting.

For an internship portfolio, consider adding:
- Screenshots or a short demo video of the running app.
- An anonymized database initialization script.
- A working public demo after completing deployment and security improvements.
- A summary of **your own contributions** if this was a group project.

## Author

GitHub: [@zCelestiaI](https://github.com/zCelestiaI)

Academic Java Web project for learning and portfolio purposes.
