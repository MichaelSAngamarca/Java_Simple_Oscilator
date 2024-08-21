package oscilattor;
import org.lwjgl.openal.AL;
import org.lwjgl.openal.ALC;
import oscilattor.utils.utils;

import java.util.function.Supplier;

import static org.lwjgl.openal.AL10.*;
import static org.lwjgl.openal.ALC10.*;
/*/
AudioThread is intended to emulate an audio thread to be passed in from the devices sound card
 */
public class AudioThread extends Thread{
    public static final int BUFFER_SIZE = 512; //size of each buffer at 512 hz
    public static final int BUFFER_COUNT = 8; //amount of buffers that will be in the buffers queue
    private final int[] buffers = new int[BUFFER_COUNT]; //buffer queue to be used for sample playback
    private final long device = alcOpenDevice(alcGetString(0,ALC_DEFAULT_DEVICE_SPECIFIER));//obtain the devices sound card.
    private final long context = alcCreateContext(device,new int[1]); //create a context that allows the AudioThread to know the context of the audio device(in this case the sound card)
    private final int source; //integer to emulate the source of the audio
    private int bufferIndex; //the current index of the buffer we aer on in teh buffers queue
    private boolean running; //flag to control the on and off signals of the thread
    private boolean closed; //flag to control if the program has closed
    private final Supplier<short[]> bufferSupplier; //supplier that

    public AudioThread(Supplier<short[]> bufferSupplier){
        this.bufferSupplier = bufferSupplier;
        alcMakeContextCurrent(context); //set the current specified context to the current context (context member)
        AL.createCapabilities(ALC.createCapabilities(device)); //
        source = alGenSources(); //points the source to the soundCard
        for(int i = 0; i < BUFFER_COUNT; i++){ //loops through the BUFFER_COUNT and calls the bufferSamples method in order obtain the audio data of the buffer queue
            bufferSamples(new short[0]);
        }
        alSourcePlay(source);
        catchInternalException();
        start();
    }


    @Override
    public synchronized void run(){
        while(!closed){
            while(!running){
                utils.handleProcedure(this::wait,false);
            }
            int processBuffers = alGetSourcei(source,AL_BUFFERS_PROCESSED);
            for(int i = 0 ; i < processBuffers; i++){
                short[] samples = bufferSupplier.get();
                if(samples == null){
                    running = false;
                    break;
                }
                alDeleteBuffers(alSourceUnqueueBuffers(source));
                buffers[bufferIndex] = alGenBuffers();
                bufferSamples(samples);
            }
            if(alGetSourcei(source,AL_SOURCE_STATE) != AL_PLAYING){
                alSourcePlay(source);
            }
            catchInternalException();
        }
        alDeleteBuffers(buffers);
        alDeleteSources(source);
        alcDestroyContext(context);
        alcCloseDevice(device);
    }

    public synchronized void playBack(){
        running = true;
        notify();
    }

    /*/

     */
    public synchronized void close(){
        closed = true;
        //break out of loop
        playBack();
    }

    public boolean isRunning(){
        return running;
    }

    /*//
    bufferSamples takes in a short array of samples from the queue and is used for playback of said buffers
    we create an integer that points to the next buffer in the array of buffers
    we fill the buffer with the audio data from the next buffer by calling alBufferData and defining the properties of the playback
    we queue the set of buffers on the source to be played in sequence
    if the bufferIndex reaches the BUFFER_COUNT then it will become 0 and start again at the beginning of the buffer queue
     */
    private void bufferSamples(short[] samples){
        int bufffer = buffers[bufferIndex++];
        alBufferData(bufffer,AL_FORMAT_MONO16,samples, gui.AudioInformation.SAMPLE_RATE); //This function fills a buffer with audio data set to the sample rate defined in the AudioInformation Class)
        alSourceQueueBuffers(source,bufffer); //Queue the set of buffers on the source to be played in sequence.
        bufferIndex %= BUFFER_COUNT; //if the bufferIndex reaches the BUFFER_COUNT then it will become 0 and start again at the beginning of the buffer queue

    }

    /*./
    Because alc cannot throw a regular runtime exception we have to create a method that calls a new class OpenAiRunTimeException that will create a whole new exception to be thrown
    we have an error integer that points to the method alcGetError which is used to obtain the exact numerical error code generated if an error occurs
    we run that error to the parameter of the OpenAlRunTimeExceptionClass and throw the appropriate message.
     */
    private void catchInternalException(){
        int error = alcGetError(device);
        if(error != ALC_NO_ERROR){
            throw new OpenAlRunTimeException(error);
        }
    }

}
