import java.awt.Graphics;
import java.util.HashSet;

public class RectPrism implements Drawable{

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
    public String toString(){
        return "RectPrism"+ID;
    }


    @Override
    public void draw2D(Graphics g) {
        for(Line l : lines)
            l.draw2D(g);
    }


    @Override
    public void draw3D(Graphics g) {
        for(Line l : lines)
            l.draw3D(g);
        for(Quad q : faces)
            q.draw3D(g);
    }
}
