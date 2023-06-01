package gui;

import models.Customer;
import models.CustomerList;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

public class AdminMainFrame extends JFrame {
    private CustomerMainFrame customerMainFrame;
    private CustomerList customerList;
    private JTextArea adminTextArea;
    private JPanel contentPane;
    private JTable customerListTable;
    private DefaultTableModel tableModel;

    public AdminMainFrame(CustomerMainFrame customerMainFrame, CustomerList customerList) {
        this.customerMainFrame = customerMainFrame;
        this.customerList = customerList;

        setBounds(100, 100, 1000, 600);
        contentPane = new JPanel();
        contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
        setContentPane(contentPane);

        setTitle("admin page");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);

        // table 설정
        tableModel = new DefaultTableModel();
        tableModel.addColumn("No");
        tableModel.addColumn("Phone Number");
        tableModel.addColumn("Count");
        tableModel.addColumn("State");
        tableModel.addColumn("Time");
        tableModel.addColumn("Message send");

        for (Customer customer : customerMainFrame.getCustomerList()) {
            Object[] rowData = {
                    customer.getNo(),
                    customer.getPhoneNumber(),
                    customer.getPeopleCount(),
                    customer.getState(),
                    customer.getTime(),
                    customer.isMessageDelivered()
            };
            tableModel.addRow(rowData);
        }

        customerListTable = new JTable(tableModel);
        JScrollPane scrollPane = new JScrollPane(customerListTable);
        contentPane.setLayout(new BorderLayout());
        contentPane.add(scrollPane, BorderLayout.CENTER);

        JLabel label = new JLabel("고객 명단");
        label.setHorizontalAlignment(SwingConstants.CENTER);
        label.setBounds(5, 5, 780, 40);
        contentPane.add(label, BorderLayout.NORTH);

        scrollPane.setBounds(5, 45, 780, 220);
        contentPane.add(scrollPane);

        // button 추가
        JButton refreshButton = new JButton("refresh");
        JButton sendMessageButton = new JButton("send message");
        JButton updateStateButton = new JButton("update state");
        JButton deleteButton = new JButton("delete");

        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new FlowLayout());
        buttonPanel.add(refreshButton);
        buttonPanel.add(sendMessageButton);
        buttonPanel.add(updateStateButton);
        buttonPanel.add(deleteButton);

        contentPane.add(buttonPanel, BorderLayout.SOUTH);

        // 새로 고침
        refreshButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                showDialog("새로고침 되었습니다");
                updateCustomerTable();
                AdminMainFrame frame = new AdminMainFrame(customerMainFrame, customerList);
                setVisible(false);
                new AdminMainFrame(customerMainFrame, customerList).setVisible(true);

            }
        });

        // 문자 전송
        sendMessageButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int selectedRow = customerListTable.getSelectedRow();
                if (selectedRow == -1) {
                    showDialog("고객을 선택해주세요");
                } else {
                    Customer customer = customerList.getCustomerList().get(selectedRow);
                    if (customer.getState().equals("대기")) {
                        customer.setMessageDelivered(true);
                        tableModel.setValueAt(true, selectedRow, 5);
                        showDialog("문자를 발송하였습니다");
                    } else {
                        showDialog("문자는 대기 상태일 때에만 전송 가능합니다");
                    }
                }
            }
        });

        // 상태 변환 버튼
        updateStateButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int selectedRow = customerListTable.getSelectedRow();
                System.out.println(selectedRow);

                if (selectedRow == -1) {
                    showDialog("고객을 선택해주세요");
                } else {
                    String newState = changeStateDialog("새로운 상태를 입력하세요");
                    if (newState != null) {
                        Customer customer = customerList.getCustomerList().get(selectedRow);
                        System.out.println(customer.getState());

                        customer.setState(newState);
                        tableModel.setValueAt(newState, selectedRow, 3);
                        showDialog("상태가 업데이트되었습니다");
                        setVisible(true);
                    }
                }
            }
        });

        // 삭제 버튼
        deleteButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int selectedRow = customerListTable.getSelectedRow();
                if (selectedRow == -1) {
                    showDialog("고객을 선택해주세요");
                } else {
                    Customer customer = customerList.getCustomerList().get(selectedRow);
                    customerList.deleteCustomer(customer);
                    updateCustomerTable();
                    showDialog("삭제 완료되었습니다");
                    setVisible(true);
                }
            }
        });

        updateCustomerTable();
    }

    private void updateCustomerTable() {
        ArrayList<Customer> customerList = customerMainFrame.getCustomerList();
        // 기존 데이터 초기화
        tableModel.setRowCount(0);

        for (Customer customer : customerList) {
            Object[] rowData = {
                    customer.getNo(),
                    customer.getPhoneNumber(),
                    customer.getPeopleCount(),
                    customer.getState(),
                    customer.getTime(),
                    customer.isMessageDelivered()
            };
            tableModel.addRow(rowData);
        }
    }

    private void showDialog(String str) {
        JDialog dialog = new JDialog(this);
        JOptionPane.showMessageDialog(dialog, str);
        dialog.dispose();
    }

    private String changeStateDialog(String str) {
        JDialog dialog = new JDialog(this);
        dialog.setVisible(true);
        String input = JOptionPane.showInputDialog(dialog, str);
        dialog.dispose();
        return input;
    }

}
