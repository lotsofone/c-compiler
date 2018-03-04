# c-compiler
学生项目，简易c语言编译器

使用Java编写，可将简单的c语言编译成8086汇编代码。

项目由三人完成，本人完成了词法分析和汇编代码生成的部分。

### 使用方法：
把你的c语言代码粘贴到main.c里面（下载后的main.c里面有示例代码），然后用IDEA运行Entry的main函数，生成的汇编代码就会出现在8086.asm里面，可以用masm编译运行观察结果。

可使用scani()，printi(int a)，printlln三个内置函数进行简单输入输出，示例代码中有使用例子。
### 实现程度：
本程序可以实现简单输入输出，函数的递归调用，二维数组。可用的数据类型只有int一种类型。变量声明必须放在一个函数的开始部分。
## 运行效果：
提供一个示例代码如下：

<image src="https://github.com/lotsofone/image-repository/blob/master/c-compiler/1.png" width="300" height="auto"></image>

编译后的代码就放入了8086.asm

<image src="https://github.com/lotsofone/image-repository/blob/master/c-compiler/2.png" width="360" height="auto"></image>

使用8086的汇报运行工具运行就得到如下效果

<image src="https://github.com/lotsofone/image-repository/blob/master/c-compiler/3.png" width="450" height="auto"></image>
