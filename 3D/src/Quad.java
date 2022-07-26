import java.awt.Graphics;
import java.util.HashSet;

import javax.print.attribute.standard.JobStateReasons;

public class Quad implements Drawable {

    private final Line3D[] lines;
    private final Point3D[] points;

    public Quad(Line3D l1, Line3D l2, Line3D l3, Line3D l4) throws Exception{
        lines = new Line3D[]{ l1, l2, l3, l4 };

        // make sure no duplicate lines
        for(int i=0; i<lines.length; i++){
            for(int ii=0; ii<lines.length; ii++){
                if(lines[i].equals(lines[ii]) && i != ii) throw new ExceptionInInitializerError();
            }
            tempPoints[i*2]=lines[i].p1();
            tempPoints[(i*2)+1]=lines[i].p2();
        }

        
        // make sure all points work
        points = getPoints(tempPoints);
    }

    public Line3D[] lines(){
        return lines;
    }

    // Doesnt work in some edge cases 
    private Point3D getPoints(Point3D[] points) throws Exception{
        
        Point3D[] endPoints = new Point3D[4];

        for(Point3D p : points){
            int s=0;
            for(Line3D l : lines){
                if(l.containsPoint(p)) s++;
            }
            if(s != 2) throw new Exception();
            if()
        }
    }  

    @Override
    public void draw(Graphics g) {
        for(Line3D l : lines)
            l.draw(g);
    }
}
