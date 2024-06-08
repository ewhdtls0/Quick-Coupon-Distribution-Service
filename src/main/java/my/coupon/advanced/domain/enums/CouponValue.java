package my.coupon.advanced.domain.enums;

import lombok.Getter;

import java.util.Random;

@Getter
public enum CouponValue {
    T_3PERCENT("3%"),
    T_5PERCENT("5%"),
    T_7PERCENT("7%"),
    T_10PERCENT("10%"),
    T_1000("1000"),
    T_2000("2000"),
    T_3000("3000"),
    T_5000("5000");

    private final String value;

    CouponValue(String value) {
        this.value = value;
    }

    private static final Random RANDOM = new Random();

    public static CouponValue getRandomValueForType(CouponType type) {
        CouponValue[] values = switch (type) {
            case FIX -> new CouponValue[]{T_1000, T_2000, T_3000, T_5000};
            case RATE -> new CouponValue[]{T_3PERCENT, T_5PERCENT, T_7PERCENT, T_10PERCENT};
        };
        return values[RANDOM.nextInt(values.length)];
    }
}
