package de.fb.spring.swing.util;

import org.apache.commons.lang3.StringUtils;
import org.joda.time.DateTime;
import org.joda.time.Duration;
import org.joda.time.Period;
import org.joda.time.format.DateTimeFormatter;
import org.joda.time.format.PeriodFormatter;
import org.joda.time.format.PeriodFormatterBuilder;


public final class TimeFormatUtils {

    private static DateTimeFormatter dateTimeFormatter;
    private static PeriodFormatter periodFormatter;

    static {

        PeriodFormatterBuilder builder = new PeriodFormatterBuilder();
        builder.printZeroAlways();
        builder.appendDays();
        builder.appendSuffix(" day", " days");
        builder.appendSeparator(", ");
        builder.appendHours();
        builder.appendSuffix(" hour", " hours");
        builder.appendSeparator(" and ");
        builder.appendMinutes();
        builder.appendSuffix(" minute", " minutes");

        periodFormatter = builder.toFormatter();

        // todo: init dateTimeFormatter
    }

    private TimeFormatUtils() {

    }

    /**
     * 
     * @param period
     * @return
     */
    public static String formatPeriod(final Period period) {
        String result = StringUtils.EMPTY;
        if (period != null) {
            result = periodFormatter.print(period);
        }
        return result;
    }

    /**
     * 
     * @param duration
     * @return
     */
    public static String formatDuration(final Duration duration) {

        /*
         * note: null is passed to toPeriodFrom() in order to increase conversion precision
         * - otherwise the days, months etc. fields of Period will not be populated. See JodaTime javadocs.
         */

        String result = StringUtils.EMPTY;
        if (duration != null) {
            result = formatPeriod(duration.toPeriodFrom(null));
        }
        return result;
    }

    /**
     * 
     * @param dateTime
     * @return
     */
    public static String formatDateTime(final DateTime dateTime) {

        return StringUtils.EMPTY; // stub
    }


}
