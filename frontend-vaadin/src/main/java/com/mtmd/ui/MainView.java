package com.mtmd.ui;

import com.mtmd.ui.infrastructure.client.ApiException;
import com.mtmd.ui.infrastructure.client.gen.V1IceApi;
import com.mtmd.ui.infrastructure.client.gen.types.Cream;
import com.mtmd.ui.infrastructure.client.gen.types.Ice;
import com.mtmd.ui.infrastructure.client.gen.types.Sorbet;
import com.mtmd.ui.infrastructure.client.gen.types.Water;
import com.vaadin.flow.component.Key;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.dependency.CssImport;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.html.Header;
import com.vaadin.flow.component.listbox.ListBox;
import com.vaadin.flow.component.listbox.MultiSelectListBox;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextArea;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.provider.DataProvider;
import com.vaadin.flow.data.provider.ListDataProvider;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.server.PWA;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.*;
import java.util.stream.Collectors;

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

    Service service;

    TextField name = new TextField("Name");
    ComboBox<String> category = new ComboBox<>("Category");
    MultiSelectListBox<String> ingredientsExisting = new MultiSelectListBox<>();
    TextField ingredients = new TextField("Ingredients");
    TextArea foodintolerances = new TextArea("Food-Intolerance");
    TextField nutrition = new TextField("Nutrition");
    TextField purchasePrice = new TextField("Purchase Price");
    TextField retailPrice = new TextField("Purchase Price");
    Grid<Ice> allIceGrid = new Grid<>();

    List<Ice> items = new ArrayList<>(10);

    /**
     * Construct a new Vaadin view.
     * <p>
     * Build the initial UI state for the user accessing the application.
     *
     */
    public MainView(@Autowired Service service) {
        this.service = service;

        items.addAll(service.loadIce());

        H2 headerNew = new H2("Add new ice-cream");

        name.setHelperText("What kind of ice");
        name.setRequired(true);

        category.setItems("Water", "Sorbet", "Cream");
        category.setHelperText("Please choose a category");
        category.setRequired(true);

        ingredients.setHelperText("Add new Ingredients");

        foodintolerances.setHelperText("Please fill in..");

        nutrition.setHelperText("in kcal / 100g");
        nutrition.setRequired(true);

        purchasePrice.setHelperText("In € / 1 Liter");
        purchasePrice.setRequired(true);

        retailPrice.setHelperText("In € / 1 Liter");
        retailPrice.setRequired(true);

        // Button click listeners can be defined as lambda expressions
        Button button = new Button("Add",
                e -> createIce());
        // Theme variants give you predefined extra styles for components.
        // Example: Primary button has a more prominent look.
        button.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        // You can specify keyboard shortcuts for buttons.
        // Example: Pressing enter in this view clicks the Button.
        button.addClickShortcut(Key.ENTER);

        // Use custom CSS classes to apply styling. This is defined in shared-styles.css.
        addClassName("centered-content");

        H2 headerAll = new H2("All types of ice-cream");


        allIceGrid.setDataProvider(DataProvider.ofCollection(items));
        allIceGrid.addColumn(Ice::getName).setHeader("Name");
        allIceGrid.addColumn(Ice::getCategory).setHeader("Category");

        updateIngredients();

        add(headerNew, name, category, ingredientsExisting, ingredients, foodintolerances, nutrition, purchasePrice, retailPrice, button);
        add(headerAll, allIceGrid);
    }

    private void updateIngredients(){
        // collect all ingredients
        ingredientsExisting.setItems(items.stream().flatMap(i -> i.getIngredients().stream()).collect(Collectors.toSet()));
    }


    // All the following belongs in a service. The view should use a dedicated view-model
    private void createIce(){
        Ice ice;
        switch(category.getValue()){
            case "Water":
                ice = createWaterIce();
                break;
            case "Sorbet":
                ice = createSorbetIce();
                break;
            case "Cream":
                ice = createCreamIce();
                break;
            default:
                throw new IllegalArgumentException("Unsupported Category");
        }
        Optional<Ice> created = service.createIce(ice);
        created.ifPresent(newIce -> items.add(newIce));
        allIceGrid.getDataCommunicator().reset();
        Notification.show("Created");
    }

    private Water createWaterIce(){
        Water ice = createIce(new Water());
        ice.setFlavourAdditive(Collections.emptyList());
        return ice;
    }

    private Sorbet createSorbetIce(){
        Sorbet ice = createIce(new Sorbet());
        ice.setFruitContentInPercent(0);
        ice.setFruits(Collections.emptyList());
        return ice;
    }

    private Cream createCreamIce(){
        Cream ice = createIce(new Cream());
        ice.setCreamInPercent(0);
        return ice;
    }

    private <T extends Ice> T createIce(T ice){
        ice.setName(name.getValue());
        ice.setCategory(category.getValue());
        ice.setNutrients(Integer.valueOf(nutrition.getValue()));
        if(ice.getIngredients() == null){
            ice.setIngredients(new HashSet<>(10));
        }
        ice.getIngredients().addAll(ingredientsExisting.getSelectedItems());
        if(ingredients.getValue() != null && !ingredients.getValue().trim().isBlank()){
            ice.getIngredients().add(ingredients.getValue());
        }
        ice.setPurchasePrice(String.format("%s EUR", purchasePrice.getValue()));
        ice.setRetailPrice(String.format("%s EUR", retailPrice.getValue()));
        return ice;
    }

}
