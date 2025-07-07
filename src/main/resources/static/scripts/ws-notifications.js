(() => {
    const userId = Number($('#currentUserId').val());
    const csrfToken = $('input[name="_csrf"]').val();

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

    if (userId) {
        const socket = new SockJS('/ws');
        const stompClient = Stomp.over(socket);

        stompClient.connect({}, function () {
            stompClient.subscribe('/topic/notifications/' + userId, function (message) {
                const payload = JSON.parse(message.body);
                const text = `📬 ${payload.sender}: ${payload.message}`;
                showToast(text);

                fetch(`/api/v1/notifications/read/${payload.id}`, {
                    method: 'POST',
                    headers: {
                        'X-CSRF-TOKEN': csrfToken
                    }
                }).catch(e => console.error('Ошибка при отметке прочитанного:', e));
            });
        });
    } else {
        console.warn("User ID не найден — WebSocket не подключен");
    }
})();
