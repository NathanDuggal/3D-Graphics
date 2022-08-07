import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import java.awt.event.*;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.BorderLayout;
import java.awt.GridLayout;

public class SettingsMenu extends JPanel implements KeyListener{
    private JSlider slider;
    private JButton exitButton;
    private JButton saveButton;
  
    public SettingsMenu(){
        super();
    
        slider = new JSlider(JSlider.HORIZONTAL);
        exitButton = new JButton("Exit");
        saveButton = new JButton("Return");
    
        exitButton.addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent e){
                FPSv1.exit();
            }
        });
        saveButton.addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent e){
                FPSv1.toggleSettings();
            }
        });

        exitButton.setFont(new Font("Arial", 0, 40));
        saveButton.setFont(new Font("Arial", 0, 40));
        slider.setMinimum(10);
        slider.setMaximum(200);
        slider.setValue(100);
    
        setBackground(Color.BLACK);

        JPanel p1 = new JPanel(new GridLayout(2,1));
        p1.setPreferredSize(new Dimension(FPSv1.WIDTH, 100));

        JLabel sens = new JLabel("Sensitivity: 1.0");
        sens.setFont(new Font("Arial", 0, 50));
        p1.add(sens);    

        slider.addChangeListener(new ChangeListener() {
            public void stateChanged(ChangeEvent e){
                sens.setText("Sensitivity: "+(double) slider.getValue()/100);
            }
        }); 

        JPanel sliderP = new JPanel(new GridLayout(1,3));
        sliderP.add(slider);
        sliderP.add(new JPanel());
        sliderP.add(new JPanel());
        p1.add(sliderP);
    
        JPanel p2 = new JPanel();
        p2.add(exitButton);
        p2.add(saveButton);
    
        add(p1);
        add(p2);

        // add(slider);
        // add(exitButton);
        // add(saveButton);
    }

    public int getSliderVal(){
        return slider.getValue();
    }

    @Override
    public void keyTyped(KeyEvent e) {
        // TODO Auto-generated method stub
        
    }

    @Override
    public void keyPressed(KeyEvent e) {
        System.out.println("Pressed");
        if(e.getKeyCode()==KeyEvent.VK_ESCAPE) FPSv1.exit();        
    }

    @Override
    public void keyReleased(KeyEvent e) {
        // TODO Auto-generated method stubw
        
    }
  }
  