package ${table.package};

#foreach($column in $table.columns)
#if($column.SqlTypeName == 'DECIMAL' || $column.SqlTypeName == 'NUMERIC')
import java.math.BigDecimal;
#end
#end
#foreach($column in $table.columns)
#if($column.SqlTypeName == 'DATE' || $column.SqlTypeName == 'TIME' || $column.SqlTypeName == 'TIMESTAMP' || $column.SqlTypeName == 'DATETIME')
import java.util.Date;
#end
#end

/**
 *
 * @author wing
 * @version 1.0
 *
 * the JAVA code is generate by middlegen
 *
 */
public class ${table.destinationClassName} {

#foreach($column in $table.columns)
#if($column.SqlTypeName == 'VARCHAR' || $column.SqlTypeName == 'CHAR' || $column.SqlTypeName == 'LONGVARCHAR')
    //$column.remarks
    private String $column.variableName;
#end
#if($column.SqlTypeName == 'INTEGER' || $column.SqlTypeName == 'INT')
    //$column.remarks
    private Integer $column.variableName;
#end
#if($column.SqlTypeName == 'BIT' || $column.SqlTypeName == 'BOOLEAN')
    //$column.remarks
    private Boolean $column.variableName;
#end
#if($column.SqlTypeName == 'TINYINT')
    //$column.remarks
    private byte $column.variableName;
#end
#if($column.SqlTypeName == 'SMALLINT')
    private Short $column.variableName;
#end
#if($column.SqlTypeName == 'BIGINT')
    //$column.remarks
    private Long $column.variableName;
#end
#if($column.SqlTypeName == 'REAL')
    //$column.remarks
    private Float $column.variableName;
#end
#if($column.SqlTypeName == 'FLOAT' || $column.SqlTypeName == 'DOUBLE')
    //$column.remarks
    private Double $column.variableName;
#end
#if($column.SqlTypeName == 'BINARY' || $column.SqlTypeName == 'VARBINARY' || $column.SqlTypeName == 'LONGVARBINARY')
    //$column.remarks
    private byte[] $column.variableName;
#end
#if($column.SqlTypeName == 'DATE' || $column.SqlTypeName == 'TIME' || $column.SqlTypeName == 'TIMESTAMP' || $column.SqlTypeName == 'DATETIME')
    //$column.remarks
    private Date $column.variableName;
#end
#if($column.SqlTypeName == 'DECIMAL' || $column.SqlTypeName == 'NUMERIC')
    //$column.remarks
    private BigDecimal $column.variableName;
#end
#end

#foreach($column in $table.columns)
#if($column.SqlTypeName == 'VARCHAR' || $column.SqlTypeName == 'CHAR' || $column.SqlTypeName == 'LONGVARCHAR')
    public String get$column.variableName.substring(0, 1).toUpperCase()$column.variableName.substring(1)() {
        return this.$column.variableName;
    }
    public void set$column.variableName.substring(0, 1).toUpperCase()$column.variableName.substring(1)(String $column.variableName) {
        this.$column.variableName = $column.variableName;
    }
#end
#if($column.SqlTypeName == 'INTEGER' || $column.SqlTypeName == 'INT')
    public Integer get$column.variableName.substring(0, 1).toUpperCase()$column.variableName.substring(1)() {
        return this.$column.variableName;
    }
    public void set$column.variableName.substring(0, 1).toUpperCase()$column.variableName.substring(1)(Integer $column.variableName) {
        this.$column.variableName = $column.variableName;
    }
#end
#if($column.SqlTypeName == 'BIT' || $column.SqlTypeName == 'BOOLEAN')
    public Boolean get$column.variableName.substring(0, 1).toUpperCase()$column.variableName.substring(1)() {
        return this.$column.variableName;
    }
    public void set$column.variableName.substring(0, 1).toUpperCase()$column.variableName.substring(1)(Boolean $column.variableName) {
        this.$column.variableName = $column.variableName;
    }
#end
#if($column.SqlTypeName == 'TINYINT')
    public byte get$column.variableName.substring(0, 1).toUpperCase()$column.variableName.substring(1)() {
        return this.$column.variableName;
    }
    public void set$column.variableName.substring(0, 1).toUpperCase()$column.variableName.substring(1)(byte $column.variableName) {
        this.$column.variableName = $column.variableName;
    }
#end
#if($column.SqlTypeName == 'SMALLINT')
    public Short get$column.variableName.substring(0, 1).toUpperCase()$column.variableName.substring(1)() {
        return this.$column.variableName;
    }
    public void set$column.variableName.substring(0, 1).toUpperCase()$column.variableName.substring(1)(Short $column.variableName) {
        this.$column.variableName = $column.variableName;
    }
#end
#if($column.SqlTypeName == 'BIGINT')
    public Long get$column.variableName.substring(0, 1).toUpperCase()$column.variableName.substring(1)() {
        return this.$column.variableName;
    }
    public void set$column.variableName.substring(0, 1).toUpperCase()$column.variableName.substring(1)(Long $column.variableName) {
        this.$column.variableName = $column.variableName;
    }
#end
#if($column.SqlTypeName == 'REAL')
    public Float get$column.variableName.substring(0, 1).toUpperCase()$column.variableName.substring(1)() {
        return this.$column.variableName;
    }
    public void set$column.variableName.substring(0, 1).toUpperCase()$column.variableName.substring(1)(Float $column.variableName) {
        this.$column.variableName = $column.variableName;
    }
#end
#if($column.SqlTypeName == 'FLOAT' || $column.SqlTypeName == 'DOUBLE')
    public Double get$column.variableName.substring(0, 1).toUpperCase()$column.variableName.substring(1)() {
        return this.$column.variableName;
    }
    public void set$column.variableName.substring(0, 1).toUpperCase()$column.variableName.substring(1)(Double $column.variableName) {
        this.$column.variableName = $column.variableName;
    }
#end
#if($column.SqlTypeName == 'BINARY' || $column.SqlTypeName == 'VARBINARY' || $column.SqlTypeName == 'LONGVARBINARY')
    public byte[] get$column.variableName.substring(0, 1).toUpperCase()$column.variableName.substring(1)() {
        return this.$column.variableName;
    }
    public void set$column.variableName.substring(0, 1).toUpperCase()$column.variableName.substring(1)(byte[] $column.variableName) {
        this.$column.variableName = $column.variableName;
    }
#end
#if($column.SqlTypeName == 'DATE' || $column.SqlTypeName == 'TIME' || $column.SqlTypeName == 'TIMESTAMP' || $column.SqlTypeName == 'DATETIME')
    public Date get$column.variableName.substring(0, 1).toUpperCase()$column.variableName.substring(1)() {
        return this.$column.variableName;
    }
    public void set$column.variableName.substring(0, 1).toUpperCase()$column.variableName.substring(1)(Date $column.variableName) {
        this.$column.variableName = $column.variableName;
    }
#end
#if($column.SqlTypeName == 'DECIMAL' || $column.SqlTypeName == 'NUMERIC')
    public BigDecimal get$column.variableName.substring(0, 1).toUpperCase()$column.variableName.substring(1)() {
        return this.$column.variableName;
    }
    public void set$column.variableName.substring(0, 1).toUpperCase()$column.variableName.substring(1)(BigDecimal $column.variableName) {
        this.$column.variableName = $column.variableName;
    }
#end
#end
}