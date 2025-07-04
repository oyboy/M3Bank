<#import "parts/common.ftlh" as c>
<@c.page>
    <div class="container">
        <h2 class="mt-5">Регистрация</h2>
        <form action="/registration" method="post" class="mt-4">
            <div class="form-group">
                <label for="firstName">Имя:</label>
                <input type="text" class="form-control" name="firstName" id="firstName" required>
                <#if !userCreateError?? && errors?? && errors.getFieldError("firstName")??>
                    <div class="alert alert-danger">${errors.getFieldError("firstName").defaultMessage}</div>
                </#if>
            </div>
            <div class="form-group">
                <label for="lastName">Фамилия:</label>
                <input type="text" class="form-control" name="lastName" id="lastName" required>
                <#if !userCreateError?? && errors?? && errors.getFieldError("lastName")??>
                    <div class="alert alert-danger">${errors.getFieldError("lastName").defaultMessage}</div>
                </#if>
            </div>

            <div class="form-group">
                <label for="email">Почта:</label>
                <input type="email" class="form-control" name="email" id="email" required>
                <#if !userCreateError?? && errors?? && errors.getFieldError("email")??>
                    <div class="alert alert-danger">${errors.getFieldError("email").defaultMessage}</div>
                </#if>
            </div>

            <div class="form-group">
                <label for="password">Пароль:</label>
                <input type="password" class="form-control" name="password" id="password" required>
                <#if !userCreateError?? && errors?? && errors.getFieldError("password")??>
                    <div class="alert alert-danger">${errors.getFieldError("password").defaultMessage}</div>
                </#if>
            </div>

            <div class="form-group">
                <label for="confirmPassword">Подтверждение пароля:</label>
                <input type="password" class="form-control" name="confirmPassword" id="confirmPassword" required>
                <#if !userCreateError?? && errors?? && errors.getFieldError("confirmPassword")??>
                    <div class="alert alert-danger">${errors.getFieldError("confirmPassword").defaultMessage}</div>
                </#if>
            </div>

            <#if userCreateError??>
                <div class="alert alert-danger">${userCreateError}</div>
            </#if>

            <input type="hidden" name="_csrf" value="${_csrf.token}">
            <button type="submit" class="btn btn-primary">Регистрация</button>
        </form>
        <a href="/login" class="mt-3 d-block">Авторизоваться</a>
    </div>
</@c.page>