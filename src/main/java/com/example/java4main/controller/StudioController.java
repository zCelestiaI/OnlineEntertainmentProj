package com.example.java4main.controller;

import com.example.java4main.dao.impl.VideoDAOImpl;
import com.example.java4main.entity.User;
import com.example.java4main.entity.Video;
import com.example.java4main.service.CloudinaryUploadService;
import com.example.java4main.service.UserService;
import com.example.java4main.service.VideoService;
import com.example.java4main.utils.SessionUtil;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.MultipartConfig;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import java.io.IOException;
import java.net.URI;
import java.util.List;
import java.util.UUID;
import java.util.regex.Pattern;

@WebServlet("/studio")
@MultipartConfig(fileSizeThreshold = 1024 * 1024,
        maxFileSize = 15L * 1024 * 1024, maxRequestSize = 17L * 1024 * 1024)
public class StudioController extends HttpServlet {
    private static final Pattern YT_ID = Pattern.compile("^[A-Za-z0-9_-]{11}$");
    private final UserService users = new UserService();
    private final VideoService videoService = new VideoService();
    private final VideoDAOImpl videos = new VideoDAOImpl();
    private final CloudinaryUploadService cloudinary = new CloudinaryUploadService();

    private User current(HttpServletRequest request, HttpServletResponse response) throws IOException {
        User sessionUser = (User) SessionUtil.get(request, "currentUser");
        if (sessionUser == null) {
            response.sendRedirect(request.getContextPath() + "/login");
            return null;
        }
        User user = users.findById(sessionUser.getId());
        if (user == null) {
            SessionUtil.invalidate(request);
            response.sendRedirect(request.getContextPath() + "/login");
        }
        return user;
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        User user = current(request, response);
        if (user == null) return;
        request.setAttribute("owner", user);
        request.setAttribute("videos", videos.findByUploader(user.getId(), true));
        request.setAttribute("cloudinaryConfigured", CloudinaryUploadService.configured());
        String editId = request.getParameter("edit");
        if (editId != null && !editId.isBlank()) {
            Video editing = videoService.findById(editId);
            if (editing != null && editing.getUploader() != null
                    && user.getId().equals(editing.getUploader().getId())) {
                request.setAttribute("editing", editing);
            }
        }
        request.getRequestDispatcher("/views/user/studio.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        User user = current(request, response);
        if (user == null) return;
        String action = request.getParameter("action");
        try {
            if ("channel".equals(action)) {
                String name = trimmed(request.getParameter("channelName"));
                String description = trimmed(request.getParameter("channelDescription"));
                if (name.isBlank() || name.length() > 100 || description.length() > 500) {
                    throw new IllegalArgumentException("Ten kenh phai co 1-100 ky tu va mo ta toi da 500 ky tu.");
                }
                user.setChannelName(name);
                user.setChannelDescription(description);
                SessionUtil.set(request, "currentUser", users.update(user));
            } else if ("delete".equals(action)) {
                String id = trimmed(request.getParameter("id"));
                Video existing = videoService.findById(id);
                requireOwner(existing, user);
                videoService.delete(id);
            } else if ("save".equals(action)) {
                saveVideo(request, user);
            } else {
                throw new IllegalArgumentException("Thao tac khong hop le.");
            }
            response.sendRedirect(request.getContextPath() + "/studio?success=1");
        } catch (IllegalArgumentException | IllegalStateException e) {
            request.setAttribute("error", e.getMessage());
            doGet(request, response);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            request.setAttribute("error", "Tai video bi gian doan. Hay thu lai.");
            doGet(request, response);
        } catch (IOException e) {
            request.setAttribute("error", "Khong the tai video len cloud. Hay thu lai hoac dan link YouTube.");
            doGet(request, response);
        }
    }

    private void saveVideo(HttpServletRequest request, User owner)
            throws IOException, ServletException, InterruptedException {
        String id = trimmed(request.getParameter("id"));
        String title = trimmed(request.getParameter("title"));
        String description = trimmed(request.getParameter("description"));
        String youtubeUrl = trimmed(request.getParameter("youtubeUrl"));
        String poster = trimmed(request.getParameter("poster"));
        if (title.isBlank() || title.length() > 255 || description.length() > 20000) {
            throw new IllegalArgumentException("Tieu de phai co 1-255 ky tu; mo ta toi da 20000 ky tu.");
        }
        if (poster.length() > 255 || (!poster.isBlank() && !poster.startsWith("https://"))) {
            throw new IllegalArgumentException("Anh bia phai la URL HTTPS (toi da 255 ky tu).");
        }
        boolean creating = id.isBlank();
        Video video = creating ? new Video() : videoService.findById(id);
        if (!creating) requireOwner(video, owner);
        if (creating) {
            video.setId(UUID.randomUUID().toString().replace("-", "").substring(0, 20));
            video.setUploader(owner);
            video.setViews(0);
        }
        video.setTitle(title);
        video.setDescription(description);
        video.setActive(request.getParameter("active") != null);

        Part uploaded = request.getContentType() != null && request.getContentType().startsWith("multipart/")
                ? request.getPart("videoFile") : null;
        if (uploaded != null && uploaded.getSize() > 0) {
            CloudinaryUploadService.UploadedMedia media = cloudinary.upload(uploaded);
            video.setSourceUrl(media.url());
            video.setSourceType("CLOUDINARY");
            video.setPoster(poster.isBlank() ? media.thumbnailUrl() : poster);
        } else if (!youtubeUrl.isBlank()) {
            String youtubeId = youtubeId(youtubeUrl);
            if (youtubeId == null) throw new IllegalArgumentException("Hay nhap mot link YouTube hop le.");
            video.setSourceType("YOUTUBE");
            video.setSourceUrl("https://www.youtube.com/watch?v=" + youtubeId);
            video.setPoster(poster.isBlank()
                    ? "https://img.youtube.com/vi/" + youtubeId + "/hqdefault.jpg" : poster);
        } else if (!poster.isBlank()) {
            video.setPoster(poster);
        }
        if (creating && (video.getSourceUrl() == null || video.getSourceUrl().isBlank())) {
            throw new IllegalArgumentException("Hay tai file video hoac nhap link YouTube.");
        }
        if (creating) videoService.create(video);
        else videoService.update(video);
    }

    private static void requireOwner(Video video, User owner) {
        if (video == null || video.getUploader() == null
                || !owner.getId().equals(video.getUploader().getId())) {
            throw new IllegalArgumentException("Ban khong so huu video nay.");
        }
    }

    private static String trimmed(String s) { return s == null ? "" : s.trim(); }

    private static String youtubeId(String url) {
        try {
            URI uri = URI.create(url);
            if (!"https".equalsIgnoreCase(uri.getScheme())) return null;
            String host = uri.getHost();
            if (host == null) return null;
            host = host.toLowerCase();
            String path = uri.getPath();
            String id = null;
            if (host.equals("youtu.be") || host.equals("www.youtu.be")) {
                id = path.replaceFirst("^/", "").split("/")[0];
            } else if (host.equals("youtube.com") || host.equals("www.youtube.com")
                    || host.equals("m.youtube.com") || host.equals("www.youtube-nocookie.com")) {
                if (path.startsWith("/shorts/")) id = path.substring(8).split("/")[0];
                else if (path.startsWith("/embed/")) id = path.substring(7).split("/")[0];
                else if (path.equals("/watch") && uri.getRawQuery() != null) {
                    for (String part : uri.getRawQuery().split("&")) {
                        if (part.startsWith("v=")) { id = part.substring(2); break; }
                    }
                }
            }
            return id != null && YT_ID.matcher(id).matches() ? id : null;
        } catch (IllegalArgumentException e) { return null; }
    }
}
