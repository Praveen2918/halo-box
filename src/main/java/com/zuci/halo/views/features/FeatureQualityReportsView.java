package com.zuci.halo.views.features;

import com.vaadin.flow.component.applayout.AppLayout;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.zuci.halo.views.dashboard.DashboardView;

@Route(value = "/dashboard/feature-quality-report", layout = DashboardView.class)
@PageTitle("FeatureQualityReport")
public class FeatureQualityReportsView extends AppLayout  {

	public FeatureQualityReportsView() {

	}
}
