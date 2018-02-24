DSEG SEGMENT
DSEG ENDS
SSEG SEGMENT STACK
STK DB 8192 DUP(?)
SSEG ENDS
CSEG SEGMENT
ASSUME CS:CSEG,DS:DSEG,SS:SSEG
START:
    MOV AX,DSEG
    MOV DS,AX
    SUB SP,2
    CALL main
    ADD SP,2
    MOV AH,4CH
    INT 21H

    printlln PROC NEAR
    MOV AH,02H
    MOV DL,10
    INT 21H
    RET
    printlln ENDP
    scani PROC NEAR
    MOV BX,0
    PUSH BX
scanire1:
    POP CX
    MOV AH,01H
    INT 21H
    CMP AL,"-"
    PUSHF

    
    CMP AL,"0"
    JB scanire1
    CMP AL,"9"
    JA scanire1
    
    SUB AL,30H
    MOV AH,0
    ADD BX,AX
    
    POPF
scanirld:
    MOV AH,01H
    INT 21H
    CMP AL,"0"
    JB scaniend
    CMP AL,"9"
    JA scaniend
    PUSH AX
    MOV AX,BX
    MOV DX,10
    MUL DX
    MOV BX,AX
    POP AX
    SUB AL,30H
    MOV AH,0
    ADD BX,AX
    JMP scanirld
scaniend:
    PUSH CX
    POPF
    JNZ scaninng
    NEG BX
scaninng:
    MOV BP,SP
    MOV [BP+2],BX
    RET
    scani ENDP
    printc PROC NEAR
    MOV BP,SP
    
    MOV DL,[BP+2]
    MOV AH,02H
    INT 21H
    
    RET
    printc ENDP
    printi PROC NEAR
    
    MOV BP,SP
    MOV AX,[BP+2]
    CMP AX,0
    JGE printinn
    MOV DL,"-"
    MOV AH,02H
    INT 21H
    MOV AX,[BP+2]
    NEG AX
    CMP AX,8000H
    JNE printinn
    MOV CX,8
    PUSH CX
    MOV AX,3276
    MOV CX,10
    JMP printidf
printinn:
    MOV CX,10
    CWD
    DIV CX
    PUSH DX
printidf:
    CWD
    DIV CX
    PUSH DX
    CWD
    DIV CX
    PUSH DX
    CWD
    DIV CX
    PUSH DX
    CWD
    DIV CX
    PUSH DX
    
    MOV CX,5

printizr:
    POP DX
    CMP DL,0
    JNZ printigo
    CMP CL,1
    JZ printigo
    LOOP printizr
    
printiag:
    POP DX
printigo:
    ADD DL,30H
    MOV AH,02H
    INT 21H
    LOOP printiag
    
    MOV DL,20H
    MOV AH,02H
    INT 21H
    
    RET
    printi ENDP
    promatrx PROC NEAR
    SUB SP,114
    MOV BP,SP
    MOV AX,4
    MOV [BP+32],AX
    MOV AX,0
    MOV [BP+72],AX
    MOV AX,0
    MOV [BP+74],AX
    MOV AX,1
    MOV [BP+76],AX
L1:
    MOV AX,[BP+72]
    CMP AX,[BP+32]
    MOV AX,1
    JL comp0
    MOV AX,0
comp0:
    MOV [BP+108],AX
    MOV AX,[BP+108]
    CMP AX,0
    JNE L2
    JMP L6
L2:
    MOV AX,0
    MOV [BP+74],AX
L3:
    MOV AX,[BP+74]
    CMP AX,[BP+32]
    MOV AX,1
    JL comp1
    MOV AX,0
comp1:
    MOV [BP+110],AX
    MOV AX,[BP+110]
    CMP AX,0
    JNE L4
    JMP L5
L4:
    MOV AX,4
    CWD
    MOV BX,[BP+72]
    IMUL BX
    MOV [BP+112],AX
    MOV AX,[BP+112]
    ADD AX,[BP+74]
    MOV [BP+0],AX
    SUB SP,2
    CALL scani
    ADD SP,0
    MOV BP,SP
    MOV AX,[BP]
    ADD BP,2
    MOV [BP+4],AX
    ADD SP,2
    MOV BP,SP
    MOV AX,[BP+4]
    MOV [BP+2],AX
    MOV AX,[BP+0]
    MOV CX,2
    MUL CL
    MOV SI,AX
    MOV AX,[BP+2]
    MOV SS:[BP+SI+40],AX
    MOV AX,[BP+74]
    ADD AX,1
    MOV [BP+74],AX
    MOV AX,[BP+76]
    ADD AX,1
    MOV [BP+76],AX
    JMP L3
L5:
    MOV AX,[BP+72]
    ADD AX,1
    MOV [BP+72],AX
    JMP L1
L6:
    MOV AX,0
    MOV [BP+72],AX
L7:
    MOV AX,[BP+72]
    CMP AX,[BP+32]
    MOV AX,1
    JL comp2
    MOV AX,0
