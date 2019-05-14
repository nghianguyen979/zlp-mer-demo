package vn.com.zalopay.demozpdk.ZPDKListener;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import vn.com.zalopay.demozpdk.Constant.ConstantCode;
import vn.com.zalopay.demozpdk.MainActivity;
import vn.zalopay.listener.ZaloPayListener;
import vn.zalopay.sdk.ZaloPayErrorCode;
import vn.zalopay.sdk.ZaloPaySDK;


public class MerchantListener implements ZaloPayListener {
    private Context mContext;

    public MerchantListener(Context mContext) {
        this.mContext = mContext;
    }

    @Override
    public void onPaymentSucceeded(String transactionId, String transToken) {
        Log.d(ConstantCode.TAG, "onSuccess: On successful with transactionId: " + transactionId + "- transtoken: " + transToken);
        MainActivity.sIMerchantNotification.handleSuccess(transactionId, transToken);
    }

    @Override
    public void onPaymentError(ZaloPayErrorCode zaloPayErrorCode, int paymentErrorCode, String zpTransToken) {
        if (zaloPayErrorCode == ZaloPayErrorCode.ZALO_PAY_NOT_INSTALLED) {
            final Handler handler = new Handler(Looper.getMainLooper());
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    new AlertDialog.Builder(mContext)
                            .setTitle("Error Payment")
                            .setMessage("ZaloPay App not install on this Device.")
                            .setPositiveButton("Open Market", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    ZaloPaySDK.getInstance().navigateToStore(mContext);
                                }
                            })
                            .setNegativeButton("Back", null).show();


                }
            }, 500);

            Log.d(ConstantCode.TAG, "onError: <br> <b> <i> ZaloPay App not install on this Device. </i> </b>");
        } else {
            MainActivity.sIMerchantNotification.handleError(zaloPayErrorCode, paymentErrorCode, zpTransToken);
            Log.d(ConstantCode.TAG, "onError: On onPaymentError with paymentErrorCode: " + paymentErrorCode + "- zpTransToken: " + zpTransToken);
        }
    }
}