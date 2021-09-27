package Operation;

import Books.Book;
import Books.Record;
import JavaFX.AlertBox;
import javafx.collections.ObservableList;

import java.sql.*;

public class connect {
    //JDBC 驱动名及数据库 URL
    static final String JDBC_DRIVER = "com.mysql.cj.jdbc.Driver";
    static final String DB_URL = "jdbc:mysql://localhost:3306/bookstore?useSSL=false&allowPublicKeyRetrieval=true&serverTimezone=UTC";

    // 数据库的用户名与密码
    static final String USER = "root";
    static final String PASS = "357159";

    //初始化数据库，插入一些数据以便调试
    public static void initialize(){
        Connection conn = null;
        PreparedStatement stmt = null;
        try {
            // 注册 JDBC 驱动
            Class.forName(JDBC_DRIVER);

            // 打开链接
            System.out.println("初始化...");
            conn = DriverManager.getConnection(DB_URL, USER, PASS);

            //删除books表中原有的内容
            String sql = "DELETE FROM books";
            stmt = conn.prepareStatement(sql);
            stmt.executeUpdate();
            //删除records表中原有的内容
            sql = "DELETE FROM records";
            stmt = conn.prepareStatement(sql);
            stmt.executeUpdate();
            //在两个表中插入一系列的值
            sql = "INSERT INTO books\n" +
                    "VALUES  (\"挪威的森林\",\"村上春树\",\"9787532725694\",\"上海译文出版社\",18.8,9.4,7.5,100),\n" +
                    "\t\t(\"国境以南 太阳以西\",\"村上春树\",\"9787532726745\",\"上海译文出版社\",13.5,5.5,7.5,20),\n" +
                    "        (\"舞！舞！舞！\",\"村上春树\",\"9787532728893\",\"上海译文出版社\",25,11.5,7.5,10),\n" +
                    "        (\"100天后会死的鳄鱼君\",\"菊池祐纪\",\"9787544868570\",\"接力出版社\",45,20,9,100),\n" +
                    "        (\"巴别塔\",\" A. S. 拜厄特\",\"9787532171965\",\"上海文艺出版社\",148,50,7.5,50),\n" +
                    "        (\"当我谈跑步时我谈些什么\",\"村上春树\",\"9787544242820\",\"南海出版公司\",25,12.5,7.5,20)";
            stmt = conn.prepareStatement(sql);
            stmt.executeUpdate();
            sql = "INSERT INTO records\n" +
                    "(type, ISBN, count, money)\n" +
                    "VALUES\n" +
                    "(0,\"9787532725694\",100,940),\n" +
                    "(0,\"9787532726745\",20,110),\n" +
                    "(0,\"9787532728893\",10,115),\n" +
                    "(0,\"9787544868570\",100,2000),\n" +
                    "(0,\"9787532171965\",50,2500),\n" +
                    "(0,\"9787544242820\",20,250)";
            stmt = conn.prepareStatement(sql);
            stmt.executeUpdate();

            // 完成后关闭
            stmt.close();
            conn.close();
        } catch (SQLException se) {
            // 处理 JDBC 错误
            se.printStackTrace();
        } catch (Exception e) {
            // 处理 Class.forName 错误
            e.printStackTrace();
        } finally {
            // 关闭资源
            try {
                if (stmt != null) stmt.close();
            } catch (SQLException se2) {
            }// 什么都不做
            try {
                if (conn != null) conn.close();
            } catch (SQLException se) {
                se.printStackTrace();
            }
        }
    }

