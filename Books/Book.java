package Books;

public class Book {
    String name;        //书名
    String author;      //作者名
    String ISBN;        //ISBN号
    String publisher;   //出版社
    float price;        //定价
    float cost;         //进货价
    float discount = 10;//折扣
    int stock;          //库存量

    public Book(){}

    public Book(String name, String author, String ISBN, String publisher, float price, float cost, float discount, int stock){
        this.name = name;
        this.author = author;
        this.ISBN = ISBN;
        this.publisher = publisher;
        this.price = price;
        this.cost = cost;
        this.discount = discount;
        this.stock = stock;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getAuthor() {
        return author;
    }

    public void setPublisher(String publisher) {
        this.publisher = publisher;
    }

    public String getPublisher() {
        return publisher;
    }

    public void setISBN(String ISBN) {
        this.ISBN = ISBN;
    }

    public String getISBN() {
        return ISBN;
    }

    public void setPrice(float price) {
        this.price = price;
    }

    public float getPrice() {
        return price;
    }

    public void setDiscount(float discount) {
        this.discount = discount;
    }

    public float getDiscount() {
        return discount;
    }

    public void setStock(int stock) {
        this.stock = stock;
    }

    public int getStock() {
        return stock;
    }

    public void setCost(float cost) {
        this.cost = cost;
    }

    public float getCost() {
        return cost;
    }

}
