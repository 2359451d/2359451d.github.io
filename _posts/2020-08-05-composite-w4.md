---
layout: post
title: "Cousera笔记 - 计算机组成4 · 乘法器&除法器"
date: 2020-07-29
excerpt: "乘法运算，乘法器的实现，除法运算，除法器的实现"
tags: [学习笔记, coursera, 计算机组成, 2020]
feature: https://www.gla.ac.uk/media/Media_299663_smxx.jpg
comments: true
---

[参考课程 - 计算机组成](https://www.coursera.org/learn/jisuanji-zucheng/home/week/4)

* 目录
{:toc}

## 乘法器 & 除法器

### 乘法的运算过程

简化后运算过程

![](/static/2020-08-06-01-12-33.png)
![](/static/2020-08-06-01-17-11.png)
![](/static/2020-08-06-01-17-52.png)

* 乘积初始化为`0`
* 每个中间结果产生后直接与当前乘积累加
* 每个中间结果产生后，被乘数向左移动一位