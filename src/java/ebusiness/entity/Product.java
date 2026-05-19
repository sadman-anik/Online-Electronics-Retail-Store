package ebusiness.entity;

import jakarta.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = "PRODUCT")
@Inheritance(strategy = InheritanceType.JOINED)
@NamedQueries({
    @NamedQuery(
        name = "Product.findAll",
        query = "SELECT p FROM Product p ORDER BY p.id"
    ),
    @NamedQuery(
        name = "Product.findByModel",
        query = "SELECT p FROM Product p WHERE LOWER(p.model) LIKE LOWER(:model)"
    )
})
public abstract class Product implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String brand;

    @Column(nullable = false)
    private String model;

    private String displaySize;
    private String weight;
    private String operatingSystem;
    private String connectivity;
    private Boolean wifiCapable;

    public Long getId() { return id; }
    public String getBrand() { return brand; }
    public void setBrand(String brand) { this.brand = brand; }
    public String getModel() { return model; }
    public void setModel(String model) { this.model = model; }
    public String getDisplaySize() { return displaySize; }
    public void setDisplaySize(String displaySize) { this.displaySize = displaySize; }
    public String getWeight() { return weight; }
    public void setWeight(String weight) { this.weight = weight; }
    public String getOperatingSystem() { return operatingSystem; }
    public void setOperatingSystem(String operatingSystem) { this.operatingSystem = operatingSystem; }
    public String getConnectivity() { return connectivity; }
    public void setConnectivity(String connectivity) { this.connectivity = connectivity; }
    public Boolean getWifiCapable() { return wifiCapable; }
    public void setWifiCapable(Boolean wifiCapable) { this.wifiCapable = wifiCapable; }
}
