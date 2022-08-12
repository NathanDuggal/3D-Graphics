import java.awt.Graphics;

public interface Orientable{
    public double x();
    public double y();
    public double z();
    public int obscures(Orientable o);
    public int getDist();
    public void draw2D(Graphics g);
    public void draw3D(Graphics g);
}
