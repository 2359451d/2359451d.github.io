---
layout: post
title: 记录 - 可视化基础 - matplotlib
date: 2020-12-02
excerpt: "记录"
tags: [code,其他，可视化, 2020, question]
feature: http://user.ceng.metu.edu.tr/~ys/ceng707-dsa/overview.png
comments: true
---

* 目录
{:toc}

## 快速选择

![](/static/2020-12-02-22-50-06.png)

## 散点图

散点图( scatter diagram) :

* 是以一个变量为横坐标，另一变量为纵坐标，利用散点(坐标点)的分布形态**反映变量关系的一种图形**

```python
# ’.’、 ‘o’ ,小点还是大点
plot(x,y,'.',color=(r,g,b))
plt.xlabel('x轴标签')
plt.ylabel('y轴标签')
plt.grid(True)

plt.show()

```

## 折线图

■折线图:

* 也称趋势图，它是用直线段将各数据点连接起来而组成的图形，以折线方式显示**数据的变化趋势**

`plt.plot(x,y,style,color,linewidth);`

* `style`
  * canvas style
  * ![](/static/2020-12-02-23-28-05.png)
* `color`
  * line color
* `linewidth`

`plt.title('title of the graph')`

## 饼图

饼图( Pie Graph) :

* 又称圆形图。是一个划分为几个扇形的圆形统计图，它能够直观地反映**个体与总体的比例关系**

`plt.pie(x, labels, colors, explode, autopct);`

* `labels`
  * 饼图各部分标签
* `explode`
  * 需要突出的块状序列
* `autopct`
  * 饼图占比的显示格式
  * %.2f :保留两位小数

![](/static/2020-12-02-23-32-59.png)
![](/static/2020-12-02-23-35-34.png)
![](/static/2020-12-02-23-35-43.png)

## 柱状图

■柱形图:

* 是一种以长方形的单位长度，根据数据大小绘制的统计图,用来**比较两个或以上的数据(时间或类别)**。

bar(left,height,width,color) #竖向柱形图
barh(bottom,width,height,color) #横向柱形图

![](/static/2020-12-02-23-38-54.png)
![](/static/2020-12-02-23-39-07.png)
![](/static/2020-12-02-23-40-16.png)
![](/static/2020-12-02-23-40-44.png)