comp2:
    MOV [BP+8],AX
    MOV AX,[BP+8]
    CMP AX,0
    JNE L8
    JMP L12
L8:
    MOV AX,0
    MOV [BP+74],AX
L9:
    MOV AX,[BP+74]
    CMP AX,[BP+32]
    MOV AX,1
    JL comp3
    MOV AX,0
comp3:
    MOV [BP+16],AX
    MOV AX,[BP+16]
    CMP AX,0
    JNE L10
    JMP L11
L10:
    MOV AX,4
    CWD
    MOV BX,[BP+72]
    IMUL BX
    MOV [BP+14],AX
    MOV AX,[BP+14]
    ADD AX,[BP+74]
    MOV [BP+24],AX
    MOV AX,[BP+24]
    MOV CX,2
    MUL CL
    MOV SI,AX
    MOV AX,SS:[BP+SI+40]
    MOV [BP+20],AX
    SUB SP,2
    MOV AX,[BP+20]
    PUSH AX
    CALL printi
    ADD SP,2
    MOV BP,SP
    MOV AX,[BP]
    ADD BP,2
    MOV [BP+28],AX
    ADD SP,2
    MOV BP,SP
    MOV AX,[BP+74]
    ADD AX,1
    MOV [BP+74],AX
    JMP L9
L11:
    SUB SP,2
    CALL printlln
    ADD SP,0
    MOV BP,SP
    MOV AX,[BP]
    ADD BP,2
    MOV [BP+34],AX
    ADD SP,2
    MOV BP,SP
    MOV AX,[BP+72]
    ADD AX,1
    MOV [BP+72],AX
    JMP L7
L12:
    MOV AX,[BP+32]
    SUB AX,1
    MOV [BP+72],AX
L13:
    MOV AX,[BP+72]
    CMP AX,0
    MOV AX,1
    JG comp4
    MOV AX,0
comp4:
    MOV [BP+84],AX
    MOV AX,[BP+84]
    CMP AX,0
    JNE L14
    JMP L18
L14:
    MOV AX,0
    MOV [BP+74],AX
L15:
    MOV AX,[BP+74]
    CMP AX,[BP+72]
    MOV AX,1
    JL comp5
    MOV AX,0
comp5:
    MOV [BP+80],AX
    MOV AX,[BP+80]
    CMP AX,0
    JNE L16
    JMP L17
L16:
    MOV AX,4
    CWD
    MOV BX,[BP+72]
    IMUL BX
    MOV [BP+92],AX
    MOV AX,[BP+92]
    ADD AX,[BP+74]
    MOV [BP+88],AX
    MOV AX,[BP+88]
    MOV CX,2
    MUL CL
    MOV SI,AX
    MOV AX,SS:[BP+SI+40]
    MOV [BP+38],AX
    MOV AX,4
    CWD
    MOV BX,[BP+72]
    IMUL BX
    MOV [BP+96],AX
    MOV AX,[BP+96]
    ADD AX,[BP+74]
    MOV [BP+100],AX
    MOV AX,4
    CWD
    MOV BX,[BP+74]
    IMUL BX
    MOV [BP+106],AX
    MOV AX,[BP+106]
    ADD AX,[BP+72]
    MOV [BP+104],AX
    MOV AX,[BP+104]
    MOV CX,2
    MUL CL
    MOV SI,AX
    MOV AX,SS:[BP+SI+40]
    MOV [BP+6],AX
    MOV AX,[BP+6]
    MOV [BP+98],AX
    MOV AX,[BP+100]
    MOV CX,2
    MUL CL
    MOV SI,AX
    MOV AX,[BP+98]
    MOV SS:[BP+SI+40],AX
    MOV AX,4
    CWD
    MOV BX,[BP+74]
    IMUL BX
    MOV [BP+12],AX
    MOV AX,[BP+12]
    ADD AX,[BP+72]
    MOV [BP+10],AX
    MOV AX,[BP+38]
    MOV [BP+18],AX
    MOV AX,[BP+10]
    MOV CX,2
    MUL CL
    MOV SI,AX
    MOV AX,[BP+18]
    MOV SS:[BP+SI+40],AX
    MOV AX,[BP+74]
    ADD AX,1
    MOV [BP+74],AX
    JMP L15
L17:
    MOV AX,[BP+72]
    SUB AX,1
    MOV [BP+72],AX
    JMP L13
L18:
    SUB SP,2
    CALL printlln
    ADD SP,0
    MOV BP,SP
    MOV AX,[BP]
    ADD BP,2
    MOV [BP+22],AX
    ADD SP,2
    MOV BP,SP
    MOV AX,0
    MOV [BP+72],AX
L19:
    MOV AX,[BP+72]
    CMP AX,[BP+32]
    MOV AX,1
    JL comp6
    MOV AX,0
comp6:
    MOV [BP+30],AX
    MOV AX,[BP+30]
    CMP AX,0
    JNE L20
    JMP L24
