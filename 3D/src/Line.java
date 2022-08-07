import java.awt.Color;
import java.awt.Graphics;
import static java.lang.Math.*;

public class Line implements Orientable{

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
        double rely1 = y1 - App.currY();
        double relx2 = x2 - App.currX();
        double rely2 = y2 - App.currY();

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

        int[] p1coords = FPSv1.getDisplayableCoords(x1, y1, z1);
        int[] p2coords = FPSv1.getDisplayableCoords(x2, y2, z2);
    
        int visx1 = p1coords[0];
        int visy1 = p1coords[1];
        int visx2 = p2coords[0];
        int visy2 = p2coords[1];

        if(ID==0){
            // g.setColor(Color.RED);
            App.drawString(g, 
                "visy1: "+visy1+"\n"+
                "visx1: "+visx1+"\n"+
                "rotx: "+p1coords[2]+"\n"+
                "roty: "+p1coords[3]+"\n"+
                "rotz: "+p1coords[4]+"\n"
            ,1000, 0);
        }

        if(p1coords[3]==1 || p2coords[3]==1) g.setColor(Color.RED);
        if(!(p1coords[3]==1 && p2coords[3]==1)) g.drawLine(visx1,visy1,visx2,visy2);
        g.setColor(App.lineColor);
    }
    @Override
    public int x() {
        return (x1+x2)/2;
    }
    @Override
    public int y() {
        return (y1+y2)/2;
    }
    @Override
    public int z() {
        return (z1+z2)/2;
    }
    @Override
    public int getDist() {
        return FPSv1.getAvgDist(new Point3D[]{p1,p2});
    }
    @Override
    public int obscures(Orientable o) {
        // TODO Auto-generated method stub
        return 0;
    }
}
