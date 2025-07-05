<#import "../parts/common.ftlh" as c>
<@c.page>
    <h1>Create New Account</h1>

    <#if error??>
        <div class="error">${error}</div>
    </#if>

    <form action="/account/create" method="post">
        <label for="type">Account Type:</label>
        <select name="type" id="type" required>
            <#list accountTypes as type>
                <option value="${type}">${type}</option>
            </#list>
        </select>

        <label for="balance">Initial Balance:</label>
        <input type="number" id="balance" name="balance" step="0.01" min="0" required>

        <input type="hidden" name="_csrf" value="${_csrf.token}">
        <button type="submit">Create Account</button>
    </form>

    <#if message??>
        <div class="success">${message}</div>
    </#if>
</@c.page>
