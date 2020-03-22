顶点着色器
---
经过之前的学习，大家对OpenGL的渲染流程有了一个大概的了解。那么，我们先来看一张经典的流程图：  
![pipeline](./渲染流程图.png)
从图中可以看到，整个流程的第一步便是顶点着色器。

## 一个简单的顶点着色器程序
顶点着色器顾名思义，它的作用就是对图形的顶点数据进行处理，下面是一段最简单的顶点着色器程序：
```
precision mediump float;
attribute vec4 a_Position;
void main() {
    gl_Position = a_Position;
}
```
 
第一行声明了着色器使用中等精度（precision:精确、精度 ）  
第二行声明了一个vec4属性的变量a_Position  
第三行main为入口函数  
第四行将a_Position直接赋值给gl_Position  
第五行函数结束  

### 须知
* 顶点着色器程序并不是只执行一次，有多少个点便要执行多少次，一次只处理一个顶点。（与之呼应的是片段着色器，有多少个像素便执行多少次）
* attribute表明这个变量是系列变量，每次执行都会改变，这里对应着每一个顶点（与之相对的是uniform，每次执行都是同一个值）
* vec4表示这是一个四维变量（其它还有vec2二维变量），对应一个顶点的xyzw，xyz表示其空间坐标，默认为0，w表示其归一化，默认为1。
* gl_Position是顶点着色器的一个内置变量，表示着色器的输出。

### 顶点坐标系统
在手机屏幕或者原生View中，其原点（0,0）是左上角，x轴和y轴分别向右和向下延伸，没有z轴。其可视区域是是手机屏幕的大小或者View的大小。  
而在openGL中，坐标范围是可以设置的，其默认范围为[-1,1]  
```
//设置坐标范围
gl?.glFrustumf(-1f, 1f, -1f, 1f, -5f, 5f)
```

为了对openGL的坐标系统有一个更全面认识，我们需要知道几个比较重要的坐标系统：
* 物体空间
即某个图形（三角形、正方体、物体模型、人物模型）对于自己空间的坐标

* 世界空间
图形在相对于外部的坐标，在一个大的场景中，各个模型所处的位置及方向

* 观察空间
在场景中，我们摄像机所观察的位置及方向

* 裁剪空间
摄像机所观察的景深及范围

* 屏幕空间
相机所观察到的视图，映射到屏幕上

*小问题：刚刚我们设置的坐标是什么空间的坐标？*

### 坐标变换*

首先，定义一个简单的正三角形：
```
var triangleCoords = floatArrayOf(     // in counterclockwise order:
        0.0f, 0.622008459f, 0.0f,      // top
        -0.5f, -0.311004243f, 0.0f,    // bottom left
        0.5f, -0.311004243f, 0.0f      // bottom right
)
```
然后我们给它一个世界坐标：
```
//世界坐标
val worldPosition = floatArrayOf(
    0.2f, 0.2f, 0f, 1f
)
```
重新编写顶点着色器：
```
precision mediump float;
attribute vec4 a_Position;
uniform vec4 world_Position;
void main() {
    gl_Position = vec4(
       vPosition.x * world_Position.w + world_Position.x,
       vPosition.y * world_Position.w + world_Position.y,
       vPosition.z * world_Position.w + world_Position.z,
       vPosition.w)
}
```
这样，就把一个三角形放入了世界空间中（这里只设置了它的位置和大小，并没有设置其方向）

在正常开发中，我们都试用矩阵进行计算：
```
//平移矩阵
val translateMatrix  = floatArrayOf(
    1.0f, 0.0f, 0.0f, x,
    0.0f, 1.0f, 0.0f, y,
    0.0f, 0.0f, 1.0f, z,
    0.0f, 0.0f, 0.0f, 1.0f
)
//缩放矩阵
val scaleMatrix   = floatArrayOf(
    w, 0.0f, 0.0f, 0.0f,
    0.0f, w, 0.0f, 0.0f,
    0.0f, 0.0f, w, 0.0f,
    0.0f, 0.0f, 0.0f, 1.0f
)
```
* 平移矩阵  
![t](./平移矩阵.webp)
* 缩放矩阵
![t](./缩放矩阵.webp)
* x轴旋转矩阵
![t](./x轴旋转矩阵.webp)
* y轴旋转矩阵
![t](./y轴旋转矩阵.webp)
* z轴旋转矩阵
![t](./z轴旋转矩阵.webp)



着色器变量关联