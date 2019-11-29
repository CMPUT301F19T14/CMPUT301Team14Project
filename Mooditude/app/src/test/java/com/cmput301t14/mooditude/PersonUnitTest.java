package com.cmput301t14.mooditude;

import com.cmput301t14.mooditude.models.Person;

import org.junit.Assert;
import org.junit.Test;

public class PersonUnitTest {

    @Test
    public void personClassTest(){
        Person person = new Person("test@test.com","test");
        Assert.assertTrue(person.getEmail().equals("test@test.com"));
        Assert.assertTrue(person.getName().equals("test"));

        person.setEmail("set@set.com");
        person.setName("set");
        Assert.assertTrue(person.getEmail().equals("set@set.com"));
        Assert.assertTrue(person.getName().equals("set"));

    }
}
