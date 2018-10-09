词法分析器

语言版本：Java8.0 IDE：Intellij IEDA

编译原理课程完成的词法分析器

实验要求

1．待分析的简单的词法

（1）关键字：
 	begin  if  then  while  do  end  
 
注：所有的关键字都是小写。

（2）运算符和界符：
. +  -  *  /   = <>  <  <= >  >=  (  ) ; := 

（3）其他单词是标识符（IDENT）和整型常数（NUMBER），通过以下正规式定义：

IDENT  ::= letter (letter | digit)*

NUMBER ::= digit digit*

注：所有的IDENT和NUMBER的长度不超过20

（4）空格有空白、制表符和换行符组成。空格一般用来分隔IDENT、NUMBER、运算符、界符和关键字，词法分析阶段通常被忽略。整个程序串长度不超过80个字符。

2．	词法分析程序的功能：

输入：所给文法的源程序字符串,以“.”结束。

输出：二元组（sym, token或number）构成的序列。

其中：sym为单词种别
      token为存放的单词自身字符串；
      number为整型常数。对于数值串需要转化为实际的值。

示例：

源程序：  begin x:=9;if x<10 then x:=(x+10)*2 end. 

经过词法分析后输出如下序列：(15,begin)(21,x)(14,:=)(22,9)(13,;)……
