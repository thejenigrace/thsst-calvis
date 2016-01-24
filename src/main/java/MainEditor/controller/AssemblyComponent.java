package MainEditor.controller;

/**
 * Created by Goodwin Chua on 1/23/2016.
 */
public abstract class AssemblyComponent {

    protected SystemController sysCon;

    public abstract void update();

    public void setSysCon(SystemController sysCon){
        this.sysCon = sysCon;
    }

}
