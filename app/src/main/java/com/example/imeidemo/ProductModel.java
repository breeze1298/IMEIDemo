package com.example.imeidemo;

public class ProductModel {
    //To long when changed the price to int
    private String name,price;

    //For Firebase
    private ProductModel(){}

    //For Us to pass the data
    private ProductModel(String name,String price)
    {
        this.name=name;
        this.price=price;

    }
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }
}
