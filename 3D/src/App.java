import java.awt.Color;
import java.awt.Font;
import java.awt.Point;
import java.awt.Cursor;
import java.awt.Toolkit;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Canvas;
import java.awt.event.ActionEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.KeyEvent;
import static java.lang.Character.*;
import java.awt.image.BufferedImage;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import javax.swing.event.MouseInputListener;

public class App extends Canvas implements MouseInputListener, KeyListener, Runnable
{
    private final Color fillColor = Color.BLACK;
    private final Color lineColor = Color.WHITE;//new Color(255,0,255);

    public static final int width = Main.WIDTH;
    public static final int height = Main.HEIGHT;

    private static double currX;
    private static double currY;
    private static double currZ;
    private static double currS;
    // in radians, angles around the z, y, and x axes respectively
    private static double currT;
    private static double currU;
    private static double currV;

    private static int lastMouseX = 0;
    private static int lastMouseY = 0;
    private static int fov;
    private static String debugStr = "";

    private BufferedImage back;

    // KeyEvents are represented as Integers
    private HashMap<Integer,Boolean> keys;

    // All non-player Objects
    private ArrayList<Drawable> objs;
  
    public App(){

        initObjs();
        initInput();

        setBackground(fillColor);

        new Thread(this).start();

        setVisible(true);
    }

    private void initInput(){
        this.addMouseMotionListener(this);
        //this.addMouseListener(this);
        this.addKeyListener(this);

        keys = new HashMap<>();
        keys.put(KeyEvent.VK_W, false);
        keys.put(KeyEvent.VK_A, false);
        keys.put(KeyEvent.VK_S, false);
        keys.put(KeyEvent.VK_D, false);
        keys.put(KeyEvent.VK_T, false);
        keys.put(KeyEvent.VK_U, false);
        keys.put(KeyEvent.VK_V, false);
        keys.put(KeyEvent.VK_ESCAPE, false);
        keys.put(KeyEvent.VK_F1, false);
        keys.put(KeyEvent.VK_LEFT, false);
        keys.put(KeyEvent.VK_RIGHT, false);
        keys.put(KeyEvent.VK_UP, false);
        keys.put(KeyEvent.VK_DOWN, false);
        keys.put(KeyEvent.VK_SHIFT, false);
    }

    public void initObjs(){

        currX = -200;
        currY = 250;
        currZ = 100;
        currT = Math.PI/4;//-Math.PI/2;
        currU = 0;
        currV = 0; // should not change the roll?
        currS = 5;
        fov = 100;

        objs = new ArrayList<>();

        try{

            RectPrism pog = new RectPrism(0, 0, 0, 100, 100, 100);

            //objs.add(new Line3D(100,200,0,300,700,0));
            objs.add(pog);

            for(int i=0; i < 8; i++){
                objs.add(new RectPrism(i*150, 0, 0, 50, 50, 500));
                objs.add(new RectPrism(i*150, 500, 0, 50, 50, 500));
            }
        }catch(Throwable t){
            System.out.println("Attempted to initialize bad object");
        }
    }

