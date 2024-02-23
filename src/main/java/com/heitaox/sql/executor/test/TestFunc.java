package com.heitaox.sql.executor.test;


import java.io.UnsupportedEncodingException;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

/**
 * created by Yingjie Zheng at 2019-08-30 09:31
 */
public class TestFunc {


    public static void main(String[] args) throws UnsupportedEncodingException {

        System.out.println(LocalTime.now().format(DateTimeFormatter.ofPattern( "HH:mm:ss")));

    }
}
