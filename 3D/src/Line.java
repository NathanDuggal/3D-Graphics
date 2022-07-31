import java.awt.Color;
import java.awt.Graphics;
import static java.lang.Math.*;

public class Line implements Drawable{

    private static int IDNum = 0;

    public final int ID;
    public final int x1,y1,z1,x2,y2,z2;
    public final Point3D p1,p2;
    public Line(int x1, int y1, int z1, int x2,int y2, int z2) throws Exception{
        this.x1=x1;
        this.y1=y1;
        this.z1=z1;
        this.x2=x2;
        this.y2=y2;
        this.z2=z2;
        p1=new Point3D(x1,y1,z1);
        p2=new Point3D(x2,y2,z2);
        checkPoints();
        ID=IDNum++;
    }
    public Line(Point3D p1, Point3D p2) throws Exception{
        this.p1=p1;
        this.p2=p2;
        x1=p1.x;
        y1=p1.y;
        z1=p1.z;
        x2=p2.x;
        y2=p2.y;
        z2=p2.z;
        checkPoints();
        ID=IDNum++;
    }

    public boolean containsPoint(Point3D p){
        return p1.equals(p) || p2.equals(p);
    }

    private void checkPoints() throws Exception{
        if(p1.equals(p2)){
            System.out.println("Attempted to initialize line with identical points" + p1+p2);
            throw new Exception("Points cannot be the same");
        }
    }

    @Override
    public String toString(){
        return "Line"+ID;
    }

    // bad bad bad 
    @Override
    public int hashCode(){
        return 1;
    }

    @Override
    public boolean equals(Object o){
        if(o==null || !(o instanceof Line))
            return false;
        Line l = (Line) o;

        // if((p1.equals(l.p1) && p2.equals(l.p2)) || (p1.equals(l.p2) && p2.equals(l.p1)))
        //     System.out.println("Equal lines detected "+(++linesRem));

        return (p1.equals(l.p1) && p2.equals(l.p2)) || (p1.equals(l.p2) && p2.equals(l.p1));
    }

    // Where most of the magic happens
    @Override
    public void draw2D(Graphics g) {

        double a = -App.currT();

        double relx1 = x1 - App.currX();
        double rely1 = z1 - App.currY();
        double relx2 = x2 - App.currX();
        double rely2 = z2 - App.currY();

        double rotx1 = (relx1*cos(a) - rely1*sin(a));
        double roty1 = (relx1*sin(a) + rely1*cos(a));
        double rotx2 = (relx2*cos(a) - rely2*sin(a));
        double roty2 = (relx2*sin(a) + rely2*cos(a));

        // xcosT - ysinT , xsinT + ycosT

        g.drawLine(
            (int) (rotx1+App.width/2),
            (int) (roty1+App.height/2),
            (int) (rotx2+App.width/2),
            (int) (roty2+App.height/2)
        );
    }

    @Override
    public void draw3D(Graphics g) {
        
        double c = -App.currT();
        double b = App.currU() * sin(App.currT());
        double a = App.currU() * cos(App.currT());

        double fov = App.fov();

        double relx1 = x1 - App.currX();
        double rely1 = y1 - App.currY();
        double relz1 = z1 - App.currZ();
        double relx2 = x2 - App.currX();
        double rely2 = y2 - App.currY();
        double relz2 = z2 - App.currZ();

        // double rotx1 = (relx1*cos(c) - rely1*sin(c));
        // double roty1 = (relx1*sin(c) + rely1*cos(c));
        // double rotx2 = (relx2*cos(c) - rely2*sin(c));
        // double roty2 = (relx2*sin(c) + rely2*cos(c));

        double rotx1 = (
            relx1*cos(b)*cos(c) + 
            rely1*(sin(a)*sin(b)*cos(c) - cos(a)*sin(c)) + 
            relz1*(cos(a)*sin(b)*cos(c) + sin(a)*sin(c))
        );
        double roty1 = (
            relx1*cos(b)*sin(c) + 
            rely1*(sin(a)*sin(b)*sin(c) + cos(a)*cos(c)) +
            relz1*(cos(a)*sin(b)*sin(c) - sin(a)*cos(c))
        );
        double rotz1 = (
            relx1*-sin(b) + 
            rely1*sin(a)*cos(b) +
            relz1*cos(a)*cos(b)
        );
        double rotx2 = (
            relx2*cos(b)*cos(c) + 
            rely2*(sin(a)*sin(b)*cos(c) - cos(a)*sin(c)) + 
            relz2*(cos(a)*sin(b)*cos(c) + sin(a)*sin(c))
        );
        double roty2 = (
            relx2*cos(b)*sin(c) + 
            rely2*(sin(a)*sin(b)*sin(c) + cos(a)*cos(c)) +
            relz2*(cos(a)*sin(b)*sin(c) - sin(a)*cos(c))
        );
        double rotz2 = (
            relx2*-sin(b) + 
            rely2*sin(a)*cos(b) +
            relz2*cos(a)*cos(b)
        );

        // double rotx1;
        // double roty1;
        // double rotz1;
        // double rotx2;
        // double roty2;
        // double rotz2;

        if(ID != 0 && roty1 > 0 || roty2 > 0)
            return;

        double visx1 = rotx1 * fov / roty1;
        double visy1 = rotz1 * fov / roty1;
        double visx2 = rotx2 * fov / roty2;
        double visy2 = rotz2 * fov / roty2;

        if(ID==0){
            g.setColor(Color.RED);
            App.drawString(g, 
                "relx1: "+relx1+"\n"+
                "rely1: "+rely1+"\n"+
                "relz1: "+relz1+"\n"+
                "rotx1: "+rotx1+"\n"+
                "roty1: "+roty1+"\n"+
                "visy1: "+visy1+"\n"+
                "visx1: "+visx1+"\n", 1000, 0);
        }

        g.drawLine(
            (int) (visx1+App.width/2), 
            (int) (visy1+App.height/2), 
            (int) (visx2+App.width/2), 
            (int) (visy2+App.height/2)
        );
        g.setColor(App.lineColor);
    }
}
