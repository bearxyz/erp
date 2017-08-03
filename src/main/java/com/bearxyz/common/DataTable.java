package com.bearxyz.common;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by bearxyz on 2017/6/21.
 */
public class DataTable<T> implements Serializable {

    private Integer draw = 0;

    private Long recordsTotal = (long)0;

    private Long recordsFiltered = (long)0;

    private List<T> data = new ArrayList<>();

    public Integer getDraw() {
        return draw;
    }

    public void setDraw(Integer draw) {
        this.draw = draw;
    }

    public List<T> getData() {
        return data;
    }

    public void setData(List<T> data) {
        this.data = data;
    }

    public Long getRecordsTotal() {
        return recordsTotal;
    }

    public void setRecordsTotal(Long recordsTotal) {
        this.recordsTotal = recordsTotal;
    }

    public Long getRecordsFiltered() {
        return recordsFiltered;
    }

    public void setRecordsFiltered(Long recordsFiltered) {
        this.recordsFiltered = recordsFiltered;
    }
}