    public void update(Graphics window){

        // ----------------------------
        // Key Detection --------------
        // ----------------------------

        if(keys.get(KeyEvent.VK_W)){
            currY+=-currS*Math.cos(currT);
            currX+=currS*Math.sin(currT);
        }

        if(keys.get(KeyEvent.VK_S)){
            currY+=currS*Math.cos(currT);
            currX+=-currS*Math.sin(currT);
        }

        if(keys.get(KeyEvent.VK_A)){
            currX+=currS*Math.cos(currT);
            currY+=currS*Math.sin(currT);
        }
        
        if(keys.get(KeyEvent.VK_D)){
            currX+=-currS*Math.cos(currT);
            currY+=-currS*Math.sin(currT);
        }

        if(keys.get(KeyEvent.VK_T)){
            currT=0;
        }
        if(keys.get(KeyEvent.VK_U)){
            currU=0;
        }
        if(keys.get(KeyEvent.VK_V)){
            currV=0;
        }
        if(keys.get(KeyEvent.VK_ESCAPE)){
            Main.exit();
        }
        if(keys.get(KeyEvent.VK_F1)){
            Main.fullScreen();
        }
        
         
        if(keys.get(KeyEvent.VK_LEFT)){
            currT+=Math.PI/100;
            // currU+=(Math.PI/100)*Math.cos(currT);
            // currV+=-(Math.PI/100)*Math.sin(currT);
        }
    
        if(keys.get(KeyEvent.VK_RIGHT)){
            currT+=-Math.PI/100;
            // currU+=-(Math.PI/100)*Math.cos(currT);
            // currV+=(Math.PI/100)*Math.sin(currT);
        }

        if(keys.get(KeyEvent.VK_UP)){
            currU+=(Math.PI/100)*Math.cos(currT);
            currV+=(Math.PI/100)*Math.sin(currT);
        }
    
        if(keys.get(KeyEvent.VK_DOWN)){
            currU+=-(Math.PI/100)*Math.cos(currT);
            currV+=-(Math.PI/100)*Math.sin(currT);
        }

        if(keys.get(KeyEvent.VK_SHIFT)){
            currS=30;
            fov=100;
        }else{
            currS=5;
            fov=400;
        }

        // Should always be last
        paint(window);
    }

    public void paint(Graphics window){
	
        // set up the double buffering to make the animation nice and smooth
        Graphics2D twoDGraph = (Graphics2D)window;

        // take a snap shop of the current screen and same it as an image
        // that is the exact same width and height as the current screen
        if(back==null) back = (BufferedImage)(createImage(width,height));

        // create a graphics reference to the back ground image
        // we will draw all changes on the background image
        Graphics graphToBack = back.createGraphics();
        
        graphToBack.setColor(fillColor);
        graphToBack.fillRect(0,0,width,height);

        // ----------------------------
        // Drawing Stuff --------------
        // ----------------------------

        graphToBack.setColor(lineColor);

        for(Drawable d : objs)
            d.draw3D(graphToBack);

        graphToBack.fillOval((width/2) - 5, (height/2) - 5, 10, 10);

        debugStr =  "X : "+currX+"\n"+
                    "Y : "+currY+"\n"+
                    "Z : "+currZ+"\n"+
                    "T : "+currT%(2*Math.PI)+"\n"+
                    "U : "+currU%(2*Math.PI)+"\n"+
                    "V : "+currV%(2*Math.PI)+"\n";

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
    public static double currZ(){
        return currZ;
    }
    public static double currT(){
        return currT;
    }
    public static double currU(){
        return currU;
    }
    public static double currV(){
        return currV;
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
                Thread.currentThread().sleep(5);
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

    @Override
    public void mouseClicked(MouseEvent e) {
        System.out.println("Clicked"+e.getX()+" "+e.getY());
    }

    @Override
    public void mousePressed(MouseEvent e) {
        // TODO Auto-generated method stub
        
    }

    @Override
    public void mouseReleased(MouseEvent e) {
        //System.out.println(e.getX()+" "+e.getY());
    }

    @Override
    public void mouseEntered(MouseEvent e) {
        // TODO Auto-generated method stub
        
    }

    @Override
    public void mouseExited(MouseEvent e) {
        // TODO Auto-generated method stub
        
    }

    @Override
    public void mouseDragged(MouseEvent e) {
        System.out.println("Dragged"+e.getX()+" "+e.getY());
        
    }

    @Override
    public void mouseMoved(MouseEvent e) {
        currT += (lastMouseX-e.getX()) * Math.PI/1000;
        lastMouseX = e.getX();
        currU += (lastMouseY-e.getY()) * Math.PI/1000 * Math.cos(currT);
        currV += (lastMouseY-e.getY()) * Math.PI/1000 * Math.sin(currT);
        lastMouseY = e.getY();
    }
}

