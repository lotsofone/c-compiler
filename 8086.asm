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
;(func, promatrx, int, _)    Qfunc
    promatrx PROC NEAR
    SUB SP,114
    MOV BP,SP
;(array, una, 16, _)    Qarray
;(declare, _, int, _)    Qdeclare
;(arrayend, _, _, _)    Qarrayend
;(declare, msize, int, _)    Qdeclare
;(=, 4, _, msize)    ASSIGNOP
    MOV AX,4
    MOV [BP+32],AX
;(declare, i, int, _)    Qdeclare
;(=, 0, _, i)    ASSIGNOP
    MOV AX,0
    MOV [BP+72],AX
;(declare, j, int, _)    Qdeclare
;(=, 0, _, j)    ASSIGNOP
    MOV AX,0
    MOV [BP+74],AX
;(declare, k, int, _)    Qdeclare
;(=, 1, _, k)    ASSIGNOP
    MOV AX,1
    MOV [BP+76],AX
;(declare, temp, int, _)    Qdeclare
;(label, L1, _, _)    Qlabel
L1:
;(tempDecl, t1, _, _)    Qtemp_declare
;(<, i, msize, t1)    RELOP
    MOV AX,[BP+72]
    CMP AX,[BP+32]
    MOV AX,1
    JL comp0
    MOV AX,0
comp0:
    MOV [BP+108],AX
;(if, t1, L2, L6)    Qif
    MOV AX,[BP+108]
    CMP AX,0
    JNE L2
    JMP L6
;(label, L2, _, _)    Qlabel
L2:
;(=, 0, _, j)    ASSIGNOP
    MOV AX,0
    MOV [BP+74],AX
;(label, L3, _, _)    Qlabel
L3:
;(tempDecl, t2, _, _)    Qtemp_declare
;(<, j, msize, t2)    RELOP
    MOV AX,[BP+74]
    CMP AX,[BP+32]
    MOV AX,1
    JL comp1
    MOV AX,0
comp1:
    MOV [BP+110],AX
;(if, t2, L4, L5)    Qif
    MOV AX,[BP+110]
    CMP AX,0
    JNE L4
    JMP L5
;(label, L4, _, _)    Qlabel
L4:
;(tempDecl, t3, _, _)    Qtemp_declare
;(*, 4, i, t3)    STAR
    MOV AX,4
    CWD
    MOV BX,[BP+72]
    IMUL BX
    MOV [BP+112],AX
;(tempDecl, t4, _, _)    Qtemp_declare
;(+, t3, j, t4)    PLUS
    MOV AX,[BP+112]
    ADD AX,[BP+74]
    MOV [BP+0],AX
;(tempDecl, t6, _, _)    Qtemp_declare
;(call, scani, _, t6)    Qcall
    SUB SP,2
    CALL scani
    ADD SP,0
    MOV BP,SP
    MOV AX,[BP]
    ADD BP,2
    MOV [BP+4],AX
    ADD SP,2
    MOV BP,SP
;(tempDecl, t5, _, _)    Qtemp_declare
;(=, t6, _, t5)    ASSIGNOP
    MOV AX,[BP+4]
    MOV [BP+2],AX
;(arrayin, t5, t4, una)    Qarrayin
    MOV AX,[BP+0]
    MOV CX,2
    MUL CL
    MOV SI,AX
    MOV AX,[BP+2]
    MOV SS:[BP+SI+40],AX
;(+, j, 1, j)    PLUS
    MOV AX,[BP+74]
    ADD AX,1
    MOV [BP+74],AX
;(+, k, 1, k)    PLUS
    MOV AX,[BP+76]
    ADD AX,1
    MOV [BP+76],AX
;(goto, L3, _, _)    Qgoto
    JMP L3
;(label, L5, _, _)    Qlabel
L5:
;(+, i, 1, i)    PLUS
    MOV AX,[BP+72]
    ADD AX,1
    MOV [BP+72],AX
;(goto, L1, _, _)    Qgoto
    JMP L1
;(label, L6, _, _)    Qlabel
L6:
;(=, 0, _, i)    ASSIGNOP
    MOV AX,0
    MOV [BP+72],AX
;(label, L7, _, _)    Qlabel
L7:
;(tempDecl, t10, _, _)    Qtemp_declare
;(<, i, msize, t10)    RELOP
    MOV AX,[BP+72]
    CMP AX,[BP+32]
    MOV AX,1
    JL comp2
    MOV AX,0
comp2:
    MOV [BP+8],AX
;(if, t10, L8, L12)    Qif
    MOV AX,[BP+8]
    CMP AX,0
    JNE L8
    JMP L12
;(label, L8, _, _)    Qlabel
L8:
;(=, 0, _, j)    ASSIGNOP
    MOV AX,0
    MOV [BP+74],AX
;(label, L9, _, _)    Qlabel
L9:
;(tempDecl, t11, _, _)    Qtemp_declare
;(<, j, msize, t11)    RELOP
    MOV AX,[BP+74]
    CMP AX,[BP+32]
    MOV AX,1
    JL comp3
    MOV AX,0
comp3:
    MOV [BP+16],AX
;(if, t11, L10, L11)    Qif
    MOV AX,[BP+16]
    CMP AX,0
    JNE L10
    JMP L11
;(label, L10, _, _)    Qlabel
L10:
;(tempDecl, t12, _, _)    Qtemp_declare
;(*, 4, i, t12)    STAR
    MOV AX,4
    CWD
    MOV BX,[BP+72]
    IMUL BX
    MOV [BP+14],AX
;(tempDecl, t13, _, _)    Qtemp_declare
;(+, t12, j, t13)    PLUS
    MOV AX,[BP+14]
    ADD AX,[BP+74]
    MOV [BP+24],AX
;(tempDecl, t14, _, _)    Qtemp_declare
;(arrayout, una, t13, t14)    Qarrayout
    MOV AX,[BP+24]
    MOV CX,2
    MUL CL
    MOV SI,AX
    MOV AX,SS:[BP+SI+40]
    MOV [BP+20],AX
;(putarg, t14, _, _)    Qputarg
;(tempDecl, t15, _, _)    Qtemp_declare
;(call, printi, _, t15)    Qcall
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
;(+, j, 1, j)    PLUS
    MOV AX,[BP+74]
    ADD AX,1
    MOV [BP+74],AX
;(goto, L9, _, _)    Qgoto
    JMP L9
;(label, L11, _, _)    Qlabel
L11:
;(tempDecl, t17, _, _)    Qtemp_declare
;(call, printlln, _, t17)    Qcall
    SUB SP,2
    CALL printlln
    ADD SP,0
    MOV BP,SP
    MOV AX,[BP]
    ADD BP,2
    MOV [BP+34],AX
    ADD SP,2
    MOV BP,SP
;(+, i, 1, i)    PLUS
    MOV AX,[BP+72]
    ADD AX,1
    MOV [BP+72],AX
;(goto, L7, _, _)    Qgoto
    JMP L7
;(label, L12, _, _)    Qlabel
L12:
;(-, msize, 1, i)    MINUS
    MOV AX,[BP+32]
    SUB AX,1
    MOV [BP+72],AX
;(label, L13, _, _)    Qlabel
L13:
;(tempDecl, t20, _, _)    Qtemp_declare
;(>, i, 0, t20)    RELOP
    MOV AX,[BP+72]
    CMP AX,0
    MOV AX,1
    JG comp4
    MOV AX,0
