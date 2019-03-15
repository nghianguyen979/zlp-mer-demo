package vn.com.zalopay.demozpdk.interfaces;

import vn.zalopay.sdk.ZaloPayErrorCode;

public interface IMerchantNotification {

    void handleSuccess(String transactionId, String transToken);
    void handleError(ZaloPayErrorCode zaloPayErrorCode, int paymentErrorCode, String zpTransToken);
}
