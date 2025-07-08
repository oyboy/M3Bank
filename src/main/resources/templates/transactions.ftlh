<#import "parts/common.ftlh" as c>
<@c.page>
    <div class="container mt-5">
        <div class="row">
            <div class="col-md-12">
                <div class="card">
                    <div class="card-header bg-primary text-white">
                        <h1 class="mb-0">Transaction History</h1>
                    </div>
                    <div class="card-body">
                        <div id="transaction-list">
                            <p>Loading transaction history...</p>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </div>
    <script src="scripts/transactions.js"></script>
</@c.page>
