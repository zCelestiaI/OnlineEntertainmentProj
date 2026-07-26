<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>

<jsp:include page="/views/layout/header.jsp"/>
<jsp:include page="/views/layout/menu.jsp"/>

<div class="container mt-5">

    <div class="row justify-content-center">

        <div class="col-md-5">

            <div class="card shadow">

                <div class="card-header text-center">

                    <h3>Login</h3>

                </div>

                <div class="card-body">

                    <form method="post"
                          action="${pageContext.request.contextPath}/login">

                        <div class="mb-3">

                            <label>User ID</label>

                            <input
                                    type="text"
                                    name="id"
                                    class="form-control"
                                    required>

                        </div>

                        <div class="mb-3">

                            <label>Password</label>

                            <input
                                    type="password"
                                    name="password"
                                    class="form-control"
                                    required>

                        </div>

                        <button class="btn btn-primary w-100">

                            Login

                        </button>

                    </form>

                    <c:if test="${message!=null}">

                        <div class="alert alert-danger mt-3">

                                ${message}

                        </div>

                    </c:if>

                    <div class="text-center mt-3">

                        <a href="${pageContext.request.contextPath}/forgot-password">

                            Forgot password?

                        </a>

                    </div>

                    <div class="text-center mt-2">

                        <a href="${pageContext.request.contextPath}/register">

                            Create account

                        </a>

                    </div>

                </div>

            </div>

        </div>

    </div>

</div>

<jsp:include page="/views/layout/footer.jsp"/>