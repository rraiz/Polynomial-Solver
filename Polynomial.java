import java.util.*;
import java.io.*;

public class Polynomial
{
   private List<Double> nums;
   private TreeSet<Double> roots;
   private double bounds;
   private static final double ALMOST_ZERO = 0.000001;


   public Polynomial(String x)
   {
      nums = new ArrayList<Double>();
      roots = new TreeSet<Double>();
      nums = buildNums(x);
      bounds = bisectBounds();
   }
  

   public  boolean sameSign (double x, double y)
   {
      if(x < 0 && y < 0)
         return true;
      if(x > 0 && y > 0)
         return true;
      if(x == 0 && y == 0)
         return true;
               
      return false;
   }

   public  double average(double x, double y)
   {
      return (x+y)/2;
   }

   public  double solve(double x)
   {
   
      if(nums.size() < 2)
      {
         System.out.println("No variables for number to pass through equation");
         return nums.get(0);
      }
      
      int degree = nums.size()-1;
      double answer = 0;
      
      for(int i = 0; i<nums.size(); i++)
      {
         answer = answer + Math.pow(x, degree) * nums.get(i);
         degree--;
      }
      return answer;
   }

   private double bisectBounds()
   {
      List<Double> absV = new ArrayList<Double>();
      for(int i = 0; i<nums.size(); i++)
         absV.add(Math.abs(nums.get(i))); 
         
      double first = absV.remove(0);
      double maxEle = 0;
      double sum = 0;
      
      for(int i = 0; i<absV.size(); i++)
      {
         absV.set(i, absV.get(i)/first);
         if(absV.get(i) > maxEle) 
            maxEle = absV.get(i);
            
         sum = sum + absV.get(i);  
      }
      
      if( Math.max(1,sum) <= (maxEle + 1))
         return Math.max(1,sum);
      
      else
         return maxEle+1;
      
   }



   public TreeSet<Double> findZeros()
   {
      
      if(nums.size()-1 == 0)
      {
         if(nums.get(0) == 0)
         {
            roots.add(0.0);
            return roots;
         }
         System.out.println("No real roots");
         return roots;  
      }
   
      if(nums.size()-1 == 2)
         return quadraticZeros();
      
      if(nums.size()-1 == 1)
         return linearZeros();
   
   
      bisectZeros(1);
      
      if(roots.size() != nums.size()-1)
         bisectZeros(0.5);
         
      if(roots.size() != nums.size()-1)
         bisectZeros(0.25);
         
      if(roots.size() != nums.size()-1)
         bisectZeros(0.125);
   
   
      return roots;
   }
   
   public  TreeSet<Double> bisectZeros(double j)
   {
   
      double i = -bounds;         
      
      while(i<bounds)
      {
         
         double p1 = i;
         double p2 = i+j;
                     
         if(sameSign(solve(p1),solve(p2)) == false)
         {
            double sol = binarySearch(i,p2);
            String round = "" + sol;
            if(round.substring(0, round.indexOf(".")+7) != null)
               round = round.substring(0, round.indexOf(".")+7);
            
            roots.add(Double.parseDouble(round));
         } 
         i = i+j;   
      }
         
      return roots;
      
   }
   
   public double binarySearch(double left, double right)
   {
      if(left > right)
         return -1;			//key not found in array
           
      double mid = (left+right)/2;	//find index in the middle of subarray
      
      if(solve(left) <= ALMOST_ZERO && solve(left) >= 0)
         return left;
      
      if(solve(right) <= ALMOST_ZERO && solve(right) >= 0)
         return right;
    
      if((solve(mid) <= ALMOST_ZERO && solve(mid) >= 0) || (solve(mid) >= ALMOST_ZERO && solve(mid) <= 0))
         return mid;		//we found it – return its index
      
           
      if(sameSign(solve(left), solve(mid)) == false)		
         return binarySearch(left, mid-0.0000000001);       //search in left side
           
      return binarySearch(mid+0.0000000001, right);            //seach in right side
   }  

