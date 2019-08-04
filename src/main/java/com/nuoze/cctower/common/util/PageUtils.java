package com.nuoze.cctower.common.util;

import java.util.List;

/**
 * @author JiaShun
 * @date 2019-03-03 15:42
 */
public class PageUtils {

    private static final long serialVersionUID = 1L;
    private int total;
    private List<?> rows;

    public PageUtils(List<?> list, int total) {
        this.rows = list;
        this.total = total;
    }

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }

    public List<?> getRows() {
        return rows;
    }

    public void setRows(List<?> rows) {
        this.rows = rows;
    }
}
