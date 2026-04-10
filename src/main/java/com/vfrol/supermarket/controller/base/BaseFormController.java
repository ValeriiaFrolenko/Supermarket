package com.vfrol.supermarket.controller.base;

import com.vfrol.supermarket.controller.util.AsyncRunner;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import net.synedra.validatorfx.Validator;

public abstract class BaseFormController<C, D> extends BaseModalController {

    protected final Validator validator = new Validator();
    protected boolean isEditMode = false;

    @FXML protected VBox formPanel;
    @FXML protected Label titleLabel;
    @FXML protected Button saveButton;

    protected abstract String getEntityName();
    protected abstract void setupValidation();
    protected abstract void populateFields(D dto);
    protected abstract C buildDTO();
    protected abstract void saveEntity(C dto);

     @FXML
    public void initialize() {
        titleLabel.setText("Add " + getEntityName());
        setupValidation();
        if (saveButton != null) {
            saveButton.disableProperty().bind(validator.containsErrorsProperty());
        }
    }

    public void setupForEdit(D dto) {
        this.isEditMode = true;
        titleLabel.setText("Edit " + getEntityName());
        populateFields(dto);
    }

    @FXML
    public void onSave() {
        C dto = buildDTO();
        AsyncRunner.runAsync(
                () -> saveEntity(dto),
                this::closeForm,
                formPanel
        );
    }

    @FXML
    public void onCancel() {
        closeForm();
    }

    protected void closeForm() {
        closeWindow(formPanel);
    }
}