comp4:
    MOV [BP+84],AX
;(if, t20, L14, L18)    Qif
    MOV AX,[BP+84]
    CMP AX,0
    JNE L14
    JMP L18
;(label, L14, _, _)    Qlabel
L14:
;(=, 0, _, j)    ASSIGNOP
    MOV AX,0
    MOV [BP+74],AX
;(label, L15, _, _)    Qlabel
L15:
;(tempDecl, t21, _, _)    Qtemp_declare
;(<, j, i, t21)    RELOP
    MOV AX,[BP+74]
    CMP AX,[BP+72]
    MOV AX,1
    JL comp5
    MOV AX,0
comp5:
    MOV [BP+80],AX
;(if, t21, L16, L17)    Qif
    MOV AX,[BP+80]
    CMP AX,0
    JNE L16
    JMP L17
;(label, L16, _, _)    Qlabel
L16:
;(tempDecl, t22, _, _)    Qtemp_declare
;(*, 4, i, t22)    STAR
    MOV AX,4
    CWD
    MOV BX,[BP+72]
    IMUL BX
    MOV [BP+92],AX
;(tempDecl, t23, _, _)    Qtemp_declare
;(+, t22, j, t23)    PLUS
    MOV AX,[BP+92]
    ADD AX,[BP+74]
    MOV [BP+88],AX
;(arrayout, una, t23, temp)    Qarrayout
    MOV AX,[BP+88]
    MOV CX,2
    MUL CL
    MOV SI,AX
    MOV AX,SS:[BP+SI+40]
    MOV [BP+38],AX
;(tempDecl, t25, _, _)    Qtemp_declare
;(*, 4, i, t25)    STAR
    MOV AX,4
    CWD
    MOV BX,[BP+72]
    IMUL BX
    MOV [BP+96],AX
;(tempDecl, t26, _, _)    Qtemp_declare
;(+, t25, j, t26)    PLUS
    MOV AX,[BP+96]
    ADD AX,[BP+74]
    MOV [BP+100],AX
;(tempDecl, t28, _, _)    Qtemp_declare
;(*, 4, j, t28)    STAR
    MOV AX,4
    CWD
    MOV BX,[BP+74]
    IMUL BX
    MOV [BP+106],AX
;(tempDecl, t29, _, _)    Qtemp_declare
;(+, t28, i, t29)    PLUS
    MOV AX,[BP+106]
    ADD AX,[BP+72]
    MOV [BP+104],AX
;(tempDecl, t30, _, _)    Qtemp_declare
;(arrayout, una, t29, t30)    Qarrayout
    MOV AX,[BP+104]
    MOV CX,2
    MUL CL
    MOV SI,AX
    MOV AX,SS:[BP+SI+40]
    MOV [BP+6],AX
;(tempDecl, t27, _, _)    Qtemp_declare
;(=, t30, _, t27)    ASSIGNOP
    MOV AX,[BP+6]
    MOV [BP+98],AX
;(arrayin, t27, t26, una)    Qarrayin
    MOV AX,[BP+100]
    MOV CX,2
    MUL CL
    MOV SI,AX
    MOV AX,[BP+98]
    MOV SS:[BP+SI+40],AX
;(tempDecl, t31, _, _)    Qtemp_declare
;(*, 4, j, t31)    STAR
    MOV AX,4
    CWD
    MOV BX,[BP+74]
    IMUL BX
    MOV [BP+12],AX
;(tempDecl, t32, _, _)    Qtemp_declare
;(+, t31, i, t32)    PLUS
    MOV AX,[BP+12]
    ADD AX,[BP+72]
    MOV [BP+10],AX
;(tempDecl, t33, _, _)    Qtemp_declare
;(=, temp, _, t33)    ASSIGNOP
    MOV AX,[BP+38]
    MOV [BP+18],AX
;(arrayin, t33, t32, una)    Qarrayin
    MOV AX,[BP+10]
    MOV CX,2
    MUL CL
    MOV SI,AX
    MOV AX,[BP+18]
    MOV SS:[BP+SI+40],AX
;(+, j, 1, j)    PLUS
    MOV AX,[BP+74]
    ADD AX,1
    MOV [BP+74],AX
;(goto, L15, _, _)    Qgoto
    JMP L15
;(label, L17, _, _)    Qlabel
L17:
;(-, i, 1, i)    MINUS
    MOV AX,[BP+72]
    SUB AX,1
    MOV [BP+72],AX
;(goto, L13, _, _)    Qgoto
    JMP L13
;(label, L18, _, _)    Qlabel
L18:
;(tempDecl, t36, _, _)    Qtemp_declare
;(call, printlln, _, t36)    Qcall
    SUB SP,2
    CALL printlln
    ADD SP,0
    MOV BP,SP
    MOV AX,[BP]
    ADD BP,2
    MOV [BP+22],AX
    ADD SP,2
    MOV BP,SP
;(=, 0, _, i)    ASSIGNOP
    MOV AX,0
    MOV [BP+72],AX
;(label, L19, _, _)    Qlabel
L19:
;(tempDecl, t37, _, _)    Qtemp_declare
;(<, i, msize, t37)    RELOP
    MOV AX,[BP+72]
    CMP AX,[BP+32]
    MOV AX,1
    JL comp6
    MOV AX,0
comp6:
    MOV [BP+30],AX
;(if, t37, L20, L24)    Qif
    MOV AX,[BP+30]
    CMP AX,0
    JNE L20
    JMP L24
;(label, L20, _, _)    Qlabel
L20:
;(=, 0, _, j)    ASSIGNOP
    MOV AX,0
    MOV [BP+74],AX
;(label, L21, _, _)    Qlabel
L21:
;(tempDecl, t38, _, _)    Qtemp_declare
;(<, j, msize, t38)    RELOP
    MOV AX,[BP+74]
    CMP AX,[BP+32]
    MOV AX,1
    JL comp7
    MOV AX,0
comp7:
    MOV [BP+26],AX
;(if, t38, L22, L23)    Qif
    MOV AX,[BP+26]
    CMP AX,0
    JNE L22
    JMP L23
;(label, L22, _, _)    Qlabel
L22:
;(tempDecl, t39, _, _)    Qtemp_declare
;(*, 4, i, t39)    STAR
    MOV AX,4
    CWD
    MOV BX,[BP+72]
    IMUL BX
    MOV [BP+36],AX
;(tempDecl, t40, _, _)    Qtemp_declare
;(+, t39, j, t40)    PLUS
    MOV AX,[BP+36]
    ADD AX,[BP+74]
    MOV [BP+78],AX
;(arrayout, una, t40, temp)    Qarrayout
    MOV AX,[BP+78]
    MOV CX,2
    MUL CL
    MOV SI,AX
    MOV AX,SS:[BP+SI+40]
    MOV [BP+38],AX
;(tempDecl, t42, _, _)    Qtemp_declare
;(*, 4, i, t42)    STAR
    MOV AX,4
    CWD
    MOV BX,[BP+72]
    IMUL BX
    MOV [BP+86],AX
;(tempDecl, t43, _, _)    Qtemp_declare
;(+, t42, j, t43)    PLUS
    MOV AX,[BP+86]
    ADD AX,[BP+74]
    MOV [BP+82],AX
