package com.zuci.halo.views.x;

import com.vaadin.flow.component.applayout.AppLayout;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.router.RouterLayout;
import com.zuci.halo.views.navbar.NavBarView;
import com.zuci.halo.views.pipeline.PipelineView;

@Route(value = "/pipeline/xview",layout = NavBarView.class)
@PageTitle("XViewReport")
public class XView extends AppLayout implements RouterLayout{

	public XView() {
		
		// TODO Auto-generated constructor stub
	}
	
	

}
