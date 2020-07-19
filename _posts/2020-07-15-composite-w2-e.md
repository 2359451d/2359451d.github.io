---
layout: post
title: "Cousera笔记 - 计算机组成2 · MARS MIPS Simulator练习"
date: 2020-07-15
excerpt: "MARS: MIPS模拟器练习，概念补全"
tags: [学习笔记, coursera, 计算机组成, 2020, exercise]
feature: https://www.gla.ac.uk/media/Media_299663_smxx.jpg
comments: true
---

计算机组成第二讲作业，笨比写法orz

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

### 调用过程栈帧变化

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

## 第一题

看几个帮助文档写出来的东西非常低效草，，个人思路是字典/switch/jumptable但是没搞明白jumptable怎么实现的

先用if结构实现了，完整的就不贴了，丢人没什么意义
![](/static/2020-07-15-23-27-02.png)