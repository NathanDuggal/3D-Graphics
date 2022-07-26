import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Canvas;
import java.awt.event.ActionEvent;
import java.awt.event.KeyListener;
import java.awt.event.KeyEvent;
import static java.lang.Character.*;
import java.awt.image.BufferedImage;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class App extends Canvas implements KeyListener, Runnable
{
    private final Color fillColor = Color.BLACK;
    private final Color lineColor = Color.WHITE;

    public static final int width = Main.WIDTH;
    public static final int height = Main.HEIGHT;

    private static double currX;
    private static double currY;
    // in radians
    private static double currT;
    private static int fov = 400;
    private static String debugStr = "";

    private BufferedImage back;

    // KeyEvents are represented as Integers
    private HashMap<Integer,Boolean> keys;

    // All non-player Objects
    private ArrayList<Drawable> objs;
  
    public App(){

        initObjs();
        initKeys();

        setBackground(fillColor);

        new Thread(this).start();

        setVisible(true);
    }

    private void initKeys(){
        this.addKeyListener(this);

        keys = new HashMap<>();
        keys.put(KeyEvent.VK_W, false);
        keys.put(KeyEvent.VK_A, false);
        keys.put(KeyEvent.VK_S, false);
        keys.put(KeyEvent.VK_D, false);
        keys.put(KeyEvent.VK_Q, false);
        keys.put(KeyEvent.VK_E, false);
    }

    public void initObjs(){

        try{

            RectPrism pog = new RectPrism(0, 0, 0, 100, 100, 100);

            currX = (width/2) - 20;
            currY = (height/2) - 20;

            objs = new ArrayList<>();

            objs.add(new Line3D(100,200,0,300,700,0));

            for(int i=0; i < 8; i++){
                Line3D l1 = new Line3D(i*100,200,0,300,700,0);
            }
        }catch(Exception e){
        
        }
    }

    public void update(Graphics window){
        paint(window);
    }

    public void paint(Graphics window){
	
        //set up the double buffering to make the game animation nice and smooth
        Graphics2D twoDGraph = (Graphics2D)window;

        //take a snap shop of the current screen and same it as an image
        //that is the exact same width and height as the current screen
        if(back==null)
        back = (BufferedImage)(createImage(width,height));

        //create a graphics reference to the back ground image
        //we will draw all changes on the background image
        Graphics graphToBack = back.createGraphics();
        
        graphToBack.setColor(fillColor);
        graphToBack.fillRect(0,0,width,height);

        // ----------------------------
        // Key Detection --------------
        // ----------------------------

        if(keys.get(KeyEvent.VK_W)){
            currY+=-5*Math.cos(currT);
            currX+=-5*Math.sin(currT);
        }

        if(keys.get(KeyEvent.VK_S)){
            currY+=5*Math.cos(currT);
            currX+=5*Math.sin(currT);
        }

        if(keys.get(KeyEvent.VK_A)){
            currX+=-5*Math.cos(currT);
            currY+=5*Math.sin(currT);
        }
        
        if(keys.get(KeyEvent.VK_D)){
            currX+=5*Math.cos(currT);
            currY+=-5*Math.sin(currT);
        }
         
        if(keys.get(KeyEvent.VK_Q)){
            currT+=-Math.PI/100;
        }
    
        if(keys.get(KeyEvent.VK_E)){
            currT+=Math.PI/100;
        }

        // ----------------------------
        // Drawing Stuff --------------
        // ----------------------------

        graphToBack.setColor(lineColor);

        for(Drawable d : objs)
            d.draw(graphToBack);

        graphToBack.fillOval((width/2) - 20, (height/2) - 20, 40, 40);

        debugStr =  "X : "+currX+"\n"+
                    "Y : "+currY+"\n"+
                    "T : "+currT%(2*Math.PI)+"\n";

        graphToBack.setFont(new Font("Dialog",0,30));
        drawString(graphToBack, debugStr, 10, 10);

        twoDGraph.drawImage(back, null, 0, 0);
    }

    public void keyPressed(KeyEvent e){
        if(keys.keySet().contains(e.getKeyCode())){
            keys.put(e.getKeyCode(),true);
            repaint();
        }
    }

    public void keyReleased(KeyEvent e){
        if(keys.keySet().contains(e.getKeyCode())){
            keys.put(e.getKeyCode(),false);
            repaint();
        }
    }

    public static double currX(){
        return currX;
    }
    public static double currY(){
        return currY;
    }
    public static double currT(){
        return currT;
    }
    public static int fov(){
        return fov;
    }
    public static void drawString(Graphics g, String text, int x, int y) {
        for (String line : text.split("\n"))
          g.drawString(line, x, y += g.getFontMetrics().getHeight());
    }

    public void run(){
        try
        {
            while(true)
            {
                Thread.currentThread().sleep(1);
                repaint();
            }
        }catch(Exception e)
        {
        }
    }

    @Override
    public void keyTyped(KeyEvent e) {
        // TODO Auto-generated method stub
        
    }
}

