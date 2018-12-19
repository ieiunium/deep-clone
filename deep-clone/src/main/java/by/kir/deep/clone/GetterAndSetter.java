package by.kir.deep.clone;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.lang.reflect.Method;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class GetterAndSetter {
    private Method getter;
    private Method isGetter;
    private Method setter;
}
