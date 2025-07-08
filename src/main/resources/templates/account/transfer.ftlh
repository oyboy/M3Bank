<#import "../parts/common.ftlh" as c>
<@c.page>
    <div class="container mt-5">
        <div class="row">
            <div class="col-md-8 offset-md-2">
                <div class="card">
                    <div class="card-header bg-warning text-white">
                        <h1 class="mb-0">Transfer Money</h1>
                    </div>
                    <div class="card-body">
                        <#if error??>
                            <div class="alert alert-danger" role="alert">${error}</div>
                        </#if>

                        <form action="/account/transfer" method="post">
                            <input type="hidden" name="sender_uuid" value="${sender_uuid}">

                            <div class="mb-3">
                                <label for="receiver_uuid" class="form-label">Receiver Account UUID:</label>
                                <input type="text" class="form-control" id="receiver_uuid" name="receiver_uuid" required>
                            </div>

                            <div class="mb-3">
                                <label for="amount" class="form-label">Amount:</label>
                                <div class="input-group">
                                    <input type="number" class="form-control" id="amount" name="amount" step="0.01" min="0.01" required>
                                    <span class="input-group-text">RUB</span> <!-- Замените на нужную валюту -->
                                </div>
                            </div>

                            <input type="hidden" name="_csrf" value="${_csrf.token}">
                            <button type="submit" class="btn btn-primary">
                                <i class="fas fa-exchange-alt"></i> Transfer
                            </button>
                            <a href="/account" class="btn btn-secondary">
                                <i class="fas fa-arrow-left"></i> Back to Accounts
                            </a>
                        </form>

                        <#if message??>
                            <div class="alert alert-success" role="alert">${message}</div>
                        </#if>
                    </div>
                </div>
            </div>
        </div>
    </div>
</@c.page>
