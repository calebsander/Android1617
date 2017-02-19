package org.gearticks.opmodes.utility;

import android.support.annotation.NonNull;
import java.text.MessageFormat;

public class Utils {
    public static final String TAG = "Gearticks"; //for use as tag in Android logging

    /**
     * Throws an AssertionError if the object is null.
     * Always returns a non-null object.
     * Uses a default assertion message.
     * @param object the object being asserted to be non-null
     * @return the object (if it was not null)
     */
    public static @NonNull <T> T assertNotNull(T object){
        return assertNotNull(object, "Object can not be null");
    }

    /**
     * Throws an AssertionError if the object is null.
     * Always returns a non-null object
     * @param object - object to test
     * @param message - message for AssertionError
     * @return the object (if it was not null)
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
     * @param condition the condition being asserted to be true
     * @param messageFormat the format string for the message
     * @param messageParameters the parameters to the format string
     */
    public static void assertTrue(final boolean condition, final String messageFormat, final Object... messageParameters) {
        if (!condition) {
            throw new AssertionError(new MessageFormat(messageFormat).format(messageParameters));
        }
    }
}
