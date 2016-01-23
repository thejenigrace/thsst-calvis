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

    public void beginSimulation(HashMap<String, CALVISInstruction> m){
        HashMap<String, CALVISInstruction> map = m;
        Iterator<String> keys = map.keySet().iterator();

        while (map.containsKey(registers.get("EIP"))){
            String currentLine = registers.get("EIP");
            System.out.println("EIP: "+ currentLine);
            map.get(currentLine).execute(); // EXECUTE THE CALVIS INSTRUCTION
            int value = Integer.parseInt(currentLine, 16);
            value++;
            registers.set("EIP", Integer.toHexString(value));

            updateInterface();
        }
    }

    public Iterator<String> getRegisterKeys(){
        return this.registers.getRegisterKeys();
    }

    public void updateInterface(){
        //registers
    }

}
