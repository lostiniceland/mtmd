package com.mtmd.ui;

import com.mtmd.ui.infrastructure.client.ApiException;
import com.mtmd.ui.infrastructure.client.gen.V1IceApi;
import com.mtmd.ui.infrastructure.client.gen.types.IceType;
import com.vaadin.flow.component.Key;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.dependency.CssImport;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.html.Header;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextArea;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.server.PWA;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Collections;
import java.util.List;

/**
 * A sample Vaadin view class.
 * <p>
 * To implement a Vaadin view just extend any Vaadin component and
 * use @Route annotation to announce it in a URL as a Spring managed
 * bean.
 * Use the @PWA annotation make the application installable on phones,
 * tablets and some desktop browsers.
 * <p>
 * A new instance of this class is created for every new user and every
 * browser tab/window.
 */
@Route
@PWA(name = "Vaadin Application",
        shortName = "Ice App",
        description = "Your favorite Ice-Cream",
        enableInstallPrompt = false)
@CssImport("./styles/shared-styles.css")
@CssImport(value = "./styles/vaadin-text-field-styles.css", themeFor = "vaadin-text-field")
public class MainView extends VerticalLayout {

    /**
     * Construct a new Vaadin view.
     * <p>
     * Build the initial UI state for the user accessing the application.
     *
     */
    public MainView(@Autowired Service service) {

        H2 headerNew = new H2("Add new ice-cream");

        TextField name = new TextField("Name");
        name.setHelperText("What kind of ice");
        name.setRequired(true);

        ComboBox<String> category = new ComboBox<>("Category");
        category.setItems("Water", "Sorbet", "Cream");
        category.setHelperText("Please choose a category");
        category.setRequired(true);

        TextArea foodintolerances = new TextArea("Food-Intolerance");
        foodintolerances.setHelperText("Please fill in..");

        TextField nutrition = new TextField("Nutrition");
        nutrition.setHelperText("in kcal / 100g");
        nutrition.setRequired(true);

        TextField purchasePrice = new TextField("Purchase Price");
        purchasePrice.setHelperText("In € / 1 Liter");
        purchasePrice.setRequired(true);

        TextField retailPrice = new TextField("Purchase Price");
        retailPrice.setHelperText("In € / 1 Liter");
        retailPrice.setRequired(true);

        // Button click listeners can be defined as lambda expressions
        Button button = new Button("Add",
                e -> Notification.show("Not Yet Implemented!"));
        // Theme variants give you predefined extra styles for components.
        // Example: Primary button has a more prominent look.
        button.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        // You can specify keyboard shortcuts for buttons.
        // Example: Pressing enter in this view clicks the Button.
        button.addClickShortcut(Key.ENTER);

        // Use custom CSS classes to apply styling. This is defined in shared-styles.css.
        addClassName("centered-content");

        H2 headerAll = new H2("All types of ice-cream");

        Grid<IceType> allIceGrid = new Grid<>();
        allIceGrid.setItems(service.loadIce());
        allIceGrid.addColumn(IceType::getName).setHeader("Name");
        allIceGrid.addColumn(i -> i.getCategory().getName()).setHeader("Category");


        add(headerNew, name, category, foodintolerances, nutrition, purchasePrice, retailPrice, button);
        add(headerAll, allIceGrid);
    }

}
