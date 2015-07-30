package com.wing.framework.common;

import com.wing.framework.annotation.CodeType;
import com.wing.framework.cache.CodeCache;
import com.wing.framework.converter.JsonDateTimeSerializer;
import com.wing.framework.utils.DateUtil;
import com.wing.system.codeinfo.model.SCodeInfo;
import org.codehaus.jackson.map.annotate.JsonSerialize;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 *
 * Flexgird数据对象
 *
 * @author: panwb
 *
 * Date: 14-2-13
 * Time: 下午4:36
 */
public class FlexGrid {

    private String page;

    private String total;

    private List<Row> rows;

    public FlexGrid(List list, int page, int total) {

        this.setPage(String.valueOf(page));
        this.setTotal(String.valueOf(total));

        rows = new ArrayList<Row>();

        try {

            int count = 0;
            for(Object object : list) {
                Row cell = new Row();
                cell.setId(String.valueOf(count+1));
                Field[] fields = object.getClass().getDeclaredFields();
                Object[] values = new Object[fields.length];
                int i = 0;
                for(Field field : fields) {

                    //获取用户注解
                    Annotation[] annotations = field.getAnnotations();

                    field.setAccessible(true);
                    values[i] = field.get(object);
                    if(values[i] instanceof Date) {

                        //判断是否注解了JsonDateTimeSerializer
                        for(Annotation annotation : annotations) {
                            if(annotation.annotationType().equals(JsonSerialize.class)) {
                                if(((JsonSerialize)annotation).using().equals(JsonDateTimeSerializer.class)) {
                                    values[i] = DateUtil.formatDateTime(values[i]);
                                }
                                break;
                            }
                        }
                        //如果未格式化，格式化为yyyy-MM-dd
                        if(values[i] instanceof Date) {
                            values[i] = DateUtil.formatDate(values[i]);
                        }
                    }

                    //判断是否注解了CodeType
                    for(Annotation annotation : annotations) {
                        if(annotation.annotationType().equals(CodeType.class)) {
                            List<SCodeInfo> codeInfoList = CodeCache.getInstance().getValue(((CodeType) annotation).id());

                            for(SCodeInfo codeInfo : codeInfoList) {
                                if(codeInfo.getCodeValue().equals(field.get(object).toString())) {
                                    values[i] = codeInfo.getCodeName();
                                    break;
                                }
                            }
                            break;
                        }
                    }

                    i++;
                }
                cell.setCell(values);
                count++;
                rows.add(cell);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public String getPage() {
        return page;
    }

    public void setPage(String page) {
        this.page = page;
    }

    public List getRows() {
        return rows;
    }

    public void setRows(ArrayList<Row> rows) {
        this.rows = rows;
    }

    public String getTotal() {
        return total;
    }

    public void setTotal(String total) {
        this.total = total;
    }
}
