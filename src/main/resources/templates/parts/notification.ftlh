<#import "common.ftlh" as c>
<@c.page>
    <div class="toast-container position-fixed top-0 end-0 p-3" style="z-index: 1055">
        <div id="notif-toast" class="toast align-items-center text-bg-primary border-0" role="alert" aria-live="assertive" aria-atomic="true">
            <div class="d-flex">
                <div class="toast-body" id="notif-text">
                    Уведомление!
                </div>
                <button type="button" class="btn-close btn-close-white me-2 m-auto" data-bs-dismiss="toast" aria-label="Закрыть"></button>
            </div>
        </div>
    </div>
    <input type="hidden" id="currentUserId" value="${user.id}">
    <input type="hidden" name="_csrf" value="${_csrf.token}">
    <script src="scripts/notifications.js"></script>
    <script src="scripts/ws-notifications.js"></script>
</@c.page>