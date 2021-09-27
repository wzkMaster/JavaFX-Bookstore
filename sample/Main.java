package sample;

import Books.Book;
import Books.Record;
import JavaFX.AlertBox;
import JavaFX.ConfirmBox;
import Operation.connect;
import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.*;
import javafx.stage.Stage;

import static Operation.connect.*;

public class Main extends Application {

    //声明一些控件作为全局变量，因为这些控件会在多个函数中被使用。
    Stage window;
    Scene mainScene, bookScene, recordScene;
    TableView<Record> recordTable;
    TableView<Book> bookTable;
    ChoiceBox<Integer> typeChoice;
    TextField ISBNInput, countInput, nameInput, authorInput, publisherInput
                , priceInput, costInput, discountInput;
    VBox bookLayout, recordLayout, mainLayout;
    Label amount, profit;

    //启动程序，展示主界面
    public void start(Stage primaryStage) {
        initialize();  //对数据库进行初始化，自动生成一些数据，方便后续调试
        //设置Stage和窗口标题
        window = primaryStage;
        window.setTitle("书店管理系统");

        //主界面的控件创建
        Label label = new Label("请选择要查看或修改的信息"); //一条提示语

        Button bookButton = new Button("图书信息");  //进入图书信息界面的按钮
        bookButton.setOnAction(e -> window.setScene(setBookScene()));  //点击按钮，触发进入图书信息界面的函数

        Button recordButton = new Button("交易信息");  //进入交易信息界面的按钮
        recordButton.setOnAction(e -> window.setScene(setRecordScene()));  //点击按钮，出发进入交易信息界面的函数

        //设置主界面布局
        mainLayout = new VBox(10);
        mainLayout.getChildren().addAll(label, bookButton, recordButton);
        mainLayout.setAlignment(Pos.CENTER);
        //展示主界面
        mainScene = new Scene(mainLayout,300,200);
        window.setScene(mainScene);
        window.show();
    }

    //当用户在除了主界面之外的地方点击关闭按钮时，自动返回主界面
    private void closeProgram() {
        //首先询问用户是否要退出
        Boolean answer = ConfirmBox.display("提示","你确定要退出吗？");
        if(answer)
            //如果是的话，就返回主界面
            window.setScene(mainScene);
            //然后把setOnCloseRequest调回正常模式，不然会陷入死循环
            window.setOnCloseRequest(e -> window.close());
    }

