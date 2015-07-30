package com.wing.framework.cache;

import org.apache.log4j.Logger;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
/**
 *
 * 功能：代码缓存监听器，应用启动时将代码数据存于内存，应用停止时销毁内存中的代码缓存
 * desc:ServletContextListener用于监听WEB应用启动和销毁的事件,
 * 是 ServletContext 的监听者，如果 ServletContext 发生变化，
 * 如服务器启动时 ServletContext 被创建，服务器关闭时 ServletContext 将要被销毁
 *
 * @author panwb
 *
 */
public class CodeCacheListener implements ServletContextListener {
	
	private static Logger logger = Logger.getLogger(CodeCacheListener.class);

	//应用启动时触发,实现将代码数据存于内存
	@Override
	public void contextInitialized(ServletContextEvent servletcontextevent) {
		try {

			logger.info("加载代码缓存数据！");
            CodeCache.getInstance().loadData();

		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	//应用停止时触发，实现清除内存中的代码数据
	@Override
	public void contextDestroyed(ServletContextEvent servletcontextevent) {
		try {

			logger.info("摧毁代码缓存数据！");
            CodeCache.getInstance().destroy();

		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
}
