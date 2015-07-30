package com.wing.framework.mybatis;

import com.wing.framework.common.Page;
import com.wing.framework.mybatis.dialect.Dialect;
import com.wing.framework.mybatis.dialect.Dialect.Type;
import com.wing.framework.mybatis.dialect.MySql5Dialect;
import org.apache.ibatis.builder.xml.dynamic.ForEachSqlNode;
import org.apache.ibatis.executor.ErrorContext;
import org.apache.ibatis.executor.ExecutorException;
import org.apache.ibatis.executor.statement.StatementHandler;
import org.apache.ibatis.mapping.BoundSql;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.mapping.ParameterMapping;
import org.apache.ibatis.mapping.ParameterMode;
import org.apache.ibatis.plugin.*;
import org.apache.ibatis.reflection.MetaObject;
import org.apache.ibatis.reflection.property.PropertyTokenizer;
import org.apache.ibatis.session.Configuration;
import org.apache.ibatis.session.RowBounds;
import org.apache.ibatis.type.TypeHandler;
import org.apache.ibatis.type.TypeHandlerRegistry;
import org.apache.log4j.Logger;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Properties;

/**
 * 
 * 分页拦截器
 * 
 * @author panwb
 *
 */
@Intercepts({@Signature(type=StatementHandler.class,method="prepare",args={Connection.class})})
public class PaginationInterceptor implements Interceptor {
	
	//日志
	static Logger logger = Logger.getLogger(PaginationInterceptor.class.getName());
    
    @Override
    public Object intercept(Invocation invocation) throws Throwable {

       StatementHandler statementHandler = (StatementHandler)invocation.getTarget();
       BoundSql boundSql = statementHandler.getBoundSql();
       MetaObject metaStatementHandler = MetaObject.forObject(statementHandler);
       RowBounds rowBounds = (RowBounds)metaStatementHandler.getValue("delegate.rowBounds");

       if(rowBounds ==null|| rowBounds == RowBounds.DEFAULT){

           return invocation.proceed();
       }

       Configuration configuration = (Configuration)metaStatementHandler.getValue("delegate.configuration");
       Dialect.Type databaseType  =null;

       try{

           databaseType = Type.valueOf(configuration.getVariables().getProperty("dialect").toUpperCase());
       } catch(Exception e){

       }

       if(databaseType ==null){
           throw new RuntimeException("the value of the dialect property in configuration.xml is not defined : "+ configuration.getVariables().getProperty("dialect"));

       }

       Dialect dialect =null;

       switch(databaseType){

           case MYSQL:
              dialect =new MySql5Dialect();
       }
       
       MappedStatement mappedStatement = (MappedStatement) metaStatementHandler.getValue("delegate.mappedStatement");
       
       Object parameterObject = boundSql.getParameterObject();// 分页SQL<select>中parameterType属性对应的实体参数，即Mapper接口中执行分页方法的参数,该参数不得为空
       if (parameterObject == null) {
           throw new NullPointerException("parameterObject尚未实例化！");
       } else {
    	   
    	   String originalSql = (String)metaStatementHandler.getValue("delegate.boundSql.sql");
    	   
           Connection connection = (Connection) invocation.getArgs()[0];
           String countSql = dialect.getCountString(originalSql);
           PreparedStatement countStmt = connection.prepareStatement(countSql);
           BoundSql countBS = new BoundSql(mappedStatement.getConfiguration(), countSql, boundSql.getParameterMappings(), parameterObject);
           setParameters(countStmt, mappedStatement, countBS, parameterObject);
           ResultSet rs = countStmt.executeQuery();
           int count = 0;
           if (rs.next()) {
               count = rs.getInt(1);
           }
           rs.close();
           countStmt.close();
           
           Page page = null;
           if (parameterObject instanceof Page) { // 参数就是Page实体
               page = (Page) parameterObject;
               page.setTotalSize(count);
           } else {
        	   
        	   page = (Page) ((HashMap)parameterObject).get("page");
        	   
        	   //判断是否存在page字段
               if (page != null) {
                   page.setTotalSize(count);
                   ((HashMap)parameterObject).put("page", page);
               }
           }

	       metaStatementHandler.setValue("delegate.boundSql.sql", dialect.getLimitString(originalSql, rowBounds.getOffset(), rowBounds.getLimit()));
	       metaStatementHandler.setValue("delegate.rowBounds.offset", RowBounds.NO_ROW_OFFSET );
	       metaStatementHandler.setValue("delegate.rowBounds.limit", RowBounds.NO_ROW_LIMIT );
       }

       //判断是否记录debug日志
       if(logger.isDebugEnabled()){
    	   logger.debug("生成分页SQL : "+ boundSql.getSql());
       }
       
       return invocation.proceed();
    }
    
    private void setParameters(PreparedStatement ps,
            MappedStatement mappedStatement, BoundSql boundSql,
            Object parameterObject) throws SQLException {
        ErrorContext.instance().activity("setting parameters")
                .object(mappedStatement.getParameterMap().getId());
        List<ParameterMapping> parameterMappings = boundSql
                .getParameterMappings();
        if (parameterMappings != null) {
            Configuration configuration = mappedStatement.getConfiguration();
            TypeHandlerRegistry typeHandlerRegistry = configuration
                    .getTypeHandlerRegistry();
            MetaObject metaObject = parameterObject == null ? null
                    : configuration.newMetaObject(parameterObject);
            for (int i = 0; i < parameterMappings.size(); i++) {
                ParameterMapping parameterMapping = parameterMappings.get(i);
                if (parameterMapping.getMode() != ParameterMode.OUT) {
                    Object value;
                    String propertyName = parameterMapping.getProperty();
                    PropertyTokenizer prop = new PropertyTokenizer(propertyName);
                    if (parameterObject == null) {
                        value = null;
                    } else if (typeHandlerRegistry
                            .hasTypeHandler(parameterObject.getClass())) {
                        value = parameterObject;
                    } else if (boundSql.hasAdditionalParameter(propertyName)) {
                        value = boundSql.getAdditionalParameter(propertyName);
                    } else if (propertyName
                            .startsWith(ForEachSqlNode.ITEM_PREFIX)
                            && boundSql.hasAdditionalParameter(prop.getName())) {
                        value = boundSql.getAdditionalParameter(prop.getName());
                        if (value != null) {
                            value = configuration.newMetaObject(value)
                                    .getValue(
                                            propertyName.substring(prop
                                                    .getName().length()));
                        }
                    } else {
                        value = metaObject == null ? null : metaObject
                                .getValue(propertyName);
                    }
                    TypeHandler typeHandler = parameterMapping.getTypeHandler();
                    if (typeHandler == null) {
                        throw new ExecutorException(
                                "There was no TypeHandler found for parameter "
                                        + propertyName + " of statement "
                                        + mappedStatement.getId());
                    }
                    typeHandler.setParameter(ps, i + 1, value,
                            parameterMapping.getJdbcType());
                }
            }
        }
    }


    @Override
    public Object plugin(Object target) {

       return Plugin.wrap(target, this);
    }

    @Override
    public void setProperties(Properties properties) {

    }
}
