package com.scammers.m3bank.models;

import com.scammers.m3bank.enums.TransactionType;
import lombok.*;

import java.time.LocalDateTime;

@Builder
@Getter
@EqualsAndHashCode
@RequiredArgsConstructor
public class Transaction {
    private final Long id;
    private final TransactionType type;
    private final Double amount;
    private final LocalDateTime timestamp;
    private final String sourceAccountId;
    private final String targetAccountId;

    @Override
    public String toString() {
        return String.format(
                """
                Transaction %d {
                    type: %s,
                    amount: %.2f,
                    timestamp: %s,
                    sourceAccountId: '%s',
                    targetAccountId: '%s'
                }
                """,
                id, type, amount, timestamp, sourceAccountId, targetAccountId
        );
    }
}
