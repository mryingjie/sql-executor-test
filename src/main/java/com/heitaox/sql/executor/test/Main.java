package com.heitaox.sql.executor.test;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;

/**
 * @author Yingjie Zheng
 * @date 2023/7/25 19:00
 * @description
 */
public class Main {

    public static void main(String[] args) throws UnsupportedEncodingException {
        String s = "\u6296\u97f3\u4e1a\u52a1\u5ba1\u6838\u72b6\u6001";

        System.out.println(URLDecoder.decode(s,"UTF-8"));
    }

}


