package com.example.java4main.controller;

import com.example.java4main.dao.impl.VideoDAOImpl;
import com.example.java4main.entity.User;
import com.example.java4main.entity.Video;
import com.example.java4main.service.UserService;
import com.example.java4main.service.VideoService;
import com.example.java4main.utils.SessionUtil;
import com.example.java4main.utils.YouTubeUtil;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import java.io.IOException;
import java.util.UUID;

@WebServlet("/studio")
public class StudioController extends HttpServlet {
    private final UserService users = new UserService();
    private final VideoService service = new VideoService();
    private final VideoDAOImpl videos = new VideoDAOImpl();

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
        String editId = request.getParameter("edit");
        if (editId != null && !editId.isBlank()) {
            Video editing = service.findById(editId);
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
        try {
            String action = request.getParameter("action");
            if ("channel".equals(action)) {
                String name = trimmed(request.getParameter("channelName"));
                String description = trimmed(request.getParameter("channelDescription"));
                if (name.isBlank() || name.length() > 100 || description.length() > 500) {
                    throw new IllegalArgumentException("Ten kenh phai co 1-100 ky tu, mo ta toi da 500 ky tu.");
                }
                user.setChannelName(name);
                user.setChannelDescription(description);
                SessionUtil.set(request, "currentUser", users.update(user));
            } else if ("delete".equals(action)) {
                Video existing = service.findById(trimmed(request.getParameter("id")));
                requireOwner(existing, user);
                service.delete(existing.getId());
            } else if ("save".equals(action)) {
                saveVideo(request, user);
            } else {
                throw new IllegalArgumentException("Thao tac khong hop le.");
            }
            response.sendRedirect(request.getContextPath() + "/studio?success=1");
        } catch (IllegalArgumentException e) {
            request.setAttribute("error", e.getMessage());
            doGet(request, response);
        }
    }

    private void saveVideo(HttpServletRequest request, User owner) {
        String id = trimmed(request.getParameter("id"));
        String title = trimmed(request.getParameter("title"));
        String description = trimmed(request.getParameter("description"));
        String youtubeUrl = trimmed(request.getParameter("youtubeUrl"));
        String thumbnailOverride = trimmed(request.getParameter("poster"));

        if (title.isBlank() || title.length() > 255 || description.length() > 20000) {
            throw new IllegalArgumentException("Tieu de phai co 1-255 ky tu; mo ta toi da 20000 ky tu.");
        }
        if (!thumbnailOverride.isBlank() && (thumbnailOverride.length() > 255
                || !thumbnailOverride.startsWith("https://"))) {
            throw new IllegalArgumentException("Thumbnail phai la link HTTPS, toi da 255 ky tu.");
        }

        boolean creating = id.isBlank();
        Video video = creating ? new Video() : service.findById(id);
        if (creating) {
            video.setId(UUID.randomUUID().toString().replace("-", "").substring(0, 20));
            video.setUploader(owner);
            video.setViews(0);
        } else {
            requireOwner(video, owner);
        }
        video.setTitle(title);
        video.setDescription(description);
        video.setActive(request.getParameter("active") != null);

        if (!youtubeUrl.isBlank()) {
            String ytId = YouTubeUtil.extractId(youtubeUrl);
            if (ytId == null) {
                throw new IllegalArgumentException("Link YouTube khong hop le. Dung youtu.be, youtube.com/watch, shorts hoac live.");
            }
            video.setSourceType("YOUTUBE");
            video.setSourceUrl(YouTubeUtil.canonicalUrl(ytId));
            video.setPoster(thumbnailOverride.isBlank()
                    ? YouTubeUtil.thumbnailUrl(ytId) : thumbnailOverride);
        } else if (creating) {
            throw new IllegalArgumentException("Hay dan link YouTube de dang video.");
        } else if (!thumbnailOverride.isBlank()) {
            video.setPoster(thumbnailOverride);
        }

        if (creating) service.create(video);
        else service.update(video);
    }

    private static void requireOwner(Video video, User owner) {
        if (video == null || video.getUploader() == null
                || !owner.getId().equals(video.getUploader().getId())) {
            throw new IllegalArgumentException("Ban khong so huu video nay.");
        }
    }

    private static String trimmed(String value) {
        return value == null ? "" : value.trim();
    }
}
