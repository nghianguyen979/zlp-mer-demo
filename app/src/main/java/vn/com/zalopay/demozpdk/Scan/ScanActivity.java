package vn.com.zalopay.demozpdk.Scan;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.util.SparseArray;
import android.widget.Toast;

import com.google.android.gms.vision.barcode.Barcode;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;
import java.util.Locale;
import java.util.Set;

import info.androidhive.barcode.BarcodeReader;
import vn.com.zalopay.demozpdk.Constant.ConstantCode;
import vn.com.zalopay.demozpdk.R;
import vn.com.zalopay.demozpdk.ZPDKListener.MerchantListener;
import vn.com.zalopay.demozpdk.interfaces.IMerchantNotification;
import vn.zalopay.sdk.MerchantReceiver;
import vn.zalopay.sdk.ZaloPayErrorCode;
import vn.zalopay.sdk.ZaloPaySDK;

/**
 * Created by vinhha on 12/05/2018.
 */
public class ScanActivity extends AppCompatActivity implements BarcodeReader.BarcodeReaderListener {

    BarcodeReader barcodeReader;
//    MerchantReceiver receiver;
//    IntentFilter intentFilter;
    int flow = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scan);

        //default is link flow
        flow = getIntent().getIntExtra(ConstantCode.KEY_TESTING_TYPE, 1);

        // get the barcode reader instance
        barcodeReader = (BarcodeReader) getSupportFragmentManager().findFragmentById(R.id.barcode_scanner);
//        receiver = new MerchantReceiver();
//        intentFilter = new IntentFilter("vn.zalopay.sdk.ZP_ACTION");

    }

    @Override
    protected void onResume() {
        super.onResume();
//        registerReceiver(receiver, intentFilter);
        Toast.makeText(getApplicationContext(), "onResume - ScanActivity", Toast.LENGTH_SHORT).show();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
//        unregisterReceiver(receiver);
    }

    @Override
    public void onScanned(Barcode barcode) {
        // playing barcode reader beep sound
        barcodeReader.playBeep();

        try {
            if(flow == 0) {
                JSONObject jObj = new JSONObject(barcode.displayValue);
                String zptranstoken = jObj.getString("zptranstoken");

                ZaloPaySDK.getInstance().payOrder(this, zptranstoken, new MerchantListener(this));
            }else{
                linkWithZaloPay(barcode.displayValue);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void linkWithZaloPay(String url) {
        //https://sandboxqc.zalopay.com.vn/v001/zte/wallet/getrequestinfo?requestid=12581_190418000000508
        Uri uri = Uri.parse(url);
        String requestid = uri.getQueryParameter("requestid");
        Intent intent = new Intent("android.intent.action.VIEW");
        intent.setData(Uri.parse(String.format(Locale.getDefault(), "zalopay://merchant/link?source=app&requestid=%1$s&appid=%2$d", requestid, 3)));
        startActivityForResult(intent, 1020);
    }

    @Override
    protected void onPostResume() {
        super.onPostResume();
    }

    @Override
    public void onScannedMultiple(List<Barcode> barcodes) {

    }

    @Override
    public void onBitmapScanned(SparseArray<Barcode> sparseArray) {
    }

    @Override
    public void onScanError(String errorMessage) {
        Toast.makeText(getApplicationContext(), "Error occurred while scanning " + errorMessage, Toast.LENGTH_SHORT).show();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        ZaloPaySDK.getInstance().onActivityResult(requestCode, resultCode, data);
    }
}
