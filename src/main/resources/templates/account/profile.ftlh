<#import "../parts/common.ftlh" as c>
<@c.page>
    <h1>My Accounts</h1>

    <#if error??>
        <div class="error">${error}</div>
    </#if>

    <#if accounts?? && (accounts?size > 0)>
        <table>
            <thead>
            <tr>
                <th>Account UUID</th>
                <th>Balance</th>
                <th>Account Type</th>
                <th>Actions</th>
            </tr>
            </thead>
            <tbody>
            <#list accounts as account>
                <tr>
                    <td>${account.accountUUID}</td>
                    <td>${account.balance}</td>
                    <td>${account.accountType}</td>
                    <td>
                        <a href="/account/view/${account.accountUUID}">View</a> |
                        <a href="/account/transfer/${account.accountUUID}">Transfer</a>
                    </td>
                </tr>
            </#list>
            </tbody>
        </table>
    <#else>
        <p>No accounts found.</p>
    </#if>

    <a href="/account/create">Create a new account</a>
</@c.page>
