package com.etl.base.common.util;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 * 反射操作工具类
 */
public final class ReflectUtils {

  /**
   * <p>
   * 反射 method 方法名，例如 getId
   * </p>
   *
   * @param field
   * @param str   属性字符串内容
   * @return
   */
  public static String getMethodCapitalize(Field field, final String str) {
    Class<?> fieldType = field.getType();
    // fix #176
    return boolean.class.equals(fieldType) ? "is" : "get" + upperFirstChar(str);
  }

  private static String upperFirstChar(String str){
    char[] chars = str.toCharArray();
    if(Character.isUpperCase(chars[0])){
      return str;
    } else {
      chars[0] = Character.toUpperCase(chars[0]);
      return new String(chars);
    }
  }

  /**
   * <p>
   * 获取 public get方法的值
   * </p>
   *
   * @param entity 实体
   * @param str    属性字符串内容
   * @return Object
   */
  public static Object getMethodValue(Object entity, String str) throws Exception {
    if (null == entity) {
      return null;
    }

    Class<?> cls = entity.getClass();

    Map<String, Field> fieldMaps = getFieldMap(cls);
    AssertUtils.notEmpty(fieldMaps, String.format("Error: NoSuchField in %s for %s.  Cause:", cls.getSimpleName(), str));

    Method method = cls.getMethod(getMethodCapitalize(fieldMaps.get(str), str));
    return method.invoke(entity);
  }

  /**
   * <p>
   * 反射对象获取泛型
   * </p>
   *
   * @param clazz 对象
   * @param index 泛型所在位置
   * @return Class
   */
  public static Class getSuperClassGenricType(final Class clazz, final int index) {
    Type genType = clazz.getGenericSuperclass();
    if (!(genType instanceof ParameterizedType)) {
      return Object.class;
    }
    Type[] params = ((ParameterizedType) genType).getActualTypeArguments();
    if (index >= params.length || index < 0) {
      return Object.class;
    }
    if (!(params[index] instanceof Class)) {
      return Object.class;
    }
    return (Class) params[index];
  }

  /**
   * <p>
   * 获取该类的所有属性列表
   * </p>
   *
   * @param clazz 反射类
   * @return
   */
  public static Map<String, Field> getFieldMap(Class<?> clazz) {
    List<Field> fieldList = getFieldList(clazz);
    Map<String, Field> fieldMap = Collections.emptyMap();
    if (CollectionUtils.isNotEmpty(fieldList)) {
      fieldMap = new LinkedHashMap<>();
      for (Field field : fieldList) {
        fieldMap.put(field.getName(), field);
      }
    }
    return fieldMap;
  }

  /**
   * <p>
   * 获取该类的所有属性列表
   * </p>
   *
   * @param clazz 反射类
   * @return
   */
  public static List<Field> getFieldList(Class<?> clazz) {
    if (null == clazz) {
      return null;
    }
    List<Field> fieldList = new LinkedList<>();
    Field[] fields = clazz.getDeclaredFields();
    for (Field field : fields) {
            /* 过滤静态属性 */
      if (Modifier.isStatic(field.getModifiers())) {
        continue;
      }
            /* 过滤 transient关键字修饰的属性 */
      if (Modifier.isTransient(field.getModifiers())) {
        continue;
      }
      fieldList.add(field);
    }
        /* 处理父类字段 */
    Class<?> superClass = clazz.getSuperclass();
    if (superClass.equals(Object.class)) {
      return fieldList;
    }
        /* 排除重载属性 */
    return excludeOverrideSuperField(fieldList, getFieldList(superClass));
  }

  /**
   * <p>
   * 排序重置父类属性
   * </p>
   *
   * @param fieldList      子类属性
   * @param superFieldList 父类属性
   */
  public static List<Field> excludeOverrideSuperField(List<Field> fieldList, List<Field> superFieldList) {
    // 子类属性
    Map<String, Field> fieldMap = new HashMap<>();
    for (Field field : fieldList) {
      fieldMap.put(field.getName(), field);
    }
    for (Field superField : superFieldList) {
      if (null == fieldMap.get(superField.getName())) {
        // 加入重置父类属性
        fieldList.add(superField);
      }
    }
    return fieldList;
  }

  /**
   * java bean 转 map
   * @param javabean
   * @return
   * @throws Exception
   */
  public static Map<String, Object> javaBeanToMap(Object javabean) throws Exception {
    Map<String, Object> map = new LinkedHashMap<String, Object>();
    if (javabean == null) { return map; }

    Class javabeanClass = javabean.getClass();
    List<Field> fields = getClassAndParentFields(javabeanClass);
    for (Field field : fields) {
      if (!field.isAccessible()) {
        field.setAccessible(true);
      }
      map.put(field.getName(), field.get(javabean));
    }

    return map;
  }

  private static List<Field> getClassAndParentFields(Class cs) {
    List<Field> list = new ArrayList<Field>();
    for (; cs != Object.class; cs = cs.getSuperclass()) {
      Field[] fields = cs.getDeclaredFields();
      for (Field field : fields) {
        list.add(field);
      }
    }
    return list;
  }

  private ReflectUtils(){}

}
