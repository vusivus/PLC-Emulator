org 0x2000
0x1E6E : MOVLB 0x0
0x1E70 : BTFSC 0x70, 0, 1
0x1E72 : GOTO 0x01E7E
0x1E76 : MOVLB 0x1
0x1E78 : BSF 0x101, 1, 1
0x1E7A : GOTO 0x01E82
0x1E7E : MOVLB 0x1
0x1E80 : BCF 0x101, 1, 1
0x1E82 : MOVLB 0x1
0x1E84 : BTFSS 0x101, 1, 1
0x1E86 : GOTO 0x01E96
0x1E8A : MOVLB 0x1
0x1E8C : BSF 0x102, 1, 1
0x1E8E : MOVLB 0x0
0x1E90 : BSF 0x73, 0, 1
0x1E92 : GOTO 0x01E9E
0x1E96 : MOVLB 0x0
0x1E98 : BCF 0x73, 0, 1
0x1E9A : MOVLB 0x1
0x1E9C : BCF 0x102, 1, 1
END