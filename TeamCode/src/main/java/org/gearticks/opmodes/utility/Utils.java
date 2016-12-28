package org.gearticks.opmodes.utility;

public class Utils {
    public static void assertThat(boolean condition, String message) {
        if (!condition) throw new AssertionError(message);
    }
    public static void assertThat(boolean condition) {
        assertThat(condition, "Assertion failed");
    }

    public static <T> T assertNotNull(T object){
        assertThat(object != null, "Object cannot be null");
        return object;
    }
}
