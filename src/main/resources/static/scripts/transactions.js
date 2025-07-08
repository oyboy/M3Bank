document.addEventListener('DOMContentLoaded', function() {
    fetch('/api/v1/history')
        .then(response => {
            if (!response.ok) {
                throw new Error('Network response was not ok');
            }
            return response.json();
        })
        .then(transactions => {
            displayTransactions(transactions);
        })
        .catch(error => {
            console.error('There was a problem fetching the transaction history:', error);
            document.getElementById('transaction-list').innerHTML = '<p class="alert alert-danger">Failed to load transaction history.</p>';
        });

    function displayTransactions(transactions) {
        let tableHtml = `
            <div class="table-responsive">
                <table class="table table-striped table-bordered">
                    <thead>
                        <tr>
                            <th>ID</th>
                            <th>Type</th>
                            <th>Amount</th>
                            <th>Timestamp</th>
                            <th>Source Account</th>
                            <th>Target Account</th>
                        </tr>
                    </thead>
                    <tbody>
                        ${transactions.map(transaction => `
                            <tr>
                                <td>${transaction.id}</td>
                                <td>${transaction.type}</td>
                                <td>${transaction.amount}</td>
                                <td>${new Date(transaction.timestamp).toLocaleString()}</td>
                                <td>${transaction.sourceAccountId || '-'}</td>
                                <td>${transaction.targetAccountId || '-'}</td>
                            </tr>
                        `).join('')}
                    </tbody>
                </table>
            </div>
        `;
        document.getElementById('transaction-list').innerHTML = tableHtml;
    }
});
