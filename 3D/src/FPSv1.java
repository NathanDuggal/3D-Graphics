import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JSlider;

import java.awt.Color;
import java.awt.Component;
import java.awt.Container;
import java.awt.Cursor;
import java.awt.Point;
import java.awt.image.BufferedImage;
import java.awt.Toolkit;
import java.awt.GraphicsEnvironment;
import java.awt.GraphicsDevice;
import java.awt.event.*;  
import static java.lang.Math.*;

public class FPSv1 extends JFrame
{
  public static final int WIDTH = (int)Toolkit.getDefaultToolkit().getScreenSize().getWidth();
  public static final int HEIGHT = (int)Toolkit.getDefaultToolkit().getScreenSize().getHeight();

  // Current monitor
  public static GraphicsDevice device = GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice();

  public static JFrame frame;

  public static SettingsMenu settings;
  public static App app;
  
  public static Cursor blankCursor;

  public FPSv1(){
    super("3D Graphics");

    // Initialize assets
    settings = new SettingsMenu();
    app = new App();

    blankCursor = Toolkit.getDefaultToolkit().createCustomCursor(
      new BufferedImage(16, 16, BufferedImage.TYPE_INT_ARGB),
      new Point(0, 0), "blank cursor"
    );

    // JFrame setup
    setSize(WIDTH,HEIGHT);

    ((Component)app).setFocusable(true);

    getContentPane().add(app);
    setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    setUndecorated(true); 

    setVisible(true);
  }

  public static void main( String args[] )
  {

    System.out.println(11.0%1);
    frame = new FPSv1();

    // Set the blank cursor to the JFrame.
    frame.getContentPane().setCursor(blankCursor);
  }

  public static void exit(){
    // device.setFullScreenWindow(null);
    app.close();
    frame.dispose();
  }

  public static void fullScreen(){
    System.out.println("Fullscreen?");
    device.setFullScreenWindow(frame);
  }

  public static void toggleSettings(){

    Container content = frame.getContentPane();

    if(content.getComponents()[0] == app){
      System.out.println("Switching to settings");
      content.setCursor(Cursor.getDefaultCursor());
      content.remove(app);
      content.add(settings);
      frame.pack();
      frame.setSize(WIDTH,HEIGHT);
      settings.requestFocus();
    }else{
      System.out.println("Switching to app");
      App.setSens(settings.getSliderVal("Sensitivity"));
      App.setFOV(settings.getSliderVal("FOV"));
      content.setCursor(blankCursor);
      content.remove(settings);
      content.add(app);
      app.requestFocus();
    }
  }

  // Maps a point in space to where it should appear on the screen
  public static int[] getDisplayableCoords(int x, int y, int z){
        
    double a = App.currT() + PI/2;
    double b = App.currU() * cos(App.currT());
    double c = App.currU() * sin(App.currT());

    // if(App.totU()>=(PI/2 - 0.01)){ 
    //   // System.out.println("here");
    //   a=PI/2;
    // }

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
  
    // Why
    if(roty < 0) roty=1;

    // Idk what this does exactly
    double visx = rotx * fov / roty;
    double visy = rotz * fov / roty;

    int finx = (int)visx+WIDTH/2;
    int finy = (int)-visy+HEIGHT/2;

    // return new int[]{(int)(visx*WIDTH/2)+WIDTH/2, (int)(-visy*HEIGHT/2)+HEIGHT/2, (int) rotx, (int) roty, (int) rotz};
    return new int[]{finx, finy, (int) rotx, (int) roty, (int) rotz};
  }

  public static int getAvgDist(Point3D...points){
    int totX = 0;
    int totY = 0;
    int totZ = 0;

    for(Point3D p : points){
      totX+=p.x;
      totY+=p.y;
      totZ+=p.z;
    }

    totX/=points.length;
    totY/=points.length;
    totZ/=points.length;

    int sum=0;
    sum+=pow(App.currX()-totX, 2);
    sum+=pow(App.currY()-totY, 2);
    sum+=pow(App.currZ()-totZ, 2);

    return (int)sqrt(sum);
  }
}

