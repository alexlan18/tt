package ${table.package};

#if($table.PkColumn.SqlTypeName == 'DECIMAL' || $table.PkColumn.SqlTypeName == 'NUMERIC')
import java.math.BigDecimal;
#end
#if($table.PkColumn.SqlTypeName == 'DATE' || $table.PkColumn.SqlTypeName == 'TIME' || $table.PkColumn.SqlTypeName == 'TIMESTAMP')
import java.util.Date;
#end
import java.util.List;
import java.util.HashMap;


import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.wing.framework.base.BaseDao;
import com.wing.framework.common.Page;
import ${plugin.middlegen.appname}.model.${table.destinationClassName};
import ${plugin.middlegen.appname}.dao.${table.destinationClassName}Dao;
import ${plugin.middlegen.appname}.model.${table.destinationClassName}Params;

import javax.annotation.Resource;

#set($JavaType = "")
#if($table.PkColumn.SqlTypeName == 'VARCHAR' || $table.PkColumn.SqlTypeName == 'CHAR' || $table.PkColumn.SqlTypeName == 'LONGVARCHAR')
#set($JavaType = "String")
#end
#if($table.PkColumn.SqlTypeName == 'INTEGER' || $table.PkColumn.SqlTypeName == 'INT')
#set($JavaType = "Integer")
#end
#if($table.PkColumn.SqlTypeName == 'BIT' || $table.PkColumn.SqlTypeName == 'BOOLEAN')
#set($JavaType = "Boolean")
#end
#if($table.PkColumn.SqlTypeName == 'TINYINT')
#set($JavaType = "Byte")
#end
#if($table.PkColumn.SqlTypeName == 'SMALLINT')
#set($JavaType = "Short")
#end
#if($table.PkColumn.SqlTypeName == 'BIGINT')
#set($JavaType = "Long")
#end
#if($table.PkColumn.SqlTypeName == 'REAL')
#set($JavaType = "Float")
#end
#if($table.PkColumn.SqlTypeName == 'FLOAT' || $table.PkColumn.SqlTypeName == 'DOUBLE')
#set($JavaType = "Double")
#end
#if($table.PkColumn.SqlTypeName == 'BINARY' || $table.PkColumn.SqlTypeName == 'VARBINARY' || $table.PkColumn.SqlTypeName == 'LONGVARBINARY')
#set($JavaType = "byte[]")
#end
#if($table.PkColumn.SqlTypeName == 'DATE' || $table.PkColumn.SqlTypeName == 'TIME' || $table.PkColumn.SqlTypeName == 'TIMESTAMP')
#set($JavaType = "Date")
#end
#if($table.PkColumn.SqlTypeName == 'DECIMAL' || $table.PkColumn.SqlTypeName == 'NUMERIC')
#set($JavaType = "BigDecimal")
#end

#set($valueOf = ".valueOf")

/**
 *
 * @author wing
 * @version 1.0
 *
 * the JAVA code is generate by middlegen
 *
 */
@Service("${table.destinationClassName}Service")
public class ${table.destinationClassName}Service {

    public static final String BEAN_ID = "${table.destinationClassName}Service";

    //注入Dao层
    @Resource(name = "${table.destinationClassName}Dao")
    private ${table.destinationClassName}Dao ${table.destinationClassName.substring(0,1).toLowerCase()}${table.destinationClassName.substring(1)}Dao;

    //注入BaseDao
    @Resource(name = "BaseDao")
    private BaseDao baseDao;

    /**
     *
     * 查询数据
     *
     * @param ${table.destinationClassName.substring(0,1).toLowerCase()}${table.destinationClassName.substring(1)}Params
     * @param page
     *
     */
    @Transactional
    public List query${table.destinationClassName}List(${table.destinationClassName}Params ${table.destinationClassName.substring(0,1).toLowerCase()}${table.destinationClassName.substring(1)}Params, Page page){

        HashMap<String, Object> params = new HashMap<String, Object>();

        params.put("params", ${table.destinationClassName.substring(0,1).toLowerCase()}${table.destinationClassName.substring(1)}Params);
        if(page.getSortname() != null && !page.getSortname().equals("")) {
            params.put("sortname", page.getSortname());
        }
        if(page.getSortorder() != null && !page.getSortorder().equals("")) {
            params.put("sortorder", page.getSortorder());
        }

        return baseDao.queryListByPage("query${table.destinationClassName}", params, page);
    }

