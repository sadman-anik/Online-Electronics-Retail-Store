package ebusiness.controller;

import ebusiness.ejb.CustomerEJB;
import ebusiness.entity.Customer;
import ebusiness.util.ValidationUtil;
import jakarta.ejb.EJB;
import jakarta.enterprise.context.RequestScoped;
import jakarta.faces.application.FacesMessage;
import jakarta.faces.context.FacesContext;
import jakarta.inject.Named;
import java.util.ArrayList;
import java.util.List;

@Named("customerController")
@RequestScoped
public class CustomerController {

    @EJB
    private CustomerEJB customerEJB;

    private Customer customer = new Customer();
    private Long selectedCustomerId;
    private String searchName;
    private List<Customer> customerList = new ArrayList<>();

    public String doCreateCustomer() {
        FacesContext ctx = FacesContext.getCurrentInstance();
        if (ValidationUtil.isBlank(customer.getName())) ctx.addMessage(null, new FacesMessage("Customer name is required."));
        if (ValidationUtil.isBlank(customer.getAddress())) ctx.addMessage(null, new FacesMessage("Address is required."));
        if (ValidationUtil.isBlank(customer.getEmailAddress())) ctx.addMessage(null, new FacesMessage("Email address is required."));
        if (!ctx.getMessageList().isEmpty()) return null;

        customerEJB.createCustomer(customer);
        customerList = customerEJB.findCustomers();
        ctx.addMessage(null, new FacesMessage("Successfully created the customer: " + customer.getName()));
        return "listCustomers.xhtml";
    }

    public String doSearchCustomer() {
        customerList = customerEJB.searchCustomers(ValidationUtil.trimToEmpty(searchName));
        return "foundCustomers.xhtml";
    }

    public Customer getSelectedCustomer() {
        if (selectedCustomerId == null) return null;
        return customerEJB.findCustomerById(selectedCustomerId);
    }

    public Customer getCustomer() { return customer; }
    public void setCustomer(Customer customer) { this.customer = customer; }
    public Long getSelectedCustomerId() { return selectedCustomerId; }
    public void setSelectedCustomerId(Long selectedCustomerId) { this.selectedCustomerId = selectedCustomerId; }
    public String getSearchName() { return searchName; }
    public void setSearchName(String searchName) { this.searchName = searchName; }
    public List<Customer> getCustomerList() { return customerList.isEmpty() ? customerEJB.findCustomers() : customerList; }
    public void setCustomerList(List<Customer> customerList) { this.customerList = customerList; }
}