;(tempDecl, t44, _, _)    Qtemp_declare
;(arrayout, una, t43, t44)    Qarrayout
    MOV AX,[BP+82]
    MOV CX,2
    MUL CL
    MOV SI,AX
    MOV AX,SS:[BP+SI+40]
    MOV [BP+94],AX
;(putarg, t44, _, _)    Qputarg
;(tempDecl, t45, _, _)    Qtemp_declare
;(call, printi, _, t45)    Qcall
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
;(+, j, 1, j)    PLUS
    MOV AX,[BP+74]
    ADD AX,1
    MOV [BP+74],AX
;(goto, L21, _, _)    Qgoto
    JMP L21
;(label, L23, _, _)    Qlabel
L23:
;(+, i, 1, i)    PLUS
    MOV AX,[BP+72]
    ADD AX,1
    MOV [BP+72],AX
;(tempDecl, t48, _, _)    Qtemp_declare
;(call, printlln, _, t48)    Qcall
    SUB SP,2
    CALL printlln
    ADD SP,0
    MOV BP,SP
    MOV AX,[BP]
    ADD BP,2
    MOV [BP+102],AX
    ADD SP,2
    MOV BP,SP
;(goto, L19, _, _)    Qgoto
    JMP L19
;(label, L24, _, _)    Qlabel
L24:
;(return, 0, _, _)    Qreturn
    MOV AX,0
    MOV [BP+116],AX
    ADD SP,114
    RET
;(endfunc, promatrx, _, _)    Qendfunc
    ADD SP,114
    RET
    promatrx ENDP
;(func, max, int, _)    Qfunc
    max PROC NEAR
    SUB SP,2
    MOV BP,SP
;(takearg, x, int, _)    Qtakearg
;(takearg, y, int, _)    Qtakearg
;(tempDecl, t49, _, _)    Qtemp_declare
;(>=, x, y, t49)    RELOP
    MOV AX,[BP+4]
    CMP AX,[BP+6]
    MOV AX,1
    JGE comp8
    MOV AX,0
comp8:
    MOV [BP+0],AX
;(if, t49, L25, L26)    Qif
    MOV AX,[BP+0]
    CMP AX,0
    JNE L25
    JMP L26
;(label, L25, _, _)    Qlabel
L25:
;(return, x, _, _)    Qreturn
    MOV AX,[BP+4]
    MOV [BP+8],AX
    ADD SP,2
    RET
;(label, L26, _, _)    Qlabel
L26:
;(return, y, _, _)    Qreturn
    MOV AX,[BP+6]
    MOV [BP+8],AX
    ADD SP,2
    RET
;(endfunc, max, _, _)    Qendfunc
    ADD SP,2
    RET
    max ENDP
;(func, min, int, _)    Qfunc
    min PROC NEAR
    SUB SP,2
    MOV BP,SP
;(takearg, x, int, _)    Qtakearg
;(takearg, y, int, _)    Qtakearg
;(tempDecl, t50, _, _)    Qtemp_declare
;(<=, x, y, t50)    RELOP
    MOV AX,[BP+4]
    CMP AX,[BP+6]
    MOV AX,1
    JLE comp9
    MOV AX,0
comp9:
    MOV [BP+0],AX
;(if, t50, L27, L28)    Qif
    MOV AX,[BP+0]
    CMP AX,0
    JNE L27
    JMP L28
;(label, L27, _, _)    Qlabel
L27:
;(return, x, _, _)    Qreturn
    MOV AX,[BP+4]
    MOV [BP+8],AX
    ADD SP,2
    RET
;(label, L28, _, _)    Qlabel
L28:
;(return, y, _, _)    Qreturn
    MOV AX,[BP+6]
    MOV [BP+8],AX
    ADD SP,2
    RET
;(endfunc, min, _, _)    Qendfunc
    ADD SP,2
    RET
    min ENDP
;(func, gcd, int, _)    Qfunc
    gcd PROC NEAR
    SUB SP,14
    MOV BP,SP
;(takearg, x, int, _)    Qtakearg
;(takearg, y, int, _)    Qtakearg
;(declare, ca, int, _)    Qdeclare
;(tempDecl, t51, _, _)    Qtemp_declare
;(<, x, 1, t51)    RELOP
    MOV AX,[BP+16]
    CMP AX,1
    MOV AX,1
    JL comp10
    MOV AX,0
comp10:
    MOV [BP+2],AX
;(tempDecl, t52, _, _)    Qtemp_declare
;(<, y, 1, t52)    RELOP
    MOV AX,[BP+18]
    CMP AX,1
    MOV AX,1
    JL comp11
    MOV AX,0
comp11:
    MOV [BP+0],AX
;(tempDecl, t53, _, _)    Qtemp_declare
;(||, t51, t52, t53)    OR
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
;(if, t53, L29, L30)    Qif
    MOV AX,[BP+6]
    CMP AX,0
    JNE L29
    JMP L30
;(label, L29, _, _)    Qlabel
L29:
;(tempDecl, t54, _, _)    Qtemp_declare
;(-, 0, 1, t54)    MINUS
    MOV AX,0
    SUB AX,1
    MOV [BP+4],AX
;(return, t54, _, _)    Qreturn
    MOV AX,[BP+4]
    MOV [BP+20],AX
    ADD SP,14
    RET
;(label, L30, _, _)    Qlabel
L30:
;(tempDecl, t55, _, _)    Qtemp_declare
;(<, x, y, t55)    RELOP
    MOV AX,[BP+16]
    CMP AX,[BP+18]
    MOV AX,1
    JL comp14
    MOV AX,0
comp14:
    MOV [BP+8],AX
;(if, t55, L31, L32)    Qif
    MOV AX,[BP+8]
    CMP AX,0
    JNE L31
    JMP L32
;(label, L31, _, _)    Qlabel
L31:
;(=, x, _, ca)    ASSIGNOP
    MOV AX,[BP+16]
    MOV [BP+12],AX
;(=, y, _, x)    ASSIGNOP
    MOV AX,[BP+18]
    MOV [BP+16],AX
;(=, ca, _, y)    ASSIGNOP
    MOV AX,[BP+12]
    MOV [BP+18],AX
;(label, L32, _, _)    Qlabel
L32:
;(label, L33, _, _)    Qlabel
L33:
;(if, 1, L34, L37)    Qif
    MOV AX,1
    CMP AX,0
    JNE L34
    JMP L37
;(label, L34, _, _)    Qlabel
L34:
;(/, x, y, ca)    DIV
    MOV AX,[BP+16]
    CWD
    MOV BX,[BP+18]
    IDIV BX
    MOV [BP+12],AX
;(*, ca, y, ca)    STAR
    MOV AX,[BP+12]
    CWD
    MOV BX,[BP+18]
    IMUL BX
    MOV [BP+12],AX
;(-, x, ca, ca)    MINUS
    MOV AX,[BP+16]
    SUB AX,[BP+12]
    MOV [BP+12],AX
;(tempDecl, t59, _, _)    Qtemp_declare
;(==, ca, 0, t59)    RELOP
    MOV AX,[BP+12]
    CMP AX,0
    MOV AX,1
    JE comp15
    MOV AX,0
comp15:
    MOV [BP+10],AX
;(if, t59, L35, L36)    Qif
    MOV AX,[BP+10]
    CMP AX,0
    JNE L35
    JMP L36
;(label, L35, _, _)    Qlabel
L35:
;(return, y, _, _)    Qreturn
    MOV AX,[BP+18]
    MOV [BP+20],AX
    ADD SP,14
    RET
