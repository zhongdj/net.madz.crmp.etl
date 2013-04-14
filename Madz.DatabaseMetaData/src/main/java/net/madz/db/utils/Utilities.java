package net.madz.db.utils;

import java.lang.reflect.Method;
import java.util.LinkedList;
import java.util.List;

import net.madz.db.core.impl.validation.mysql.ErrorCodeTypes;
import net.madz.db.core.impl.validation.mysql.ErrorEntry;
import net.madz.db.core.meta.DottedPath;
import net.madz.db.core.meta.immutable.MetaData;

public class Utilities {

    public static void validateLength(long length) {
        if ( 0 >= length ) {
            throw new IllegalArgumentException(MessageConsts.LENGTH_MUST_BE_GREATER_THAN_ZERO);
        }
    }

    public static String handleSpecialCharacters(String value) {
        if ( value.contains("'") ) {
            return value.replace("'", "\\'");
        }
        return value;
    }

    public static void validateInputValueNotNull(Object value) {
        if ( null == value ) {
            throw new IllegalArgumentException(MessageConsts.ARGUMENT_SHOULD_NOT_BE_NULL);
        }
    }

    public static String upperCaseFirstCharacter(String name) {
        if ( null == name || name.length() <= 0 ) return null;
        return name.substring(0, 1).toUpperCase() + name.substring(1);
    }

    public static List<ErrorEntry> verifyFields(Class instance, DottedPath path, Object one, Object other, List<ErrorEntry> errorSet) {
        final LinkedList<Method> allDeclaredMethods = new LinkedList<Method>();
        getAllDeclaredMethods(instance, allDeclaredMethods);
        for ( Method method : allDeclaredMethods ) {
            try {
                if ( method.getName().startsWith("getSchema") ) {
                    continue;
                }
                if ( method.getName().startsWith("get") || method.getName().startsWith("is") ) {
                    final Class<?> returnType = method.getReturnType();
                    final Class<?>[] parameterTypes = method.getParameterTypes();
                    if ( MetaData.class.isAssignableFrom(method.getReturnType()) || method.getReturnType().isInterface()
                            || method.getReturnType().equals(Class.class) || parameterTypes.length > 0 ) {
                        continue;
                    }
                    Object first = method.invoke(one, null);
                    Object second = method.invoke(other, null);
                    if ( null == first ) {
                        if ( null != second ) {
                            errorSet.add(new ErrorEntry(path, method.getName(), ErrorCodeTypes.FIELD_NOT_MATCHED, first + "", second + ""));
                        } else {
                            continue;
                        }
                    }
                    if ( !first.equals(second) ) {
                        errorSet.add(new ErrorEntry(path, method.getName(), ErrorCodeTypes.FIELD_NOT_MATCHED, first + "", second + ""));
                    }
                }
            } catch (Exception e) {
                throw new IllegalStateException(e);
            }
        }
        return errorSet;
    }

    private static void getAllDeclaredMethods(Class instance, LinkedList<Method> allDeclaredMethods) {
        Method[] declaredMethods = instance.getDeclaredMethods();
        for ( Method method : declaredMethods ) {
            allDeclaredMethods.add(method);
        }
        Class superclass = instance.getSuperclass();
        if ( null == superclass ) {
            return;
        } else {
            getAllDeclaredMethods(superclass, allDeclaredMethods);
        }
    }
}
