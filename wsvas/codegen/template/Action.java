package ${table.package};

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.ArrayList;
import java.util.Arrays;
import javax.annotation.Resource;

import com.wing.framework.common.FlexGrid;
import com.wing.framework.common.Page;
import com.wing.framework.common.Message;
import com.wing.framework.common.constant.SysContant;
import ${plugin.middlegen.appname}.model.${table.destinationClassName}Params;
import ${plugin.middlegen.appname}.model.${table.destinationClassName};
import ${plugin.middlegen.appname}.service.${table.destinationClassName}Service;
import com.wing.framework.base.BaseAction;

#set($endIndex = $plugin.middlegen.appname.lastIndexOf("."))
#set($jspIndex = $plugin.middlegen.appname.lastIndexOf(".")+1)
#set($jsppath = $plugin.middlegen.appname.substring($jspIndex))
#set($tmpStr = $plugin.middlegen.appname.substring(0,$endIndex))
#set($startIndex = $tmpStr.lastIndexOf(".")+1)
#set($module = $tmpStr.substring($startIndex,$endIndex))
#set($getStr = "list.get(i-1).get")

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
#if($table.PkColumn.SqlTypeName == 'DATE' || $table.PkColumn.SqlTypeName == 'TIME' || $table.PkColumn.SqlTypeName == 'TIMESTAMP' || $column.SqlTypeName == 'DATETIME')
#set($JavaType = "Date")
#end
#if($table.PkColumn.SqlTypeName == 'DECIMAL' || $table.PkColumn.SqlTypeName == 'NUMERIC')
#set($JavaType = "BigDecimal")
#end

/**
 *
 * @author wing
 * @version 1.0
 *
 * the JAVA code is generate by middlegen
 *
 */
@Controller
@RequestMapping("/${module}/${table.destinationClassName.substring(0, 1).toLowerCase()}${table.destinationClassName.substring(1)}")
public class ${table.destinationClassName}Action extends BaseAction {

    //服务层
    @Resource(name = "${table.destinationClassName}Service")
    private ${table.destinationClassName}Service ${table.destinationClassName.substring(0, 1).toLowerCase()}${table.destinationClassName.substring(1)}Service;

    /**
     *
     * 跳转到列表页面
     *
     */
    @RequestMapping
    public String go${table.destinationClassName}() {
        return"/${module}/${jsppath}/${table.destinationClassName}List";
    }

    /**
     *
     * 加载数据
     *
     */
    @RequestMapping("/load")
    @ResponseBody
    public FlexGrid load${table.destinationClassName}(HttpServletRequest request, ${table.destinationClassName}Params ${table.destinationClassName.substring(0, 1).toLowerCase()}${table.destinationClassName.substring(1)}Params, Page page) {

        try {

            List results = ${table.destinationClassName.substring(0, 1).toLowerCase()}${table.destinationClassName.substring(1)}Service.query${table.destinationClassName}List(${table.destinationClassName.substring(0, 1).toLowerCase()}${table.destinationClassName.substring(1)}Params, page);
            return new FlexGrid(results, page.getCurrentPage(), page.getTotalSize());

        } catch(Exception e) {
            e.printStackTrace();
            logger.error("查询${table.destinationClassName}数据异常！");
        }

        return null;
    }

    /**
     *
     * 根据ID获取数据
     *
     */
    @RequestMapping("/{${table.PkColumn.variableName}}")
    @ResponseBody
    public ${table.destinationClassName} get${table.destinationClassName}(@PathVariable("${table.PkColumn.variableName}") $JavaType ${table.PkColumn.variableName}) {
        return ${table.destinationClassName.substring(0, 1).toLowerCase()}${table.destinationClassName.substring(1)}Service.get${table.destinationClassName}(${table.PkColumn.variableName});
    }

    /**
     *
     * 根据IDs删除数据
     *
     */
    @RequestMapping("/delete")
    @ResponseBody
    public Message delete${table.destinationClassName}(String ${table.PkColumn.variableName}s) {
        try {
            List<String> ${table.PkColumn.variableName}List = new ArrayList<String>();
            if(${table.PkColumn.variableName}s.length() > 0) {
                ${table.PkColumn.variableName}List = Arrays.asList(${table.PkColumn.variableName}s.split(","));
            }
            ${table.destinationClassName.substring(0, 1).toLowerCase()}${table.destinationClassName.substring(1)}Service.delete${table.destinationClassName}Batch(${table.PkColumn.variableName}List);
        } catch(Exception e) {
           e.printStackTrace();
           logger.error("数据" + ${table.PkColumn.variableName}s + "删除异常！");
            return new Message(SysContant.ERROR, SysContant.DELETE_ERROR_MESSAGE);
        }
        return new Message(SysContant.SUCCESS, SysContant.DELETE_SUCCESS_MESSAGE);
    }

    /**
     *
     * 保存数据
     *
     */
    @RequestMapping("/save")
    @ResponseBody
    public Message save${table.destinationClassName}(${table.destinationClassName} ${table.destinationClassName.substring(0, 1).toLowerCase()}${table.destinationClassName.substring(1)}) {

        try {
            ${table.destinationClassName.substring(0, 1).toLowerCase()}${table.destinationClassName.substring(1)}Service.save${table.destinationClassName}(${table.destinationClassName.substring(0, 1).toLowerCase()}${table.destinationClassName.substring(1)});
        } catch(Exception e) {
            e.printStackTrace();
            logger.error("数据${table.destinationClassName}保存异常！");
            return new Message(SysContant.ERROR, SysContant.SAVE_ERROR_MESSAGE);
        }
        return new Message(SysContant.SUCCESS, SysContant.SAVE_SUCCESS_MESSAGE);
    }
}
