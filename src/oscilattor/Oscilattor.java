package oscilattor;

import oscilattor.utils.RefWrapper;
import oscilattor.utils.utils;
import javax.swing.*;
import java.awt.event.ItemEvent;
public class Oscilattor extends SynthContorllerContainer {
    private static final int TONE_OFFSET_LIMIT = 200;
    private double keyFrequency;
    private RefWrapper<Integer> toneOffset = new RefWrapper<>(0);
    private RefWrapper<Integer> volume = new RefWrapper<>(100);
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
            synth.updateWaveViewer();
        });
        add(waveFormsJComboBox);
        JLabel toneParameter = new JLabel("x0.00");
        toneParameter.setBounds(165,65,50,25);
        toneParameter.setBorder(utils.windowDesign.line_border);
        utils.parameterHandling.addParameterMouseListeners(toneParameter,this,-TONE_OFFSET_LIMIT,TONE_OFFSET_LIMIT,1,toneOffset,() ->{
            applyToneOffest();
            toneParameter.setText(" x" + String.format("%.3f",getToneOffset()));
            synth.updateWaveViewer();
        });
        add(toneParameter);
        JLabel toneText = new JLabel("Tone");
        toneText.setBounds(172,40,75,25);
        add(toneText);

        JLabel volumeParameter = new JLabel("100%");
        volumeParameter.setBounds(222,65,50,25);
        volumeParameter.setBorder(utils.windowDesign.line_border);
        utils.parameterHandling.addParameterMouseListeners(volumeParameter,this,0,100,1,volume,() ->
        {
            volumeParameter.setText(" " + volume.value + "%");
            synth.updateWaveViewer();
        });
        add(volumeParameter);
        JLabel volumeText = new JLabel("Volume");
        volumeText.setBounds(222,40,75,25);
        add(volumeText);

        setSize(279,100); //set the size of the panel on the main jframe gui
        setBorder(utils.windowDesign.line_border);//calls the class windowDesign in the utils class that is used as a template to easily create these panels
        setLayout(null);

    }

    /*/
   nextSample is used to obtain teh next sample in teh wave table

    */
    public double nextSample(){
        double sample = waveTable.getWaveTableSample()[waveTableIndex] * getVolumeMultiplier();
        waveTableIndex = (waveTableIndex + waveTableStepSize) % WaveTable.sizeOfWaveTable;
        return sample;
    }

    public void setKeyFrequency(double frequency){
        keyFrequency = frequency;
        applyToneOffest();
    }
    private double getVolumeMultiplier(){
        return volume.value / 100.0;
    }

    private double getToneOffset(){
        return toneOffset.value/ 1000d;
    }

    public double[] getSampleWaveForm(int numberOfSamples){
        double[] samples = new double[numberOfSamples];
        double frequency = 1.0/(numberOfSamples / (double)gui.AudioInformation.SAMPLE_RATE);
        int index  = 0;
        int stepSize = (int)(WaveTable.sizeOfWaveTable * utils.MathHandle.offSetTone(frequency, getToneOffset()) / gui.AudioInformation.SAMPLE_RATE);
        for(int i = 0; i < numberOfSamples; i++){
            samples[i] = waveTable.getWaveTableSample()[index] * getVolumeMultiplier();
            index = (index + stepSize) % WaveTable.sizeOfWaveTable;
        }
        return samples;
    }

    private void applyToneOffest(){
        waveTableStepSize = (int)(WaveTable.sizeOfWaveTable * utils.MathHandle.offSetTone(keyFrequency, getToneOffset()) / gui.AudioInformation.SAMPLE_RATE);
    }
}
