package com.liuchenxi.foundation.http.base;

import java.io.Serializable;
import java.util.ArrayList;

public interface Displayable extends Serializable {
    /**
     * 生成日志详情的显示元素
     * @return
     */
    ArrayList<String> buildDisplayableElements();
}
