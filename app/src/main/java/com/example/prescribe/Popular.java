package com.example.prescribe;

public class Popular {

    private String pname, price, description, image;

    public Popular() {}

    @Override
    public String toString() {
        return "Popular{" +
                "pname='" + pname + '\'' +
                ", price='" + price + '\'' +
                ", description='" + description + '\'' +
                ", image='" + image + '\'' +
                '}';
    }

    public Popular(String pname, String price, String description, String image) {
        this.pname = pname;
        this.price = price;
        this.description = description;
        this.image = image;
    }

    public String getPname() {
        return pname;
    }

    public void setPname(String pname) {
        this.pname = pname;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }
}
