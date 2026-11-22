# Deploy Online Entertainment (YouTube-link MVP)

The application uses **YouTube links for all video posts**. It does **not** upload, store, or transcode video files. You need only the Java web host and SQL Server-compatible database: **no Cloudinary, object storage or YouTube API key is required** for video embeds and thumbnails.

## Components

- **App:** Render Free Docker service, Tomcat 10.1 serving `ROOT.war`.
- **Database:** Azure SQL Database Free (if the account is eligible), or another accessible SQL Server instance. Database usage and free quotas follow the provider's current terms.
- **Media:** `youtube.com` embedded iframe playback and `img.youtube.com` thumbnails.

## Database setup

1. Create a new empty SQL Server/Azure SQL database.
2. Execute [`database/schema.sql`](database/schema.sql) in the database. The script matches the application's JPA mappings, including channel name and video owner.
3. **For an existing Java 4 `PolyOE` database instead, back it up and execute [`database/migrate_youtube_mvp.sql`](database/migrate_youtube_mvp.sql)**. Do not drop the original tables.
4. The app uses Hibernate `hbm2ddl.auto=validate` and does not create tables itself.
5. Do **not** publish legacy course-demo accounts using `admin/123` or `user/123`; reset these first.

## Render

1. Connect the repository to Render, choose **New → Blueprint**, and select `render.yaml`.
2. Set secret environment variables:
   - `DB_URL`: `jdbc:sqlserver://YOUR-SERVER.database.windows.net:1433;databaseName=YOUR-DB;encrypt=true;trustServerCertificate=false`
   - `DB_USER`: database user
   - `DB_PASSWORD`: your rotated database password
3. Allow database network access from the web service according to the database host's firewall controls. Restrict access where possible.
4. Deploy. The `Dockerfile` builds a Maven WAR and starts Tomcat 10.1, listening on port 8080.
5. Verify `GET /health` returns HTTP 200 with `{"status":"ok","database":"connected"}`. HTTP 503 indicates the database is unreachable.
6. When running, open `https://YOUR-RENDER-DOMAIN.onrender.com/home`.

The URL is illustrative. Deployment has not been performed or verified from this repository yet.

## Test checklist

- Register, log in with username or email, log out, and use remember-username.
- Create/edit a personal channel at `/studio` and visit `/channel?id=USERNAME`.
- Paste a YouTube Watch / Shorts / Live / short URL to create a video.
- Confirm automatic thumbnail generation, title/description, embedded playback, search and visibility.
- Edit or delete **your own** video; confirm another user cannot do either.
- Verify existing admin-created videos still show up after SQL migration.
- Test favorites, recorded shares, reports, and WebSocket chat.

## Known limitations and precautions

- YouTube can restrict embedding for certain videos, and removed/private videos cannot play.
- The app stores your own metadata and URL, **not** the original video bytes.
- A working video link proves successful embedding, not video ownership.
- An email-sending helper is currently a console placeholder; a sharing URL may still be localhost in legacy code.
- Git history previously contained database credentials; **rotate them** even though current configuration uses environment variables.
- Password hashing has been implemented, but unrestricted public registration still requires additional security review, input validation, CSRF mitigation, WebSocket authentication, spam controls and monitoring.

For the internship CV, describe this truthfully as a **Java Servlet/JSP video-sharing and channel-management platform with YouTube embedding** rather than a native video-hosting service.
