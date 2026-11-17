<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<style>
    /* Global Styles */
    body { transition: background-color 0.3s, color 0.3s; }

    /* Dark Mode Override */
    body.dark-mode { background-color: #121212 !important; color: #f1f1f1 !important; }
    body.dark-mode .card { background-color: #1e1e1e !important; border-color: #333 !important; color: #f1f1f1 !important; }
    body.dark-mode .table { color: #f1f1f1 !important; border-color: #333 !important; }
    body.dark-mode .table thead { background-color: #000 !important; }
    body.dark-mode .list-group-item { background-color: #1e1e1e !important; color: #f1f1f1 !important; border-color: #333 !important; }
    body.dark-mode .form-control { background-color: #2a2a2a !important; color: #fff !important; border-color: #444 !important; }
    body.dark-mode .dropdown-menu { background-color: #252525 !important; border-color: #444 !important; }
    body.dark-mode .dropdown-item { color: #fff !important; }
    body.dark-mode .dropdown-item:hover { background-color: #333 !important; }
    body.dark-mode .text-muted, body.dark-mode .small { color: #bbb !important; }

    .search-box { max-width: 400px; flex-grow: 1; margin: 0 20px; }
    #theme-icon { cursor: pointer; font-size: 1.25rem; margin-right: 15px; }
</style>

<nav class="navbar navbar-expand-lg navbar-dark bg-dark shadow-sm mb-4 sticky-top">
    <div class="container">
        <a class="navbar-brand fw-bold" href="${pageContext.request.contextPath}/home">ONLINE ENTERTAINMENT</a>

        <button class="navbar-toggler" type="button" data-bs-toggle="collapse" data-bs-target="#navbarNav">
            <span class="navbar-toggler-icon"></span>
        </button>

        <div class="collapse navbar-collapse" id="navbarNav">
            <!-- Search Bar -->
            <form class="d-flex search-box" action="${pageContext.request.contextPath}/home" method="get">
                <div class="input-group">
                    <input class="form-control" type="search" name="keyword" placeholder="Tìm kiếm phim..." value="${param.keyword}">
                    <button class="btn btn-outline-warning" type="submit">Search</button>
                </div>
            </form>

            <ul class="navbar-nav me-auto">
                <li class="nav-item"><a class="nav-link" href="${pageContext.request.contextPath}/home">Home</a></li>
                <c:if test="${not empty currentUser}">
                    <li class="nav-item"><a class="nav-link" href="${pageContext.request.contextPath}/chat">Community Chat</a></li>
                    <li class="nav-item"><a class="nav-link" href="${pageContext.request.contextPath}/favorite">My Favorites</a></li>
                    <c:if test="${currentUser.admin}">
                        <li class="nav-item border-start ms-2 ps-2">
                            <a class="nav-link text-warning fw-bold" href="${pageContext.request.contextPath}/admin/dashboard">ADMIN PANEL</a>
                        </li>
                    </c:if>
                </c:if>
            </ul>

            <div class="d-flex align-items-center">
                <!-- Theme Icon -->
                <i class="bi bi-sun-fill text-warning" id="theme-icon" onclick="toggleGlobalTheme()"></i>

                <ul class="navbar-nav">
                    <c:choose>
                        <c:when test="${empty currentUser}">
                            <li class="nav-item"><a class="nav-link" href="${pageContext.request.contextPath}/login">Login</a></li>
                            <li class="nav-item"><a class="nav-link btn btn-primary btn-sm text-white ms-2" href="${pageContext.request.contextPath}/register">Register</a></li>
                        </c:when>
                        <c:otherwise>
                            <li class="nav-item dropdown">
                                <a class="nav-link dropdown-toggle" href="#" id="userDropdown" role="button" data-bs-toggle="dropdown">
                                    <strong>${currentUser.fullname}</strong>
                                </a>
                                <ul class="dropdown-menu dropdown-menu-end shadow">
                                    <li><a class="dropdown-item" href="${pageContext.request.contextPath}/profile">My Profile</a></li>
                                    <li><a class="dropdown-item" href="${pageContext.request.contextPath}/change-password">Change Password</a></li>
                                    <li><hr class="dropdown-divider"></li>
                                    <li><a class="dropdown-item text-danger" href="${pageContext.request.contextPath}/logout">Logout</a></li>
                                </ul>
                            </li>
                        </c:otherwise>
                    </c:choose>
                </ul>
            </div>
        </div>
    </div>
</nav>

<script>
    function applyTheme(theme) {
        const icon = document.getElementById('theme-icon') || document.getElementById('theme-icon-admin');
        if (theme === 'dark-mode') {
            document.body.classList.add('dark-mode');
            if (icon) {
                icon.classList.replace('bi-sun-fill', 'bi-moon-stars-fill');
                icon.classList.replace('text-warning', 'text-light');
            }
        } else {
            document.body.classList.remove('dark-mode');
            if (icon) {
                icon.classList.replace('bi-moon-stars-fill', 'bi-sun-fill');
                icon.classList.replace('text-light', 'text-warning');
            }
        }
    }

    function toggleGlobalTheme() {
        let currentTheme = localStorage.getItem('globalTheme') || 'light-mode';
        let newTheme = currentTheme === 'light-mode' ? 'dark-mode' : 'light-mode';
        localStorage.setItem('globalTheme', newTheme);
        applyTheme(newTheme);
    }

    // Run on load
    (function() {
        const savedTheme = localStorage.getItem('globalTheme') || 'light-mode';
        applyTheme(savedTheme);
    })();
</script>
