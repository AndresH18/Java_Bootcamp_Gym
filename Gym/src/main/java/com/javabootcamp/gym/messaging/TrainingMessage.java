package com.javabootcamp.gym.messaging;

import java.util.Arrays;

public record TrainingMessage(
        String trainerUsername,
        String trainerFirstName,
        String trainerLastName,
        boolean active,
        int duration,
        String year,
        String month,
        boolean delete
) {

    public enum Month {
        JANUARY(1, "JAN"),
        FEBRUARY(2, "FEB"),
        MARCH(3, "MAR"),
        APRIL(4, "APR"),
        MAY(5, "MAY"),
        JUNE(6, "JUN"),
        JULY(7, "JUL"),
        AUGUST(8, "AUG"),
        SEPTEMBER(9, "SEP"),
        OCTOBER(10, "OCT"),
        NOVEMBER(11, "NOV"),
        DECEMBER(12, "DEC");

        private final int monthNumber;
        private final String shortName;

        Month(int monthNumber, String shortName) {
            this.monthNumber = monthNumber;
            this.shortName = shortName;
        }

        public static Month of(int i) {
            if (i <= 0)
                throw new IllegalArgumentException("Value must be between 1 and 12");

            var month = Arrays.stream(Month.values()).filter(m -> m.monthNumber == i).findFirst();
            return month.orElseThrow();
        }

        public String getShortName() {
            return shortName;
        }
    }
}
