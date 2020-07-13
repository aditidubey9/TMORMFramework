package com.thinking.machines.TMORMFramework;
public class Attribute
{
public String columnName="";
public String columnType="";
public int size=0;
public boolean isPrimaryKey=false;
public boolean isForeignKey=false;
public boolean isNullable=false;
public boolean isUnique=false;
public boolean isAutoIncrement=false;
public String foreignKeyTable="";
public String foreignKeyName="";
}