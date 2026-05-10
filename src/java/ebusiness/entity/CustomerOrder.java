package ebusiness.entity;

import jakarta.persistence.*;
import java.io.Serializable;
import java.util.Date;

@Entity
@Table(name = "CUSTOMER_ORDER")
@NamedQueries({
    @NamedQuery(name = "CustomerOrder.findAll", query = "SELECT o FROM CustomerOrder o ORDER BY o.id"),
    @NamedQuery(name = "CustomerOrder.findById", query = "SELECT o FROM CustomerOrder o WHERE o.id = :id")
})
public class CustomerOrder implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(optional = false)
    private Customer customer;

    @ManyToOne(optional = false)
    private Product product;

    @Column(nullable = false)
    private Integer quantity;

    @Column(nullable = false)
    private Double unitPrice;

    @Temporal(TemporalType.TIMESTAMP)
    private Date createdAt = new Date();

    public Long getId() { return id; }
    public Customer getCustomer() { return customer; }
    public void setCustomer(Customer customer) { this.customer = customer; }
    public Product getProduct() { return product; }
    public void setProduct(Product product) { this.product = product; }
    public Integer getQuantity() { return quantity; }
    public void setQuantity(Integer quantity) { this.quantity = quantity; }
    public Double getUnitPrice() { return unitPrice; }
    public void setUnitPrice(Double unitPrice) { this.unitPrice = unitPrice; }
    public Date getCreatedAt() { return createdAt; }
    public void setCreatedAt(Date createdAt) { this.createdAt = createdAt; }

    public Double getTotalPrice() {
        if (unitPrice == null || quantity == null) return 0.0;
        return unitPrice * quantity;
    }
}
