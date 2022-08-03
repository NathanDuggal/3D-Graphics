import java.awt.Graphics;

public interface Orientable{
    public int x();
    public int y();
    public int z();
    public int obscures(Orientable o);
    public int getDist();
    public void draw2D(Graphics g);
    public void draw3D(Graphics g);
}
