词法分析器
编译原理课程完成的词法分析器，使用Java实现
实验要求
1．待分析的简单的词法
（1）关键字：
 	begin  if  then  while  do  end  
注：所有的关键字都是小写。
（2）运算符和界符
. +  -  *  /   = <>  <  <= >  >=  (  ) ; := 
（3）其他单词是标识符（IDENT）和整型常数（NUMBER），通过以下正规式定义：
IDENT  ::= letter (letter | digit)*
NUMBER ::= digit digit*
注：所有的IDENT和NUMBER的长度不超过20
（4）空格有空白、制表符和换行符组成。空格一般用来分隔IDENT、NUMBER、运算符、界符和关键字，词法分析阶段通常被忽略。整个程序串长度不超过80个字符。
2．	各种单词符号对应的类别值：
表 各种单词符号对应的类别码
单词符号 是否保留字	枚举值	    类别码 
.		        period	    0
+		        plus	    1
-		        minus	    2
*		        times	    3
/		        slash	    4
=		        eql	        5
<>		        neq	        6
<		        lss	        7
<=		        leq	        8
>		        gtr	        9
>=		        geq	        10
(		        lparen	    11
)		        rparen	    12
;		        semicolon	13
:=		        becomes	    14
begin	Y	    beginsym	15
end	    Y	    endsym	    16
if	    Y	    ifsym	    17
then	Y	    thensym	    18
while	Y	    whilesym	19
do	    Y	    dosym	    20
IDENT		    ident	    21
NUMBER		    number	    22

3．	词法分析程序的功能：
输入：所给文法的源程序字符串,以“.”结束。
输出：二元组（sym, token或number）构成的序列。
其中：sym为单词种别
      token为存放的单词自身字符串；
      number为整型常数。对于数值串需要转化为实际的值。

示例：
源程序：  begin x:=9;if x<10 then x:=(x+10)*2 end. 
经过词法分析后输出如下序列：(15,begin)(21,x)(14,:=)(22,9)(13,;)……
