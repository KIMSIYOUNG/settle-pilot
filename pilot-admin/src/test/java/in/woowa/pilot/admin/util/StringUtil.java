package in.woowa.pilot.admin.util;

import java.util.UUID;

public class StringUtil {

    public static String createRandomEmail() {
        return UUID.randomUUID() + "@woowahan.com";
    }

}
