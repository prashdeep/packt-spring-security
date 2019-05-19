package com.classpath.userregistration.service;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.temporal.TemporalUnit;
import java.util.concurrent.TimeUnit;

public class SImpleClass {

    public static void main(String[] args) {
        LocalDateTime ldt1 = LocalDateTime.of(2017, 07, 06, 23, 29, 00);
        LocalDateTime ldt2 = LocalDateTime.of(2017, 07, 06, 23, 30, 00);
        System.out.println(Duration.between(ldt1, ldt2).toMinutes());
    }
}