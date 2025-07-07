package com.scammers.m3bank.repositories;

import com.scammers.m3bank.models.dto.AuditRecord;
import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class AuditRepository {
    private final JdbcTemplate jdbcTemplate;

    public void save(AuditRecord auditRecord) {
        String command = "INSERT INTO audit_logs (username, action, timestamp) " +
                "VALUES (?, ?, ?)";

        jdbcTemplate.update(command,
                auditRecord.username(),
                auditRecord.action(),
                auditRecord.timestamp()
        );
    }
}
