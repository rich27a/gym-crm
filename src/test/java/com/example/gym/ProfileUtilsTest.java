package com.example.gym;

import com.example.gym.utils.Profile;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class ProfileUtilsTest {

    @Test
    public void testGenerateUsername(){
        String username = Profile.generateUsername("Juan","Perez");
        assertEquals("Juan.Perez",username);
    }
    @Test
    public void testGenerateUserWithDuplicate(){
        String username = Profile.generateUsername("Jose","Hernandez");
        username = Profile.generateUsername("Jose","Hernandez");
        assertEquals("Jose.Hernandez2",username);
    }
    @Test
    public void testGeneratePassword() {
        String password = Profile.generatePassword();
        assertEquals(10, password.length());
    }
}
