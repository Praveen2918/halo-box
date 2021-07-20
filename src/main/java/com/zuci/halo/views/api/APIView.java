package com.zuci.halo.views.api;

import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.zuci.halo.views.navbar.NavBarView;

@Route(value = "api", layout = NavBarView.class)
@PageTitle("API")
public class APIView extends HorizontalLayout {

	public APIView() {

	}

}
