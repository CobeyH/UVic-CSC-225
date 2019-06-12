/* HullBuilder.java
   CSC 225 - Summer 2019

   Starter code for Convex Hull Builder. Do not change the signatures
   of any of the methods below (you may add other methods as needed).

   B. Bird - 03/18/2019
   (Add your name/studentID/date here)
*/

import java.util.LinkedList;

public class HullBuilder{

    /* Add constructors as needed */

    /* addPoint(P)
       Add the point P to the internal point set for this object.
       Note that there is no facility to delete points (other than
       destroying the HullBuilder and creating a new one). 
    */
    public void addPoint(Point2d P){
        
    }

    /* getHull()
       Return a java.util.LinkedList object containing the points
       in the convex hull, in order (such that iterating over the list
       will produce the same ordering of vertices as walking around the 
       polygon).
    */
    public LinkedList<Point2d> getHull(){
        return null;
    }

    /* isInsideHull(P)
       Given an point P, return true if P lies inside the convex hull
       of the current point set (note that P may not be part of the
       current point set). Return false otherwise.
     */
    public boolean isInsideHull(Point2d P){
        return false;
    }
}