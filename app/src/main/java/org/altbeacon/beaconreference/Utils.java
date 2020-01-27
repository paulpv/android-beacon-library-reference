package org.altbeacon.beaconreference;

import androidx.annotation.Nullable;

public class Utils {
    private Utils() {
    }

    public static String TAG(Object o) {
        return TAG((o == null) ? null : o.getClass());
    }

    public static String TAG(Class c) {
        return TAG(getShortClassName(c));
    }

    /**
     * Per http://developer.android.com/reference/android/util/Log.html#isLoggable(java.lang.String, int)
     */
    public static int LOG_TAG_LENGTH_LIMIT = 23;

    /**
     * Limits the tag length to {@link #LOG_TAG_LENGTH_LIMIT}
     *
     * @param tag
     * @return the tag limited to {@link #LOG_TAG_LENGTH_LIMIT}
     */
    public static String TAG(String tag) {
        int length = tag.length();
        if (length > LOG_TAG_LENGTH_LIMIT) {
            // Turn "ReallyLongClassName" to "ReallyLo...lassName";
            int half = LOG_TAG_LENGTH_LIMIT / 2;
            tag = tag.substring(0, half) + '…' + tag.substring(length - half);
        }
        return tag;
    }

    @Nullable
    @SuppressWarnings("WeakerAccess")
    public static String getShortClassName(Object o) {
        return getShortClassName(o == null ? null : o.getClass());
    }

    @Nullable
    public static String getShortClassName(Class c) {
        return c == null ? null : c.getSimpleName();
    }
}
