# 基于JavaFX和MySQL的书店管理系统

### 该程序主要具备以下功能

1. 图书信息添加、删除
2. 图书信息修改
3. 收益和利润的自动计算

其中图书信息的添加对应现实中图书的入库，图书信息的删除对应现实中图书的下架。

将该系统与收银系统相连接之后，可以实现卖出书籍后自动减少图书库存，并自动增加总收益和利润。

### 示例输入数据

书名：苏菲的世界
作者：乔斯坦·贾德
ISBN：9787506394864
出版社：作家出版社
价格：38.00
成本：15.00
折扣：8

9787544868570 100天后会死的鳄鱼君 100

### 程序界面和设计思路展示

**①主界面**

![img](file:///C:/Users/wzk/AppData/Local/Temp/msohtmlclip1/01/clip_image002.jpg)

这个界面比较简单，界面中放置了一个label作为提示信息，然后是两个按钮，点击这两个按钮分别可以切换到图书信息界面和交易信息界面。

**②图书信息界面：**

![img](file:///C:/Users/wzk/AppData/Local/Temp/msohtmlclip1/01/clip_image004.jpg)

对于结构化数据，JavaFX中有一个布局非常适合，就是TableView，所以选择TableView作为数据的容器。界面下方是用户的输入框，用户可以在其中输入一本书的各个信息，每个框对应的信息也在文本框中以浅色字体进行了提示。输入之后，只要点击Add按钮，就会自动提交这些信息到数据库中，其中库存stock是默认为0的，所以不需要输入。如果用户输入了非法的值，比如在price的文本框内输入了其他字符，程序会发现错误然后提示用户。提交一条新的图书信息之后，这条信息也会立刻被展示在界面中。

 

**A.** **插入书籍信息**

![img](file:///C:/Users/wzk/AppData/Local/Temp/msohtmlclip1/01/clip_image006.jpg)

尝试插入一本图书，在price的输入框输入了一个非数字的字符串，点击Add，程序会弹窗提醒出现了错误。

![img](file:///C:/Users/wzk/AppData/Local/Temp/msohtmlclip1/01/clip_image008.jpg)

改成正常的值后，插入就成功了。

![img](file:///C:/Users/wzk/AppData/Local/Temp/msohtmlclip1/01/clip_image010.jpg)

书本的信息出现在了用户界面中。其中库存字段默认为0，用户只有通过插入相关的交易记录才能改变这个字段的值。

**B.** **删除书籍信息：**

![img](file:///C:/Users/wzk/AppData/Local/Temp/msohtmlclip1/01/clip_image012.jpg)

鼠标选择书籍《巴别塔》然后点击删除按钮，程序会弹窗提示用户是否确定要删除这本书，这样可以防止误操作的发生。

![img](file:///C:/Users/wzk/AppData/Local/Temp/msohtmlclip1/01/clip_image014.jpg)

成功删除之后，这本书的信息就从界面中去除了。

完成上面的插入和删除之后，打开MySQL workbench。

![img](file:///C:/Users/wzk/AppData/Local/Temp/msohtmlclip1/01/clip_image016.jpg)

数据库中的数据和用户界面中的数据是一致的。

**③交易记录界面**

![img](file:///C:/Users/wzk/AppData/Local/Temp/msohtmlclip1/01/clip_image018.jpg)

用户在输入的时候需要决定的值只有交易类型、交易图书的ISBN号和交易的图书数量。ID值是自增的，数据库会自动设置。而交易金额可以由ISBN号对应的图书信息中的价格、折扣和成本字段自动计算得出。

除了添加交易记录之外，界面右下角还会自动计算当前总的销售额和利润额，从而给用户提供更多的参考。

 

**A.** **添加交易记录**

**a.** **购买书籍**

![img](file:///C:/Users/wzk/AppData/Local/Temp/msohtmlclip1/01/clip_image020.jpg)

这里相当于购买了10本刚刚加入的图书《见识》。同样的，如果在count处输入非法的值，程序也会发现并提示用户，这里就不再重复演示了。

![img](file:///C:/Users/wzk/AppData/Local/Temp/msohtmlclip1/01/clip_image022.jpg)

点击Add按钮，程序弹窗提示插入已经成功了。

![img](file:///C:/Users/wzk/AppData/Local/Temp/msohtmlclip1/01/clip_image024.jpg)

可以看到这条交易记录插入到了表格中，其中金额字段被正确地计算了出来。

![img](file:///C:/Users/wzk/AppData/Local/Temp/msohtmlclip1/01/clip_image026.jpg)

右下角的交易记录也得到了同步的更新。

![img](file:///C:/Users/wzk/AppData/Local/Temp/msohtmlclip1/01/clip_image028.jpg)

再次切换到图书信息界面，可以看到这本书的库存从0增加到了20，和交易记录的更新是同步的。

 

由于交易额的计算依赖于图书信息中的cost字段，所以如果用户输入了一个图书信息数据库中不存在的ISBN号，系统会提示用户先去录入这本书的信息，再来进行交易记录的插入。

![img](file:///C:/Users/wzk/AppData/Local/Temp/msohtmlclip1/01/clip_image030.jpg)

用户可以点击确定进行跳转，或者点击取消留在原界面，对ISBN号的值进行修改。

 

**b.** **销售书籍**

我们尝试销售5本图书《见识》。

![img](file:///C:/Users/wzk/AppData/Local/Temp/msohtmlclip1/01/clip_image032.jpg)

点击add按钮，这条记录马上出现在了交易记录界面中。

![img](file:///C:/Users/wzk/AppData/Local/Temp/msohtmlclip1/01/clip_image034.jpg)

同时下方的交易额和利润额也马上同步更新。

![img](file:///C:/Users/wzk/AppData/Local/Temp/msohtmlclip1/01/clip_image036.jpg)

打开图书信息界面，可以看到库存被正确地减少至5本。同时5*49*0.8正好是等于196，这说明程序的计算是正确的。

![img](file:///C:/Users/wzk/AppData/Local/Temp/msohtmlclip1/01/clip_image038.jpg)

还是以刚才插入的图书《见识》为例，此时库存有5本，如果试图售出5本以上的这本书，程序会弹窗报错。

如果输入的ISBN号在图书信息中尚不存在，同样会提示错误，这点和前面购买书籍一样，不再重复演示。

 

**B.** **删除交易记录**

选中记录之后，点击删除按钮，把刚刚插入的购买书籍《见识》的记录删除

![img](file:///C:/Users/wzk/AppData/Local/Temp/msohtmlclip1/01/clip_image040.jpg)

可以看到这条记录从界面中被去除掉了。

![img](file:///C:/Users/wzk/AppData/Local/Temp/msohtmlclip1/01/clip_image042.jpg)

右下角的交易额也随之进行了更新。

![img](file:///C:/Users/wzk/AppData/Local/Temp/msohtmlclip1/01/clip_image044.jpg)

图书信息中这本书的库存也变成了10，得到了同步更新。

![img](file:///C:/Users/wzk/AppData/Local/Temp/msohtmlclip1/01/clip_image046.jpg)

![img](file:///C:/Users/wzk/AppData/Local/Temp/msohtmlclip1/01/clip_image048.jpg)

在MySQL workbench中查询相关数据，可以看出用户在程序界面进行的一系列操作与数据库是同步的。