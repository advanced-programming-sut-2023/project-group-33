import model.Application;
import model.User;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import utility.DataManager;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Scanner;

import static org.junit.jupiter.api.Assertions.*;

public class TestUser {

    User user1 = new User("bluh", "S@hand844", "duh", "asdfa@dsaf.com", 2, "bluh");
    User user2 = new User("bluhasdf", "S@hand844", "nibba", "asdfa@dsakf.com", 1, "bluh");


    @Test
    public void passCheckTest() {
        assertFalse(user1.checkPassword("duh"));
        assertTrue(user1.checkPassword("S@hand844"));

        user1.setPassword("newPass");

        assertTrue(user1.checkPassword("newPass"));
        assertFalse(user1.checkPassword("S@hand844"));
    }

    @Test
    public void emailCheckTest() {
        assertFalse(user1.checkEmail("duh"));
        assertTrue(user1.checkEmail("aSDFa@dsaf.com"));

        user1.setEmail("newEmail");

        assertTrue(user1.checkEmail("newEmail"));
        assertTrue(user1.checkEmail("newEmAIl"));
        assertFalse(user1.checkEmail("newEmAI"));
        assertFalse(user1.checkEmail("aSDFa@dsaf.com"));
    }

    @Test
    public void gettersTest(){
        assertEquals("bluh", user1.getUsername());
        assertEquals("bluhasdf", user2.getUsername());

        assertNull(user1.getSlogan());
        assertNull(user2.getSlogan());
    }
}
