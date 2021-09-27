package Books;

public class Record {
    int id;
    int type;
    String ISBN;
    int count;
    float money;

    public Record(){}

    public Record(int id, int type, String ISBN, int count, float money) {
        this.id = id;
        this.type = type;
        this.ISBN = ISBN;
        this.count = count;
        this.money = money;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getISBN() {
        return ISBN;
    }

    public void setISBN(String ISBN) {
        this.ISBN = ISBN;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public float getMoney() {
        return money;
    }

    public void setMoney(float money) {
        this.money = money;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }
}
