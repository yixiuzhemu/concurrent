package com.ly.bean.util;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Map.Entry;

public class ConvertUtils {
	/**
	 * 将对象t中与对象m中相同的属性设置值
	 * @param t 
	 * @param m
	 * @return M 
	 */
	public static <T, M> M convertEntity(T t, Class<M> m) {
		try {
			if (t == null || m == null) {
				return null;
			}
			Class<? extends Object> t1 = t.getClass();
			M m1 = m.newInstance();
			Field[] filds = t1.getDeclaredFields();
			Field[] filds1 = m.getDeclaredFields();
			for (Field f1 : filds1) {
				String name = f1.getName();
				for (Field f2 : filds) {
					String fildName = f2.getName();
					if (name.equals(fildName)) {
						String setMethodName = "set"
								+ name.substring(0, 1).toUpperCase()
								+ name.substring(1);
						String getMethodName = "get"
								+ fildName.substring(0, 1).toUpperCase()
								+ fildName.substring(1);
						Method getMethod = getMethodByName(t1, getMethodName);
						Object value = getMethod.invoke(t);
						Method setMethod = getMethodByName(m, setMethodName);
						setMethod.invoke(m1, value);
					}
				}
			}
			return m1;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
	/**
	 * 根据o 对应对象属性值，v作为值存储到对象中
	 * @param o 属性名称
	 * @param v 属性值
	 * @param entity 指定对象
	 */
	public static <T, V, M> void convertVO(T o, V v, M entity) {
		try {
			// 获取对象的类信息
			Class<? extends Object> voClass = entity.getClass();
			if (entity != null) {
				// 获取类的属性信息
				Field[] fields = voClass.getDeclaredFields();
				Method method = null;
				// 遍历属性，获取属性的set方法，然后将值 设置到对象中
				for (Field field : fields) {
					String name = field.getName();
					if (name.equals(o)) {
						name = "set" + name.substring(0, 1).toUpperCase()
								+ name.substring(1);
						method = getMethodByName(voClass, name);
						if (method != null) {
							String value = v.toString();
							// 特殊字符串处理
							Integer index = value.indexOf("[") + 1;
							Integer end = value.lastIndexOf("]");
							if (index > 0 && end > 0 && index < end) {
								value = value.substring(index, end);
							}
							method.invoke(entity, value);
						}
					}
				}
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	/**
	 * 将对象t和对象v中与对象v中相同的属性设置值
	 * @param t
	 * @param m
	 * @param v
	 * @return V
	 */
	public static <T, M, V> V converTAndM2V(T t, M m, Class<V> v) {
		if (t != null && m != null && v != null) {
			try {
				Class<? extends Object> t1 = t.getClass();
				Class<? extends Object> m1 = m.getClass();
				V v1 = v.newInstance();

				Field[] fields = v.getDeclaredFields();
				Field[] fields2 = m1.getDeclaredFields();
				Field[] fields3 = t1.getDeclaredFields();
				for (Field field : fields) {
					String fieldName = field.getName();
					String field3Name = null;
					String field2Name = null;
					for (Field field3 : fields3) {
						field3Name = field3.getName();
						if (fieldName.equals(field3Name)) {
							String methodName = "set"
									+ fieldName.substring(0, 1).toUpperCase()
									+ fieldName.substring(1);
							String method3Name = "get"
									+ fieldName.substring(0, 1).toUpperCase()
									+ fieldName.substring(1);
							Method method3 = getMethodByName(t1, method3Name);
							Object value = method3.invoke(t);
							Method method = getMethodByName(v, methodName);
							if (method != null) {
								method.invoke(v1, value);
								break;
							}
						}
					}
					for (Field field2 : fields2) {
						field2Name = field2.getName();
						if (fieldName.equals(field2Name)) {
							String methodName = "set"
									+ fieldName.substring(0, 1).toUpperCase()
									+ fieldName.substring(1);
							String method2Name = "get"
									+ fieldName.substring(0, 1).toUpperCase()
									+ fieldName.substring(1);
							Method method2 = getMethodByName(m1, method2Name);
							Object value = method2.invoke(m);
							Method method = getMethodByName(v, methodName);
							if (method != null) {
								method.invoke(v1, value);
								break;
							}

						}
					}

				}
				return v1;
			} catch (Exception e) {
				e.printStackTrace();
				return null;
			}
		}
		return null;
	}
	/**
	 * 将map信息转换成对象信息,
	 * @param map 指定的集合
	 * @param voClass 需要转换的对象
	 * @param isNeedAll 是否为空都包含
	 * @return M 返回指定的对象
	 */
	public static <T, M> M convertEntity(Map<String, List<String>> map,
			Class<M> voClass,boolean isNeedAll) {
		if (voClass == null) {
			return null;
		}
		M entity = null;
		try {
			// 获取类实例
			entity = voClass.newInstance();
		} catch (Exception e) {
			System.err.print("获取类实例失败!");
		}
		// 遍历map
		Set<Entry<String, List<String>>> set = map.entrySet();
		Iterator<Entry<String, List<String>>> iterator = set.iterator();
		while (iterator.hasNext()) {
			Entry<String, List<String>> next = iterator.next();
			// 将map中的所有信息 存储到对象中
			convertVO(next.getKey(), next.getValue(), entity);
		}
		// 判断对象中是否存在空值
		if(isNeedAll){
			return entity;
		}
			boolean entityIsNUll = ConvertUtils.EntityIsNUll(entity);
			if (entityIsNUll) {
				return null;
			}
			return entity;
		
	}
	/**
	 * 将Object数组 转换为对应的对象
	 * @param m
	 * @param t
	 * @return T 返回指定的对象
	 */
	public static <M, T> T ConvertObject(M[] m, Class<T> t) {
		T t2 = null;
		try {
			Field[] fields = t.getDeclaredFields();
			t2 = t.newInstance();
			Method method = null;
			if (m.length == fields.length) {
				for (int i = 0; i < fields.length; i++) {
					Field field = fields[i];
					String type = field.getGenericType().toString();
					String name = field.getName();

					name = "set" + name.substring(0, 1).toUpperCase()
							+ name.substring(1);
					method = getMethodByName(t, name);
					if (method != null) {
						if(m[i]==null){
							continue;
						}
						String value = m[i].toString();
						switch (type) {
						case "class java.lang.Integer":
							Integer integer = Integer.valueOf(value);
							method.invoke(t2, integer);
							break;
						case "class java.lang.Double":
							Double double1 = Double.valueOf(value);
							method.invoke(t2, double1);
							break;
						case "class java.lang.String":
							method.invoke(t2, value);
							break;
						case "class java.sql.Timestamp":
							Timestamp time = Timestamp.valueOf(value);
							method.invoke(t2, time);
							break;
						default:
							break;
						}
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
		return t2;
	}
	/**
	 * 将对象M 中的 值依次设置到 对象T 中，直到一方全部设置值或者另一方的值全部被获取（支持Map）
	 * @param m
	 * @param t
	 * @param size
	 * @return
	 */
	public static <M, T> List<T> convertObjectBySort(M m, Class<T> t, int size) {
		List<T> list = new ArrayList<T>();
		T t2 = null;
		int length = 0;
		try {
			Class<? extends Object> mClass = m.getClass();
			Field[] fields2 = mClass.getDeclaredFields();
			Field[] fields = t.getDeclaredFields();
			length = fields2.length < fields.length ? fields2.length
					: fields.length;
			Method method = null;
			Method method2 = null;
			for (int j = 0; j < size; j++) {
				t2 = t.newInstance();
				for (int i = 0; i < length; i++) {
					Field field = fields[i];
					Field field2 = fields2[i];
					String type = field.getGenericType().toString();
					String name = field.getName();
					String name2 = field2.getName();
					name = "set" + name.substring(0, 1).toUpperCase()
							+ name.substring(1);
					name2 = "get" + name2.substring(0, 1).toUpperCase()
							+ name2.substring(1);
					method = getMethodByName(t, name);
					method2 = getMethodByName(mClass, name2);
					if (method != null && method2 != null) {
						Object o = method2.invoke(m);
						if (o == null) {
							break;
						}
						String value = o.toString();
						String[] trim = StringUtils.splitStringTrim(value, ",");
						if (trim.length > j) {
							value = trim[j];
						}
						switch (type) {
						case "class java.lang.Integer":
							if (!"".equals(value)) {
								Integer integer = Integer.valueOf(value);
								method.invoke(t2, integer);
							}
							break;
						case "class java.lang.Double":

							if (!"".equals(value)) {
								Double double1 = Double.valueOf(value);
								method.invoke(t2, double1);
							}
							break;
						case "class java.lang.String":
							method.invoke(t2, value);
							break;
						case "class java.sql.Timestamp":
							if (!"".equals(value)) {
								Timestamp time = Timestamp.valueOf(value);
								method.invoke(t2, time);
							}
							break;
						case "java.util.Map<java.lang.String, java.lang.String>":
							if (value.indexOf("{") != -1
									&& value.indexOf("=") != -1) {
								String k = value.substring(
										value.indexOf("{") + 1,
										value.indexOf("="));
								String v = value.substring(
										value.indexOf("=") + 1,
										value.indexOf("}"));
								Map<String, String> map = new HashMap<String, String>();
								map.put(k, v);
								method.invoke(t2, map);
							}

						default:
							break;
						}
					}
				}
				list.add(t2);
			}

		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
		return list;
	}
	/**
	 * 通过名称获取方法
	 * @param voClass 指定的对象
	 * @param methodName 方法名称
	 * @return Method 返回对应对象中对应的方法名称的方法
	 */
	public static <M> Method getMethodByName(Class<M> voClass, String methodName) {
		Method[] methods = voClass.getMethods();
		for (Method method : methods) {
			if (methodName.equals(method.getName())) {
				return method;
			}
		}
		return null;
	}
	
	/**
	 * 判断对象的属性是否存在空值
	 * @param entity 需要判断的对象
	 * @return boolean 返回true（存在空值） 或者 false （不存在空值）
	 */
		public static <M> boolean EntityIsNUll(M entity) {
			boolean hasNull = false;
			try {
				// 获取对象的类信息
				Class<? extends Object> voClass = entity.getClass();
				// 获取类中的属性信息
				Field[] fields = voClass.getDeclaredFields();
				Method method = null;
				// 遍历 属性信息 获取其get方法，判断是否存在空值
				for (Field field : fields) {
					String name = field.getName();
					name = "get" + name.substring(0, 1).toUpperCase()
							+ name.substring(1);
					method = getMethodByName(voClass, name);
					if (method != null) {
						Object o = method.invoke(entity);
						if (o == null) {
							hasNull = true;
							return hasNull;
						}
					}
				}
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			return hasNull;
		}

}
