package oscilattor.utils;
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
}