L20:
    MOV AX,0
    MOV [BP+74],AX
L21:
    MOV AX,[BP+74]
    CMP AX,[BP+32]
    MOV AX,1
    JL comp7
    MOV AX,0
comp7:
    MOV [BP+26],AX
    MOV AX,[BP+26]
    CMP AX,0
    JNE L22
    JMP L23
L22:
    MOV AX,4
    CWD
    MOV BX,[BP+72]
    IMUL BX
    MOV [BP+36],AX
    MOV AX,[BP+36]
    ADD AX,[BP+74]
    MOV [BP+78],AX
    MOV AX,[BP+78]
    MOV CX,2
    MUL CL
    MOV SI,AX
    MOV AX,SS:[BP+SI+40]
    MOV [BP+38],AX
    MOV AX,4
    CWD
    MOV BX,[BP+72]
    IMUL BX
    MOV [BP+86],AX
    MOV AX,[BP+86]
    ADD AX,[BP+74]
    MOV [BP+82],AX
    MOV AX,[BP+82]
    MOV CX,2
    MUL CL
    MOV SI,AX
    MOV AX,SS:[BP+SI+40]
    MOV [BP+94],AX
    SUB SP,2
    MOV AX,[BP+94]
    PUSH AX
    CALL printi
    ADD SP,2
    MOV BP,SP
    MOV AX,[BP]
    ADD BP,2
    MOV [BP+90],AX
    ADD SP,2
    MOV BP,SP
    MOV AX,[BP+74]
    ADD AX,1
    MOV [BP+74],AX
    JMP L21
L23:
    MOV AX,[BP+72]
    ADD AX,1
    MOV [BP+72],AX
    SUB SP,2
    CALL printlln
    ADD SP,0
    MOV BP,SP
    MOV AX,[BP]
    ADD BP,2
    MOV [BP+102],AX
    ADD SP,2
    MOV BP,SP
    JMP L19
L24:
    MOV AX,0
    MOV [BP+116],AX
    ADD SP,114
    RET
    ADD SP,114
    RET
    promatrx ENDP
    max PROC NEAR
    SUB SP,2
    MOV BP,SP
    MOV AX,[BP+4]
    CMP AX,[BP+6]
    MOV AX,1
    JGE comp8
    MOV AX,0
comp8:
    MOV [BP+0],AX
    MOV AX,[BP+0]
    CMP AX,0
    JNE L25
    JMP L26
L25:
    MOV AX,[BP+4]
    MOV [BP+8],AX
    ADD SP,2
    RET
L26:
    MOV AX,[BP+6]
    MOV [BP+8],AX
    ADD SP,2
    RET
    ADD SP,2
    RET
    max ENDP
    min PROC NEAR
    SUB SP,2
    MOV BP,SP
    MOV AX,[BP+4]
    CMP AX,[BP+6]
    MOV AX,1
    JLE comp9
    MOV AX,0
comp9:
    MOV [BP+0],AX
    MOV AX,[BP+0]
    CMP AX,0
    JNE L27
    JMP L28
L27:
    MOV AX,[BP+4]
    MOV [BP+8],AX
    ADD SP,2
    RET
L28:
    MOV AX,[BP+6]
    MOV [BP+8],AX
    ADD SP,2
    RET
    ADD SP,2
    RET
    min ENDP
    gcd PROC NEAR
    SUB SP,14
    MOV BP,SP
    MOV AX,[BP+16]
    CMP AX,1
    MOV AX,1
    JL comp10
    MOV AX,0
comp10:
    MOV [BP+2],AX
    MOV AX,[BP+18]
    CMP AX,1
    MOV AX,1
    JL comp11
    MOV AX,0
comp11:
    MOV [BP+0],AX
    MOV AX,[BP+2]
    MOV BX,[BP+0]
    CMP AX,0
    MOV AX,1
    JNZ comp12
    MOV AX,0
comp12:
    CMP BX,0
    MOV BX,1
    JNZ comp13
    MOV BX,0
comp13:
    OR AX,BX
    MOV [BP+6],AX
    MOV AX,[BP+6]
    CMP AX,0
    JNE L29
    JMP L30
L29:
    MOV AX,0
    SUB AX,1
    MOV [BP+4],AX
    MOV AX,[BP+4]
    MOV [BP+20],AX
    ADD SP,14
    RET
L30:
    MOV AX,[BP+16]
    CMP AX,[BP+18]
    MOV AX,1
    JL comp14
    MOV AX,0
comp14:
    MOV [BP+8],AX
    MOV AX,[BP+8]
    CMP AX,0
    JNE L31
    JMP L32
L31:
    MOV AX,[BP+16]
    MOV [BP+12],AX
    MOV AX,[BP+18]
    MOV [BP+16],AX
    MOV AX,[BP+12]
    MOV [BP+18],AX
L32:
L33:
    MOV AX,1
    CMP AX,0
    JNE L34
    JMP L37
