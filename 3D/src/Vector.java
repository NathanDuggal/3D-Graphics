import java.awt.Graphics;
import static java.lang.Math.*;

public class Vector implements Orientable{

    public final double x,y,z;
    public final int ID;
    private static int IDnum;

    public Vector(double x, double y, double z){
        this.x=x;
        this.y=y;
        this.z=z;
        ID=IDnum++;
    }

    public Vector getUnit(){
        double m = getMag();
        return new Vector(x/m,y/m,z/m);
    }

    public double getMag(){
        return sqrt(x*x+y*y+z*z);
    }

    @Override
    public double x(){
        return x;
    }
    @Override
    public double y(){
        return y;
    }
    @Override
    public double z(){
        return z;
    }
    @Override
    public int getDist(){
        return FPSv1.getAvgDist(this);
    }

    // bad bad bad 
    @Override
    public int hashCode(){
        return 1;
    }

    @Override
    public String toString(){
        return "< "+x+" "+y+" "+z+" > ";
    }

    @Override
    public void draw2D(Graphics g) {
        // TODO Auto-generated method stub
        
    }

    @Override
    public boolean equals(Object o){
        if(o==null || !(o instanceof Vector))
            return false;
        Vector p = (Vector) o;
        return x==p.x && y==p.y && z==p.z;
    }

    @Override
    public void draw3D(Graphics g) {
        // TODO Auto-generated method stub
        
    }

    @Override
    public int obscures(Orientable o) {
        // TODO Auto-generated method stub
        return 0;
    }
}
