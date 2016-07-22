package thsst.calvis.simulatorvisualizer.animation;

import thsst.calvis.simulatorvisualizer.animation.instruction.gp.*;
import thsst.calvis.simulatorvisualizer.animation.instruction.mmx.*;
import thsst.calvis.simulatorvisualizer.animation.instruction.sse.Cmpps;
import thsst.calvis.simulatorvisualizer.animation.instruction.sse.Cmpss;
import thsst.calvis.simulatorvisualizer.animation.instruction.sse2.*;
import thsst.calvis.simulatorvisualizer.animation.instruction.x87.*;
import thsst.calvis.simulatorvisualizer.model.CalvisAnimation;

import java.util.HashMap;

/**
 * Created by Goodwin Chua on 5 Jul 2016.
 */
public class AnimationMap extends HashMap<String, CalvisAnimation> {

    public AnimationMap() {
        super();
        this.generateGP();
        this.generateMMX();
        this.generateSSE();
        this.generateSSE2();
        this.generatex87();
    }

    private void generateGP() {
        this.generateGPDataTransfer();
        this.generateGPBinaryArithmetic();
        this.generateGPDecimalArithmetic();
        this.generateGPRotate();
        this.generateGPStack();
        this.generateGPJump();
    }

    private void generateMMX() {
        this.generateMMXDataTransfer();
        this.generateMMXComparison();
        this.generateMMXPacked();
        this.generateMMXUnpacked();
        this.generateMMXLogical();
        this.generateMMXShift();
    }

    private void generateSSE() {
        this.generateSSEDataTransfer();
        this.generateSSEComparison();
        this.generateSSEUnpacked();
        this.generateSSELogical();
    }

    private void generateSSE2() {
        this.generateSSE2DataTransfer();
        this.generateSSE2Comparison();
        this.generateSSE2Unpacked();
        this.generateSSE2Logical();
    }

    private void generatex87(){
        this.generatex87DataTransfer();
        this.generatex87Exchange();
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

    private void generateGPRotate() {
        this.put("ROR", new Ror());
        this.put("ROL", new Rol());
        this.put("RCR", new Rcr());
        this.put("RCL", new Rcl());
    }

    private void generateGPStack() {
        this.put("PUSH", new Push());
        this.put("PUSHA", new Pusha());
        this.put("PUSHAD", new Pushad());
        this.put("POP", new Pop());
        this.put("POPA", new Popa());
        this.put("POPAD", new Popad());
    }

    private void generateGPJump() {
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

    private void generateMMXDataTransfer() {
        this.put("MOVD", new Movd());
        this.put("MOVQ", new Movq());
        this.put("MAXSS", new Maxss());
        this.put("MAXPS", new Maxps());
        this.put("MINSD", new Minsd());
    }

    private void generateMMXComparison() {
        this.put("PCMPEQB", new Pcmpeq(1));
        this.put("PCMPEQW", new Pcmpeq(2));
        this.put("PCMPEQD", new Pcmpeq(3));
        this.put("PCMPGTB", new Pcmpgt(1));
        this.put("PCMPGTW", new Pcmpgt(2));
        this.put("PCMPGTD", new Pcmpgt(3));
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

    private void generateMMXShift() {
        this.put("PSLLW", new Psllw());
        this.put("PSLLD", new Pslld());
        this.put("PSLLQ", new Psllq());
        this.put("PSRAW", new Psraw());
        this.put("PSRAD", new Psrad());
    }

    private void generateSSEDataTransfer() {
//        this.put("MOVDQ2Q", new Movdq2q());
//        this.put("MOVDQA", new Movdqa());
//        this.put("MOVDQU", new Movdqu());
//        this.put("MOVHLPS", new Movhlps());
//        this.put("MOVHPS", new Movhps());
//        this.put("MOVLPS", new Movlps());
//        this.put("MOVMSKPS", new Movmskps());
//        this.put("MOVQ2DQ", new Movq2dq());
//        this.put("MOVSB", new Movsb());
//        this.put("MOVSS", new Movss());
//        this.put("MOVSW", new Movsw());
    }

    private void generateSSEComparison() {
        this.put("CMPSS", new Cmpss());
        this.put("CMPPS", new Cmpps());
    }

    private void generateSSE2Comparison() {
        this.put("CMPSD", new Cmpsd());
        this.put("CMPPD", new Cmpsd());
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

    private void generateSSE2DataTransfer() {
//        this.put("MOVHLPD", new Movhlpd());
//        this.put("MOVHPD", new Movhpd())
//        this.put("MOVLPD", new Movlpd());
//        this.put("MOVMSKPD", new Movmskpd());
//        this.put("MOVSD", new Movsd());
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

    private void generatex87DataTransfer() {
        this.put("FLD", new Fld());
        this.put("FST", new Fst());
        this.put("FSTP", new Fstp());
        this.put("FILD", new Fild());
        this.put("FIST", new Fist());
        this.put("FISTP", new Fistp());
        this.put("FBLD", new Fbld());
        this.put("FBSTP", new Fbstp());
        this.put("FLD1", new Fld1());
        this.put("FLDZ", new Fldz());
        this.put("FLDPI", new Fldpi());
        this.put("FLDL2E", new Fldl2e());
        this.put("FLDLN2", new Fldln2());
        this.put("FLDL2T", new Fldl2t());
        this.put("FLDLG2", new Fldlg2());
    }

    private void generatex87Exchange() {
        this.put("FXCH", new Fxch());
    }

}
