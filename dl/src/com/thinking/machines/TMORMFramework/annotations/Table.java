package com.thinking.machines.TMORMFramework.annotations;
import java.lang.annotation.*;
import java.lang.reflect.*;
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public@interface Table{
public String value();
}
