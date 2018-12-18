package by.kir.deep.clone;

import by.kir.deep.clone.model.Address;
import by.kir.deep.clone.model.User;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import lombok.SneakyThrows;
import org.apache.commons.lang.SerializationUtils;

import java.io.IOException;
import java.util.ArrayList;

public class Cloners {

    private static final ObjectMapper objectMapper = new ObjectMapper();
    private static final Gson gson = new Gson();


    public static User cloneSerialize(User src) {
        return (User) SerializationUtils.clone(src);
    }

    public static User cloneGson(User src) {
        return gson.fromJson(gson.toJson(src), User.class);
    }

    @SneakyThrows
    public static User cloneJackson(User src) {
        return objectMapper
                .readValue(objectMapper.writeValueAsString(src), User.class);
    }

    public static User cloneCustomReflection(User src) {
        return ReflectionCloner.cloneObject(src);
    }

    public static User cloneCustomFastReflection(User src) {
        return PojoCloner.cloneObject(src);
    }

    public static User vanila(User user) {
        if (user == null) {
            return null;
        }
        User user1 = new User();
        if (user.getFirstName() != null) {
            user1.setFirstName(new String(user.getFirstName()));
        }
        if (user.getLastName() != null) {
            user1.setLastName(new String(user.getLastName()));
        }
        if (user.getAddress() != null) {
            Address address = new Address();
            if (user.getAddress().getLines()!=null) {
                address.setLines(new ArrayList<>());
                for (String line : user.getAddress().getLines()) {
                    address.getLines().add(new String(line));
                }
            }
            user1.setAddress(address);
        }
        return user1;
    }
}
