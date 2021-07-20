package com.zuci.halo.views.pipeline;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.ComponentUtil;
import com.vaadin.flow.component.applayout.AppLayout;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.tabs.Tab;
import com.vaadin.flow.component.tabs.TabVariant;
import com.vaadin.flow.component.tabs.Tabs;
import com.vaadin.flow.component.tabs.TabsVariant;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.router.RouterLayout;
import com.vaadin.flow.router.RouterLink;
import com.zuci.halo.views.data.DataQualityReportsView;
import com.zuci.halo.views.features.FeatureQualityReportsView;
import com.zuci.halo.views.model.ModelQualityReportsView;
import com.zuci.halo.views.navbar.NavBarView;
import com.zuci.halo.views.x.XView;

@Route(value = "pipeline", layout = NavBarView.class)
@PageTitle("Pipeline")
public class PipelineView extends AppLayout implements RouterLayout {

	private Tabs menu;

	public PipelineView() {
		
	}
	public HorizontalLayout dashview() {
		HorizontalLayout header = new HorizontalLayout();
		header.setWidthFull();
		menu = createMenuTabs();
		header.add(createTabMenuBar(menu));
		return header;
	}

	private VerticalLayout createTabMenuBar(Tabs menu) {
		VerticalLayout layout = new VerticalLayout();
		layout.setSpacing(false);
		layout.setPadding(false);
		layout.setAlignItems(FlexComponent.Alignment.CENTER);
		layout.add(menu);
		return layout;
	}

	private static Tabs createMenuTabs() {
		final Tabs tabs = new Tabs(false);
		tabs.addThemeVariants(TabsVariant.LUMO_EQUAL_WIDTH_TABS);
		tabs.getStyle().set("width", "-webkit-fill-availables");
		tabs.add(getAvailableTabs());
		return tabs;
	}

	private static Tab[] getAvailableTabs() {
		return new Tab[] { createTab("X", XView.class),
				createTab("Y", FeatureQualityReportsView.class),
				createTab("Z", ModelQualityReportsView.class) };
	}

	private static Tab createTab(String text, Class<? extends Component> navigationTarget) {
		final Tab tab = new Tab();
		tab.addThemeVariants(TabVariant.LUMO_ICON_ON_TOP);
		tab.add(new RouterLink(text, navigationTarget));
		ComponentUtil.setData(tab, Class.class, navigationTarget);
		return tab;
	}

	/*@Override
	protected void afterNavigation() {
		super.afterNavigation();
		getTabForComponent(getContent()).ifPresent(menu::setSelectedTab);
	}

	private Optional<Tab> getTabForComponent(Component component) {
		return menu.getChildren().filter(tab -> ComponentUtil.getData(tab, Class.class).equals(component.getClass()))
				.findFirst().map(Tab.class::cast);
	}*/

}
