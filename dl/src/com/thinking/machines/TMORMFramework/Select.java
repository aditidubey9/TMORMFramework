package com.thinking.machines.TMORMFramework;
import com.thinking.machines.TMORMFramework.*;
import com.thinking.machines.TMORMFramework.annotations.*;
import com.thinking.machines.TMORMFramework.exceptions.*;
import java.lang.annotation.*;
import java.lang.reflect.*;
import java.io.*;
import com.google.gson.*;
import java.sql.*;
import java.util.*;
public class Select
{
public Map<String,FieldInfo> columnInfoMap=null;
public String tableName="";
public String query="";
public Class cl=null;
public Connection connection=null;
public boolean orderByInvoked=false;
public boolean orderTypeInvoked=false;
public boolean logicalOperatorInvoked=false;
public boolean whereInvoked=false;
public boolean operatorInvoked=false;
public List<Object> values=null;

public Select(String tableName,Map<String,FieldInfo> columnInfoMap,Connection connection,Class cl)
{
this.tableName=tableName;
this.columnInfoMap=columnInfoMap;
this.query="select * from " +tableName;
this.cl=cl;
this.connection=connection;
this.values=new LinkedList<>();
}

public List query()
{
List<Object> targetObjects=new LinkedList<>();
try
{
System.out.println(this.query);
PreparedStatement ps=this.connection.prepareStatement(this.query);
int i=1;
int j=0;
while(j<this.values.size())
{
ps.setObject(i,this.values.get(j));
i++;
j++;
}
Object targetObject=null;
ResultSet rs=null;
Object h=null;
Object value=null;
Method method=null;
rs=ps.executeQuery();

while(rs.next())
{
targetObject=this.cl.newInstance();
for(FieldInfo fi:columnInfoMap.values())
{
value=rs.getObject(fi.variableName);
method=fi.setMethod;
h=method.invoke(targetObject,value);
}
targetObjects.add(targetObject);
}
}catch(Exception e)
{


}
return targetObjects;

}

public Select orderBy(String columnName)
{
try
{
if(this.whereInvoked==true)
{
throw new TMORMException("orderBy method cannot be applied here");
}
else
{
if(orderByInvoked==true)
{
this.query=this.query+","+(columnInfoMap.get(columnName)).variableName;
this.orderTypeInvoked=false;
}
else
{
this.query=this.query+" order by "+(columnInfoMap.get(columnName)).variableName;
this.orderByInvoked=true;
}
}
}catch(Exception e)
{
}
return this;
}

public Select descending()
{
try
{
if(this.orderTypeInvoked==true || this.logicalOperatorInvoked==true)
{
throw new TMORMException("descending method cannot be applied here");
}
else
{
if(this.operatorInvoked==true || this.orderByInvoked==true)
{
this.query=this.query+" desc";
this.orderTypeInvoked=true;
}
else
{
throw new TMORMException("descending method cannot be applied here");
}
}
}catch(Exception e)
{
}
return this;
}

public Select ascending()
{
try
{
if(this.orderTypeInvoked==true || this.logicalOperatorInvoked==true)
{

throw new TMORMException("ascending method cannot be applied here");
}
else
{
if(this.operatorInvoked==true || this.orderByInvoked==true)
{
this.query=this.query+" asc";
this.orderTypeInvoked=true;
}
else
{
throw new TMORMException("ascending method cannot be applied here");
}
}
}catch(Exception e)
{
}
return this;
}

public Select where(String columnName)
{
try
{
if(orderByInvoked==true || whereInvoked==true)
{
throw new TMORMException("where method cannot be applied here");
}
else
{
this.query=this.query+ " where "+(columnInfoMap.get(columnName)).variableName;
this.whereInvoked=true;
}
}catch(Exception e)
{
}
return this;
}

public Select eq(Object value)
{
try
{
if(operatorInvoked==true)
{
throw new TMORMException("eq method cannot be applied here");
}
else
{
if(whereInvoked==true || logicalOperatorInvoked==true)
{
this.values.add(value);
this.query=this.query+"=?";
this.operatorInvoked=true;
}
else
{
throw new TMORMException("eq method cannot be applied here");
}
}
}catch(Exception e)
{
}
return this;
}

public Select gt(Object value)
{
try
{
if(operatorInvoked==true)
{
throw new TMORMException("gt method cannot be applied here");
}
else
{
if(whereInvoked==true || logicalOperatorInvoked==true)
{
this.values.add(value);
this.query=this.query+">?";
this.operatorInvoked=true;
}
else
{
throw new TMORMException("gt method cannot be applied here");
}
}
}catch(Exception e)
{
}
return this;
}

public Select lt(Object value)
{
try
{
if(operatorInvoked==true)
{
throw new TMORMException("lt method cannot be applied here");
}
else
{
if(whereInvoked==true || logicalOperatorInvoked==true)
{
this.values.add(value);
this.query=this.query+"<?";
this.operatorInvoked=true;
}
else
{
throw new TMORMException("lt method cannot be applied here");
}
}
}catch(Exception e)
{
}
return this;
}

public Select ne(Object value)
{
try
{
if(operatorInvoked==true)
{
throw new TMORMException("ne method cannot be applied here");
}
else
{
if(whereInvoked==true || logicalOperatorInvoked==true)
{
this.values.add(value);
this.query=this.query+"<>?";
this.operatorInvoked=true;
}
else
{
throw new TMORMException("ne method cannot be applied here");
}
}
}catch(Exception e)
{
}
return this;
}

public Select ge(Object value)
{
try
{
if(operatorInvoked==true)
{
throw new TMORMException("ge method cannot be applied here");
}
else
{
if(whereInvoked==true || logicalOperatorInvoked==true)
{
this.values.add(value);
this.query=this.query+">=?";
this.operatorInvoked=true;
}
else
{
throw new TMORMException("ge method cannot be applied here");
}
}
}catch(Exception e)
{
}
return this;
}

public Select le(Object value)
{
try
{
if(operatorInvoked==true)
{
throw new TMORMException("le method cannot be applied here");
}
else
{
if(whereInvoked==true || logicalOperatorInvoked==true)
{
this.values.add(value);
this.query=this.query+"<=?";
this.operatorInvoked=true;
}
else
{
throw new TMORMException("le method cannot be applied here");
}
}
}catch(Exception e)
{
}
return this;
}

public Select or(String columnName)
{
try
{
if(whereInvoked==false || logicalOperatorInvoked==true)
{
throw new TMORMException("or method cannot be applied here");
}
else
{
this.query=this.query+" or "+(columnInfoMap.get(columnName)).variableName;
this.logicalOperatorInvoked=true;
this.operatorInvoked=false;
}
}catch(Exception e)
{
}
return this;
}

public Select and(String columnName)
{
try
{
if(whereInvoked==false || logicalOperatorInvoked==true)
{
throw new TMORMException("and method cannot be applied here");
}
else
{
this.query=this.query+" and "+(columnInfoMap.get(columnName)).variableName;
this.logicalOperatorInvoked=true;
this.operatorInvoked=false;
}
}catch(Exception e)
{
}
return this;
}
}