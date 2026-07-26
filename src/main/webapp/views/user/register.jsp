<%@ page contentType="text/html;charset=UTF-8" language="java" %>

<jsp:include page="/views/layout/header.jsp"/>
<jsp:include page="/views/layout/menu.jsp"/>

<div class="container mt-5">

    <div class="row justify-content-center">

        <div class="col-md-6">

            <div class="card shadow">

                <div class="card-header">

                    <h3 class="text-center">

                        Register

                    </h3>

                </div>

                <div class="card-body">

                    <form method="post"
                          action="${pageContext.request.contextPath}/register">

                        <div class="mb-3">

                            <label>User ID</label>

                            <input
                                    type="text"
                                    name="id"
                                    class="form-control"
                                    required>

                        </div>

                        <div class="mb-3">

                            <label>Full Name</label>

                            <input
                                    type="text"
                                    name="fullname"
                                    class="form-control"
                                    required>

                        </div>

                        <div class="mb-3">

                            <label>Email</label>

                            <input
                                    type="email"
                                    name="email"
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

                        <button class="btn btn-success w-100">

                            Register

                        </button>

                    </form>

                </div>

            </div>

        </div>

    </div>

</div>

<jsp:include page="/views/layout/footer.jsp"/>