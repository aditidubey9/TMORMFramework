import com.thinking.machines.TMORMFramework.annotations.*;
import java.lang.annotation.*;
import java.lang.reflect.*;
@View(value="test_view")
public class Student
{
@Column(value="roll_number")
@PrimaryKey
private int rollNumber;
@Column(value="nm")
private String name;
@Column(value="ct")
private String city;
@Column(value="sch")
private String school;
public Student()
{
this.rollNumber=0;
this.name="";
this.city="";
this.school="";
}
public void setRollNumber(int rollNumber)
{
this.rollNumber=rollNumber;
}
public int getRollNumber()
{
return this.rollNumber;
}
public void setName(String name)
{
this.name=name;
}
public String getName()
{
return this.name;
}

public void setCity(String city)
{
this.city=city;
}
public String getCity()
{
return this.city;
}

public void setSchool(String school)
{
this.school=school;
}
public String getSchool()
{
return this.school;
}

}