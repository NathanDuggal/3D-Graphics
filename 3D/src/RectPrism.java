import java.awt.Graphics;
import java.security.spec.ECFieldF2m;
import java.util.HashSet;
import java.util.Set;

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
        faces = new Quad[]{
            
        }
        System.out.println("Prism successfully initialized with "+points.length+" points and "+lines.length+" lines");
    }


    @Override
    public void draw(Graphics g) {
        //g.drawPolygon(, yPoints, 4);
    }
}
