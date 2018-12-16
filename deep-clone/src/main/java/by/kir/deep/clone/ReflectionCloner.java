package by.kir.deep.clone;

import lombok.SneakyThrows;

import java.lang.reflect.Field;

public class ReflectionCloner {


    @SneakyThrows
    @SuppressWarnings("all")
    public static <T> T cloneObject(T object) {
        if (object == null){
            return object;
        }
        Class clazz = object.getClass();
        if (clazz == String.class) {
            return (T) new String((String)object);
        } else if (clazz == Integer.class) {
            return (T) new Integer((Integer)object);
        } else if (clazz == Long.class) {
            return (T) new Long((Long)object);
        } else if (clazz == Double.class) {
            return (T) new Double((Double)object);
        } else if (clazz == Float.class) {
            return (T) new Float((Float)object);
        } else if (clazz == Short.class) {
            return (T) new Short((Short)object);
        } else if (clazz == Byte.class) {
            return (T) new Byte((Byte)object);
        } else if (clazz == Character.class) {
            return (T) new Character((Character)object);
        } else if (clazz == Boolean.class) {
            return (T) new Boolean((Boolean)object);
        } else  if (clazz.isArray()){
            Object[] array = ((Object[]) object).clone();
            for (int i = 0; i < array.length; i++) {
                array[i] = cloneObject(array[i]);
            }
            return (T) array;
        }
        Object newInstance = clazz.newInstance();
        Field[] declaredFields = clazz.getDeclaredFields();
        for (Field declaredField : declaredFields) {
            if (java.lang.reflect.Modifier.isStatic(declaredField.getModifiers())) {
                continue;
            }
            declaredField.setAccessible(true);
            Object oldFieldValue = declaredField.get(object);
            Object newFieldValue = cloneObject(oldFieldValue);
            declaredField.set(newInstance, newFieldValue);
        }
        return ((T) newInstance);
    }
}
