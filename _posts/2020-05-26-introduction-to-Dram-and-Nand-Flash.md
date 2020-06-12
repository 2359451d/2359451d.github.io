---
layout: post
title: "记录 - DRAM & NAND Flash区别&定义介绍"
date: 2020-05-18
excerpt: "底层架构记录（RAM,ROM,FLash,SSD,DMA,接口协议总线等），转载文章部分直译"
tags: [system, 2020, 计算机组成，其他，文章]
feature: https://www.gla.ac.uk/media/Media_299663_smxx.jpg
comments: true
---

[参考文章 - A clear introduction to definition and difference between DRAM and NAND Flash(1)](https://www.elinfor.com/knowledge/a-clear-introduction-to-definition-and-difference-between-dram-and-nand-flash-1-p-11098)

* [2](https://www.elinfor.com/knowledge/a-clear-introduction-to-definition-and-difference-between-dram-and-nand-flash-2-p-11099)
* [3](https://www.elinfor.com/knowledge/a-clear-introduction-to-definition-and-difference-between-dram-and-nand-flash-end-p-11100)

<center>部分直译注意</center>

## Interface & Bus & Protocol

[参考文章 - FlashvsSSD](http://www.seotest.cn/yingjian/20001.html)

🍊 接口 - interface

![](http://www.seotest.cn/d/file/news/20190330/2018051614122170.jpg)

> 把几条导电的铜线做成不同形状的插头, 主要有性能&外观差别
>
> 主流SSD接口有
>
> * SATA
> * mSATA
> * m.2
> * PCI-E

🍊 总线 - bus

![](http://www.seotest.cn/d/file/news/20190330/2018051614122171.jpg)

> 是我们看不到的东西，可以理解为**数据传输的“公路”**.
>
> 有**sata总线和PCI-e总线**两种

* PCI-E总线
  * 分等级，Xlevel，数字越大，速度越快
  * <font color="blue">目前SSD都用X2，X4等级</font>，显卡为X16级别

🍊 协议 - interface

![](http://www.seotest.cn/d/file/news/20190330/2018051614122173.jpg)
![](/static/2020-05-27-23-13-20.png)

---

![](http://www.seotest.cn/d/file/news/20190330/2018051614122174.jpg)

### SATA Interface

![](http://www.seotest.cn/d/file/news/20190330/2018051614122175.jpg)

> SATA接口属于**老式**的接口，分SATA 3GB和SATA6 GB，
>
> **HDD**使用的是这种接口，**兼容性强，新老电脑基本都通用**
>
> **SSD**用这种接口速度稍**慢，延迟稍高**，最大速度不会超过600MB/s

### mSATA Interface

![](http://www.seotest.cn/d/file/news/20190330/2018051614122176.jpg)

> 应用于早期笔记本. 现在大多用M.2接口，mSATA接口被淘汰

### M.2 Interface

![](http://www.seotest.cn/d/file/news/20190330/2018051614122177.jpg)

🍬 M.2主板插槽类型

* M key插槽 - 新式M.2插槽
  * 又称“SOCKET 3”,<font color="purple">近几年新主板上用到</font>
    * <font color="blue">目前主流新主板都配M.2接口, 总线走PCI-EX4高速</font>
    * <font color="blue">速度轻松达到1500MB/S, 如支持NVMe协议速度轻松达到2000MB/S</font>
* B key插槽 - 老式M.2插槽
  * 又称“SOCKET 2”,<font color="purple">用于老式主板&笔记本</font>
    * <font color="blue">目前主流新主板都配M.2接口, 总线走PCI-EX4高速</font>
  * <font color="blue">速度慢</font>，1000MB/S

🍬 M.2接口类型

* **M型接口**的SSD<font color="blue">性能较好，价格普遍比SATA接口SSD贵，并且需要主板配备M.2接口</font>
* **B&M接口**的SSD<font color="blue">兼容性好，两种M.2接口插槽都能用</font>

### PCI-E Interface

![](http://www.seotest.cn/d/file/news/20190330/2018051614122178.jpg)

* 如图, SSD为PCI-EX4接口，支持PCI-EX4总线
  * 但是现在大多主板没有PCI-EX4插槽，<font color="blue">所以一般接在显卡的插槽里使用</font>
  * 这种接口SSD速度很快，但很多平台不支持，所以不是很常见

---

## Memory

![](https://pic1.zhimg.com/80/8bf6ef80ddbcd9ed5984fe9a51bb5b78_720w.jpg)

> 内存用于存储程序 & 数据。根据用途可分为 【**主存 & 辅存**】
>
> 通常使用DRAM，然而NAND Flash类似硬盘.

* 主存 & 辅存最主要的区别：断电后是否易失
  * ROM定义上算内存，只能读取(如不可擦PROM，存储BIOS程序)，手机ROM严格意义上算闪存(外/辅存)
  * <font color="red">所以默认定义上ROM看作不可写入的，所以将ROM归类为内存分类</font>
* <font color="purple">CPU直接访问硬盘耗时长，所以需要内存作为中介</font>
  * 硬盘数据复制进内存
  * CPU取缓存/内存数据进行计算
* <font color="purple">CPU与内存也为独立芯片，所以需要缓存来匹配速度</font>

### Computing Speed

![](https://www.elinfor.com/article/M/e/Memory%20hierarchy.JPG)

* 速度越快，容量越小，成本越高

### Volatile & Non-volatile Memory

![](/static/2020-05-27-00-05-10.png)

* Volatile Memory(VM)
  * 加电数据存在，断电易失
  * SRAM,DRAM,SDRAM,DDR-SDRAM
* NON-Volatile Memory(NVM)
  * 断电不易失
  * ROM,PROM,EPROM,EEPROM,FLASH ROM,.etc

### Memory Hierarchy

![](https://www.elinfor.com/article/M/e/Memory%20hierarchy.JPG)

> 理解不同种类存储器之间速读写速度，成本，容量等差异。以达到最大利用。

* 寄存器 - register
  * CPU处理器内部内存，临时存储&计算数据
* 缓存 - cache
  * CPU处理器内存内存，暂时存储程序&数据
  * <font color="blue">一般为SRAM(读写更快)</font>
* 主存 - main memory
  * 处理器外部内存，临时存储程序&数据
  * <font color="blue">一般为DRAM(读写更快), 但以改进为SDRAM/DDR</font>
* 辅存 - assistant memory
  * 永久存储程序&数据
  * Flash ROM(闪存)，disk drive，CD-ROM，tape drive, .etc

### RAM

🍊 主要种类

* SRAM - 静态RAM
  * 通常6个晶体管组成，存储1bit信息
  * 读写速度快，成本高，通常作为CPUhuancun
* DRAM - 动态RAM
  * 1个晶体管&1个电容组成，存储1bit数据
  * 需要周期性更新以保存数据
  * 读写速度慢，成本低，通常作为主存使用
  * <font color="blue">现代主流CPU采用改进的SDRAM & DDR-SDRAM</font>
* SDRAM(Synchronous) - 同步动态RAM
  * 主板上CPU & SDRAM访问数据时有相同时钟【同步】
  * SDRAM比DRAM访问更快，<font color="red">在DRAM基础上，在输入输出接口进行时钟同步，以提高读写效率</font>

---

#### Memory Module

🍊 内存条/内存模组

![](https://www.elinfor.com/article/S/R/SRAM%20and%20DRAM.png)

> 该**内存条(memory module,或称内存模组**)有8个DRAM芯片.

* 一般由一块小电路板 & 若干个DRAM芯片构成.

#### DRAM

![](/static/2020-05-29-22-48-49.png)
![](/static/2020-05-30-21-43-00.png)

> DRAM芯片内部核心结构: **存储阵列(memory array**) - 由若干行(row lines)&若干列(bit lines)构成

* 外部给行地址 & 列地址，可以选中一个**存储单元(memory cell)**
  * 一个存储单元存放若干个bit，常见 4bit & 8bit
    * 其他参考后续memory cell部分: SLC, MLC, TLC,.etc

🍬 每个(基本)**存储单元**的电路结构 - structure of DRAM cell

![](/static/2020-05-30-21-56-41.png)
![](/static/2020-05-30-22-00-10.png)

* 通过电容保存1bit数据，<font color="red">存在漏电效应(电荷流失): 导致数据丢失</font>
  * 需要周期性刷新所有单元，原数据`1/0`，刷新后保持原来数据
* 根据 行、列组合信号，<font color="red">选中对应的存储单元</font>
* 写入操作
  * <font color="blue">根据I/O数据线驱动，对电容进行充电/放电操作</font>，从而完成写入1/0
* 读取操作
  * <font color="blue">电容对外部I/O数据线进行驱动</font>，从而完成读取1/0

🍬 DRAM特点 & 用途 - 作为主存

![](/static/2020-05-30-22-13-56.png)

* (S)DRAM
  * <font color="red">在DRAM基础上，在输入输出接口进行时钟同步，以提高读写效率</font>
* DDR
  * <font color="red">在SDRAM基础上改进</font>
  * DDR1,DDR2,DDR3
* <font color="purple">优点</font>
  * 集成度高
  * 功耗低
  * 价格低
* <font color="purple">缺点</font>
  * 定时刷新
  * 速度慢(<font color="red">由电容器周期性刷新，充放电造成</font>)

---

![](/static/2020-06-01-22-12-02.png)
![](https://www.elinfor.com/article/C/a/Capacitor.jpg)

* 当MOS不导通，电容器不充电，数据为0
* 当MOS导通，电容器充电，数据为1

🍬 SRAM与DRAM不一样，不需要分成行地址 & 列地址分别选择

* <font color="red">SRAM相对灵活，一个地址所对应的存储单元数量也许是 8/10/32/40/64 bit</font>
  * <font color="purple">DRAM地址所对应选中的基本存储单元数常见为 4/8bit</font>

##### SDRAM

🍊 同步动态随机存取内存

> 一个有**同步接口**的DRAM. 在响应控制输入前会等待一个时钟信号，以与总线同步

* <font color="red">在DRAM基础上，在输入输出接口进行时钟同步，以提高读写效率</font>

##### DDR-SDRAM

🍊 双倍速率SDRAM

> **有双倍数据传输率的SDRAM**，其数据传输速度为系统时钟频率的两倍，由于速度增加，其传输性能优于传统的SDRAM

* 1个时钟周期内传输2次数据

##### FRAM

🍊 铁电存储器 - ferroelectric RAM

> 基于DRAM的快速读写能力，**结合断电后不易失的能力**

🍬 比闪存更快，但不能像SRAM&DRAM一样密集

* 小范围应用手机，功率表等

#### SRAM

🍊 静态RAM - 基本存储单元(memory cell)

![](/static/2020-05-31-22-19-24.png)

* 由6个晶体管组成(相比之下DRAM由1个晶体管&1个电容器组成)
  * 组合才能保存1bit数据
* <font color="blue">3个input信号</font>
  * `BL` - 传送读写数据
  * `BLbar` - 传送读写数据
  * `WL` - 控制外部是否可以访问该**基本存储单元(cell)**
* GND - 0
* VDD - 1

🍬 SRAM写入1过程实例

![](/static/2020-05-31-22-47-35.png)

* <font color="red">只要SRAM处于通电状态，GND,VDD信号保持稳定</font>
* <font color="blue">写入1，BL=1，BLbar=0</font>

![](/static/2020-06-01-20-45-50.png)

* 晶体管工作速度很快，SRAM写入过程很快
  * <font color="purple">当所有晶体管稳定，可以结束这次写入</font>
  * `WL： 1->0`，使M5 & M6晶体管关闭，不断电情况下，M1,M2,M3,M4仍保存元原数据

---

🍬 SRAM读出过程实例

![](/static/2020-06-01-21-01-45.png)

* `WL=1`，使M5,M6连通
* `BL` & `BLbar`信号不通过外部驱动，<font color="red">通过M5&M6被SRAM存储单元驱动</font>
  * `BL=1` & `BLbar=0` -> 可判断出存储的数据为1

---

🍊 SRAM特点

* 优点
  * 速度较快(相对于DRAM的电容充放电速度)
* 缺点
  * 集成度低(需要6个晶体管存储1bit，造成芯片面积大)
  * 功耗较高()
  * 价格较高

🍙 SRAM - 实现现代CPU高速缓存

![](/static/2020-06-01-21-56-06.png)

##### Storage Matrix

🍙 存储矩阵 - storage matrix
![](/static/2020-06-01-21-33-51.png)

> 由多个基本存储单元电路构成。
>
> 可以从外部输入**多个地址线 & 地址译码驱动电路**，从而根据地址信号线地值选中**存储矩阵中若干个SRAM基本存储单元**

🍬 SRAM与DRAM不一样，不需要分成行地址 & 列地址分别选择

* <font color="red">SRAM相对灵活，一个地址所对应的存储单元数量也许是 8/10/32/40/64 bit</font>
  * <font color="purple">DRAM地址所对应选中的基本存储单元数常见为 4/8bit</font>

🍬 <font color="red">通过地址线选中一组存储单元后，为了确定读or写操作</font>

![](/static/2020-06-01-21-43-45.png)

* 外部需要有 读写控制信号 & 读写控制电路
  * 读写控制电路会将外部数据线与SRAM基本存储单元相连
* <font color="blue">写 - 外部数据线信号驱动内部存储单元</font>
* <font color="blue">读 - 外部数据线信号驱动内部存储单元</font>

🍙 片选线 - 片选信号

![](/static/2020-06-01-21-48-35.png)

* 表明当前是否需要对SRAM存储器进行 读/写

#### DRAM vs SRAM

![](/static/2020-06-01-22-03-57.png)

### ROM

![](http://file2.dzsc.com/data/18/05/18/172957169.jpg)
![](/static/2020-05-26-20-39-44.png)

🍊 主要种类

* ROM - 掩膜工艺ROM
  * 一旦由<font color="red">厂家生产出品</font通过地址线选中一组存储单元后，为了确定读or写操作>，不可修改
* PROM - 可一次性编程ROM
  * PROM<font color="red">用户可以写入信息</font>，一旦写入之后，信息就会永久的固定下来，只可读出，不可再改变其内容
* EPROM - 紫外线擦除可改写ROM
  * 可由用户写入，<font color="purple">允许反复擦除重新写入，需从主板中取出修改</font>
* <font color="blue">EE(P)ROM - 电擦除可改写ROM</font>
  *  可以在电脑上或专用设备上擦除已有信息，重新编程。一般用在即插即用
  *  <font color="red">不需从计算机中取出即可修改</font>
  *  <font color="purple">特殊形式的闪存</font>
* Flash ROM - 快闪ROM（即<font color="red">闪存</font>）
  * <font color="red">固态内存</font>
  * <font color="blue">比EEPROM读写更快，结合了RAM&ROM优点</font>
  * 相比SRAM, <font color="red">非易失，成本低</font>
  * <font color="blue">主要结构: NOR & NAND</font>
    * 大多数情况下闪存只是用来存储少量的代码，**这时 NOR 闪存更适合一些。而 NAND 则是高资料存储密度的理想解决方案**。

#### Flash ROM

🍊 闪存 - Flash ROM

> 非易失性内存(可算辅存，但严格来说是固态内存)，比EEPROM(电擦除可改写ROM)读写更快，是其改进
>
> **作为存储介质，是一种存储技术**

🍬 与RAM相比

* 读写比RAM慢，但不易失
* <font color="blue">不像RAM一样需要以字节为单位改写数据</font>

🍙 闪存技术应用

* 闪存卡
  * SM卡，CF卡，SD卡等
  * <font color="red">这些闪存卡虽然外观、规格不同，但是技术原理都是相同的</font>
* 闪存盘
* SSD固态硬盘

##### NOR/NAND Flash

🍬 大多数情况下闪存只是用来存储少量的代码，**这时 NOR 闪存更适合一些。而 NAND 则是高资料存储密度的理想解决方案**

* NOR型更像内存，价格贵，容量小
  * <font color="blue">读快，写慢，适用于写一次可以读很多代码的场景</font>
  * 通常用于存储程序代码(如OS代码，重要代码)，并直接在flash内运行
* NAND型更像硬盘，成本低，容量大
  * <font color="blue">最常用的闪存应用产品，如闪存盘等都为NAND型</font>
  * <font color="purple">NAND型类似硬盘的操作方式，写速快</font>

🍊 NAND闪存
![](/static/2020-05-26-20-06-22.png)

#### SSD (Solid State Disk)

🍊 固态硬盘(固态驱动器)

> 用固态电子存储芯片阵列制成的硬盘.
>
> 由**控制单元**&**存储单元(FLASH闪存/DRAM芯片)**组成

🍙 不一定必须使用闪存作为存储介质，因此命名为“固态驱动器”

🍬 基于闪存

* <font color="blue">采用FLASH芯片作为存储介质，通常的SSD</font>
  * 笔记本硬盘，存储卡，U盘等样式
  * <font color="blue">可移动，不受电源控制，适用各种环境</font>
* 固态硬盘内主体其实就是一块PCB板，<font color="red">而这块PCB板上最基本的配件就是控制芯片，缓存芯片（部分低端硬盘无缓存芯片）和用于存储数据的闪存芯片</font>

🍬 SSD与机械硬盘HDD对比

![](/static/2020-05-27-22-05-13.png)
![](/static/2020-05-27-22-05-38.png)

##### Memory Cell (SSD: Flash Cell)

🍊 内存单元

[参考 - flash&SSD区别](http://www.seotest.cn/yingjian/20001.html)

> 指最小的数据访问结构.
>
> * 如1内存单元由1个MOSFET & 电容组成 - 【1T1C】
> * 如1内存单元由1个MOSFET & 电阻组成 - 【1T1R】
> * 如1内存单元由1个diode(二极管) & 电阻组成 - 【1D1R】

![](http://www.seotest.cn/d/file/news/20190330/2018051614122167.jpg)

* 每个内存单元不一定只存储1bit数据，根据存储的data bits数量，可分为:
  * **SLC - single-level cell**
    * 每个存储单元只存储1bit的数据，这种存储方式稳定性强，读写速度很快，而且不会出错，并且寿命长，因此价格也是最贵的
  * **MLC - multi-level cell**
    * 一般我们说的MLC就是指两个，所以，MLC的每个存储单元要放2bit的数据。
  * **TLC - three-level cell**
    * TLC颗粒的每个存储单元要挤3bit的数据。(由于MLC是多个bit的意思，多个包含3个，所以，有些厂商，如三星，会把自家使用TLC颗粒的EVO系列固态硬盘称为“3bit MLC”）
    * 价格亲民，最常见
  * QLC - quad-level cell
  * <font color="red">以上【闪存颗粒】是SSD用来存储数据的东西，挑选固态最重要的参数</font>

🍬 SLC,MLC,TLC这三种颗粒在速度&使用寿命上差距很大

* <font color="purple">TLC颗粒虽然最差，寿命长，性价比高</font>

🍙 闪存颗粒品质比较

![](http://www.seotest.cn/d/file/news/20190330/2018051614122168.jpg)

* “白片” & “黑片”SSD价格通常较低，质量差

### Direct Memory Access

🍊 DMA - 直接存储器访问

![](https://img-blog.csdnimg.cn/20190826131837459.png)

> 主要功能是可以把数据从一个地方搬到另外一个地方(**内存<->I/O等外设**)，而且不占用CPU。不用CPU暂存数据
> 