;(label, L36, _, _)    Qlabel
L36:
;(=, y, _, x)    ASSIGNOP
    MOV AX,[BP+18]
    MOV [BP+16],AX
;(=, ca, _, y)    ASSIGNOP
    MOV AX,[BP+12]
    MOV [BP+18],AX
;(goto, L33, _, _)    Qgoto
    JMP L33
;(label, L37, _, _)    Qlabel
L37:
;(endfunc, gcd, _, _)    Qendfunc
    ADD SP,14
    RET
    gcd ENDP
;(func, lcm, int, _)    Qfunc
    lcm PROC NEAR
    SUB SP,14
    MOV BP,SP
;(takearg, x, int, _)    Qtakearg
;(takearg, y, int, _)    Qtakearg
;(tempDecl, t60, _, _)    Qtemp_declare
;(<, x, 1, t60)    RELOP
    MOV AX,[BP+16]
    CMP AX,1
    MOV AX,1
    JL comp16
    MOV AX,0
comp16:
    MOV [BP+12],AX
;(tempDecl, t61, _, _)    Qtemp_declare
;(<, y, 1, t61)    RELOP
    MOV AX,[BP+18]
    CMP AX,1
    MOV AX,1
    JL comp17
    MOV AX,0
comp17:
    MOV [BP+10],AX
;(tempDecl, t62, _, _)    Qtemp_declare
;(||, t60, t61, t62)    OR
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
;(if, t62, L38, L39)    Qif
    MOV AX,[BP+2]
    CMP AX,0
    JNE L38
    JMP L39
;(label, L38, _, _)    Qlabel
L38:
;(tempDecl, t63, _, _)    Qtemp_declare
;(-, 0, 1, t63)    MINUS
    MOV AX,0
    SUB AX,1
    MOV [BP+0],AX
;(return, t63, _, _)    Qreturn
    MOV AX,[BP+0]
    MOV [BP+20],AX
    ADD SP,14
    RET
;(label, L39, _, _)    Qlabel
L39:
;(putarg, x, _, _)    Qputarg
;(putarg, y, _, _)    Qputarg
;(tempDecl, t64, _, _)    Qtemp_declare
;(call, gcd, _, t64)    Qcall
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
;(tempDecl, t65, _, _)    Qtemp_declare
;(/, x, t64, t65)    DIV
    MOV AX,[BP+16]
    CWD
    MOV BX,[BP+6]
    IDIV BX
    MOV [BP+4],AX
;(tempDecl, t66, _, _)    Qtemp_declare
;(*, t65, y, t66)    STAR
    MOV AX,[BP+4]
    CWD
    MOV BX,[BP+18]
    IMUL BX
    MOV [BP+8],AX
;(return, t66, _, _)    Qreturn
    MOV AX,[BP+8]
    MOV [BP+20],AX
    ADD SP,14
    RET
;(endfunc, lcm, _, _)    Qendfunc
    ADD SP,14
    RET
    lcm ENDP
;(func, fbnic, int, _)    Qfunc
    fbnic PROC NEAR
    SUB SP,12
    MOV BP,SP
;(takearg, n, int, _)    Qtakearg
;(tempDecl, t67, _, _)    Qtemp_declare
;(>, n, 2, t67)    RELOP
    MOV AX,[BP+14]
    CMP AX,2
    MOV AX,1
    JG comp20
    MOV AX,0
comp20:
    MOV [BP+0],AX
;(if, t67, L40, L41)    Qif
    MOV AX,[BP+0]
    CMP AX,0
    JNE L40
    JMP L41
;(label, L40, _, _)    Qlabel
L40:
;(tempDecl, t68, _, _)    Qtemp_declare
;(-, n, 1, t68)    MINUS
    MOV AX,[BP+14]
    SUB AX,1
    MOV [BP+4],AX
;(putarg, t68, _, _)    Qputarg
;(tempDecl, t69, _, _)    Qtemp_declare
;(call, fbnic, _, t69)    Qcall
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
;(tempDecl, t70, _, _)    Qtemp_declare
;(-, n, 2, t70)    MINUS
    MOV AX,[BP+14]
    SUB AX,2
    MOV [BP+6],AX
;(putarg, t70, _, _)    Qputarg
;(tempDecl, t71, _, _)    Qtemp_declare
;(call, fbnic, _, t71)    Qcall
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
;(tempDecl, t72, _, _)    Qtemp_declare
;(+, t69, t71, t72)    PLUS
    MOV AX,[BP+2]
    ADD AX,[BP+10]
    MOV [BP+8],AX
;(return, t72, _, _)    Qreturn
    MOV AX,[BP+8]
    MOV [BP+16],AX
    ADD SP,12
    RET
;(goto, L42, _, _)    Qgoto
    JMP L42
;(label, L41, _, _)    Qlabel
L41:
;(return, 1, _, _)    Qreturn
    MOV AX,1
    MOV [BP+16],AX
    ADD SP,12
    RET
;(label, L42, _, _)    Qlabel
L42:
;(endfunc, fbnic, _, _)    Qendfunc
    ADD SP,12
    RET
    fbnic ENDP
;(func, sqrt, int, _)    Qfunc
    sqrt PROC NEAR
    SUB SP,18
    MOV BP,SP
;(takearg, k, int, _)    Qtakearg
;(declare, kret, int, _)    Qdeclare
;(declare, ktt, int, _)    Qdeclare
;(=, 0, _, ktt)    ASSIGNOP
    MOV AX,0
    MOV [BP+14],AX
;(tempDecl, t73, _, _)    Qtemp_declare
;(<, k, 0, t73)    RELOP
    MOV AX,[BP+20]
    CMP AX,0
    MOV AX,1
    JL comp21
    MOV AX,0
comp21:
    MOV [BP+2],AX
;(if, t73, L43, L44)    Qif
    MOV AX,[BP+2]
    CMP AX,0
    JNE L43
    JMP L44
;(label, L43, _, _)    Qlabel
L43:
;(tempDecl, t74, _, _)    Qtemp_declare
;(-, 0, 1, t74)    MINUS
    MOV AX,0
    SUB AX,1
    MOV [BP+0],AX
;(return, t74, _, _)    Qreturn
    MOV AX,[BP+0]
    MOV [BP+22],AX
    ADD SP,18
    RET
;(label, L44, _, _)    Qlabel
L44:
;(tempDecl, t75, _, _)    Qtemp_declare
;(==, k, 0, t75)    RELOP
    MOV AX,[BP+20]
    CMP AX,0
    MOV AX,1
    JE comp22
    MOV AX,0
comp22:
    MOV [BP+6],AX
;(if, t75, L45, L46)    Qif
    MOV AX,[BP+6]
    CMP AX,0
    JNE L45
    JMP L46
;(label, L45, _, _)    Qlabel
L45:
;(return, 0, _, _)    Qreturn
    MOV AX,0
    MOV [BP+22],AX
    ADD SP,18
    RET
;(label, L46, _, _)    Qlabel
L46:
;(tempDecl, t76, _, _)    Qtemp_declare
;(/, k, 2, t76)    DIV
    MOV AX,[BP+20]
    CWD
    MOV BX,2
    IDIV BX
    MOV [BP+4],AX
;(+, t76, 1, kret)    PLUS
    MOV AX,[BP+4]
    ADD AX,1
    MOV [BP+8],AX
