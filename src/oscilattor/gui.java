package oscilattor;
import javax.swing.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.*;
public class gui {
    private final JFrame window = new JFrame("Oscilattor");
    private boolean shouldGenerate;
    private int wavePosition;
    private final AudioThread thread = new AudioThread(() ->{
        if(!shouldGenerate){
            return null;
        }
        short[] s = new short[AudioThread.BUFFER_SIZE];
        for(int i = 0; i < AudioThread.BUFFER_SIZE; ++i){
            s[i] = (short)(Short.MAX_VALUE * Math.sin((2*Math.PI * 440) / AudioInformation.SAMPLE_RATE*wavePosition++));
        }
        return s;
    });

    /*/
    constructor used to set the properties for the JFrame Window
     */
    public gui(){
        window.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if(!thread.isRunning()){
                    shouldGenerate = true;
                    thread.playBack();

                }
            }
            @Override
            public void keyReleased(KeyEvent e) {
                shouldGenerate = false;

            }
        });
        window.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                thread.close();
            }
        });
        window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        window.setSize(600,300);
        window.setResizable(false);
        window.setLayout(null);
        window.setLocationRelativeTo(null);
        window.setVisible(true);
    }

    public static class AudioInformation{
        public static int SAMPLE_RATE = 44100;
    }

}
