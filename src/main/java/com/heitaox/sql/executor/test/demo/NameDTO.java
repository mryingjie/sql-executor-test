package com.heitaox.sql.executor.test.demo;

import cn.hutool.json.JSONUtil;
import com.google.common.collect.Lists;
import lombok.Data;

import java.util.ArrayList;
import java.util.Objects;

/**
 * @author Yingjie Zheng
 * @date 2023/5/16 21:32
 * @description
 */
@Data
public class NameDTO {

    private String name;

    private String id;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        NameDTO nameDTO = (NameDTO) o;
        return Objects.equals(id, nameDTO.id);
    }

    public static void main(String[] args) {
        ArrayList<String> strings = Lists.newArrayList("https://12313XDA", "https://821731");
        System.out.println(JSONUtil.toJsonStr(strings));
        System.out.println(strings);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
