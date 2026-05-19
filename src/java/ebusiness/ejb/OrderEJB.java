package ebusiness.ejb;

import ebusiness.entity.Customer;
import ebusiness.entity.CustomerOrder;
import ebusiness.entity.Product;
import ebusiness.ejb.OrderException;
import jakarta.ejb.Stateless;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import java.util.Date;
import java.util.List;

@Stateless
public class OrderEJB {

    @PersistenceContext(unitName = "RetailStorePU")
    private EntityManager em;

    // Order creation validates inventory before reducing product stock.
    public CustomerOrder createOrder(Long customerId, Long productId, Integer quantity) throws OrderException {
        if (customerId == null) {
            throw new OrderException("Customer is required.");
        }
        if (productId == null) {
            throw new OrderException("Product is required.");
        }
        if (quantity == null || quantity <= 0) {
            throw new OrderException("Quantity must be greater than zero.");
        }
        if (quantity > 1000) {
            throw new OrderException("Quantity must be 1000 or less.");
        }

        Customer customer = em.find(Customer.class, customerId);
        Product product = em.find(Product.class, productId);

        if (customer == null) {
            throw new OrderException("Customer not found.");
        }
        if (product == null) {
            throw new OrderException("Product not found.");
        }
        if (product.getStockNumber() == null || product.getStockNumber() <= 0) {
            throw new OrderException(product.getBrandModel() + " is currently out of stock.");
        }
        if (product.getStockNumber() < quantity) {
            throw new OrderException("Not enough stock for " + product.getBrandModel() + ".");
        }

        CustomerOrder order = new CustomerOrder();
        order.setCustomer(customer);
        order.setProduct(product);
        order.setQuantity(quantity);
        order.setUnitPrice(product.getPrice());
        order.setCreatedAt(new Date());

        product.setStockNumber(product.getStockNumber() - quantity);
        em.persist(order);
        return order;
    }

    public List<CustomerOrder> findOrders() {
        return em.createNamedQuery("CustomerOrder.findAll", CustomerOrder.class).getResultList();
    }

    public CustomerOrder findOrderById(Long id) {
        if (id == null) {
            return null;
        }
        return em.find(CustomerOrder.class, id);
    }

    public void deleteOrder(Long id) throws OrderException {
        CustomerOrder order = em.find(CustomerOrder.class, id);
        if (order == null) {
            throw new OrderException("Order not found.");
        }
        Product product = order.getProduct();
        if (product != null && order.getQuantity() != null) {
            int currentStock = product.getStockNumber() == null ? 0 : product.getStockNumber();
            product.setStockNumber(currentStock + order.getQuantity());
        }
        em.remove(order);
    }
}
