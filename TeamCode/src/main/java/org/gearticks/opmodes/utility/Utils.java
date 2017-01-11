package org.gearticks.opmodes.utility;

import android.support.annotation.NonNull;
import java.text.MessageFormat;

public class Utils {
    public static final String TAG = "Gearticks"; //for use as tag in Android logging

    /**
     * Throws an AssertionError if the object is null.
     * Always returns a non-null object.
     * Uses a default assertion message.
     * @param object
     * @param <T>
     * @return
     */
    public static @NonNull <T> T assertNotNull(T object){
        return assertNotNull(object, "Object can not be null");
    }

    /**
     * Throws an AssertionError if the object is null.
     * Always returns a non-null object
     * @param object - object to test
     * @param message - message for AssertionError
     * @param <T>
     * @return
     */
    public static @NonNull <T> T assertNotNull(final T object, final String message) {
        if (object == null) {
            throw new AssertionError(message);
        }
        return object;
    }

    /**
     * Throws an AssertionError if the condition is false.
     * @param condition - boolean condition to test
     * @param message - message for AssertionError
     */
    public static void assertTrue(final boolean condition, final String message) {
        if (!condition) {
            throw new AssertionError(message);
        }
    }

    /**
     * Only applies the format to the message if the condition is false.
     * More efficient since it avoid String concatenation when condition is true.
     * @param condition
     * @param messageFormat
     * @param messageParameters
     */
    public static void assertTrue(final boolean condition, final String messageFormat, final Object... messageParameters) {
        if (!condition) {
            throw new AssertionError(new MessageFormat(messageFormat).format(messageParameters));
        }
    }
}
