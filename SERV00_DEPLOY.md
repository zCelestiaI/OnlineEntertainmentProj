# Deploy Online Entertainment to Serv00 (no credit card)

This deployment target retains the Java Servlet/JSP + WebSocket application and uses the existing **Neon PostgreSQL** database. Video files are still hosted on YouTube; the web application stores only metadata.

**Costs:** Serv00 advertises a no-cost hosting account without a payment card. This is a shared service: 512 MB RAM, finite system-process quotas and no guaranteed SLA. Account applications are manually reviewed (the site says usually a few hours to 72 hours, but longer delays are possible).

**Compatibility:** This application uses **Jakarta Servlet 6.0**, therefore it needs **Tomcat 10.1**. Serv00's general Tomcat documentation also describes Tomcat 10.0, which is NOT sufficient. Verify that `/usr/local/apache-tomcat-10.1` exists on your assigned server, or arrange a private Tomcat 10.1 installation in your home directory. Do not deploy this WAR on Tomcat 9 or 10.0.

## 1. Create your Serv00 account

Sign up at https://www.serv00.com/register-account/ . Activate it after manual approval; you will receive the SSH host and username.

These steps require a **confirmed Serv00 account**. The assistant cannot create an external hosting account on your behalf.

## 2. Download the tested WAR from GitHub

GitHub Actions runs Maven + JUnit and attaches the `online-entertainment-war` build artifact. Open:

https://github.com/zCelestiaI/OnlineEntertainmentProj/actions

Select the latest successful **Java WAR build** workflow, scroll to **Artifacts**, then download `online-entertainment-war`. Extract `Java4Main-1.0-SNAPSHOT.war` from it. Keep the source and credentials separate.

## 3. Prepare Serv00 (SSH / DevilWEB)

Sign in via SSH (`ssh YOUR_LOGIN@YOUR_ASSIGNED_SERV00_HOST`).

Enable execution of custom programs: `devil binexec on`, then disconnect/reconnect SSH.

Reserve TWO available **TCP** ports through the **Port reservation** panel (the examples below assume `HTTP_PORT` and `SHUTDOWN_PORT` are ports you actually reserved):

```sh
devil port add HTTP_PORT tcp
devil port add SHUTDOWN_PORT tcp
```

Check the installed Tomcat runtimes:

```sh
ls -d /usr/local/apache-tomcat-10.1*
java -version
```

Use JDK 11 or later. If Tomcat 10.1 is installed, copy it to the home directory:

```sh
cp -r /usr/local/apache-tomcat-10.1 ~/apache-tomcat
```

If the source directory does not exist, **stop here rather than substituting Tomcat 10.0**. You will need to install Tomcat 10.1 in your home directory from the official Apache distribution. Ask for help with your exact assigned host.

## 4. Configure Tomcat

Edit `~/apache-tomcat/conf/server.xml`. Change the server shutdown port (often 8005) to your reserved `SHUTDOWN_PORT`. Change the HTTP Connector port (often 8080) to your reserved `HTTP_PORT`. Do not set either port to a value that is not reserved/available.

Make sure the application is served as `ROOT.war` so the site paths are `/home`, `/register`, `/studio`, `/health`.

Create `~/apache-tomcat/bin/setenv.sh` via SFTP or a server-side editor, keeping your real password out of GitHub and shell history:

```sh
#!/bin/sh
export DB_URL='jdbc:postgresql://NEON_HOST:5432/polyoe?sslmode=require'
export DB_USER='NEON_DB_USERNAME'
export DB_PASSWORD='NEON_DB_PASSWORD'
export DB_SCHEMA_MODE='validate'
export CATALINA_OPTS='-Xms64m -Xmx256m -XX:MaxMetaspaceSize=128m -Djava.awt.headless=true'
```

Obtain the **real** database hostname, username, and password from your Neon project's **Connection Details**. The Neon database named `polyoe` already contains the four required tables. Use `validate`, **not** `create`, `create-drop` or `update`, to avoid unintended schema modifications.

Protect the environment file:

```sh
chmod 600 ~/apache-tomcat/bin/setenv.sh
```

## 5. Upload the WAR

Use SFTP/WinSCP to transfer the tested WAR to:

```
~/apache-tomcat/webapps/ROOT.war
```

After first deployment Tomcat expands it into `~/apache-tomcat/webapps/ROOT/`.

Start Tomcat:

```sh
~/apache-tomcat/bin/startup.sh
```

Verify logs via:

```sh
tail -n 100 ~/apache-tomcat/logs/catalina.out
```

If the log file has a different name, inspect `~/apache-tomcat/logs/`.

## 6. Create the public site

In DevilWEB → WWW websites, create or edit the `YOUR_LOGIN.serv00.net` site to **Proxy**:

- proxy target server: `localhost`
- proxy target port: your reserved `HTTP_PORT`
- proxy protocol: HTTP between Nginx and Tomcat (HTTPS is handled by the public proxy)
- enable/force HTTPS on the public domain if supported.

The proxy supports WebSocket connections, so the chat endpoint can use WSS through the HTTPS domain.

Do not delete an existing domain without checking whether it hosts something else.

## 7. Auto-start Tomcat after reboot

In DevilWEB → Cron, add a task with trigger **after reboot** (`@reboot`) to start Tomcat:

```sh
/bin/sh /usr/home/YOUR_LOGIN/apache-tomcat/bin/startup.sh
```

Replace `YOUR_LOGIN` with the real Unix account name and check the home directory path with `echo $HOME`.

## 8. Verify before sharing the CV link

Visit your actual public Serv00 domain:

- `https://YOUR_LOGIN.serv00.net/health` → expected HTTP 200 and `{"status":"ok","database":"connected"}`
- `/home` → home page
- `/register` → register a throwaway user (password at least 8 characters)
- `/login` → log in with username and email
- `/studio` → create a channel and submit an HTTPS YouTube URL
- `/channel?id=USERNAME` → published videos
- `/video?id=...` → YouTube embedded player and thumbnail
- `/chat` → verify real WebSocket connection

**Do not claim 24/7 SLA or a successful public deployment until the real site and its runtime have been checked.** Serv00 may suspend applications that exceed its limits.

## Technical references

- Serv00 free service and limits: https://www.serv00.com/
- Serv00 Tomcat instructions: https://docs.serv00.com/Tomcat/
- Serv00 reverse proxy: https://docs.serv00.com/WWW/
- Serv00 port reservations: https://docs.serv00.com/Port_reservation/