L34:
    MOV AX,[BP+16]
    CWD
    MOV BX,[BP+18]
    IDIV BX
    MOV [BP+12],AX
    MOV AX,[BP+12]
    CWD
    MOV BX,[BP+18]
    IMUL BX
    MOV [BP+12],AX
    MOV AX,[BP+16]
    SUB AX,[BP+12]
    MOV [BP+12],AX
    MOV AX,[BP+12]
    CMP AX,0
    MOV AX,1
    JE comp15
    MOV AX,0
comp15:
    MOV [BP+10],AX
    MOV AX,[BP+10]
    CMP AX,0
    JNE L35
    JMP L36
L35:
    MOV AX,[BP+18]
    MOV [BP+20],AX
    ADD SP,14
    RET
L36:
    MOV AX,[BP+18]
    MOV [BP+16],AX
    MOV AX,[BP+12]
    MOV [BP+18],AX
    JMP L33
L37:
    ADD SP,14
    RET
    gcd ENDP
    lcm PROC NEAR
    SUB SP,14
    MOV BP,SP
    MOV AX,[BP+16]
    CMP AX,1
    MOV AX,1
    JL comp16
    MOV AX,0
comp16:
    MOV [BP+12],AX
    MOV AX,[BP+18]
    CMP AX,1
    MOV AX,1
    JL comp17
    MOV AX,0
comp17:
    MOV [BP+10],AX
    MOV AX,[BP+12]
    MOV BX,[BP+10]
    CMP AX,0
    MOV AX,1
    JNZ comp18
    MOV AX,0
comp18:
    CMP BX,0
    MOV BX,1
    JNZ comp19
    MOV BX,0
comp19:
    OR AX,BX
    MOV [BP+2],AX
    MOV AX,[BP+2]
    CMP AX,0
    JNE L38
    JMP L39
L38:
    MOV AX,0
    SUB AX,1
    MOV [BP+0],AX
    MOV AX,[BP+0]
    MOV [BP+20],AX
    ADD SP,14
    RET
L39:
    SUB SP,2
    MOV AX,[BP+16]
    PUSH AX
    MOV AX,[BP+18]
    PUSH AX
    CALL gcd
    ADD SP,4
    MOV BP,SP
    MOV AX,[BP]
    ADD BP,2
    MOV [BP+6],AX
    ADD SP,2
    MOV BP,SP
    MOV AX,[BP+16]
    CWD
    MOV BX,[BP+6]
    IDIV BX
    MOV [BP+4],AX
    MOV AX,[BP+4]
    CWD
    MOV BX,[BP+18]
    IMUL BX
    MOV [BP+8],AX
    MOV AX,[BP+8]
    MOV [BP+20],AX
    ADD SP,14
    RET
    ADD SP,14
    RET
    lcm ENDP
    fbnic PROC NEAR
    SUB SP,12
    MOV BP,SP
    MOV AX,[BP+14]
    CMP AX,2
    MOV AX,1
    JG comp20
    MOV AX,0
comp20:
    MOV [BP+0],AX
    MOV AX,[BP+0]
    CMP AX,0
    JNE L40
    JMP L41
L40:
    MOV AX,[BP+14]
    SUB AX,1
    MOV [BP+4],AX
    SUB SP,2
    MOV AX,[BP+4]
    PUSH AX
    CALL fbnic
    ADD SP,2
    MOV BP,SP
    MOV AX,[BP]
    ADD BP,2
    MOV [BP+2],AX
    ADD SP,2
    MOV BP,SP
    MOV AX,[BP+14]
    SUB AX,2
    MOV [BP+6],AX
    SUB SP,2
    MOV AX,[BP+6]
    PUSH AX
    CALL fbnic
    ADD SP,2
    MOV BP,SP
    MOV AX,[BP]
    ADD BP,2
    MOV [BP+10],AX
    ADD SP,2
    MOV BP,SP
    MOV AX,[BP+2]
    ADD AX,[BP+10]
    MOV [BP+8],AX
    MOV AX,[BP+8]
    MOV [BP+16],AX
    ADD SP,12
    RET
    JMP L42
L41:
    MOV AX,1
    MOV [BP+16],AX
    ADD SP,12
    RET
L42:
    ADD SP,12
    RET
    fbnic ENDP
    sqrt PROC NEAR
    SUB SP,18
    MOV BP,SP
    MOV AX,0
    MOV [BP+14],AX
    MOV AX,[BP+20]
    CMP AX,0
    MOV AX,1
    JL comp21
    MOV AX,0
comp21:
    MOV [BP+2],AX
    MOV AX,[BP+2]
    CMP AX,0
    JNE L43
    JMP L44
L43:
    MOV AX,0
    SUB AX,1
    MOV [BP+0],AX
    MOV AX,[BP+0]
    MOV [BP+22],AX
    ADD SP,18
    RET
L44:
    MOV AX,[BP+20]
    CMP AX,0
    MOV AX,1
    JE comp22
    MOV AX,0
