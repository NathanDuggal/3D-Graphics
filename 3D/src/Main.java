import javax.swing.JFrame;
import java.awt.Component;
import java.awt.Cursor;
import java.awt.Point;
import java.awt.image.BufferedImage;
import java.awt.Toolkit;
import java.awt.GraphicsEnvironment;
import java.awt.GraphicsDevice;

public class Main extends JFrame
{
  public static final int WIDTH = (int)Toolkit.getDefaultToolkit().getScreenSize().getWidth();
  public static final int HEIGHT =(int)Toolkit.getDefaultToolkit().getScreenSize().getHeight();

  public static JFrame main;

  static GraphicsDevice device = GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice();

  public Main()
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
    main = new Main();

    // Transparent 16 x 16 pixel cursor image.
    BufferedImage cursorImg = new BufferedImage(16, 16, BufferedImage.TYPE_INT_ARGB);

    // Create a new blank cursor.
    Cursor blankCursor = Toolkit.getDefaultToolkit().createCustomCursor(
        cursorImg, new Point(0, 0), "blank cursor");

    // Set the blank cursor to the JFrame.
    main.getContentPane().setCursor(blankCursor);
  }

  public static void exit(){
    device.setFullScreenWindow(null);
  }
  public static void fullScreen(){
    System.out.println("Fullscreen?");
    device.setFullScreenWindow(main);
  }
}
