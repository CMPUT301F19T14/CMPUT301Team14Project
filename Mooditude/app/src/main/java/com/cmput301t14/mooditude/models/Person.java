package com.cmput301t14.mooditude.models;


/**
 * Define one user class contain name and email to identify one person
 */
public class Person {
    private String name;
    private String email;

    /**
     * Constructor
     * @param email user email
     * @param name user name
     */
    public Person( String email, String name) {
        this.name = name;
        this.email = email;
    }

    /**
     * get name
     * @return user name
     */
    public String getName() {
        return name;
    }

    /**
     * set name
     * @param name user name
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * get email
     * @return user email
     */
    public String getEmail() {
        return email;
    }

    /**
     * set email
     * @param email user email
     */
    public void setEmail(String email) {
        this.email = email;
    }
}