comp22:
    MOV [BP+6],AX
    MOV AX,[BP+6]
    CMP AX,0
    JNE L45
    JMP L46
L45:
    MOV AX,0
    MOV [BP+22],AX
    ADD SP,18
    RET
L46:
    MOV AX,[BP+20]
    CWD
    MOV BX,2
    IDIV BX
    MOV [BP+4],AX
    MOV AX,[BP+4]
    ADD AX,1
    MOV [BP+8],AX
L47:
    MOV AX,[BP+14]
    CMP AX,32
    MOV AX,1
    JL comp23
    MOV AX,0
comp23:
    MOV [BP+10],AX
    MOV AX,[BP+10]
    CMP AX,0
    JNE L48
    JMP L49
L48:
    MOV AX,[BP+20]
    CWD
    MOV BX,[BP+8]
    IDIV BX
    MOV [BP+12],AX
    MOV AX,[BP+8]
    ADD AX,[BP+12]
    MOV [BP+16],AX
    MOV AX,[BP+16]
    CWD
    MOV BX,2
    IDIV BX
    MOV [BP+8],AX
    MOV AX,[BP+14]
    ADD AX,1
    MOV [BP+14],AX
    JMP L47
L49:
    MOV AX,[BP+8]
    MOV [BP+22],AX
    ADD SP,18
    RET
    ADD SP,18
    RET
    sqrt ENDP
    hanotic PROC NEAR
    SUB SP,10
    MOV BP,SP
    MOV AX,[BP+12]
    CMP AX,1
    MOV AX,1
    JG comp24
    MOV AX,0
comp24:
    MOV [BP+8],AX
    MOV AX,[BP+8]
    CMP AX,0
    JNE L50
    JMP L51
L50:
    MOV AX,[BP+12]
    SUB AX,1
    MOV [BP+2],AX
    SUB SP,2
    MOV AX,[BP+2]
    PUSH AX
    CALL hanotic
    ADD SP,2
    MOV BP,SP
    MOV AX,[BP]
    ADD BP,2
    MOV [BP+0],AX
    ADD SP,2
    MOV BP,SP
    MOV AX,[BP+0]
    CWD
    MOV BX,2
    IMUL BX
    MOV [BP+6],AX
    MOV AX,[BP+6]
    ADD AX,1
    MOV [BP+4],AX
    MOV AX,[BP+4]
    MOV [BP+14],AX
    ADD SP,10
    RET
L51:
    MOV AX,1
    MOV [BP+14],AX
    ADD SP,10
    RET
    ADD SP,10
    RET
    hanotic ENDP
    profbar PROC NEAR
    SUB SP,88
    MOV BP,SP
    MOV AX,1
    MOV [BP+84],AX
    MOV AX,1
    MOV CX,2
    MUL CL
    MOV SI,AX
    MOV AX,[BP+84]
    MOV SS:[BP+SI+22],AX
    MOV AX,1
    MOV [BP+82],AX
    MOV AX,2
    MOV CX,2
    MUL CL
    MOV SI,AX
    MOV AX,[BP+82]
    MOV SS:[BP+SI+22],AX
    MOV AX,3
    MOV [BP+2],AX
L52:
    MOV AX,[BP+2]
    CMP AX,30
    MOV AX,1
    JL comp25
    MOV AX,0
comp25:
    MOV [BP+4],AX
    MOV AX,[BP+4]
    CMP AX,0
    JNE L53
    JMP L54
L53:
    MOV AX,[BP+2]
    SUB AX,1
    MOV [BP+8],AX
    MOV AX,[BP+8]
    MOV CX,2
    MUL CL
    MOV SI,AX
    MOV AX,SS:[BP+SI+22]
    MOV [BP+14],AX
    MOV AX,[BP+2]
    SUB AX,2
    MOV [BP+12],AX
    MOV AX,[BP+12]
    MOV CX,2
    MUL CL
    MOV SI,AX
    MOV AX,SS:[BP+SI+22]
    MOV [BP+18],AX
    MOV AX,[BP+14]
    ADD AX,[BP+18]
    MOV [BP+16],AX
    MOV AX,[BP+16]
    MOV [BP+10],AX
    MOV AX,[BP+2]
    MOV CX,2
    MUL CL
    MOV SI,AX
    MOV AX,[BP+10]
    MOV SS:[BP+SI+22],AX
    MOV AX,[BP+2]
    ADD AX,1
    MOV [BP+2],AX
    JMP L52
L54:
    MOV AX,1
    MOV [BP+2],AX
L55:
    MOV AX,[BP+2]
    CMP AX,30
    MOV AX,1
    JL comp26
    MOV AX,0
comp26:
    MOV [BP+20],AX
    MOV AX,[BP+20]
    CMP AX,0
    JNE L56
    JMP L57
