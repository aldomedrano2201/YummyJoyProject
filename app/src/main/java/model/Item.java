package model;

import java.util.Date;

import util.ItemStatus;

public class Item {
    private int id;
    private String name;
    private Integer numberItemsAvailable;
    private String time;
    private Double price;
    private ItemStatus.Status status;

    public Item(int id, String name, String time, Double price) {
        this.id = id;
        this.name = name;
        this.numberItemsAvailable = numberItemsAvailable;
        this.time = time;
        this.price = price;
        this.status = ItemStatus.Status.PUBLISHED;
    }


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getNumberItemsAvailable() {
        return numberItemsAvailable;
    }

    public void setNumberItemsAvailable(Integer numberItemsAvailable) {
        this.numberItemsAvailable = numberItemsAvailable;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public ItemStatus.Status getStatus() {
        return status;
    }

    public void setStatus(ItemStatus.Status status) {
        this.status = status;
    }
}
