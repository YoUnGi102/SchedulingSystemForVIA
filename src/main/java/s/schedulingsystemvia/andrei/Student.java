package s.schedulingsystemvia.andrei;

public class Student
{
  final private String name;
  final private int VIA_number;
  final private String programme;

  public final static String LEGAL_PROGRAMME[] =
      {"Software Technology Engineering",
      "Architecture",
      "Global Business of Software Engineering",
      "Mechanical Engineering",
      "Business"};

  public Student(String name,int VIA_number,String programme)
  {
    this.name = name;

    if(100000 <= VIA_number && VIA_number <= 999999){
      this.VIA_number = VIA_number;
    }
    else{
      throw new IllegalArgumentException("Illegal VIA Number: " + VIA_number);
    }

    if(isLEGALPROGRAMME(programme))
    {
      this.programme = programme;
    }
    else
    {
      throw new IllegalArgumentException("Illegal Programme: " + programme);
    }
  }

  public String getName()
  {
   return name;
  }

  public int getVIA_number()
  {
    return VIA_number;
  }

  public String getProgramme()
  {
    return programme;
  }

  public static boolean isLEGALPROGRAMME(String programme)
  {

  for(int i = 0; i < LEGAL_PROGRAMME.length; i++)
  {
    if(programme.equals( LEGAL_PROGRAMME[i].toString()))
    {
      return true;
    }
  }
  return false;
}

public boolean equals(Object obj)
{
  if (!(obj instanceof Student))
  {
    return false;
  }
  Student other = (Student) obj;
  return programme.equals(programme) && VIA_number == other.VIA_number && name == other.name;
}
public String toString()
{
  return name + VIA_number + programme;
}
}
