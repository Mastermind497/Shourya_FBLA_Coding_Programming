package com.backend;

import com.vaadin.flow.component.textfield.EmailField;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.data.binder.Binder.BindingBuilder;
import com.vaadin.flow.data.binder.Validator;
import com.vaadin.flow.function.SerializablePredicate;

import java.util.ArrayList;
import java.util.List;

/**
 * A Class Which allows validating in Emails. This is used to make sure
 * the '@' Symbol is in every email that is entered
 */
public class ValidEmailField extends EmailField {

    /**
     * The content in the field
     */
    private final Content content = new Content();
    /**
     * Allows Validation of Text
     */
    private final Binder<Content> binder = new Binder<>();
    /**
     * All of the validators that the field has to use when validating if an input is allowed.
     * <p>
     * This includes things such as length, content (contains @), et cetera
     */
    private final List<Validator<String>> validators = new ArrayList<>();

    /**
     * Changes the default constructor to make sure that the bean is never null
     */
    public ValidEmailField() {
        binder.setBean(content);
    }

    /**
     * Adds a validator to the list of validators
     *
     * @param predicate    The condition that must be met
     * @param errorMessage The Error Message if the condition is not met
     */
    public void addValidator(
            SerializablePredicate<String> predicate,
            String errorMessage) {
        addValidator(Validator.from(predicate, errorMessage));
    }

    /**
     * Adds a validator to the list of validators
     *
     * @param validator A {@link Validator}, which includes condition that must be met and
     *                  an error message if the condition is not met
     */
    public void addValidator(Validator<String> validator) {
        validators.add(validator);
        build();
    }

    /**
     * Checks to see if all the validations are met
     */
    private void build() {
        BindingBuilder<Content, String> builder =
                binder.forField(this);

        for (Validator<String> v : validators) {
            builder.withValidator(v);
        }

        builder.bind(
                Content::getContent, Content::setContent);
    }

    /**
     * This Class controls the content stored in the email field, such as the email
     * someone types in
     */
    class Content {
        String content;

        public String getContent() {
            return content;
        }

        public void setContent(String content) {
            this.content = content;
        }
    }
}
