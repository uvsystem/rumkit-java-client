package com.dbsys.rs.client;

import java.sql.Time;
import java.sql.Date;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.Month;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;
import java.util.Calendar;

/**
 * Utility class to working with java.sql.Date and java.sql.Time.
 * 
 * @author Deddy Christoper Kakunsi
 *
 */
public class DateUtil {

    public static final int EPOCH_YEAR = 1970;
    public static final int EPOCH_MONTH = 1;
    public static final int EPOCH_DAY = 1;

    public static final long DAY_IN_MILIS = 86400000L;
    public static final long HOUR_IN_MILIS = 3600000L;

    public static final String DEFAULT_DELIMETER = "-";


    //Time Utility
    /**
     * Buat object java.sql.Time dari waktu saat ini.
     * @return
     */
    public static Time getTime() {
            LocalTime localTime = getNowLocalTime();

            return toTime(localTime);
    }

    public static int[] createArray(Time time) {
            int[] arrOfTime = new int[3];

            arrOfTime[0] = getHour(time);
            arrOfTime[1] = getMinute(time);
            arrOfTime[2] = getSecond(time);

            return arrOfTime;
    }

    public static Time getTime(int hour, int minute, int second) {
            LocalTime localTime = getLocalTime(hour, minute, second);

            return toTime(localTime);
    }

    /**
     * Ubah java.lang.String menjadi java.util.Time.
     * @param timeString format = hh:mm:ss
     * @return
     */
    public static Time getTime(String timeString) {
            if (timeString == null || timeString.equals(""))
                    return null;

            LocalTime localTime = getLocalTime(timeString);

            return toTime(localTime);
    }

    /**
     * Ubah java.lang.String menjadi java.util.Time.
     * @param timeString format = hh:mm:ss
     * @param delim pemisah antara jam, menit, dan detik
     * @return
     */
    public static Time getTime(String timeString, String delim) {
            if (timeString == null || timeString.equals(""))
                    return null;

            LocalTime localTime = getLocalTime(timeString, delim);

            return toTime(localTime);
    }

    public static LocalTime getLocalTime(ZoneId zoneId) {
            return LocalTime.now(DEFAULT_ZONE_ID);
    }

    /**
     * The string must be "13:10:05".
     * @param timeString
     * @return
     */
    public static LocalTime getLocalTime(String timeString) {
            return getLocalTime(timeString, ":");
    }

    public static LocalTime getLocalTime(String timeString, String delim) {
            if (timeString == null || timeString.equals(""))
                    return null;

            String elStr[] = timeString.split(delim);

            int second = 0;
            if (elStr.length == 3)
                    second = Integer.parseInt(elStr[2]);

            return getLocalTime(Integer.parseInt(elStr[0]), getMonthInt(elStr[1]), second);
    }

    public static LocalTime getLocalTime(int hour, int minute, int second) {
            return LocalTime.of(hour, minute, second);
    }

    public static LocalTime getNowLocalTime() {
            return getLocalTime(DEFAULT_ZONE_ID);
    }

    public static Time toTime(LocalTime localTime) {
            return Time.valueOf(localTime);
    }

    public static int getHour(Time time) {
            LocalTime localTime = time.toLocalTime();

            return localTime.getHour();
    }

    public static int getMinute(Time time) {
            LocalTime localTime = time.toLocalTime();

            return localTime.getMinute();
    }

    public static int getSecond(Time time) {
            LocalTime localTime = time.toLocalTime();

            return localTime.getSecond();
    }

    //Date Utility
    /**
     * Buat object java.sql.Date dari hari ini.
     * 
     * @return tanggal
     */
    public static Date getDate() {
            LocalDate localDate = getNowLocalDate();

            return toDate(localDate);
    }

    public static int[] createArray(Date date) {
            int[] arrOfDate = new int[3];

            arrOfDate[0] = getDay(date);
            arrOfDate[1] = getMonthInt(date);
            arrOfDate[2] = getYear(date);

            return arrOfDate;
    }

    /**
     * Ubah java.lang.String menjadi java.sql.Date.<br />
     * Menggunakan "-" sebagai delimeter.
     * 
     * @param dateString format = yyyy/MM/dd
     * 
     * @return tanggal
     */
    public static Date getDate(String dateString) {
            if (dateString == null || dateString.equals(""))
                    return null;

            LocalDate localDate = getLocalDate(dateString);

            return toDate(localDate);
    }
    
    public static Date getDate(Calendar calendar) {
        int day = calendar.get(Calendar.DATE);
        int month = calendar.get(Calendar.MONTH);
        int year = calendar.get(Calendar.YEAR);
        
        return getDate(year, month + 1, day);
    }

    /**
     * Ubah java.lang.String menjadi java.sql.Date.
     * 
     * @param dateString format = yyyy/MM/dd
     * @param delim pemisah antar unit bulan, tanggal, dan tahun.
     * 
     * @return tanggal
     */
    public static Date getDate(String dateString, String delim) {
            if (dateString == null || dateString.equals(""))
                    return null;

            LocalDate localDate = getLocalDate(dateString, delim);

            return toDate(localDate);
    }

