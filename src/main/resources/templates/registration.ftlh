<#import "parts/common.ftlh" as c>
<@c.page>
    <div class="container">
        <h2 class="mt-5">Регистрация</h2>
        <form id="registrationForm" class="mt-4">
            <div class="form-group">
                <label for="firstName">Имя:</label>
                <input type="text" class="form-control" id="firstName" required>
                <div class="alert alert-danger d-none" id="error-firstName"></div>
            </div>

            <div class="form-group">
                <label for="lastName">Фамилия:</label>
                <input type="text" class="form-control" id="lastName" required>
                <div class="alert alert-danger d-none" id="error-lastName"></div>
            </div>

            <div class="form-group">
                <label for="email">Почта:</label>
                <input type="email" class="form-control" id="email" required>
                <div class="alert alert-danger d-none" id="error-email"></div>
            </div>

            <div class="form-group">
                <label for="password">Пароль:</label>
                <input type="password" class="form-control" id="password" required>
                <div class="alert alert-danger d-none" id="error-password"></div>
            </div>

            <div class="form-group">
                <label for="confirmPassword">Подтверждение пароля:</label>
                <input type="password" class="form-control" id="confirmPassword" required>
                <div class="alert alert-danger d-none" id="error-confirmPassword"></div>
            </div>

            <div class="alert alert-danger d-none" id="server-error"></div>

            <input type="hidden" id="csrf-token" value="${_csrf.token}">
            <button type="submit" class="btn btn-primary">Регистрация</button>
        </form>
        <a href="/login" class="mt-3 d-block">Авторизоваться</a>
    </div>

    <script src="scripts/register.js"></script>
</@c.page>
