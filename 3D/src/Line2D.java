import java.awt.Graphics;

public class Line2D implements Drawable {
    private final int x1,y1,x2,y2;
    public Line2D(int x1, int y1, int x2, int y2){
        this.x1=x1;
        this.y1=y1;
        this.x2=x2;
        this.y2=y2;
    }
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

    @Override
    public void draw(Graphics g) {
        double cx = -App.currX() + App.width/2;
        double cy = -App.currY() + App.height/2;
        //g.drawLine(x1+cx, y1+cy, x2+cx, y2+cy);
    }
}