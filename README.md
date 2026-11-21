# piglin_trade_calculator
## 简介
这是一个scarpet脚本，用于计算猪灵交易的理论平均效率 \
作者：Cupperum29，Deepseek V4.1 Flash辅助 \
适配版本：1.16.2+ \
‼️注意‼️：\
由于部分交易核心的输出不是连续的，因此不能单纯把效率除以打包机效率来确定打包机数量，需注意漏斗以及分流器的吸力，否则会导致收集率不同程度地下降


## 安装
[官方文档](https://github.com/gnembon/fabric-carpet/blob/c6d48ab4/docs/scarpet/api/Overview.md?plain=1#L21-L23) \
说人话：把.sc文件放进存档的scripts文件夹里，进入存档后再输入`/script load <脚本名>`即可成功加载。 \
如果你是单人游戏想要存档都启用这个脚本而嫌一个个复制粘贴太麻烦，那就把它放进`.minecraft/config/carpet/scripts`里。 \
如果是打开着存档的情况对scarpet脚本有修改，先输入/reload再执行上述操作。 \
另外，推荐打开地毯的自动加载脚本规则`/carpet scriptsAutoload true`，这样进入存档时就能自动加载scarpet脚本。
## 功能
该脚本注册了两个指令
```
/piglin_trade_calculator <count> 
/piglin_trade_calculator <count> <item>
```
第一个参数count输入猪灵数量，仅可输入正整数；第二个参数item输入物品种类，仅可输入预选项中的物品。 \
\
前者将像地毯的漏斗计数器那样输出当前版本所处的范围、猪灵的数量、总效率以及每种物品的效率。 \
如图所示 \
<img width="761" height="716" alt="QQ_2026-11-17_20-49-27" src="https://github.com/user-attachments/assets/f8c566ae-5ee5-4228-8e82-5abb6b945f65" /> \
\
后者将单独输出猪灵和指定物品的效率。 \
如图所示 \
<img width="742" height="49" alt="QQ_2026-11-17_20-49-55" src="https://github.com/user-attachments/assets/412beb8a-aded-444f-afad-19b67866a246" />
