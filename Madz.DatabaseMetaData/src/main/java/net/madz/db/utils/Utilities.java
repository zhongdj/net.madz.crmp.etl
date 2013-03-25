package net.madz.db.utils;


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
}
