import java.awt.Color;
import java.awt.Graphics;
import java.util.ArrayList;
//import static java.lang.Math.*;

public class Quad implements Orientable {

    private final Line[] lines;
    private final Vector[] points;
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
        points = new Vector[]{};
        ID=IDnum++;
    }

    // Way better
    public Quad(Vector p1, Vector p2, Vector p3, Vector p4) throws ExceptionInInitializerError, Exception{
        points = new Vector[]{ p1,p2,p3,p4 };

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

    public Vector[] points(){
        return points;
    }

    @Override
    public double x(){
        return (points[0].x + points[1].x + points[2].x + points[3].x)/4;
    }

    @Override
    public double y(){
        return (points[0].y + points[1].y + points[2].y + points[3].y)/4;
    }

    @Override
    public double z(){
        return (points[0].z + points[1].z + points[2].z + points[3].z)/4;
    }

    // Very scuffed tbh
    @Override
    public int getDist(){
        return FPSv1.getAvgDist(points);
    }


    // Doesnt work in some edge cases 
    private Vector[] getPoints(Vector[] points) throws Exception{
        
        Vector[] endPoints = new Vector[4];

        for(Vector p : points){
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

    public void draw3D(Graphics g, Color c){
        int[] tempX = new int[4];
        int[] tempY = new int[4];

        int count = 0;

        for(int i=0; i < 4; i++){

            int[] coords = FPSv1.getDisplayableCoords(points[i].x, points[i].y, points[i].z);

            tempX[i] = coords[0];
            tempY[i] = coords[1];

            if(coords[3] == 1) count++; 
        }

        g.setColor(c);
        // Red faces are being drawn wrong
        if(count>0) g.setColor(Color.RED);
        if(count<=2) g.fillPolygon(tempX, tempY, 4);
        g.setColor(App.lineColor);
    }

    @Override
    public void draw3D(Graphics g) {
        int i=ID+1;
        draw3D(g,new Color((i*200)%255,(i*140)%255,(i*100)%255));
    }

    @Override
    public int obscures(Orientable o) {
        // TODO Auto-generated method stub
        return 0;
    }
}
