<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<style>
    /* CSS đồng bộ với menu.jsp */
    body { transition: background-color 0.3s, color 0.3s; }
    body.dark-mode { background-color: #121212 !important; color: #f1f1f1 !important; }
    body.dark-mode .card { background-color: #1e1e1e !important; border-color: #333 !important; }
    body.dark-mode .table { color: #f1f1f1 !important; border-color: #333 !important; }
    body.dark-mode .form-control { background-color: #2a2a2a !important; color: #fff !important; }
    #theme-icon-admin { cursor: pointer; font-size: 1.25rem; }
</style>
<!-- Nhúng Icons cho Admin -->
<link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap-icons@1.10.0/font/bootstrap-icons.css">

<nav class="navbar navbar-expand-lg navbar-dark mb-4 shadow" style="background-color: #111111;">
    <div class="container">
        <a class="navbar-brand fw-bold" href="${pageContext.request.contextPath}/admin/dashboard" style="color: #ffc107;">
            ADMINISTRATION TOOL
        </a>
        <div class="collapse navbar-collapse" id="adminNavbar">
            <ul class="navbar-nav me-auto">
                <li class="nav-item"><a class="nav-link" href="${pageContext.request.contextPath}/admin/dashboard" style="color: #28a745;">DASHBOARD</a></li>
                <li class="nav-item"><a class="nav-link" href="${pageContext.request.contextPath}/admin/video" style="color: #28a745;">VIDEOS</a></li>
                <li class="nav-item"><a class="nav-link" href="${pageContext.request.contextPath}/admin/user" style="color: #28a745;">USERS</a></li>
                <li class="nav-item"><a class="nav-link" href="${pageContext.request.contextPath}/admin/report" style="color: #28a745;">REPORTS</a></li>
                <li class="nav-item border-start ms-2 ps-2">
                    <a class="nav-link text-info" href="${pageContext.request.contextPath}/home">VIEW SITE</a>
                </li>
            </ul>
            <div class="d-flex align-items-center">
                <!-- Nút Toggle Theme cho Admin -->
                <i class="bi bi-sun-fill text-warning me-3" id="theme-icon-admin" onclick="toggleGlobalTheme()"></i>

                <ul class="navbar-nav">
                    <li class="nav-item dropdown">
                        <a class="nav-link dropdown-toggle text-white" href="#" id="adminDropdown" role="button" data-bs-toggle="dropdown">
                            Admin: <strong>${currentUser.fullname}</strong>
                        </a>
                        <ul class="dropdown-menu dropdown-menu-end shadow">
                            <li><a class="dropdown-item" href="${pageContext.request.contextPath}/profile">My Profile</a></li>
                            <li><a class="dropdown-item" href="${pageContext.request.contextPath}/change-password">Change Password</a></li>
                            <li><hr class="dropdown-divider"></li>
                            <li><a class="dropdown-item text-danger" href="${pageContext.request.contextPath}/logout">Logout</a></li>
                        </ul>
                    </li>
                </ul>
            </div>
        </div>
    </div>
</nav>

<script>
    function applyTheme(theme) {
        const icon = document.getElementById('theme-icon-admin') || document.getElementById('theme-icon');
        if (theme === 'dark-mode') {
            document.body.classList.add('dark-mode');
            if (icon) { icon.classList.replace('bi-sun-fill', 'bi-moon-stars-fill'); icon.classList.replace('text-warning', 'text-light'); }
        } else {
            document.body.classList.remove('dark-mode');
            if (icon) { icon.classList.replace('bi-moon-stars-fill', 'bi-sun-fill'); icon.classList.replace('text-light', 'text-warning'); }
        }
    }
    function toggleGlobalTheme() {
        let currentTheme = localStorage.getItem('globalTheme') || 'light-mode';
        let newTheme = currentTheme === 'light-mode' ? 'dark-mode' : 'light-mode';
        localStorage.setItem('globalTheme', newTheme);
        applyTheme(newTheme);
    }
    (function() {
        const savedTheme = localStorage.getItem('globalTheme') || 'light-mode';
        applyTheme(savedTheme);
    })();
</script>
