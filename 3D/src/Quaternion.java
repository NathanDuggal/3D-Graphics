import static java.lang.Math.*;

import java.text.NumberFormat;

public class Quaternion extends Vector {

    public final double r;
    
    public Quaternion(double r, double x, double y, double z){
        super(x,y,z);
        this.r = r;
    }

    public Quaternion(Vector v){
        super(v.x,v.y,v.z);
        r=0;
    }

    public Quaternion(Vector v, double rot){
        super(sin(rot/2)*v.getUnit().x,sin(rot/2)*v.getUnit().y,sin(rot/2)*v.getUnit().z);
        r = cos(rot/2);
    }

    public Quaternion getInverse(){
        return new Quaternion(r, -x, -y, -z);
    }

    public Vector asVector() {
        // if(r != 0) throw new Exception();
        return new Vector(x, y, z);
    }

    public Quaternion multiply(Quaternion q){
        return new Quaternion(
            r*q.r-x*q.x-y*q.y-z*q.z,
            r*q.x+x*q.r-y*q.z+z*q.y,
            r*q.y+x*q.z+y*q.r-z*q.x,
            r*q.z-x*q.y+y*q.x+z*q.r
        );
    }

    public String toString(){
        NumberFormat f = App.f;
        return "< r: "+f.format(r)+" x: "+f.format(x)+" y: "+f.format(y)+" z: "+f.format(z)+" > ";
    }
}
