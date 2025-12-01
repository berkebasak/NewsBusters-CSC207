package view;

import interface_adapter.ViewManagerModel;

import javax.swing.*;
import java.awt.*;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

public class ViewManager implements PropertyChangeListener {
    private static final Dimension LOGIN_SIZE = new Dimension(720, 520);
    private static final Dimension DEFAULT_APP_SIZE = new Dimension(1500, 1000);

    private final CardLayout cardLayout;
    private final JPanel views;
    private final ViewManagerModel viewManagerModel;
    private JFrame hostFrame;

    public ViewManager(JPanel views, CardLayout cardLayout, ViewManagerModel viewManagerModel) {
        this.views = views;
        this.cardLayout = cardLayout;
        this.viewManagerModel = viewManagerModel;
        this.viewManagerModel.addPropertyChangeListener(this);
    }

    public void setHostFrame(JFrame hostFrame) {
        this.hostFrame = hostFrame;
        this.hostFrame.setSize(LOGIN_SIZE);
    }

    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        if ("state".equals(evt.getPropertyName())) {
            String viewName = (String) evt.getNewValue();
            cardLayout.show(views, viewName);

            if (hostFrame != null) {
                SwingUtilities.invokeLater(() -> {
                    Dimension targetSize;
                    if (LoginView.VIEW_NAME.equals(viewName) || SignupView.VIEW_NAME.equals(viewName)) {
                        targetSize = LOGIN_SIZE;
                    } else {
                        targetSize = DEFAULT_APP_SIZE;
                    }
                    if (!hostFrame.getSize().equals(targetSize)) {
                        hostFrame.setSize(targetSize);
                        hostFrame.setLocationRelativeTo(null);
                    }
                });
            }
        }
    }
}
