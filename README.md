# Online Entertainment

**Java Web video discovery and creator-channel platform**

A Java Servlet/JSP project developed from Java Web coursework into a personal portfolio application. Users can create channels, publish YouTube-linked video posts, discover and watch embedded videos, and interact through a simple community chat.

**[Live Demo](https://online-entertainment-web-production.up.railway.app/)** · **[Source Code](https://github.com/zCelestiaI/OnlineEntertainmentProj)** · **[Deployment Guide](DEPLOYMENT.md)**

> **Project scope:** Videos are published by pasting a YouTube URL. This app stores video metadata, channel information, and interactions; YouTube serves the actual video stream. There is **no native video-file upload or video hosting** on this server.

## What the application does

### Accounts and personal channels
- Register, sign in with username or email, sign out, edit a profile, and change a password.
- Each account has a personal channel with an editable name and description.
- New passwords are stored using salted PBKDF2-HMAC-SHA256 hashes; legacy coursework accounts can be upgraded after successful sign-in.

### Video publishing and discovery
- **Creator Studio:** create, edit, hide/show, and delete video posts belonging to the signed-in account.
- Accept supported HTTPS YouTube Watch, Shorts, Live, Embed, and `youtu.be` links.
- Extract the video ID and automatically generate a YouTube thumbnail; an HTTPS thumbnail URL may also be provided.
- Browse public video posts, search by title, visit creator channels, and navigate paginated listings.
- Watch videos with an embedded YouTube player; see view counts and related videos.
- Like/unlike videos and save sharing activity. Sharing a link works; **automatic email delivery is not implemented**.

### Administration and community
- Admin-only pages for user/video management and favorite/share reports.
- A lightweight **real-time community chat** built with Jakarta WebSocket and JSON messages. It is a prototype, not a production chat service.

## Tech stack

| Area | Technologies |
| --- | --- |
| Backend | Java (target 11), Jakarta Servlet 6.0, JSP, JSTL |
| Data access | Hibernate ORM 6.4, JPA |
| Frontend | HTML, CSS, JavaScript, Bootstrap 5 |
| Databases | PostgreSQL 16 (Neon, deployed demo); SQL Server (original local coursework database) |
| Web server | Apache Tomcat 10.1 |
| Delivery | Maven, Docker, GitHub Actions, Railway |
| Chat | Jakarta WebSocket, Jackson JSON |

The Docker image builds with **JDK 17** and runs on **Tomcat 10.1 / Java 17**. The Maven project currently targets Java 11 bytecode.

## Live deployment

| Component | Current setup |
| --- | --- |
| Website | [online-entertainment-web-production.up.railway.app](https://online-entertainment-web-production.up.railway.app/) |
| Web hosting | Railway, Docker image with Tomcat 10.1 and `ROOT.war` |
| Database | Neon PostgreSQL (`polyoe`) |
| Video player and thumbnails | YouTube embed and thumbnail URLs |
| Health endpoint | [/health](https://online-entertainment-web-production.up.railway.app/health) |

The demo uses hosting free/trial resources and **does not promise uninterrupted 24/7 availability**. Some YouTube videos cannot be embedded because of video-specific restrictions.

## Run locally

**Requirements:** JDK 17, Maven (or the included Maven Wrapper), and Apache Tomcat 10.1. You also need PostgreSQL or SQL Server.

### 1. Clone the repository

~~~bash
git clone https://github.com/zCelestiaI/OnlineEntertainmentProj.git
cd OnlineEntertainmentProj
~~~

### 2. Prepare an empty database

Choose **one** of these setups:

**PostgreSQL:** Create an empty database and run [`database/postgres_schema.sql`](database/postgres_schema.sql). This script is intended for **new, empty databases only**.

**SQL Server:** Create an empty `PolyOE` database and run [`database/schema.sql`](database/schema.sql). If upgrading an *existing* coursework database, back it up first and review [`database/migrate_youtube_mvp.sql`](database/migrate_youtube_mvp.sql) instead.

### 3. Configure environment variables

Do not commit passwords, connection strings containing credentials, or local environment files.

PostgreSQL example (PowerShell):

~~~powershell
$env:DB_URL = 'jdbc:postgresql://localhost:5432/polyoe'
$env:DB_USER = 'your-db-user'
$env:DB_PASSWORD = 'your-db-password'
$env:DB_SCHEMA_MODE = 'validate'
~~~

SQL Server alternative (PowerShell):

~~~powershell
$env:DB_URL = 'jdbc:sqlserver://localhost:1433;databaseName=PolyOE;encrypt=true;trustServerCertificate=true'
$env:DB_USER = 'your-db-user'
$env:DB_PASSWORD = 'your-db-password'
$env:DB_SCHEMA_MODE = 'validate'
~~~

`DB_SCHEMA_MODE=validate` checks the existing schema without creating tables.

### 4. Build and start

On Windows:

~~~powershell
.\mvnw.cmd clean verify
~~~

On macOS/Linux:

~~~bash
./mvnw clean verify
~~~

Deploy `target/Java4Main-1.0-SNAPSHOT.war` to Tomcat 10.1's `webapps` folder. Rename it to `ROOT.war` to use the same URLs as the live site, then start Tomcat and open:

- `http://localhost:8080/home` — browse videos
- `http://localhost:8080/register` — create an account
- `http://localhost:8080/studio` — manage your channel after login
- `http://localhost:8080/health` — check database connectivity

If you keep the original WAR filename, the context path will instead start with `/Java4Main-1.0-SNAPSHOT`.

## Code structure

~~~text
src/main/java/com/example/java4main/
  controller/    HTTP endpoints for users, videos, channels and admin
  service/       Application/business logic
  dao/           JPA data access interfaces and implementations
  entity/        User, Video, Favorite and Share entities
  filter/        Session-based access control and encoding
  utils/         Passwords, YouTube URLs and persistence configuration
  websocket/     Community chat endpoint and JSON messages
src/main/webapp/views/
  user/          Public and signed-in pages
  admin/         Administration pages
database/        SQL Server and PostgreSQL schema/migrations
src/test/        JUnit tests
~~~

## What is intentionally out of scope

This is a **student portfolio MVP**, not a replacement for YouTube. It does not provide native video uploads/transcoding, subscriptions, comments, personalized recommendation algorithms, email-based password recovery, or automated email sharing. The current chat is a small prototype and is not intended as a hardened public messaging product.

If a YouTube video is private, removed, or has embedding disabled, playback may be unavailable. A channel posting an existing YouTube URL does **not** claim ownership of that video's content.

## Development and deployment notes

- [**Deployment guide**](DEPLOYMENT.md): current Railway + Neon setup, plus operational notes.
- [**Serv00 deployment notes**](SERV00_DEPLOY.md): an alternative, **not the live deployment**.
- [**GitHub Actions**](.github/workflows/build.yml): Maven verification, a build artifact, and a Docker image build check.
- [**PostgreSQL schema**](database/postgres_schema.sql) and [**SQL Server schema**](database/schema.sql).

The project is a learning exercise and has known areas for improvement, particularly authorization hardening, CSRF protection, WebSocket identity verification, and broader automated tests. No real database credentials are stored in the current tracked configuration; any credentials exposed in older Git history should be rotated.

---

Built as part of my Java Web learning journey and expanded for a software development internship portfolio.
