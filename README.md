# Online Entertainment — Java Web Video Platform

A portfolio-oriented video discovery and creator-channel MVP built with Java Servlet/JSP, Hibernate/JPA, SQL Server, and Tomcat.

## How videos work

**YouTube URLs are the single publishing source.** Users do **not** upload video files to this application's server.

1. Register a free account, which also creates a personal channel.
2. Visit **Creator Studio** at `/studio`.
3. Paste a valid `https://youtube.com/watch?v=...`, `https://youtu.be/...`, Shorts, Live, or Embed link.
4. Enter a title and description. The app extracts the YouTube video ID, saves a canonical YouTube URL, and **automatically generates a thumbnail URL** using `img.youtube.com/vi/{id}/hqdefault.jpg`. An HTTPS custom thumbnail can be supplied.
5. A public channel page lists the user's videos. Visitors watch them in an embedded YouTube iframe, directly on Online Entertainment.

The website stores **only video metadata and YouTube references** in SQL Server. Video hosting, streaming, and transcoding remain on YouTube; the app neither copies nor rehosts the media. This MVP does **not** use Cloudinary or any object-storage account.

**Important:** Embedding is subject to each video's visibility, YouTube embedding restrictions, copyright, and YouTube availability. Sharing an existing YouTube URL in a personal channel is not a YouTube upload or proof of ownership of that video.

## Features

- Registration, login by username or email, logout, optional remember-username cookie, password changes.
- Password hashing via salted PBKDF2-HMAC-SHA256, with migration for legacy plaintext student demo accounts.
- Create/edit a channel display name and description.
- Creator Studio: create/edit/delete **your own** YouTube-linked videos and toggle visibility.
- Public channels, home video discovery, search and pagination.
- Embedded YouTube playback, view counts, recommended videos, favorites/likes, recorded shares.
- Administrator dashboard, user and video management, favorites/share reports.
- WebSocket chat prototype using a Jackson-based `MessageEncoder` and `MessageDecoder`.

Not yet implemented: native video upload, subscriptions, comments, transcoding, recommendation algorithms, real email sending, and production-grade content moderation. This is a student demo, not a fully operational YouTube replacement.

## Tech stack

| Layer | Technology |
| --- | --- |
| Backend | Java 11, Jakarta Servlet 6.0, JSP/JSTL |
| Persistence | Hibernate ORM 6.4, Jakarta Persistence (JPA) |
| Database | Microsoft SQL Server / Azure SQL; PostgreSQL for optional free demo |
| Frontend | Bootstrap 5, HTML/CSS, JavaScript |
| Chat prototype | Jakarta WebSocket, Jackson JSON encoder/decoder |
| Build/hosting | Maven, WAR, Apache Tomcat 10.1, Docker/Render |

## Run locally

Prerequisites: JDK 11+, Maven or Maven Wrapper, Tomcat 10.1, SQL Server.

- **New empty database:** Create `PolyOE` and run [`database/schema.sql`](database/schema.sql).
- **Existing database from the Java 4 coursework:** Back it up, then run [`database/migrate_youtube_mvp.sql`](database/migrate_youtube_mvp.sql). Do not recreate the tables or run an empty-database script on live data.
- Configure DB access via environment variables, for example in PowerShell:

```powershell
$env:DB_URL = 'jdbc:sqlserver://localhost:1433;databaseName=PolyOE;encrypt=true;trustServerCertificate=true'
$env:DB_USER = 'your-db-user'
$env:DB_PASSWORD = 'your-db-password'
.\mvnw.cmd clean package
```

- Deploy `target/Java4Main-1.0-SNAPSHOT.war` to Tomcat 10.1's `webapps` directory; open `http://localhost:8080/Java4Main-1.0-SNAPSHOT/home`.

## Deploy a public demo

See [`DEPLOYMENT.md`](DEPLOYMENT.md) for Render + Azure SQL steps. The repo contains a `Dockerfile`, Render blueprint, and GitHub Actions build verification. There is **not yet a verified live hosting URL**. The `/health` endpoint checks whether the SQL Server database is connected.

Before opening access to real users, rotate any previously committed DB passwords, remove the weak coursework demo credentials (`admin/123`, `user/123`), and review authorization, WebSocket access, CSRF defenses, and rate limiting.

## Portfolio

Created for Java Web coursework and expanded as a personal Java Web portfolio project.

GitHub: [@zCelestiaI](https://github.com/zCelestiaI)

## Optional PostgreSQL demo hosting

The application retains SQL Server as its academic-project database. For a temporary free demo, it also accepts a JDBC PostgreSQL URL. Use `DB_SCHEMA_MODE=update` **only for a new, empty demo database** so Hibernate creates its mapped tables; SQL Server defaults to `validate`. Use `DB_URL=jdbc:postgresql://HOST:5432/DATABASE?sslmode=require`, plus `DB_USER` and `DB_PASSWORD`. Never commit real connection credentials. PostgreSQL avoids an Azure subscription, but Render web hosting may still require payment verification. This setting is not an automatic SQL Server data migration.
