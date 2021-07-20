package com.zuci.halo.views.main;

import com.vaadin.flow.component.applayout.AppLayout;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.router.RouteAlias;

/**
 * The main view is a top-level placeholder for other views.
 */
@Route(value = "login")
@RouteAlias(value = "")
@PageTitle("Login")
public class MainView extends AppLayout {

	public MainView() {

	}
}