    public static Date getDate(int year, int month, int day) {
            LocalDate localDate = getLocalDate(day, month, year);

            return toDate(localDate);
    }

    public static Date getDate(int year, Month month, int day) {
            LocalDate localDate = getLocalDate(day, month, year);

            return toDate(localDate);
    }

    /**
     * Membuat tanggal dari hari ini ditambah {@code i}.
     * 
     * @param i jumlah yang akan ditambahkan
     * 
     * @return tanggal
     */
    public static Date getDate(int i) {
            Date today = getDate();

            return add(today, i);
    }

    public static Date getFirstDate() {
            LocalDate localDate = LocalDate.now();
            Date date = Date.valueOf(localDate);

            int year = getYear(date);
            Month month = getMonth(date);

            return getFirstDate(month, year);
    }

    public static Date getFirstDate(Month month, int year) {
            LocalDate firstDate = LocalDate.of(year, month, 1);

            return toDate(firstDate);
    }

    public static Date getFirstDate(int year) {
            LocalDate firstDate = LocalDate.of(year, Month.JANUARY, 1);

            return toDate(firstDate);
    }

    public static Date getLastDate() {
            LocalDate localDate = LocalDate.now();
            Date date = Date.valueOf(localDate);

            int year = getYear(date);
            Month month = getMonth(date);

            return getLastDate(month, year);
    }

    public static Date getLastDate(Month month, int year) {
            LocalDate firstDate = LocalDate.of(year, month, getLastDay(month, year));

            return toDate(firstDate);
    }

    public static Date getLastDate(int year) {
            LocalDate firstDate = LocalDate.of(year, Month.DECEMBER, 31);

            return toDate(firstDate);
    }

    public static int getLastDay(Month month, int year) {
            int lastDate = month.maxLength();

            if ((month.equals(Month.FEBRUARY)) && (year % 4 != 0))
                    lastDate = 28;

            return lastDate;
    }

    /**
     * Return the last day of month and year.
     * @param month {@code int}
     * @param year {@code int}
     * @return the last day
     */
    public static int getLastDay(int month, int year) {
            return getLastDay(Month.of(month), year);
    }

    /**
     * Return month in {@code int} representation from {@code String}.
     * @param month
     * @return month in {@code int} representation
     */
    public static int getMonthInt(String month) {
            return Integer.parseInt(month);
    }

    public static Month getMonth(Date date) {
            LocalDate localDate = date.toLocalDate();

            return localDate.getMonth();
    }

    public static int getMonthInt(Date date) {
            Month month = getMonth(date);

            return month.getValue();
    }

    public static int getYear(Date date) {
            LocalDate localDate = date.toLocalDate();

            return localDate.getYear();
    }

    public static int getYear() {
            return getYear(getDate());
    }

    public static int getDay(Date date) {
            LocalDate localDate = date.toLocalDate();

            return localDate.getDayOfMonth();
    }

    /**
     * Check whether two dates equals or not. Comparison between year, month, and day only.
     * @param date1
     * @param date2
     * @return true if year, month, and day are equals. Otherwise, false.
     */
    public static boolean equals(Date date1, Date date2) {
            if (getYear(date1) != getYear(date2))
                    return false;
            if (getMonthInt(date1) != getMonthInt(date2))
                    return false;
            return getDay(date1) == getDay(date2);
    }

    public static Date add(Date awal, int i) {
            LocalDate localDate = awal.toLocalDate();

            LocalDate newDate = localDate.plusDays(i);

            return toDate(newDate);
    }

    public static Date substract(Date awal, int i) {
            LocalDate localDate = awal.toLocalDate();

            LocalDate newDate = localDate.minusDays(i);

            return toDate(newDate);
    }

    // LocalDate
    private static final ZoneId DEFAULT_ZONE_ID = ZoneId.of("Asia/Singapore");

    public static LocalDate getLocalDate(ZoneId zoneId) {
            return LocalDate.now(zoneId);
    }

    /**
     * Format yyyy/MM/dd
     * @param dateString
     * @return
     */
    public static LocalDate getLocalDate(String dateString) {
            return getLocalDate(dateString, DEFAULT_DELIMETER);
    }

    /**
     * Format yyyy/MM/dd
     * @param dateString
     * @param delim
     * @return
     */
    public static LocalDate getLocalDate(String dateString, String delim) {
            if (dateString == null || dateString.equals(""))
                    return null;

            // year-month-date
            String elStr[] = dateString.split(delim);

            return getLocalDate(Integer.parseInt(elStr[2]), getMonthInt(elStr[1]), Integer.parseInt(elStr[0]));
    }

    public static LocalDate getLocalDate(int day, int month, int year) {
            int lastDay = getLastDay(month, year);
            int antara = day - lastDay;

            if (antara > 0) {
                    month++;
                    day = antara;
            }

            return LocalDate.of(year, month, day);
    }

