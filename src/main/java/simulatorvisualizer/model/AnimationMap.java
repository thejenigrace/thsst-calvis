package simulatorvisualizer.model;

import simulatorvisualizer.model.instructionanimation.*;

import java.util.HashMap;

/**
 * Created by Goodwin Chua on 5 Jul 2016.
 */
public class AnimationMap extends HashMap<String, CalvisAnimation> {

    public AnimationMap(){
        super();
        generateGP();
    }

    private void generateGP() {
        generateGPDataTransfer();
    }

    private void generateGPDataTransfer(){
        this.put("MOV", new Mov());
        this.put("STC", new Stc());
        this.put("CLC", new Clc());
        this.put("CMC", new Cmc());
        this.put("CLD", new Cld());
        this.put("STD", new Std());
        this.put("STI", new Sti());
        this.put("CLI", new Cli());
        this.put("SAHF", new Sahf());
        this.put("LAHF", new Lahf());
        this.put("PUSHF", new Pushf());
        this.put("PUSHFD", new Pushfd());
        this.put("POPF", new Popf());
        this.put("POPFD", new Popfd());
        this.put("AND", new And());
        this.put("OR", new Or());
        this.put("XOR", new Xor());
        this.put("NOT", new Not());
        this.put("TEST", new Test());
        this.put("BT", new Bt());
        this.put("BTS", new Bts());
        this.put("BTR", new Btr());
        this.put("BTC", new Btc());
        this.put("BSF", new Bsf());
        this.put("BSR", new Bsr());
        this.put("CBW", new Cbw());
        this.put("CWDE", new Cwde());
        this.put("CWD", new Cwd());
        this.put("CDQ", new Cdq());
        this.put("MOVSX", new Movsx());
        this.put("MOVZX", new Movzx());
        this.put("SHL", new Shl());
        this.put("SAL", new Sal());
        this.put("SHR", new Shr());
        this.put("SAR", new Sar());
        this.put("SHLD", new Shld());
        this.put("SHRD", new Shrd());
        this.put("SET", new Set());
    }

}
