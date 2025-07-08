document.addEventListener("DOMContentLoaded", () => {
    const usersContainer = document.getElementById("usersContainer");
    const transactionTable = document.getElementById("transactionTable");
    const transactionBody = document.getElementById("transactionTableBody");
    const messageBox = document.getElementById("adminMessage");
    const csrfToken = document.getElementById("csrf-token").value;
    const currentUserLabel = document.getElementById("currentUserLabel");
    const loadBtn = document.getElementById("loadTransactions");

    // Загрузка и отрисовка пользователей
    async function loadUsers() {
        const res = await fetch('/api/v1/admin/users');
        if (!res.ok) {
            showMessage("Ошибка загрузки пользователей", "danger");
            return;
        }
        const users = await res.json();
        usersContainer.innerHTML = "";

        users.forEach(user => {
            // col для ровной сетки
            const col = document.createElement("div");
            col.className = "col-md-6 mb-4";

            const card = document.createElement("div");
            card.className = "card h-100 shadow-sm";

            const cardBody = document.createElement("div");
            cardBody.className = "card-body d-flex flex-column";

            const title = document.createElement("h5");
            title.className = "card-title";
            title.textContent = `${user.firstName} ${user.lastName}`;

            // Покажем email и ID пользователя (ID не было видно!)
            const subTitle = document.createElement("h6");
            subTitle.className = "card-subtitle mb-2 text-muted";
            subTitle.innerHTML = `Email: ${user.email} <br> <small>ID: ${user.id}</small>`;

            // Счета пользователя
            const list = document.createElement("ul");
            list.className = "list-group list-group-flush mt-3";

            user.accounts.forEach(acc => {
                const li = document.createElement("li");
                li.className = "list-group-item d-flex justify-content-between align-items-center";

                li.innerHTML = `
                    <div>
                        <strong>Счёт:</strong> ${acc.accountUUID} <br>
                        <strong>Тип:</strong> ${acc.accountType} <br>
                        <strong>Баланс:</strong> ${acc.balance.toFixed(2)} руб. <br>
                        <strong>Статус:</strong> ${acc.blocked ? "<span class='text-danger'>Заблокирован</span>" : "<span class='text-success'>Активен</span>"}
                    </div>
                    <button data-uuid="${acc.accountUUID}" class="btn btn-sm ${acc.blocked ? 'btn-success' : 'btn-danger'} toggle-block-btn">
                        ${acc.blocked ? "Разблокировать" : "Заблокировать"}
                    </button>
                `;


                list.appendChild(li);
            });

            cardBody.appendChild(title);
            cardBody.appendChild(subTitle);

            card.appendChild(cardBody);
            card.appendChild(list);

            // Нажатие на карточку — загрузка транзакций этого пользователя
            card.style.cursor = "pointer";
            card.addEventListener("click", () => {
                loadTransactions(user.id);
            });

            col.appendChild(card);
            usersContainer.appendChild(col);
        });

        // Кнопки блокировки счетов
        document.querySelectorAll(".toggle-block-btn").forEach(btn => {
            btn.addEventListener("click", async (e) => {
                e.stopPropagation(); // чтобы клик не пошёл на карточку и не грузил транзакции
                const uuid = e.target.dataset.uuid;
                const action = e.target.textContent.includes("Разблокировать") ? "unblock" : "block";

                const response = await fetch(`/api/v1/admin/accounts/${uuid}/${action}`, {
                    method: 'POST',
                    headers: {
                        "X-Requested-With": "XMLHttpRequest",
                        "X-CSRF-TOKEN": csrfToken,
                    }
                });
                if (response.ok) {
                    showMessage("Статус счёта изменён", "success");
                    loadUsers();
                } else {
                    showMessage("Не удалось изменить статус счета", "danger");
                }
            });
        });
    }

    // Загрузка транзакций пользователя
    async function loadTransactions(userId) {
        if (!userId) return;
        try {
            const res = await fetch(`/api/v1/admin/users/${userId}/transactions`);
            if (!res.ok) throw new Error("Ошибка загрузки транзакций");

            const transactions = await res.json();
            transactionBody.innerHTML = "";

            transactions.forEach(tx => {
                const row = `
                    <tr>
                        <td>${tx.id}</td>
                        <td>${tx.type}</td>
                        <td>${tx.amount.toFixed(2)}</td>
                        <td>${tx.sourceAccountId ?? "-"}</td>
                        <td>${tx.targetAccountId ?? "-"}</td>
                        <td>${new Date(tx.timestamp).toLocaleString()}</td>
                    </tr>
                `;
                transactionBody.insertAdjacentHTML("beforeend", row);
            });
            transactionTable.classList.remove("d-none");
            currentUserLabel.textContent = `(ID: ${userId})`;
        } catch (err) {
            showMessage("Ошибка при загрузке транзакций", "danger");
            console.error(err);
            clearTransactions();
            currentUserLabel.textContent = "";
        }
    }

    function clearTransactions() {
        transactionBody.innerHTML = "";
        transactionTable.classList.add("d-none");
    }

    function showMessage(msg, type) {
        messageBox.innerText = msg;
        messageBox.className = `alert alert-${type}`;
        messageBox.classList.remove("d-none");
        setTimeout(() => messageBox.classList.add("d-none"), 5000);
    }

    loadBtn.addEventListener("click", () => {
        const userId = userIdInput.value.trim();
        if (!userId) {
            showMessage("Введите ID пользователя", "warning");
            return;
        }
        loadTransactions(userId);
    });

    loadUsers();
});

