package view;

import interface_adapter.ViewManagerModel;
import interface_adapter.login.LoginController;
import interface_adapter.login.LoginState;
import interface_adapter.login.LoginViewModel;
import javax.swing.*;
import java.awt.*;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

public class LoginView extends JPanel implements PropertyChangeListener {
    public static final String VIEW_NAME = LoginViewModel.VIEW_NAME;

    private final LoginViewModel loginViewModel;
    private LoginController loginController;
    private ViewManagerModel viewManagerModel;

    private final JTextField usernameField = new JTextField(20);
    private final JPasswordField passwordField = new JPasswordField(20);
    private final JButton loginButton = new JButton("Login");
    private final JButton signupButton = new JButton("Create Account");
    private final JLabel messageLabel = new JLabel();

    public LoginView(LoginViewModel loginViewModel) {
        this.loginViewModel = loginViewModel;
        this.loginViewModel.addPropertyChangeListener(this);

        setLayout(new BorderLayout(10, 10));
        setBorder(BorderFactory.createEmptyBorder(20, 30, 20, 30));

        JLabel title = new JLabel("Login to NewsBusters", SwingConstants.CENTER);
        title.setFont(new Font("SansSerif", Font.BOLD, 22));
        add(title, BorderLayout.NORTH);

        JPanel formPanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(6, 6, 6, 6);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.weightx = 0;
        gbc.fill = GridBagConstraints.NONE;
        formPanel.add(new JLabel("Username:"), gbc);

        gbc.gridx = 1;
        gbc.weightx = 0;
        gbc.fill = GridBagConstraints.NONE;
        usernameField.setColumns(16);
        formPanel.add(usernameField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.weightx = 0;
        gbc.fill = GridBagConstraints.NONE;
        formPanel.add(new JLabel("Password:"), gbc);

        gbc.gridx = 1;
        gbc.weightx = 0;
        gbc.fill = GridBagConstraints.NONE;
        passwordField.setColumns(16);
        formPanel.add(passwordField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.weightx = 0;
        gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.CENTER;
        gbc.fill = GridBagConstraints.NONE;
        gbc.insets = new Insets(12, 6, 0, 6);
        JPanel loginButtonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 0, 0));
        loginButtonPanel.setOpaque(false);
        loginButtonPanel.add(loginButton);
        formPanel.add(loginButtonPanel, gbc);

        add(formPanel, BorderLayout.CENTER);

        JPanel messagePanel = new JPanel(new BorderLayout());
        messagePanel.setOpaque(false);
        messageLabel.setForeground(Color.RED);
        messageLabel.setHorizontalAlignment(SwingConstants.CENTER);
        messageLabel.setText(" ");
        messagePanel.add(messageLabel, BorderLayout.CENTER);

        JPanel createAccountPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 0, 0));
        createAccountPanel.setOpaque(false);
        createAccountPanel.add(signupButton);

        JPanel bottomPanel = new JPanel();
        bottomPanel.setOpaque(false);
        bottomPanel.setLayout(new BoxLayout(bottomPanel, BoxLayout.Y_AXIS));
        messagePanel.setAlignmentX(Component.CENTER_ALIGNMENT);
        createAccountPanel.setAlignmentX(Component.CENTER_ALIGNMENT);
        bottomPanel.add(messagePanel);
        bottomPanel.add(Box.createVerticalStrut(8));
        bottomPanel.add(createAccountPanel);
        add(bottomPanel, BorderLayout.SOUTH);

        loginButton.addActionListener(e -> {
            if (loginController != null) {
                loginController.login(
                        usernameField.getText(),
                        new String(passwordField.getPassword())
                );
            }
        });

        signupButton.addActionListener(e -> {
            if (viewManagerModel != null) {
                viewManagerModel.changeView(SignupView.VIEW_NAME);
            }
        });
    }

    public void setLoginController(LoginController loginController) {
        this.loginController = loginController;
    }

    public void setViewManagerModel(ViewManagerModel viewManagerModel) {
        this.viewManagerModel = viewManagerModel;
    }

    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        String propertyName = evt.getPropertyName();
        LoginState state = loginViewModel.getState();

        if ("reset".equals(propertyName)) {
            clearFields();
            messageLabel.setForeground(Color.RED);
            messageLabel.setText(" ");
            return;
        }

        if (state.getError() != null) {
            messageLabel.setForeground(Color.RED);
            messageLabel.setText(state.getError());
        } else if (state.getMessage() != null) {
            messageLabel.setForeground(new Color(0, 128, 0));
            messageLabel.setText(state.getMessage());
            clearFields();
        } else {
            messageLabel.setText(" ");
        }
    }

    private void clearFields() {
        usernameField.setText("");
        passwordField.setText("");
    }
}
