package gui;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.SwingConstants;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;

import models.Customer;
import models.CustomerList;
//import sms.SendMessage;

public class AdminMainFrame extends JFrame {
    private final CustomerMainFrame customerMainFrame;
    private final CustomerList customerList;
    private final JTable customerListTable;
    private final DefaultTableModel tableModel;

    public AdminMainFrame(CustomerMainFrame customerMainFrame, CustomerList customerList) {
        this.customerMainFrame = customerMainFrame;
        this.customerList = customerList;

        setBounds(100, 100, 1000, 600);
        JPanel contentPane = new JPanel();
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
        tableModel.addColumn("대기번호");


        for (Customer customer : customerMainFrame.getCustomerList()) {
            Object[] rowData = {
                    customer.getNo(),
                    customer.getPhoneNumber(),
                    customer.getPeopleCount(),
                    customer.getState(),
                    customer.getTime(),
                    customer.isMessageDelivered(),
                    customer.getWaitingNumber()
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
                    String newState = changeStateDialog(new String[]{"대기", "입장", "퇴장"});
                    if (newState != null) {
                        Customer customer = customerList.getCustomerList().get(selectedRow);
                        System.out.println(customer.getState());

                        if (customer.getState().equals("대기") && !newState.equals("대기")) {
                            customer.setWaitingNumber(0); // 대기번호 초기화
                            tableModel.setValueAt(0, selectedRow, 6); // 대기번호 열 업데이트
                        }

                        customer.setState(newState);
                        tableModel.setValueAt(newState, selectedRow, 3);
                        showDialog("상태가 업데이트되었습니다");
                        setVisible(true);
                        updateWaitingCount();
                        updateCustomerTable();
                        announceReservation();
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
                    updateWaitingCount();
                    updateCustomerTable();
                    announceReservation();
                }
            }
        });

//        addWindowListener(new WindowAdapter() {
//            @Override
//            public void windowClosed(WindowEvent e) {
//                updateWaitingCount();
//            }
//        });
//
//        updateCustomerTable();
    }

    private void updateWaitingCount() {
        int waitingCount = 0;
        ArrayList<Customer> customers = customerMainFrame.getCustomerList();
        for (Customer customer : customers) {
            if (customer.getState().equals("대기")) {
                waitingCount++;
            }
        }
        customerMainFrame.updateWaitingCount(waitingCount);
    }

    private void updateCustomerTable() {
        ArrayList<Customer> customerList = customerMainFrame.getCustomerList();
        // 기존 데이터 초기화
        tableModel.setRowCount(0);

        int waitingNumber = 1; // 대기번호 초기값 설정

        for (Customer customer : customerList) {
            if (customer.getState().equals("대기")) {
                customer.setWaitingNumber(waitingNumber); // 대기번호 다시 지정
                waitingNumber++; // 대기번호 증가
            }
            Object[] rowData = {
                    customer.getNo(),
                    customer.getPhoneNumber(),
                    customer.getPeopleCount(),
                    customer.getState(),
                    customer.getTime(),
                    customer.isMessageDelivered(),
                    customer.getWaitingNumber(),
                    customer.getWaitingNumber()
            };
            tableModel.addRow(rowData);
        }
    }

    private void showDialog(String str) {
        JDialog dialog = new JDialog(this);
        JOptionPane.showMessageDialog(dialog, str);
        dialog.dispose();
    }

    private String changeStateDialog(String[] options) {
        JDialog dialog = new JDialog(this);
        dialog.setVisible(true);
        String input = (String) JOptionPane.showInputDialog(dialog, "새로운 상태를 선택하세요", "state selection", JOptionPane.PLAIN_MESSAGE, null, options, options[0]);
        dialog.dispose();
        return input;
    }

    // 대기 번호가 3인 고객 데이터 가져오기
    private Customer getCustomerWithWaitingNumber3() {
        for (Customer customer : customerList.getCustomerList()) {
            if (customer.getWaitingNumber() == 3) {
                return customer;
            }
        }
        return null; // 대기 번호가 3인 고객이 없을 경우 null 반환
    }
    private void announceReservation() {
        Customer customer = getCustomerWithWaitingNumber3();
        if (customer != null) {
            String phoneNumber = customer.getPhoneNumber();
            String customerInfo = "전화번호: " + phoneNumber + "\n인원수: " + customer.getPeopleCount() ;
            String text = "\n\n예약이 3팀 남았습니다. \n입장을 준비해주세요";

            // 메시지를 발송
//            SendMessage message= new SendMessage();
//            message.sendOne(phoneNumber,customerInfo+text);
        }
    }

}
