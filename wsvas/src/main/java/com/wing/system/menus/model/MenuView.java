package com.wing.system.menus.model;

import java.util.List;

/**
 * @author: panwb
 *
 * Date: 14-3-14
 * Time: 下午2:54
 */
public class MenuView {

    private String title;

    private List<SMenus> content;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public List<SMenus> getContent() {
        return content;
    }

    public void setContent(List<SMenus> content) {
        this.content = content;
    }
}
