<#import "../parts/common.ftlh" as c>
<@c.page>
    <div class="container mt-5">
        <div class="row">
            <div class="col-md-8 offset-md-2">
                <div class="card">
                    <div class="card-header bg-success text-white">
                        <h1 class="mb-0">Create New Account</h1>
                    </div>
                    <div class="card-body">
                        <#if error??>
                            <div class="alert alert-danger" role="alert">${error}</div>
                        </#if>

                        <form action="/account/create" method="post">
                            <div class="mb-3">
                                <label for="type" class="form-label">Account Type:</label>
                                <select class="form-select" name="type" id="type" required>
                                    <#list accountTypes as type>
                                        <option value="${type}">${type}</option>
                                    </#list>
                                </select>
                            </div>

                            <div class="mb-3">
                                <label for="balance" class="form-label">Initial Balance:</label>
                                <input type="number" class="form-control" id="balance" name="balance" step="0.01" min="0" required>
                            </div>

                            <input type="hidden" name="_csrf" value="${_csrf.token}">
                            <button type="submit" class="btn btn-primary">
                                <i class="fas fa-plus-circle"></i> Create Account
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