   private TreeSet<Double> linearZeros()
   {
      double temp = 0-nums.get(1);
      temp = temp/nums.get(0);
      roots.add(temp);
       
      return roots;
   }

   private TreeSet<Double> quadraticZeros()
   {
      double sol = Math.pow(nums.get(1),2)-4*nums.get(0)*nums.get(2);
      if(sol < 0)
      {
         System.out.println("No real solutions");
         return roots;
      }
      
      double posAns = (-(nums.get(1)) + Math.sqrt(sol))/(2*nums.get(0));
      double negAns = (-(nums.get(1)) - Math.sqrt(sol))/(2*nums.get(0));
   
      roots.add(posAns);
      roots.add(negAns);
      return roots;
   }


   private List<Double> buildNums(String x)
   {
      List<Double> z = new ArrayList<Double>();
   
      int getDegree = getDegree(x);
      int currDegree;
      
      for(int i = 0; i<=getDegree; i++)
      {
         z.add(0.0);
      }
            
      while(!(x.equals("")))
      {
         if(x.indexOf("x") == -1) //just constant (5)
         {
            z.set(getDegree-getDegree(x), Double.parseDouble((x.substring(0,x.length()))));
            break;
         }
                     
         if((x.substring(0,1).equals("-") || x.substring(0,1).equals("+")) && x.indexOf("x") == -1) //-3 +3
         {
            if(x.substring(0,1).equals("-"))
            {
               z.set(getDegree-getDegree(x), Double.parseDouble(x.substring(0, x.length()))); //-3
            }
            else
               z.set(getDegree-getDegree(x), Double.parseDouble(x.substring(1, x.length()))); //+3
            break;
         }
         
         if(x.substring(0, x.indexOf("x")).equals("-") || x.substring(0, x.indexOf("x")).equals("") || x.substring(0, x.indexOf("x")).equals("+")) //-x^2, x^2, +x^2
         {
            if((x.substring(0, x.indexOf("x")).equals("-"))) 
            {
               z.set(getDegree-getDegree(x),-1.0); //-x
            }
            else
               z.set(getDegree-getDegree(x),1.0); //x
               
            if(x.indexOf("^") == -1)
               x = x.substring(x.indexOf("x")+1, x.length()); //+-x
            else   
               x = x.substring(x.indexOf("^")+2, x.length()); //+-x^2
            continue;   
         }
         
         if((x.substring(0,1).equals("-") || x.substring(0,1).equals("+")) && x.indexOf("x") != -1) //-5x^2 -5x +5x^2 +5x
         {
            if(x.substring(0,1).equals("-"))
               z.set(getDegree-getDegree(x), Double.parseDouble(x.substring(0, x.indexOf("x")))); //-5x
            else
               z.set(getDegree-getDegree(x), Double.parseDouble(x.substring(1, x.indexOf("x")))); //+5x
         
            if(x.indexOf("^") == -1)
               x = x.substring(x.indexOf("x")+1, x.length()); //+-5x
            else
               x = x.substring(x.indexOf("^")+2, x.length()); //+-5x^2    
            continue;
         }
            
         z.set(getDegree-getDegree(x), Double.parseDouble(x.substring(0, x.indexOf("x") ))); //5x^2 ( the beginning)
         if(x.indexOf("^") != -1)
            x = x.substring(x.indexOf("^")+2, x.length());
         else
            x = x.substring(x.indexOf("x")+1, x.length());     
      }       
      return z;
   }

   private int getDegree(String x)
   {
      if(x.indexOf("x") == -1)
         return 0;
         
      if(x.indexOf("^") == -1)
         return 1;
               
      return Integer.parseInt(x.substring(x.indexOf("^")+1, x.indexOf("^")+2));
   }

   public String toString()
   {
      return nums.toString();
   }


}