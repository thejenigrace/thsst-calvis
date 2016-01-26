package MainEditor.model;

import SimulatorVisualizer.controller.SystemController;

/**
 * Created by Goodwin Chua on 1/23/2016.
 */
public abstract class AssemblyComponent {

    protected SystemController sysCon;

    public abstract void update();

    public abstract void build();

    public void setSysCon(SystemController sysCon){
        this.sysCon = sysCon;
    }

}