L56:
    MOV AX,[BP+2]
    MOV CX,2
    MUL CL
    MOV SI,AX
    MOV AX,SS:[BP+SI+22]
    MOV [BP+86],AX
    SUB SP,2
    MOV AX,[BP+86]
    PUSH AX
    CALL printi
    ADD SP,2
    MOV BP,SP
    MOV AX,[BP]
    ADD BP,2
    MOV [BP+0],AX
    ADD SP,2
    MOV BP,SP
    SUB SP,2
    CALL printlln
    ADD SP,0
    MOV BP,SP
    MOV AX,[BP]
    ADD BP,2
    MOV [BP+6],AX
    ADD SP,2
    MOV BP,SP
    MOV AX,[BP+2]
    ADD AX,1
    MOV [BP+2],AX
    JMP L55
L57:
    MOV AX,0
    MOV [BP+90],AX
    ADD SP,88
    RET
    ADD SP,88
    RET
    profbar ENDP
    profbst PROC NEAR
    SUB SP,10
    MOV BP,SP
    MOV AX,1
    MOV [BP+8],AX
L58:
    MOV AX,[BP+8]
    CMP AX,20
    MOV AX,1
    JL comp27
    MOV AX,0
comp27:
    MOV [BP+4],AX
    MOV AX,[BP+4]
    CMP AX,0
    JNE L59
    JMP L60
L59:
    SUB SP,2
    MOV AX,[BP+8]
    PUSH AX
    CALL fbnic
    ADD SP,2
    MOV BP,SP
    MOV AX,[BP]
    ADD BP,2
    MOV [BP+2],AX
    ADD SP,2
    MOV BP,SP
    SUB SP,2
    MOV AX,[BP+2]
    PUSH AX
    CALL printi
    ADD SP,2
    MOV BP,SP
    MOV AX,[BP]
    ADD BP,2
    MOV [BP+6],AX
    ADD SP,2
    MOV BP,SP
    MOV AX,[BP+8]
    ADD AX,1
    MOV [BP+8],AX
    SUB SP,2
    CALL printlln
    ADD SP,0
    MOV BP,SP
    MOV AX,[BP]
    ADD BP,2
    MOV [BP+0],AX
    ADD SP,2
    MOV BP,SP
    JMP L58
L60:
    MOV AX,0
    MOV [BP+12],AX
    ADD SP,10
    RET
    ADD SP,10
    RET
    profbst ENDP
    promami PROC NEAR
    SUB SP,44
    MOV BP,SP
    MOV AX,0
    MOV [BP+22],AX
    MOV AX,0
    MOV [BP+28],AX
    MOV AX,0
    MOV [BP+42],AX
    MOV AX,0
    MOV [BP+22],AX
L61:
    MOV AX,[BP+22]
    CMP AX,10
    MOV AX,1
    JL comp28
    MOV AX,0
comp28:
    MOV [BP+30],AX
    MOV AX,[BP+30]
    CMP AX,0
    JNE L62
    JMP L63
L62:
    SUB SP,2
    CALL scani
    ADD SP,0
    MOV BP,SP
    MOV AX,[BP]
    ADD BP,2
    MOV [BP+24],AX
    ADD SP,2
    MOV BP,SP
    MOV AX,[BP+24]
    MOV [BP+34],AX
    MOV AX,[BP+22]
    MOV CX,2
    MUL CL
    MOV SI,AX
    MOV AX,[BP+34]
    MOV SS:[BP+SI+0],AX
    MOV AX,[BP+22]
    ADD AX,1
    MOV [BP+22],AX
    JMP L61
L63:
    MOV AX,0
    MOV CX,2
    MUL CL
    MOV SI,AX
    MOV AX,SS:[BP+SI+0]
    MOV [BP+28],AX
    MOV AX,0
    MOV CX,2
    MUL CL
    MOV SI,AX
    MOV AX,SS:[BP+SI+0]
    MOV [BP+42],AX
    MOV AX,1
    MOV [BP+22],AX
    SUB SP,2
    CALL printlln
    ADD SP,0
    MOV BP,SP
    MOV AX,[BP]
    ADD BP,2
    MOV [BP+38],AX
    ADD SP,2
    MOV BP,SP
L64:
    MOV AX,[BP+22]
    CMP AX,10
    MOV AX,1
    JL comp29
    MOV AX,0
comp29:
    MOV [BP+36],AX
    MOV AX,[BP+36]
    CMP AX,0
    JNE L65
    JMP L66
L65:
    MOV AX,[BP+22]
    MOV CX,2
    MUL CL
    MOV SI,AX
    MOV AX,SS:[BP+SI+0]
    MOV [BP+40],AX
    SUB SP,2
    MOV AX,[BP+28]
    PUSH AX
    MOV AX,[BP+40]
    PUSH AX
    CALL max
    ADD SP,4
    MOV BP,SP
    MOV AX,[BP]
    ADD BP,2
    MOV [BP+28],AX
    ADD SP,2
    MOV BP,SP
    MOV AX,[BP+22]
    MOV CX,2
    MUL CL
    MOV SI,AX
    MOV AX,SS:[BP+SI+0]
    MOV [BP+32],AX
    SUB SP,2
    MOV AX,[BP+42]
    PUSH AX
    MOV AX,[BP+32]
    PUSH AX
    CALL min
    ADD SP,4
    MOV BP,SP
    MOV AX,[BP]
    ADD BP,2
    MOV [BP+42],AX
    ADD SP,2
    MOV BP,SP
    MOV AX,[BP+22]
    ADD AX,1
    MOV [BP+22],AX
    JMP L64
