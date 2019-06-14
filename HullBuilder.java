/* HullBuilder.java
   CSC 225 - Summer 2019

   Starter code for Convex Hull Builder. Do not change the signatures
   of any of the methods below (you may add other methods as needed).

   B. Bird - 03/18/2019
   (Add your name/studentID/date here)
   */

import java.util.LinkedList;
import java.util.ArrayList;
import java.util.Iterator;
//---------------------------------//
// My Own Linked List implementation
// --------------------------------//
class PointList {
    Node head;  // head of list 

    public void remove(Node P) {
        if(P == head) {
            head = head.next;
            head.prev = null;
        } else if(P.next == null) {
            P.prev.next = null;
        } else {
            P.prev.next = P.next;
            P.next.prev = P.prev;
        }
    }

    public void insert(Node curr, Node P) {
        curr.next = P;
        P.prev = curr;
    }

    public void print() {
        System.out.println("----------\nPrinting Hull\n-------------");
        int length = 1;
        Node curr = head;
        while(curr.hasNext()) {
            if(curr == head) {
                System.out.println("Curr: " + curr.data + " Next: " + curr.next.data);
            } else {
                System.out.println("Prev: " + curr.prev.data + " Curr: " + curr.data + " Next: " + curr.next.data);
            }
            curr = curr.next;
            length++;
        }
        System.out.println("Prev: " + curr.prev.data + " Curr: " + curr.data);
        System.out.println("Length " + length + "\n");

    }
}

class Node { 
    Point2d data; 
    Node next; 
    Node prev;
    Node(Point2d d)  { data = d;  next=null; prev=null;} 

    public boolean hasNext() {
        if(next != null) {
            return true;
        }
        return false;
    }

}

public class HullBuilder{

    public ArrayList<Point2d> upperHull = new ArrayList<Point2d>();
    private ArrayList<Point2d> lowerHull = new ArrayList<Point2d>();

    private PointList arrayToList(ArrayList<Point2d> array) {
        PointList list = new PointList();
        list.head = new Node(array.get(0)); 
        Node curr = list.head;
        for(int i = 1; i < array.size(); i++) {
            curr.next = new Node(array.get(i));
            curr.next.prev = curr;
            curr = curr.next;
        }
        return list;
    }

    //Adds P to list in the correct positon as to keep list in sorted order by x-coord
    private void addToMiddle(PointList list, Point2d P) {
        Node curr = list.head;
        Node prev = curr;
        Node toInsert = new Node(P);
        while(curr.hasNext()) {
            if(curr.data.x == P.x && curr.data.y == P.y){
                return;
            } //Duplicate points should not be added
            if(P.x < curr.data.x) {
                if(curr == list.head) {
                    list.head = toInsert;
                    toInsert.next = curr;
                } else {
                    list.insert(curr.prev, toInsert);
                }
                return;
            }
            prev = curr;
            curr = curr.next;
        }
        if(curr.data.x > P.x) {
            list.insert(curr.prev, toInsert);
        } else {
            list.insert(curr, toInsert);
        }
        return;
    }

    private ArrayList<Point2d> reassembleHull(PointList hull, int mode) {
        Node prev = hull.head;
        Node curr = prev.next;
        ArrayList<Point2d> newHull = new ArrayList<Point2d>();

        hull.print();

        while(curr.hasNext()) {
            int chir = Point2d.chirality(prev.data, curr.data, curr.next.data);
            if(mode == chir) {
                newHull.add(curr.next.data);
            } else {
                curr = curr.next;
                hull.remove(curr.prev);
                continue; 
            }
            prev = curr;
            curr = curr.next;
        }
        return newHull;
    }

    /* addPoint(P)
       Add the point P to the internal point set for this object.
       Note that there is no facility to delete points (other than
       destroying the HullBuilder and creating a new one). 
       */
    public void addPoint(Point2d P) {
        //If the hulls are empty just add the point
        if(upperHull.size() == 0) {
            upperHull.add(P);
            lowerHull.add(P);
            return;
        } else if(upperHull.size() == 1) { //If there is only 1 point in the list, you cannot make a turn
            if(P.x < upperHull.get(0).x) { //If it is to the left, shift over elements and add P at start
                upperHull.add(upperHull.get(0));
                lowerHull.add(lowerHull.get(0));
                upperHull.set(0, P);
                lowerHull.set(0, P);
            } else if(P.x > upperHull.get(0).x) { //If P is the the right, just add it to the end
                upperHull.add(P);
                lowerHull.add(P);
            }
            return; //If P is already in list, don't add anything
        } //This is all constant time because it will deal with at most 2 elements.

        //Create new linked lists to copy arrays into (for fast deletion time) and add P in sorted order
        PointList newUpper = arrayToList(upperHull);
        PointList newLower = arrayToList(lowerHull);
        
        addToMiddle(newUpper, P);
        addToMiddle(newLower, P);
        
        upperHull = reassembleHull(newUpper,  1);
        lowerHull = reassembleHull(newLower, -1);

    }

    /* getHull()
       Return a java.util.LinkedList object containing the points
       in the convex hull, in order (such that iterating over the list
       will produce the same ordering of vertices as walking around the 
       polygon).
       */
    public LinkedList<Point2d> getHull() {
        LinkedList<Point2d> finalHull = new LinkedList<Point2d>();

        for(int i = 0; i < upperHull.size(); i++) {
            finalHull.add(upperHull.get(i));
        }
        //Now add lower hull, excluding the first and last point becasue they were already included from the upper hull
        for(int j = lowerHull.size() - 2; j > 0; j--) {
            finalHull.add(lowerHull.get(j));
        }
        return finalHull;
    }

    /* isInsideHull(P)
       Given an point P, return true if P lies inside the convex hull
       of the current point set (note that P may not be part of the
       current point set). Return false otherwise.
       */
    public boolean isInsideHull(Point2d P) {
        return false;
    }

    public static void main(String args[]) {
        HullBuilder hb = new HullBuilder();
        Point2d p1 = new Point2d(1,2);
        Point2d p2 = new Point2d(3,2);
        Point2d p3 = new Point2d(4,2);
        Point2d p4 = new Point2d(2,3);
        Point2d p0 = new Point2d(0,2);
        Point2d p5 = new Point2d(8,2);

        ArrayList<Point2d> test = new ArrayList<Point2d>();
        test.add(p1);
        test.add(p2);
        test.add(p3);

        PointList list = hb.arrayToList(test);
        hb.addToMiddle(list, p4);
        hb.addToMiddle(list, p0);
        hb.addToMiddle(list, p5);
        
        HullBuilder hb2  = new HullBuilder();
        hb2.addPoint(p1);
        hb2.addPoint(p4);
        hb2.addPoint(p2);
        
        for(int i = 0; i < hb2.upperHull.size(); i++) {
            System.out.println("UpperHull Point: " + hb2.upperHull.get(i));
        }
        for(int i = 0; i < hb2.lowerHull.size(); i++) {
            System.out.println("LowerHull Point: " + hb2.lowerHull.get(i));
        }
    }
}