    //获取当前最后一个交易记录的id，用于计算后续插入的交易记录的id值
    public static int selectLastID(){
        Connection conn = null;
        Statement stmt = null;
        int id = 0;
        try {
            // 注册 JDBC 驱动
            Class.forName(JDBC_DRIVER);

            // 打开链接
            System.out.println("查找最后一个ID值...");
            conn = DriverManager.getConnection(DB_URL, USER, PASS);

            stmt = conn.createStatement();
            //选择数据库中当前值最大的ID，由于ID是一个自增列，所以最大的ID就是最后一个ID
            String sql = "SELECT id FROM records ORDER BY id DESC LIMIT 1";
            ResultSet rs = stmt.executeQuery(sql);
            if(rs.next())
                id = rs.getInt("id");

            // 完成后关闭
            rs.close();
            stmt.close();
            conn.close();
        } catch (SQLException se) {
            // 处理 JDBC 错误
            se.printStackTrace();
        } catch (Exception e) {
            // 处理 Class.forName 错误
            e.printStackTrace();
        } finally {
            // 关闭资源
            try {
                if (stmt != null) stmt.close();
            } catch (SQLException se2) {
            }// 什么都不做
            try {
                if (conn != null) conn.close();
            } catch (SQLException se) {
                se.printStackTrace();
            }
            return id;
        }
    }

    //获取数据库中所有交易记录的信息，储存在一个ObservableList中
    public static ObservableList selectRecords(ObservableList<Record> records){
        Connection conn = null;
        Statement stmt = null;
        Record record;
        try {
            // 注册 JDBC 驱动
            Class.forName(JDBC_DRIVER);

            // 打开链接
            System.out.println("查找所有交易记录...");
            conn = DriverManager.getConnection(DB_URL, USER, PASS);

            stmt = conn.createStatement();
            //获取数据库中所有的交易记录
            String sql = "SELECT * FROM records";
            ResultSet rs = stmt.executeQuery(sql);
            //往list中添加所有数据库中的交易记录
            while(rs.next()){
                records.add(new Record(rs.getInt("id"), rs.getInt("type"), rs.getString("ISBN"), rs.getInt("count")
                        , rs.getFloat("money")));
            }

            // 完成后关闭
            rs.close();
            stmt.close();
            conn.close();
        } catch (SQLException se) {
            // 处理 JDBC 错误
            se.printStackTrace();
        } catch (Exception e) {
            // 处理 Class.forName 错误
            e.printStackTrace();
        } finally {
            // 关闭资源
            try {
                if (stmt != null) stmt.close();
            } catch (SQLException se2) {
            }// 什么都不做
            try {
                if (conn != null) conn.close();
            } catch (SQLException se) {
                se.printStackTrace();
            }
            return records;
        }
    }

    //往数据库中插入一条交易记录
    public static void insertRecord(int type, String ISBN, int count, float money) {
        Connection conn = null;
        PreparedStatement stmt = null;
        try {
            // 注册 JDBC 驱动
            Class.forName(JDBC_DRIVER);

            // 打开链接
            System.out.println("插入交易记录...");
            conn = DriverManager.getConnection(DB_URL, USER, PASS);

            //根据给定的数据，插入一条交易记录。ID是自增列，所以不用设定值。
            String sql = "INSERT INTO records (type, ISBN, count, money) VALUES(?, ?, ?, ?) ";
            stmt = conn.prepareStatement(sql);
            stmt.setInt(1,type);
            stmt.setString(2,ISBN);
            stmt.setInt(3,count);
            stmt.setFloat(4,money);
            //result代表受影响的行数，大于0表示更新成功
            int result = stmt.executeUpdate();
            //根据语句执行的结果，弹窗提示用户插入是否成功
            if(result > 0)
                AlertBox.display("提示","成功插入交易记录！");
            else AlertBox.display("提示","在插入交易记录时出现了错误!");

            // 完成后关闭
            stmt.close();
            conn.close();
        } catch (SQLException se) {
            // 处理 JDBC 错误
            se.printStackTrace();
        } catch (Exception e) {
            // 处理 Class.forName 错误
            e.printStackTrace();
        } finally {
            // 关闭资源
            try {
                if (stmt != null) stmt.close();
            } catch (SQLException se2) {
            }// 什么都不做
            try {
                if (conn != null) conn.close();
            } catch (SQLException se) {
                se.printStackTrace();
            }
        }
    }

