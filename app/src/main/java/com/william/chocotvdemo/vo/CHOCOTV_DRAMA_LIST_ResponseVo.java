package com.william.chocotvdemo.vo;

import java.io.Serializable;
import java.util.List;

/**
 * Created by William on 2018/3/10.
 */
public class CHOCOTV_DRAMA_LIST_ResponseVo implements Serializable {
    private static final long serialVersionUID = 1L;

    private List<CHOCOTV_DRAMA_LIST_drama_list_item_EntityVo> data;

    public List<CHOCOTV_DRAMA_LIST_drama_list_item_EntityVo> getData() {
        return data;
    }

    public void setData(List<CHOCOTV_DRAMA_LIST_drama_list_item_EntityVo> data) {
        this.data = data;
    }
}