    public static LocalDate getLocalDate(int day, Month month, int year) {
            int lastDay = getLastDay(month, year);
            int antara = day - lastDay;

            if (antara > 0) {
                    int monthValue = month.getValue();
                    month = Month.of(++monthValue);
                    day = antara;
            }

            return LocalDate.of(year, month, day);
    }

    public static LocalDate getNowLocalDate() {
            return getLocalDate(DEFAULT_ZONE_ID);
    }

    public static Instant getInstant(LocalDate localDate) {
            return localDate.atStartOfDay().atZone(DEFAULT_ZONE_ID).toInstant();
    }

    public static Date toDate(LocalDate localDate) {
            return Date.valueOf(localDate);
    }

    public static boolean isFormatted(String dateString) {
            // bulan-tanggal-tahun
            String arrStr[] = dateString.split(DEFAULT_DELIMETER);

            return arrStr[0].length() >= 4;
    }

    /**
     * Change date format from MM/dd/YYYY to yyyy/MM/dd
     * @param dateString
     * @return
     */
    public static String formatDateString(String dateString) {
            // bulan-tanggal-tahun
            String arrStr[] = dateString.split(DEFAULT_DELIMETER);

            if (arrStr[1].length() == 1)
                    arrStr[1] = String.format("0%s", arrStr[1]);

            if (arrStr[0].length() == 1)
                    arrStr[0] = String.format("0%s", arrStr[0]);

            // tahun-bulan-tanggal
            return String.format("%s-%s-%s", arrStr[2], arrStr[0], arrStr[1]);
    }

    /**
     * Format: dd/MM/yyyy.
     * @param date
     * @param delim
     * @return
     */
    public static String toFormattedStringDate(Date date, String delim) {
            int[] arrOfDate = createArray(date);

            return String.format("%d%s%d%s%d", arrOfDate[0], delim, arrOfDate[1], delim, arrOfDate[2]);
    }

    /**
     * Convert java.sql.Date menjadi java.lang.String.
     * @param date
     * @param delim
     * @return String date with format: mm/DD/yyyy
     */
    public static String toStringDate(Date date, String delim) {
            int[] arrOfDate = createArray(date);

            return String.format("%d%s%d%s%d", arrOfDate[1], delim, arrOfDate[0], delim, arrOfDate[2]);
    }

    public static String toFormattedStringTime(Time time, String delim) {
            int[] arrOfDate = createArray(time);
            String[] arr = new String[3];

            int index = -1;
            for (int i : arrOfDate) {

                    index++;
                    arr[index] = String.valueOf(i);
                    if ( String.valueOf(i).length() < 2)
                            arr[index] = String.format("0%d", i);

            }

            return String.format("%s%s%s%s%s", arr[0], delim, arr[1], delim, arr[2]);
    }

    /**
     * Code the date.
     * @param date
     * @return
     */
    public static String codedDate(Date date) {
            return String.format("%d%d%d", getYear(date), getMonthInt(date), getDay(date));
    }

    /**
     * Code the time.
     * @param time
     * @return
     */
    public static String codedTime(Time time) {
            return String.format("%d%d%d", getHour(time), getMinute(time), getSecond(time));
    }

    /**
     * Code it.
     * @param date
     * @return
     */
    public static String codedString(Date date) {
            Time time = new Time(date.getTime());

            return String.format("%s%s", codedDate(date), codedTime(time));
    }

    public static long toMilis(int year, int month, int day) {
            LocalDate epoch = LocalDate.of(EPOCH_YEAR, EPOCH_MONTH, EPOCH_DAY);

            int lastDay = getLastDay(month, year);

            if (day > lastDay) {
                    month += 1;
                    day -= lastDay;
            }

            LocalDate created = LocalDate.of(year, month, day);

            long p = ChronoUnit.DAYS.between(epoch, created);

            return p * DAY_IN_MILIS;
    }

    public static int calculate(Date awal, Date akhir) {
            Long millis1 = awal.getTime();
            Long millis2 = akhir.getTime();
            Long hasil = (millis2 - millis1) / DAY_IN_MILIS;

            return hasil.intValue();
    }

    public static int calculate(Time awal, Time akhir) {
            Long millis3 = (akhir.getTime() - awal.getTime());
            Long hasil = millis3 / HOUR_IN_MILIS;

            if (millis3 % HOUR_IN_MILIS > 0)
                    hasil++;

            return hasil.intValue();
    }

    public static boolean between(Date tanggalDaftar, Date awal, Date akhir) {
        long _tanggalDaftar = getCount(tanggalDaftar);
        long _awal = getCount(awal);
        long _akhir = getCount(akhir);
        
        return (_tanggalDaftar >= _awal) && (_tanggalDaftar <= _akhir); 
    }
    
    public static int getCount(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        
        int day = calendar.get(Calendar.DATE);
        int month = calendar.get(Calendar.MONTH);
        int year = calendar.get(Calendar.YEAR);

        return (year * 365) + (month * 30) + day;
    }
}
