import com.thinking.machines.TMORMFramework.*;
import com.thinking.machines.TMORMFramework.annotations.*;
import com.thinking.machines.TMORMFramework.exceptions.*;
import java.lang.reflect.*;
import java.util.*;
class psp
{
public static void main(String gg[])
{
try
{
TMDB tmdb=TMDB.getInstance();
/*Student s=new Student();
s.setRollNumber(102);
s.setName("prachi");
s.setCity("indore");
s.setSchool("St.marys");
tmdb.save(s);*/
//tmdb.update(s);
List<Student> students=tmdb.select("Student").orderBy("city").ascending().query();
for(Student s:students)
{
System.out.println(s.getRollNumber()+","+s.getName()+","+s.getCity()+","+s.getSchool());
}

//tmdb.select("Student").orderBy("city").descending().orderBy("school").ascending().query();
//tmdb.select("Student").where("city").eq("ujjain").orderBy("name").query();
//tmdb.select("Student").where("rollNumber").eq(1000).and("city").descending().ne("ujjain").query();
//tmdb.remove(101,Student.class);
}catch(Exception e)
{
System.out.println(e.getMessage());
}
}
}