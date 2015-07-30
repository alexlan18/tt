package com.wing.framework.utils;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;

/**
 * 反射动态设置和获取BEAN的属性值
 * @author zhongzh
 */
public class ReflectUtil {

	/**
	 * 通过反射设置bean的属性值
	 * 
	 * @param bean
	 * @param fname: 属性名
	 * @param ftype: 属性类型
	 * @param fvalue: 属性值
	 */
	public static void setFieldValue(Object bean, String fname, Class ftype, Object fvalue) {
		if ((bean == null) || (fname == null) || ("".equals(fname)) || ((fvalue != null) && (!ftype.isAssignableFrom(fvalue.getClass())))) {
			return;
		}

		Class clazz = bean.getClass();
		try {
			Method method = clazz.getDeclaredMethod("set" + Character.toUpperCase(fname.charAt(0)) + fname.substring(1), new Class[] { ftype });
			if (!Modifier.isPublic(method.getModifiers())) {
				method.setAccessible(true);
			}

			method.invoke(bean, new Object[] { fvalue });
		} catch (Exception me) {
			try {
				Field field = clazz.getDeclaredField(fname);
				if (!Modifier.isPublic(field.getModifiers())) {
					field.setAccessible(true);
				}
				field.set(bean, fvalue);
			} catch (Exception fe) {
				fe.printStackTrace();
			}
		}
	}
}
