package vn.com.zalopay.demozpdk;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import vn.com.zalopay.demozpdk.Constant.ConstantCode;
import vn.com.zalopay.demozpdk.Scan.ScanActivity;
import vn.com.zalopay.demozpdk.interfaces.IMerchantNotification;
import vn.zalopay.sdk.MerchantReceiver;
import vn.zalopay.sdk.ZaloPayErrorCode;
import vn.zalopay.sdk.ZaloPaySDK;

public class MainActivity extends AppCompatActivity {

    Button mButtonScan;
    private RadioGroup radioGroup;
    private RadioButton payOption, linkOption;
    public static IMerchantNotification sIMerchantNotification;
    private int TESTING_TYPE = 1; // default is link option

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        transparentToolbar();
        setContentView(R.layout.activity_main);

        mButtonScan = findViewById(R.id.btn_scan);

        mButtonScan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, ScanActivity.class);
                intent.putExtra(ConstantCode.KEY_TESTING_TYPE, TESTING_TYPE);
                startActivity(intent);
            }
        });
        sIMerchantNotification = new IMerchantNotification() {
            @Override
            //    @Override
            public void handleSuccess(final String transactionId, final String transToken) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        new AlertDialog.Builder(MainActivity.this)
                                .setTitle("Payment Success")
                                .setMessage(String.format("TransactionId: %s - TransToken: %s", transactionId, transToken))
                                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                    }
                                })
                                .setNegativeButton("Cancel", null).show();
                    }
                });

            }

            @Override
            public void handleError(ZaloPayErrorCode zaloPayErrorCode, int paymentErrorCode, String zpTransToken) {

                new AlertDialog.Builder(MainActivity.this)
                        .setTitle("Payment Fail")
                        .setMessage(String.format("PaymentErrorCode: %s - TransToken: %s", paymentErrorCode, zpTransToken))
                        .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                            }
                        })
                        .setNegativeButton("Cancel", null).show();

            }
        };
        handleViewOptions();
    }

    private void handleViewOptions() {
        radioGroup = (RadioGroup) findViewById(R.id.myRadioGroup);

        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                // find which radio button is selected
                if (checkedId == R.id.payoption) {
                    TESTING_TYPE = 0;
                } else if (checkedId == R.id.linkoption) {
                    TESTING_TYPE = 1;
                }
            }

        });
    }

    private void transparentToolbar() {
        if (Build.VERSION.SDK_INT >= 19 && Build.VERSION.SDK_INT < 21) {
            setWindowFlag(this, WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS, true);
        }
        if (Build.VERSION.SDK_INT >= 19) {
            getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
        }
        if (Build.VERSION.SDK_INT >= 21) {
            setWindowFlag(this, WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS, false);
            getWindow().setStatusBarColor(Color.TRANSPARENT);
        }
    }

    private void setWindowFlag(Activity activity, final int bits, boolean on) {
        Window win = activity.getWindow();
        WindowManager.LayoutParams winParams = win.getAttributes();
        if (on) {
            winParams.flags |= bits;
        } else {
            winParams.flags &= ~bits;
        }
        win.setAttributes(winParams);
    }

    @Override
    protected void onResume() {
        super.onResume();
    }
}
