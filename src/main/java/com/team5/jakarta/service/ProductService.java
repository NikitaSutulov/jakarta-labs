package com.team5.jakarta.service;

import com.team5.jakarta.dao.ProductDao;
import com.team5.jakarta.model.Product;
import jakarta.ejb.EJB;
import jakarta.ejb.EJBException;
import jakarta.ejb.Stateless;
import jakarta.ejb.TransactionAttribute;
import jakarta.ejb.TransactionAttributeType;
import jakarta.inject.Inject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

@Stateless
public class ProductService {

    @Inject
    private ProductDao productDao;

    @EJB
    private AuditService auditService;

    Logger logger = LoggerFactory.getLogger(this.getClass());

    @TransactionAttribute(TransactionAttributeType.SUPPORTS)
    public List<Product> getAllProducts() {
        return productDao.findAll();
    }

    @TransactionAttribute(TransactionAttributeType.SUPPORTS)
    public List<Product> getProductsByCategoryId(int categoryId) {
        return productDao.findAllByCategoryId(categoryId);
    }

    @TransactionAttribute(TransactionAttributeType.SUPPORTS)
    public Product getProductById(int id) {
        return productDao.findById(id).orElse(null);
    }

    @TransactionAttribute(TransactionAttributeType.REQUIRED)
    public Product addProduct(Product product) {
        return productDao.save(product);
    }

    @TransactionAttribute(TransactionAttributeType.REQUIRED)
    public boolean updateProduct(Product product) {
        return productDao.update(product);
    }

    @TransactionAttribute(TransactionAttributeType.REQUIRED)
    public boolean deleteProduct(int id) {
        return productDao.delete(id);
    }

    @TransactionAttribute(TransactionAttributeType.SUPPORTS)
    public int countAll() {
        return productDao.countAll();
    }

    /**
     * Lab 7, п.3 + п.4 (REQUIRED).
     * Масово застосовує знижку до всіх товарів вказаної категорії — багато UPDATE
     * у таблиці `products` в межах однієї транзакції. Викликає AuditService у
     * тій же транзакції (REQUIRED): якщо forceFailure=true або падає інша помилка,
     * відкочуються і оновлення цін, і запис у журналі аудиту.
     */
    @TransactionAttribute(TransactionAttributeType.REQUIRED)
    public int applyDiscountToCategory(int categoryId, double percent, boolean forceFailure) {
        if (percent <= 0 || percent >= 100) {
            throw new IllegalArgumentException("percent має бути в (0, 100), отримано: " + percent);
        }
        List<Product> products = productDao.findAllByCategoryId(categoryId);
        if (products.isEmpty()) {
            throw new IllegalStateException("У категорії " + categoryId + " немає товарів для знижки");
        }

        int updated = 0;
        for (Product p : products) {
            double oldPrice = p.getPrice();
            double newPrice = round2(oldPrice * (1.0 - percent / 100.0));
            p.setPrice(newPrice);
            productDao.update(p);
            updated++;
            logger.info("Discount applied: product#{} {} -> {}", p.getId(), oldPrice, newPrice);
        }

        String details = "categoryId=" + categoryId + ", percent=" + percent + ", rows=" + updated;
        auditService.logInCurrentTransaction("BULK_DISCOUNT", details, true);

        if (forceFailure) {
            throw new EJBException("Симульована помилка після bulk-discount: транзакція має відкотитись");
        }
        return updated;
    }

    /**
     * Lab 7, п.4 (REQUIRES_NEW).
     * Та сама бізнес-операція, але журнал аудиту фіксує спробу В ОКРЕМІЙ транзакції
     * перед основною роботою (REQUIRES_NEW у AuditService). Якщо основна частина
     * відкочується, запис BULK_DISCOUNT_ATTEMPT у журналі ЗАЛИШАЄТЬСЯ — це і є
     * демонстрація розповсюдження REQUIRES_NEW.
     *
     * УВАГА: applyDiscountToCategory(...) викликається через `this` (self-invocation)
     * — анотація @TransactionAttribute внутрішнього методу ігнорується. Тут це
     * безпечно, бо обидва методи REQUIRED і вже працюють в одній транзакції.
     */
    @TransactionAttribute(TransactionAttributeType.REQUIRED)
    public int applyDiscountToCategoryWithIndependentAudit(int categoryId, double percent, boolean forceFailure) {
        String attemptDetails = "categoryId=" + categoryId + ", percent=" + percent
                + ", forceFailure=" + forceFailure;
        auditService.logInNewTransaction("BULK_DISCOUNT_ATTEMPT", attemptDetails, false);

        int updated = applyDiscountToCategory(categoryId, percent, forceFailure);

        auditService.logInNewTransaction(
                "BULK_DISCOUNT_SUCCESS",
                "categoryId=" + categoryId + ", percent=" + percent + ", rows=" + updated,
                true);
        return updated;
    }

    private static double round2(double v) {
        return Math.round(v * 100.0) / 100.0;
    }
}
