package org.gearticks.opmodes.utility;

/**
 * Created by irene on 12/27/2016.
 */

public class Utils {

    public static <T> T assertNotNull(T object){
        if (object == null){
            throw new AssertionError("Object can not be null");
        }
        return object;
    }
}
