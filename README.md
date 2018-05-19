# Hanoi-Game
2004年初学Java练手的小程序，很丑，见谅。

<img src="https://github.com/yueritian/Hanoi-Game/blob/master/docPics/page.png" width="500" alt="运行界面"/>

## 游戏规则
>1. 请先选择层数再进行游戏
>2. 按“+”，“-”增加或减少塔层数
>3. 按“开始”启动游戏
>4. 按“a移动”，“b移动”，“c移动”抓起放下塔。
>5. 不能长层压短层的 将壹中的塔完全一样的搬到贰叁中游戏成功

## 练习思路
>1. 问题描述： 
>* 塔的绘制，以及移动过程中的绘制；
>* 英雄榜留下阁下大名;
>* 玩家选择塔层数进行游戏，玩家移动一塔座上的塔到另一塔座下，游戏完成后显示玩家的游戏信息.
>2. 解决方案： 
>* 数组使用——取/放塔
>* 按钮事件使用——响应用户操作
>* 多线程;
>* paint()方法；
>* 数据库使用。

## 变量说明
>* 最小塔长：   w1 = 10，
>* 其它塔长：   w2 = (2n-1)*10；
>* 每个塔高：   h = 10;
>* 最大x坐标： X1 = 70，
>* 其它x坐标： X2 = 70-(n-1)*10；
>* 最大y坐标： Y1 = 207; 
>* 其它y坐标： Y2 = 207-(n-1)*(10+2) 
>* 塔长成等差数列：  an=30+（n-1）*20= (2n-1)*10
>* X坐标也成等差数列：an= 70-(n-1)*10
>* 参看下图：
>> <img src="https://github.com/yueritian/Hanoi-Game/blob/master/docPics/demo.png" width="200" alt="坐标值介绍"/>

## 主要代码说明
<img src="https://github.com/yueritian/Hanoi-Game/blob/master/docPics/0.png" width="500" alt="介绍"/>
<img src="https://github.com/yueritian/Hanoi-Game/blob/master/docPics/1.png" width="500" alt="介绍"/>
<img src="https://github.com/yueritian/Hanoi-Game/blob/master/docPics/2.png" width="500" alt="介绍"/>
