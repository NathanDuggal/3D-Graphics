import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSlider;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridLayout;

public class SlideableSetting extends JPanel{
    
    private JSlider slider;
    private JLabel label;
    // super scuffed that this is needed
    private JPanel sliderPanel;
    private double scalar;

    public SlideableSetting(){
        super();
    };

    public SlideableSetting(String name, double min, double max, double init) throws Exception{
        super(new GridLayout(2,1));

        setPreferredSize(new Dimension(FPSv1.WIDTH, 100));

        scalar=1;
        int check = 0;
        while(min%1 != 0 || max%1 != 0){
            scalar*=10;
            min*=10;
            max*=10;
            init*=10;
            if(check > 100) throw new Exception();
        }

        slider = new JSlider(JSlider.HORIZONTAL, (int) min, (int) max, (int) init);
        slider.addChangeListener(new ChangeListener() {
            public void stateChanged(ChangeEvent e){
                label.setText(name+": "+slider.getValue()/scalar);
            }
        }); 

        label = new JLabel(name+": "+init/scalar);
        label.setFont(new Font("Arial", 0, 50));   
        
        // Terrible
        sliderPanel = new JPanel(new GridLayout(1,3));
        sliderPanel.add(slider);
        sliderPanel.add(new JPanel());
        sliderPanel.add(new JPanel());

        add(label);
        add(sliderPanel);
    }

    public double getValue(){
        return slider.getValue()/scalar;
    }
}
