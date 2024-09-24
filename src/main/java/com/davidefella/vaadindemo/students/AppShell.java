package com.davidefella.vaadindemo.students;

import com.vaadin.flow.component.page.AppShellConfigurator;
import com.vaadin.flow.theme.Theme;
import com.vaadin.flow.theme.material.Material;
import org.springframework.stereotype.Component;

@Component
@Theme(themeClass = Material.class)
public class AppShell implements AppShellConfigurator {
    // Qui puoi aggiungere configurazioni aggiuntive se necessario
}