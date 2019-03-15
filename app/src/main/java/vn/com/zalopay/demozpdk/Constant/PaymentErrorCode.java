package vn.com.zalopay.demozpdk.Constant;

/**
 * Created by vinhha on 03/05/2018.
 */
public enum PaymentErrorCode {
    USER_CANCEL(4);

    private final int value;

    private PaymentErrorCode(int value) {
        this.value = value;
    }

    public int getValue() {
        return this.value;
    }
}
