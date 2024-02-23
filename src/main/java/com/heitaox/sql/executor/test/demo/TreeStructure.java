package com.heitaox.sql.executor.test.demo;

import lombok.Data;

import java.util.List;

/**
 * @author Yingjie Zheng
 * @date 2023/5/8 10:57
 * @description
 */
@Data
public class TreeStructure {
    
    private String type;

    private String desc;

    private List<TreeStructure> next;

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public List<TreeStructure> getNext() {
        return next;
    }

    public void setNext(List<TreeStructure> next) {
        this.next = next;
    }
}