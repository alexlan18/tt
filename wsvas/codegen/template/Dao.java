package ${table.package};

#if($table.PkColumn.SqlTypeName == 'DECIMAL' || $table.PkColumn.SqlTypeName == 'NUMERIC')
import java.math.BigDecimal;
#end
#if($table.PkColumn.SqlTypeName == 'DATE' || $table.PkColumn.SqlTypeName == 'TIME' || $table.PkColumn.SqlTypeName == 'TIMESTAMP' || $column.SqlTypeName == 'DATETIME')
import java.util.Date;
#end
import java.util.List;

#set($endIndex = $table.package.lastIndexOf("."))
import ${table.package.substring(0, $endIndex)}.model.${table.destinationClassName};
import com.wing.framework.mybatis.mapper.SqlMapper;
import org.springframework.stereotype.Component;
import org.apache.ibatis.annotations.Param;

import ${plugin.middlegen.appname}.model.${table.destinationClassName}Params;

/**
 *
 * @author wing
 * @version 1.0
 *
 * the JAVA code is generate by middlegen
 *
 */
@Component("${table.destinationClassName}Dao")
public interface ${table.destinationClassName}Dao extends SqlMapper {

    /**
     *
     * 查询数据
     *
    */
    public List query${table.destinationClassName}(@Param("params") ${table.destinationClassName}Params params);

    /**
     *
     * 根据主键获取数据
     *
     */
#if($table.PkColumn.SqlTypeName == 'VARCHAR' || $table.PkColumn.SqlTypeName == 'CHAR' || $table.PkColumn.SqlTypeName == 'LONGVARCHAR')
    public ${table.destinationClassName} get${table.destinationClassName}By${table.PkColumn.variableName.substring(0,1).toUpperCase()}${table.PkColumn.variableName.substring(1)}(String ${table.PkColumn.variableName});
#end
#if($table.PkColumn.SqlTypeName == 'INTEGER' || $table.PkColumn.SqlTypeName == 'INT')
    public ${table.destinationClassName} get${table.destinationClassName}By${table.PkColumn.variableName.substring(0,1).toUpperCase()}${table.PkColumn.variableName.substring(1)}(Integer ${table.PkColumn.variableName});
#end
#if($table.PkColumn.SqlTypeName == 'BIT' || $table.PkColumn.SqlTypeName == 'BOOLEAN')
    public ${table.destinationClassName} get${table.destinationClassName}By${table.PkColumn.variableName.substring(0,1).toUpperCase()}${table.PkColumn.variableName.substring(1)}(Boolean ${table.PkColumn.variableName});
#end
#if($table.PkColumn.SqlTypeName == 'TINYINT')
    public ${table.destinationClassName} get${table.destinationClassName}By${table.PkColumn.variableName.substring(0,1).toUpperCase()}${table.PkColumn.variableName.substring(1)}(Byte ${table.PkColumn.variableName});
#end
#if($table.PkColumn.SqlTypeName == 'SMALLINT')
    public ${table.destinationClassName} get${table.destinationClassName}By${table.PkColumn.variableName.substring(0,1).toUpperCase()}${table.PkColumn.variableName.substring(1)}(Short ${table.PkColumn.variableName});
#end
#if($table.PkColumn.SqlTypeName == 'BIGINT')
    public ${table.destinationClassName} get${table.destinationClassName}By${table.PkColumn.variableName.substring(0,1).toUpperCase()}${table.PkColumn.variableName.substring(1)}(Long ${table.PkColumn.variableName});
#end
#if($table.PkColumn.SqlTypeName == 'REAL')
    public ${table.destinationClassName} get${table.destinationClassName}By${table.PkColumn.variableName.substring(0,1).toUpperCase()}${table.PkColumn.variableName.substring(1)}(Float ${table.PkColumn.variableName});
#end
#if($table.PkColumn.SqlTypeName == 'FLOAT' || $table.PkColumn.SqlTypeName == 'DOUBLE')
    public ${table.destinationClassName} get${table.destinationClassName}By${table.PkColumn.variableName.substring(0,1).toUpperCase()}${table.PkColumn.variableName.substring(1)}(Double ${table.PkColumn.variableName});
#end
#if($table.PkColumn.SqlTypeName == 'BINARY' || $table.PkColumn.SqlTypeName == 'VARBINARY' || $table.PkColumn.SqlTypeName == 'LONGVARBINARY')
    public ${table.destinationClassName} get${table.destinationClassName}By${table.PkColumn.variableName.substring(0,1).toUpperCase()}${table.PkColumn.variableName.substring(1)}(byte[] ${table.PkColumn.variableName});
#end
#if($table.PkColumn.SqlTypeName == 'DATE' || $table.PkColumn.SqlTypeName == 'TIME' || $table.PkColumn.SqlTypeName == 'TIMESTAMP' || $column.SqlTypeName == 'DATETIME')
    public ${table.destinationClassName} get${table.destinationClassName}By${table.PkColumn.variableName.substring(0,1).toUpperCase()}${table.PkColumn.variableName.substring(1)}(Date ${table.PkColumn.variableName});
#end
#if($table.PkColumn.SqlTypeName == 'DECIMAL' || $table.PkColumn.SqlTypeName == 'NUMERIC')
    public ${table.destinationClassName} get${table.destinationClassName}By${table.PkColumn.variableName.substring(0,1).toUpperCase()}${table.PkColumn.variableName.substring(1)}(BigDecimal ${table.PkColumn.variableName});
#end

