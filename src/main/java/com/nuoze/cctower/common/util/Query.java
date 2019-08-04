package com.nuoze.cctower.common.util;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * @author JiaShun
 * @date 2019-03-03 15:43
 */
public class Query extends LinkedHashMap<String, Object>  {

    private static final long serialVersionUID = 1L;
    /**
     * 第几页
     */
    private int offset;
    /**
     * 每页条数
     */
    private int limit;

    public Query(Map<String, Object> params) {
        this.putAll(params);
        // 分页参数
        this.offset = Integer.parseInt(params.get("offset").toString());
        this.limit = Integer.parseInt(params.get("limit").toString());
        this.put("offset", offset);
        this.put("page", offset / limit + 1);
        this.put("limit", limit);
    }

    public int getOffset() {
        return offset;
    }

    public void setOffset(int offset) {
        this.put("offset", offset);
    }

    public int getLimit() {
        return limit;
    }

    public void setLimit(int limit) {
        this.limit = limit;
    }
}
