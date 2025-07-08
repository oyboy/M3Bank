document.addEventListener("DOMContentLoaded", function () {
    const form = document.getElementById("registrationForm");

    form.addEventListener("submit", async function (e) {
        e.preventDefault();
        document.querySelectorAll("[id^=error-]").forEach(div => {
            div.classList.add("d-none");
            div.innerText = "";
        });
        const serverError = document.getElementById("server-error");
        serverError.classList.add("d-none");
        serverError.innerText = "";

        const data = {
            firstName: document.getElementById("firstName").value.trim(),
            lastName: document.getElementById("lastName").value.trim(),
            email: document.getElementById("email").value.trim(),
            password: document.getElementById("password").value,
            confirmPassword: document.getElementById("confirmPassword").value
        };

        const csrfToken = document.getElementById("csrf-token").value;

        try {
            const response = await fetch("/api/v1/auth/register", {
                method: "POST",
                headers: {
                    "Content-Type": "application/json",
                    "X-CSRF-TOKEN": csrfToken
                },
                body: JSON.stringify(data)
            });

            if (response.status === 201) {
                window.location.href = "/login";
            } else {
                const body = await response.json();

                if (body.error) {
                    serverError.innerText = body.error;
                    serverError.classList.remove("d-none");
                } else if (typeof body === "object") {
                    for (const [field, message] of Object.entries(body)) {
                        const errorDiv = document.getElementById("error-" + field);
                        if (errorDiv) {
                            errorDiv.innerText = message;
                            errorDiv.classList.remove("d-none");
                        }
                    }
                } else {
                    serverError.innerText = "Неизвестная ошибка регистрации";
                    serverError.classList.remove("d-none");
                }
            }
        } catch (err) {
            serverError.innerText = "Ошибка соединения с сервером";
            serverError.classList.remove("d-none");
            console.error(err);
        }
    });
});
