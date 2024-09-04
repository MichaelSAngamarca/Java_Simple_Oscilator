package oscilattor.utils;

import oscilattor.SynthContorllerContainer;

import javax.swing.*;
import javax.swing.border.Border;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;
import java.awt.image.BufferedImage;

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
        public static double offSetTone(double baseFrequency, double frequencyMultiplier){
            return baseFrequency * pow(2.0,frequencyMultiplier);
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

        public static void addParameterMouseListeners(Component component, SynthContorllerContainer container, int minValue, int maxValue, int valueStep,RefWrapper<Integer> parameter, Procedure onChangeProcedure){
            component.addMouseListener(new MouseAdapter() {
                @Override
                public void mouseReleased(MouseEvent e) {
                    component.setCursor(Cursor.getDefaultCursor());
                }

                @Override
                public void mousePressed(MouseEvent e) {
                    final Cursor MOUSE_CURSOR = Toolkit.getDefaultToolkit().createCustomCursor(new BufferedImage(16,16,BufferedImage.TYPE_INT_ARGB), new Point(0, 0), "mouseCursor");
                    component.setCursor(MOUSE_CURSOR);
                    container.setMouseLocation(e.getLocationOnScreen());
                    component.setCursor(Cursor.getDefaultCursor());
                }
            });
            component.addMouseMotionListener(new MouseMotionAdapter() {
                @Override
                public void mouseDragged(MouseEvent e) {
                    if(container.getMouseLocation().y != e.getYOnScreen()){
                        boolean mouseMoveUp = container.getMouseLocation().y - e.getYOnScreen() > 0;
                        if(mouseMoveUp && parameter.value < maxValue){
                            parameter.value += valueStep;
                        }
                        else if(!mouseMoveUp && parameter.value > minValue){
                            parameter.value -= valueStep;
                        }
                        if(onChangeProcedure != null){
                            handleProcedure(onChangeProcedure, true);
                        }
                    }
                    PARAMETER_ROBOT.mouseMove(container.getMouseLocation().x,container.getMouseLocation().y);
                }
            });
        }
    }
}
