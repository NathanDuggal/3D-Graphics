import java.awt.Color;
import java.awt.Graphics;
import java.util.ArrayList;
import static java.lang.Math.*;

public class Quad implements Drawable {

    private final Line[] lines;
    private final Point3D[] points;
    private final int ID;
    private static int IDnum;

    public Quad(Line l1, Line l2, Line l3, Line l4) throws ExceptionInInitializerError, Exception{
        lines = new Line[]{ l1, l2, l3, l4 };

        // make sure no duplicate lines
        for(int i=0; i<lines.length; i++){
            for(int ii=0; ii<lines.length; ii++){
                if(lines[i].equals(lines[ii]) && i != ii) throw new ExceptionInInitializerError();
            }
            // tempPoints[i*2]=lines[i].p1();
            // tempPoints[(i*2)+1]=lines[i].p2();
        }
        
        // make sure all points work
        // points = getPoints(tempPoints);
        points = new Point3D[]{};
        ID=IDnum++;
    }

    // Way better
    public Quad(Point3D p1, Point3D p2, Point3D p3, Point3D p4) throws ExceptionInInitializerError, Exception{
        points = new Point3D[]{ p1,p2,p3,p4 };

        // make sure no duplicate points
        for(int i=0; i<points.length; i++){
            for(int ii=0; ii<points.length; ii++){
                if(i != ii && points[i].equals(points[ii])) throw new ExceptionInInitializerError();
            }
        }

        lines = new Line[]{
            new Line(p1, p2),
            new Line(p2, p3),
            new Line(p3, p4),
            new Line(p4, p1)
        };
        ID=IDnum++;
    }

    public Line[] lines(){
        return lines;
    }

    public Point3D[] points(){
        return points;
    }

    // Doesnt work in some edge cases 
    private Point3D[] getPoints(Point3D[] points) throws Exception{
        
        Point3D[] endPoints = new Point3D[4];

        for(Point3D p : points){
            int s=0;
            for(Line l : lines){
                if(l.containsPoint(p)) s++;
            }
            if(s != 2) throw new Exception();
            // if()
        }

        return endPoints;
    } 

    @Override
    public String toString(){
        return "Quad"+ID;
    }

    @Override
    public void draw2D(Graphics g) {
        for(Line l : lines)
            l.draw2D(g);
    }

    @Override
    public void draw3D(Graphics g) {

        int[] tempXArray = new int[4];
        int[] tempYArray = new int[4];
        int i=0;
        boolean flag = false;

        for(Point3D p : points){
            double c = -App.currT();
            double b = App.currU() * sin(App.currT());
            double a = App.currU() * cos(App.currT());
    
            double fov = App.fov();
    
            double relx1 = p.x - App.currX();
            double rely1 = p.y - App.currY();
            double relz1 = p.z - App.currZ();

            // double rotx1 = (relx1*cos(c) - rely1*sin(c));
            // double roty1 = (relx1*sin(c) + rely1*cos(c));
            // double rotx2 = (relx2*cos(c) - rely2*sin(c));
            // double roty2 = (relx2*sin(c) + rely2*cos(c));
    
            double rotx1 = (
                relx1*cos(b)*cos(c) + 
                rely1*(sin(a)*sin(b)*cos(c) - cos(a)*sin(c)) + 
                relz1*(cos(a)*sin(b)*cos(c) + sin(a)*sin(c))
            );
            double roty1 = (
                relx1*cos(b)*sin(c) + 
                rely1*(sin(a)*sin(b)*sin(c) + cos(a)*cos(c)) +
                relz1*(cos(a)*sin(b)*sin(c) - sin(a)*cos(c))
            );
            double rotz1 = (
                relx1*-sin(b) + 
                rely1*sin(a)*cos(b) +
                relz1*cos(a)*cos(b)
            );
    
            if(roty1 > 0)
                flag = true;
    
            tempXArray[i] = (int) (rotx1 * fov / roty1) + App.width/2;
            tempYArray[i]  = (int) (rotz1 * fov / roty1) + App.height/2;
            i++;
        }

        if(!flag){
            g.setColor(Color.GRAY);
            g.fillPolygon(tempXArray, tempYArray, 4);
            g.setColor(App.lineColor);
        }
    }
}
