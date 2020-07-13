package com.thinking.machines.TMORMFramework;
import java.lang.annotation.*;
import java.lang.reflect.*;

public class FieldInfo
{
public String variableName="";
public String dataType="";
public boolean isPrimary=false;
public boolean isAutoIncrement=false;
public Method setMethod=null;
public Method getMethod=null;
}