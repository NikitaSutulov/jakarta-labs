package com.team5.jakarta.dao;

import com.team5.jakarta.model.AuditLog;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import lombok.NoArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

@ApplicationScoped
@NoArgsConstructor
public class AuditLogDao {

    @PersistenceContext(unitName = "myPersistenceUnit")
    private EntityManager em;

    Logger logger = LoggerFactory.getLogger(this.getClass());

    public AuditLog save(AuditLog log) {
        logger.debug("Saving audit log entry: {}", log.getOperation());
        em.persist(log);
        return log;
    }

    public List<AuditLog> findAllOrdered() {
        logger.debug("Fetching all audit log entries (ordered DESC by id)");
        return em.createQuery("SELECT a FROM AuditLog a ORDER BY a.id DESC", AuditLog.class)
                .getResultList();
    }
}
