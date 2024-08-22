package oscilattor;

import oscilattor.utils.utils;

import javax.swing.*;
import javax.tools.Tool;
import java.awt.*;
import java.awt.event.ItemEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;
import java.awt.image.BufferedImage;
import java.util.Random;
public class Oscilattor extends SynthContorllerContainer {
    private static final int TONE_OFFSET_LIMIT = 200;
    private double keyFrequency;
    private int toneOffset;
    private int waveTableStepSize; //step size of each frame of the wave table
    private int waveTableIndex; // index of each frame we are on in the wave table

    private WaveTable waveTable = WaveTable.SINE;
    /*/
    Constructor that creates the basis of the design for a simple oscillator
     */
    public Oscilattor(gui synth) {
        super(synth);
        JComboBox<WaveTable> waveFormsJComboBox = new JComboBox<>(WaveTable.values());
        waveFormsJComboBox.setSelectedItem(WaveTable.SINE);
        waveFormsJComboBox.setSelectedIndex(0);
        waveFormsJComboBox.setBounds(10,10,75,25);
        waveFormsJComboBox.addItemListener(listner -> {
            if(listner.getStateChange() == ItemEvent.SELECTED){
                waveTable = (WaveTable) listner.getItem();
            }
        });
        add(waveFormsJComboBox);
        JLabel toneParameter = new JLabel("x0.00");
        toneParameter.setBounds(165,65,50,25);
        toneParameter.setBorder(utils.windowDesign.line_border);
        toneParameter.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                final Cursor MOUSE_CURSOR = Toolkit.getDefaultToolkit().createCustomCursor(new BufferedImage(16,16,BufferedImage.TYPE_INT_ARGB), new Point(0, 0), "mouseCursor");
                setCursor(MOUSE_CURSOR);
                mouseLocation = e.getLocationOnScreen();
            }

            @Override
            public void mouseReleased(MouseEvent e) {
                setCursor(Cursor.getDefaultCursor());
            }
        });
        toneParameter.addMouseMotionListener(new MouseMotionAdapter() {
            @Override
            public void mouseDragged(MouseEvent e) {
                if(mouseLocation.y != e.getYOnScreen()){
                    boolean mouseUp = mouseLocation.y - e.getYOnScreen() > 0;
                    if(mouseUp && toneOffset < TONE_OFFSET_LIMIT){
                        toneOffset++;

                    } else if (!mouseUp && toneOffset > -TONE_OFFSET_LIMIT) {
                        --toneOffset;
                    }
                    applyToneOffest();
                    toneParameter.setText("x" + String.format("%.3f",getToneOffset()));
                }
                utils.parameterHandling.PARAMETER_ROBOT.mouseMove(mouseLocation.x, mouseLocation.y);
            }
        });
        add(toneParameter);
        JLabel toneText = new JLabel("Tone");
        toneText.setBounds(172,40,75,25);
        add(toneText);
        setSize(279,100); //set the size of the panel on the main jframe gui
        setBorder(utils.windowDesign.line_border);//calls the class windowDesign in the utils class that is used as a template to easily create these panels
        setLayout(null);

    }

    public void setKeyFrequency(double frequency){
        keyFrequency = frequency;
        applyToneOffest();
    }

    private double getToneOffset(){
        return toneOffset / 100d;
    }

    private void applyToneOffest(){
        waveTableStepSize = (int)(WaveTable.sizeOfWaveTable * (keyFrequency * Math.pow(2,getToneOffset())) / gui.AudioInformation.SAMPLE_RATE);
    }

    /*/
    nextSample is used to obtain teh next sample in teh wave table

     */
    public double nextSample(){
        double sample = waveTable.getWaveTableSample()[waveTableIndex];
        waveTableIndex = (waveTableIndex + waveTableStepSize) % WaveTable.sizeOfWaveTable;
        return sample;
    }
}
