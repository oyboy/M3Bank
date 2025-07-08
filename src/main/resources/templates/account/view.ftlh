<#import "../parts/common.ftlh" as c>
<@c.page>
    <div class="container mt-5">
        <div class="row">
            <div class="col-md-8 offset-md-2">
                <div class="card">
                    <div class="card-header bg-info text-white">
                        <h1 class="mb-0">Account Information</h1>
                    </div>
                    <div class="card-body">
                        <#if error??>
                            <div class="alert alert-danger" role="alert">${error}</div>
                        </#if>

                        <#if account??>
                            <div class="table-responsive">
                                <table class="table table-bordered">
                                    <tbody>
                                    <tr>
                                        <th scope="row">Account UUID</th>
                                        <td>${account.accountUUID}</td>
                                    </tr>
                                    <tr>
                                        <th scope="row">Balance</th>
                                        <td>${account.balance?string.currency}</td>
                                    </tr>
                                    <tr>
                                        <th scope="row">Account Type</th>
                                        <td>${account.accountType}</td>
                                    </tr>
                                    <tr>
                                        <th scope="row">Blocked</th>
                                        <td>${account.blocked?string("Yes", "No")}</td>
                                    </tr>
                                    </tbody>
                                </table>
                            </div>

                            <a href="/account/transfer/${account.accountUUID}" class="btn btn-warning">
                                <i class="fas fa-exchange-alt"></i> Transfer Money
                            </a>
                            <a href="/account" class="btn btn-secondary">
                                <i class="fas fa-arrow-left"></i> Back to Accounts
                            </a>
                        <#else>
                            <p class="alert alert-warning">Account not found.</p>
                        </#if>
                    </div>
                </div>
            </div>
        </div>
    </div>
</@c.page>