    //根据交易id删除数据库中的一条交易记录
    public static void deleteRecord(int id){
        Connection conn = null;
        PreparedStatement stmt = null;
        try {
            // 注册 JDBC 驱动
            Class.forName(JDBC_DRIVER);

            // 打开链接
            System.out.println("删除交易记录...");
            conn = DriverManager.getConnection(DB_URL, USER, PASS);

            //根据给定的ID，删除一条交易记录
            String sql = "DELETE FROM records WHERE id = ?";
            stmt = conn.prepareStatement(sql);
            stmt.setInt(1, id);
            stmt.executeUpdate();

            AlertBox.display("提示","成功删除交易记录");

            // 完成后关闭
            stmt.close();
            conn.close();
        } catch (SQLException se) {
            // 处理 JDBC 错误
            se.printStackTrace();
        } catch (Exception e) {
            // 处理 Class.forName 错误
            e.printStackTrace();
        } finally {
            // 关闭资源
            try {
                if (stmt != null) stmt.close();
            } catch (SQLException se2) {
            }// 什么都不做
            try {
                if (conn != null) conn.close();
            } catch (SQLException se) {
                se.printStackTrace();
            }
        }
    }

    //获取当前总的进货成本
    public static float getTotalCost(){
        Connection conn = null;
        Statement stmt = null;
        float cost = 0;
        try {
            // 注册 JDBC 驱动
            Class.forName(JDBC_DRIVER);

            // 打开链接
            System.out.println("计算总交易额...");
            conn = DriverManager.getConnection(DB_URL, USER, PASS);

            stmt = conn.createStatement();
            //选择所有的购买记录，即type为0的记录的金额
            String sql = "SELECT money FROM records WHERE type=0";
            ResultSet rs = stmt.executeQuery(sql);
            //把这些金额加总，得到总成本
            while(rs.next()){
                cost += rs.getFloat(1);
            }

            // 完成后关闭
            rs.close();
            stmt.close();
            conn.close();
        } catch (SQLException se) {
            // 处理 JDBC 错误
            se.printStackTrace();
        } catch (Exception e) {
            // 处理 Class.forName 错误
            e.printStackTrace();
        } finally {
            // 关闭资源
            try {
                if (stmt != null) stmt.close();
            } catch (SQLException se2) {
            }// 什么都不做
            try {
                if (conn != null) conn.close();
            } catch (SQLException se) {
                se.printStackTrace();
            }
            return cost;
        }
    }

    //获取当前总的交易额
    public static float getTotalAmount(){
        Connection conn = null;
        Statement stmt = null;
        float amount = 0;
        try {
            // 注册 JDBC 驱动
            Class.forName(JDBC_DRIVER);

            // 打开链接
            System.out.println("计算总交易额...");
            conn = DriverManager.getConnection(DB_URL, USER, PASS);

            //把所有出售图书的交易记录的金额加总，得到总交易额
            stmt = conn.createStatement();
            String sql = "SELECT money FROM records WHERE type=1";
            ResultSet rs = stmt.executeQuery(sql);
            while(rs.next()){
                amount += rs.getFloat(1);
            }

            // 完成后关闭
            rs.close();
            stmt.close();
            conn.close();
        } catch (SQLException se) {
            // 处理 JDBC 错误
            se.printStackTrace();
        } catch (Exception e) {
            // 处理 Class.forName 错误
            e.printStackTrace();
        } finally {
            // 关闭资源
            try {
                if (stmt != null) stmt.close();
            } catch (SQLException se2) {
            }// 什么都不做
            try {
                if (conn != null) conn.close();
            } catch (SQLException se) {
                se.printStackTrace();
            }
            return amount;
        }
    }

    //获取数据库中所有图书的信息，储存在一个ObservableList中
    public static ObservableList selectBooks(ObservableList<Book> books){
        Connection conn = null;
        Statement stmt = null;
        Book book;
        try {
            // 注册 JDBC 驱动
            Class.forName(JDBC_DRIVER);

            // 打开链接
            System.out.println("查找全部书籍...");
            conn = DriverManager.getConnection(DB_URL, USER, PASS);

            stmt = conn.createStatement();
            //获取数据库中所有图书的信息
            String sql = "SELECT * FROM books";
            ResultSet rs = stmt.executeQuery(sql);
            //把这些信息插入到list中
            while(rs.next()){
                books.add(new Book(rs.getString("name"), rs.getString("author"), rs.getString("ISBN"), rs.getString("publisher")
                        , rs.getFloat("price"), rs.getFloat("cost"), rs.getFloat("discount"), rs.getInt("stock")));
            }

            // 完成后关闭
            rs.close();
            stmt.close();
            conn.close();
        } catch (SQLException se) {
            // 处理 JDBC 错误
            se.printStackTrace();
        } catch (Exception e) {
            // 处理 Class.forName 错误
            e.printStackTrace();
        } finally {
            // 关闭资源
            try {
                if (stmt != null) stmt.close();
            } catch (SQLException se2) {
            }// 什么都不做
            try {
                if (conn != null) conn.close();
            } catch (SQLException se) {
                se.printStackTrace();
            }
            return books;
        }
    }

