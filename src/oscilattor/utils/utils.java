package oscilattor.utils;

import javax.swing.*;
import javax.swing.border.Border;
import java.awt.*;
import java.lang.Math;

import static java.lang.Math.*;

/*/
Simple Helper Class that is used in helping when trying to invoke methods like wait
 */
public class utils {
    public static void handleProcedure(Procedure procedure,  boolean printStackTrace){
        try{
            procedure.invoke();
        }
        catch (Exception e){
            if(printStackTrace){
                e.printStackTrace();
            }

        }
    }
    /*/
    creates a static final Border and simply class the .createLineBorder method to set the color as black.
     */
    public static class windowDesign{
        public static final Border line_border = BorderFactory.createLineBorder(Color.black);
    }

    /*/
    this class simply handles the conversion of the frequency.
     */
    public static class MathHandle{
        public static double frequencyConverter(double frequency){
            return 2 * PI * frequency;

        }
        public static double frequencyMath(int keyNumber){
            return pow(2,((double) (keyNumber - 49) /12)) * 440;
        }
    }

    public static class parameterHandling{
        public static final Robot PARAMETER_ROBOT;
        static{
            try{
                PARAMETER_ROBOT = new Robot();
            }
            catch (AWTException e){
                throw new ExceptionInInitializerError("Cannot Construct Robot");
            }
        }
    }
}
