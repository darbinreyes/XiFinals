.att_syntax prefix
.text
.globl _Ifib_ii
_Ifib_ii:
	pushq %rbp
	movq %rsp, %rbp
	subq $16, %rsp
	pushq %rbx
	pushq %r12
	pushq %r13
	pushq %r14
	pushq %r15
	movq %rdi, %r15		# movq %rdi, %r(9)
	movq $2, %rbx		# movq $2, %r(32)
	cmpq %rbx, %r15		# cmpq %r(32), %r(9)
	jge .L17		# jge .L17
	.L16:		# .L16:
	movq %r15, %rax		# movq %r(9), %rax
	jmp .L15		# jmp .L15
	jmp .L18		# jmp .L18
	.L17:		# .L17:
	movq $1, %rbx		# movq $1, %r(33)
	movq %r15, %r13		# movq %r(9), %r(34)
	subq %rbx, %r13		# subq %r(33), %r(34)
	movq %r13, %rdi		# movq %r(34), %rdi
	call _Ifib_ii		# call _Ifib_ii
	movq %rax, %r13		# movq %rax, %r(19)
	movq $2, %rbx		# movq $2, %r(35)
	subq %rbx, %r15		# subq %r(35), %r(36)
	movq %r15, %rdi		# movq %r(36), %rdi
	call _Ifib_ii		# call _Ifib_ii
	movq %rax, %rbx		# movq %rax, %r(20)
	addq %rbx, %r13		# addq %r(20), %r(37)
	movq %r13, %rax		# movq %r(37), %rax
	jmp .L15		# jmp .L15
	.L18:		# .L18:
	.L15:		# .L15:
	popq %r15
	popq %r14
	popq %r13
	popq %r12
	popq %rbx
	addq $16, %rsp
	movq %rbp, %rsp
	popq %rbp
	ret
	
.globl _Imain_paai
_Imain_paai:
	pushq %rbp
	movq %rsp, %rbp
	subq $16, %rsp
	pushq %rbx
	pushq %r12
	pushq %r13
	pushq %r14
	pushq %r15
	movq %rdi, %rbx		# movq %rdi, %r(10)
	leaq .L20(%rip), %rdi		# leaq .L20(%rip), %rdi
	call _I_c_internal_strdup_aii		# call _I_c_internal_strdup_aii
	movq %rax, %rbx		# movq %rax, %r(21)
	movq %rbx, %rdi		# movq %r(21), %rdi
	call _Iprint_pai		# call _Iprint_pai
	call _Ireadln_ai		# call _Ireadln_ai
	movq %rax, %r13		# movq %rax, %r(23)
	movq $16, %r12		# movq $16, %r(39)
	movq %r12, %rdi		# movq %r(39), %rdi
	call _I_alloc_i		# call _I_alloc_i
	movq %rax, %rbx		# movq %rax, %r(24)
	movq %rbx, %rdi		# movq %r(13), %rdi
	movq %r13, %rsi		# movq %r(11), %rsi
	call _IparseInt_t2ibai		# call _IparseInt_t2ibai
	movq 0(%rbx), %r13		# movq 0(%r(13)), %r(14)
	movq 8(%rbx), %rbx		# movq 8(%r(13)), %r(15)
	movq $1, %r12		# movq $1, %r(40)
	xorq %r12, %rbx		# xorq %r(40), %r(41)
	movq $1, %r12		# movq $1, %r(42)
	xorq %r12, %rbx		# xorq %r(42), %r(43)
	testq $1, %rbx		# testq $1, %r(43)
	jne .L22		# jne .L22
	.L21:		# .L21:
	leaq .L23(%rip), %rdi		# leaq .L23(%rip), %rdi
	call _I_c_internal_strdup_aii		# call _I_c_internal_strdup_aii
	movq %rax, %rbx		# movq %rax, %r(26)
	movq %rbx, %rdi		# movq %r(26), %rdi
	call _Iprintln_pai		# call _Iprintln_pai
	jmp .L19		# jmp .L19
	.L22:		# .L22:
	movq %r13, %rdi		# movq %r(14), %rdi
	call _Ifib_ii		# call _Ifib_ii
	movq %rax, %rbx		# movq %rax, %r(28)
	movq %rbx, %rdi		# movq %r(28), %rdi
	call _IunparseInt_aii		# call _IunparseInt_aii
	movq %rax, %rbx		# movq %rax, %r(29)
	movq %rbx, %rdi		# movq %r(29), %rdi
	call _Iprintln_pai		# call _Iprintln_pai
	.L19:		# .L19:
	popq %r15
	popq %r14
	popq %r13
	popq %r12
	popq %rbx
	addq $16, %rsp
	movq %rbp, %rsp
	popq %rbp
	ret
	
