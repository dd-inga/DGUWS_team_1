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
    private int waitingCount = 0;
    private JLabel waitingLabel;
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
        phoneNumberTextField = new JTextField(11);
        countLabel = new JLabel("인원 수");
        countTextField = new JTextField(5);
        waitingLabel = new JLabel("웨이팅 수: " + waitingCount);
        waitingLabel.setBounds(240, 200, 100, 30);
        add(waitingLabel);

        JPanel buttonPanel = new JPanel(new GridLayout(4, 3, 5, 5));

        for (int i = 1; i <= 9; i++) {
            JButton numberButton = new JButton(Integer.toString(i));
            // numberButton.setBackground(Color.WHITE);
            // numberButton.setFont(new Font("맑은 고딕", Font.BOLD, 16));
            numberButton.addActionListener(new NumberButtonActionListener());
            buttonPanel.add(numberButton);
        }

        JButton zeroButton = new JButton("0");
        // zeroButton.setBackground(Color.WHITE);
        // zeroButton.setFont(new Font("맑은 고딕", Font.BOLD, 16));
        zeroButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (phoneNumberTextField.getText().length() < 11) {
                    phoneNumberTextField.setText(phoneNumberTextField.getText() + "0");
                } else {
                    countTextField.setText(countTextField.getText() + "0");
                }
            }
        });

        // delete button
        JButton deleteButton = new JButton("Del");
        // deleteButton.setBackground(Color.WHITE);
        // deleteButton.setFont(new Font("맑은 고딕", Font.BOLD, 16));
        deleteButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String currentCount = countTextField.getText();
                if (!currentCount.isEmpty()) {
                    countTextField.setText(currentCount.substring(0, currentCount.length() - 1));
                } else {
                    String phoneNumberText = phoneNumberTextField.getText();
                    if (!phoneNumberText.isEmpty()) {
                        phoneNumberTextField.setText(phoneNumberText.substring(0, phoneNumberText.length() - 1));
                    }
                }
            }
        });

        phoneNumberTextField.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                countTextField.requestFocus();
            }
        });

        buttonPanel.add(zeroButton);
        buttonPanel.add(deleteButton);

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
        adminPageButton = new JButton("관리자 모드");
        adminPageButton.addActionListener(e -> {
            System.out.println("관리자 페이지 이동");
            showAdminLoginDialog();
        });

        // 위치, 크기 설정
        titleLabel.setBounds(200, 10, 100, 20);
        buttonPanel.setBounds(20, 60, 200, 200);
        phoneNumberLabel.setBounds(240, 60, 80, 30);
        phoneNumberTextField.setBounds(300, 60, 150, 30);
        countLabel.setBounds(240, 100, 150, 30);
        countTextField.setBounds(300, 100, 150, 30);
        submitButton.setBounds(240, 170, 80, 30);
        adminPageButton.setBounds(20, 300, 100, 30);

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

            Customer customer = new Customer(waitingNumber, phoneNumber, count, "대기", new Timestamp(nowDate), false);

            customerList.addCustomer(customer);
            phoneNumberTextField.setText("");
            countTextField.setText("");

            waitingCount++;
            waitingLabel.setText("웨이팅 수: " + waitingCount);
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
        adminFrame = new AdminMainFrame(this, customerList);
        adminFrame.setVisible(true);
    }

    static void setAdminLoggedIn(boolean loggedIn) {
        isAdminLoggedIn = loggedIn;
    }

    public void updateWaitingCount(int count) {
        waitingCount = count;
        waitingLabel.setText("웨이팅 수: " + waitingCount);
    }

    public ArrayList<Customer> getCustomerList() {
        return customerList.getCustomerList();
    }

    private class NumberButtonActionListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            JButton sourceButton = (JButton) e.getSource();
            String buttonText = sourceButton.getText();
            if (phoneNumberTextField.getText().length() < 11) {
                phoneNumberTextField.setText(phoneNumberTextField.getText() + buttonText);
            } else {
                String currentCount = countTextField.getText();
                countTextField.setText(currentCount + buttonText);
            }
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            CustomerMainFrame customerFrame = new CustomerMainFrame();
            customerFrame.setVisible(true);
        });
    }
}
