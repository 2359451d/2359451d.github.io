---
layout: post
title: "记录 - 加密传输，隐私"
date: 2020-05-11
excerpt: "复习nose lecture - security & privacy(cryptography,MD5,symmetric & asymmetric&hybrid 对称&非对称&混合加密，数字签名，HTTPS,(D)TLS,SSL，SSH，密钥托管)"
tags: [学习笔记, network, 2020, 网络]
feature: https://techgenix.com/tgwordpress/wp-content/uploads/2020/01/Azure-Linux-VM-boot-errors-FINAL-Shutterstock-350x233.jpg
comments: true
---

上学期内容，之前笔记不知道写哪去了，**再  放  送**

* 目录
{:toc}

## Privacy and Security

### Network Monitoring & Need for Encryption

![](/static/2020-05-11-21-03-17.png)

* 通信中产生的流量拦截 - “Pervasive Monitoring is an Attack”
* 某些组织可能需要监听流量
  * 网络通信维护 & 故障排除
* 恶意用户的流量监听 - 用户&数据窃取&主动攻击
  * PWLAN公共无线局域网的监听
  * 路由器被黑，可监听主干路连接

#### Confidentiality: Common Cryptography

🍊 机密性

![](/static/2020-05-11-21-48-52.png)

* 通过**数据加密**获取

