package SimulatorVisualizer.controller;

import EnvironmentConfiguration.model.CALVISInstruction;
import EnvironmentConfiguration.model.Memory;
import EnvironmentConfiguration.model.RegisterList;

import java.util.HashMap;
import java.util.Iterator;

/**
 * Created by Goodwin Chua on 12/11/2015.
 */
public class SimulationEngine {

    private RegisterList registers;
    private Memory memory;

    public SimulationEngine(RegisterList registers, Memory memory){
        this.registers = registers;
        this.memory = memory;
    }

    public void beginSimulation(HashMap<Integer, CALVISInstruction> m){
        HashMap<Integer, CALVISInstruction> map = m;
        Iterator<Integer> keys = map.keySet().iterator();
        while (keys.hasNext()){
            System.out.println("hello");
            Integer key = keys.next();
            map.get(key).execute(); // EXECUTE THE CALVIS INSTRUCTION
            registers.print();
        }
    }
}
