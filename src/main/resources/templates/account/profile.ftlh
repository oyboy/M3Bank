<#import "../parts/common.ftlh" as c>
<@c.page>
    <div class="container mt-5">
        <#include "../parts/notification.ftlh">

        <div class="row">
            <div class="col-md-8 offset-md-2">
                <div class="card">
                    <div class="card-header bg-primary text-white">
                        <h1 class="mb-0">My Accounts</h1>
                    </div>
                    <div class="card-body">
                        <#if error??>
                            <div class="alert alert-danger" role="alert">${error}</div>
                        </#if>

                        <#if accounts?? && (accounts?size > 0)>
                            <div class="table-responsive">
                                <table class="table table-striped table-hover">
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
                                                <a href="/account/view/${account.accountUUID}" class="btn btn-sm btn-info">View</a>
                                                <a href="/account/transfer/${account.accountUUID}" class="btn btn-sm btn-warning">Transfer</a>
                                            </td>
                                        </tr>
                                    </#list>
                                    </tbody>
                                </table>
                            </div>
                        <#else>
                            <p class="alert alert-info">No accounts found.</p>
                        </#if>

                        <a href="/account/create" class="btn btn-success">Create a New Account</a>
                    </div>
                </div>
            </div>
        </div>
    </div>
</@c.page>
