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
import java.util.logging.Level;
import java.util.logging.Logger;

@Named("customerController")
@RequestScoped
public class CustomerController {
    private static final Logger LOGGER = Logger.getLogger(CustomerController.class.getName());

    @EJB
    private CustomerEJB customerEJB;

    private Customer customer = new Customer();
    private Long selectedCustomerId;
    private Customer selectedCustomer;
    private boolean selectedCustomerResolved;
    private String searchName;
    private List<Customer> customerList = new ArrayList<>();

    public String doCreateCustomer() {
        FacesContext ctx = FacesContext.getCurrentInstance();
        if (!validateCustomer(customer, ctx))
            return null;
        try {
            customerEJB.createCustomer(customer);
            customerList = customerEJB.findCustomers();
            ctx.addMessage(null, new FacesMessage("Successfully created the customer: " + customer.getName()));
            return "listCustomers.xhtml";
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Failed to create customer: " + customer.getName(), e);
            ctx.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR,
                    "Create customer failed",
                    "Unable to save customer right now. Please check the details and try again."));
            return null;
        }
    }

    private Boolean validateCustomer(Customer customer, FacesContext ctx) {
        if (ValidationUtil.isBlank(customer.getName()))
            ctx.addMessage(null, new FacesMessage("Customer name is required."));
        if (ValidationUtil.isBlank(customer.getAddress()))
            ctx.addMessage(null, new FacesMessage("Address is required."));
        if (ValidationUtil.isBlank(customer.getEmailAddress()))
            ctx.addMessage(null, new FacesMessage("Email address is required."));
        
        return ctx.getMessageList().isEmpty();
    }

    public String doSearchCustomer() {
        searchName = ValidationUtil.trimToEmpty(searchName);
        if (ValidationUtil.isBlank(searchName)) {
            FacesContext.getCurrentInstance().addMessage(
                    null,
                    new FacesMessage(FacesMessage.SEVERITY_ERROR, "Customer name is required for search.", null));
            return null;
        }
        customerList = customerEJB.searchCustomers(searchName);
        return "foundCustomers.xhtml";
    }

    public Customer getSelectedCustomer() {
        if (selectedCustomerId == null) {
            return null;
        }
        if (!selectedCustomerResolved) {
            selectedCustomer = customerEJB.findCustomerById(selectedCustomerId);
            selectedCustomerResolved = true;
            if (selectedCustomer == null) {
                FacesContext.getCurrentInstance().addMessage(
                    null,
                    new FacesMessage(
                        FacesMessage.SEVERITY_WARN,
                        "Customer not found",
                        "No customer exists for id " + selectedCustomerId + "."
                    )
                );
            }
        }
        return selectedCustomer;
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
