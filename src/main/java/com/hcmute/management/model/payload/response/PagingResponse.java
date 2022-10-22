package com.hcmute.management.model.payload.response;

import lombok.Getter;
import lombok.Setter;

import java.util.HashMap;
import java.util.Map;

@Getter
@Setter
public class PagingResponse {
    private Boolean success;
    private int status;
    private String message;
    private Map<String,Object> data;
    private boolean last;
    private int totalPages;
    private int totalElements;
    private int size;
    private boolean first;
    private boolean empty;
    public PagingResponse ()
    {
        this.data=new HashMap<>();
    }

    public PagingResponse(Boolean success, int status, String message, Map<String, Object> data, boolean last, int totalPages, int totalElements, int size, boolean first, boolean empty) {
        this.success = success;
        this.status = status;
        this.message = message;
        this.data = data;
        this.last = last;
        this.totalPages = totalPages;
        this.totalElements = totalElements;
        this.size = size;
        this.first = first;
        this.empty = empty;
    }
}
