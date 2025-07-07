package com.scammers.m3bank.components;

import com.scammers.m3bank.annotations.Auditable;
import com.scammers.m3bank.models.dto.AuditRecord;
import com.scammers.m3bank.repositories.AuditRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Aspect
@Component
@RequiredArgsConstructor
@Slf4j
public class AuditAspect {
    private final AuditRepository auditRepository;

    @Around("@annotation(auditable)")
    public Object logAudit(ProceedingJoinPoint joinPoint, Auditable auditable) throws Throwable {
        String username = getCurrentUsername();

        String action = auditable.action();
        String method = joinPoint.getSignature().toShortString();

        LocalDateTime timestamp = LocalDateTime.now();

        log.info("📝 [AUDIT] {} invoked '{}' at {}", username, action.isBlank() ? method : action, timestamp);

        auditRepository.save(new AuditRecord(username, action, timestamp));

        return joinPoint.proceed();
    }

    private String getCurrentUsername() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        return (auth != null && auth.isAuthenticated()) ? auth.getName() : "anonymous";
    }
}
