package by.kir.deep.clone;

import by.kir.deep.clone.model.Address;
import by.kir.deep.clone.model.User;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import org.apache.commons.lang.SerializationUtils;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;

public class MainTest {

    private static Address address;
    private static User user;

    @BeforeClass
    public static void init() {
        address = new Address(new ArrayList(Arrays.asList("a", "b", "c")));
        user = new User("123", "456", address);
    }

    @Test
    public void test1() {
        User deepCopy = Cloners.cloneSerialize(user);
        assertUser(deepCopy);
    }

    @Test
    public void whenModifyingOriginalObject_thenGsonCloneShouldNotChange() {
        User deepCopy = Cloners.cloneGson(user);
        assertUser(deepCopy);
    }

    @Test
    public void whenModifyingOriginalObject_thenJacksonCopyShouldNotChange() throws IOException {
        User deepCopy = Cloners.cloneJackson(user);
        assertUser(deepCopy);
    }

    @Test
    public void reflectionClone() throws IOException {
        User deepCopy = ReflectionCloner.cloneObject(user);
        assertUser(deepCopy);
    }

    @Test
    public void vanila() throws IOException {
        User deepCopy = Cloners.vanila(user);
        assertUser(deepCopy);
    }

    @Test
    public void vanila1() throws IOException {
        User deepCopy = Cloners.cloneCustomFastReflection(user);
        assertUser(deepCopy);
    }


    private void assertUser(User deepCopy) {
        Assert.assertNotNull(deepCopy);
        Assert.assertNotSame(user, deepCopy);
        Assert.assertNotSame(user.getFirstName(), deepCopy.getFirstName());
        Assert.assertNotSame(user.getLastName(), deepCopy.getLastName());
        Assert.assertNotSame(user.getAddress(), deepCopy.getAddress());
        Assert.assertNotSame(user.getAddress().getLines(), deepCopy.getAddress().getLines());
        Assert.assertNotSame(user.getAddress().getLines().get(0), deepCopy.getAddress().getLines().get(0));
        Assert.assertNotSame(user.getAddress().getLines().get(1), deepCopy.getAddress().getLines().get(1));
        Assert.assertNotSame(user.getAddress().getLines().get(2), deepCopy.getAddress().getLines().get(2));
        Assert.assertEquals(user.getFirstName(), deepCopy.getFirstName());
        Assert.assertEquals(user.getLastName(), deepCopy.getLastName());
        Assert.assertEquals(user.getAddress().getLines(), deepCopy.getAddress().getLines());
        Assert.assertEquals(user.getAddress().getLines().get(0), deepCopy.getAddress().getLines().get(0));
        Assert.assertEquals(user.getAddress().getLines().get(1), deepCopy.getAddress().getLines().get(1));
        Assert.assertEquals(user.getAddress().getLines().get(2), deepCopy.getAddress().getLines().get(2));
    }
}
