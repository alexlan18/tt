package com.wing.system.menus.model;

import com.wing.framework.common.Message;

import java.lang.String;

/**
 *
 * 菜单信息
 *
 * @author: panwb
 *
 * Date: 14-3-17
 * Time: 上午11:08
 */
public class MenuMessage extends Message {

    private SMenus sMenus;

    public MenuMessage(String code, String message, SMenus sMenus) {
        super(code, message);
        this.sMenus = sMenus;
    }

    public SMenus getsMenus() {
        return sMenus;
    }

    public void setsMenus(SMenus sMenus) {
        this.sMenus = sMenus;
    }
}
