package com.wazzup.service;


import io.netty.util.internal.StringUtil;
import org.springframework.beans.factory.annotation.Value;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Base64;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ServiceFunctions<T> {
    private static final String EMAIL_REGEX = "^(.+)@(.+)$";
    private static final String PHONE_NUMBER_REGEX = "(0|91)?[7-9][0-9]{9}";

    private static final SimpleDateFormat DATE_TIME_FORMAT = new SimpleDateFormat("yyyy-MM-dd HH:mm");

    private static final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd");

    private static final DateTimeFormatter LOCAL_DATE_FORMATTER = DateTimeFormatter.ofPattern("dd/MM/yyyy");

    @Value("${confirmation.env.address}")
    private String serviceAddress;
    private static String REGISTRATION_MAIL = "<html>" +
            "<head>" +
            "<meta charset='UTF-8'>" +
            "</head>" +
            "<body> " +
            "<h2> Hej %s!</h2>" +
            "<h2> Dziękujemy za rejestrację, proszę kliknąć w link aby aktywować konto </h2>" +
            "<a href='http://127.0.0.1:8081/user/api/user/confirm?token=%s'>Aktywuj konto</a>" +
            "<hr>"+
            "Z pozdrowieniami, <br> " +
            "Zespół Whatssup!<br>" +
            "Kontakt: eventmailingv1@gmail.com" +
            "</body>" +
            "</html>";

    public static boolean validEmail(String email) {
        if (isNull(email))
            return false;
        Pattern pattern = Pattern.compile(EMAIL_REGEX, Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(email);

        return matcher.find();
    }

    public static boolean validPhoneNumber(String phoneNumber) {
        if (isNull(phoneNumber))
            return false;
        Pattern pattern = Pattern.compile(PHONE_NUMBER_REGEX, Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(phoneNumber);

        return matcher.find();
    }

    public static boolean isNull(String text) {
        if (StringUtil.isNullOrEmpty(text))
            return true;
        return text.isBlank() || text.isEmpty();
    }

    public static <T> boolean isNull(T obj) {
        return obj == null;
    }

    public static Timestamp parseTimestamp(String timestamp) {
        try {
            return new Timestamp(DATE_TIME_FORMAT.parse(timestamp).getTime());
        } catch (ParseException ex) {
            throw new IllegalArgumentException("Timestamp exception: " + ex.getMessage());
        }
    }

    public static String[] parseToArray(String value) {
        return value.split("-");
    }

    public static boolean dateBefore(Timestamp startDate) {
        Timestamp now = new Timestamp(System.currentTimeMillis());

        return startDate.before(now);
    }

    public static Date dateTimeToDate(LocalDateTime dateToConvert) {
        return Date
                .from(dateToConvert.atZone(ZoneId.systemDefault())
                        .toInstant());
    }

    public static Date stringToDate(String date) {
        String correctDate = null;
        if (date.contains("")) {
            correctDate = date.replaceAll("\\.", "-");
        } else {
            correctDate = date;
        }

        try {
            return DATE_FORMAT.parse(correctDate);
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }
    }

    public static LocalDate stringToLocalDate(String date) {
        return  LocalDate.parse(date, LOCAL_DATE_FORMATTER);
    }

    public static String buildCreateAccountMailMessage(String name, String confirmationToken) {
        String test = String.format(REGISTRATION_MAIL, name, confirmationToken);
        return String.format(REGISTRATION_MAIL, name, confirmationToken);
    }


}

