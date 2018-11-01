package com.lanou.model.vo;

import java.io.Serializable;
import java.util.List;

/**
 * ClassName : paginationVO
 * PackageName : com.lanou.model.vo
 * Description :
 *
 * @Autor : Administrator
 * @Date : 2018/8/17 21:54
 * @Version : 1.0
 */
public class PaginationVO<T> implements Serializable{
    /**
     * 总记录数
     */
    private Long total;

    /**
     * 数据
     */
    private List<T> dataList;

    public Long getTotal(){
        return total;
    }

    public void setTotal(Long total) {
        this.total = total;
    }

    public List<T> getDataList() {
        return dataList;
    }

    public void setDataList(List<T> dataList) {
        this.dataList = dataList;
    }
}
