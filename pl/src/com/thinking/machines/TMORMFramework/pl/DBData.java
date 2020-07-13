package com.thinking.machines.TMORMFramework.pl;
public class DBData implements java.io.Serializable
{
private String connectionURL;
private String username;
private String password;
public DBData()
{
this.connectionURL="";
this.username="";
this.password="";
}
public void setConnectionURL(String connectionURL)
{
this.connectionURL=connectionURL;
}
public String getConnectionURL()
{
return this.connectionURL;
}
public void setUsername(String username)
{
this.username=username;
}
public String getUsername()
{
return this.username;
}
public void setPassword(String password)
{
this.password=password;
}
public String getPassword()
{
return this.password;
}
}