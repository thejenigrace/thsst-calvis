package thsst.calvis.simulatorvisualizer.model;

import thsst.calvis.simulatorvisualizer.model.instructionanimation.*;

import java.util.HashMap;

/**
 * Created by Goodwin Chua on 5 Jul 2016.
 */
public class AnimationMap extends HashMap<String, CalvisAnimation> {

    public AnimationMap() {
        super();
        generateGP();
    }

    private void generateGP() {
        this.generateGPDataTransfer();
        this.generateGPBinaryArithmetic();
        this.generateGPDecimalArithmetic();
        this.generateGPRotate();
        this.generateGPStack();
        this.generateGPJump();
        this.generateDataTransfer();
        this.generateMMXPacked();
        this.generateMMXUnpacked();
        this.generateMMXLogical();
        this.generateSSEUnpacked();
        this.generateSSELogical();
        this.generateSSE2Unpacked();
        this.generateSSE2Logical();
    }

    private void generateGPDataTransfer() {
        this.put("MOV", new Mov());
        this.put("LEA", new Lea());
        this.put("XLAT", new Xlat());
        this.put("CMOV", new Cmov());
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
        this.put("XCHG", new Xchg());
        this.put("BSWAP", new Bswap());
        this.put("XADD", new Xadd());
        this.put("CMPXCHG", new Cmpxchg());
        this.put("CMPXCHGS8", new Cmpxchgs8());
    }

    private void generateGPBinaryArithmetic() {
        this.put("ADD", new Add());
        this.put("ADC", new Ad("CF", true));
        this.put("ADCX", new Ad("CF", false));
        this.put("ADOX", new Ad("OF", false));
        this.put("SUB", new Sub());
        this.put("SBB", new Sbb());
        this.put("INC", new Inc());
        this.put("DEC", new Dec());
        this.put("CMP", new Cmp());
        this.put("NEG", new Neg());
        this.put("MUL", new Mul());
        this.put("IMUL", new Imul());
    }

    private void generateGPDecimalArithmetic() {
        this.put("DAA", new Daa());
        this.put("DAS", new Das());
        this.put("AAA", new Aaa());
        this.put("AAS", new Aas());
        this.put("AAM", new Aam());
    }

    private void generateGPRotate(){
        this.put("ROR", new Ror());
        this.put("ROL", new Rol());
        this.put("RCR", new Rcr());
        this.put("RCL", new Rcl());
    }

    private void generateGPStack(){
        this.put("PUSH", new Push());
        this.put("PUSHA", new Pusha());
        this.put("PUSHAD", new Pushad());
        this.put("POP", new Pop());
        this.put("POPA", new Popa());
        this.put("POPAD", new Popad());
    }

    private void generateGPJump(){
        this.put("JMP", new Jmp());
        this.put("J", new J());
        this.put("JECXZ", new Jecxz());
        this.put("JCXZ", new Jcxz());
        this.put("LOOPE", new Loope());
        this.put("LOOPNE", new Loopne());
        this.put("LOOPZ", new Loopz());
        this.put("LOOPNZ", new Loopnz());
        this.put("LOOP", new Loop());
    }

    private void generateDataTransfer(){
        this.put("MOVD", new Movd());
        this.put("MOVQ", new Movq());
        this.put("MAXSS", new Maxss());
        this.put("MAXPS", new Maxps());
//        this.put("MOVDQ2Q", new Movdq2q());
//        this.put("MOVDQA", new Movdqa());
//        this.put("MOVDQU", new Movdqu());
//        this.put("MOVHLPS", new Movhlps());
//        this.put("MOVHLPD", new Movhlpd());
//        this.put("MOVHPD", new Movhpd());
//        this.put("MOVHPS", new Movhps());
//        this.put("MOVLPD", new Movlpd());
//        this.put("MOVLPS", new Movlps());
//        this.put("MOVMSKPD", new Movmskpd());
//        this.put("MOVMSKPS", new Movmskps());
//        this.put("MOVQ2DQ", new Movq2dq());
//        this.put("MOVSB", new Movsb());
//        this.put("MOVSD", new Movsd());
//        this.put("MOVSS", new Movss());
//        this.put("MOVSW", new Movsw());
        this.put("MINSD", new Minsd());
    }

    private void generateMMXPacked() {
        this.put("PACKSSWB", new Packsswb());
        this.put("PACKSSDW", new Packssdw());
        this.put("PACKUSWB", new Packuswb());
    }

    private void generateMMXUnpacked() {
        this.put("PUNPCKHBW", new Punpckhbw());
        this.put("PUNPCKHWD", new Punpckhwd());
        this.put("PUNPCKHDQ", new Punpckhdq());
        this.put("PUNPCKLBW", new Punpcklbw());
        this.put("PUNPCKLWD", new Punpcklwd());
        this.put("PUNPCKLDQ", new Punpckldq());
    }

    private void generateMMXLogical() {
        this.put("PAND", new Pand());
        this.put("PANDN", new Pandn());
        this.put("POR", new Por());
        this.put("PXOR", new Pxor());
    }

    private void generateSSELogical() {
        this.put("ANDPS", new Andps());
        this.put("ANDNPS", new Andnps());
        this.put("ORPS", new Orps());
        this.put("XORPS", new Xorps());
    }

    private void generateSSEUnpacked() {
        this.put("SHUFPS", new Shufps());
        this.put("UNPCKHPS", new Unpckhps());
        this.put("UNPCKLPS", new Unpcklps());
    }

    private void generateSSE2Logical() {
        this.put("ANDPD", new Andpd());
        this.put("ANDNPD", new Andnpd());
        this.put("ORPD", new Orpd());
        this.put("XORPD", new Xorpd());
    }

    private void generateSSE2Unpacked() {
        this.put("SHUFPD", new Shufpd());
        this.put("UNPCKHPD", new Unpckhpd());
        this.put("UNPCKLPD", new Unpcklpd());
    }
}
