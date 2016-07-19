package thsst.calvis.simulatorvisualizer.animation;

import thsst.calvis.simulatorvisualizer.animation.instruction.mmx.Pcmpeq;
import thsst.calvis.simulatorvisualizer.animation.instruction.sse.*;
import thsst.calvis.simulatorvisualizer.animation.instruction.gp.*;
import thsst.calvis.simulatorvisualizer.model.CalvisAnimation;

import java.util.HashMap;

/**
 * Created by Goodwin Chua on 5 Jul 2016.
 */
public class AnimationMap extends HashMap<String, CalvisAnimation> {

    public AnimationMap() {
        super();
        generateGP();
        this.generateMMX();
        this.generateSSE();
        this.generateSSE2();
    }

    private void generateGP() {
        this.generateGPDataTransfer();
        this.generateGPBinaryArithmetic();
        this.generateGPDecimalArithmetic();
        this.generateGPRotate();
        this.generateGPStack();
        this.generateGPJump();
        this.generateDataTransfer();
        this.generateMMX();
        this.generateSSE();
        this.generateMMXComparison();
    }

    private void generateMMX(){
        this.generateArithMMX();
    }

    private void generateSSE2(){
        this.generateSSE2FPArith();
        this.generateSSE2DataTransfer();
        this.generateSSE2Integer();
    }

    private void generateArithMMX(){
        this.put("PADDB", new Paddb());
        this.put("PADDW", new Paddw());
        this.put("PADDD", new Paddd());
        this.put("PSUBB", new Psubb());
        this.put("PSUBW", new Psubw());
        this.put("PSUBD", new Psubd());
        this.put("PMULLW", new Pmullw());
        this.put("PMULHW", new Pmulhw());
        this.put("PMADDWD", new Pmaddwd());
        this.put("PSUBSB", new Psubsb());
        this.put("PSUBSW", new Psubsw());
        this.put("PADDSB", new Paddsb());
        this.put("PADDSW", new Paddsw());
        this.put("PADDUSB", new Paddusb());
        this.put("PADDUSW", new Paddusw());
        this.put("PSUBUSB", new Psubusb());
        this.put("PSUBUSW", new Psubusw());
    }

    private void generateSSEDataTransfer(){
        this.put("MOVAPS", new Movaps());
        this.put("MOVUPS", new Movups());
        this.put("MOVSS", new Movss());
        this.put("MOVLPS", new Movlps());
        this.put("MOVHPS", new Movhps());
        this.put("MOVLHPS", new Movlhps());
        this.put("MOVHLPS", new Movhlps());
        this.put("MOVMSKPS", new Movmskps());
    }

    private void generateSSE2DataTransfer(){
        this.put("MOVAPD", new Movapd());
        this.put("MOVUPD", new Movupd());
        this.put("MOVSD", new Movsd());
        this.put("MOVLPD", new Movlpd());
        this.put("MOVHPD", new Movhpd());
        this.put("MOVMSKPD", new Movmskpd());
    }

    private void generateSSE2FPArith(){
        this.put("ADDPD", new Addpd());
        this.put("SUBPD", new Subpd());
        this.put("ADDSD", new Addsd());
        this.put("SUBSD", new Subsd());
        this.put("MULPD", new Mulpd());
        this.put("MULSD", new Mulsd());
        this.put("DIVPD", new Divpd());
        this.put("DIVSD", new Divsd());
        this.put("SQRTPD", new Sqrtpd());
        this.put("SQRTSD", new Sqrtsd());
        this.put("MAXPD", new Maxpd());
        this.put("MAXSD", new Maxsd());
        this.put("MINPD", new Minpd());
        this.put("MINSD", new Minsd());
    }

    private void generateSSE2Integer(){
        this.put("MOVDQA", new Movdqa());
        this.put("MOVDQU", new Movdqu());
        this.put("MOVQ2DQ", new Movq2dq());
        this.put("MOVDQ2Q", new Movdq2q());
        this.put("PADDQ", new Paddq());
        this.put("PSUBQ", new Psubq());
        this.put("PMULUDQ", new Pmuludq());
        this.put("PSHUFD", new Pshufd());
        this.put("PSHUFHW", new Pshufhw());
        this.put("PSHUFLW", new Pshuflw());
        this.put("PSLLDQ", new Pslldq());
        this.put("PSRLDQ", new Psrldq());
        this.put("PUNPCKHQDQ", new Punpckhqdq());
        this.put("PUNPCKLQDQ", new Punpcklqdq());
    }

    private void generateSSEFPArith(){
        this.put("ADDPS", new Addps());
        this.put("SUBPS", new Subps());
        this.put("ADDSS", new Addss());
        this.put("SUBSS", new Subss());
        this.put("MULPS", new Mulps());
        this.put("MULSS", new Mulss());
        this.put("DIVPS", new Divps());
        this.put("DIVSS", new Divss());
        this.put("RCPPS", new Rcpps());
//        this.put("RCPSS", new Rcpss());
        this.put("SQRTPS", new Sqrtps());
        this.put("SQRTSS", new Sqrtss());
        this.put("RSQRTPS", new Rsqrtss());
        this.put("RSQRTSS", new Rsqrtss());
        this.put("MAXPS", new Maxps());
        this.put("MAXSS", new Maxss());
        this.put("MINPS", new Maxps());
        this.put("MINSS", new Maxss());
    }

    private void generateSSE() {
        this.generateSSEDataTransfer();
        this.generateSSEFPArith();
        this.generateSSEIntArith();
    }

    private void generateSSEIntArith(){
        this.put("PAVGB", new Pavgb());
        this.put("PAVGW", new Pavgw());
        this.put("PEXTRW", new Pextrw());
        this.put("PINSRW", new Pinsrw());
        this.put("PMAXSW", new Pmaxsw());
        this.put("PMINSW", new Pminsw());
        this.put("PMAXUB", new Pmaxub());
        this.put("PMINUB", new Pminub());
        this.put("PMOVMSKB", new Pmovmskb());
        this.put("PMULHUW", new Pmulhuw());
        this.put("PSADBW", new Psadbw());
        this.put("PSHUFW", new Pshufw());
    }

    private void generateMMXComparison() {
        this.put("PCMPEQB", new Pcmpeq());
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
        this.put("DIV", new Div());
        this.put("IDIV", new Div());
    }

    private void generateGPDecimalArithmetic() {
        this.put("DAA", new Daa());
        this.put("DAS", new Das());
        this.put("AAA", new Aaa());
        this.put("AAS", new Aas());
        this.put("AAM", new Aam());
        this.put("AAD", new Aad());
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
        this.put("MINSD", new TrashMinsd());
    }
}
