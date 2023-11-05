package com.javabootcamp.gym.data.model;

import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

class UserTest {
    @Test
    void testUserConstructorWithId() {
        User user = new User(1, "Andres", "Hoyos", "andres.hoyos", "some_password", true);

        assertEquals(1, user.getId());
        assertEquals("Andres", user.getFirstName());
        assertEquals("Hoyos", user.getLastName());
        assertEquals("andres.hoyos", user.getUsername());
        assertEquals("some_password", user.getPassword());
        assertTrue(user.isActive());
    }

    @Test
    void testUserConstructorWithoutId() {
        User user = new User("Andres", "Hoyos");

        assertEquals("Andres", user.getFirstName());
        assertEquals("Hoyos", user.getLastName());
        assertFalse(user.isActive());

    }

    @Test
    void testUserNotEquals() {

        User user1 = new User(1, "Andres", "Hoyos", "andres.hoyos", "some_password", true);
        User user2 = new User(2, "David", "Mejia", "david.mejia", "some_password", false);


        assertNotEquals(user1, user2);
    }

    @Test
    void testUserHashCode() {
        User user1 = new User(1, "Andres", "Hoyos", "andres.hoyos", "some_password", true);
        User user2 = new User(2, "David", "Mejia", "david.mejia", "some_password", false);

        assertNotEquals(user1.hashCode(), user2.hashCode());
    }

    @Test
    void testUserGettersSetters() {
        LocalDate date1 = LocalDate.now();
        LocalDate date2 = date1.minusYears(1);
        User user = new User(1, "Andres", "Hoyos", "andres.hoyos", "some_password", true);

        user.setId(2);
        user.setFirstName("David");
        user.setLastName("Mejia");
        user.setUsername("david.mejia");
        user.setPassword("new_password");
        user.setActive(false);


        assertNotEquals(1, user.getId());
        assertNotEquals("Andres", user.getFirstName());
        assertNotEquals("Hoyos", user.getLastName());
        assertNotEquals("andres.hoyos", user.getUsername());
        assertNotEquals("some_password", user.getPassword());
        assertNotEquals(true, user.isActive());



        assertEquals(2, user.getId());
        assertEquals("David", user.getFirstName());
        assertEquals("Mejia", user.getLastName());
        assertEquals("david.mejia", user.getUsername());
        assertEquals("new_password", user.getPassword());
        assertFalse(user.isActive());
    }
}