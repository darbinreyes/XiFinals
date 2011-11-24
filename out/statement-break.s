.att_syntax prefix
.text
.globl _Itest_paib
_Itest_paib:
	pushq %rbp
	movq %rsp, %rbp
	subq $16, %rsp
	pushq %rbx
	pushq %r12
	pushq %r13
	pushq %r14
	pushq %r15
	movq %rdi, %rbx		# movq %rdi, %r(msg)
	movq %rsi, %r12		# movq %rsi, %r(result)
	leaq .L626(%rip), %rdi		# leaq .L626(%rip), %rdi
	call _I_c_internal_strdup_aii		# call _I_c_internal_strdup_aii
	movq %rax, %r13		# movq %rax, %r(3363)
	movq %r13, %rdi		# movq %r(3363), %rdi
	call _Iprint_pai		# call _Iprint_pai
	movq %rbx, %rdi		# movq %r(msg), %rdi
	call _Iprintln_pai		# call _Iprintln_pai
	movq %r12, %rdi		# movq %r(result), %rdi
	call _Iassert_pb		# call _Iassert_pb
	jmp .L625		# jmp .L625
	.L625:		# .L625:
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
	movq %rdi, %rbx		# movq %rdi, %r(args)
	movq $0, %rbx		# movq $0, %r(3375)
	movq %rbx, %r14		# movq %r(3375), %r(c)
	jmp .L628		# jmp .L628
	.L628:		# .L628:
	movq $10, %rbx		# movq $10, %r(3376)
	cmpq %rbx, %r14		# cmpq %r(3376), %r(c)
	jge .L630		# jge .L630
	.L629:		# .L629:
	movq $5, %rbx		# movq $5, %r(3377)
	cmpq %rbx, %r14		# cmpq %r(3377), %r(c)
	jne .L632		# jne .L632
	.L631:		# .L631:
	jmp .L630		# jmp .L630
	.L630:		# .L630:
	leaq .L633(%rip), %rdi		# leaq .L633(%rip), %rdi
	call _I_c_internal_strdup_aii		# call _I_c_internal_strdup_aii
	movq %rax, %r12		# movq %rax, %r(3367)
	movq $1, %r15		# movq $1, %r(3379)
	movq $5, %r13		# movq $5, %r(3380)
	cmpq %r13, %r14		# cmpq %r(3380), %r(c)
	je .L634		# je .L634
	.L635:		# .L635:
	movq $0, %r15		# movq $0, %r(3381)
	jmp .L634		# jmp .L634
	.L634:		# .L634:
	movq %r12, %rdi		# movq %r(3367), %rdi
	movq %r15, %rsi		# movq %r(3359), %rsi
	call _Itest_paib		# call _Itest_paib
	movq $0, %rbx		# movq $0, %r(3382)
	movq %rbx, %r13		# movq %r(3382), %r(d)
	movq $0, %rbx		# movq $0, %r(3383)
	movq %rbx, %r14		# movq %r(3383), %r(c)
	jmp .L636		# jmp .L636
	.L636:		# .L636:
	movq $10, %rbx		# movq $10, %r(3384)
	cmpq %rbx, %r14		# cmpq %r(3384), %r(c)
	jge .L638		# jge .L638
	.L637:		# .L637:
	jmp .L639		# jmp .L639
	.L639:		# .L639:
	movq $10, %rbx		# movq $10, %r(3385)
	cmpq %rbx, %r13		# cmpq %r(3385), %r(d)
	jge .L641		# jge .L641
	.L640:		# .L640:
	movq $5, %rbx		# movq $5, %r(3386)
	cmpq %rbx, %r13		# cmpq %r(3386), %r(d)
	jne .L643		# jne .L643
	.L642:		# .L642:
	jmp .L641		# jmp .L641
	.L641:		# .L641:
	incq %r14		# incq %r(c)
	jmp .L636		# jmp .L636
	.L643:		# .L643:
	incq %r13		# incq %r(d)
	jmp .L639		# jmp .L639
	.L638:		# .L638:
	leaq .L644(%rip), %rdi		# leaq .L644(%rip), %rdi
	call _I_c_internal_strdup_aii		# call _I_c_internal_strdup_aii
	movq %rax, %r12		# movq %rax, %r(3369)
	movq $1, %rbx		# movq $1, %r(3388)
	movq $5, %r15		# movq $5, %r(3389)
	cmpq %r15, %r13		# cmpq %r(3389), %r(d)
	je .L645		# je .L645
	.L646:		# .L646:
	movq $0, %rbx		# movq $0, %r(3390)
	jmp .L645		# jmp .L645
	.L645:		# .L645:
	movq %r12, %rdi		# movq %r(3369), %rdi
	movq %rbx, %rsi		# movq %r(3361), %rsi
	call _Itest_paib		# call _Itest_paib
	leaq .L647(%rip), %rdi		# leaq .L647(%rip), %rdi
	call _I_c_internal_strdup_aii		# call _I_c_internal_strdup_aii
	movq %rax, %r12		# movq %rax, %r(3371)
	movq $1, %r15		# movq $1, %r(3392)
	movq $10, %r13		# movq $10, %r(3393)
	cmpq %r13, %r14		# cmpq %r(3393), %r(c)
	je .L648		# je .L648
	.L649:		# .L649:
	movq $0, %r15		# movq $0, %r(3394)
	jmp .L648		# jmp .L648
	.L648:		# .L648:
	movq %r12, %rdi		# movq %r(3371), %rdi
	movq %r15, %rsi		# movq %r(3362), %rsi
	call _Itest_paib		# call _Itest_paib
	jmp .L627		# jmp .L627
	.L627:		# .L627:
	popq %r15
	popq %r14
	popq %r13
	popq %r12
	popq %rbx
	addq $16, %rsp
	movq %rbp, %rsp
	popq %rbp
	ret
	.L632:		# .L632:
	incq %r14		# incq %r(c)
	jmp .L628		# jmp .L628
	
