package ebusiness.controller;

import ebusiness.ejb.CustomerEJB;
import ebusiness.ejb.OrderEJB;
import ebusiness.ejb.OrderException;
import ebusiness.ejb.ProductEJB;
import ebusiness.entity.Customer;
import ebusiness.entity.CustomerOrder;
import ebusiness.entity.Product;
import jakarta.ejb.EJB;
import jakarta.enterprise.context.RequestScoped;
import jakarta.faces.application.FacesMessage;
import jakarta.faces.context.FacesContext;
import jakarta.inject.Named;
import java.util.Collections;
import java.util.List;

@Named("orderController")
@RequestScoped
public class OrderController {

    @EJB
    private OrderEJB orderEJB;
    @EJB
    private CustomerEJB customerEJB;
    @EJB
    private ProductEJB productEJB;

    private Long customerId;
    private Long productId;
    private Integer quantity;
    private Long searchOrderId;
    private CustomerOrder foundOrder;

    public String doCreateOrder() {
        FacesContext ctx = FacesContext.getCurrentInstance();
        if (customerId == null || productId == null) {
            ctx.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR,
                    "Order not created", "Select both a customer and a product before creating the order."));
            return null;
        }
        try {
            CustomerOrder order = orderEJB.createOrder(customerId, productId, quantity);
            ctx.addMessage(null, new FacesMessage("Successfully created order #" + order.getId()));
            return "listOrders.xhtml";
        } catch (OrderException ex) {
            ctx.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR,
                    ex.getMessage(), null));
            return null;
        }
    }

    public String doSearchOrder() {
        foundOrder = orderEJB.findOrderById(searchOrderId);
        if (foundOrder == null) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("Order not found."));
            return null;
        }
        return "foundOrder.xhtml";
    }

    public String doDeleteOrder(Long id) {
        FacesContext ctx = FacesContext.getCurrentInstance();
        try {
            orderEJB.deleteOrder(id);
            ctx.addMessage(null, new FacesMessage("The order has been deleted."));
        } catch (OrderException ex) {
            ctx.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Delete failed", ex.getMessage()));
        }
        return "listOrders.xhtml";
    }

    public List<CustomerOrder> getOrderList() { return orderEJB.findOrders(); }
    public List<Customer> getCustomerList() { return customerEJB.findCustomers(); }
    public List<Product> getProductList() { return productEJB.findProducts(); }
    public Long getCustomerId() { return customerId; }
    public void setCustomerId(Long customerId) { this.customerId = customerId; }
    public Long getProductId() { return productId; }
    public void setProductId(Long productId) { this.productId = productId; }
    public Integer getQuantity() { return quantity; }
    public void setQuantity(Integer quantity) { this.quantity = quantity; }
    public Long getSearchOrderId() { return searchOrderId; }
    public void setSearchOrderId(Long searchOrderId) { this.searchOrderId = searchOrderId; }
    public CustomerOrder getFoundOrder() { return foundOrder; }
    public List<CustomerOrder> getFoundOrderList() {
        return foundOrder == null ? Collections.emptyList() : Collections.singletonList(foundOrder);
    }
}
