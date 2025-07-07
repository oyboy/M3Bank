$(document).ready(() => {
    const userId = Number($('#currentUserId').val());

    if (!userId) {
        console.warn("User ID не задан — уведомления не будут загружаться.");
    } else {
        function showToast(message) {
            const toastEl = document.getElementById('notif-toast');
            const toastText = document.getElementById('notif-text');

            if (!toastEl || !toastText) {
                console.error("Элементы toast не найдены в DOM.");
                return;
            }

            toastText.innerText = message;

            const toast = new bootstrap.Toast(toastEl);
            toast.show();
        }

        function fetchNotifications() {
            fetch(`/api/v1/notifications?userId=${userId}`)
                .then(response => {
                    if (!response.ok) {
                        throw new Error(`Ошибка запроса: ${response.status}`);
                    }
                    return response.json();
                })
                .then(data => {
                    if (data.length > 0) {
                        const latest = data[0];
                        const sender = latest.sender || "Отправитель";
                        const message = latest.message || "Новое уведомление";
                        showToast(`📬 ${sender}: ${message}`);
                    }
                })
                .catch(error => {
                    console.error("Ошибка при получении уведомлений:", error);
                });
        }

        fetchNotifications();
    }
});