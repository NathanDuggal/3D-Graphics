import java.awt.Graphics;

public class Line3D implements Drawable{

    private static int linesRem = 0;
    private static int IDNum = 0;

    private final int ID;
    private final int x1,y1,z1,x2,y2,z2;
    private final Point3D p1,p2;
    public Line3D(int x1, int y1, int z1, int x2,int y2, int z2) throws Exception{
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
    public Line3D(Point3D p1, Point3D p2) throws Exception{
        this.p1=p1;
        this.p2=p2;
        x1=p1.x();
        y1=p1.y();
        z1=p1.z();
        x2=p2.x();
        y2=p2.y();
        z2=p2.z();
        checkPoints();
        ID=IDNum++;
    }

    // idk if all this is even necessary if all vars were public finals 
    public int x1(){
        return x1;
    }
    public int x2(){
        return x2;
    }
    public int y1(){
        return y1;
    }
    public int y2(){
        return y2;
    }
    public int z1(){
        return z1;
    }
    public int z2(){
        return z2;
    }
    public Point3D p1(){
        return p1;
    }
    public Point3D p2(){
        return p2;
    }

    public boolean containsPoint(Point3D p){
        return p1.equals(p) || p2.equals(p);
    }

    private void checkPoints() throws Exception{
        if(p1.equals(p2)){
            System.out.println("Line is gone" + p1+p2);
            throw new Exception("Points cannot be the same");
        }
    }

    // bad bad bad 
    @Override
    public int hashCode(){
        return 1;
    }

    @Override
    public String toString(){
        return "Line( "+p1+" "+p2+" )";
    }

    @Override
    public boolean equals(Object o){
        if(o==null || !(o instanceof Line3D))
            return false;
        Line3D l = (Line3D) o;

        if((p1.equals(l.p1) && p2.equals(l.p2)) || (p1.equals(l.p2) && p2.equals(l.p1)))
            System.out.println("Equal lines detected "+(++linesRem));

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

        double rotx1 = (relx1*Math.cos(a) - rely1*Math.sin(a));
        double roty1 = (relx1*Math.sin(a) + rely1*Math.cos(a));
        double rotx2 = (relx2*Math.cos(a) - rely2*Math.sin(a));
        double roty2 = (relx2*Math.sin(a) + rely2*Math.cos(a));

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
        
        double a = -App.currT();
        double fov = App.fov();

        double relx1 = x1 - App.currX();
        double rely1 = y1 - App.currY();
        double relz1 = z1 - App.currZ();
        double relx2 = x2 - App.currX();
        double rely2 = y2 - App.currY();
        double relz2 = z2 - App.currZ();

        double rotx1 = (relx1*Math.cos(a) - rely1*Math.sin(a));
        double roty1 = (relx1*Math.sin(a) + rely1*Math.cos(a));
        double rotx2 = (relx2*Math.cos(a) - rely2*Math.sin(a));
        double roty2 = (relx2*Math.sin(a) + rely2*Math.cos(a));

        if(roty1 > 0 || roty2 > 0)
            return;

        double visX1 = rotx1 * fov / roty1;
        double visZ1 = relz1 * fov / roty1;
        double visX2 = rotx2 * fov / roty2;
        double visZ2 = relz2 * fov / roty2;

        if(ID==0){
            App.drawString(g, 
                "relx1: "+relx1+"\n"+
                "rely1: "+rely1+"\n"+
                "relz1: "+relz1+"\n"+
                "rotx1: "+rotx1+"\n"+
                "roty1: "+roty1+"\n"+
                "visZ1: "+visZ1+"\n"+
                "visX1: "+visX1+"\n", 1000, 0);
        }

        g.drawLine(
            (int) (visX1+App.width/2), 
            (int) (visZ1+App.height/2), 
            (int) (visX2+App.width/2), 
            (int) (visZ2+App.height/2)
        );
    }
}
