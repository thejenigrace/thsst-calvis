package Editor.controller;

import Editor.view.View;
import EnvironmentConfiguration.model.CALVISInstruction;
import EnvironmentConfiguration.model.CALVISParser;
import SimulatorVisualizer.controller.SimulationEngine;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.HashMap;
import java.util.Iterator;

/**
 * Created by Goodwin Chua on 12/11/2015.
 */
public class EditorController {

    private CALVISParser parser;
    private View v;
    private SimulationEngine sim;

    public EditorController(CALVISParser parser, SimulationEngine sim){
        this.parser = parser;
        this.sim = sim;

        this.v = new View();
        this.v.addActionListener(eval);
    }

    ActionListener eval = new ActionListener(){
        @Override
        public void actionPerformed(ActionEvent e) {
            // TODO Auto-generated method stub
            System.out.println("Parsing: \n" + v.getCode());
            HashMap<Integer, CALVISInstruction> mappedInstruction = parser.parse(v.getCode());
            sim.beginSimulation(mappedInstruction);
        }

    };

}
