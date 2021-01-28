package in.woowa.pilot.admin.presentation;

import org.springframework.restdocs.snippet.Attributes;

import static org.springframework.restdocs.snippet.Attributes.key;

public class DocumentFormat {

    public static Attributes.Attribute getDateTimeFormat() {
        return key("format").value("yyyy-MM-dd'T'HH:mm:ss");
    }

    public static Attributes.Attribute getEmailFormat() {
        return key("format").value("siyoung@woowahan.com");
    }

    public static Attributes.Attribute getMemberRoleFormat() {
        return key("format").value("ADMIN, NORMAL");
    }

    public static Attributes.Attribute getAuthProvider() {
        return key("format").value("LOCAL, GOOGLE");
    }

    public static Attributes.Attribute getOrderStatus() {
        return key("format").value("ORDER, ORDER_CONFIRM, DELIVERY, DELIVERY_CONFIRM, CANCEL");
    }

    public static Attributes.Attribute getSettleType() {
        return key("format").value("DAILY, WEEK, MONTH");
    }

    public static Attributes.Attribute getPaymentType() {
        return key("format").value("CARD, MOBILE, COUPON, POINT, EMPTY");
    }

    public static Attributes.Attribute getPaymentOption() {
        return key("format").value("OWNER_COUPON, BAEMIN_COUPON, EMPTY");
    }

    public static Attributes.Attribute getRewardType() {
        return key("format").value("DELIVERY_ACCIDENT, SYSTEM_ERROR, ABUSING, ETC");
    }

    public static Attributes.Attribute getSettleStatus() {
        return key("format").value("CREATED, COMPLETED");
    }
}
