/*
 * Copyright 2014-2024 setNone. All rights reserved. 
 */
package com.king.chat.socket.bean;

import java.util.List;
import java.util.Map;

/**
 * ExpressionList.java - 表情集合，
 *
 * @author jiengyh
 *
 * 2014年6月25日 上午9:26:46
 */
public class ExpressionList {
    private String name;
    private List<Expression> expList;
    private Map<String,Expression> expMap;
    /**
     * @return the name
     */
    public String getName() {
        return name;
    }
    /**
     * @param name the name to set
     */
    public void setName(String name) {
        this.name = name;
    }
    /**
     * @return the expList
     */
    public List<Expression> getExpList() {
        return expList;
    }
    /**
     * @param expList the expList to set
     */
    public void setExpList(List<Expression> expList) {
        this.expList = expList;
    }

    public Map<String, Expression> getExpMap() {
        return expMap;
    }

    public void setExpMap(Map<String, Expression> expMap) {
        this.expMap = expMap;
    }
}
