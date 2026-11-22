package com.example.java4main.utils;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class YouTubeUtilTest {
    private static final String ID = "dQw4w9WgXcQ";

    @Test void acceptsWatchAndShortLinks() {
        assertEquals(ID, YouTubeUtil.extractId("https://www.youtube.com/watch?v=" + ID + "&t=20"));
        assertEquals(ID, YouTubeUtil.extractId("https://youtu.be/" + ID + "?si=abc"));
        assertEquals(ID, YouTubeUtil.extractId("https://youtube.com/shorts/" + ID));
        assertEquals(ID, YouTubeUtil.extractId("https://www.youtube.com/live/" + ID));
        assertEquals(ID, YouTubeUtil.extractId("https://www.youtube-nocookie.com/embed/" + ID));
    }

    @Test void rejectsBadOrImpersonatingHosts() {
        assertNull(YouTubeUtil.extractId("http://www.youtube.com/watch?v=" + ID));
        assertNull(YouTubeUtil.extractId("https://youtube.com.attacker.test/watch?v=" + ID));
        assertNull(YouTubeUtil.extractId("https://evil.test/youtu.be/" + ID));
        assertNull(YouTubeUtil.extractId("https://www.youtube.com/watch?v=short"));
        assertNull(YouTubeUtil.extractId("https://www.youtube.com/watch?v=" + ID + "evil"));
        assertNull(YouTubeUtil.extractId(""));
        assertNull(YouTubeUtil.extractId("not a URL"));
    }

    @Test void buildsCanonicalUrlAndThumbnail() {
        assertEquals("https://www.youtube.com/watch?v=" + ID, YouTubeUtil.canonicalUrl(ID));
        assertEquals("https://img.youtube.com/vi/" + ID + "/hqdefault.jpg",
                YouTubeUtil.thumbnailUrl(ID));
    }
}
