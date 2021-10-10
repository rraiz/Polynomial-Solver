import java.util.*;
import java.io.*;

public class  Bisection_Lab
{


   public static void main(String[] args)
   {
      String p1 = "16x^3-1000x+3";
   
      Polynomial nums1 = new Polynomial(p1);
   
   
      TreeSet x = nums1.findZeros();
   
      
      System.out.println(x);
   
   }
   

}
