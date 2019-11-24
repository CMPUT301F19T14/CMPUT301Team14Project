package com.cmput301t14.mooditude.models;

public class Person {
    private String name;
    private String email;

    public Person( String email, String name) {
        this.name = name;
        this.email = email;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