;(label, L47, _, _)    Qlabel
L47:
;(tempDecl, t78, _, _)    Qtemp_declare
;(<, ktt, 32, t78)    RELOP
    MOV AX,[BP+14]
    CMP AX,32
    MOV AX,1
    JL comp23
    MOV AX,0
comp23:
    MOV [BP+10],AX
;(if, t78, L48, L49)    Qif
    MOV AX,[BP+10]
    CMP AX,0
    JNE L48
    JMP L49
;(label, L48, _, _)    Qlabel
L48:
;(tempDecl, t79, _, _)    Qtemp_declare
;(/, k, kret, t79)    DIV
    MOV AX,[BP+20]
    CWD
    MOV BX,[BP+8]
    IDIV BX
    MOV [BP+12],AX
;(tempDecl, t80, _, _)    Qtemp_declare
;(+, kret, t79, t80)    PLUS
    MOV AX,[BP+8]
    ADD AX,[BP+12]
    MOV [BP+16],AX
;(/, t80, 2, kret)    DIV
    MOV AX,[BP+16]
    CWD
    MOV BX,2
    IDIV BX
    MOV [BP+8],AX
;(+, ktt, 1, ktt)    PLUS
    MOV AX,[BP+14]
    ADD AX,1
    MOV [BP+14],AX
;(goto, L47, _, _)    Qgoto
    JMP L47
;(label, L49, _, _)    Qlabel
L49:
;(return, kret, _, _)    Qreturn
    MOV AX,[BP+8]
    MOV [BP+22],AX
    ADD SP,18
    RET
;(endfunc, sqrt, _, _)    Qendfunc
    ADD SP,18
    RET
    sqrt ENDP
;(func, hanotic, int, _)    Qfunc
    hanotic PROC NEAR
    SUB SP,10
    MOV BP,SP
;(takearg, n, int, _)    Qtakearg
;(tempDecl, t83, _, _)    Qtemp_declare
;(>, n, 1, t83)    RELOP
    MOV AX,[BP+12]
    CMP AX,1
    MOV AX,1
    JG comp24
    MOV AX,0
comp24:
    MOV [BP+8],AX
;(if, t83, L50, L51)    Qif
    MOV AX,[BP+8]
    CMP AX,0
    JNE L50
    JMP L51
;(label, L50, _, _)    Qlabel
L50:
;(tempDecl, t84, _, _)    Qtemp_declare
;(-, n, 1, t84)    MINUS
    MOV AX,[BP+12]
    SUB AX,1
    MOV [BP+2],AX
;(putarg, t84, _, _)    Qputarg
;(tempDecl, t85, _, _)    Qtemp_declare
;(call, hanotic, _, t85)    Qcall
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
;(tempDecl, t86, _, _)    Qtemp_declare
;(*, t85, 2, t86)    STAR
    MOV AX,[BP+0]
    CWD
    MOV BX,2
    IMUL BX
    MOV [BP+6],AX
;(tempDecl, t87, _, _)    Qtemp_declare
;(+, t86, 1, t87)    PLUS
    MOV AX,[BP+6]
    ADD AX,1
    MOV [BP+4],AX
;(return, t87, _, _)    Qreturn
    MOV AX,[BP+4]
    MOV [BP+14],AX
    ADD SP,10
    RET
;(label, L51, _, _)    Qlabel
L51:
;(return, 1, _, _)    Qreturn
    MOV AX,1
    MOV [BP+14],AX
    ADD SP,10
    RET
;(endfunc, hanotic, _, _)    Qendfunc
    ADD SP,10
    RET
    hanotic ENDP
;(func, profbar, int, _)    Qfunc
    profbar PROC NEAR
    SUB SP,88
    MOV BP,SP
;(array, fbn, 30, _)    Qarray
;(declare, _, int, _)    Qdeclare
;(arrayend, _, _, _)    Qarrayend
;(declare, i, int, _)    Qdeclare
;(tempDecl, t88, _, _)    Qtemp_declare
;(=, 1, _, t88)    ASSIGNOP
    MOV AX,1
    MOV [BP+84],AX
;(arrayin, t88, 1, fbn)    Qarrayin
    MOV AX,1
    MOV CX,2
    MUL CL
    MOV SI,AX
    MOV AX,[BP+84]
    MOV SS:[BP+SI+22],AX
;(tempDecl, t89, _, _)    Qtemp_declare
;(=, 1, _, t89)    ASSIGNOP
    MOV AX,1
    MOV [BP+82],AX
;(arrayin, t89, 2, fbn)    Qarrayin
    MOV AX,2
    MOV CX,2
    MUL CL
    MOV SI,AX
    MOV AX,[BP+82]
    MOV SS:[BP+SI+22],AX
;(=, 3, _, i)    ASSIGNOP
    MOV AX,3
    MOV [BP+2],AX
;(label, L52, _, _)    Qlabel
L52:
;(tempDecl, t90, _, _)    Qtemp_declare
;(<, i, 30, t90)    RELOP
    MOV AX,[BP+2]
    CMP AX,30
    MOV AX,1
    JL comp25
    MOV AX,0
comp25:
    MOV [BP+4],AX
;(if, t90, L53, L54)    Qif
    MOV AX,[BP+4]
    CMP AX,0
    JNE L53
    JMP L54
;(label, L53, _, _)    Qlabel
L53:
;(tempDecl, t92, _, _)    Qtemp_declare
;(-, i, 1, t92)    MINUS
    MOV AX,[BP+2]
    SUB AX,1
    MOV [BP+8],AX
;(tempDecl, t93, _, _)    Qtemp_declare
;(arrayout, fbn, t92, t93)    Qarrayout
    MOV AX,[BP+8]
    MOV CX,2
    MUL CL
    MOV SI,AX
    MOV AX,SS:[BP+SI+22]
    MOV [BP+14],AX
;(tempDecl, t94, _, _)    Qtemp_declare
;(-, i, 2, t94)    MINUS
    MOV AX,[BP+2]
    SUB AX,2
    MOV [BP+12],AX
;(tempDecl, t95, _, _)    Qtemp_declare
;(arrayout, fbn, t94, t95)    Qarrayout
    MOV AX,[BP+12]
    MOV CX,2
    MUL CL
    MOV SI,AX
    MOV AX,SS:[BP+SI+22]
    MOV [BP+18],AX
;(tempDecl, t96, _, _)    Qtemp_declare
;(+, t93, t95, t96)    PLUS
    MOV AX,[BP+14]
    ADD AX,[BP+18]
    MOV [BP+16],AX
;(tempDecl, t91, _, _)    Qtemp_declare
;(=, t96, _, t91)    ASSIGNOP
    MOV AX,[BP+16]
    MOV [BP+10],AX
;(arrayin, t91, i, fbn)    Qarrayin
    MOV AX,[BP+2]
    MOV CX,2
    MUL CL
    MOV SI,AX
    MOV AX,[BP+10]
    MOV SS:[BP+SI+22],AX
;(+, i, 1, i)    PLUS
    MOV AX,[BP+2]
    ADD AX,1
    MOV [BP+2],AX
;(goto, L52, _, _)    Qgoto
    JMP L52
;(label, L54, _, _)    Qlabel
L54:
;(=, 1, _, i)    ASSIGNOP
    MOV AX,1
    MOV [BP+2],AX
