# CALVIS
Development of CALVIS x86-32

CALVIS Version 1.3.5 07/23/16

General Usage Notes
---------------------------------------------------------
- The application can facilitate the customization of registers and instructions
that the user desires to include in the program and allows the user to write and
edit x86-32 assembly language code (*.calvis). Currently, CALVIS only accepts
*.calvis text file format.
- After configuration, CALVIS will take around 45 seconds to load. If you think
your machine has encountered a deadlock, please allow CALVIS to finish the operation.

Installation
---------------------------------------------------------
(JDK) Java SE Development Kit 8u91 download link:
http://www.oracle.com/technetwork/java/javase/downloads/jdk8-downloads-2133151.html

For Windows:

    1. Install your jdk folder in the Users/Name of User/Program Files/Java
    2. Proceed to the jdk folder
    3. Copy the "currency.data" located inside "C:\Program Files\Java\jdk1.8.0_91\jre\lib"
	 to "C:\Program Files\Java\jdk1.8.0_91\lib"
	 4. Run the CALVIS-1.3.5.jar with your configuration preferences.

For Mac OSX:

    1. Copy the "currency.data" located inside "/Library/Java/JavaVirtualMachines/jdk1.8.0_91.jdk/Contents/Home/jre/lib" to "/Library/Java/JavaVirtualMachines/jdk1.8.0_91.jdk/Contents/Home/lib"
    2. Run the CALVIS-1.3.5.jar with your configuration preferences.

---------------------------------------------------------

The developers can be reached at:

    Jennica Alcalde
    Email: jeca.interactive@gmail.com
    
    Goodwin Chua
	Email: goodwin_chua@dlsu.edu.ph
    
    Ivan Demabildo
	Email: ivan_demabildo@dlsu.edu.ph

    Marielle Ong
	Email: marielle_ong@dlsu.edu.ph

Please submit suggestions, report the problems and bugs that you encountered to
the developers.

Copyright 2016 CALVIS. All rights reserved.
---------------------------------------------------------
Sample Assembly Code:

    MOV EAX, 0xFFFFabcd     ; move hex value to EAX
    MOV BX, [0x0001]        ; move a hex value from the memory to   BX (follows little endian)
    SUB EAX, EBX            ; subtract EBX from EAX
    MOV word [0x00FF], AX   ; move the value of register to address 0x00FF
    STD                     ; set direction flag

    MOV EAX, 100_d          ; move 100 as decimal value to EAX
    MOV word [0x0000], 0xFF ; move 00FFh in the memory space     starting from 0000 to 0003

    JMP label1              ; jumps to label1
    MOV EAX, EBX            ; skipped
    label1: CLD             ; clears direction flag

    ;To declare a variable
    SECTION .DATA
    label2 DB 0xDD, 3, 3, 2, 1, 23
    alpha dw 12345


Memory
--------------------------------------------------------------------------------
Range: 0000 0000 - 0000 FFFF
- Accepts byte, word, dword, qword


Registers
--------------------------------------------------------------------------------
EAX (AX, AH, AL), EBX (BX, BH, BL), ECX (CX, CH, CL), EDX (DX, DH, DL),
ESI, EDI, ESP, EBP, EIP, DS, ES, FS, GS, SS


Flags
--------------------------------------------------------------------------------
Carry, Sign, Overflow, Zero, Parity, Auxiliary, Direction, Interrupt


List of Available Instructions
---------------------------------------------------------
    Legend:
    r: 32-bit Register
    d: MM0-MM7 Register
    x: XMM0-XMM7 Register
    m: Memory
    i: Immediate Value

