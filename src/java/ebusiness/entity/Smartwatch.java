package ebusiness.entity;

import jakarta.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = "SMARTWATCH")
public class Smartwatch extends Product implements Serializable {

    private String healthMonitoring;
    private String fitnessTracking;
    private String wearableConnectivity;

    public String getHealthMonitoring() { return healthMonitoring; }
    public void setHealthMonitoring(String healthMonitoring) { this.healthMonitoring = healthMonitoring; }
    public String getFitnessTracking() { return fitnessTracking; }
    public void setFitnessTracking(String fitnessTracking) { this.fitnessTracking = fitnessTracking; }
    public String getWearableConnectivity() { return wearableConnectivity; }
    public void setWearableConnectivity(String wearableConnectivity) { this.wearableConnectivity = wearableConnectivity; }
}
