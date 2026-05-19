package ebusiness.entity;

import jakarta.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = "TABLET")
public class Tablet extends Product implements Serializable {

    private String storageCapacity;
    private Boolean stylusSupport;
    private String batteryCapacity;

    public String getStorageCapacity() { return storageCapacity; }
    public void setStorageCapacity(String storageCapacity) { this.storageCapacity = storageCapacity; }
    public Boolean getStylusSupport() { return stylusSupport; }
    public void setStylusSupport(Boolean stylusSupport) { this.stylusSupport = stylusSupport; }
    public String getBatteryCapacity() { return batteryCapacity; }
    public void setBatteryCapacity(String batteryCapacity) { this.batteryCapacity = batteryCapacity; }
}