Instruction: 

    destination    |    source
    aaa
    aad
    aad	            i,,
    aam
    aam                 i,,
    aas
    adc                 r/m,r/m/i,
    adcx                r32,r32/m32,
    add					r/m,r/m/i,
    addpd				x,x/m128,
    addps				x,x/m128,
    addsd				x,x/m64,
    addss				x,x/m32,
    adox				r32,r32/m32,
    and					r/m,r/m/i,
    andnpd			    x,x/m128,
    andnps			    x,x/m128,
    andpd				x,x/m128,
    andps				x,x/m128,
    bsf					r16/r32,r16/r32/m16/m32,
    bsr					r16/r32,r16/r32/m16/m32,
    bswap				r32,,
    bt					r16/r32/m16/m32,r16/r32/i,
    btc					r16/r32/m16/m32,r16/r32/i,
    btr					r16/r32/m16/m32,r16/r32/i,
    bts					r16/r32/m16/m32,r16/r32/i,
    cbw
    cdq
    clc
    cld
    cli
    cls
    cmc
    cmov				r16/r32,r16/r32/m16/m32,
    cmp					r/m,r/m/i,
    cmppd				x,x/m128,i
    cmpps				x,x/m128,i
    cmpsb
    cmpsd
    cmpsd				x,x/m64,i
    cmpss				x,x/m32,i
    cmpsw
    cmpxchg			r/m,r,
    cmpxchgs8		m64,,
    comisd			x,x/m32,
    comiss			x,x/m32,
    cvtdq2pd		x,d/m64,
    cvtdq2ps		x,d/m128,
    cvtpd2dq		d,x/m128,
    cvtpd2pi		d,x/m128,
    cvtpd2ps		x,x/m64,
    cvtpi2pd		x,d/m64,
    cvtpi2ps		x,d/m64,
    cvtps2dq		d,x/m128,
    cvtps2pd		x,x/m64,
    cvtps2pi		d,x/m64,
    cvtsd2si		r32,x/m64,
    cvtsd2ss		x,x/m64,
    cvtsi2sd		x,r32/m32,
    cvtsi2ss		x,r32/d/m32/m64,
    cvtss2sd		x,x/m32,
    cvtss2si		r32,x/m32,
    cvttpd2dq		d,x/m128,
    cvttpd2pi		d,x/m128,
    cvttps2dq		x,x/m128,
    cvttps2pi		d,x/m64,
    cvttsd2si		r32,x/m64,
    cvttss2si		r32,x/m32,
    cwd
    cwde
    daa
    das
    dec					r/m,,
    div					r/m,,
    divps				x,x/m128,
    divss				x,x/m32,
    idiv				r/m,,
    imul				r/m,,
    imul				r16/r32,r16/r32/m16/m32/i,
    imul				r16/r32,r16/r32/m16/m32,i
    inc					r/m,,
    j						l,,
    jcxz				l,,
    jecxz				l,,
    jmp					r16/r32/m16/m32/l,,
    lahf
    lds					r16/r32,m32/m64,
    lea					r16/r32,m16/m32,
    les					r16/r32,m32/m64,
    lfs					r16/r32,m32/m64,
    lgs					r16/r32,m32/m64,
    lodsb
    lodsd
    lodsw
    loop				l,,
    loope				l,,
    loopne			l,,
    loopnz			l,,
    loopz				l,,
    lss					r16/r32,m32/m64,
    maxps				x,x/m128,
    maxss				x,x/m32,
    minps				x,x/m128,
    minss				x,x/m32,
    mov					r/m,r/m/i,
    movapd			x/m128,x/m128,
    movaps			x/m128,x/m128,
    movd				r32/d/x/m32,r32/d/x/m32,
    movdq2q			d,x,
    movdqa			x/m128,x/m128,
    movdqu			x/m128,x/m128,
    movhlps			x,x,
    movhpd			x/m64,x/m64,
    movhps			x/m64,x/m64,
    movlhps			x,x,
    movlpd			x/m64,x/m64,
    movlps			x/m64,x/m64,
    movmskpd		r32,x,
    movmskps		r32,x,
    movq				r64/d/x/m64,r64/d/x/m64,
    movq2dq			x,d,
    movsb
    movsd
    movsd				x/m64,x/m64,
    movss				x/m32,x/m32,
    movsw
    movsx				r16/r32,r/m,
    movupd			x/m128,x/m128,
    movups			x/m128,x/m128,
    movzx				r16/r32,r/m,
    mul					r/m,,
    mulps				x,x/m128,
    mulss				x,x/m32,
    neg					r/m
    nop
    not					r/m,,
    or					r/m,r/m/i,
    orpd				x,x/m128,
    orps				x,x/m128,
    packssdw		d/x,d/x/m64/m128,
    packsswb		d/x,d/x/m64/m128,
    packuswb		d/x,d/x/m64/m128,
    paddq				x/d,x/d/m128/m64,
    pand				d/x,d/x/m64/m128,
    pandn				d/x,d/x/m64/m128,
    pavgb				x/d,x/m128/d/m64,
    pavgw				x/d,x/m128/d/m64,
    pcmpeqb			d/x,d/x/m64/m128,
    pcmpeqd			d/x,d/x/m64/m128,
    pcmpeqw			d/x,d/x/m64/m128,
    pcmpgtb			d/x,d/x/m64/m128,
    pcmpgtd			d/x,d/x/m64/m128,
    pcmpgtw			d/x,d/x/m64/m128,
    pextrw			r16/m16,x/d,i
    pmaxsw			x/m128,d/m64,
    pminsw			x/m128,d/m64,
    pminub			x/d,x/m128/d/m64,
    pmovmskb		r32,x/d,
    pmulhw			x/m128,d/m64,
    pmullw			x/m128,d/m64,
    pmuludq			x/d/m128/m64,x/d/m128/m64,
    pop					r16/r32/m16/m32/m64,,
    popa
    popad
    popf
    popfd
    por					d/x,d/x/m64/m128,
    printf
    psadbw			x/m128,d/m64,
    pshufd			x,x/m128,i
    pshufhw			x,x/m128,i
    pshuflw			x,x/m128,i
    pshufw			d,d/m64,i
    pslld				d/x,d/x/m64/m128/i,
    pslldq			x,i,
    psllq				d/x,d/x/m64/m128/i,
    psllw				d/x,d/x/m64/m128/i,
    psrad				d/x,d/x/m64/m128/i,
    psraw				d/x,d/x/m64/m128/i,
    psrldq			x,i,
    psubq,x/d		x/d/m128/m64,
    punpckhbw		d/x,d/x/m64/m128,
    punpckhdq		d/x,d/x/m64/m128,
    punpckhqdq	x,x/m128,
    punpckhwd		d/x,d/x/m64/m128,
    punpcklbw		d/x,d/x/m64/m128,
    punpckldq		d/x,d/x/m64/m128,
    punpcklqdq	x,x/m128,
    punpcklwd		d/x,d/x/m64/m128,
    push				r16/r32/m16/m32/m64/i/l,,
    pusha
    pushad
    pushf
    pushfd
    pxor				d/x,d/x/m64/m128,
    rcl					r/m,c/i,
    rcpps				x,x/m128,
    rcpss				x,x/m32,
    rcr					r/m,c/i,
    rol					r/m,c/i,
    ror					r/m,c/i,
    rsqrtps			x,x/m128,
    rsqrtss			x,x/m32,
    sahf
    sal					r/m,c/i,
    sar					r/m,c/i,
    sbb					r/m,r/m/i,
    scanf
    scasb
    scasd
    scasw
    set					r8/m8,,
    shl					r/m,c/i,
    shld				r16/r32/m16/m32,r16/r32,c/i
    shr					r/m,c/i,
    shrd				r16/r32/m16/m32,r16/r32,c/i
    shufpd			x,x/m128,i
    shufps			x,x/m128,i
    sqrtpd			x,x/m128,
    sqrtps			x,x/m128,
    sqrtss			x,x/m32,
    stc
    std
    sti
    stosb
    stosd
    stosw
    sub					r/m,r/m/i,
    subps				x,x/m128,
    subss				x,x/m32,
    test				r/m,r/i,
    ucomisd			x,x/m32,
    ucomiss			x,x/m32,
    unpckhpd		x,x/m128,
    unpckhps		x,x/m128,
    unpcklpd		x,x/m128,
    unpcklps		x,x/m128,
    xadd				r/m,r/m,
    xchg 				r/m,r/m,
    xlat
    xor 				r/m,r/m/i,
    xorpd 			x,x/m128,
    xorps 			x,x/m128,
