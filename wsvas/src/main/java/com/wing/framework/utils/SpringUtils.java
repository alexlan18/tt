package com.wing.framework.utils;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.wing.framework.base.BaseDao;

/**
 * spring工具类
 * 
 * @author zhongzh
 * 
 */
public class SpringUtils {


	private static ApplicationContext applicationContext=createContext();

	private static ApplicationContext createContext() {
		String[] context = {
				"classpath:/spring.xml",
				"classpath:/spring-*.xml"
				};
		return new ClassPathXmlApplicationContext(context);
	}

	public static ApplicationContext getApplicationContext() {
		return applicationContext;
	}

	/**
	 * 根据beanId取得实例
	 * @param <T>
	 * @param beanId,bean的id或name
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public static <T> T getBean(String beanId) {
		return (T) applicationContext.getBean(beanId);
	}
	
	public static void main(String[] args) {
		BaseDao baseDao = (BaseDao)SpringUtils.getBean("BaseDao");
		System.out.println("class name:"+baseDao.getClass().getName());
	}
}
