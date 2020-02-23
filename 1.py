dict={'lsy':190,'dy':120,'wy':130,'x':110}
name = input("请输入姓名：")
height = dict[name]
heightList= list(dict.values())
name = list(dict)
count=0
while height<heightList[count]:
    print(height)
    print("--------->",heightList[count])
    print(name[count])
    count+=1
