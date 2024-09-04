package oscilattor;

import javax.swing.*;
import java.awt.*;

public class SynthContorllerContainer extends JPanel {
    protected boolean on;
    private gui synth;
    private Point mouseLocation;
    public Point getMouseLocation() {
        return mouseLocation;
    }

    public void setMouseLocation(Point mouseLocation) {
        this.mouseLocation = mouseLocation;
    }
    public SynthContorllerContainer(gui Synth){
        this.synth = Synth;
    }

    public boolean isOn(){
        return on;
    }

    public void setOn(boolean on){
        this.on = on;
    }

    @Override
    public Component add(Component comp) {
        comp.addKeyListener(synth.getKeyListener());
        return super.add(comp);
    }

    @Override
    public Component add(Component comp, int index){
        comp.addKeyListener(synth.getKeyListener());
        return super.add(comp, index);
    }

    @Override
    public Component add(String name, Component comp) {
        comp.addKeyListener(synth.getKeyListener());
        return super.add(name, comp);
    }

    @Override
    public void add(Component comp, Object constraints) {
        comp.addKeyListener(synth.getKeyListener());
        super.add(comp, constraints);
    }

    @Override
    public void add(Component comp, Object constraints, int index) {
        comp.addKeyListener(synth.getKeyListener());
        super.add(comp, constraints, index);
    }
}
