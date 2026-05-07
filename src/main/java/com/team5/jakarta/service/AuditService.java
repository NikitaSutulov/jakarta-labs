package com.team5.jakarta.service;

import com.team5.jakarta.dao.AuditLogDao;
import com.team5.jakarta.model.AuditLog;
import jakarta.ejb.Stateless;
import jakarta.ejb.TransactionAttribute;
import jakarta.ejb.TransactionAttributeType;
import jakarta.inject.Inject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Окремий @Stateless bean — це принципово.
 * Якби ці методи жили в ProductService й викликалися через `this`, EJB-проксі
 * було б обійдено й анотації @TransactionAttribute не виконалися б
 * (класична пастка self-invocation).
 */
@Stateless
public class AuditService {

    @Inject
    private AuditLogDao auditLogDao;

    Logger logger = LoggerFactory.getLogger(this.getClass());

    @TransactionAttribute(TransactionAttributeType.REQUIRED)
    public void logInCurrentTransaction(String operation, String details, boolean succeeded) {
        logger.debug("logInCurrentTransaction: op={}, ok={}", operation, succeeded);
        write(operation, details, succeeded);
    }

    @TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
    public void logInNewTransaction(String operation, String details, boolean succeeded) {
        logger.debug("logInNewTransaction (REQUIRES_NEW): op={}, ok={}", operation, succeeded);
        write(operation, details, succeeded);
    }

    @TransactionAttribute(TransactionAttributeType.MANDATORY)
    public void logMandatory(String operation, String details, boolean succeeded) {
        logger.debug("logMandatory (MANDATORY): op={}, ok={}", operation, succeeded);
        write(operation, details, succeeded);
    }

    @TransactionAttribute(TransactionAttributeType.SUPPORTS)
    public List<AuditLog> getAll() {
        return auditLogDao.findAllOrdered();
    }

    private void write(String operation, String details, boolean succeeded) {
        AuditLog entry = new AuditLog();
        entry.setOperation(operation);
        entry.setDetails(details);
        entry.setCreatedAt(LocalDateTime.now());
        entry.setSucceeded(succeeded);
        auditLogDao.save(entry);
    }
}
