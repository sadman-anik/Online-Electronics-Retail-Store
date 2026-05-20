package ebusiness.ejb;

import ebusiness.entity.Product;
import ebusiness.entity.Smartwatch;
import ebusiness.entity.Tablet;
import ebusiness.ejb.ProductException;
import jakarta.ejb.Stateless;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;
import java.util.List;

@Stateless
public class ProductEJB {

    @PersistenceContext(unitName = "RetailStorePU")
    private EntityManager em;

    // Product inventory operations cover the shared product table and subtype tables.
    public Tablet createTablet(Tablet tablet) {
        em.persist(tablet);
        return tablet;
    }

    public Smartwatch createSmartwatch(Smartwatch smartwatch) {
        em.persist(smartwatch);
        return smartwatch;
    }

    public List<Tablet> findTablets() {
        return em.createNamedQuery("Tablet.findAll", Tablet.class).getResultList();
    }

    public List<Smartwatch> findSmartwatches() {
        return em.createNamedQuery("Smartwatch.findAll", Smartwatch.class).getResultList();
    }

    public List<Product> findProducts() {
        return em.createNamedQuery("Product.findAll", Product.class).getResultList();
    }

    public List<Tablet> searchTablets(String model) {
        TypedQuery<Tablet> query = em.createNamedQuery("Tablet.searchByModel", Tablet.class);
        query.setParameter("model", "%" + model + "%");
        return query.getResultList();
    }

    public List<Smartwatch> searchSmartwatches(String model) {
        TypedQuery<Smartwatch> query = em.createNamedQuery("Smartwatch.searchByModel", Smartwatch.class);
        query.setParameter("model", "%" + model + "%");
        return query.getResultList();
    }

    public Product findProductById(Long id) {
        if (id == null) {
            return null;
        }
        return em.find(Product.class, id);
    }

    public void reduceStock(Product product, int quantity)
        throws ProductException {
        if (product == null || product.getId() == null) {
            throw new ProductException("Product is required.");
        }
        if (quantity <= 0) {
            throw new ProductException("Quantity must be greater than zero.");
        }

        Product managed = em.find(Product.class, product.getId());
        if (managed == null) {
            throw new ProductException("Product not found.");
        }
        Integer currentStock = managed.getStockNumber();
        if (currentStock == null || currentStock < quantity) {
            throw new ProductException(
                "Not enough stock for " + managed.getBrandModel() + "."
            );
        }

        managed.setStockNumber(currentStock - quantity);
    }

    public void restoreStock(Product product, int quantity) {
        Product managed = em.find(Product.class, product.getId());
        managed.setStockNumber(managed.getStockNumber() + quantity);
    }
}
