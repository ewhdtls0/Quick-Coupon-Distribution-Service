package my.coupon.advanced.domain.enums;

import lombok.Getter;

@Getter
public enum CouponType {
    FIX("FIX"),
    RATE("RATE");

    private final String value;

    CouponType(String value) {
        this.value = value;
    }
}
