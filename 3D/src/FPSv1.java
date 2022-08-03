import javax.swing.JFrame;
import java.awt.Component;
import java.awt.Cursor;
import java.awt.Point;
import java.awt.image.BufferedImage;
import java.awt.Toolkit;
import java.awt.GraphicsEnvironment;
import java.awt.GraphicsDevice;
import static java.lang.Math.*;

public class FPSv1 extends JFrame
{
  public static final int WIDTH = (int)Toolkit.getDefaultToolkit().getScreenSize().getWidth();
  public static final int HEIGHT =(int)Toolkit.getDefaultToolkit().getScreenSize().getHeight();

  public static JFrame main;

  static GraphicsDevice device = GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice();

  public FPSv1()
  {
    super("3D Graphics");
    setSize(WIDTH,HEIGHT);

    App app = new App();
    ((Component)app).setFocusable(true);

    getContentPane().add(app);
    setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

    setUndecorated(true); 
    setVisible(true);
  }

  public static void main( String args[] )
  {
    main = new FPSv1();

    // Transparent 16 x 16 pixel cursor image.
    BufferedImage cursorImg = new BufferedImage(16, 16, BufferedImage.TYPE_INT_ARGB);

    // Create a new blank cursor.
    Cursor blankCursor = Toolkit.getDefaultToolkit().createCustomCursor(
        cursorImg, new Point(0, 0), "blank cursor");

    // Set the blank cursor to the JFrame.
    main.getContentPane().setCursor(blankCursor);
  }

  public static void exit(){
    // device.setFullScreenWindow(null);
    main.dispose();
  }
  public static void fullScreen(){
    System.out.println("Fullscreen?");
    device.setFullScreenWindow(main);
  }

  // Returns int array length 0 if coords should not be displayed
  public static int[] getDisplayableCoords(int x, int y, int z){
        
    double a = App.currT() + PI/2;
    double b = App.currU() * cos(App.currT());
    double c = App.currU() * sin(App.currT());

    double fov = App.fov();

    double relx = x - App.currX();
    double rely = y - App.currY();
    double relz = z - App.currZ();

    double rotx = (
        relx*cos(a)*cos(b) + 
        rely*(cos(a)*sin(b)*sin(c) - sin(a)*cos(c)) + 
        relz*(cos(a)*sin(b)*cos(c) + sin(a)*sin(c))
    );
    double roty = (
        relx*sin(a)*cos(b) + 
        rely*(sin(a)*sin(b)*sin(c) + cos(a)*cos(c)) +
        relz*(sin(a)*sin(b)*cos(c) - cos(a)*sin(c))
    );
    double rotz = (
        relx*-sin(b) + 
        rely*cos(b)*sin(c) +
        relz*cos(b)*cos(b)
    );

    double visx;
    double visy;

    // Why
    if(roty < 0 || roty < 0)
        return new int[0];

    visx = rotx * fov / roty;
    visy = rotz * fov / roty;

    return new int[]{(int)visx+WIDTH/2, (int)-visy+HEIGHT/2};
  }

  public static int getAvgDist(Point3D...points){
    int sumOfSums = 0;
    int i=0;
    for(Point3D p : points){
      int sum=0;
      sum+=pow(App.currX()-p.x, 2);
      sum+=pow(App.currY()-p.y, 2);
      sum+=pow(App.currZ()-p.z, 2);
      sumOfSums+=sqrt(sum);
      i++;
    }
    return sumOfSums/i;
  }
}
