<#import "../parts/common.ftlh" as c>
<@c.page>
    <h1>Transfer Money</h1>

    <#if error??>
        <div class="error">${error}</div>
    </#if>

    <form action="/account/transfer" method="post">
        <input type="hidden" name="sender_uuid" value="${sender_uuid}">

        <label for="receiver_uuid">Receiver Account UUID:</label>
        <input type="text" id="receiver_uuid" name="receiver_uuid" required>

        <label for="amount">Amount:</label>
        <input type="number" id="amount" name="amount" step="0.01" min="0.01" required>

        <input type="hidden" name="_csrf" value="${_csrf.token}">
        <button type="submit">Transfer</button>
    </form>

    <#if message??>
        <div class="success">${message}</div>
    </#if>
</@c.page>
