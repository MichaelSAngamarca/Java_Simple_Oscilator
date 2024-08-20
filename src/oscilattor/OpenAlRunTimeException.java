package oscilattor;
import static org.lwjgl.openal.AL10.*;


public class OpenAlRunTimeException extends RuntimeException {
    OpenAlRunTimeException(int errorCode){
        super("Internal" + (errorCode == AL_INVALID_NAME ? "invalid name" : errorCode == AL_INVALID_ENUM ? "invalid enum" : errorCode == AL_INVALID_VALUE ? "invalid value" : errorCode == AL_INVALID_OPERATION ? "invalid operation" : "unkown") + "OpenAl Exception");
    }
}
