package EnvironmentConfiguration.model;

/**
 * Created by Goodwin Chua on 12/11/2015.
 */
public class CALVISInstruction {
    private Instruction ins;
    private Object[] params;
    private RegisterList registers;
    private Memory memory;

    public CALVISInstruction(Instruction ins, Object[] params, RegisterList registers, Memory memory) {
        this.ins = ins;
        this.params = params;
        this.registers = registers;
        this.memory = memory;
    }

    public void execute(){
        int numParameters = params.length;
        try {
            switch(numParameters){
                case 0: this.ins.execute(registers, memory);
                        break;
                case 1: this.ins.execute((Token) params[0], registers, memory);
                        break;
                case 2: this.ins.execute((Token) params[0], (Token) params[1], registers, memory);
                        break;
                case 3: this.ins.execute((Token) params[0], (Token) params[1], (Token) params[2], registers, memory);
                        break;
            }
        } catch (Exception e){
            System.out.println("Instruction Execution Error: " + e.getCause());
            e.printStackTrace();
        }
    }
}
