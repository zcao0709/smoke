package com.zhongyun.smoke.common;

import java.util.List;

/**
 * Created by caozhennan on 2018/1/5.
 */
public class Page<T> {
    List<T> content;
    long total;

    public Page(List<T> content, long total) {
        this.content = content;
        this.total = total;
    }

    public List<T> getContent() {
        return content;
    }

    public long getTotal() {
        return total;
    }
}