    //根据ISBN号返回一个Book对象
    public static Book selectBook(String ISBN){
        Connection conn = null;
        PreparedStatement stmt = null;
        Book book = null;
        try {
            // 注册 JDBC 驱动
            Class.forName(JDBC_DRIVER);

            // 打开链接
            System.out.println("查找书籍...");
            conn = DriverManager.getConnection(DB_URL, USER, PASS);

            //根据给定的ISBN号，查询数据库中这本书的信息，如果没有则book的值为null
            String sql = "SELECT * FROM books WHERE ISBN = ?";
            stmt = conn.prepareStatement(sql);
            stmt.setString(1,ISBN);
            ResultSet rs = stmt.executeQuery();
            if(rs.next()){
                book= new Book(rs.getString("name"), rs.getString("author"), rs.getString("ISBN"), rs.getString("publisher")
                        , rs.getFloat("price"), rs.getFloat("cost"), rs.getFloat("discount"), rs.getInt("stock"));
            }

            // 完成后关闭
            rs.close();
            stmt.close();
            conn.close();
        } catch (SQLException se) {
            // 处理 JDBC 错误
            se.printStackTrace();
        } catch (Exception e) {
            // 处理 Class.forName 错误
            e.printStackTrace();
        } finally {
            // 关闭资源
            try {
                if (stmt != null) stmt.close();
            } catch (SQLException se2) {
            }// 什么都不做
            try {
                if (conn != null) conn.close();
            } catch (SQLException se) {
                se.printStackTrace();
            }
            return book;
        }
    }

    //往数据库中插入一条图书信息
    public static void insertBook(Book book){
        Connection conn = null;
        PreparedStatement stmt = null;
        try {
            // 注册 JDBC 驱动
            Class.forName(JDBC_DRIVER);

            // 打开链接
            System.out.println("插入书籍...");
            conn = DriverManager.getConnection(DB_URL, USER, PASS);

            //根据给定的数据，往数据库中插入一条图书信息
            String sql = "INSERT INTO books VALUES(?, ?, ?, ?, ?, ?, ?, ?) ";
            stmt = conn.prepareStatement(sql);
            stmt.setString(1,book.getName());
            stmt.setString(2,book.getAuthor());
            stmt.setString(3,book.getISBN());
            stmt.setString(4,book.getPublisher());
            stmt.setFloat(5,book.getPrice());
            stmt.setFloat(6,book.getCost());
            stmt.setFloat(7,book.getDiscount());
            stmt.setInt(8,book.getStock());
            //result代表受影响的行数，大于0表示更新成功
            int result = stmt.executeUpdate();
            //根据语句执行的结果，弹窗提示用户插入是否成功
            if(result > 0)
                AlertBox.display("提示","成功插入书籍！");
            else AlertBox.display("提示","在插入书籍的过程中出现了错误！");

            // 完成后关闭
            stmt.close();
            conn.close();
        } catch (SQLException se) {
            // 处理 JDBC 错误
            se.printStackTrace();
        } catch (Exception e) {
            // 处理 Class.forName 错误
            e.printStackTrace();
        } finally {
            // 关闭资源
            try {
                if (stmt != null) stmt.close();
            } catch (SQLException se2) {
            }// 什么都不做
            try {
                if (conn != null) conn.close();
            } catch (SQLException se) {
                se.printStackTrace();
            }
        }
    }

