---
layout: post
title: "System LECTURE 21 - Spectre & Meltdown"
date: 2020-06-02
excerpt: "两大热门漏洞"
tags: [学习笔记, system, 计算机组成, 2020]
feature: https://www.gla.ac.uk/media/Media_299663_smxx.jpg
comments: true
---

* 目录
{:toc}

[参考 - 简要了解](https://www.jianshu.com/p/301c91b57896)
[参考 - 科普spectre&meltdown以及自测方法](http://blog.sciencenet.cn/blog-684007-1093420.html)

![](/static/meltdownAndSpectre.png)

## Two Problems

![](/static/2020-06-02-21-40-39.png)

* 两个安全漏洞: 内存攻击
* Meltdown
  * 影响intel处理器
  * 存在修复补丁
* Spectre
  * 影响几乎所有CPU
  * 暂无解决对策

🍬 检查系统更新状态

---

![](/static/2020-06-07-22-13-40.png)

* 这两个漏洞相似，都通过缓存速度得出私有字节

🍬 不同点

* Spectre - 使用分支预测，请求一个已知数据块
* Meltdown - 利用内存映射在英特尔处理器上完成的方式，并使用陷阱

### Meltdown

> 利用Intel处理器的**预测执行**设计，攻击内存空间。有机会获取OS级别的数据
>
> INTEL的熔断缺陷是，其处理器先进行**推测访问，然后再判断是否合法，如果不合法就消除影响**(与IBP分支预测无关)

* 这种访问顺序不影响最终计算结果，<font color="blue">却影响系统某部分(如缓存)的变化</font>
  * AMDCPU没有这个缺陷是因为，先判断合法性再执行指令

🍬 解决方法

* 可以通过补丁进行修复，<font color="blue">对系统性能有影响</font>
* 把核心代码空间与用户空间隔离开，每次需要系统调用时清空地址翻译表，使系统调用成本非常高
  * <font color="purple">使用intel的系统可能有高达30%的性能损失</font>

![](/static/2020-06-08-22-01-50.png)
![](/static/2020-06-09-00-52-23.png)

* mapping user space to kernel space

![](/static/2020-06-09-02-56-53.png)

* measuring the speed of access

### Spectre

> 破坏应用之间的隔离性，攻击者有机会访问**应用程序所使用的内存空间&数据**
>
> Spectre目前有两种变体，AMD官方宣称的对Spectre的'near zero risk'是指的Spectre的一种变体而非全部
>
> 幽灵攻击利用被攻击代码&CPU双重缺陷，**被攻击代码必须会根据某个条件产生习惯性操作**

🍬 假设攻击目标中有“如果A则B”这样的代码

* 攻击者先不断调用被攻击代码，每次给出条件A为TRUE，<font color="blue">如果处理器根据之前的经验预测下一个操作条件不变而根据错误的预测进行猜想执行，攻击者就可能诱发数据的泄露</font>
* 难以通过补丁修复

🍙 理论上，凡是具备间接分叉预测（IBP） 执行的处理器都可能存在幽灵缺陷

* 但AMD的指令分叉预测系统比较复杂（具备人工智能），研究人员目前尚未找到其规律与实际突破口

![](/static/2020-06-08-22-00-04.png)

## Memory: Overview

> * victim process(被窃取数据): 内存中存放**私有**数据字节**S**，受保护
> * attacker process: 判断最近访问内存地址的进程

* 存在一段代码被执行后，根据S值决定victim进程访问内存地址
  * 尽管由于内存保护机制，该代码段不会直接被执行(缓存，乱序执行预测)
* 攻击者推测缓存速度(哪个加载更快)，推测S原值

### Instructions don't all take the same time

![](/static/2020-06-04-23-51-22.png)

* 快指令 - RRR指令
* 慢指令 - RX指令(涉及内存操作) & 复杂计算(浮点数除法)

### Dealing with slow operations

🍊 慢指令应对方法

* 访问内存
  * 缓存 - 存放近期使用的数据，包括有效地址&内容
  * 每次内存访问，CPU会先检查缓存
  * 如缓存未命中，访问内存并将该数据写进缓存
* 其他复杂计算慢指令 - CPU乱序执行 & 预测机制

### Memory protection

🍊 内存保护机制

* 用户进程内存地址 0~N-1 (`N`: 内存大小)
* CPU硬件: 段地址寄存器SA
  * 自动为用户进程计算每个有效地址
  * 内存地址 <-> 物理地址 <font color="red">映射范围SA~SA+N-1</font>
  * 每次内存访问，CPU都会检查地址是否越界，如越界产生CPU中断，并将控制权转交OS终止用户进程 - segmentation fault

### How the cache works

![](/static/2020-06-05-00-49-40.png)

* 每条指令有相应有效地址
  * 如缓存命中，快速获取数据
  * 如缓存未命中，CPU等待(相对慢)内存访问，同时拷贝进缓存

### Effect of Cache

* 每个内存地址对应缓存线
* 如数据`a`最近使用，会存储在缓存中
  * 1个CPU周期获取
  * 否则，100个时钟周期获取

![](/static/2020-06-08-19-46-01.png)

🍬 刷新-重载算法

#### Locality

* 随机位置访问无法高效利用缓存，真实情况下，不断访问相同数据
* Locality
  * 最近访问的数据
  * 地址相近最近访问数据

## Out of Order Execution

![](/static/2020-06-06-20-13-42.png)

### Superscalar Processors

![](/static/2020-06-06-20-26-07.png)

* 现代CPU并行处理(超标量架构)提高效率
* 管线模型 - pipelining
* 超标量 - superscalar
  * 当CPU处理慢指令，会同时处理其他独立指令

### ALU vs Functional Units

* 快指令 - 通过ALU算术单元1时钟周期内完成
* 慢指令 - 需要多个时钟周期
* 高性能CPU中，慢指令通过**单独功能性单元**完成
  * 内存访问/浮点数操作
  * 实现多个功能性单元并行处理

### Parallel Execution with Funcitonal Units

* 当管线操作慢指令，分配单独功能性单元进行计算
  * 乱序，并行处理独立指令
* 如指令之间有关联必须停滞管线

### Executing the Operation

![](/static/2020-06-06-20-37-21.png)

### Distributed Control & the Common Data Bus

1. 分配功能性单元指令
2. “name”标识register
3. 目标register未知结果值命名“name”
4. 当功能性单元计算结果时，给所有单元广播该结果“name=3.14”
5. 每个单元检查，如匹配，加载该结果

#### The Common Data Bus

🍊 总线

![](/static/2020-06-06-20-58-50.png)

> 指计算机组件间规范化的**交换数据（data）的方式**，即以一种通用的方式为各组件提供数据传送和控制逻辑

* 需要总线传输数据，标签tag来标识数据值
  * 抽象方面，将tag看作数据的名称
  * 实际，tag是数据的id

#### Structure of the CDB

🍊 总线结构

![](/static/2020-06-06-20-59-37.png)

* sources - 数据源，可写入CDB
* destination - 接收CDB数据
  * 操作数
* bus control - 控制访问CDB的数据源

### Issuing an Instruction

![](/static/2020-06-06-21-13-44.png)

* 操作数繁忙
  * 数据传送至FLR总线
  * 目标设置busy bit，tag设置为数据id("name")
* 目标繁忙
  * 指令送至管线模型，继续执行
  * 数据tag传送至FLR总线

### When a Functional Unit Finishes

![](/static/2020-06-06-21-19-20.png)

* 完成计算后，请求数据写入CDB
  * 多个单元可以并行写入
* id-值对传输
  * id number - tag
  * value - floating point number
* 每个单元读取CDB中数据，对比是否与广播tag相匹配
  * 如匹配，该单元从CDB中读取数据,设置`busy bit=0`

### Effect of the CDB

* 通过`name`标识各数据结果，而不是地址
* 数据一旦可用就会被传送到需要它的地方

### Speculative Execution

🍊 推测执行

> CPU动态执行技术的主要内容之一(另一个是分支预测)，指通过预测程序流来调整指令的执行，并分析程序的数据流来选择指令执行的最佳顺序。
>
> 主要目的是**为了提高CPU的运算速度**。
>
> **推测执行是依托于分枝预测基础上的**，在分枝预测程序是否分枝后所进行的处理也就是推测执行

* 超标量架构 - 预测执行机制
* 执行前，执行可能的指令
* 如不应被执行，撤销效果

#### Speculation

![](/static/2020-06-06-21-30-07.png) 

* 1&2需要访问内存. 如数据不存在缓存命中，需要从主存调用，多个CPU时钟周期
* CPU通过预测，提高效率
  * 如推测指令4jump不会执行，会继续执行后续指令5,6,7

#### Commit or Rollback

![](/static/2020-06-06-21-30-07.png) 
![](/static/2020-06-07-22-08-08.png)

* CPU可以预测执行150~200个指令
* 当指令3cmp指令所需数据取回，可以执行指令4jump。【判断预测是否正确】
  * 如果预测正确，CPU提交之前所预测执行的操作
  * 如果预测错误，撤销操作，丢弃数据&重置推测寄存器`le`

### Handling Input

![](/static/2020-06-08-19-47-54.png)

* 如进程访问其他进程内存空间，产生segmentation错误
