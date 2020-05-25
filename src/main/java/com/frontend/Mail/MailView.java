package com.frontend.Mail;

import com.backend.Mail;
import com.frontend.MainView;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.notification.NotificationVariant;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextArea;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.Route;

import static com.vaadin.flow.component.orderedlayout.FlexComponent.Alignment.CENTER;

@Route(value = "announcement", layout = MainView.class)
public class MailView extends VerticalLayout {
    
    TextField subjectField = new TextField();
    TextArea  bodyField    = new TextArea();
    Button    sendButton   = saveButton();
    
    public MailView() {
        VerticalLayout itemsLayout = new VerticalLayout();
        itemsLayout.setWidth("50%");
        itemsLayout.setJustifyContentMode(JustifyContentMode.CENTER);
        itemsLayout.setAlignItems(CENTER);
        
        subjectField.setPlaceholder("Subject");
        bodyField.setPlaceholder("Body");
        
        subjectField.setWidthFull();
        bodyField.setWidthFull();
        bodyField.setHeight("30em");
        
        itemsLayout.addAndExpand(subjectField, bodyField, sendButton);
        setJustifyContentMode(JustifyContentMode.CENTER);
        setAlignItems(CENTER);
        add(itemsLayout);
    }
    
    public Button saveButton() {
        Button sendButton = new Button("Send");
        sendButton.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        
        sendButton.setIcon(VaadinIcon.ARROW_FORWARD.create());
        sendButton.setIconAfterText(true);
        
        sendButton.addClickListener(buttonClickEvent -> {
            if (subjectField.getValue() == null || subjectField.getValue().isEmpty()) {
                Notification error = new Notification();
                error.setText("Subject can not be empty");
                error.addThemeVariants(NotificationVariant.LUMO_ERROR);
                error.setDuration(5000);
                error.open();
//            } else if (bodyField.getValue() == null || bodyField.getValue().isEmpty()) {
//                Notification error = new Notification();
//                error.setText("Body can not be empty");
//                error.addThemeVariants(NotificationVariant.LUMO_ERROR);
//                error.setDuration(5000);
//                error.open();
            } else {
                Mail.sendMessage(subjectField.getValue(), bodyField.getValue());
                
                Notification success = new Notification();
                success.setText("Body can not be empty");
                success.addThemeVariants(NotificationVariant.LUMO_SUCCESS);
                success.setDuration(5000);
                success.open();
            }
        });
        return sendButton;
    }
}
