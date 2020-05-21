---
layout: post
title: "记录 - 如何设计&构建CPU"
date: 2020-05-18
excerpt: "底层架构记录，转载文章部分直译"
tags: [system, 2020, 计算机组成，其他，文章]
feature: https://www.gla.ac.uk/media/Media_299663_smxx.jpg
comments: true
---

[参考文章 - How CPUs are Designed (by William Gayde)](https://www.techspot.com/article/1821-how-cpus-are-designed-and-built/)

<center>部分直译注意</center>

* 目录
{:toc}

该系列涵盖计算机结构体系，处理器电路设计，超大规模集成电路VLSI，芯片制造，以及未来趋势。

1. [计算机体系结构基础 - Computer Architecture Fundamentals](https://www.techspot.com/article/1821-how-cpus-are-designed-and-built/)

   * 指令集架构 ISA
   * 缓存   caching
   * 管线   pipelines
   * 超线程&超标量 hyperthread & hyperscalar

2. [CPU设计过程 - CPU Design Process](https://www.techspot.com/article/1830-how-cpus-are-designed-and-built-part-2/)

   * 电路图 schematics
   * 晶体管 transistors
   * 逻辑门 logic gates
   * 时钟   clocking

3. [芯片的设计与实现 Laying Out & Physically Building the Chip](https://www.techspot.com/article/1840-how-cpus-are-designed-and-built-part-3/)

   * 超大规模集成电路   VLSI
   * Si制造 silicon fabrication

4. [当前计算机体系结构的发展趋势&未来热点 - Current Trends & Future Hot Topics in Computer](https://www.techspot.com/article/1853-how-cpus-are-designed-the-future/)

   * 加速器
   * 3D集成
   * fpga
   * 进内存计算 near memory computing

## Computer Architecture Fundamentals

🍊 计算机结构体系基础

### 计算机如何识别指令？How Machine Recoginize the Instructions

> 首先，我们需要一个 CPU 功能的基本定义。 **最简单的解释是，CPU 遵循一组指令，对一组输入执行某些操作。** (例如，可以是从内存中读取一个值，然后将其添加到另一个值，最后将结果存储回内存中的不同位置。 也可以是更复杂的东西，比如除以两个数，如果前面的计算结果大于零)
>
> 当想运行os(应用程序)或游戏时，该**程序本身就是CPU进行执行的一组指令集**.(这些指令从内存中加载，在简单处理器上，按个执行，直到程序完成)
>
> <font color='red'>当用高级语言编写程序时，CPU无法直接执行。机器只能执行1,0</font>因此需要方法来表示这些代码(高级语言->(低级语言)->汇编语言->机器语言)

---

🍬 高级语言(程序) -> 机器语言

![](https://static.techspot.com/articles-info/1821/images/2019-04-21-image-10.png)

> 程序被编译成一套称为汇编语言的低级指令，作为指令集架构的一部分。 **指令集架构ISA是一个CPU的构建&指令执行基础**
>
> 最常见的ISA: x86、 MIPS、 ARM、 RISC-V 和 PowerPC. <font color='blue'>每个 ISA 有不同的语法/指令</font>

🍬 ISA分类

> ISA可分为2类: 固定长度 & 可变长度.
>
> RISC-V ISA 【使用固定长度指令集，这意味着每条指令中一些的预定义位决定了指令类型】
>
> x86 ISA 【可变长度指令集，指令可以用不同的方式进行编码，不同部分的位数也指令类型不同。 因此<font color="purple">x86 cpu 的指令解码器通常是整个设计中最复杂的</font>】

* 固定长度ISA指令解码更容易，但支持的指令数量有限制。
* 常见RISC-V ISA有大约100个指令 & 开源. x86专有，通常视为有几千条指令
  * <font color="purple">虽然不同ISA之间存在差异，但核心功能相同</font>

![RISC-V](https://static.techspot.com/articles-info/1821/images/2019-04-22-image-2.png)

* RISC-V指令例子。右边7bit为操作码，决定指令类型。
* 每条指令包括
  * 使用的寄存器
  * 执行的函数
* 汇编指令被分解成二进制机器码形式以供CPU理解

### 指令执行过程 The Execution Process of Instructions

1. 将指令从内存中**载入CPU**开始执行
2. **指令解码**，以便CPU确定指令类型

   * 算术指令
   * 分支指令
   * 内存指令
   * 一旦CPU确定指令类型，获取操作数(寄存器,内存)
   * 大部分CPU是64bit，意味每个数据大小为64bit

   ![](https://static.techspot.com/articles-info/1821/images/2019-04-21-image-2.png)
   
   * 64bit: CPU寄存器，数据路径，内存地址的宽度。<font color="purple">一台计算机一次可处理的信息量，相比32bit，64bit架构一次可处理2倍的信息量</font>

3. 得到指令操作数后，对输入数据进行操作

   * 计算完毕后，可能需要访问内存，或将结果存储至内部寄存器中
   * 存储完毕后，CPU更新各个组件元素状态，执行下一条指令

---

🍙 以上描述仅为简化，现代大多CPU将这几个阶段划分更多个阶段以提高效率

* 意味着CPU将在每个周期完成几个指令，<font color="red">但任何一条指令从开始到结束可能需要更多周期</font>
* 这种模型称为指令管线(pipeline,或流水线技术)，因为需要一定时间来填充管线。一旦管线填充满，得到恒定输出。
* <font color="purple">并非所有指令可以同时完成</font>
* 现代CPU通常选择执行给定时间内最优先执行的指令【<font color="blue">乱序执行</font>,如超标量必须采用乱序执行来增加并行度】，并缓冲其他指令<font color="red">如果当前指令未就绪，CPU跳转至其他位置检查就绪指令</font>

![4-stage pipeline](https://static.techspot.com/articles-info/1821/images/2019-04-21-image-4.png)
![4-stage-pipeline](/static/2020-05-18-01-47-09.png)

* 分支预测器(指令管线化，提高效率)，4层管线架构.彩色方格代表<font color="blue">互相依赖</font>的指令

![](/static/2020-05-18-01-45-31.png)

* [参考](https://www.cnblogs.com/CorePower/p/CorePower.html)
* 计算机中的流水线是把一个重复的过程分解为若干个子过程，每个子过程与其他子过程并行进行。由于这种工作方式与工厂中的生产流水线十分相似， 因此称为流水线技术
* 本质：时间并行技术

---

#### SMT/Hyper-Threading

🍊 超标量CPU架构

> 在一个CPU内核中实现了，**指令集并行运算**【在相同CPU主频下实现更高吞吐量】
>
> 现代CPU采用乱序执行 & **超标量体系结构【CPU在管线模型每个阶段一次执行多个指令(通过多个管线副本实现)，如多个就绪指令之间无依赖，则同时执行】**.
>
> 超标量架构的常见实现为 - 超线程 Simultaneous Muitithreading(SMT). 即Hyper-Threading
>
> 超标量架构，实现一定CPU同时执行多个指令. 充分利用一定CPU中的执行部件.
>
> 超线程/同时多线程(SMT), 超标量基础上，CPU并行处理独立不同线程的指令. 【线程级并行(TLP), 需要显式多线程程序】，**即多个管线模型**

![](https://static.techspot.com/articles-info/1821/images/2019-04-21-image-7.png)

#### CPU Components: Caches & Branch Predictor

🍊 缓存 & 分支预测器

* 为了实现吞吐量的提高，CPU还具有其他组件,模块. 每个模块用于特定用途. 其中作用最大的是 **缓存 & 分支预测器**
  * 其他结构: 重新排序缓冲区，寄存器别名表，保留站本文章不予概括

---

##### Cache Hierarchy

🍊 Cache

> 缓存如RAM & SSD等存储结构. 特点在于<font color="blue">访问延迟 & 访问速度</font>
>
> <font color="purple"></font>RAM读写很快，但CPU速度远远大于RAM
>
> 如数据不在RAM中，则访问SSD中的数据可能需要数万个周期. 如CPU不具有缓存，将慢慢停止工作

🍬 CPU缓存结构

> CPU通常有<font color="purple">3层缓存</font>，即内存(缓存)分层结构.

* L1缓存 - 最小最快
* L2缓存 - 中间
* L3缓存 - 最大最慢

> 3层缓存结构之上为一些<font color="purple">小寄存器，用于计算过程中存储单个数据值</font>
>
> <font color="red">寄存器在存储结构中速度是最快的</font>
>
> 当编译器将 高级语言->汇编语言时，会决定如何最大利用寄存器
>
> 典型CPU上，每个核心都有两个L1缓存.
>
> * 一个用于<font color="blue">指令</font>
> * 一个用于<font color="blue">数据</font>
> * 当CPU执行代码时，最常使用的指令 & 数据都被缓存
  * 大大加快执行速度

🍬 CPU访问相关：缓存命中 & 何时访问内存

![](https://img-blog.csdn.net/20160730162655871)
![](https://static.techspot.com/articles-info/1821/images/2019-04-21-image-8.png)

> 当CPU从内存请求数据时，<font color="blue">先检查L1缓存. 如命中，数据可以在几个周期内快速访问. 如不存在, CPU检查L2缓存 & L3缓存</font>
>
> <font color="red">如未在缓存分层结构中找到数据，则访问内存(Memory)</font>

* CPU <-> Register -> (Cache) ->Memory

---

##### Branch Predictor

🍊 分支预测器

> 类似条件语句. 如为T，则执行某路指令. 反之，执行其他指令.
>
> 为了解决CPU同时执行多条指令，判断指令是否为分支 & 条件真假 & 是否正确执行的问题，现代高性能处理器都采用<font color="purple">“推测(speculation)”技术</font>

* CPU跟踪每个分支指令，并推测某分支是否执行
  * 如分支采用(预测正确)，继续执行后续指令
  * 如分支不采用(预测错误)，停止执行，<font color="blue">删除所有已经开始执行的不正确指令，并从正确条件开始</font>

🍬 分支预测器是DL最早形式之一，会随着分支的进展学习分支行为

* <font color="red">这种预测技术提供利益的同时，暴露了安全漏洞.</font>
  * 著名的Spectre攻击，利用了其中漏洞
  * 使用特殊代码，让CPU执行会泄露内存值的代码
* 该技术可保证安全性的同时，需要牺牲性能效率

## CPU Design Process

🍊 内容(本章可参考1S L20)

* 电路图 schematics
* 晶体管 transistors
* 逻辑门 logic gates
* 时钟   clocking

### FET（NMOS,PMOS）

🍬 FET - 场效应管

* 场效应管（FET）是利用控制输入回路的电场效应来控制输出回路电流的一种半导体器件
* 分类: 结型JFET，绝缘型MOSFET

> CPU & 其他大多数字技术都有晶管体应用
>
> 现代处理器主要使用的晶管体主要有2种:
>
> * PMOS
> * NMOS

* NMOS(N沟道绝缘型场效应管)栅极加+压时，导通
* PMOS(P沟道绝缘型场效应管)栅极加-压时，导通

> 通过<font color="purple">互补的方式组合这两种晶体管，可组成CMOS逻辑门</font>

### Logic Gate

🍊 任何复杂CPU都可以分解成**逻辑门，存储元件(时序电路，SRAM/DRAM)，晶体管**

🍊 逻辑门

> 接收input, 输出output的简单设备
>
> And2 - 与门
> Inv2 - 反相器
> Nand - 与非门
> OR2 - 或门
> NOR2 - 或非门
> XOR2 - 异或门
> XNOR2 - 异或非门/同或门

![](https://static.techspot.com/articles-info/1830/images/2019-05-10-image.png)

> 反相器inv2 & 与非门 Nand2
>
> inv2
>
> * PMOS连电源VDD
> * NMOS接地GND
> * OUT信号与IN信号相反
>
> Nand2
>
> * 至少一个input为F/-

* 组合电子元件的设计过程 = 芯片的制造
  * 唯一区别: 现代芯片上有数十亿个晶体管

---

🍊 EXAMPLE - 全加器 full adder
![](https://static.techspot.com/articles-info/1830/images/2019-05-10-image-2.png)

> 1位全加器:
>
> * input: a, b, carry-in
> * output: sum, carry-out
> * 基本设计涉及5个逻辑门，组合可得到任意大小加法器

---

### Sequential Logic: Storage

🍬 组合一系列的逻辑门，针对input实现某功能称为**组合逻辑电路**

* 然而基本计算机需要能够存储数据的**时序逻辑电路(sequential logic)**
* <font color="red">数字电路根据逻辑功能不同，分为：组合(逻辑)电路 & 时序(逻辑)电路</font>
  * 组合电路: 任意时刻input取决于该时刻input，与原状态无关
  * 时序电路：任意时刻input取决于该时刻input & 之前状态input

🍙 时序逻辑电路

* 连接反相器 & 其他逻辑门，而时序电路是一种输出<font color="purple">不仅与当前的输入有关，而且与其输出状态的原始状态有关，其相当于在组合逻辑的输入端加上了一个反馈输入</font>，在其电路中有一个存储电路，其可以将输出的状态保持住
* 反馈电路用于存储1bit数据(先前状态) - 静态RAM(SRAM)
  * 与动态RAM(DRAM)相对
  * 存储的数据总是直接连接到VDD/GND

#### SRAM (Static) & DRAM (Dynamic)

> 静态RAM，所谓的“静态”，是指这种存储器只要保持通电，里面储存的数据就可以恒常保持。
>
> 相对之下，动态随机存取存储器（DRAM）里面所储存的数据就需要周期性地更新(**否则内部的数据即会消失**)。
>
> 然而，当电力供应停止时，SRAM储存的数据还是会消失（被称为volatile memory），这与在断电后还能储存资料的ROM或闪存是不同的。

![](https://static.techspot.com/articles-info/1830/images/2019-05-10-image-4.png)

* 实现1bitSRAM: 使用**6个晶体管**
* TOP信号 - Word Line( WL)
  * 存储add, 当启用该数据送至Bit Line(BL)
* Bit Line Bar(BLB) - BL输出
  * 与BL值相反
* M1 & M3 - 反相器
* M2 & M4 - 反相器

🍙 SRAM这样的**时序电路**用来构建<font color="blue">CPU高速缓存(主要是二级)&寄存器</font>

* SRAM非常稳定，但每1bit数据都需要6~8个晶体管用来存储。相对DRAM，<font color="red">SRAM成本更高</font>

![](https://static.techspot.com/articles-info/1830/images/2019-05-10-image-5.png)

🍙 DRAM将数据存储在一个小型电容器中，不涉及逻辑门的使用

* 动态，指电容器的电压会动态变化，因为没有连接VDD/GND
* <font color="blue">DRAM只涉及1个晶体管，用于访问存储在电容器中的数据</font>【因此，可以密集地设计，集成度很好，成本低】
* 因为电容器的电荷太小，所以需要周期性刷新数据，否则存储的数据会消失

🍬 <font color="red">SRAM不像DRAM一样需要不断刷新，而且工作速度较快，但由于存储单元器件较多，集成度不太高，功耗也较大</font>

### Clock: Synchronization

🍊 清楚CPU的某些组件如何构建(cache&registers->SRAM,.etc)之后, <font color="blue">需要清楚如何连接所有组件 & 同步</font>

> CPU关键部件都连接至一个时钟信号. 实现同步，确保所有数据在正确时刻到达，确保不会出现glitches现象(input某时刻不稳定)
>
> * 该时钟信号，在一个**提前规定的频率区间**内，高低频切换
> * 低频->高频，执行计算

🍬 超频问题

* 将CPU时钟频率提高(如加压，可以加快晶体管切换速度)，每秒有更多周期，CPU性能提升
* 但是有阈值，最大处理频率。<font color="red">如时钟超过一定的频率，晶体管就不能足够快地切换，会产生故障，glitches/hazard(会导致电路损坏)现象</font>

🍬 功率问题 - 开关

> 现代CPU时钟占约30%~40%，为了节约能源，大多低功耗设计会在芯片设计时关闭部分不使用的组件

* 通过**时钟门控，电源门控**关闭

## Laying Out & Physically Building the Chip

🍊 芯片设计&实现

> CPU&其他数字电路(时序&组合)都由晶体管构成
>
> * 晶体管是电子可控开关，通过加压控制
>
> CPU内晶体管基本为Si，因为Si是很好的半导体素材

🍊 掺杂工艺

* 将精选杂质添加到Si衬底上，以改变导电性(改变电子行为，以控制)
* 有两种主要掺杂类型

> 如果精确控制一定数量的电子供体元素，可以形成N型区域。由于电子过多，转化为负电荷。即N型沟道。反之Si中添加一定数量电子受体元素，形成带正电的P型沟道
>
> 将杂质添加至Si的工艺过程称为【离子注入&扩散】

### MOSFET

🍊 绝缘型场效应管
![](https://static.techspot.com/articles-info/1840/images/2019-05-04-image-2.png)

> 通过结合多个沟道特性来制造晶体管。集成电路中使用的晶体管称为MOSFET(Metal-Oxide-Semiconductor Field Effect Transistors)
>
> 有4个连接。N沟道MOSFET，电流:源->漏，P沟道MOSFET电流：源<-漏
>
> **栅极Gate是用于控制晶体管的开关**
