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
    private waveForms waveForm = waveForms.SINE;
    private int wavePosition;
    private final Random random = new Random();
    private double FREQUENCY;
    private double keyFrequency;
    private int toneOffset;
    /*/
    Constructor that creates the basis of the design for a simple oscillator
     */
    public Oscilattor(gui synth) {
        super(synth);
        JComboBox<waveForms> waveFormsJComboBox = new JComboBox<>(waveForms.values());
        waveFormsJComboBox.setSelectedIndex(0);
        waveFormsJComboBox.setBounds(10,10,75,25);
        waveFormsJComboBox.addItemListener(listner -> {
            if(listner.getStateChange() == ItemEvent.SELECTED){
                waveForm = waveForms.valueOf(waveFormsJComboBox.getSelectedItem().toString());
                System.out.println(waveForm);
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

    public double getKeyFrequency(){
        return FREQUENCY;
    }

    public void setKeyFrequency(double frequency){
        keyFrequency = this.FREQUENCY= frequency;
        applyToneOffest();
    }

    private double getToneOffset(){
        return toneOffset / 100d;
    }

    private void applyToneOffest(){
        FREQUENCY = keyFrequency * Math.pow(2,getToneOffset());
    }
    /*/
    enum that is used to represent the 5 essential wave forms
     */
    private enum waveForms{
        SINE,SAW,SQUARE,TRIANGLE,NOISE
    }

    public double nextSample(){
        double tDivP = (wavePosition++ / (double) gui.AudioInformation.SAMPLE_RATE) / (1d/FREQUENCY);
        switch (waveForm){
            case SINE:
                return Math.sin(utils.MathHandle.frequencyConverter(FREQUENCY) * wavePosition / gui.AudioInformation.SAMPLE_RATE);
            case SAW:
                return 2d*(tDivP - Math.floor(0.5 + tDivP));
            case SQUARE:
                return Math.signum(Math.sin(utils.MathHandle.frequencyConverter(FREQUENCY) * wavePosition / gui.AudioInformation.SAMPLE_RATE));
            case TRIANGLE:
                return 2d * Math.abs(2d*(tDivP - Math.floor(0.5 + tDivP))) - 1;
            case NOISE:
                return random.nextDouble();
            default:
                throw new RuntimeException("Unkown WaveSignal");
        }
    }
}
