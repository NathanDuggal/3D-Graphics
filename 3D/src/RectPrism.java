import java.awt.Graphics;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.awt.Color;

public class RectPrism implements Orientable{

    private final Point3D[] points;
    private final Quad[] faces;
    private final Line[] lines;
    private final int x,y,z;
    public final int ID;
    private static int IDnum;

    // Ez init?
    public RectPrism(int x, int y, int z, int l, int w, int h, RectPrism o){

        if(o != null){
            this.x=x+o.x;
            this.y=x+o.y;
            this.z=x+o.z;
        }else{
            this.x=x;
            this.y=y;
            this.z=z;
        }

        points = new Point3D[]{
            new Point3D(x, y, z),

            new Point3D(x+l, y, z),
            new Point3D(x, y+w, z),
            new Point3D(x, y, z+h),

            new Point3D(x+l, y+w, z),
            new Point3D(x, y+w, z+h),
            new Point3D(x+l, y, z+h),

            new Point3D(x+l, y+w, z+h)
        };
        faces = new Quad[6];
        try{
            faces[0] = new Quad(points[0], points[1], points[4], points[2]);
            faces[1] = new Quad(points[0], points[3], points[5], points[2]);
            faces[2] = new Quad(points[0], points[3], points[6], points[1]);
            faces[3] = new Quad(points[3], points[6], points[7], points[5]);
            faces[4] = new Quad(points[1], points[6], points[7], points[4]);
            faces[5] = new Quad(points[2], points[5], points[7], points[4]);
        } catch(ExceptionInInitializerError e){
            System.out.println("Attempted to create quad with duplicate points");
        } catch(Exception e){
            System.out.println("Attempted to create bad line");
        }

        HashSet<Line> linesSet = new HashSet<>();

        for(Quad q : faces){
            for(Line li : q.lines()){
                linesSet.add(li);
            }
        }

        Line[] tempLines = new Line[12];

        lines = linesSet.toArray(tempLines);


        if(o != null) ID=o.ID;
        else ID=IDnum++;
        
        // System.out.println("Prism successfully initialized with "+lines.length+" lines");
    }

    public RectPrism(int x, int y, int z, int l, int w, int h){
        this(x, y, z, l, w, h, null);
    }

    @Override
    public int x(){
        return x;
    }
    @Override
    public int y(){
        return y;
    }
    @Override
    public int z(){
        return z;
    }

    // Unsafe because points can be modified
    public Quad[] faces(){
        return faces;
    }

    // Unsafe because lines can be modified
    public Line[] lines(){
        return lines;
    }

    // Scuffed, needs a rewrite
    @Override
    public int getDist(){
        return FPSv1.getAvgDist(points);
    }

    @Override
    public String toString(){
        return "RectPrism"+ID;
    }


    @Override
    public void draw2D(Graphics g) {
        for(Line l : lines)
            l.draw2D(g);
    }


    // Pretty scuffed
    @Override
    public void draw3D(Graphics g) {

        // System.out.println("drawing rectprism");

        int i=ID+1;

        ArrayList<Orientable> drawables = new ArrayList<>(); //Arrays.asList(lines);
     
        for(Line l : lines)
            drawables.add(l);

        for(Quad q : faces)
            drawables.add(q);

        Collections.sort(drawables, (Orientable a, Orientable b) -> {
            return b.getDist() - a.getDist();
        });

        for(Orientable d : drawables){
            if(d instanceof Quad) ((Quad) d).draw3D(g, new Color((i*200)%255,(i*140)%255,(i*100)%255));
            else d.draw3D(g);
        }
    }

    @Override
    public int obscures(Orientable o) {
        // TODO Auto-generated method stub
        return 0;
    }
}