L66:
    SUB SP,2
    MOV AX,[BP+28]
    PUSH AX
    CALL printi
    ADD SP,2
    MOV BP,SP
    MOV AX,[BP]
    ADD BP,2
    MOV [BP+26],AX
    ADD SP,2
    MOV BP,SP
    SUB SP,2
    MOV AX,[BP+42]
    PUSH AX
    CALL printi
    ADD SP,2
    MOV BP,SP
    MOV AX,[BP]
    ADD BP,2
    MOV [BP+20],AX
    ADD SP,2
    MOV BP,SP
    MOV AX,0
    MOV [BP+46],AX
    ADD SP,44
    RET
    ADD SP,44
    RET
    promami ENDP
    probuble PROC NEAR
    SUB SP,56
    MOV BP,SP
    MOV AX,0
    MOV [BP+24],AX
    MOV AX,0
    MOV [BP+26],AX
L67:
    MOV AX,[BP+24]
    CMP AX,10
    MOV AX,1
    JL comp30
    MOV AX,0
comp30:
    MOV [BP+32],AX
    MOV AX,[BP+32]
    CMP AX,0
    JNE L68
    JMP L69
L68:
    SUB SP,2
    CALL scani
    ADD SP,0
    MOV BP,SP
    MOV AX,[BP]
    ADD BP,2
    MOV [BP+46],AX
    ADD SP,2
    MOV BP,SP
    MOV AX,[BP+46]
    MOV [BP+28],AX
    MOV AX,[BP+24]
    MOV CX,2
    MUL CL
    MOV SI,AX
    MOV AX,[BP+28]
    MOV SS:[BP+SI+0],AX
    MOV AX,[BP+24]
    ADD AX,1
    MOV [BP+24],AX
    JMP L67
L69:
    MOV AX,1
    MOV [BP+24],AX
L70:
    MOV AX,[BP+24]
    CMP AX,10
    MOV AX,1
    JL comp31
    MOV AX,0
comp31:
    MOV [BP+52],AX
    MOV AX,[BP+52]
    CMP AX,0
    JNE L71
    JMP L77
L71:
    MOV AX,0
    MOV [BP+26],AX
L72:
    MOV AX,[BP+26]
    CMP AX,[BP+24]
    MOV AX,1
    JL comp32
    MOV AX,0
comp32:
    MOV [BP+48],AX
    MOV AX,[BP+48]
    CMP AX,0
    JNE L73
    JMP L76
L73:
    MOV AX,[BP+26]
    MOV CX,2
    MUL CL
    MOV SI,AX
    MOV AX,SS:[BP+SI+0]
    MOV [BP+44],AX
    MOV AX,[BP+24]
    MOV CX,2
    MUL CL
    MOV SI,AX
    MOV AX,SS:[BP+SI+0]
    MOV [BP+40],AX
    MOV AX,[BP+44]
    CMP AX,[BP+40]
    MOV AX,1
    JG comp33
    MOV AX,0
comp33:
    MOV [BP+38],AX
    MOV AX,[BP+38]
    CMP AX,0
    JNE L74
    JMP L75
L74:
    MOV AX,[BP+26]
    MOV CX,2
    MUL CL
    MOV SI,AX
    MOV AX,SS:[BP+SI+0]
    MOV [BP+20],AX
    MOV AX,[BP+24]
    MOV CX,2
    MUL CL
    MOV SI,AX
    MOV AX,SS:[BP+SI+0]
    MOV [BP+34],AX
    MOV AX,[BP+34]
    MOV [BP+22],AX
    MOV AX,[BP+26]
    MOV CX,2
    MUL CL
    MOV SI,AX
    MOV AX,[BP+22]
    MOV SS:[BP+SI+0],AX
    MOV AX,[BP+20]
    MOV [BP+30],AX
    MOV AX,[BP+24]
    MOV CX,2
    MUL CL
    MOV SI,AX
    MOV AX,[BP+30]
    MOV SS:[BP+SI+0],AX
L75:
    MOV AX,[BP+26]
    ADD AX,1
    MOV [BP+26],AX
    JMP L72
L76:
    MOV AX,[BP+24]
    ADD AX,1
    MOV [BP+24],AX
    JMP L70
L77:
    MOV AX,0
    MOV [BP+24],AX
    SUB SP,2
    CALL printlln
    ADD SP,0
    MOV BP,SP
    MOV AX,[BP]
    ADD BP,2
    MOV [BP+54],AX
    ADD SP,2
    MOV BP,SP
