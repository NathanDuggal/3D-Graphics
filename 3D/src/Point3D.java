import java.awt.Graphics;

public class Point3D implements Drawable{

    private final int x,y,z;

    public Point3D(int x, int y, int z){
        this.x=x;
        this.y=y;
        this.z=z;
    }

    public int x(){
        return x;
    }
    public int y(){
        return y;
    }
    public int z(){
        return z;
    }

    // bad bad bad 
    @Override
    public int hashCode(){
        return 1;
    }

    @Override
    public String toString(){
        return "( "+x+","+y+","+z+" )";
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
