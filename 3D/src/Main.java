import javax.swing.JFrame;
import java.awt.Component;
import java.awt.Cursor;
import java.awt.Point;
import java.awt.image.BufferedImage;
import java.awt.Toolkit;

public class Main extends JFrame
{
  public static final int WIDTH = 1920;
  public static final int HEIGHT = 1080;

  public static JFrame main;

  public Main()
  {
    super("3D Graphics");
    setSize(WIDTH,HEIGHT);

    App app = new App();
    ((Component)app).setFocusable(true);

    getContentPane().add(app);
    setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
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
}