;(label, L55, _, _)    Qlabel
L55:
;(tempDecl, t98, _, _)    Qtemp_declare
;(<, i, 30, t98)    RELOP
    MOV AX,[BP+2]
    CMP AX,30
    MOV AX,1
    JL comp26
    MOV AX,0
comp26:
    MOV [BP+20],AX
;(if, t98, L56, L57)    Qif
    MOV AX,[BP+20]
    CMP AX,0
    JNE L56
    JMP L57
;(label, L56, _, _)    Qlabel
L56:
;(tempDecl, t99, _, _)    Qtemp_declare
;(arrayout, fbn, i, t99)    Qarrayout
    MOV AX,[BP+2]
    MOV CX,2
    MUL CL
    MOV SI,AX
    MOV AX,SS:[BP+SI+22]
    MOV [BP+86],AX
;(putarg, t99, _, _)    Qputarg
;(tempDecl, t100, _, _)    Qtemp_declare
;(call, printi, _, t100)    Qcall
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
;(tempDecl, t101, _, _)    Qtemp_declare
;(call, printlln, _, t101)    Qcall
    SUB SP,2
    CALL printlln
    ADD SP,0
    MOV BP,SP
    MOV AX,[BP]
    ADD BP,2
    MOV [BP+6],AX
    ADD SP,2
    MOV BP,SP
;(+, i, 1, i)    PLUS
    MOV AX,[BP+2]
    ADD AX,1
    MOV [BP+2],AX
;(goto, L55, _, _)    Qgoto
    JMP L55
;(label, L57, _, _)    Qlabel
L57:
;(return, 0, _, _)    Qreturn
    MOV AX,0
    MOV [BP+90],AX
    ADD SP,88
    RET
;(endfunc, profbar, _, _)    Qendfunc
    ADD SP,88
    RET
    profbar ENDP
;(func, profbst, int, _)    Qfunc
    profbst PROC NEAR
    SUB SP,10
    MOV BP,SP
;(declare, i, int, _)    Qdeclare
;(=, 1, _, i)    ASSIGNOP
    MOV AX,1
    MOV [BP+8],AX
;(label, L58, _, _)    Qlabel
L58:
;(tempDecl, t103, _, _)    Qtemp_declare
;(<, i, 20, t103)    RELOP
    MOV AX,[BP+8]
    CMP AX,20
    MOV AX,1
    JL comp27
    MOV AX,0
comp27:
    MOV [BP+4],AX
;(if, t103, L59, L60)    Qif
    MOV AX,[BP+4]
    CMP AX,0
    JNE L59
    JMP L60
;(label, L59, _, _)    Qlabel
L59:
;(putarg, i, _, _)    Qputarg
;(tempDecl, t104, _, _)    Qtemp_declare
;(call, fbnic, _, t104)    Qcall
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
;(putarg, t104, _, _)    Qputarg
;(tempDecl, t105, _, _)    Qtemp_declare
;(call, printi, _, t105)    Qcall
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
;(+, i, 1, i)    PLUS
    MOV AX,[BP+8]
    ADD AX,1
    MOV [BP+8],AX
;(tempDecl, t107, _, _)    Qtemp_declare
;(call, printlln, _, t107)    Qcall
    SUB SP,2
    CALL printlln
    ADD SP,0
    MOV BP,SP
    MOV AX,[BP]
    ADD BP,2
    MOV [BP+0],AX
    ADD SP,2
    MOV BP,SP
;(goto, L58, _, _)    Qgoto
    JMP L58
;(label, L60, _, _)    Qlabel
L60:
;(return, 0, _, _)    Qreturn
    MOV AX,0
    MOV [BP+12],AX
    ADD SP,10
    RET
;(endfunc, profbst, _, _)    Qendfunc
    ADD SP,10
    RET
    profbst ENDP
;(func, promami, int, _)    Qfunc
    promami PROC NEAR
    SUB SP,44
    MOV BP,SP
;(array, arr, 10, _)    Qarray
;(declare, _, int, _)    Qdeclare
;(arrayend, _, _, _)    Qarrayend
;(declare, i, int, _)    Qdeclare
;(=, 0, _, i)    ASSIGNOP
    MOV AX,0
    MOV [BP+22],AX
;(declare, tmpmax, int, _)    Qdeclare
;(=, 0, _, tmpmax)    ASSIGNOP
    MOV AX,0
    MOV [BP+28],AX
;(declare, tmpmin, int, _)    Qdeclare
;(=, 0, _, tmpmin)    ASSIGNOP
    MOV AX,0
    MOV [BP+42],AX
;(=, 0, _, i)    ASSIGNOP
    MOV AX,0
    MOV [BP+22],AX
;(label, L61, _, _)    Qlabel
L61:
;(tempDecl, t108, _, _)    Qtemp_declare
;(<, i, 10, t108)    RELOP
    MOV AX,[BP+22]
    CMP AX,10
    MOV AX,1
    JL comp28
    MOV AX,0
comp28:
    MOV [BP+30],AX
;(if, t108, L62, L63)    Qif
    MOV AX,[BP+30]
    CMP AX,0
    JNE L62
    JMP L63
;(label, L62, _, _)    Qlabel
L62:
;(tempDecl, t110, _, _)    Qtemp_declare
;(call, scani, _, t110)    Qcall
    SUB SP,2
    CALL scani
    ADD SP,0
    MOV BP,SP
    MOV AX,[BP]
    ADD BP,2
    MOV [BP+24],AX
    ADD SP,2
    MOV BP,SP
;(tempDecl, t109, _, _)    Qtemp_declare
;(=, t110, _, t109)    ASSIGNOP
    MOV AX,[BP+24]
    MOV [BP+34],AX
;(arrayin, t109, i, arr)    Qarrayin
    MOV AX,[BP+22]
    MOV CX,2
    MUL CL
    MOV SI,AX
    MOV AX,[BP+34]
    MOV SS:[BP+SI+0],AX
;(+, i, 1, i)    PLUS
    MOV AX,[BP+22]
    ADD AX,1
    MOV [BP+22],AX
;(goto, L61, _, _)    Qgoto
    JMP L61
;(label, L63, _, _)    Qlabel
L63:
;(arrayout, arr, 0, tmpmax)    Qarrayout
    MOV AX,0
    MOV CX,2
    MUL CL
    MOV SI,AX
    MOV AX,SS:[BP+SI+0]
    MOV [BP+28],AX
;(arrayout, arr, 0, tmpmin)    Qarrayout
    MOV AX,0
    MOV CX,2
    MUL CL
    MOV SI,AX
    MOV AX,SS:[BP+SI+0]
    MOV [BP+42],AX
;(=, 1, _, i)    ASSIGNOP
    MOV AX,1
    MOV [BP+22],AX
;(tempDecl, t114, _, _)    Qtemp_declare
;(call, printlln, _, t114)    Qcall
    SUB SP,2
    CALL printlln
    ADD SP,0
    MOV BP,SP
    MOV AX,[BP]
    ADD BP,2
    MOV [BP+38],AX
    ADD SP,2
    MOV BP,SP
;(label, L64, _, _)    Qlabel
L64:
;(tempDecl, t115, _, _)    Qtemp_declare
;(<, i, 10, t115)    RELOP
    MOV AX,[BP+22]
    CMP AX,10
    MOV AX,1
    JL comp29
    MOV AX,0
comp29:
    MOV [BP+36],AX
