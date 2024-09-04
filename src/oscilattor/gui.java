package oscilattor;
import oscilattor.utils.utils;

import javax.swing.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.*;
public class gui {
    private boolean shouldGenerate; //boolean flag to control if audio should be playing
    private Oscilattor[] oscilattors = new Oscilattor[3];
    private static final HashMap<Character,Double> KEY_FREQ = new HashMap<>(); //Hashmap in order to store the characters of the midi controller and the frequencies that correlate to that note
    private final WaveViewer waveViewer = new WaveViewer(oscilattors);
    static{
        final int Starting_key = 16;
        final int key_freq_increment = 2;
        final char[] keys = "zxcvbnm,./asdfghjkl;'#qwertyuiop[]".toCharArray();
        for(int i = Starting_key, key = 0; i < keys.length * key_freq_increment + Starting_key; i+=key_freq_increment,++key){
            KEY_FREQ.put(keys[key],utils.MathHandle.frequencyMath(i));
        }
    }
    private final AudioThread thread = new AudioThread(() ->{
        if(!shouldGenerate){
            return null;
        }
        short[] s = new short[AudioThread.BUFFER_SIZE];
        for(int i = 0; i < AudioThread.BUFFER_SIZE; i++){
            double d = 0;
            for(Oscilattor o : oscilattors){
                d+= o.nextSample() / oscilattors.length;
            }
            s[i] = (short)(Short.MAX_VALUE * d);
        }
        return s;
    });

    private final KeyAdapter keyListener = new KeyAdapter() {
        @Override
        public void keyPressed(KeyEvent e) {
            if(!KEY_FREQ.containsKey(e.getKeyChar())){
                return;
            }
            if(!thread.isRunning()){
                for(Oscilattor o: oscilattors){
                    o.setKeyFrequency(KEY_FREQ.get(e.getKeyChar()));
                }
                shouldGenerate = true;
                thread.playBack();
            }
        }
        @Override
        public void keyReleased(KeyEvent e) {
            shouldGenerate = false;

        }
    };

    /*/
    constructor used to set the properties for the JFrame Window and to take in MIDI Input
     */
    public gui(){
        JFrame window = new JFrame("Oscilattor");
        int y  = 0;
        for(int i = 0; i < oscilattors.length; i++){
            oscilattors[i] = new Oscilattor(this);
            oscilattors[i].setLocation(5,y);
            window.add(oscilattors[i]);
            y += 105;
        }
        waveViewer.setBounds(290,0,320,300);
        window.add(waveViewer);
        window.addKeyListener(keyListener);
        window.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                thread.close();
            }
        });
        window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        window.setSize(640,345);
        window.setResizable(false);
        window.setLayout(null);
        window.setLocationRelativeTo(null);
        window.setVisible(true);
    }

    public KeyAdapter getKeyListener() {
        return keyListener;
    }

    public void updateWaveViewer(){
        waveViewer.repaint();
    }
    /*/
        Set the Sample_Rate for the audio thread of 44100;
    */
    public static class AudioInformation{
        public static int SAMPLE_RATE = 44100;
    }

}
