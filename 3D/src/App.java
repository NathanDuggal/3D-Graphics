import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Canvas;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.KeyEvent;
import java.awt.Robot;
import java.awt.image.BufferedImage;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.*;
import static java.lang.Math.*;
import javax.swing.event.MouseInputListener;

public class App extends Canvas implements MouseInputListener, KeyListener, Runnable
{
    public static final Color fillColor = Color.BLACK;
    public static final Color lineColor = Color.WHITE;//new Color(255,0,255);

    public static final int width = FPSv1.WIDTH;
    public static final int height = FPSv1.HEIGHT;

    private boolean running = true;

    private static double currX;
    private static double currY;
    private static double currZ;
    private static double currS;
    private static double currSens;
    // In radians
    private static double currT;
    private static double currU;
    private static double currV;
    private static double fov;
    private static double defFov;

    private static String debugStr = "";

    private static int frameCount = 0;
    private static double currTime;
    private static double lastTime;
    private static NumberFormat f = new DecimalFormat("#0.00");     

    private BufferedImage back;

    // KeyEvents are represented as Integers
    private HashMap<Integer,Boolean> keys;

    // To fascilitate mouse controls
    private Robot r;
    private boolean mReset;

    // All non-player Objects
    private HashMap<String, Orientable> objs;
    private ArrayList<Orientable> toDraw;
    private double dir = 0.5;
    private double inc = 0;
  
    public App(){
        initInput();
        setBackground(fillColor);

        new Thread(this).start();

        setVisible(true);
        initObjs();
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
        keys.put(KeyEvent.VK_ENTER, false);
        keys.put(KeyEvent.VK_NUMPAD4, false);
        keys.put(KeyEvent.VK_NUMPAD6, false);
        keys.put(KeyEvent.VK_NUMPAD8, false);
        keys.put(KeyEvent.VK_NUMPAD2, false);
        keys.put(KeyEvent.VK_NUMPAD7, false);
        keys.put(KeyEvent.VK_NUMPAD9, false);
        keys.put(KeyEvent.VK_SHIFT, false);
        keys.put(KeyEvent.VK_CONTROL, false);
        keys.put(KeyEvent.VK_SPACE, false);
        keys.put(KeyEvent.VK_BACK_QUOTE, false);

        try{
            r = new Robot();
        }catch(Exception e){
            System.out.println("Robot init failed");
        }
        mReset=false;
        //r.mouseMove(width/2, height/2);
    }

    public void initObjs(){

        currX = -200;
        currY = 250;
        currZ = 100;
        currT = 0;
        currU = 0;
        currV = 0;
        currS = 1;
        currSens = 1;
        defFov = 400;
        fov = 400;

        objs = new HashMap<>();
        toDraw = new ArrayList<>();

        try{

            // objs.add(new Line3D(100,200,0,300,700,0));
            
            Line redLine = new Line(new Point3D(-10000,0,0), new Point3D(10000,0,0));

            objs.put(redLine.toString(),redLine);

            RectPrism originPrism = new RectPrism(0, 0, 0, 100, 100, 100);
            objs.put(originPrism.toString(), originPrism);

            // RectPrism bigPrism = new RectPrism(-5000, -5000, -5000, 10000, 10000, 10000);
            // objs.put("bigPrism",bigPrism);

            RectPrism rotPrism = new RectPrism(0, 0, 1000, 100, 100, 100);
            objs.put("rotPrism",rotPrism);

            for(int i=0; i < 8; i++){

                RectPrism rp1 = new RectPrism(i*150, 0, 0, 50, 50, 500);
                RectPrism rp2 = new RectPrism(i*150, 500, 0, 50, 50, 500);

                objs.put(rp1.toString(), rp1);
                objs.put(rp2.toString(), rp2);
            }

        }catch(Throwable t){
            System.out.println("Attempted to initialize bad object");
        }
    }