;(if, t115, L65, L66)    Qif
    MOV AX,[BP+36]
    CMP AX,0
    JNE L65
    JMP L66
;(label, L65, _, _)    Qlabel
L65:
;(putarg, tmpmax, _, _)    Qputarg
;(tempDecl, t116, _, _)    Qtemp_declare
;(arrayout, arr, i, t116)    Qarrayout
    MOV AX,[BP+22]
    MOV CX,2
    MUL CL
    MOV SI,AX
    MOV AX,SS:[BP+SI+0]
    MOV [BP+40],AX
;(putarg, t116, _, _)    Qputarg
;(call, max, _, tmpmax)    Qcall
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
;(putarg, tmpmin, _, _)    Qputarg
;(tempDecl, t118, _, _)    Qtemp_declare
;(arrayout, arr, i, t118)    Qarrayout
    MOV AX,[BP+22]
    MOV CX,2
    MUL CL
    MOV SI,AX
    MOV AX,SS:[BP+SI+0]
    MOV [BP+32],AX
;(putarg, t118, _, _)    Qputarg
;(call, min, _, tmpmin)    Qcall
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
;(+, i, 1, i)    PLUS
    MOV AX,[BP+22]
    ADD AX,1
    MOV [BP+22],AX
;(goto, L64, _, _)    Qgoto
    JMP L64
;(label, L66, _, _)    Qlabel
L66:
;(putarg, tmpmax, _, _)    Qputarg
;(tempDecl, t121, _, _)    Qtemp_declare
;(call, printi, _, t121)    Qcall
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
;(putarg, tmpmin, _, _)    Qputarg
;(tempDecl, t122, _, _)    Qtemp_declare
;(call, printi, _, t122)    Qcall
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
;(return, 0, _, _)    Qreturn
    MOV AX,0
    MOV [BP+46],AX
    ADD SP,44
    RET
;(endfunc, promami, _, _)    Qendfunc
    ADD SP,44
    RET
    promami ENDP
;(func, probuble, int, _)    Qfunc
    probuble PROC NEAR
    SUB SP,56
    MOV BP,SP
;(declare, temp, int, _)    Qdeclare
;(array, a, 10, _)    Qarray
;(declare, _, int, _)    Qdeclare
;(arrayend, _, _, _)    Qarrayend
;(declare, i, int, _)    Qdeclare
;(declare, j, int, _)    Qdeclare
;(=, 0, _, i)    ASSIGNOP
    MOV AX,0
    MOV [BP+24],AX
;(=, 0, _, j)    ASSIGNOP
    MOV AX,0
    MOV [BP+26],AX
;(label, L67, _, _)    Qlabel
L67:
;(tempDecl, t123, _, _)    Qtemp_declare
;(<, i, 10, t123)    RELOP
    MOV AX,[BP+24]
    CMP AX,10
    MOV AX,1
    JL comp30
    MOV AX,0
comp30:
    MOV [BP+32],AX
;(if, t123, L68, L69)    Qif
    MOV AX,[BP+32]
    CMP AX,0
    JNE L68
    JMP L69
;(label, L68, _, _)    Qlabel
L68:
;(tempDecl, t125, _, _)    Qtemp_declare
;(call, scani, _, t125)    Qcall
    SUB SP,2
    CALL scani
    ADD SP,0
    MOV BP,SP
    MOV AX,[BP]
    ADD BP,2
    MOV [BP+46],AX
    ADD SP,2
    MOV BP,SP
;(tempDecl, t124, _, _)    Qtemp_declare
;(=, t125, _, t124)    ASSIGNOP
    MOV AX,[BP+46]
    MOV [BP+28],AX
;(arrayin, t124, i, a)    Qarrayin
    MOV AX,[BP+24]
    MOV CX,2
    MUL CL
    MOV SI,AX
    MOV AX,[BP+28]
    MOV SS:[BP+SI+0],AX
;(+, i, 1, i)    PLUS
    MOV AX,[BP+24]
    ADD AX,1
    MOV [BP+24],AX
;(goto, L67, _, _)    Qgoto
    JMP L67
;(label, L69, _, _)    Qlabel
L69:
;(=, 1, _, i)    ASSIGNOP
    MOV AX,1
    MOV [BP+24],AX
;(label, L70, _, _)    Qlabel
L70:
;(tempDecl, t127, _, _)    Qtemp_declare
;(<, i, 10, t127)    RELOP
    MOV AX,[BP+24]
    CMP AX,10
    MOV AX,1
    JL comp31
    MOV AX,0
comp31:
    MOV [BP+52],AX
;(if, t127, L71, L77)    Qif
    MOV AX,[BP+52]
    CMP AX,0
    JNE L71
    JMP L77
;(label, L71, _, _)    Qlabel
L71:
;(=, 0, _, j)    ASSIGNOP
    MOV AX,0
    MOV [BP+26],AX
;(label, L72, _, _)    Qlabel
L72:
;(tempDecl, t128, _, _)    Qtemp_declare
;(<, j, i, t128)    RELOP
    MOV AX,[BP+26]
    CMP AX,[BP+24]
    MOV AX,1
    JL comp32
    MOV AX,0
comp32:
    MOV [BP+48],AX
;(if, t128, L73, L76)    Qif
    MOV AX,[BP+48]
    CMP AX,0
    JNE L73
    JMP L76
;(label, L73, _, _)    Qlabel
L73:
;(tempDecl, t129, _, _)    Qtemp_declare
;(arrayout, a, j, t129)    Qarrayout
    MOV AX,[BP+26]
    MOV CX,2
    MUL CL
    MOV SI,AX
    MOV AX,SS:[BP+SI+0]
    MOV [BP+44],AX
;(tempDecl, t130, _, _)    Qtemp_declare
;(arrayout, a, i, t130)    Qarrayout
    MOV AX,[BP+24]
    MOV CX,2
    MUL CL
    MOV SI,AX
    MOV AX,SS:[BP+SI+0]
    MOV [BP+40],AX
;(tempDecl, t131, _, _)    Qtemp_declare
;(>, t129, t130, t131)    RELOP
    MOV AX,[BP+44]
    CMP AX,[BP+40]
    MOV AX,1
    JG comp33
    MOV AX,0
comp33:
    MOV [BP+38],AX
;(if, t131, L74, L75)    Qif
    MOV AX,[BP+38]
    CMP AX,0
    JNE L74
    JMP L75
;(label, L74, _, _)    Qlabel
L74:
;(arrayout, a, j, temp)    Qarrayout
    MOV AX,[BP+26]
    MOV CX,2
    MUL CL
    MOV SI,AX
    MOV AX,SS:[BP+SI+0]
    MOV [BP+20],AX
;(tempDecl, t134, _, _)    Qtemp_declare
;(arrayout, a, i, t134)    Qarrayout
    MOV AX,[BP+24]
    MOV CX,2
    MUL CL
    MOV SI,AX
    MOV AX,SS:[BP+SI+0]
    MOV [BP+34],AX
;(tempDecl, t133, _, _)    Qtemp_declare
;(=, t134, _, t133)    ASSIGNOP
    MOV AX,[BP+34]
    MOV [BP+22],AX
;(arrayin, t133, j, a)    Qarrayin
    MOV AX,[BP+26]
    MOV CX,2
    MUL CL
    MOV SI,AX
    MOV AX,[BP+22]
    MOV SS:[BP+SI+0],AX