    //展示图书信息界面
    private Scene setBookScene(){
        //把setOnCloseRequest改为返回主界面
        window.setOnCloseRequest(e -> {
            e.consume();  //把原本的关闭请求取消，否则即使用户选择不关闭，界面还是会被关闭
            closeProgram();
        });
        //声明TableView中的各列
        TableColumn<Book, String> nameColumn = new TableColumn<>("Name");
        nameColumn.setMinWidth(100);
        nameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));

        TableColumn<Book, String> authorColumn = new TableColumn<>("Author");
        authorColumn.setMinWidth(75);
        authorColumn.setCellValueFactory(new PropertyValueFactory<>("author"));

        TableColumn<Book, String> ISBNColumn = new TableColumn<>("ISBN");
        ISBNColumn.setMinWidth(75);
        ISBNColumn.setCellValueFactory(new PropertyValueFactory<>("ISBN"));

        TableColumn<Book, String> publisherColumn = new TableColumn<>("publisher");
        publisherColumn.setMinWidth(60);
        publisherColumn.setCellValueFactory(new PropertyValueFactory<>("publisher"));

        TableColumn<Book, Float> priceColumn = new TableColumn<>("Price");
        priceColumn.setMinWidth(30);
        priceColumn.setCellValueFactory(new PropertyValueFactory<>("price"));

        TableColumn<Book, Float> costColumn = new TableColumn<>("Cost");
        costColumn.setMinWidth(30);
        costColumn.setCellValueFactory(new PropertyValueFactory<>("cost"));

        TableColumn<Book, Float> discountColumn = new TableColumn<>("Discount");
        discountColumn.setMinWidth(20);
        discountColumn.setCellValueFactory(new PropertyValueFactory<>("discount"));

        TableColumn<Book, Integer> stockColumn = new TableColumn<>("Stock");
        stockColumn.setMinWidth(15);
        stockColumn.setCellValueFactory(new PropertyValueFactory<>("stock"));

        //对各个输入框进行声明和一些初始调整
        nameInput = new TextField();
        nameInput.setPromptText("name");  //设置提示信息，下同
        nameInput.setMinWidth(25);

        authorInput = new TextField();
        authorInput.setPromptText("author");
        authorInput.setMinWidth(25);

        ISBNInput = new TextField();
        ISBNInput.setPromptText("ISBN");
        ISBNInput.setMinWidth(25);

        publisherInput = new TextField();
        publisherInput.setPromptText("publisher");
        publisherInput.setMinWidth(25);

        priceInput = new TextField();
        priceInput.setPromptText("price");
        priceInput.setMinWidth(25);

        costInput = new TextField();
        costInput.setPromptText("cost");
        costInput.setMinWidth(25);

        discountInput = new TextField();
        discountInput.setPromptText("discount");
        discountInput.setMinWidth(25);

        //添加和删除的按钮设置以及其相应事件的注册
        Button addButton = new Button("Add");
        addButton.setMinWidth(50);
        addButton.setOnAction(e -> addBookButtonClicked());
        Button deleteButton = new Button("Delete");
        deleteButton.setMinWidth(90);
        deleteButton.setOnAction(e -> deleteBookButtonClicked());

        //摆放输入框和按钮的HBox设置
        HBox hBox = new HBox();
        hBox.setPadding(new Insets(10,10,5,10));
        hBox.setSpacing(10);
        hBox.getChildren().addAll(nameInput,authorInput,ISBNInput,publisherInput,priceInput,costInput,discountInput,addButton,deleteButton);

        //图书信息表格的设置
        bookTable = new TableView<>();
        bookTable.setItems(getBooks());
        bookTable.getColumns().addAll(nameColumn, authorColumn, ISBNColumn, publisherColumn, priceColumn, costColumn, discountColumn, stockColumn);

        //最终界面的设置
        bookLayout = new VBox(10);
        bookLayout.getChildren().addAll(bookTable,hBox);
        bookScene = new Scene(bookLayout,800,475);
        return bookScene;
    }

    //展示交易信息界面
    private Scene setRecordScene(){
        window.setOnCloseRequest(e -> {
            e.consume();
            closeProgram();
        });
        //声明TableView中的各列
        TableColumn<Record, Integer> idColumn = new TableColumn<>("id");
        idColumn.setMinWidth(100);
        idColumn.setCellValueFactory(new PropertyValueFactory<>("id"));

        TableColumn<Record, Integer> typeColumn = new TableColumn<>("Type");
        typeColumn.setMinWidth(100);
        typeColumn.setCellValueFactory(new PropertyValueFactory<>("type"));

        TableColumn<Record, Integer> ISBNColumn = new TableColumn<>("ISBN");
        ISBNColumn.setMinWidth(100);
        ISBNColumn.setCellValueFactory(new PropertyValueFactory<>("ISBN"));

        TableColumn<Record, Integer> countColumn = new TableColumn<>("Count");
        countColumn.setMinWidth(100);
        countColumn.setCellValueFactory(new PropertyValueFactory<>("count"));

        TableColumn<Record, Integer> moneyColumn = new TableColumn<>("Money");
        moneyColumn.setMinWidth(100);
        moneyColumn.setCellValueFactory(new PropertyValueFactory<>("money"));

        //type只有两种合法值，为了防止用户乱填，所以采用下拉选择框的形式
        Label label = new Label("Type:");
        typeChoice = new ChoiceBox<>();
        typeChoice.getItems().addAll(0,1);
        typeChoice.setValue(0);
        //设置输入框
        ISBNInput = new TextField();
        ISBNInput.setPromptText("ISBN");
        ISBNInput.setMinWidth(25);

        countInput = new TextField();
        countInput.setPromptText("Count");
        countInput.setMinWidth(25);
        //设置添加和删除按钮
        Button addButton = new Button("Add");
        addButton.setMinWidth(50);
        addButton.setOnAction(e -> addRecordButtonClicked());
        Button deleteButton = new Button("Delete");
        deleteButton.setMinWidth(90);
        deleteButton.setOnAction(e -> deleteRecordButtonClicked());
        //声明一个HBox来摆放输入框和按钮
        HBox hBox = new HBox();
        hBox.setPadding(new Insets(10,10,10,10));
        hBox.setSpacing(10);
        hBox.getChildren().addAll(label,typeChoice,ISBNInput,countInput,addButton,deleteButton);
        //提示用户如何选择type的信息
        Label prompt = new Label("提示：Type为0表示进货，Type为1表示销售\t\t\t\t\t\t");
        //呈现总交易额和总利润的信息并把这些放到一个HBox中
        amount = new Label("总销售额："+getTotalAmount());
        profit = new Label("总利润："+(getTotalAmount()-getTotalCost()));  //总利润等于总销售额减总成本
        HBox h2 = new HBox();
        h2.setPadding(new Insets(5,10,0,10));
        h2.setSpacing(10);
        h2.getChildren().addAll(prompt,amount,profit);

        //生成数据表
        recordTable = new TableView<>();
        recordTable.setItems(getRecords());
        recordTable.getColumns().addAll(idColumn, typeColumn, ISBNColumn, countColumn, moneyColumn);
        //把各个组件整合到Scene中
        recordLayout = new VBox(10);
        recordLayout.getChildren().addAll(recordTable,h2,hBox);
        recordScene = new Scene(recordLayout, 700,500);
        return recordScene;
    }

    //当删除图书按钮被点击时，把该图书的信息从界面中去除，同时删除数据库中这本书的信息
    private void deleteBookButtonClicked() {
        ObservableList<Book> booksSelected, allBooks;
        allBooks = bookTable.getItems();
        booksSelected = bookTable.getSelectionModel().getSelectedItems();
        //把选中的图书图书从数据库中删除
        for(Book book:booksSelected){
            //让用户确定是否要删除
            Boolean answer = ConfirmBox.display("提示","你确定要删除《"+book.getName()+"》这本书吗？");
            if(!answer){
                return;
            }
            //在数据库中删除图书的信息
            deleteBook(book.getISBN());
        }
        //把tableView中这本图书的信息删除
        booksSelected.forEach(allBooks::remove);
    }

    //当添加图书按钮被点击时，在界面中添加这本书的信息，同时在数据库中也添加这本书的信息
    private void addBookButtonClicked() {
        Book book = new Book();
        Float discount;
        Float price;
        Float cost;

        //利用用户输入的值生成一个新的Book对象
        book.setName(nameInput.getText());
        book.setAuthor(authorInput.getText());
        book.setISBN(ISBNInput.getText());
        book.setPublisher(publisherInput.getText());
        //在用户输入非法值时捕捉异常，并弹窗提示
        try {
            book.setDiscount(Float.parseFloat(discountInput.getText()));
            discount = Float.parseFloat(discountInput.getText());
            book.setDiscount(discount);
        }
        catch (NumberFormatException e){
            AlertBox.display("Error",discountInput.getText()+" is not a float!\nPlease reset it.");
            return;
        }
        try {
            book.setPrice(Float.parseFloat(priceInput.getText()));
            price = Float.parseFloat(priceInput.getText());
            book.setPrice(price);
        }
        catch (NumberFormatException e){
            AlertBox.display("Error",priceInput.getText()+" is not a float!\nPlease reset it.");
            return;
        }
        try {
            book.setCost(Float.parseFloat(costInput.getText()));
            cost = Float.parseFloat(costInput.getText());
            book.setCost(cost);
        }
        catch (NumberFormatException e){
            AlertBox.display("Error",priceInput.getText()+" is not a float!\nPlease reset it.");
            return;
        }
        //库存默认设置为0
        book.setStock(0);
        //在数据库和TableView中插入这本书的信息
        insertBook(book);
        bookTable.getItems().add(book);
        //若添加成功就把输入框清空，为用户下一次的输入做好准备
        nameInput.clear();
        authorInput.clear();
        ISBNInput.clear();
        publisherInput.clear();
        priceInput.clear();
        costInput.clear();
        discountInput.clear();
    }

    //当删除交易记录按钮被点击时，把该交易记录的信息从界面中去除，同时删除数据库中这次交易的信息
    private void deleteRecordButtonClicked() {
        ObservableList<Record> recordsSelected, allRecords;
        int count;
        allRecords = recordTable.getItems();
        recordsSelected = recordTable.getSelectionModel().getSelectedItems();
        for(Record record:recordsSelected){
            //询问用户是否确定要删除该交易记录
            Boolean answer = ConfirmBox.display("提示","你确定要删除这条交易记录吗？");
            if(!answer){
                return;
            }
            //在数据库中删除这条交易记录
            deleteRecord(record.getId());
            //根据删除的交易记录的类型，对相应的图书库存量进行增减
            count = selectBook(record.getISBN()).getStock(); //获取这本书当前的库存
            if(record.getType()==1){
                //如果删除了一条出售图书的记录，则增加相应图书的库存量
                addStock(record, count);
            }
            else if(record.getType()==0){
                //如果删除了一条购买图书的记录，则减少相应图书的库存量
                reduceStock(record, count);
            }
            else {
                AlertBox.display("提示","出现未知错误！");
            }
        }
        //在界面中把这条交易记录删除
        recordsSelected = recordTable.getSelectionModel().getSelectedItems();
        recordsSelected.forEach(allRecords::remove);
        //更新总交易额和总利润
        amount.setText("总交易额："+getTotalAmount());
        profit.setText("总利润："+(getTotalAmount()-getTotalCost()));
    }

    //当添加交易记录按钮被点击时，在界面中添加此次交易的信息，同时在数据库中也添加此次交易的信息
    private void addRecordButtonClicked() {
        Record record = new Record();
        int count;
        float money=0;
        //根据用户输入的ISBN号，从数据库中获取被交易的图书信息
        Book book = selectBook(ISBNInput.getText());
        //如果没有对应的书籍，提示用户先到图书信息界面进行录入
        if(book==null) {
            boolean answer = ConfirmBox.display("提示", "数据库中还没有这本书的信息，\n现在为你跳转到图书信息界面，请先录入图书信息。");
            if(answer)
                window.setScene(setBookScene());
            else
                return;
        }
        //根据用户输入的值和已有的信息，生成一个record对象
        record.setId(connect.selectLastID()+1);
        record.setType(typeChoice.getValue());
        record.setISBN(ISBNInput.getText());
        //检查用户输入的值是否合法，如果不合法会弹窗提示
        try {
            record.setCount(Integer.parseInt(countInput.getText()));
            count = Integer.parseInt(countInput.getText());
        }
        catch (NumberFormatException e){
            AlertBox.display("Error",countInput.getText()+" is not an integer!\nPlease reset it.");
            return;
        }
        //根据交易的类型，对交易金额进行计算
        if(typeChoice.getValue()==0){
            //如果是购买，金额的值为购买数量乘每本书的成本
            money = count * book.getCost();
            addStock(record, book.getStock());
        }
        else if(typeChoice.getValue()==1){
            //如果是出售，先检查库存是否充足，不够的话弹窗提示并取消这条交易记录的插入
            if(count > book.getStock()){
                AlertBox.display("提示","库存不足，交易无效");
                return;
            }
            //库存充足的话，根据图书价格、折扣和出售图书的数量计算金额
            else {
                money = count * book.getPrice() * book.getDiscount() / 10;
                reduceStock(record, book.getStock());
            }
        }
        record.setMoney(money);
        //在数据库中插入这条交易记录
        insertRecord(typeChoice.getValue(),ISBNInput.getText(),count,money);
        //在用户界面中插入交易记录
        recordTable.getItems().add(record);
        //清空输入框，为下一次输入做好准备
        ISBNInput.clear();
        countInput.clear();
        //更新总交易额和总利润值
        amount.setText("总交易额："+getTotalAmount());
        profit.setText("总利润："+(getTotalAmount()-getTotalCost()));
    }

    //获取一个存放在list中的已有交易记录的列表
    public ObservableList<Record> getRecords(){
        ObservableList<Record> records = FXCollections.observableArrayList();
        return selectRecords(records);
    }

    //获取一个存放在list中的已有书籍信息的列表
    public ObservableList<Book> getBooks(){
        ObservableList<Book> books = FXCollections.observableArrayList();
        return selectBooks(books);
    }

    public static void main(String[] args) {
        launch(args);
    }

}
