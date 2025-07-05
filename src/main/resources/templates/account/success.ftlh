<#import "../parts/common.ftlh" as c>

<@c.page>
    <div class="container">
        <div class="row justify-content-center">
            <div class="col-md-6 col-12">
                <div class="card shadow-lg rounded-lg border-0 bg-success text-white">
                    <div class="card-body">
                        <h2 class="card-title text-center mb-4">Transfer Successful!</h2>
                        <p class="card-text text-center lead mb-4">Your transfer was completed successfully.</p>

                        <div class="text-center">
                            <p><strong>Transferred Amount:</strong> ${amount} <span class="badge badge-light">${currency}</span></p>
                            <p><strong>Receiver's Account:</strong> ${receiver_uuid}</p>
                        </div>

                        <div class="text-center">
                            <a href="/account" class="btn btn-light btn-lg mx-2">Go to My Accounts</a>
                            <a href="/account/transfer" class="btn btn-dark btn-lg mx-2">Make another transfer</a>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </div>
</@c.page>
