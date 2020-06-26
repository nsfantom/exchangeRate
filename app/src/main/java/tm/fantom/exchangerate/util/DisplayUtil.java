package tm.fantom.exchangerate.util;

import android.content.Context;
import android.content.res.Resources;
import android.util.TypedValue;

public final class DisplayUtil {

    public static int dpToPx(Context context, int dp) {
        Resources resources = context.getResources();
        // Converts dip into its equivalent px
        return Math.round(TypedValue.applyDimension(
                TypedValue.COMPLEX_UNIT_DIP, dp, resources.getDisplayMetrics()));
    }
}