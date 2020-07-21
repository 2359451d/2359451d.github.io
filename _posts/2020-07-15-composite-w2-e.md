---
layout: post
title: "Cousera笔记 - 计算机组成2 · MARS MIPS Simulator练习"
date: 2020-07-15
excerpt: "MARS: MIPS模拟器练习，概念补全，重点:string array定义(伪)&procedure定义"
tags: [学习笔记, coursera, 计算机组成, 2020, exercise]
feature: https://www.gla.ac.uk/media/Media_299663_smxx.jpg
comments: true
---

计算机组成第二讲作业，很多地方可以酌情优化，有缘再补

[参考课程 - 计算机组成](https://www.coursera.org/learn/jisuanji-zucheng/home/week/3)
[其他参考 - lecture notes (VT)](http://courses.cs.vt.edu/~cs2505/fall2010/Notes/pdf/)
[其他参考 - lecture notes (U,WU)](https://courses.cs.washington.edu/courses/cse378/09wi/lectures.html)

* 目录
{:toc}

## Accessing Array Data in MIPS

[reference document](http://people.cs.pitt.edu/~xujie/cs447/AccessingArray.htm)

![](/static/2020-07-19-00-09-00.png)
![](/static/2020-07-19-00-10-51.png)

🍊 MIPS访问数组

1. 数组支持3种基本操作

    * 获取数组元素`x = list[i]`
    * 存储值至数组特定索引`list[i] = x`
    * 数组长度`list.length`

2. 访问数组元素需通过元素地址后使用`lw`/`sw`指令. MIPS中每个字占`32bit(4byte)`

    * 如声明`list: .word 3, 0, 1, 2, 6, -2, 4, 7, 3, 7`，则`la $t3, list`载入地址为数组中元素`3`的地址。如访问`0`元素，偏移量为`4`

```mips
# $t4:= list[6]

la $t3, list         # put address of list into $t3
li $t2, 6            # put the index into $t2
add $t2, $t2, $t2    # double the index
add $t2, $t2, $t2    # double the index again (now 4x)
add $t1, $t2, $t3    # combine the two components of the address
lw $t4, 0($t1)       # get the value from the array cell

```

如需要存储`$t4`内容至数组元素`list[6]`，使用

* `sw $t4, 0($t1)`

## Array Declaration

[参考](http://courses.cs.vt.edu/~cs2505/summer2011/Notes/pdf/T23.MIPSArrays.pdf)

🍊 分配数组固定内存空间

```mips
.data
list:  .space 1000
```

* 定义数组大小（单位：Byte），label可作为数组起始地址
* ![](/static/2020-07-18-23-10-17.png)

---

🍊 定义元素

```mips
.data
vowels: .byte 'a', 'e', 'i', 'o', 'u'
pow2: .word 1, 2, 4, 8, 16, 32, 64, 128
```

* `.byte`定义
  * 初始化连续的字节空间，数组每个元素存储在单个字节中
  * 元素地址`vowels[k] == vowels+k(index)`
* `.word`定义
  * 初始化连续字(4byte)空间，数组每个元素存储在单个字中
  * 元素地址`pow2[k] == pow2+ 4 * k(index)`
* `.asciiz`定义（<font color="blue">特殊string数组</font>）
  * 类似`.byte`定义生成的单字符数组，不同的是，以`0x00`结尾，<font color="blue">判断结束符，省去提前知道数组长度</font>
  * ![](/static/2020-07-18-23-18-39.png)

---

🍬 其他声明方法，如初始化生成相同元素(默认)

![](/static/2020-07-19-00-15-49.png)

## Switch

[reference](http://euler.ecs.umass.edu/ece232/pdf/07-MIPSIII-11.pdf)

![](/static/2020-07-16-00-31-54.png)
![](/static/2020-07-16-00-32-24.png)

![](/static/2020-07-16-00-50-48.png)

## Conditional & Unconditional Branches

🍬 注意可使用直接转移or间接转移

🍊 条件分支 - branch if $s1 equals $s2

* `beq $s1, $s2, L1`
* Similarly, `bne $s1, $s2, L1`

🍊 无条件分支

* `j L1`
* `jr $s5`，多条case语句&跳转实用(间接转移)

![](/static/2020-07-16-00-59-36.png)

## j & jr & jal

🍊 3种跳转指令

* `j`
  * 最简单的跳转
* `jr`
  * 涉及函数调用，跳转至寄存器指定地址，一般用于函数调用结尾
* `jal`
  * 链接&跳转，将下条指令地址存入`$ra`作为返回后继续执行的地址

## Procedures

### Some Term

[reference](http://jjc.hydrus.net/cs61c/handouts/proced1.pdf)

![](/static/2020-07-19-15-38-52.png)

> prologue: 栈空间分配&相关寄存器信息初始化代码块称为*prologue*
>
> epilogue：栈帧释放&重载更新寄存器代码块称为*epilogue*
>
> body：其他部分代码块称为*body*，即过程/函数体

---

🍊 NON-LEAF & LEAF PROCEDURE

> Non-leaf procedure: a procedure that **call other procedure**, for example, main function
>
> Leaf procedure: a procedure that **doesn't call any other procedure**

### Concepts & Register Related

🍊 涉及寄存器

![](/static/2020-07-16-01-15-48.png)

* `$2~$3/$v0,$v1`: 过程返回值
* `$4~$7/$a0~$a3`: 过程参数
* `$gp`: 全局指针
* `$sp`: 栈指针
* `$fp`: 栈帧指针
* `$ra`: 返回地址

🍊 PC寄存器

* program coutner
* 存放当前指令地址或下条指令地址

🍊 jal指令

* 进行过程调用: 跳转&链接
* `PC+4`的值存至`$ra`寄存器中(返回地址)
* `jal NewProcedureAddress`跳转至过程地址

🍬 因为jal指令会改变`$ra`的值，因此使用前需要存储该值

🍊 栈

![](/static/2020-07-18-14-16-43.png)

* 过程的暂存器scratchpad - 易失的
  * 调用其他过程时，寄存器都会改变
* 因此每个过程的寄存器值需要备份在栈(内存维护)中
* <font color="blue">注意，栈用于存放local variables & extra args & return values</font>

🍬 栈&过程相关寄存器

![](/static/2020-07-18-14-39-22.png)

* 值备份
  * 静态变量寄存器(saved register) - `$s0-s7`,<font color="blue">使用完毕后调用者进行restore</font>
  * 栈指针 - `$sp`
  * 返回地址 - `$ra`
* 不备份
  * 变量 - `$t0-t9`
  * 过程参数 - `$a0-a3`
  * 过程返回值 - `$v0-v1`

### Call & Return

1. `jal`调用过程后，参数存入`$a0-a3`
2. 每个过程维护自己栈变量空间
3. 创建栈空间后，更新栈指针`$sp`
4. 过程调用完毕后，将返回值存入`$v0-v1`，<font color="blue">释放栈空间，`$sp`栈指针减量</font>

### Leaf Procedure Example

![](/static/2020-07-18-14-58-44.png)
![](/static/2020-07-18-15-07-09.png)

### Calling Procedure: change of the Stack Frame

[参考1](https://www.cnblogs.com/zlcxbb/p/5759776.html)
[参考2](https://www.cnblogs.com/yanwei-wang/p/8065855.html)
】】】】】】**寄存器**
* `%ebp` - 帧指针寄存器 extended base pointer
  * 永远指向系统栈中最上面栈帧的底部
* `%esp` - 栈指针寄存器 extended stack pointer
  * 永远指向系统栈中最上面栈帧的顶部
* <font color="blue">程序运行时，栈指针`%esp`可以移动，访问过程信息通过`%ebp-4,+8`等地址</font>

🍊 函数栈帧（代码区）

* ESP和EBP之间的内存空间为当前栈帧
* EBP - 当前栈帧底部，帧指针
* ESP - 当前栈帧顶部，栈指针

---

🍬 函数调用包括以下步骤

* 参数入栈： 将参数<font color="blue">从右向左</font>，依次压入栈中
* 返回地址入栈（EBP）：调用指令的下条指令地址压入栈中，供函数返回时继续执行
* 代码区跳转：CPU从当前代码区，跳转至被调用函数callee入口处
* **栈帧调整**
  * <font color="blue">保存当前栈帧状态值</font>，用于恢复本栈帧(EBP压入栈)
  * 切换当前栈帧至<font color="blue">新栈帧，新EBP<-旧ESP</font>
  * <font color="blue">为新栈帧分配空间，新ESP-所需空间（由高到低入栈）</font>，抬高栈顶

### Giving a Procedure Arguments

🍊 涉及4个寄存器(MIPS),`$a0~a3`，每个字长`32bit`

* 如需要额外参数，则需要先将现有寄存器信息压入栈

### Push Elements to Stack

🍊 压栈

* `$sp`栈指针下移(减法，栈帧地址由下生长，高到低)
* 将元素压入栈

![](/static/2020-07-20-20-40-17.png)

```mips
# push $t1 & $t2
sub $sp, $sp, 8
sw $t1, 4($sp)
sw $t2, 0($sp)

# similarily
sw $t1, -4($sp)
sw $t2, -8($sp)
sub $sp, $sp, 8
```

### Accessing and Pop Elements

![](/static/2020-07-20-20-43-41.png)

🍊 获取信息

* 知道偏移量情况下，可以获取栈中任意元素

🍊 获取`$t1`值

```mips
lw $s0, 4($sp)
```

🍊 弹栈，释放栈帧

* 如弹出`$t2`，`addi $sp, $sp, 4`
  * 数据仍存在于RAM中，但无法通过`$sp`访问

## Exercise 1

🍬 坑，mips中定义string数组没有直接方法，不能直接`List: .word "ABC","CBD","DEF",...`定义，每个string元素实质可看作是指针(标签)

* 第二种解法，通过`.asciiz`定义的结束符`\0`判断偏移量，循环遍历,<font color='blue'>注意生成的是字符长串，非数组，每个元素由结束符'\0'隔开</font>
* `\0`占1个字节

![](/static/2020-07-20-22-45-04.png)
![](/static/2020-07-20-22-45-35.png)

看几个帮助文档写出来的东西非常低效草，，个人思路是字典/switch/jumptable但是没搞明白jumptable怎么实现的

先用if结构实现了，完整的就不贴了，丢人没什么意义
![](/static/2020-07-15-23-27-02.png)

---

### Solution

#### Python
🍊 改进 - 对照高级语言实现

```python
"""
determine if the input character is capital
return boolean
"""
def isCapital(char):
    if 65<=ord(char)<=90:
        return True

"""
determine if the input character is lower
return boolean
"""
def isUncapitalized(char):
    if 97<=ord(char)<=122:
        return True

"""
determine if the input character is numeric
return boolean
"""
def isNumeric(char):
    if 48<=ord(char)<=57:
        return True

"""
main method
"""
if __name__ == "__main__":
    capitalList = ["Alpha","Bravo","China","Delta","Echo","Foxtrot","Golf","Hotel","India","Juliet","Kilo","Lima","Mary","November","Oscar","Paper","Quebec","Research","Sierra","Tango","Uniform","Victor","Whisky","X-ray","Yankee","Zulu"]
    numericList = ["zero","First","Second","Third","Fourth","Fifth","Sixth","Seventh","Eighth","Nineth"]
    lowerList = ["alpha","bravo","china","delta","echo","foxtrot","golf","hotel","india","juliet","kilo","lima","mary","november","oscar","paper","quebec","research","sierra","tango","uniform","victor","whisky","x-ray","yankee","zulu"]
    while True:
        prompt = input("Please type a character, '?' to exit:\n")
        if prompt == '?':
            break
        if isNumeric(prompt):
            result = numericList[int(prompt)]
        elif isCapital(prompt):
            # 65-90
            result = capitalList[ord(prompt)-65]
        elif isUncapitalized(prompt):
            # 97-122
            result = lowerList[ord(prompt)-97]
        else:
            result = '*' # result = chr(42)
        print(result)
```

#### MIPS

```mips
	.data
  newline:	.asciiz "\n"
  msg_prompt:	.asciiz "Please type a character, '?' to exit:"
  capitalListLen:	.space 104 # 26 * 4B = 104B
  capitalList: .asciiz "Alpha","Bravo","China","Delta","Echo","Foxtrot","Golf","Hotel","India","Juliet","Kilo","Lima","Mary","November","Oscar","Paper","Quebec","Research","Sierra","Tango","Uniform","Victor","Whisky","X-ray","Yankee","Zulu"
  numericListLen:	.space 40
  numericList: .asciiz "zero","First","Second","Third","Fourth","Fifth","Sixth","Seventh","Eighth","Nineth"
  lowerListLen:		.space 104
  lowerList:  .asciiz  "alpha","bravo","china","delta","echo","foxtrot","golf","hotel","india","juliet","kilo","lima","mary","november","oscar","paper","quebec","research","sierra","tango","uniform","victor","whisky","x-ray","yankee","zulu"
	.text
main:
  ## print prompt & read char
  li $v0, 4
  la $a0, msg_prompt
  syscall
  li $v0, 4
  la $a0, newline
  syscall
  li $v0, 12 
  syscall
  
  ## check if input=='?'
  move $a0, $v0 # $a0 = prompt
  beq $a0, 63, Terminate # prompt=='?' ,type '?' to terminate

########################
checkIfCapital:
  jal isCapital # isCapital(prompt)
  seq $t0, $v0, 1 # set $t0=1 if $v0 == 1
  bne $t0, 1, checkIfNumeric # branch if not isCapital(prompt)
  
  ## if is capital
  subi $a0, $a0, 65 # prompt-65, arg1: index
  la $a1, capitalList # arg2: &List
  jal getElement
  move $v1, $v0
  la $a0, newline
  li $v0, 4
  syscall
  la $a0, ($v1)
  li $v0, 4
  syscall
  la $a0, newline
  li $v0, 4
  syscall
  j main
########################
checkIfNumeric:
  jal isNumeric # isNumeric(prompt)
  seq $t0, $v0, 1 # set $t0=1 if $v0 == 1
  bne $t0, 1, checkIfLower # branch if not isNumeric(prompt)
  
  ## if is numeric
  subi $a0, $a0, 48 # prompt-48, arg1: index
  la $a1, numericList # arg2: &List
  jal getElement
  move $v1, $v0
  la $a0, newline
  li $v0, 4
  syscall
  la $a0, ($v1)
  li $v0, 4
  syscall
  la $a0, newline
  li $v0, 4
  syscall
  j main
########################
checkIfLower:
  jal isUncapitalized # isUncapitalized(prompt)
  seq $t0, $v0, 1 # set $t0=1 if $v0 == 1
  bne $t0, 1, other # branch if not isNumeric(prompt)
  
  ## if is lower case
  subi $a0, $a0, 97 # prompt-97, arg1: index
  la $a1, lowerList # arg2: &List
  jal getElement
  move $v1, $v0
  la $a0, newline
  li $v0, 4
  syscall
  la $a0, ($v1)
  li $v0, 4
  syscall
  la $a0, newline
  li $v0, 4
  syscall
  j main
########################
other: # print ‘*’
  li $v0, 4
  la $a0, newline
  syscall
  li $a0, 42
  li $v0, 11
  syscall
  li $v0, 4
  la $a0, newline
  syscall
  j main
########################
Terminate:
  li $v0, 10
  syscall
########################
getElement: # int getElement($a0: index, $a1: list), return the element address
  addi $sp, $sp, -12 # reuse 3 reg, allocating 12B of contiguous block
  sw $ra, 12($sp)
  sw $t0, 8($sp)
  sw $t1, 4($sp)
  sw $t2, 0($sp)

  ## initialise
  move $t0, $a1 # $t0=i=start address of List
  li $t1, 0 # $t1=j=0
  Loop: # find the index(byte)
  	beq $t1, $a0, endLoop # branch if $t1==$a0: index, (scan finished at the position expected)
  	lb $t2, ($t0) # $t2:= capitalList[i](byte)
  	addi $t0, $t0, 1
  	beq $t2, $zero, nextElement # $t2?= '\0'
  	j Loop
  nextElement:
  	addi $t1, $t1, 1
  	j Loop
  endLoop:
        move $v0, $t0
  lw $ra, 12($sp)
  lw $t0, 8($sp)
  lw $t1, 4($sp)
  lw $t2, 0($sp)
  addi $sp, $sp, 12
  jr $ra
########################
isCapital:
  # one arg: char allocate 4B Stack
  addi $sp, $sp, -4
  sw $t0, 0($sp)
  sw $ra, 4($sp)
  
  move $t0, $a0 # char = $a0
  blt $t0, 65, notNumeric # branch if char<=65
  bgt $t0, 90, notNumeric # branch if char>=90
  li $v0, 1
  j endIsCapital
notNumeric:
  li $v0, 0
endIsCapital:
  lw $ra, 4($sp)
  lw $t0, 0($sp)
  addi $sp, $sp, 4
  jr $ra
########################
isNumeric:
  # arg: char allocate 4B  contiguous block of memory for stack
  addi $sp, $sp, -4
  sw $ra, 4($sp)
  sw $t0, 0($sp) # char
  
  move $t0, $a0
  blt $t0, 48, isAlphabet
  bgt $t0, 57, isAlphabet
  li $v0, 1
  j endIsNumeric
isAlphabet:
  li $v0, 0
endIsNumeric:
  lw $ra, 4($sp)
  lw $t0, 0($sp)
  addi $sp, $sp, 4
  jr $ra
########################
isUncapitalized:
  # arg: char allocate 4B contiguous block of memory for stack
  addi $sp, $sp, -4
  sw $ra, 4($sp)
  sw $t0, 0($sp)
  
  move $t0, $a0
  blt $t0, 97, notUncapitalized
  bgt $t0, 122, notUncapitalized
  li $v0, 1
  j endIsUncapitalized
notUncapitalized:
  li $v0, 0
endIsUncapitalized:
  lw $ra, 4($sp)
  lw $t0, 0($sp)
  addi $sp, $sp, 4
  jr $ra
```

---

指针写法参考

![](/static/2020-07-21-19-08-02.png)

## Exercise 2

![](/static/2020-07-21-23-03-32.png)

注意string录入缓冲地址&大小，其他无难度

### Solution

#### Python

`charFinder.py`

```python
def charFinder(user_input,ch):
    flag = -1
    for i in range(len(user_input)):
            if user_input[i]==ch:
                flag = i
                break
    return flag

if __name__ == "__main__":
    prompt = input("Please type a string:")
    while True:
        char = input("Please type a char for searching, '?' to exit:") # case sensetive
        if char=='?':
            break
        index = charFinder(prompt, char)
        if index!=-1:
            print("Success!Location:{0}".format(index+1))
        else:
            print("Fail!")
```

#### MIPS

```mips
	.data
string_buffer: .space 1024 
msg_prompt:  .asciiz "Please type a string:\n"
msg_prompt2:  .asciiz "Please type a char for searching, '?' to exit:\n"
msg_fail:  .asciiz "Fail!\n"
msg_success:  .asciiz "Success!Location: "
newline: .asciiz "\n"
	.text
main:
## print prompt & read string
  li $v0, 4
  la $a0, msg_prompt
  syscall
  la $a0, string_buffer
  li $a1, 1024
  li $v0, 8
  syscall
  move $t0, $a0 # $t0:= string
#########################################
Loop:
## print prompt & read char circularly
  li $v0, 4
  la $a0, msg_prompt2
  syscall
  li $v0, 12
  syscall
  move $t1, $v0 # $t1:= char
  li $v0, 4
  la $a0, newline
  syscall

  beq $t1, 63, endLoop
  move $a0, $t0
  move $a1, $t1
  jal charFinder # $v0 = charFinder(prompt, char)
  beq $v0, -1,Fail # flag=index=-1
  move $t2, $v0
Success:
  la $a0, msg_success
  li $v0, 4
  syscall

  move $a0, $t2
  li $v0, 1
  syscall
  
  la $a0, newline
  li $v0, 4
  syscall
  
  j Loop
Fail:
  li $v0, 4
  la $a0, msg_fail
  syscall
  j Loop
endLoop:
  li $v0, 10
  syscall
#########################################
charFinder:# int charFinder($a0: prompt, $a1: char)
  addi $sp, $sp, -16 # allocate 20B contiguous block of memory
  sw $ra, 16($sp)
  sw $t0, 12($sp)
  sw $t1, 8($sp)
  sw $t2, 4($sp)
  sw $t3, 0($sp)
  
  li $t0, -1 # flag = -1
  li $t1, 0 # i = 0
  
  readByte:
    move $t2, $a0
    add $t2, $t2, $t1
    add $t1, $t1, 1 # i++
    lb $t3, ($t2) # current char
    beq $t3, $zero, endReadByte
    bne $t3, $a1, readByte
    move $t0, $t1
  endReadByte:
  move $v0, $t0 # return flag
  lw $ra, 16($sp)
  lw $t0, 12($sp)
  lw $t1, 8($sp)
  lw $t2, 4($sp)
  lw $t3, 0($sp)
  addi $sp, $sp, 16
  jr $ra
  

```