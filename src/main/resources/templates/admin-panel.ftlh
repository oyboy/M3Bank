<#import "parts/common.ftlh" as c>
<@c.page>
    <div class="container mt-5">
        <h2 class="mb-4">Админ-панель</h2>

        <div id="usersContainer" class="row"></div>

        <hr class="my-4">

        <h4>Транзакции пользователя <span id="currentUserLabel"></span></h4>
        <div class="table-responsive">
            <table class="table table-bordered table-hover mt-3 d-none" id="transactionTable">
                <thead class="thead-dark">
                <tr>
                    <th>ID транзакции</th>
                    <th>Тип</th>
                    <th>Сумма</th>
                    <th>Откуда</th>
                    <th>Куда</th>
                    <th>Дата</th>
                </tr>
                </thead>
                <tbody id="transactionTableBody"></tbody>
            </table>
        </div>

        <div class="form-inline mt-3 mb-3">
            <input
                    type="text"
                    id="userIdInput"
                    class="form-control mr-2"
                    placeholder="Введите ID пользователя"
            />
            <button id="loadTransactions" class="btn btn-primary">
                Загрузить транзакции
            </button>
        </div>

        <div id="adminMessage" class="alert d-none mt-3" role="alert"></div>
    </div>
    <input type="hidden" id="csrf-token" value="${_csrf.token}">
    <script src="/scripts/admin.js"></script>
</@c.page>
