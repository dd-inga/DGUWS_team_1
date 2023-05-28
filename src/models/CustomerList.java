package models;

import java.util.ArrayList;

public class CustomerList {
    private ArrayList<Customer> customers;

    public CustomerList() {
        customers = new ArrayList<>();
    }

    public void addCustomer(Customer customer) {
        customers.add(customer);
    }

    public ArrayList<Customer> getCustomerList() {
        return customers;
    }
}