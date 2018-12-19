package by.kir.deep.clone;

import lombok.SneakyThrows;
import net.sf.cglib.reflect.FastClass;
import net.sf.cglib.reflect.FastConstructor;
import net.sf.cglib.reflect.FastMethod;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class PojoCloner {


    @SneakyThrows
    @SuppressWarnings("all")
    public static <T> T cloneObject(T object) {
        if (object == null) {
            return object;
        }
        Class clazz = object.getClass();
        if (clazz == String.class) {
            return (T) new String((String) object);
        } else if (clazz == Integer.class) {
            return (T) new Integer((Integer) object);
        } else if (clazz == Long.class) {
            return (T) new Long((Long) object);
        } else if (clazz == Double.class) {
            return (T) new Double((Double) object);
        } else if (clazz == Float.class) {
            return (T) new Float((Float) object);
        } else if (clazz == Short.class) {
            return (T) new Short((Short) object);
        } else if (clazz == Byte.class) {
            return (T) new Byte((Byte) object);
        } else if (clazz == Character.class) {
            return (T) new Character((Character) object);
        } else if (clazz == Boolean.class) {
            return (T) new Boolean((Boolean) object);
        } else if (clazz.isArray()) {
            Object[] array = ((Object[]) object).clone();
            for (int i = 0; i < array.length; i++) {
                array[i] = cloneObject(array[i]);
            }
            return (T) array;
        } else if (object instanceof List) {
            List src = (List) object;
            List list = createInstance(src);
            for (int i = 0; i < src.size(); i++) {
                list.add(cloneObject(src.get(i)));
            }
            return (T) list;
        }
        final FastClass fastClass = FastClass.create(clazz);
        final FastConstructor constructor = fastClass.getConstructor(clazz.getDeclaredConstructor());
        final Object newInstance = constructor.newInstance();
        final Method[] declaredMethods = clazz.getDeclaredMethods();
        final Map<String, GetterAndSetter> getterAndSetterMap = new HashMap<>();
        for (Method declaredMethod : declaredMethods) {
            if (java.lang.reflect.Modifier.isStatic(declaredMethod.getModifiers())) {
                continue;
            }
            if (declaredMethod.getName().startsWith("get")) {
                final String key = declaredMethod.getName().substring(3);
                GetterAndSetter getterAndSetter = getGetterAndSetter(getterAndSetterMap, key);
                getterAndSetter.setGetter(declaredMethod);
            }
            if (declaredMethod.getName().startsWith("set")) {
                final String key = declaredMethod.getName().substring(3);
                GetterAndSetter getterAndSetter = getGetterAndSetter(getterAndSetterMap, key);
                getterAndSetter.setSetter(declaredMethod);
            }
            if (declaredMethod.getName().startsWith("is")) {
                final String key = declaredMethod.getName().substring(2);
                GetterAndSetter getterAndSetter = getGetterAndSetter(getterAndSetterMap, key);
                getterAndSetter.setIsGetter(declaredMethod);
            }
//            FastMethod fastMethod = fastClass.getMethod(declaredMethod);
//            Object oldFieldValue = declaredField.get(object);
//            Object newFieldValue = cloneObject(oldFieldValue);
        }

        List<GetterAndSetter> collect = getterAndSetterMap
                .entrySet()
                .stream()
                .map(Map.Entry::getValue)
                .peek(it -> {
                    if (it.getGetter() == null) {
                        it.setGetter(it.getIsGetter());
                    }
                })
                .filter(it -> it.getGetter() != null && it.getSetter() != null)
                .collect(Collectors.toList());

        for (GetterAndSetter it : collect) {
            final FastMethod fastGetter = fastClass.getMethod(it.getGetter());
            final FastMethod fastSetter = fastClass.getMethod(it.getSetter());
            final Object newValue = cloneObject(fastGetter.invoke(object, null));
            fastSetter.invoke(newInstance, new Object[]{newValue});
        }
        return ((T) newInstance);
    }

    private static GetterAndSetter getGetterAndSetter(Map<String, GetterAndSetter> getterAndSetterMap, String key) {
        GetterAndSetter getterAndSetter = getterAndSetterMap.get(key);
        if (getterAndSetter == null) {
            getterAndSetter = new GetterAndSetter();
            getterAndSetterMap.put(key, getterAndSetter);
        }
        return getterAndSetter;
    }

    @SneakyThrows
    public static <T> T createInstance(T src){
        final FastClass fastClass = FastClass.create(src.getClass());
        final FastConstructor constructor = fastClass.getConstructor(src.getClass().getDeclaredConstructor());
        return (T) constructor.newInstance();
    }
}