package com.yscall.conversation.bean;

import java.io.Serializable;

/**
 * Created by 你的样子 on 2018/4/10.
 * 搜索内容类型
 * @author gerile
 */
public class ContentInfo implements Serializable{

    private String content;
    private ContentEnum type;

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public ContentEnum getType() {
        return type;
    }

    public void setType(ContentEnum type) {
        this.type = type;
    }

    @Override
    public String toString() {
        return "SearchInfo{" +
                "content='" + content + '\'' +
                ", type=" + type +
                '}';
    }
}