    //根据ISBN号删除一条图书信息
    public static void deleteBook(String ISBN){
        Connection conn = null;
        PreparedStatement stmt = null;
        try {
            // 注册 JDBC 驱动
            Class.forName(JDBC_DRIVER);

            // 打开链接
            System.out.println("删除书籍...");
            conn = DriverManager.getConnection(DB_URL, USER, PASS);

            //根据给定的ISBN号，从数据库中删除相应的书籍信息
            String sql = "DELETE FROM books WHERE ISBN = ?";
            stmt = conn.prepareStatement(sql);
            stmt.setString(1,ISBN);
            stmt.executeUpdate();

            AlertBox.display("提示","成功删除书籍！");

            // 完成后关闭
            stmt.close();
            conn.close();
        } catch (SQLException se) {
            // 处理 JDBC 错误
            se.printStackTrace();
        } catch (Exception e) {
            // 处理 Class.forName 错误
            e.printStackTrace();
        } finally {
            // 关闭资源
            try {
                if (stmt != null) stmt.close();
            } catch (SQLException se2) {
            }// 什么都不做
            try {
                if (conn != null) conn.close();
            } catch (SQLException se) {
                se.printStackTrace();
            }
        }
    }

    //根据购买的图书数量对相应图书的库存进行增加
    public static void addStock(Record record, int count){
        Connection conn = null;
        PreparedStatement pstmt = null;
        PreparedStatement stmt = null;
        try {
            // 注册 JDBC 驱动
            Class.forName(JDBC_DRIVER);

            // 打开链接
            System.out.println("修改库存...");
            conn = DriverManager.getConnection(DB_URL, USER, PASS);

            //根据ISBN号来确定要修改库存的图书，并把这本书的库存值进行更新
            String sql = "UPDATE books SET stock = ? WHERE ISBN = ?";
            pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1,count + record.getCount());
            pstmt.setString(2,record.getISBN());
            //result代表受影响的行数，大于0表示更新成功
            int result = pstmt.executeUpdate();
            //弹窗提示运行是否成功
            if(result>0){
                AlertBox.display("提示","更新图书库存信息成功！");
            }
            else {
                AlertBox.display("提示","在更新图书库存信息时出现了错误！");
            }

            // 完成后关闭
            pstmt.close();
            conn.close();
        } catch (SQLException se) {
            // 处理 JDBC 错误
            se.printStackTrace();
        } catch (Exception e) {
            // 处理 Class.forName 错误
            e.printStackTrace();
        } finally {
            // 关闭资源
            try {
                if (stmt != null) stmt.close();
            } catch (SQLException se2) {
            }// 什么都不做
            try {
                if (conn != null) conn.close();
            } catch (SQLException se) {
                se.printStackTrace();
            }
        }
    }

    //根据出售的图书数量对相应图书的库存进行削减
    public static void reduceStock(Record record, int count){
        Connection conn = null;
        PreparedStatement pstmt = null;
        PreparedStatement stmt = null;

        try {
            // 注册 JDBC 驱动
            Class.forName(JDBC_DRIVER);

            // 打开链接
            System.out.println("修改库存...");
            conn = DriverManager.getConnection(DB_URL, USER, PASS);

            //根据ISBN号确定要更新库存的图书，并更新其库存数量
            String sql = "UPDATE books SET stock=? WHERE ISBN=?";
            pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1,count - record.getCount());
            pstmt.setString(2,record.getISBN());
            //result代表受影响的行数，大于0表示更新成功
            int result = pstmt.executeUpdate();
            //弹窗提示运行是否成功
            if(result>0){
                AlertBox.display("提示","更新图书库存信息成功");
            }
            else {
                AlertBox.display("提示","在更新图书库存信息时出现了错误");
            }

            // 完成后关闭
            pstmt.close();
            conn.close();
        } catch (SQLException se) {
            // 处理 JDBC 错误
            se.printStackTrace();
        } catch (Exception e) {
            // 处理 Class.forName 错误
            e.printStackTrace();
        } finally {
            // 关闭资源
            try {
                if (stmt != null) stmt.close();
            } catch (SQLException se2) {
            }// 什么都不做
            try {
                if (conn != null) conn.close();
            } catch (SQLException se) {
                se.printStackTrace();
            }
        }
    }

}