[参考 - 常见3种加密](https://www.cnblogs.com/shoshana-kong/p/10934550.html)
🍬 3种基本数据加密方式

* **对称加密** symmetric cryptography (单密钥加密)
  * 经典加密算法： AES（Advanced EncryPtion Standard）**高级加密标准** <font color="red">。是现在公认的最安全的加密方式，是对称密钥加密中最流行的算法。</font>
  * DES (Data Encryption Standard): 数据加密标准
* **非对称加密** public key cryptography (RSA，公钥加密)
  * 密钥算法 diffie-hellman algorithm
  * 公钥加密算法Rivest-Shamir-Adleman algorithm
  * 椭圆曲线加密算法 Elliptic curve-based algorithms
* **MD5加密**
  * 哈希算法

## MD5

### Extension: Cryptographic Hash Func

🍊 密码**散列函数**(或，加密散列函数)

> 这种散列函数的输入数据，**通常被称为消息（message），而它的输出结果，经常被称为消息摘要（message digest）或摘要（digest）**
>
> 应用于数字签名，消息认证码
>
> 具有： 单向性(对于两个不同的消息，它不能给与相同的散列数值)，不可预见(消息摘要看起来和原始的数据没有任何的关系)

🍬 散列碰撞

* <font color="blue">如果两个散列值相同，两个输入值很可能是相同的，但也可能不同</font>，这种情况称为“散列碰撞（collision）”
* 这通常是两个不同长度的输入值，<font color="purple">刻意计算出相同的输出值，然而具有强混淆特性的散列函数会产生完全不同的散列值</font>

---

🍊 MD5

* **密码散列函数**，可以产生一个 <font color="red">128bits</font>的**哈希序列(散列值，即信息摘要<签名>)**

🍊 目的

* 用于确保信息传输完整一致

🍬 特点

* **不可逆**
  * 无法通过信息摘要，还原出原始信息
* **单向性**
  * 可以通过原始信息计算得出信息摘要
* **恒定性**
  * 原始消息恒定，输出的信息摘要也恒定
* **不可预测性**
  * 轻微改变都会造成不同信息摘要

![](/static/2020-05-12-15-28-29.png)

> 该算法存在弱点(如无法防止碰撞)，一般建议改用其他算法(如SHA-2)

### Principle: Calculation Steps

![](/static/2020-05-12-13-48-31.png)
![](/static/2020-05-12-14-18-02.png)

---

![](/static/2020-05-12-14-00-09.png)
![](/static/2020-05-12-14-03-31.png)
![](/static/2020-05-12-14-05-42.png)
![](/static/2020-05-12-14-08-05.png)
![](/static/2020-05-12-14-08-15.png)

---

[参考 - 简单了解MD5算法的原理](https://www.jiamisoft.com/blog/24003-jdljmd5.html)

![](/static/2020-05-12-14-28-46.png)

* 补位后长度是512位的整数倍，即**16 * 32bit 字的整数倍**

![](/static/2020-05-12-14-29-54.png)

* （每一个变量给出的数值是高字节存于内存低地址，低字节存于内存高地址，即大端字节序。在程序中**链接变量(chaining variable)**`A、B、C、D`的值分别为0x67452301，0xEFCDAB89，0x98BADCFE，0x10325476）

![](/static/2020-05-12-14-46-56.png)

* 当设置好这四个**链接变量**`A,B,C,D`后<font color="red">复制到另外4个临时变量`a,b,c,d`</font>，就开始**进入算法的四轮循环运算**。循环的次数是信息中512位信息分组的数目(如 16 * 32 bit word，**每轮16次操作，共4轮**)
  * 每一轮的每次操作中，对`a, b, c`其中三个做一次非线性函数运算`F(同一轮中)`，<font color="red">所得结果 + 第4个变量 + 子分组 + 一个常数</font>
  * 上面的结果最后,<font color="red">向右环移一个不定的数，并加上`a, b, c ,d`其中一个，</font>
  * 最后将结果取代临时变量`a, b, c, d`其中一个

* 该一轮(`F`函数，16次操作)完成之后，`A,B,C,D`寄存器变量加上`a,b,c,d`临时变量的值. <font color="red">进行下一轮计算，最后将结果级联，输出</font>

## Symmetrical Encryption (AES)

🍊 对称加密(单密钥加密)

> 采用单钥密码系统的加密方法，**同一个密钥**可以同时用作信息的**加密和解密**

![](/static/2020-05-12-16-25-40.png)

![](/static/2020-05-12-16-28-25.png)
![](/static/2020-05-12-16-35-59.png)

🍬 对称加密 - 常见加密算法
  * AES（Advanced EncryPtion Standard）**高级加密标准** <font color="red">。是现在公认的最安全的加密方式，是对称密钥加密中最流行的算法。</font>
  * DES (Data Encryption Standard): 数据加密标准
    * ![](/static/2020-05-12-22-50-19.png)

---

### AES: Advanced Encryption Standard

[参考 - AES加密算法的详细介绍与实现](https://blog.csdn.net/qq_28205153/article/details/55798628)
![](https://imgconvert.csdnimg.cn/aHR0cDovL2ltZy5ibG9nLmNzZG4ubmV0LzIwMTcwMjE5MDgyOTA5Njg4?x-oss-process=image/format,png)

🍊 terms

* 密钥

> 用于加密明文的密码. **对称加密算法中, 加密与解密使用相同密钥**.
>
> 密钥由接收方&发送方协商产生，不可以直接传输，导致**密钥泄露**. **通常通过非对称加密算法加密密钥**，然后进行传输，或**面对面商量密钥**

* 密文

> 明文经过加密函数加密后的数据

* AES加密函数

> 设AES加密函数为E，则 **C = E(K, P)**,其中P为明文，K为密钥，C为密文。也就是说，把**明文P和密钥K**作为**加密函数E的参数输入**，则加密函数E会输出**密文C**

* AES解密函数

> 设AES解密函数为D，则 **P = D(K, C)**,其中C为密文，K为密钥，P为明文。也就是说，把**密文C和密钥K**作为解密函数的参数输入，则解密函数会输出**明文P**

## Asymmetrical Encryption

🍊 非对称加密(公钥加密)

> 由对应的**一对唯一**性密钥（即**公开密钥和私有密钥**）组成的加密方法。
>
> 解决了**密钥的发布和管理问题**，是商业密码的核心。
>
> 在公钥加密体制中，没有公开的是**私钥**，公开的是**公钥**

![](/static/2020-05-13-23-15-36.png)

🍙 如果用**公开密钥对数据进行加密**，只有**用对应的私有密钥才能解密**

🍙 如果用**私有密钥对数据进行加密**，只有**用对应的公开密钥才能解密**

🍊 特性

* 算法复杂，安全性基于算法&密钥
* <font color="red">加密解密速度慢</font>

🍬 非对称加密 - 常见加密算法
  * D-H(Diffie-Hellman) 密钥交换协议中的**公钥加密算法**
  * RSA(Rivest-Shamir-Adleman)加密算法
  * ECC(Elliptic curve-based)**椭圆曲线加密算法**

---

> 非对称: **指一对加密密钥与解密密钥**
>
> 密钥加密后所得的信息，只能用解密密钥才能解密。**知道其中一个，不能计算出另外一个**
>
> 公开的密钥为**公钥**；不公开的密钥为**私钥**

### Extension: Digital Signature

🍊 数字签名(公钥数字签名)

> 发送方生成的无法伪造的一段数字串，**对信息发送方信息真实性的有效证明**
>
> 一套数字签名包含两种互补运算: **一个用于生成签名(私钥加密)，另一个用于验证(公钥解密)**
>
> 是非对称公钥加密技术的应用

🍬 数字签名目的

* 证明消息来自发送方(非对称)
![](/static/2020-05-13-18-43-45.png)

* 证明消息内容无篡改(MD5+非对称)
![](/static/2020-05-13-18-48-57.png)

### RSA

🍊 公钥加密体制中

* **加密密钥 - PK公钥公开**，**解密密钥 - SK密钥保密**
* 加密E/解密D 算法公开

🍊 RSA原理

* 生成一对RSA密钥，一个公开，一个保密(用户)
* 长度至少500bit，**推荐1024bit**
  * 导致计算量大，因此传送信息时，<font color="red">采用传统加密(单密钥)方法&公钥加密相结合: DES/IDEA/AES加密传输通信，然后使用RSA密钥加密对话密钥&信息摘要.</font>接收方解密获取明文

---

[参考](https://www.cnblogs.com/lene-y/p/10368269.html)

🍬 RSA等非对称加密，为什么**公钥加密，私钥解密**？

* 公钥多人拥有，私钥一人拥有
* 私钥长度长，破解难度大
* <font color="red">公钥加密，私钥解密 - 用于保护数据传输机密性</font>
  * A生成一对公私钥，将公钥发送给B，B接收到后用这个公钥加密对称算法的密钥，然后发送给A，A接收到后利用私钥解密得到对称算法的密钥
* <font color="red">私钥加密，公钥解密 - 用于<b>数字签名</b>【防篡改 & 防假冒 & 不值得加密的非关键性数据】</font>
  * 甲方：私钥(甲知)加密明文进行签名
  * 乙方：通过甲方公钥(甲乙公开)验证签名
  * 验证成功：数据确实来自甲方(精确匹配)，数据未修改.
  * <font color="blue">相反，公钥公开，所有用户都可以做签名，无法精确判断来自哪一方</font>

## Distinguish Between Symmetrical & Asymmetrical Encryption

🍊 对称(单密钥)&非对称(公钥)加密方法对比

* 对称加密
  * 只有**1种密钥**,非公开,<font color="red">加密解密需要同一个密钥</font>
  * 加密快(如AES加密算法)
  * 密钥传输麻烦
* 非对称加密
  * 有**2种密钥(密钥，公钥)**，其中一个公开
  * 加密解密慢(如RSA加密算法)
  * 密钥传输方便

### Application: RSA+AES = Hybrid

🍬 RSA(非对称)&AES(对称)加密结合使用

* 对称加密，密钥传输麻烦。
* 一般通过**RSA加密AES的密钥**，加密后密钥传输到接收方，接收方(非对称)解密得到AES密钥.然后使用该密钥进行通信

## ------

## Extension: BASE64 Encoding

> 最常见的用于传输8bit字节码(byte-code)的编码方式之一
>
> **64个(可打印)字符**来表示**任意二进制数据**的方法
>
> 是**二进制到字符**的过程

🍊 特性

* 可用于HTTP下，**传递较长标识信息**
* <font color="red">具有不可读性，需要解码后阅读</font>
* 不适用于URL，因为标准Base64编码可能出现char,`+` & `/`
* Base64适用于**小段内容的编码，比如数字证书签名、Cookie的内容等**。

---

![](/static/2020-05-13-19-59-29.png)

## Cryptography: Symmetric vs Asymmetric

![](/static/2020-05-13-19-29-30.png)

🍊 对称加密(单密钥加密)

* 对称加密算法将明文处理为密文(cipher-text)
  * 密文为二进制数据，可能需要**base64编码(二进制数据处理成文本)**
* 加密解密快，适合大数据
* 通信对话由秘钥保护
  * 加密解密使用**相同秘钥**
  * <font color="blue">避免data泄露，安全保存秘钥</font>

🍬 需要考虑如何保存对称加密过程中的SK

* 一般结合RSA & AES等进行传输

---

![](/static/2020-05-13-20-07-01.png)

🍊 非对称加密(公钥加密)

* 一对密钥
  * 公钥PK - 公开
  * 秘钥SK - 保密
* 公钥加密，私钥解密
  * 解决了传输过程中key的保管问题

🍬 非对称加密加密解密**速度慢**

## Cryptography: Hybrid

[参考 - HTTPS请求流程(证书的签名及验证过程)](https://blog.csdn.net/gsn1125227/article/details/98594039)

![](/static/2020-05-13-23-05-28.png)

* 结合使用对称&非对称加密
  * 生成随机session密钥，用于对称加密
  * 使用非对称加密体制 - 加密session秘钥
    * 相对快，session key大小不大
  * 使用对称加密体制 - 加密数据
    * 加密解密需要使用 session key

<<<<<<< HEAD
🍬 应用 ：如 <font color="blue">HTTP(应用层)</font>中使用的**传输层安全协议TLS**

* 由TLS协议进行创建加密通道需要的协商和认证。<font color="red">应用层协议传送的数据在通过TLS协议时都会被加密，</font>从而保证通信的私密性

🍙 具体

* HTTPS为了兼顾安全与效率，同时使用了对称加密和非对称加密。数据是被对称加密传输的，对称加密过程需要客户端的一个密钥，为了确保能把该密钥安全传输到服务器端，采用非对称加密对该密钥进行加密传输，总的来说，**对数据进行对称加密，对称加密所要使用的密钥通过非对称加密传输。**

### Extension: TLS/SSL & HTTPS(HTTP+SSL) & SSH

![](/static/2020-05-14-00-22-10.png)

* <font color="blue">SSL3.0后废案成TLS1.0</font>

🍙 TLS & SSL & DTLS

* 都为网络通信提供 安全(加密) & 数据完整性(可用签名测)
* 应用 : <font color="blue">在传输层 & 应用层之间对网络链接(数据)进行加密</font>
* 区别：TLS加密算法更严格，TLS能在不同端口使用. 三个协议都工作于传输层之上，<font color="purple">DTLS基于UDP传输协议，TLS基于TCP传输协议之上</font>

![](/static/2020-05-15-22-26-39.png)

🍊 TLS - Transport Layer Secruity 【传输层安全协议】

* 前身基于<font color="blue">SSL安全套接层协议(已废案)</font>

> 安全传输层协议（TLS）用于在任意**两个通信应用程序之间**提供保密性和数据完整性
>
> 由 **TLS 记录协议（TLS Record）和 TLS 握手协议（TLS Handshake）** 两层组成

🍬 TLS协议优点

* 与应用层协议(HTTP,FTP,Telnet)等无耦合。

🍊 DTLS - Datagram TLS 【数据包传输层安全协议】

* <font color="purple">保证UDP数据安全。</font>基于TLS。

---

🍊 SSL -  Secure Sockets Layer 【安全套接字协议】

> 安全套接层（Secure Sockets Layer，缩写作SSL）是**一种安全协议**，目的是为互联网通信提供安全及数据完整性保障。
>
> 包含： 记录层(协议确定传输层数据的封装格式) & 传输层(安全协议使用X.509认证, 使用**混合加密**)
>
> 多用于浏览器&服务器之间的安全传输(如HTTPS内置于浏览器中)

🍬 SSL提供服务

* 认证
* 加密数据
* 维护数据完整性

🍙 SSL证书重要性

> SSL证书可以保证网站的信息从用户浏览器到服务器之间的传输是高强度加密传输的，是不会被窃取和篡改的。
>
> 在有SSL安全证书情况下，只有发送&接收方能够接到相关信息。

---

🍊 HTTPS

![](/static/2020-05-14-00-34-09.png)

---

🍊 SSH (Secure Shell)

> 建立在**应用层**基础之上的安全协议。
>
> 为**远程登陆会话(防止远程管理过程中信息泄露)**&其他网络服务提供安全性
>
> SSH客户端适用多平台

[参考 - difference between SSL & SSH](https://www.rapidsslonline.com/ssl/difference-between-ssh-and-ssl/)

🍬 SSH & SSL对比

* 使用端口不同
  * SSH - 443
  * SSL - 22
* 使用情景不同
  * SSL - 基于传输层，加密连接
  * SSH - 基于应用层，<font color="purple">主要实现加密远程登录服务，需要客户端验证。之后可远程访问进行commands操作。</font>

#### Extension: CSR

[参考 - csr&ssl区别](https://blog.csdn.net/zltAlma/article/details/89350476)

[参考 - What is a CSR](https://www.globalsign.com/en/blog/what-is-a-certificate-signing-request-csr)

[参考 - 完整: What is a Certificate Signing Request?](https://www.namecheap.com/support/knowledgebase/article.aspx/337/67/what-is-a-certificate-signing-request-csr)

🍊 Certificate Signing Request - 证书签名请求文档

> CSR文件安装在**服务器端**
>
> **CSR是获取SSL数字证书的第一步**。包含需**提供给CA认证中心生成CSR文档时的所需信息**(名，组织，国家).
>
> CSR文件包含用于加密传输给server端(已安装证书)数据的公钥(用户可知)
>
> **同时生成服务器端秘钥，需要进行安全保管，只有server具有RSA秘钥**
>
> 如秘钥遗失，可再次申请CSR文件&生成新的RSA密钥对
>
> OpenSSL等工具可生成，[OpenSSL:SSL证书请求文件CSR生成指南](http://service.oray.com/question/4985.html)
>
> CSR基于**BASE64**编码格式(二进制->text). 例子:
> ![](/static/2020-05-16-05-29-36.png)

---

[参考 - HTTPS请求流程(证书的签名及验证过程)](https://blog.csdn.net/gsn1125227/article/details/98594039)

🍙 完整流程

1. client向server发起第一个`HTTPS`请求，携带client的`SSL/TLS`信息。<font color="purple">已安装证书的server具有一对非对称加密密钥对</font>.

   * <font color="blue">私钥</font>由server保管用于解密
   * <font color="blue">公钥</font>发放给用户用于client加密data

2. client接收server数字SSL证书(公钥)，进行验证合法性

   * client检查系统内置存放的CA认证中心公钥，验证是否为受信任的证书

3. 如server证书合法，<font color="purple">client生成对称加密密钥，进行数据加密。并用server的公钥对该对称加密密钥加密，获得<b>会话session密钥</b></font>

   * <font color="green">第一次HTTPS会话请求结束</font>

4. client发起第二个`HTTP`请求，将会话密钥发送给服务器
5. server接收会话密钥(密文)，用自己的私钥进行非对称解密，获取对称加密秘钥

   * 之后再用该对称加密秘钥对数据进行对称加密，获得密文

6. server将(对称加密后)密文传输给client
7. client收到密文，进行对称解密，获得明文

   * <font color="green">整个HTTPS传输完成</font>

#### Summary

[参考 -  SSL/TLS,HTTPS,SSH综述](https://ssl.zzidc.com/chanpinzixun/2019/0809/674.html)

1. SSL 安全套接字层协议

   * SSL**传输层之上**，对socket链接加密的协议
   * <font color="blue">多用于Internet中，<b>浏览器&服务器之间的安全传输</b></font>
   * ![](/static/2020-05-14-18-13-59.png)
   * ![](/static/2020-05-14-18-14-45.png)

2. TLS 传输层安全协议

<<<<<<< HEAD
<<<<<<< HEAD
<<<<<<< HEAD
   * TLS**传输层之上**，<font color='blue'>用于任何两个应用程序(即，任意端口)之间的传输</font>。<font color="purple">TLS保证TCP数据，DTLS保证UDP数据传输</font>
=======
   * TLS**传输层之上**，<font color='blue'>用于任何两个应用程序(即，任意端口)之间的传输</font>
>>>>>>> 2681846... # This is a combination of 2 commits.
=======
   * TLS**传输层之上**，<font color='blue'>用于任何两个应用程序(即，任意端口)之间的传输</font>。<font color="purple">TLS保证TCP数据，DTLS保证UDP数据传输</font>
>>>>>>> 00c2fc8... key escrow
=======
   * TLS**传输层之上**，<font color='blue'>用于任何两个应用程序(即，任意端口)之间的传输</font>
>>>>>>> f111854... up
   * ![](/static/2020-05-14-18-27-42.png)

3. HTTPS(<b>在传输层适用SSL/TSL加密的HTTP</b>)

   * HTTP（S）本身是应用层协议
   * 同一台web服务器，同时支持HTTP(80端口) & HTTPS(443端口)

4. SSH (Secure Shell)

   * <font color="blue">应用层的通信加密协议</font>
   * <font color="red">通常用于远程登陆会话</font>
   * ![](/static/2020-05-14-18-40-26.png)

### Extension: Hybrid Cryptography
=======
🍬 应用 ：如 HTTP中的传输层安全协议TLS

### Extension: TLS

* 前身基于<font color="blue">SSL安全套接层协议()</font>

> 安全传输层协议（TLS）用于**在两个通信应用程序之间提供保密性和数据完整性**
>
> 由 **TLS 记录协议（TLS Record）和 TLS 握手协议（TLS Handshake）** 两层组成

---

### Extension
>>>>>>> a47f5fc... progress in cryptography

[参考1](https://blog.csdn.net/jiang_xinxing/article/details/54134631)

🍬 混合加密补充

> 使用对称加密处理明文. 公钥加密处理会话密钥(即先前对称加密中涉及的密钥).

* 对称加密 - 加密明文
  * 明文被处理成信息摘要，保证机密性
* 非对称加密 - 加密对称加密密钥
  * 即加密得到**临时、随机，会话密钥**，体积小，加密解密速度快
* <font color="red">明文加密解密需要使用会话密钥</font>

🍙 具体机制

![](/static/2020-05-13-23-42-01.png)

---

🍊 会话密钥

* 为本次通信生成的临时密钥，**一般由伪随机数生成器产生**.
<<<<<<< HEAD
* 作为对称加密的密钥使用

## Authentication: Data Tampering

🍬 已知，<font color="blue">私钥加密，公钥解密可以验证数字签名</font>，保证了数据来自发送方&机密性

🍊 数据篡改 - 密码散列

![](/static/2020-05-14-19-01-16.png)
![](/static/2020-05-14-19-12-01.png)

* 结合 <font color="blue">密码散列函数(如MD5，SHA-2) & 公钥加密</font>
  * 生成数字签名
  * 任意长度明文 -> 生成固定长度的哈希散列(如256, 128 bit)
  * <font color="purple">满足单向性(难以由散列函数输出的结果，回推输入的数据是什么)</font>
  * <font color="purple">不同明文生成不同散列</font>
* 可用于验证数据源

🍬 密码散列应用

* MD5 & SHA-1 (都被证实可破解)
* SHA-2(a.k.a, SHA-256)
  * ![](/static/2020-05-14-19-43-49.png)

---

🍊 数字签名

![](/static/2020-05-14-19-44-39.png)

* 生成
  * 明文生成消息摘要(密码哈希散列)
  * 非对称秘钥加密该哈希散列
* 验证
  * 重新计算明文密码散列
  * <font color="blue">用非对称公钥解密先前的数字签名</font>
  * 比较两个哈希散列是否匹配

<<<<<<< HEAD
## ASIDE：Existing Secure Protocols
=======
## Existing Secure Protocols
>>>>>>> f111854... up

🍊 安全协议 - 提供机密性&验证

![](/static/2020-05-15-21-20-43.png)

* IPSec - Internet Protocol Security
  * 是一个协议包，通过对**IP协议的分组（网络层,第三层）进行加密和认证**来保护IP协议的网络传输协议族（一些相互关联的协议的集合）
  * 是一组基于网络层的，应用密码学的安全通信协议族。<font color="purple">IPSec不是具体指哪个协议，而是一个开放的协议族。</font>
  * <font color="blue">主要用于VPNs(虚拟专用网)</font>
    * ![](/static/2020-05-15-21-45-01.png)
* SSL - Secure Sockets Layer
  * SSL1.0-3.0之后废案使用TLS1.X，工作在传输层之上
* TLS - Transport Layer Security (TLS 1.2)
  * 适用于任意端口应用程序，基于TCP之上工作
* Datagram TLS - DTLS
  * 基于传输层UDP协议
<<<<<<< HEAD
* Secure RTP - Real-Time Transport Protocol
  * 工作于应用层
  * 即时传输协议，即使传输<font color="purple">媒体流(multimedia)</font>数据。SRTP为加密版本，减少了服务访问拒绝攻击等风险。
  * <font color="blue">RTP协议很少单独使用，通常与RTSDPS & SDP等协议结合使用</font>
* SSH - Secure Shell
  * 安全外壳协议**框架**
  * 适用于多平台，基于应用层。<font color="purple">通常用于远程<b>登陆</b>会话加密。需要客户端验证</font>

## ASIDE：Using TLS

![](/static/2020-05-16-00-05-52.png)

* IETF国际互联网工程任务组，提供[如何安全使用TLS&DTLS](https://tools.ietf.org/html/rfc7525)文档
  * 项目需要引入TLS时参考
  * IETF - [程序中使用TLS](https://datatracker.ietf.org/wg/uta/charter/)
* TLS协议开源项目 - <font color="red">OpenSSL</font>
  * 提供多种SSL相关服务，如CSR(证书签名请求文件)，秘钥生成，SSL证书安装等。[[下载](https://wiki.openssl.org/index.php/Binaries)]
  * 应用程序可以使用这个包来进行<font color="purple">安全通信，避免窃听，同时确认另一端连接者的身份。这个包广泛被应用在互联网的网页服务器上</font>
  * 通过OpenSSL库提供的[commands](https://www.openssl.org/docs/manmaster/man1/)，可以进行为服务器申请数字CSR证书&SSL文件等操作
* OS,WIN使用系统库

## Key Escrow

[参考 - 现代密码学13讲密钥托管](https://wenku.baidu.com/view/a7c215e068dc5022aaea998fcc22bcd126ff42d8.html)

🍊 密钥托管

> 也称托管加密，目的：保证个人匿名性&隐私。
>
> 实现：结合已加密的数据 & 数据恢复密钥
>
> **可通过数据恢复密钥得到解密密钥，由所信任的委托人持有**。【即，一个备用的解密途径，对政府&个人用户有利，如用户遗失秘钥】

🍙 (数据)密钥恢复 - key recovery

* 一种私钥加密方法。所信任的授权方(如政府)可从加密数据中恢复密钥

![](/static/2020-05-16-23-31-23.png)

* 密钥托管： 可能造成恶意攻击
* 托管系统基本组成
  * 第三方外部加密解密途径，可访问用户明文
* 前瞻性解决方案与后瞻性解决方案 - 加密这样第三方托管系统的方式
  * 强制性的密钥公开法，强制性的解密法等等

<<<<<<< HEAD
## ---

## Developing Secure Network App: Robustness Principle

[参考 - 伯斯塔尔法则(此文章包含部分启发性原则,UI用户体验可用性问题)](http://www.biliui.com/home/info/detail/id/499.html)

🍊 伯斯塔尔法则(a.k.a 鲁棒性原则)
![](/static/2020-05-16-23-47-01.png)

> 对接受的内容保持自由，对发送的内容保持谨慎.
>
> 核心思想： 系统/产品应保有一定程度容错能力.
>
> 该核心思想应用与UI/UX领域

## Validating Input Data

![](/static/2020-05-17-00-01-07.png)

* 网络程序需要处理不可信第三方提供的数据
  * 传输数据可能不服从协议规范
  * 存在网络原因，丢包等
  * 存在恶意攻击
* <font color="purple">因此，处理前必须验证数据</font>
* 网络app
  * 必须仔细指定正确和不正确的输入行为
  * 必须仔细验证输入&处理错误
  * 如果使用类型和内存不安全的语言(如 c 和 c + +) ，则必须格外小心，因为这些语言具有额外的缺陷模式

### Example: Buffer Overflow Attack

🍊 缓冲区溢出攻击

* 内存安全的编程语言 - 检查数组界限
  * 越界访问异常，编译/运行时期警告
* 非内存安全的编程语言 - C/C++
  * 缓存区检查，由程序员负责
  * 容易造成core dump【核心转储，程序运行过程中发生异常，由OS将当前内存状况存储在一个core文件中】，或未定义行为

---

🍊 网络安全问题

![](/static/2020-05-17-00-32-42.png)
=======
## ---
>>>>>>> 00c2fc8... key escrow
=======
* Secure RTP - Real-Time Transport Protocol
>>>>>>> f111854... up
=======
* 作为对称加密的密钥使用
>>>>>>> a47f5fc... progress in cryptography