    /**
     *
     * 插入数据
     * @param ${table.destinationClassName.substring(0,1).toLowerCase()}${table.destinationClassName.substring(1)}
     *
     */
    @Transactional
    public void save${table.destinationClassName}(${table.destinationClassName} ${table.destinationClassName.substring(0,1).toLowerCase()}${table.destinationClassName.substring(1)}) {
#if($JavaType == "String")
        if(${table.destinationClassName.substring(0,1).toLowerCase()}${table.destinationClassName.substring(1)}.get${table.PkColumn.variableName.substring(0,1).toUpperCase()}${table.PkColumn.variableName.substring(1)}() != null && !"".equals(${table.destinationClassName.substring(0,1).toLowerCase()}${table.destinationClassName.substring(1)}.get${table.PkColumn.variableName.substring(0,1).toUpperCase()}${table.PkColumn.variableName.substring(1)}())) {
#end
#if($JavaType != "String")
        if(${table.destinationClassName.substring(0,1).toLowerCase()}${table.destinationClassName.substring(1)}.get${table.PkColumn.variableName.substring(0,1).toUpperCase()}${table.PkColumn.variableName.substring(1)}() != null) {
#end
            ${table.destinationClassName} result = get${table.destinationClassName}(${table.destinationClassName.substring(0,1).toLowerCase()}${table.destinationClassName.substring(1)}.get${table.PkColumn.variableName.substring(0,1).toUpperCase()}${table.PkColumn.variableName.substring(1)}());
            if(result != null) {
                ${table.destinationClassName.substring(0,1).toLowerCase()}${table.destinationClassName.substring(1)}Dao.update(${table.destinationClassName.substring(0,1).toLowerCase()}${table.destinationClassName.substring(1)});
            } else {
                ${table.destinationClassName.substring(0,1).toLowerCase()}${table.destinationClassName.substring(1)}Dao.insert(${table.destinationClassName.substring(0,1).toLowerCase()}${table.destinationClassName.substring(1)});
            }
        } else {
            ${table.destinationClassName.substring(0,1).toLowerCase()}${table.destinationClassName.substring(1)}Dao.insert(${table.destinationClassName.substring(0,1).toLowerCase()}${table.destinationClassName.substring(1)});
        }
    }

    /**
     *
     * 删除数据
     * @param ${table.PkColumn.variableName}
     *
     */
    @Transactional
    public void delete${table.destinationClassName}($JavaType ${table.PkColumn.variableName}) {

        ${table.destinationClassName.substring(0,1).toLowerCase()}${table.destinationClassName.substring(1)}Dao.delete(${table.PkColumn.variableName});
    }

    /**
     *
     * 删除数据
     * @param ${table.destinationClassName.substring(0,1).toLowerCase()}${table.destinationClassName.substring(1)}List
     *
     */
    @Transactional
    public void delete${table.destinationClassName}Batch(List<String> ${table.destinationClassName.substring(0,1).toLowerCase()}${table.destinationClassName.substring(1)}List) {
        for(String ${table.PkColumn.variableName} : ${table.destinationClassName.substring(0,1).toLowerCase()}${table.destinationClassName.substring(1)}List) {
            delete${table.destinationClassName}($JavaType$valueOf(${table.PkColumn.variableName}));
        }
    }

    /**
     *
     * 根据ID获取数据
     * @param ${table.PkColumn.variableName}
     *
     */
    @Transactional
    public ${table.destinationClassName} get${table.destinationClassName}($JavaType ${table.PkColumn.variableName}) {

        return ${table.destinationClassName.substring(0,1).toLowerCase()}${table.destinationClassName.substring(1)}Dao.get${table.destinationClassName}By${table.PkColumn.variableName.substring(0,1).toUpperCase()}${table.PkColumn.variableName.substring(1)}(${table.PkColumn.variableName});
    }
}