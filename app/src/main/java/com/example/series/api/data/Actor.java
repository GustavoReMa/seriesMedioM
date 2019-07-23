package com.example.series.api.data;

public class Actor {
    private String name;
    private String image;
    private String role;

    public Actor(String name, String image, String role) {
        this.name = name;
        this.image = image;
        this.role = role;
    }

    public String getName() {
        return name;
    }

    public String getImage() {
        return image;
    }

    public String getRole() {
        return role;
    }
}
