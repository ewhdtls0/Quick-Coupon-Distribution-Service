package my.coupon.advanced.domain.enums;

import lombok.Getter;

import java.util.Random;

@Getter
public enum CouponType {
    FIX("FIX"),
    RATE("RATE");

    private final String value;

    CouponType(String value) {
        this.value = value;
    }

    private static final Random RANDOM = new Random();

    public static CouponType getRandomType() {
        return values()[RANDOM.nextInt(values().length)];
    }
}
