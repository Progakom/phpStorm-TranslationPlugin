package stocrm.kotlin;

import com.intellij.openapi.options.Configurable;
import org.jetbrains.annotations.Nls;

import javax.swing.*;

import org.jetbrains.annotations.Nullable;

/**
 * Provides controller functionality for application settings.
 */
public class AppSettingsConfigurable implements Configurable {

    private AppSettingsComponent mySettingsComponent;

    // A default constructor with no arguments is required because this implementation
    // is registered as an applicationConfigurable EP

    @Nls(capitalization = Nls.Capitalization.Title)
    @Override
    public String getDisplayName() {
        return "Translation Plugin Settings";
    }

    @Override
    public JComponent getPreferredFocusedComponent() {
        return mySettingsComponent.getPreferredFocusedComponent();
    }

    @Nullable
    @Override
    public JComponent createComponent() {
        mySettingsComponent = new AppSettingsComponent();
        return mySettingsComponent.getPanel();
    }

    @Override
    public boolean isModified() {
        AppSettingsState settings = AppSettingsState.getInstance();
        boolean modified = !mySettingsComponent.getUserNameText().equals(settings.userId);
        return modified;
    }

    @Override
    public void apply() {
        AppSettingsState settings = AppSettingsState.getInstance();
        settings.userId = mySettingsComponent.getUserNameText();
    }

    @Override
    public void reset() {
        AppSettingsState settings = AppSettingsState.getInstance();
        mySettingsComponent.setUserNameText(settings.userId);
    }

    @Override
    public void disposeUIResources() {
        mySettingsComponent = null;
    }

}