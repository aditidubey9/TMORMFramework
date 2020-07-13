package com.thinking.machines.TMORMFramework;
import com.thinking.machines.TMORMFramework.annotations.*;
import com.thinking.machines.TMORMFramework.exceptions.*;
import com.thinking.machines.TMORMFramework.*;
import java.lang.annotation.*;
import java.lang.reflect.*;
import java.io.*;
import com.google.gson.*;
import java.sql.*;
import java.util.*;

public class TMDB
{
public static  List<Tables> tablesList=new LinkedList<>();
public static List<Tables> viewsList=new LinkedList<>();
public static Map<String,FieldInfo> fieldInfoMap=new HashMap<>();
public static Connection connection=null;
public void insertValidator(Object value,Attribute attribute,String tableName)
{
try
{
String query="";
Statement statement=connection.createStatement();
ResultSet rs=null;
if(attribute.isAutoIncrement==true)
{
return;
}
if(attribute.isPrimaryKey==true)
{
query="select * from "+tableName+" where "+attribute.columnName+"="+value;
rs=statement.executeQuery(query);
if(rs.next())
{
throw new TMORMException("Primary key should be unique.Find duplicate value for column="+attribute.columnName+",Value="+value);
}
}
if(attribute.isNullable==false)
{
if((String.valueOf(value)).trim().length()==0 || value==null)
{
throw new TMORMException("value cant be empty for column="+attribute.columnName+" with not null constraint");
}
}
if(attribute.isUnique==true)
{
query="select * from "+tableName+" where "+attribute.columnName+"="+value;
rs=statement.executeQuery(query);
if(rs.next())
{
throw new TMORMException("Duplicate entry value must be unique.\nColumnName:"+attribute.columnName+"\nValue provided: "+value);
}
}
if(attribute.isForeignKey==true)
{
query="select * from "+attribute.foreignKeyTable+" where "+attribute.foreignKeyName+"="+value;
if(!rs.next())
{
throw new TMORMException("The value you want to insert in column="+attribute.columnName+" of table "+tableName+" should be available in column="+attribute.foreignKeyName+" of table "+attribute.foreignKeyTable+" you provide value="+value);
}
}
}catch(Exception es)
{
}
}

public void updateValidator(Object value,Attribute attribute,String tableName)
{
try
{
String query="";
Statement statement=connection.createStatement();
ResultSet rs=null;
if(attribute.isPrimaryKey==true)
{
query="select * from "+tableName+" where "+attribute.columnName+"="+value;
rs=statement.executeQuery(query);
if(!rs.next())
{
throw new TMORMException("Primary key does not exists in the table");
}
}
if(attribute.isAutoIncrement==true)
{
return;
}
if(attribute.isNullable==false)
{
if((String.valueOf(value)).trim().length()==0 || value==null)
{
throw new TMORMException("value cant be empty for column="+attribute.columnName+" with not null constraint");
}
}
if(attribute.isUnique==true)
{
query="select * from "+tableName+" where "+attribute.columnName+"="+value;
rs=statement.executeQuery(query);
if(rs.next())
{
throw new TMORMException("Duplicate entry value must be unique.\nColumnName:"+attribute.columnName+"\nValue provided: "+value);
}
}
if(attribute.isForeignKey==true)
{
query="select * from "+attribute.foreignKeyTable+" where "+attribute.foreignKeyName+"="+value;
if(!rs.next())
{
throw new TMORMException("The value you want to insert in column="+attribute.columnName+" of table "+tableName+" should be available in column="+attribute.foreignKeyName+" of table "+attribute.foreignKeyTable+" you provide value="+value);
}
}
}catch(Exception es)
{
}
}

public void deleteValidator(Object value,String primaryKeyColumn,String tableName)
{
try
{
String query="";
Statement statement=connection.createStatement();
ResultSet rs=null;
query="select * from "+tableName+" where "+primaryKeyColumn+"="+value;
rs=statement.executeQuery(query);
if(!rs.next())
{
throw new TMORMException("Primary key does not exists in the table");
}
}catch(Exception es)
{
}
}

public void save(Object object)
{
try
{
Table t=null;
Column c=null;
PrimaryKey pk=null;
AutoIncrement ai=null;
String tableName="";
String columnName="";
String methodName="";
String variable="";
boolean isPrimaryKey=false;
boolean isAutoIncrement=false;
FieldInfo fi=null;
Class cl=object.getClass();
Method method=null;
View v=null;
v=(View)cl.getAnnotation(View.class);
if(v!=null)
{
return;
}
t=(Table)cl.getAnnotation(Table.class);  
if(t!=null)
{
System.out.println(t.value());
tableName=t.value();
Field[] fields=cl.getDeclaredFields();
for(Field field:fields)
{
c=(Column)field.getAnnotation(Column.class);  
if(c!=null)
{
System.out.println(c.value());
columnName=c.value();
fi=new FieldInfo();
fi.variableName=field.getName();
fi.dataType=(field.getType()).getName();

System.out.println(fi.variableName);
System.out.println(fi.dataType);

pk=(PrimaryKey)field.getAnnotation(PrimaryKey.class);
if(pk!=null)
{
fi.isPrimary=true;
}  
System.out.println(fi.isPrimary);
ai=(AutoIncrement)field.getAnnotation(AutoIncrement.class);  
if(ai!=null)
{
fi.isAutoIncrement=true;
}
System.out.println(fi.isAutoIncrement);

variable=field.getName();
methodName="get"+(variable).substring(0,1).toUpperCase()+variable.substring(1);
System.out.println(methodName);

method=cl.getMethod(methodName);
fi.getMethod=method;
methodName="set"+(variable).substring(0,1).toUpperCase()+variable.substring(1);

System.out.println(methodName);

method=cl.getMethod(methodName,field.getType());
fi.setMethod=method;
fieldInfoMap.put(columnName,fi);
}
}
}
List<Attribute> attributes=null;
int i=0;
while(i<tablesList.size())
{
if(((tablesList.get(i)).tableName).equalsIgnoreCase(tableName))
{
attributes=(tablesList.get(i)).attributes;
break;
}
i++;
}
String m="";
Object h=null;
System.out.println((tablesList.get(i)).insert);
PreparedStatement ps=connection.prepareStatement((tablesList.get(i)).insert);
fi=new FieldInfo();
i=1;
Object primaryKey=null;
for(Attribute attribute:attributes)
{
fi=fieldInfoMap.get(attribute.columnName);
method=fi.getMethod;
h=method.invoke(object);

insertValidator(h,attribute,tableName);

if((attribute.isAutoIncrement)==true)
{
continue;
}
ps.setObject(i,h);

i++;
}
ps.executeUpdate();
ps.close();
connection.close();
}catch(Exception e)
{
}
}

public void update(Object object)
{
try
{
Table t=null;
Column c=null;
PrimaryKey pk=null;
AutoIncrement ai=null;
String tableName="";
String columnName="";
String methodName="";
String variable="";
boolean isPrimaryKey=false;
boolean isAutoIncrement=false;
FieldInfo fi=null;
Class cl=object.getClass();
Method method=null;
View v=null;
v=(View)cl.getAnnotation(View.class);
if(v!=null)
{
return;
}
t=(Table)cl.getAnnotation(Table.class);  
if(t!=null)
{
System.out.println(t.value());
tableName=t.value();
Field[] fields=cl.getDeclaredFields();
for(Field field:fields)
{
c=(Column)field.getAnnotation(Column.class);  
if(c!=null)
{
System.out.println(c.value());
columnName=c.value();
fi=new FieldInfo();
fi.variableName=field.getName();
fi.dataType=(field.getType()).getName();

System.out.println(fi.variableName);
System.out.println(fi.dataType);

pk=(PrimaryKey)field.getAnnotation(PrimaryKey.class);
if(pk!=null)
{
fi.isPrimary=true;
}  
System.out.println(fi.isPrimary);
ai=(AutoIncrement)field.getAnnotation(AutoIncrement.class);  
if(ai!=null)
{
fi.isAutoIncrement=true;
}
System.out.println(fi.isAutoIncrement);

variable=field.getName();
methodName="get"+(variable).substring(0,1).toUpperCase()+variable.substring(1);
System.out.println(methodName);

method=cl.getMethod(methodName);
fi.getMethod=method;
methodName="set"+(variable).substring(0,1).toUpperCase()+variable.substring(1);

System.out.println(methodName);

method=cl.getMethod(methodName,field.getType());
fi.setMethod=method;
fieldInfoMap.put(columnName,fi);
}
}
}
List<Attribute> attributes=null;
int i=0;
while(i<tablesList.size())
{
if(((tablesList.get(i)).tableName).equalsIgnoreCase(tableName))
{
attributes=(tablesList.get(i)).attributes;
break;
}
i++;
}
String m="";
Object h=null;

System.out.println((tablesList.get(i)).update);

PreparedStatement ps=connection.prepareStatement((tablesList.get(i)).update);
Object primaryKey=null;
fi=new FieldInfo();
i=1;
for(Attribute attribute:attributes)
{
fi=fieldInfoMap.get(attribute.columnName);

method=fi.getMethod;
h=method.invoke(object);

updateValidator(h,attribute,tableName);

if((attribute.isPrimaryKey)==true)
{
primaryKey=h;
continue;
}
if((attribute.isAutoIncrement)==true)
{
continue;
}

ps.setObject(i,h);

i++;
}
ps.setObject(i,primaryKey);
ps.executeUpdate();
ps.close();
connection.close();
}catch(Exception e)
{
}
}

public void remove(Object obj,Class cl)
{
try
{
Table t=null;
View v=null;
String tableName="";
v=(View)cl.getAnnotation(View.class);
if(v!=null)
{
return;
}
t=(Table)cl.getAnnotation(Table.class);  
if(t!=null)
{
System.out.println(t.value());
tableName=t.value();
}
String primaryKeyColumn="";
int i=0;
while(i<tablesList.size())
{
if(((tablesList.get(i)).tableName).equalsIgnoreCase(tableName))
{
primaryKeyColumn=(tablesList.get(i)).primaryKeyColumn;
break;
}
i++;
}
Object h=obj;

System.out.println((tablesList.get(i)).delete);

PreparedStatement ps=connection.prepareStatement((tablesList.get(i)).delete);

deleteValidator(h,primaryKeyColumn,tableName);

ps.setObject(1,h);
ps.executeUpdate();
ps.close();
connection.close();
}catch(Exception e)
{
}
}

public Select select(String className)
{
Select s=null;
try
{
Table t=null;
Column c=null;
String tableName="";
String columnName="";
String variable="";
PrimaryKey pk=null;
AutoIncrement ai=null;
String methodName="";
boolean isPrimaryKey=false;
boolean isAutoIncrement=false;
Method method=null;

FieldInfo fi=null;
Map<String,FieldInfo> columnInfoMap=new HashMap<>();
int i=0;
boolean found=false;
Class cl=Class.forName(className);
View v=null;
v=(View)cl.getAnnotation(View.class);
if(v!=null)
{
tableName=v.value();
i=0;
while(i<viewsList.size())
{
if(((viewsList.get(i)).tableName).equalsIgnoreCase(tableName))
{
found=true;
break;
}
i++;
}
}
else
{
t=(Table)cl.getAnnotation(Table.class);  
if(t!=null)
{
tableName=t.value();
}
i=0;
while(i<tablesList.size())
{
if(((tablesList.get(i)).tableName).equalsIgnoreCase(tableName))
{
found=true;
break;
}
i++;
}
}
if(found==false)
{
throw new TMORMException(tableName+" does not exists");
}
Field[] fields=cl.getDeclaredFields();
for(Field field:fields)
{
c=(Column)field.getAnnotation(Column.class);  
if(c!=null)
{
columnName=field.getName();
fi=new FieldInfo();
fi.variableName=c.value();
fi.dataType=(field.getType()).getName();

pk=(PrimaryKey)field.getAnnotation(PrimaryKey.class);
if(pk!=null)
{
fi.isPrimary=true;
}  
ai=(AutoIncrement)field.getAnnotation(AutoIncrement.class);  
if(ai!=null)
{
fi.isAutoIncrement=true;
}
variable=field.getName();
methodName="get"+(variable).substring(0,1).toUpperCase()+variable.substring(1);

method=cl.getMethod(methodName);
fi.getMethod=method;
methodName="set"+(variable).substring(0,1).toUpperCase()+variable.substring(1);

method=cl.getMethod(methodName,field.getType());
fi.setMethod=method;
columnInfoMap.put(columnName,fi);
}
}
s=new Select(tableName,columnInfoMap,connection,cl);
}catch(Exception es)
{
es.printStackTrace();
}
return s;
}
public static TMDB getInstance()
{
try
{
Gson g=new Gson();
String jsonFilePath="c:/TMORMFramework/dbconf.json";
FileReader fr=new FileReader(new File(jsonFilePath));
JsonObject jo=g.fromJson(fr,JsonObject.class);
String connectionURL=jo.get("connectionURL").getAsString();
String username=jo.get("username").getAsString();
String password=jo.get("password").getAsString();
Class.forName("com.mysql.cj.jdbc.Driver");
connection=DriverManager.getConnection(connectionURL,username,password);
DatabaseMetaData metadata= connection.getMetaData();
String catalog = null;
String schemaPattern = null;
String tableNamePattern = null;
String columnNamePattern = null;
String[] types={"TABLE"}; 
String tableName="";
String insert="";
String update="";
String delete="";
String select="";
String columnName="";
String columnType="";
String isNullable="";
String isAutoIncrement="";
String primaryKeyColumn="";
String foreignKeyColumn="";
String foreignKeyTable="";
String foreignKeyName="";
int columnSize=0;
ResultSet rsColumns=null;
ResultSet rsPrimarykey=null;
ResultSet rsForeignkey=null;
ResultSet rsUniqueColumn=null;
ForeignKeyDetails foreignKeyDetails=null;
Attribute attribute=null;
Tables table=null;
List<Attribute> attributes=null;
Set<String> uniqueColumnSet=null;
Map<String,ForeignKeyDetails> foreignKeyMap=null;
ResultSet rs=metadata.getTables(catalog,schemaPattern,tableNamePattern,types);
while(rs.next())
{
table=new Tables();
tableName=rs.getString("TABLE_NAME");
System.out.println("Table name:"+tableName);
table.tableName=tableName;
rsColumns = metadata.getColumns(catalog,schemaPattern,tableName,columnNamePattern);
rsPrimarykey = metadata.getPrimaryKeys(catalog, schemaPattern, tableName);
rsForeignkey= metadata.getImportedKeys(catalog,schemaPattern, tableName);
rsUniqueColumn=metadata.getIndexInfo(null,null,tableName,true,false);
uniqueColumnSet=new HashSet<>();
foreignKeyMap=new HashMap<>();
attributes=new LinkedList<>();
while(rsUniqueColumn.next())
{
uniqueColumnSet.add(rsUniqueColumn.getString("COLUMN_NAME"));
}
while(rsPrimarykey.next())
{
primaryKeyColumn=rsPrimarykey.getString("COLUMN_NAME");
}
while(rsForeignkey.next())
{
foreignKeyColumn=rsForeignkey.getString("FKCOLUMN_NAME");
foreignKeyTable=rsForeignkey.getString("PKTABLE_NAME");
foreignKeyName=rsForeignkey.getString("PKCOLUMN_NAME");
foreignKeyDetails=new ForeignKeyDetails();
foreignKeyDetails.foreignKeyTable=foreignKeyTable;
foreignKeyDetails.foreignKeyName=foreignKeyName;
foreignKeyMap.put(foreignKeyColumn,foreignKeyDetails);
}
while(rsColumns.next())
{
attribute=new Attribute();
columnName=rsColumns.getString("COLUMN_NAME");
columnType=rsColumns.getString("TYPE_NAME");
columnSize=rsColumns.getInt("COLUMN_SIZE");
isNullable=rsColumns.getString("IS_NULLABLE");
isAutoIncrement=rsColumns.getString("IS_AUTOINCREMENT");

if(primaryKeyColumn.equalsIgnoreCase(columnName))
{
attribute.isPrimaryKey=true;
}
if(uniqueColumnSet.contains(columnName))
{
attribute.isUnique=true;
}
if(foreignKeyMap.containsKey(columnName))
{
attribute.isForeignKey=false;
attribute.foreignKeyTable=(foreignKeyMap.get(columnName)).foreignKeyTable;
attribute.foreignKeyName=(foreignKeyMap.get(columnName)).foreignKeyName;
}
if(isNullable.equalsIgnoreCase("yes"))
{
attribute.isNullable=true;
}
if(isAutoIncrement.equalsIgnoreCase("yes"))
{
attribute.isAutoIncrement=true;
}

attribute.columnName=columnName;
attribute.columnType=columnType;
attribute.size=columnSize;
attributes.add(attribute);
}
table.primaryKeyColumn=primaryKeyColumn;
table.attributes=attributes;
int i=0;
select="select * from "+table.tableName;
delete="delete from "+table.tableName+" where "+table.primaryKeyColumn+"=?";
update="update "+table.tableName+" set ";
String temp="values(";
insert="insert into "+table.tableName+" (";
while(i<attributes.size())
{
if((attributes.get(i).isAutoIncrement)==true)
{
i++;
continue;
}
if((attributes.get(i).isPrimaryKey)==false)
{
update=update+(attributes.get(i)).columnName+"=?";
}
insert=insert+(attributes.get(i)).columnName;
temp=temp+"?";
i++;
if(i<attributes.size())
{
insert=insert+",";
if((attributes.get(i-1).isPrimaryKey)==false)
{
update=update+",";
}
temp=temp+",";
}
if(i==attributes.size())
{
insert=insert+") ";
temp=temp+")";
insert=insert+temp;
update+=" where "+table.primaryKeyColumn+"=?";
}
}
table.insert=insert;
table.select=select;
table.delete=delete;
table.update=update;
tablesList.add(table);
//System.out.println(table.insert);
//System.out.println(table.select);
//System.out.println(table.delete);
//System.out.println(table.update);
}
rs.close();
rs=metadata.getTables(catalog,"tmdb",tableNamePattern,new String[]{"VIEW"});
while(rs.next())
{
table=new Tables();
tableName=rs.getString("TABLE_NAME");
System.out.println("View name:"+tableName);
table.tableName=tableName;
rsColumns = metadata.getColumns(catalog,schemaPattern,tableName,columnNamePattern);
attributes=new LinkedList<>();
while(rsColumns.next())
{
attribute=new Attribute();
columnName=rsColumns.getString("COLUMN_NAME");
columnType=rsColumns.getString("TYPE_NAME");
columnSize=rsColumns.getInt("COLUMN_SIZE");
isNullable=rsColumns.getString("IS_NULLABLE");
isAutoIncrement=rsColumns.getString("IS_AUTOINCREMENT");
attribute.columnName=columnName;
attribute.columnType=columnType;
attribute.size=columnSize;
attributes.add(attribute);
}
table.select="select * from "+tableName;
table.attributes=attributes;
viewsList.add(table);
}
}catch(Exception e)
{
e.printStackTrace();
}
return new TMDB();
}
}