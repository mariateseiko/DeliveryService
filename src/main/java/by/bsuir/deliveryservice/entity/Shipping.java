package by.bsuir.deliveryservice.entity;

import java.io.Serializable;

public class Shipping extends Entity implements Serializable {
    private String name;
    private Double pricePerKg;
    private Double pricePerKm;

    public Shipping() {}
    public Shipping(String name) {
        this.name = name;
    }
    public Shipping(String name, Double pricePerKg, Double pricePerKm) {
        this.name = name;
        this.pricePerKg = pricePerKg;
        this.pricePerKm = pricePerKm;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Double getPricePerKg() {
        return pricePerKg;
    }

    public void setPricePerKg(Double pricePerKg) {
        this.pricePerKg = pricePerKg;
    }

    public Double getPricePerKm() {
        return pricePerKm;
    }

    public void setPricePerKm(Double pricePerKm) {
        this.pricePerKm = pricePerKm;
    }
}
