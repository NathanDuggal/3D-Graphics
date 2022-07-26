import javax.swing.JFrame;
import java.awt.Component;

public class Main extends JFrame
{
  public static final int WIDTH = 1920;
  public static final int HEIGHT = 1080;

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
    Main run = new Main();
  }
}
