package gui;

import models.Customer;
import models.CustomerList;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Timestamp;
import java.util.ArrayList;

public class CustomerMainFrame extends JFrame {
    private JLabel titleLabel;
    private JLabel phoneNumberLabel;
    private JLabel countLabel;
    private JTextField phoneNumberTextField;
    private JTextField countTextField;
    private JButton submitButton;
    private JTextArea waitingNumberArea;
    private CustomerList customerList;
    private JButton adminPageButton;
    private AdminMainFrame adminFrame;

    private static boolean isAdminLoggedIn = false; // admin 페이지 로그인 성공 여부
    private int nextWaitingNumber = 0;
    private int waitingCountNumber;
    
    /* TODO : 현재 웨이팅 팀 수 보여주기
            : 전화번호 11자리 입력 후에 인원 수 입력되도록 설정하기
     */

    // constructor
    public CustomerMainFrame() {
        customerList = new CustomerList();

        setTitle("웨이팅 프로그램");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(500, 400);
        setLocationRelativeTo(null);
        setLayout(null);

        titleLabel = new JLabel("식당 웨이팅");

        // phoneNumber, count
        phoneNumberLabel = new JLabel("전화번호");
        phoneNumberTextField = new JTextField(15);
        countLabel = new JLabel("인원 수");
        countTextField = new JTextField(5);

        JPanel buttonPanel = new JPanel(new GridLayout(4, 3, 5, 5));

        for (int i = 1; i <= 9; i++) {
            JButton numberButton = new JButton(Integer.toString(i));
            numberButton.addActionListener(new NumberButtonActionListener());
            buttonPanel.add(numberButton);
        }

        JButton zeroButton = new JButton("0");
        zeroButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                phoneNumberTextField.setText(phoneNumberTextField.getText() + "0");
            }
        });
        buttonPanel.add(zeroButton);

        // submit button
        submitButton = new JButton("등록");
        submitButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.out.println("등록 버튼 클릭");
                addCustomer();
                System.out.println("Customer 추가 완료");
            }
        });

        // admin page button
        adminPageButton = new JButton("Admin");
        adminPageButton.addActionListener(e -> {
            System.out.println("관리자 페이지 이동");
            showAdminLoginDialog();
        });

        // 위치, 크기 설정
        titleLabel.setBounds(200, 10, 100, 20);
        buttonPanel.setBounds(20,40,200,200);
        phoneNumberLabel.setBounds(240,40,80,30);
        phoneNumberTextField.setBounds(300, 40, 150, 30);
        countLabel.setBounds(240, 80, 80, 30);
        countTextField.setBounds(300, 80, 150, 30);
        submitButton.setBounds(240,150,80,30);
        adminPageButton.setBounds(20, 300, 80, 30);

        // component 추가
        add(titleLabel);
        add(buttonPanel);
        add(phoneNumberLabel);
        add(phoneNumberTextField);
        add(countLabel);
        add(countTextField);
        add(submitButton);
        add(adminPageButton);

    }

    private void addCustomer() {
        String phoneNumber = phoneNumberTextField.getText().trim();
        int count = Integer.parseInt(countTextField.getText().trim());

        if (!phoneNumber.isEmpty()) {
            long nowDate = System.currentTimeMillis();
            int waitingNumber = nextWaitingNumber++;
            Customer customer = new Customer
                    (waitingNumber, phoneNumber, count, "대기", new Timestamp(nowDate), false);
            customerList.addCustomer(customer);
            phoneNumberTextField.setText("");
            countTextField.setText("");
        }
    }

    private void showAdminLoginDialog() {
        AdminLoginDialog adminLoginDialog = new AdminLoginDialog(this);
        adminLoginDialog.setVisible(true);

        if (isAdminLoggedIn) {
            showAdminPage();
        }
    }
    
    void showAdminPage() {
        adminFrame = new AdminMainFrame(this);
        adminFrame.setVisible(true);
    }

    static void setAdminLoggedIn(boolean loggedIn) {
        isAdminLoggedIn = loggedIn;
    }

    public ArrayList<Customer> getCustomerList() {
        return customerList.getCustomerList();
    }

    private class NumberButtonActionListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            JButton sourceButton = (JButton) e.getSource();
            String buttonText = sourceButton.getText();
            phoneNumberTextField.setText
                    (phoneNumberTextField.getText() + buttonText);
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            CustomerMainFrame customerFrame = new CustomerMainFrame();
            customerFrame.setVisible(true);
        });
    }
}
