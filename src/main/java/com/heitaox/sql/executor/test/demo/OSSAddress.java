package com.heitaox.sql.executor.test.demo;

import java.util.Arrays;
import java.util.Comparator;
import java.util.List;

/**
 * @author Yingjie Zheng
 * @date 2023/9/21 11:53
 * @description
 */
public class OSSAddress {



    public static void main(String[] args) {

        String address = "---s---";
        while (address.startsWith("-")) {
            address = address.substring(1);
        }
        while (address.endsWith("-")) {
            address = address.substring(0,address.length() - 1);
        }
        System.out.println(address);
    }
}
