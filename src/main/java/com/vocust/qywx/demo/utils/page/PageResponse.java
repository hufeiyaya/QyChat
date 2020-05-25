package com.vocust.qywx.demo.utils.page;

import java.io.Serializable;
import java.lang.Integer;

public class PageResponse implements Serializable {
    private static final long serialVersionUID = 5859230868818691254L;
    
    //查询到数据总数
    private Integer total;
    
    private boolean success=true;
    
    //分页内容
    private Object res;
    
    private String message;

    public Integer getTotal() {
        return total;
    }

    public void setTotal(Integer total) {
        this.total = total;
    }

    public Object getRes() {
        return res;
    }

    public void setRes(Object res) {
        this.res = res;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }
        
    

}
