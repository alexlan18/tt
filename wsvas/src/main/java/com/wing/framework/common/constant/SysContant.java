package com.wing.framework.common.constant;

/**
 * 
 * 系统常量
 * 
 * @author panwb
 *
 */
public class SysContant {

	/* 分页：默认页码大小 */
	public static Integer DEFAULT_PAGE_SIZE = 10;

	/* 分页：默认起始页 */
	public static Integer DEFAULT_CURRENT_SIZE = 1;

	/*分页：参数分隔符*/
	public static String PARAMS_SPLIT_TOKEN = ",";

	/*分页：参数值分隔符*/
	public static String PARAMS_VALUE_SPLIT_TOKEN = "=";

    /*正常响应代码值*/
    public static String SUCCESS = "0";

    /* 错误响应代码值 */
    public static String ERROR = "-1";

    /* 增删改默认提示信息 */
    public static String SUCCESS_MESSAGE = "操作成功！";
    public static String ERROR_MESSAGE = "操作失败！";
    public static String SAVE_SUCCESS_MESSAGE = "保存数据成功！";
    public static String DELETE_SUCCESS_MESSAGE = "删除数据成功！";
    public static String SAVE_ERROR_MESSAGE = "保存数据失败！";
    public static String DELETE_ERROR_MESSAGE = "删除数据失败！";

    /*登录验证信息*/
    public static String LOGIN_ERROR_NO_AUTHENTICATION = "-1";
    public static String LOGIN_ERROR_USERNAME = "0";
    public static String LOGIN_ERROR_PASSWORD = "1";
    public static String LOGIN_ERROR_USERNAME_MESSAGE = "当前用户不存在或未激活！";
    public static String LOGIN_ERROR_PASSWORD_MESSAGE = "用户密码不正确！";
    public static String LOGIN_ERROR_NO_AUTHENTICATION_MESSAGE = "当前用户无权限登录！";

    /*密码修改验证*/
    public static String MODIFY_PASSWORD_SUCCESS = "密码修改成功！";
    public static String MODIFY_PASSWORD_OLD_PASSWORD_ERROR = "原始密码不正确！";
    public static String MODIFY_PASSWORD_NEW_PASSWORD_ERROR = "两次密码不一致，请重新输入！";

    /*菜单根节点ID*/
    public static String ROOT_PARENT_MENU_ID = "ROOT_MENU";

    /* 是否超级用户 */
    public static String IS_SYS_FLAG = "1";

    public static String R = "R";

    /* 初始密码 */
    public static String INIT_PASSWORD = "123456";



    /*代码类型*/
    //是否可用
    public static final String CODE_ENABLED = "100002";

    //是否超级用户
    public static final String CODE_IS_SYS = "100003";

}