.globl _I_c_internal_strdup_aii
_I_c_internal_strdup_aii:
	pushq %rbp
	movq %rsp, %rbp
	subq $48, %rsp
	pushq %rbx
	pushq %r12
	pushq %r13
	pushq %r14
	pushq %r15
	movq %rdi, %rbx		# movq %rdi, %r(60)
	movq %rbx, -24(%rbp)		# movq %r(60), -24(%rbp)
	movq -24(%rbp), %rbx		# movq -24(%rbp), %r(61)
	movq (%rbx), %r12		# movq (%r(61)), %r(45)
	movq %r12, -32(%rbp)		# movq %r(62), -32(%rbp)
	movq -32(%rbp), %rbx		# movq -32(%rbp), %r(63)
	salq $3, %rbx		# salq $3, %r(46)
	movq $8, %r12		# movq $8, %r(47)
	addq %r12, %rbx		# addq %r(47), %r(48)
	movq %rbx, %rdi		# movq %r(48), %rdi
	call _I_alloc_i		# call _I_alloc_i
	movq %rax, %r12		# movq %rax, %r(31)
	movq %r12, -40(%rbp)		# movq %r(64), -40(%rbp)
	.L24:		# .L24:
	movq -32(%rbp), %r13		# movq -32(%rbp), %r(65)
	salq $3, %r13		# salq $3, %r(49)
	movq -40(%rbp), %r12		# movq -40(%rbp), %r(66)
	addq %r13, %r12		# addq %r(49), %r(50)
	movq -32(%rbp), %r13		# movq -32(%rbp), %r(67)
	movq %r13, -16(%rbp)		# movq %r(68), -16(%rbp)
	movq -16(%rbp), %rbx		# movq -16(%rbp), %r(69)
	salq $3, %rbx		# salq $3, %r(69)
	movq %rbx, -16(%rbp)		# movq %r(69), -16(%rbp)
	movq -24(%rbp), %r13		# movq -24(%rbp), %r(70)
	movq %r13, -8(%rbp)		# movq %r(57), -8(%rbp)
	movq -8(%rbp), %rbx		# movq -8(%rbp), %r(58)
	movq -16(%rbp), %r13		# movq -16(%rbp), %r(71)
	addq %r13, %rbx		# addq %r(71), %r(58)
	movq %rbx, -8(%rbp)		# movq %r(58), -8(%rbp)
	movq -8(%rbp), %rbx		# movq -8(%rbp), %r(59)
	movq (%rbx), %rbx		# movq (%r(59)), %r(53)
	movq %rbx, (%r12)		# movq %r(53), (%r(50))
	movq -32(%rbp), %rbx		# movq -32(%rbp), %r(72)
	decq %rbx		# decq %r(72)
	movq %rbx, -32(%rbp)		# movq %r(72), -32(%rbp)
	movq $0, %rbx		# movq $0, %r(54)
	movq -32(%rbp), %r12		# movq -32(%rbp), %r(73)
	cmpq %rbx, %r12		# cmpq %r(54), %r(73)
	jge .L24		# jge .L24
	.L25:		# .L25:
	movq $8, %rbx		# movq $8, %r(55)
	movq -40(%rbp), %r13		# movq -40(%rbp), %r(74)
	addq %rbx, %r13		# addq %r(55), %r(56)
	movq %r13, %rax		# movq %r(56), %rax
	popq %r15
	popq %r14
	popq %r13
	popq %r12
	popq %rbx
	addq $48, %rsp
	movq %rbp, %rsp
	popq %rbp
	ret
	
#.section .rodata
.align 8
.L20:
	.quad 33
	.quad 80
	.quad 108
	.quad 101
	.quad 97
	.quad 115
	.quad 101
	.quad 32
	.quad 101
	.quad 110
	.quad 116
	.quad 101
	.quad 114
	.quad 32
	.quad 97
	.quad 32
	.quad 112
	.quad 111
	.quad 115
	.quad 105
	.quad 116
	.quad 105
	.quad 118
	.quad 101
	.quad 32
	.quad 110
	.quad 117
	.quad 109
	.quad 98
	.quad 101
	.quad 114
	.quad 32
	.quad 58
	.quad 32
	.text

#.section .rodata
.align 8
.L23:
	.quad 14
	.quad 73
	.quad 110
	.quad 118
	.quad 97
	.quad 108
	.quad 105
	.quad 100
	.quad 32
	.quad 105
	.quad 110
	.quad 112
	.quad 117
	.quad 116
	.quad 33
	.text
