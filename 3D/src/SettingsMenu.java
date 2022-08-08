import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import java.awt.event.*;
import java.util.HashMap;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.BorderLayout;
import java.awt.GridLayout;

public class SettingsMenu extends JPanel implements KeyListener{
    private HashMap<String, SlideableSetting> settings;
    private JButton exitButton;
    private JButton saveButton;
  
    public SettingsMenu(){
        super();
        
        exitButton = new JButton("Exit");
        exitButton.setFont(new Font("Arial", 0, 40));
        exitButton.addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent e){
                FPSv1.exit();
            }
        });
        saveButton = new JButton("Return");
        saveButton.setFont(new Font("Arial", 0, 40));
        saveButton.addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent e){
                FPSv1.toggleSettings();
            }
        });

        JPanel buttonPanel = new JPanel();
        buttonPanel.add(exitButton);
        buttonPanel.add(saveButton);

        setBackground(Color.BLACK);

        settings = new HashMap<>();

        try{
            settings.put("Sensitivity", new SlideableSetting("Sensitivity", 0.1, 2.5, 1));
            settings.put("FOV", new SlideableSetting("FOV", 100, 1000, 400));
        }catch(Exception e){
            System.out.println("Was unable to initialize setting");
        }

        for(SlideableSetting s : settings.values()) add(s);
        add(buttonPanel);
    }

    public double getSliderVal(String key){
        return settings.get(key).getValue();
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
  