package com.park.monitoring.util;

public class ConvertUtil {

    public static Integer convertToInteger(String value) {
        if (value == null || value.isEmpty()) {
            return null;
        }
        try {
            return Integer.parseInt(value);
        } catch (NumberFormatException e) {
            throw new NumberFormatException(value + "는 유효한 정수가 아닙니다.");
        }
    }
}
