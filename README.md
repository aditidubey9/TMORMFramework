# TMORMFramework
This framework is used to make programmer free from writing sql.
Currently this framework only support mysql.

# How to use
Thier are two ways to use the framework:

1) Using framework as a service in a program.
2) Using Framework as a tool with gui.

1)Using as a service in program:

a) You have to create a file named as dbconf.json in which you have to do following entries in it:
    
    {
      connectionURL:"jdbc:mysql://localhost:3306/your_db_name",
      username:"your_user_name",
      password:"your_password"
    }
