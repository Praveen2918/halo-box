package com.zuci.halo.views.model;

import com.vaadin.flow.component.applayout.AppLayout;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.zuci.halo.views.dashboard.DashboardView;

@Route(value = "/dashboard/model-quality-report", layout = DashboardView.class)
@PageTitle("ModelQualityReport")
public class ModelQualityReportsView extends AppLayout {

	public ModelQualityReportsView() {

	}
}