    /**
     *
     * 插入数据
     *
     */
    public void insert(${table.destinationClassName} ${table.destinationClassName.substring(0,1).toLowerCase()}${table.destinationClassName.substring(1)});

    /**
     *
     * 删除数据
     *
     */
#if($table.PkColumn.SqlTypeName == 'VARCHAR' || $table.PkColumn.SqlTypeName == 'CHAR' || $table.PkColumn.SqlTypeName == 'LONGVARCHAR')
    public void delete(String ${table.PkColumn.variableName});
#end
#if($table.PkColumn.SqlTypeName == 'INTEGER' || $table.PkColumn.SqlTypeName == 'INT')
    public void delete(Integer ${table.PkColumn.variableName});
#end
#if($table.PkColumn.SqlTypeName == 'BIT' || $table.PkColumn.SqlTypeName == 'BOOLEAN')
    public void delete(Boolean ${table.PkColumn.variableName});
#end
#if($table.PkColumn.SqlTypeName == 'TINYINT')
    public void delete(Byte ${table.PkColumn.variableName});
#end
#if($table.PkColumn.SqlTypeName == 'SMALLINT')
    public void delete(Short ${table.PkColumn.variableName});
#end
#if($table.PkColumn.SqlTypeName == 'BIGINT')
    public void delete(Long ${table.PkColumn.variableName});
#end
#if($table.PkColumn.SqlTypeName == 'REAL')
    public void delete(Float ${table.PkColumn.variableName});
#end
#if($table.PkColumn.SqlTypeName == 'FLOAT' || $table.PkColumn.SqlTypeName == 'DOUBLE')
    public void delete(Double ${table.PkColumn.variableName});
#end
#if($table.PkColumn.SqlTypeName == 'BINARY' || $table.PkColumn.SqlTypeName == 'VARBINARY' || $table.PkColumn.SqlTypeName == 'LONGVARBINARY')
    public void delete(byte[] ${table.PkColumn.variableName});
#end
#if($table.PkColumn.SqlTypeName == 'DATE' || $table.PkColumn.SqlTypeName == 'TIME' || $table.PkColumn.SqlTypeName == 'TIMESTAMP' || $column.SqlTypeName == 'DATETIME')
    public void delete(Date ${table.PkColumn.variableName});
#end
#if($table.PkColumn.SqlTypeName == 'DECIMAL' || $table.PkColumn.SqlTypeName == 'NUMERIC')
    public void delete(BigDecimal ${table.PkColumn.variableName});
#end

    /**
     *
     * 更新数据
     *
     */
    public void update(${table.destinationClassName} ${table.destinationClassName.substring(0,1).toLowerCase()}${table.destinationClassName.substring(1)});
}
