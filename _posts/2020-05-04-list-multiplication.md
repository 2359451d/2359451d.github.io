---
layout: post
title: 记录 - python列表 *操作符问题
date: 2020-05-04
excerpt: "记录"
tags: [code,其他, 2020, question]
feature: http://user.ceng.metu.edu.tr/~ys/ceng707-dsa/overview.png
comments: true
---

* 目录
{:toc}

## Question

[StackOverflow参考](https://stackoverflow.com/questions/240178/list-of-lists-changes-reflected-across-sublists-unexpectedly)

python列表操作符`*`，相当于math中的加法

```python

a = [1] * 4

>>a # [1] + [1] + [1] + [1] referenced 4 times
[1, 1, 1, 1]


```

🍬 问题

```python

# myList = [[1, 1, 1, 1], [1, 1, 1, 1], [1, 1, 1, 1]]
myList = [[1] * 4] * 3

# myList = [[5, 1, 1, 1], [5, 1, 1, 1], [5, 1, 1, 1]]
myList[0][0] = 5

```

![](/static/2020-05-04-18-19-24.png)

以上，使用`*`重复引用相同地址生成最外层列表

* 修改内层列表元素，因为引用相同，其他相同地址的引用也会改变

* 修改外层列表元素，仅修改改列表本身，不存在其他引用