.globl _I_c_internal_strdup_aii
_I_c_internal_strdup_aii:
	pushq %rbp
	movq %rsp, %rbp
	subq $32, %rsp
	pushq %rbx
	pushq %r12
	pushq %r13
	pushq %r14
	pushq %r15
	movq %rdi, %rbx		# movq %rdi, %r(3407)
	movq %rbx, -16(%rbp)		# movq %r(3407), -16(%rbp)
	movq -16(%rbp), %rbx		# movq -16(%rbp), %r(3408)
	movq (%rbx), %rbx		# movq (%r(3408)), %r(3395)
	movq %rbx, %r14		# movq %r(3395), %r(t10)
	movq %r14, %rbx		# movq %r(t10), %r(3396)
	salq $3, %rbx		# salq $3, %r(3396)
	movq $8, %r12		# movq $8, %r(3397)
	addq %r12, %rbx		# addq %r(3397), %r(3398)
	movq %rbx, %rdi		# movq %r(3398), %rdi
	call _I_alloc_i		# call _I_alloc_i
	movq %rax, %r12		# movq %rax, %r(3373)
	jmp .L14		# jmp .L14
	.L14:		# .L14:
	movq %r14, %rbx		# movq %r(t10), %r(3399)
	salq $3, %rbx		# salq $3, %r(3399)
	movq %r12, %r13		# movq %r(3373), %r(3400)
	addq %rbx, %r13		# addq %r(3399), %r(3400)
	movq %r14, %r15		# movq %r(t10), %r(3401)
	salq $3, %r15		# salq $3, %r(3401)
	movq -16(%rbp), %rbx		# movq -16(%rbp), %r(3409)
	movq %rbx, -8(%rbp)		# movq %r(3410), -8(%rbp)
	movq -8(%rbp), %rbx		# movq -8(%rbp), %r(3411)
	addq %r15, %rbx		# addq %r(3401), %r(3411)
	movq %rbx, -8(%rbp)		# movq %r(3411), -8(%rbp)
	movq -8(%rbp), %rbx		# movq -8(%rbp), %r(3412)
	movq (%rbx), %rbx		# movq (%r(3412)), %r(3403)
	movq %rbx, (%r13)		# movq %r(3403), (%r(3400))
	decq %r14		# decq %r(t10)
	movq $0, %rbx		# movq $0, %r(3404)
	cmpq %rbx, %r14		# cmpq %r(3404), %r(t10)
	jge .L14		# jge .L14
	.L15:		# .L15:
	movq $8, %rbx		# movq $8, %r(3405)
	addq %rbx, %r12		# addq %r(3405), %r(3406)
	movq %r12, %rax		# movq %r(3406), %rax
	jmp .L16		# jmp .L16
	.L16:		# .L16:
	popq %r15
	popq %r14
	popq %r13
	popq %r12
	popq %rbx
	addq $32, %rsp
	movq %rbp, %rsp
	popq %rbp
	ret
	
#.section .rodata
.align 8
.L633:
	.quad 2
	.quad 35
	.quad 49
	.text

#.section .rodata
.align 8
.L626:
	.quad 8
	.quad 84
	.quad 101
	.quad 115
	.quad 116
	.quad 105
	.quad 110
	.quad 103
	.quad 58
	.text

#.section .rodata
.align 8
.L644:
	.quad 2
	.quad 35
	.quad 50
	.text

#.section .rodata
.align 8
.L647:
	.quad 2
	.quad 35
	.quad 51
	.text