L78:
    MOV AX,[BP+24]
    CMP AX,10
    MOV AX,1
    JL comp34
    MOV AX,0
comp34:
    MOV [BP+50],AX
    MOV AX,[BP+50]
    CMP AX,0
    JNE L79
    JMP L80
L79:
    MOV AX,[BP+24]
    MOV CX,2
    MUL CL
    MOV SI,AX
    MOV AX,SS:[BP+SI+0]
    MOV [BP+36],AX
    SUB SP,2
    MOV AX,[BP+36]
    PUSH AX
    CALL printi
    ADD SP,2
    MOV BP,SP
    MOV AX,[BP]
    ADD BP,2
    MOV [BP+42],AX
    ADD SP,2
    MOV BP,SP
    MOV AX,[BP+24]
    ADD AX,1
    MOV [BP+24],AX
    JMP L78
L80:
    MOV AX,0
    MOV [BP+58],AX
    ADD SP,56
    RET
    ADD SP,56
    RET
    probuble ENDP
    progclc PROC NEAR
    SUB SP,14
    MOV BP,SP
L81:
    MOV AX,1
    CMP AX,0
    JNE L82
    JMP L83
L82:
    SUB SP,2
    CALL scani
    ADD SP,0
    MOV BP,SP
    MOV AX,[BP]
    ADD BP,2
    MOV [BP+6],AX
    ADD SP,2
    MOV BP,SP
    SUB SP,2
    CALL scani
    ADD SP,0
    MOV BP,SP
    MOV AX,[BP]
    ADD BP,2
    MOV [BP+8],AX
    ADD SP,2
    MOV BP,SP
    SUB SP,2
    MOV AX,[BP+6]
    PUSH AX
    MOV AX,[BP+8]
    PUSH AX
    CALL gcd
    ADD SP,4
    MOV BP,SP
    MOV AX,[BP]
    ADD BP,2
    MOV [BP+12],AX
    ADD SP,2
    MOV BP,SP
    SUB SP,2
    MOV AX,[BP+12]
    PUSH AX
    CALL printi
    ADD SP,2
    MOV BP,SP
    MOV AX,[BP]
    ADD BP,2
    MOV [BP+10],AX
    ADD SP,2
    MOV BP,SP
    SUB SP,2
    MOV AX,[BP+6]
    PUSH AX
    MOV AX,[BP+8]
    PUSH AX
    CALL lcm
    ADD SP,4
    MOV BP,SP
    MOV AX,[BP]
    ADD BP,2
    MOV [BP+2],AX
    ADD SP,2
    MOV BP,SP
    SUB SP,2
    MOV AX,[BP+2]
    PUSH AX
    CALL printi
    ADD SP,2
    MOV BP,SP
    MOV AX,[BP]
    ADD BP,2
    MOV [BP+0],AX
    ADD SP,2
    MOV BP,SP
    SUB SP,2
    CALL printlln
    ADD SP,0
    MOV BP,SP
    MOV AX,[BP]
    ADD BP,2
    MOV [BP+4],AX
    ADD SP,2
    MOV BP,SP
    JMP L81
L83:
    MOV AX,0
    MOV [BP+16],AX
    ADD SP,14
    RET
    ADD SP,14
    RET
    progclc ENDP
    prosqrt PROC NEAR
    SUB SP,8
    MOV BP,SP
L84:
    MOV AX,1
    CMP AX,0
    JNE L85
    JMP L86
L85:
    SUB SP,2
    CALL scani
    ADD SP,0
    MOV BP,SP
    MOV AX,[BP]
    ADD BP,2
    MOV [BP+0],AX
    ADD SP,2
    MOV BP,SP
    SUB SP,2
    MOV AX,[BP+0]
    PUSH AX
    CALL sqrt
    ADD SP,2
    MOV BP,SP
    MOV AX,[BP]
    ADD BP,2
    MOV [BP+2],AX
    ADD SP,2
    MOV BP,SP
    SUB SP,2
    MOV AX,[BP+2]
    PUSH AX
    CALL printi
    ADD SP,2
    MOV BP,SP
    MOV AX,[BP]
    ADD BP,2
    MOV [BP+6],AX
    ADD SP,2
    MOV BP,SP
    SUB SP,2
    CALL printlln
    ADD SP,0
    MOV BP,SP
    MOV AX,[BP]
    ADD BP,2
    MOV [BP+4],AX
    ADD SP,2
    MOV BP,SP
    JMP L84
L86:
    MOV AX,0
    MOV [BP+10],AX
    ADD SP,8
    RET
    ADD SP,8
    RET
    prosqrt ENDP
    main PROC NEAR
    SUB SP,0
    MOV BP,SP
    MOV AX,0
    MOV [BP+2],AX
    ADD SP,0
    RET
    ADD SP,0
    RET
    main ENDP
CSEG ENDS
    END START
