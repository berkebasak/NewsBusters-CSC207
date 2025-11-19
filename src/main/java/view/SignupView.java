package view;

import interface_adapter.ViewManagerModel;
import interface_adapter.login.LoginViewModel;
import interface_adapter.signup.SignupController;
import interface_adapter.signup.SignupState;
import interface_adapter.signup.SignupViewModel;

import javax.swing.*;
import java.awt.*;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

public class SignupView extends JPanel implements PropertyChangeListener {
    public static final String VIEW_NAME = "signup";

    private final SignupViewModel signupViewModel;
    private SignupController signupController;
    private ViewManagerModel viewManagerModel;

    private final JTextField usernameField = new JTextField(20);
    private final JPasswordField passwordField = new JPasswordField(20);
    private final JPasswordField confirmPasswordField = new JPasswordField(20);
    private final JButton signupButton = new JButton("Sign Up");
    private final JButton cancelButton = new JButton("Cancel");

    private final JLabel messageLabel = new JLabel();

    public SignupView(SignupViewModel signupViewModel) {
        this.signupViewModel = signupViewModel;
        this.signupViewModel.addPropertyChangeListener(this);
        setLayout(new BorderLayout(10, 10));
        setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        JLabel title = new JLabel("Create Account", SwingConstants.CENTER);
        title.setFont(new Font("SansSerif", Font.BOLD, 20));
        add(title, BorderLayout.NORTH);

        JPanel formPanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        gbc.gridx = 0;
        gbc.gridy = 0;
        formPanel.add(new JLabel("Username:"), gbc);
        gbc.gridx = 1;
        formPanel.add(usernameField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 1;
        formPanel.add(new JLabel("Password:"), gbc);
        gbc.gridx = 1;
        formPanel.add(passwordField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 2;
        formPanel.add(new JLabel("Confirm Password:"), gbc);
        gbc.gridx = 1;
        formPanel.add(confirmPasswordField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 3;
        gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.CENTER;
        gbc.insets = new Insets(12, 5, 0, 5);
        gbc.fill = GridBagConstraints.NONE;
        JPanel signupButtonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 0, 0));
        signupButtonPanel.setOpaque(false);
        signupButtonPanel.add(signupButton);
        formPanel.add(signupButtonPanel, gbc);

        add(formPanel, BorderLayout.CENTER);

        JPanel messagePanel = new JPanel(new BorderLayout());
        messagePanel.setOpaque(false);
        messageLabel.setForeground(Color.RED);
        messageLabel.setHorizontalAlignment(SwingConstants.CENTER);
        messageLabel.setText(" ");
        messageLabel.setBorder(BorderFactory.createEmptyBorder(4, 0, 4, 0));
        messagePanel.add(messageLabel, BorderLayout.CENTER);

        JPanel cancelPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 8, 0));
        cancelPanel.setOpaque(false);
        cancelPanel.add(cancelButton);

        JPanel bottomPanel = new JPanel(new BorderLayout());
        bottomPanel.setOpaque(false);
        bottomPanel.add(messagePanel, BorderLayout.CENTER);
        bottomPanel.add(cancelPanel, BorderLayout.SOUTH);
        add(bottomPanel, BorderLayout.SOUTH);

        signupButton.addActionListener(e -> {
            if (signupController != null) {
                signupController.signup(
                        usernameField.getText(),
                        new String(passwordField.getPassword()),
                        new String(confirmPasswordField.getPassword())
                );
            }
        });

        cancelButton.addActionListener(e -> {
            clearForm();
            if (viewManagerModel != null) {
                viewManagerModel.changeView(LoginViewModel.VIEW_NAME);
            }
        });
    }

    public void setSignupController(SignupController signupController) {
        this.signupController = signupController;
    }

    public void setViewManagerModel(ViewManagerModel viewManagerModel) {
        this.viewManagerModel = viewManagerModel;
    }

    private void clearForm() {
        usernameField.setText("");
        passwordField.setText("");
        confirmPasswordField.setText("");
    }

    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        SignupState state = signupViewModel.getState();
        if ("success".equals(evt.getPropertyName())) {
            JOptionPane.showMessageDialog(
                    this,
                    state.getMessage() != null ? state.getMessage() : "Account created successfully.",
                    "Signup Successful",
                    JOptionPane.INFORMATION_MESSAGE
            );
            messageLabel.setText(" ");
            clearForm();
            return;
        }

        if (state.getError() != null) {
            messageLabel.setForeground(Color.RED);
            messageLabel.setText(state.getError());
        } else if (state.getMessage() != null) {
            messageLabel.setForeground(new Color(0, 128, 0));
            messageLabel.setText(state.getMessage());
        } else {
            messageLabel.setText(" ");
        }
    }
}