;(tempDecl, t135, _, _)    Qtemp_declare
;(=, temp, _, t135)    ASSIGNOP
    MOV AX,[BP+20]
    MOV [BP+30],AX
;(arrayin, t135, i, a)    Qarrayin
    MOV AX,[BP+24]
    MOV CX,2
    MUL CL
    MOV SI,AX
    MOV AX,[BP+30]
    MOV SS:[BP+SI+0],AX
;(label, L75, _, _)    Qlabel
L75:
;(+, j, 1, j)    PLUS
    MOV AX,[BP+26]
    ADD AX,1
    MOV [BP+26],AX
;(goto, L72, _, _)    Qgoto
    JMP L72
;(label, L76, _, _)    Qlabel
L76:
;(+, i, 1, i)    PLUS
    MOV AX,[BP+24]
    ADD AX,1
    MOV [BP+24],AX
;(goto, L70, _, _)    Qgoto
    JMP L70
;(label, L77, _, _)    Qlabel
L77:
;(=, 0, _, i)    ASSIGNOP
    MOV AX,0
    MOV [BP+24],AX
;(tempDecl, t138, _, _)    Qtemp_declare
;(call, printlln, _, t138)    Qcall
    SUB SP,2
    CALL printlln
    ADD SP,0
    MOV BP,SP
    MOV AX,[BP]
    ADD BP,2
    MOV [BP+54],AX
    ADD SP,2
    MOV BP,SP
;(label, L78, _, _)    Qlabel
L78:
;(tempDecl, t139, _, _)    Qtemp_declare
;(<, i, 10, t139)    RELOP
    MOV AX,[BP+24]
    CMP AX,10
    MOV AX,1
    JL comp34
    MOV AX,0
comp34:
    MOV [BP+50],AX
;(if, t139, L79, L80)    Qif
    MOV AX,[BP+50]
    CMP AX,0
    JNE L79
    JMP L80
;(label, L79, _, _)    Qlabel
L79:
;(tempDecl, t140, _, _)    Qtemp_declare
;(arrayout, a, i, t140)    Qarrayout
    MOV AX,[BP+24]
    MOV CX,2
    MUL CL
    MOV SI,AX
    MOV AX,SS:[BP+SI+0]
    MOV [BP+36],AX
;(putarg, t140, _, _)    Qputarg
;(tempDecl, t141, _, _)    Qtemp_declare
;(call, printi, _, t141)    Qcall
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
;(+, i, 1, i)    PLUS
    MOV AX,[BP+24]
    ADD AX,1
    MOV [BP+24],AX
;(goto, L78, _, _)    Qgoto
    JMP L78
;(label, L80, _, _)    Qlabel
L80:
;(return, 0, _, _)    Qreturn
    MOV AX,0
    MOV [BP+58],AX
    ADD SP,56
    RET
;(endfunc, probuble, _, _)    Qendfunc
    ADD SP,56
    RET
    probuble ENDP
;(func, progclc, int, _)    Qfunc
    progclc PROC NEAR
    SUB SP,14
    MOV BP,SP
;(declare, x, int, _)    Qdeclare
;(declare, y, int, _)    Qdeclare
;(label, L81, _, _)    Qlabel
L81:
;(if, 1, L82, L83)    Qif
    MOV AX,1
    CMP AX,0
    JNE L82
    JMP L83
;(label, L82, _, _)    Qlabel
L82:
;(call, scani, _, x)    Qcall
    SUB SP,2
    CALL scani
    ADD SP,0
    MOV BP,SP
    MOV AX,[BP]
    ADD BP,2
    MOV [BP+6],AX
    ADD SP,2
    MOV BP,SP
;(call, scani, _, y)    Qcall
    SUB SP,2
    CALL scani
    ADD SP,0
    MOV BP,SP
    MOV AX,[BP]
    ADD BP,2
    MOV [BP+8],AX
    ADD SP,2
    MOV BP,SP
;(putarg, x, _, _)    Qputarg
;(putarg, y, _, _)    Qputarg
;(tempDecl, t145, _, _)    Qtemp_declare
;(call, gcd, _, t145)    Qcall
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
;(putarg, t145, _, _)    Qputarg
;(tempDecl, t146, _, _)    Qtemp_declare
;(call, printi, _, t146)    Qcall
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
;(putarg, x, _, _)    Qputarg
;(putarg, y, _, _)    Qputarg
;(tempDecl, t147, _, _)    Qtemp_declare
;(call, lcm, _, t147)    Qcall
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
;(putarg, t147, _, _)    Qputarg
;(tempDecl, t148, _, _)    Qtemp_declare
;(call, printi, _, t148)    Qcall
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
;(tempDecl, t149, _, _)    Qtemp_declare
;(call, printlln, _, t149)    Qcall
    SUB SP,2
    CALL printlln
    ADD SP,0
    MOV BP,SP
    MOV AX,[BP]
    ADD BP,2
    MOV [BP+4],AX
    ADD SP,2
    MOV BP,SP
;(goto, L81, _, _)    Qgoto
    JMP L81
;(label, L83, _, _)    Qlabel
L83:
;(return, 0, _, _)    Qreturn
    MOV AX,0
    MOV [BP+16],AX
    ADD SP,14
    RET
;(endfunc, progclc, _, _)    Qendfunc
    ADD SP,14
    RET
    progclc ENDP
;(func, prosqrt, int, _)    Qfunc
    prosqrt PROC NEAR
    SUB SP,8
    MOV BP,SP
;(declare, x, int, _)    Qdeclare
;(label, L84, _, _)    Qlabel
L84:
;(if, 1, L85, L86)    Qif
    MOV AX,1
    CMP AX,0
    JNE L85
    JMP L86
;(label, L85, _, _)    Qlabel
L85:
;(call, scani, _, x)    Qcall
    SUB SP,2
    CALL scani
    ADD SP,0
    MOV BP,SP
    MOV AX,[BP]
    ADD BP,2
    MOV [BP+0],AX
    ADD SP,2
    MOV BP,SP
;(putarg, x, _, _)    Qputarg
;(tempDecl, t151, _, _)    Qtemp_declare
;(call, sqrt, _, t151)    Qcall
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
;(putarg, t151, _, _)    Qputarg
;(tempDecl, t152, _, _)    Qtemp_declare
;(call, printi, _, t152)    Qcall
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
;(tempDecl, t153, _, _)    Qtemp_declare
;(call, printlln, _, t153)    Qcall
    SUB SP,2
    CALL printlln
    ADD SP,0
    MOV BP,SP
    MOV AX,[BP]
    ADD BP,2
    MOV [BP+4],AX
    ADD SP,2
    MOV BP,SP
;(goto, L84, _, _)    Qgoto
    JMP L84
;(label, L86, _, _)    Qlabel
L86:
;(return, 0, _, _)    Qreturn
    MOV AX,0
    MOV [BP+10],AX
    ADD SP,8
    RET
;(endfunc, prosqrt, _, _)    Qendfunc
    ADD SP,8
    RET
    prosqrt ENDP
;(func, main, int, _)    Qfunc
    main PROC NEAR
    SUB SP,0
    MOV BP,SP
;(return, 0, _, _)    Qreturn
    MOV AX,0
    MOV [BP+2],AX
    ADD SP,0
    RET
;(endfunc, main, _, _)    Qendfunc
    ADD SP,0
    RET
    main ENDP
CSEG ENDS
    END START
