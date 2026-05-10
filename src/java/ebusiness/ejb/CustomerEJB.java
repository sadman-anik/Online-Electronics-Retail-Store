package ebusiness.ejb;

import ebusiness.entity.Customer;
import jakarta.ejb.Stateless;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;
import java.util.List;

@Stateless
public class CustomerEJB {

    @PersistenceContext(unitName = "RetailStorePU")
    private EntityManager em;

    public Customer createCustomer(Customer customer) {
        em.persist(customer);
        return customer;
    }

    public List<Customer> findCustomers() {
        return em.createNamedQuery("Customer.findAll", Customer.class).getResultList();
    }

    public Customer findCustomerById(Long id) {
        Customer customer = em.find(Customer.class, id);
        if (customer != null) {
            customer.getOrders().size();
        }
        return customer;
    }

    public List<Customer> searchCustomers(String name) {
        TypedQuery<Customer> query = em.createNamedQuery("Customer.searchByName", Customer.class);
        query.setParameter("name", "%" + name + "%");
        return query.getResultList();
    }
}