    public void update(Graphics window){

        // ----------------------------
        // Update Environment ---------
        // ----------------------------

        // Framecount check for major
        if(frameCount%30==0 && random() < 0.25) dir*=-1;
        inc+=dir;

        objs.put("rotPrism",new RectPrism(
            (int) (500*cos(inc*PI/200)), 
            (int) (500*sin(inc*PI/200)), 
            1000, 
            100, 
            100, 
            100, 
            (RectPrism)objs.get("rotPrism")
        ));

        // ----------------------------
        // Get Ordered List To Draw ---
        // ----------------------------

        toDraw = new ArrayList<>(objs.values());
        // ArrayList<RectPrism> toRemove = new ArrayList<>();

        // for(int i=0; i < toDraw.size(); i++){
        //     Orientable o = toDraw.get(i);
        //     if(o instanceof RectPrism){
        //         for(Line l : ((RectPrism) o).lines())
        //             toDraw.add(l);
        //         for(Quad q : ((RectPrism) o).faces())
        //             toDraw.add(q);
        //         toRemove.add((RectPrism)o);
        //     }
        // }

        // for(RectPrism r : toRemove)
        //     toDraw.remove(r);

        Collections.sort(toDraw, (Orientable a, Orientable b) -> {
            return b.getDist() - a.getDist();
        });

        // ----------------------------
        // Correct State Variables ----
        // ----------------------------

        currT=currT%(2*PI);
        currU=currU%(2*PI);

        if(currU < -PI/2)
            currU = -PI/2;
        if(currU > PI/2)
            currU = PI/2;
    
        // currV=currV%2*PI;

        // ----------------------------
        // Key Detection --------------
        // ----------------------------

        // Movement controls
        if(keys.get(KeyEvent.VK_W)){
            currX+=currS*cos(currT);
            currY+=-currS*sin(currT);
        }
        if(keys.get(KeyEvent.VK_S)){
            currX+=-currS*cos(currT);
            currY+=currS*sin(currT);
        }
        if(keys.get(KeyEvent.VK_A)){
            currX+=currS*sin(currT);
            currY+=currS*cos(currT);
        }
        if(keys.get(KeyEvent.VK_D)){
            currX+=-currS*sin(currT);
            currY+=-currS*cos(currT);
        }
        if(keys.get(KeyEvent.VK_CONTROL)){
            currZ+=-currS;
        }
        if(keys.get(KeyEvent.VK_SPACE)){
            currZ+=currS;
        }
        if(keys.get(KeyEvent.VK_SHIFT)){
            currS=10;
            fov=defFov/2;
        }else{
            currS=5;
            fov=defFov;
        }

        // Debug controls
        if(keys.get(KeyEvent.VK_T)){
            currT=0;
        }
        if(keys.get(KeyEvent.VK_U)){
            currU=0;
        }
        if(keys.get(KeyEvent.VK_V)){
            currV=0;
        }     
        if(keys.get(KeyEvent.VK_NUMPAD4)){
            currT+=-PI/500;
        }
        if(keys.get(KeyEvent.VK_NUMPAD6)){
            currT+=PI/500;
        }
        if(keys.get(KeyEvent.VK_NUMPAD8)){
            currU+=-(PI/500);
        }
        if(keys.get(KeyEvent.VK_NUMPAD2)){
            currU+=(PI/500);
        }
        if(keys.get(KeyEvent.VK_NUMPAD7)){
            defFov+=PI/200;
        }
        if(keys.get(KeyEvent.VK_NUMPAD9)){
            defFov+=-PI/200;
        }

        // Other controls
        if(keys.get(KeyEvent.VK_ESCAPE)){
            keys.put(KeyEvent.VK_ESCAPE, false);
            FPSv1.toggleSettings();
        }
        // if(keys.get(KeyEvent.VK_F1)){
        //     FPSv1.fullScreen();
        // }
        if(keys.get(KeyEvent.VK_ENTER)){
            RectPrism r = new RectPrism((int)currX, (int)currY, (int)currZ, 100, 100, 100);
            objs.put(r.toString(),r);
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

        // All objects handle their own rendering independently
        graphToBack.setColor(lineColor);
        for(Orientable d : toDraw){
            // d.draw2D(graphToBack);
            d.draw3D(graphToBack);
        }

        graphToBack.fillOval((width/2) - 5, (height/2) - 5, 10, 10);

        debugStr =  "X : "+f.format(currX)+"\n"+
                    "Y : "+f.format(currY)+"\n"+
                    "Z : "+f.format(currZ)+"\n"+
                    "T : "+f.format(currT)+"\n"+
                    "U : "+f.format(currU * sin(currT))+"\n"+
                    "V : "+f.format(currU * cos(currT))+"\n"+
                    "FOV : "+f.format(fov)+"\n"+
                    "Currently facing : < x: "+f.format(cos(currU)*sin(currT))+", z: "+f.format(cos(currU)*cos(currT))+", y: "+f.format(sin(currU))+">\n"+
                    "TPS : "+f.format((1000/(currTime-lastTime)))+"\n";

        graphToBack.setFont(new Font("Dialog",0,30));
        drawString(graphToBack, debugStr, 10, 10);

        twoDGraph.drawImage(back, null, 0, 0);
    }

    public void keyPressed(KeyEvent e){
        if(keys.keySet().contains(e.getKeyCode())){
            keys.put(e.getKeyCode(),true);
            // repaint();
        }
    }

    public void keyReleased(KeyEvent e){
        if(keys.keySet().contains(e.getKeyCode())){
            keys.put(e.getKeyCode(),false);
            // repaint();
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
        return currU;
    }
    public static double totU(){
        return currU;
    }
    public static double fov(){
        return fov;
    }
    public static int frameCount(){
        return frameCount;
    }
    public static void setSens(double sens){
        currSens = sens;
    }
    public static void setFOV(double fov){
        defFov = fov;
    }


    public static void drawString(Graphics g, String text, int x, int y) {
        for (String line : text.split("\n"))
          g.drawString(line, x, y += g.getFontMetrics().getHeight());
    }

    public void run(){
        try
        {
            while(running)
            {
                Thread.currentThread().sleep(1);
                frameCount++;
                frameCount=frameCount%Integer.MAX_VALUE;
                lastTime=currTime;
                currTime=System.currentTimeMillis();
                repaint();
            }
        }catch(Exception e)
        {
        }
    }

    public void close(){
        running=false;
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
        mouseMoved(e);
    }

    @Override
    public void mouseMoved(MouseEvent e) {

        if(!mReset){
            currU += (height/2-e.getY()) * PI/1000 * currSens;
            currT += (e.getX()-width/2) * PI/1000 * currSens;
            mReset=true;
            r.mouseMove(width/2,height/2);
        }else{
            mReset=false;
        }

        // if(FPSv1.main.isFocused()) r.mouseMove(width/2,height/2);
    }
}

