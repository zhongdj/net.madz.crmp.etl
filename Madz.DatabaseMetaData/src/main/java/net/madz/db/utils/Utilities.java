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
}
