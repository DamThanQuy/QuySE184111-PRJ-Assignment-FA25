package Models;

public class Customer {
    private int customerID;
    private String contactName;
    private String address;
    private String phone;

    public Customer() {}

    public Customer(int customerID, String contactName, String address, String phone) {
        this.customerID = customerID;
        this.contactName = contactName;
        this.address = address;
        this.phone = phone;
    }

    public int getCustomerID() {
        return customerID;
    }

    public void setCustomerID(int customerID) {
        this.customerID = customerID;
    }

    public String getContactName() {
        return contactName;
    }

    public void setContactName(String contactName) {
        this.contactName = contactName;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }
}
