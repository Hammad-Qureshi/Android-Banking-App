package sh.surge.hammad.bankmobile.Databases;

public class UserHelper {

    String name, username, password, phone, balance, cardNo, cvc;

    public UserHelper() {}

//    public UserHelper(String name, String username, String password, String phone, long balance, long cardNo, int cvc) {
//        this.name = name;
//        this.username = username;
//        this.password = password;
//        this.phone = phone;
//        this.balance = balance;
//        this.cardNo = cardNo;
//        this.cvc = cvc;
//    }

    public UserHelper(String name, String username, String password, String phone, String balance, String cardNo, String cvc) {
        this.name = name;
        this.username = username;
        this.password = password;
        this.phone = phone;
        this.balance = balance;
        this.cardNo = cardNo;
        this.cvc = cvc;
    }

//    public String getName() {
//        return name;
//    }
//
//    public void setName(String name) {
//        this.name = name;
//    }
//
//    public String getUsername() {
//        return username;
//    }
//
//    public void setUsername(String username) {
//        this.username = username;
//    }
//
//    public String getPassword() {
//        return password;
//    }
//
//    public void setPassword(String password) {
//        this.password = password;
//    }
//
//    public String getPhone() {
//        return phone;
//    }
//
//    public void setPhone(String phone) {
//        this.phone = phone;
//    }
//
//    public long getBalance() {
//        return balance;
//    }
//
//    public void setBalance(long balance) {
//        this.balance = balance;
//    }
//
//    public long getCardNo() {
//        return cardNo;
//    }
//
//    public void setCardNo(long cardNo) {
//        this.cardNo = cardNo;
//    }
//
//    public int getCvc() {
//        return cvc;
//    }
//
//    public void setCvc(int cvc) {
//        this.cvc = cvc;
//    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getBalance() {
        return balance;
    }

    public void setBalance(String balance) {
        this.balance = balance;
    }

    public String getCardNo() {
        return cardNo;
    }

    public void setCardNo(String cardNo) {
        this.cardNo = cardNo;
    }

    public String getCvc() {
        return cvc;
    }

    public void setCvc(String cvc) {
        this.cvc = cvc;
    }
}
