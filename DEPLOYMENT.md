# Deployment guide (preparation, not a live deployment)

The repository now includes a multi-stage `Dockerfile`, `render.yaml` (Render Free), and a GitHub Actions build workflow.

**Important:** The project is a student portfolio application and is **not yet safe for unrestricted public registration**. Before inviting users to register, implement secure password hashing, review authorization and input validation, protect the chat endpoint, and configure rate limits.

## Suggested demo architecture

- **Java Web:** Render Free + Docker + Tomcat 10.1, deployed as `ROOT.war`.
- **Database:** Azure SQL Database Free (Microsoft SQL Server compatible); configure firewall so your Render web service can connect.
- **Media:** The current application stores video metadata and YouTube poster URLs. It does **not** yet implement user-generated channels, direct video uploads, video transcoding or media storage.

## Prerequisites

1. An Azure subscription eligible for Azure SQL Database Free, with a database and SQL-login credentials.
2. SQL schema and demo data for the `Users`, `Video`, `Favorite`, and `Share` entities. **No schema/seed script is currently committed**. Hibernate runs with `hbm2ddl.auto=validate`.
3. A Render account connected to this GitHub repository.
4. **Rotated database credentials**. Earlier Git history contained a hardcoded password. Removing it from the latest commit does not invalidate that credential.

## Deploy a demo after preparing the database

1. Set up Azure SQL Database and make sure the database's network rules allow the app to connect.
2. In Render, choose **New → Blueprint**, connect this repository and select the `render.yaml` file.
3. Choose the free service and set the secret environment values:
   - `DB_URL`: `jdbc:sqlserver://YOUR-SERVER.database.windows.net:1433;databaseName=YOUR-DB;encrypt=true;trustServerCertificate=false`
   - `DB_USER`: the SQL username for Azure SQL
   - `DB_PASSWORD`: the rotated SQL password
4. Render builds a Docker image from the `Dockerfile` and serves Tomcat on port `8080`.
5. On successful startup and database connection, open `https://YOUR-RENDER-DOMAIN.onrender.com/home`.
6. Test login, logout, search, favorites, admin pages, reports, and WebSocket chat. Test again after waking a sleeping free instance.

**The URL above is an example, not an actual deployed URL.** This repository's presence on GitHub does not mean a public website already exists.

## Before going public

- Hash passwords with a modern adaptive KDF (BCrypt/Argon2/PBKDF2); plan a reset/migration for existing plaintext account records.
- Remove development credentials from Git history where practical, and **rotate all credentials regardless**.
- Implement server-side validation, CSRF protection where applicable, secure session-cookie attributes, and authorization checks on all mutations.
- Replace hardcoded `localhost:8080` sharing links with the actual public URL.
- Make email sending real or clearly disable the feature.
- Review WebSocket authentication and connection/message limits.
- Provide a reproducible SQL initialization script with synthetic data.
- Add channels, an uploader role model, object storage, media processing, moderation and abuse prevention before claiming YouTube-like user uploads.

For an internship CV, it is accurate to link the GitHub repository as a **Java Servlet/JSP application with an admin panel and chat prototype**, even before public hosting.
