package my.coupon.advanced.domain.enums;

public enum CouponType {
    FIX("FIX"),
    RATE("RATE");

    private final String value;

    CouponType(String value) {
        this.value = value;
    }
}
