import java.awt.Graphics;

public class Point3D implements Drawable{

    public final int x,y,z;
    public final int ID;
    private static int IDnum;

    public Point3D(int x, int y, int z){
        this.x=x;
        this.y=y;
        this.z=z;
        ID=IDnum++;
    }

    // bad bad bad 
    @Override
    public int hashCode(){
        return 1;
    }

    @Override
    public String toString(){
        return "Point"+ID;
    }

    @Override
    public void draw2D(Graphics g) {
        // TODO Auto-generated method stub
        
    }

    @Override
    public boolean equals(Object o){
        if(o==null || !(o instanceof Point3D))
            return false;
        Point3D p = (Point3D) o;
        return x==p.x && y==p.y && z==p.z;
    }

    @Override
    public void draw3D(Graphics g) {
        // TODO Auto-generated method stub
        
    }
}
