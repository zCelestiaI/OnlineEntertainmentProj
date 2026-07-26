<%@ page contentType="text/html;charset=UTF-8" language="java"%>

<jsp:include page="/views/layout/header.jsp"/>
<jsp:include page="/views/layout/menu.jsp"/>

<div class="container mt-5">

    <div class="row justify-content-center">

        <div class="col-md-5">

            <div class="card shadow">

                <div class="card-header">

                    <h3>

                        Forgot Password

                    </h3>

                </div>

                <div class="card-body">

                    <form method="post"
                          action="${pageContext.request.contextPath}/forgot-password">

                        <div class="mb-3">

                            <label>Email</label>

                            <input
                                    type="email"
                                    name="email"
                                    class="form-control"
                                    required>

                        </div>

                        <button class="btn btn-primary w-100">

                            Send Password

                        </button>

                    </form>

                    <div class="mt-3 text-success">

                        ${message}

                    </div>

                </div>

            </div>

        </div>

    </div>

</div>

<jsp:include page="/views/layout/footer.jsp"/>