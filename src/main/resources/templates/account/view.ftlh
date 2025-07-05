<#import "../parts/common.ftlh" as c>
<@c.page>
    <h1>Account Information</h1>

    <#if error??>
        <div class="error">${error}</div>
    </#if>

    <#if account??>
        <table>
            <tr>
                <th>Account UUID</th>
                <td>${account.accountUUID}</td>
            </tr>
            <tr>
                <th>Balance</th>
                <td>${account.balance}</td>
            </tr>
            <tr>
                <th>Account Type</th>
                <td>${account.accountType}</td>
            </tr>
            <tr>
                <th>Blocked</th>
                <td>${account.blocked?string("Yes", "No")}</td>
            </tr>
        </table>

        <a href="/account/transfer/${account.accountUUID}">Transfer Money</a>
    <#else>
        <p>Account not found.</p>
    </#if>
</@c.page>
