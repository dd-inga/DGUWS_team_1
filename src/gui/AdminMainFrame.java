package gui;

import models.Customer;

import java.sql.Timestamp;
import java.util.Vector;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.*;

public class AdminMainFrame extends JFrame {
    private JPanel contentPane;
    private JTable table;

    // table param
    public static Vector<String> getCustomerName() {
        Vector<String> columnName = new Vector<>();
        columnName.add("NO");
        columnName.add("PHONE NUMBER");
        columnName.add("PEOPLE COUNT");
        columnName.add("STATE");
        columnName.add("TIME");
        columnName.add("SEND MESSAGE");
        columnName.add("DELETE");
        return columnName;
    }

    public static void main(String[] args) {
        AdminMainFrame frame = new AdminMainFrame();
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }

    public AdminMainFrame() {
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setBounds(100, 100, 800, 400);
        contentPane = new JPanel();
        contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
        setContentPane(contentPane);
        setLocationRelativeTo(null);    // 가운데 정렬

        Vector<String> columnName = getCustomerName();
        Vector<Customer> customerData = new Vector<>();

        long nowDate = System.currentTimeMillis();
        Customer c1 = new Customer();
        c1.setNo(0);
        c1.setPhoneNumber("010-1234-5678");
        c1.setPeopleCount(3);
        c1.setTime(new Timestamp(nowDate));
        c1.setState("대기중");

        Customer c2 = new Customer();
        c2.setNo(1);
        c2.setPhoneNumber("010-4567-8901");
        c2.setPeopleCount(5);
        c2.setTime(new Timestamp(nowDate));
        c2.setState("대기중");

        customerData.add(c1);
        customerData.add(c2);
        DefaultTableModel tableModel = new DefaultTableModel(columnName, 0);

        for (Customer customer : customerData) {
            Vector<Object> row = new Vector<>();
            row.addElement(customer.getNo());
            row.addElement(customer.getPhoneNumber());
            row.addElement(customer.getPeopleCount());
            row.addElement(customer.getState());
            row.addElement(customer.getTime());
            // TODO : sendMessage, delete button 추가
            tableModel.addRow(row);
        }

        table = new JTable(tableModel);
        contentPane.setLayout(null);

        DefaultTableCellRenderer tableCellRenderer = new DefaultTableCellRenderer();
        tableCellRenderer.setHorizontalAlignment(SwingConstants.CENTER);

        TableColumnModel columnModel = table.getColumnModel();

        for (int i = 0; i < columnModel.getColumnCount(); i++) {
            columnModel.getColumn(i).setCellRenderer(tableCellRenderer);
        }

        JLabel label = new JLabel("대기 명단");
        label.setHorizontalAlignment(SwingConstants.CENTER);
        label.setBounds(5, 5, 780, 40);
        contentPane.add(label);

        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.setBounds(5, 45, 780, 320);
        contentPane.add(scrollPane);
    }

}
