import java.awt.Graphics;
import java.util.HashSet;

public class RectPrism implements Drawable{

    private final Point3D[] points;
    private final Quad[] faces;
    private final Line3D[] lines;
    private final int w,l,h;

    // Ez init?
    public RectPrism(int x, int y, int z, int l, int w, int h){
        this.w=w;
        this.l=l;
        this.h=h;
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

        HashSet<Line3D> linesSet = new HashSet<>();

        for(Quad q : faces){
            for(Line3D li : q.lines()){
                linesSet.add(li);
            }
        }

        Line3D[] tempLines = new Line3D[12];

        lines = linesSet.toArray(tempLines);

        System.out.println("Prism successfully initialized with "+lines.length+" lines");
    }


    @Override
    public void draw2D(Graphics g) {
        for(Line3D l : lines)
            l.draw2D(g);
    }


    @Override
    public void draw3D(Graphics g) {
        for(Line3D l : lines)
            l.draw3D(g);
    }
}
