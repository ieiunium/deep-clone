package by.kir.deep.clone;

import by.kir.deep.clone.model.User;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import lombok.SneakyThrows;
import org.apache.commons.lang.SerializationUtils;

import java.io.IOException;

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

}
