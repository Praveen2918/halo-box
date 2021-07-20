package com.zuci.halo.views.navbar;

import java.util.Optional;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.ComponentUtil;
import com.vaadin.flow.component.applayout.AppLayout;
import com.vaadin.flow.component.avatar.Avatar;
import com.vaadin.flow.component.dependency.CssImport;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.html.Image;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.tabs.Tab;
import com.vaadin.flow.component.tabs.TabVariant;
import com.vaadin.flow.component.tabs.Tabs;
import com.vaadin.flow.component.tabs.TabsVariant;
import com.vaadin.flow.router.RouterLayout;
import com.vaadin.flow.router.RouterLink;
import com.zuci.halo.views.api.APIView;
import com.zuci.halo.views.dashboard.DashboardView;
import com.zuci.halo.views.pipeline.PipelineView;

@SuppressWarnings("serial")
@CssImport(value = "./themes/halo-box/views/main-view.css")
@CssImport(value = "./themes/halo-box/views/main-view.css", themeFor = "vaadin-tabs")
// @CssImport(value = "./styles/my-grid-styles.css", themeFor = "vaadin-grid")
public class NavBarView extends AppLayout implements RouterLayout {

	private Tabs menu;
	
	public NavBarView() {
		VerticalLayout navbarAndTab = new VerticalLayout();
		HorizontalLayout header = createHeader();
		DashboardView dashboardView = new DashboardView();
		PipelineView pipelineView = new PipelineView();
		APIView apiView = new APIView();
		navbarAndTab.setWidthFull();
		navbarAndTab.setSpacing(false);
		navbarAndTab.setPadding(false);
		navbarAndTab.add(header,dashboardView.dashview());
		menu.addSelectedChangeListener(listener->{
			if(menu.getSelectedIndex() == 0)
			{
				navbarAndTab.removeAll();
				navbarAndTab.add(header,dashboardView.dashview());
			}
			else if(menu.getSelectedIndex() == 1)
			{
				navbarAndTab.removeAll();
				navbarAndTab.add(header,pipelineView.dashview());
			}
			else
			{
				navbarAndTab.removeAll();
				navbarAndTab.add(header,apiView);
			}
		});
		
		
		addToNavbar(navbarAndTab);
		
	}

	private VerticalLayout createTopBar(Tabs menu) {
		VerticalLayout layout = new VerticalLayout();
		layout.setSpacing(false);
		layout.setPadding(false);
		layout.setAlignItems(FlexComponent.Alignment.CENTER);
		layout.add(menu);
		return layout;
	}

	private HorizontalLayout createHeader() {
		HorizontalLayout header = new HorizontalLayout();
		header.getThemeList().add("dark");
		header.setWidthFull();
		header.setPadding(false);
		header.setSpacing(false);
		header.setWidthFull();
		header.setAlignItems(FlexComponent.Alignment.CENTER);
		header.setId("header");
		Image logo = new Image("images/logo.png", "halo-box logo");
		logo.setId("logo");
		header.add(logo);

		Avatar avatar = new Avatar();
		avatar.setId("avatar");
		header.add(new H1("halo-box"));
		menu = createMenuTabs();
		
		header.add(createTopBar(menu));
		header.add(avatar);
		return header;
	}

	private static Tabs createMenuTabs() {
		final Tabs tabs = new Tabs();
		tabs.addThemeVariants(TabsVariant.LUMO_EQUAL_WIDTH_TABS);
		tabs.getStyle().set("width", "-webkit-fill-availables");
		tabs.add(getAvailableTabs());
		return tabs;
	}

	private static Tab[] getAvailableTabs() {
		return new Tab[] { createTab("Dashboard", DashboardView.class), createTab("Pipeline", PipelineView.class),
				createTab("API", APIView.class) };
	}

	private static Tab createTab(String text, Class<? extends Component> navigationTarget) {
		final Tab tab = new Tab();
		tab.addThemeVariants(TabVariant.LUMO_ICON_ON_TOP);
		tab.add(new RouterLink(text, navigationTarget));
		ComponentUtil.setData(tab, Class.class, navigationTarget);
		return tab;
	}

	@Override
	protected void afterNavigation() {
		super.afterNavigation();
		getTabForComponent(getContent()).ifPresent(menu::setSelectedTab);
	}

	private Optional<Tab> getTabForComponent(Component component) {
		return menu.getChildren().filter(tab -> ComponentUtil.getData(tab, Class.class).equals(component.getClass()))
				.findFirst().map(Tab.class::cast);
	}
}
