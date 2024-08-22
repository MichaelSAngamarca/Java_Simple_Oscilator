package oscilattor;

import oscilattor.utils.utils;

enum WaveTable {
    SINE,SQUARE,SAW,TRIANGLE;

    static final int sizeOfWaveTable = 8192; //size of the wave table I am using

    private final float [] waveTableSample = new float[sizeOfWaveTable]; //float array that is used to represent all the samples contained in the wave table

    static{

        final double fundementalFrequency = 1d/((double) sizeOfWaveTable / gui.AudioInformation.SAMPLE_RATE); // this is the fundamental frequency for all samples in the wave table
        /*/
        for loop that iterates through the float array of wave table samples (each sample being i) and calculates the equation for each specific of the 4 types of audio waves.
         */
        for(int i = 0; i < sizeOfWaveTable; i++){
            double t = (double) i / gui.AudioInformation.SAMPLE_RATE; // obtain the value for time
            double tDivP = t / (1d/fundementalFrequency); // variable to store the value of the equation for time divided by the amplitude p
            SINE.waveTableSample[i] = (float) Math.sin(utils.MathHandle.frequencyConverter(fundementalFrequency) * t); //equation for sine wave
            SAW.waveTableSample[i] = (float) (2d * (tDivP - Math.floor(0.5 + tDivP))); //equation for saw wave
            SQUARE.waveTableSample[i] = (float)(Math.signum(Math.sin(utils.MathHandle.frequencyConverter(fundementalFrequency) * t))); //equation for square wave
            TRIANGLE.waveTableSample[i] = (float)(2d * Math.abs(SINE.waveTableSample[i] - 1d));//equation for triangle wave
            System.out.println(SQUARE.waveTableSample[i]);
        }
    }

    public float[] getWaveTableSample(){
        return waveTableSample; //get whatever wave table sample we are currently on in the float array.
    }
}