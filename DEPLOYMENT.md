# Online Entertainment — Deployment Guide

This guide documents the **current public demo**. The application is a Java Servlet/JSP WAR running on Apache Tomcat 10.1. It saves channel/video metadata and user interactions; YouTube provides video playback and thumbnail images.

**Live website:** https://online-entertainment-web-production.up.railway.app/

## Current deployment

| Component | Provider / configuration |
| --- | --- |
| Web service | Railway (GitHub-connected deployment, Docker) |
| Java runtime | Java 17 / Apache Tomcat 10.1 |
| Application artifact | Maven WAR, deployed as `ROOT.war` |
| Database | Neon PostgreSQL 16, database `polyoe` |
| Schema strategy | Existing database tables, `DB_SCHEMA_MODE=validate` |
| Health check | `GET /health` |
| Videos | YouTube embeds and thumbnails (no video file storage) |

Hosting free/trial allowances can change or expire; this is **not** an uptime guarantee.

## 1. Database preparation

For a **new, empty PostgreSQL database**, execute [`database/postgres_schema.sql`](database/postgres_schema.sql) to create the tables. This script is not a migration and must not be rerun blindly on existing data.

For the currently configured Neon database, the schema already exists. Keep `DB_SCHEMA_MODE=validate` when deploying.

The project also supports SQL Server for local coursework. Use [`database/schema.sql`](database/schema.sql) only for a new empty SQL Server database. For upgrading an existing `PolyOE` database, review [`database/migrate_youtube_mvp.sql`](database/migrate_youtube_mvp.sql), and back up first.

## 2. Railway service

1. Connect the repository's `master` branch to a Railway web service.
2. Build from the included [`Dockerfile`](Dockerfile). It compiles the WAR in a Maven stage and serves it from Tomcat as `ROOT.war`.
3. Configure **private** service environment variables in Railway (not in Git):
   - `DB_URL`: `jdbc:postgresql://YOUR-NEON-HOST:5432/polyoe?sslmode=require`
   - `DB_USER`: PostgreSQL database username
   - `DB_PASSWORD`: PostgreSQL database password
   - `DB_SCHEMA_MODE`: `validate`
4. Use port `8080`, the Tomcat port exposed by the Docker image, and set the health check path to `/health`.
5. Deploy and check the web-service logs if startup fails.

Never put actual database credentials in a Markdown document, commit, or public screenshot. If an old password has appeared in Git history, rotate it.

## 3. Verify the deployment

- `/health`: HTTP 200 and `{"status":"ok","database":"connected"}` when database access succeeds.
- `/home`: public videos are listed.
- `/register` and `/login`: account creation and login.
- `/studio`: after login, update the channel and publish a YouTube URL.
- `/channel?id=USERNAME`: public channel and visible video posts.
- `/video?id=VIDEO_ID`: embedded playback if the source video permits embedding.
- `/favorite`: signed-in account's liked videos.
- `/chat`: lightweight WebSocket community chat; verify from two authenticated browser sessions.

A successful `/health` response confirms a database query, **not** every application feature.

## 4. CI and build behavior

[GitHub Actions](.github/workflows/build.yml) runs `mvn verify`, uploads the built WAR, and checks that the Docker image can build. The Dockerfile currently uses `-DskipTests` during its image build; tests are run by the separate CI job.

The Maven project compiles against Java 11 source/target settings while the Docker builder and Tomcat runtime use JDK 17.

## 5. Troubleshooting

| Symptom | Things to check |
| --- | --- |
| HTTP 503 from `/health` | Neon connection values, database availability and deployed schema |
| WAR fails at startup | Tomcat 10.1 / Servlet 6 compatibility, JDBC environment variables and Hibernate validation |
| YouTube embed unavailable | Source video visibility, deleted video or YouTube's embed restrictions |
| WebSocket cannot connect | HTTPS/WSS URL, browser console and Railway/Tomcat application logs |
| Page is empty or server errors | Service logs, schema compatibility and whether the database has any video records |

## 6. Alternative hosting notes

The repository contains [`render.yaml`](render.yaml) from an earlier **Render** exploration and [`SERV00_DEPLOY.md`](SERV00_DEPLOY.md) for a possible **Serv00** deployment. Neither describes the website currently published on Railway. They are optional reference material, not evidence that those targets are running.

## Project limits

Online Entertainment is a student portfolio MVP. It does not upload or host video files, run transcoding, send automated sharing emails, or implement email password recovery. The chat works as a lightweight real-time prototype but is not presented as a security-hardened messaging platform. Authorization, CSRF safeguards, WebSocket identity checks and broader test coverage remain areas for future improvement.
