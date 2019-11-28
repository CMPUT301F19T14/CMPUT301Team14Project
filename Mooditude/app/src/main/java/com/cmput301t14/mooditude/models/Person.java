package com.cmput301t14.mooditude.models;


/**
 * Define one user class contain name and email to identify one person
 */
public class Person {
    private String name;
    private String email;

    /**
     * Constructor
     * @param email
     * @param name
     */
    public Person( String email, String name) {
        this.name = name;
        this.email = email;
    }

    /**
     * get name
     * @return
     */
    public String getName() {
        return name;
    }

    /**
     * set name
     * @param name
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * get email
     * @return
     */
    public String getEmail() {
        return email;
    }

    /**
     * set email
     * @param email
     */
    public void setEmail(String email) {
        this.email = email;
    }
}
