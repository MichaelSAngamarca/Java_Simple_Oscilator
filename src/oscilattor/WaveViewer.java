package oscilattor;

import oscilattor.utils.utils;

import javax.swing.*;
import java.awt.*;
import java.util.function.Function;

public class WaveViewer extends JPanel {
    private Oscilattor[] oscilattors;

    public WaveViewer(Oscilattor[] oscilattors) {
        this.oscilattors = oscilattors;
        setBorder(utils.windowDesign.line_border);

    }

    @Override
    public void paintComponent(Graphics g) {
        Graphics2D graphics2D = (Graphics2D) g;
        final int PAD = 25;
        super.paintComponent(g);
        int numOfSamples = getWidth() - PAD * 2;
        double [] mixedSamples = new double[numOfSamples];
        graphics2D.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        for(Oscilattor oscilattor : oscilattors) {
            double[] samples = oscilattor.getSampleWaveForm(numOfSamples);
            for(int i = 0; i < samples.length; i++) {
                mixedSamples[i] += samples[i] / oscilattors.length;
            }
        }
        int midy = getHeight() / 2;
        Function<Double,Integer> sampleToYCord = sample -> (int)(midy + sample * (midy - PAD));
        graphics2D.drawLine(PAD,midy,getWidth()-PAD,midy);
        graphics2D.drawLine(PAD,PAD,PAD,getHeight()-PAD);
        for(int i = 0; i < numOfSamples; i++) {
            int nextY = i ==numOfSamples - 1 ? sampleToYCord.apply(mixedSamples[i]) : sampleToYCord.apply(mixedSamples[i + 1]);
            graphics2D.drawLine(PAD + i,sampleToYCord.apply(mixedSamples[i]), PAD + i + 1, nextY );
        }

    }
}
