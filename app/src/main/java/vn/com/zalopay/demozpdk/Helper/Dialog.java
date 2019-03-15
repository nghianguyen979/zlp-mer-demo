package vn.com.zalopay.demozpdk.Helper;

import android.app.AlertDialog;
import android.content.Context;
import android.text.Spanned;

/**
 * Created by vinhha on 12/05/2018.
 */
public class Dialog {
    public static void showNaturalDialog(Context context, String title, Spanned message, String textButton) {
        new AlertDialog.Builder(context)
                .setTitle(title)
                .setMessage(message)
                .setNegativeButton(textButton, null).show();
    }
}
