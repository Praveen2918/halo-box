package com.zuci.halo.views.data;

import java.io.File;
import java.io.IOException;
import java.util.concurrent.atomic.AtomicInteger;

import org.apache.commons.io.FileUtils;
import org.json.JSONException;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.storedobject.chart.AbstractColor;
import com.storedobject.chart.BarChart;
import com.storedobject.chart.Border;
import com.storedobject.chart.CategoryData;
import com.storedobject.chart.Data;
import com.storedobject.chart.DataMatrix;
import com.storedobject.chart.DataType;
import com.storedobject.chart.DonutChart;
import com.storedobject.chart.LineChart;
import com.storedobject.chart.NightingaleRoseChart;
import com.storedobject.chart.Position;
import com.storedobject.chart.RectangularCoordinate;
import com.storedobject.chart.SOChart;
import com.storedobject.chart.Size;
import com.storedobject.chart.Toolbox;
import com.storedobject.chart.XAxis;
import com.storedobject.chart.YAxis;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.applayout.AppLayout;
import com.vaadin.flow.component.dependency.CssImport;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.router.RouterLayout;
import com.zuci.halo.views.navbar.NavBarView;

@Route(value = "/dashboard/data-quality-report", layout = NavBarView.class)
@CssImport(value = "/styles/views/DataQualityReportView/font-styles.css")
@PageTitle("DataQualityReport")
public class DataQualityReportsView extends AppLayout implements RouterLayout {

	// relative root path where all inputs for charts are present
	private final String mainPath = "./Inputs/DataQualityReport";

	// root layout which has all titles and charts
	private final VerticalLayout mainlayout;

	public DataQualityReportsView() throws JSONException, IOException {
		String input = mainPath + "/meta-data.json";
		// Reading meta-data.json into a JsonObject , where meta-data has folder names
		// as keys and file names inside the folder as JsonArray value
		JsonObject metadata = readJsonFile(input);
		mainlayout = readDataToBuildChart(metadata);
		setContent(mainlayout);
	}

	// Function to read any .json file in the specified path passed as parameter
	private JsonObject readJsonFile(String path) throws JSONException, IOException {

		File file = new File(path);
		String content = FileUtils.readFileToString(file, "utf-8");
		JsonParser parser = new JsonParser();
		JsonElement rootNode = parser.parse(content);
		JsonObject object = null;
		if (rootNode.isJsonObject()) {
			object = rootNode.getAsJsonObject();
		}
		return object;
	}

	// Function to iterate over the meta-data and read the data in files of all
	// folders for every iteration
	private VerticalLayout readDataToBuildChart(JsonObject metadata) {
		VerticalLayout allcharts = new VerticalLayout();
		metadata.entrySet().forEach(entry -> {
			JsonArray data = readFilesInFolder(entry.getKey(), entry.getValue());
			allcharts.add(buildchart(entry.getKey(), data));
		});
		return allcharts;
	}

	// Function to read all files in a specified folder and covert into JsonArray
	// consisting of all JsonObjects
	private JsonArray readFilesInFolder(String key, JsonElement value) {
		JsonArray dataArray = new JsonArray();
		value.getAsJsonArray().forEach(item -> {
			String filename = item.getAsString();
			String filepath = mainPath + "/" + key + "/" + filename;
			try {
				JsonObject dataObj = readJsonFile(filepath);
				dataArray.add(dataObj);
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		});
		return dataArray;
	}

	// Takes the JsonArray of JsonObjects of files in folder and builds the layouts
	// for chart
	private VerticalLayout buildchart(String heading, JsonArray chartdata) {

		VerticalLayout mainchartLayout = new VerticalLayout();
		mainchartLayout.add(addheading(heading), layoutsForchart(chartdata));
		return mainchartLayout;
	}
	
	
	private Component addheading(String heading) {
		HorizontalLayout headingLayout = new HorizontalLayout();
		heading = heading.replace('_', ' ');
		Label title = new Label(heading.toUpperCase());
		headingLayout.setWidthFull();
		title.addClassName("heading-label");
		headingLayout.setJustifyContentMode(FlexComponent.JustifyContentMode.CENTER);
		headingLayout.add(title);
		return headingLayout;
	}

	private Component layoutsForchart(JsonArray chartdata) {
		VerticalLayout titleAndChart = new VerticalLayout();
		titleAndChart.setWidthFull();
		int size = chartdata.size();
		int i = 0;
		int chartsInRow = 2;
		while (size > 0) {
			int no_hlayouts = (size == 1) ? size : size / chartsInRow;
			for (int j = 0; j < no_hlayouts; j++) {
				chartsInRow = (size == 1) ? size : chartsInRow;
				HorizontalLayout chartrow = new HorizontalLayout();
				chartrow.setWidthFull();
				for (int k = 0; k < chartsInRow; k++) {
					JsonObject obj = chartdata.get(i).getAsJsonObject();
					chartrow.add(createchart(obj));
					i++;
				}
				titleAndChart.add(chartrow);
			}
			size = size - (no_hlayouts * chartsInRow);
		}

		return titleAndChart;
	}

	private Component createchart(JsonObject obj) {
		VerticalLayout individualChartlayout = new VerticalLayout();
		HorizontalLayout titleLayout = new HorizontalLayout();
		titleLayout.setWidthFull();
		titleLayout.setJustifyContentMode(FlexComponent.JustifyContentMode.START);
		String title = obj.get("title").getAsString().toUpperCase();
		Label titlelabel = new Label(title);
		titleLayout.add(titlelabel);
		titlelabel.addClassName("title-label");
		HorizontalLayout chartlayout = new HorizontalLayout();
		chartlayout.setWidthFull();
		chartlayout.setJustifyContentMode(FlexComponent.JustifyContentMode.CENTER);
		if (obj.get("Type").getAsString().equals("donut")) {
			if (obj.size() <= 6) {
				chartlayout.add(NightingaleRoseChart(obj));
			} else {
				chartlayout.add(donutChart(obj));
			}
		} else if (obj.get("Type").getAsString().equals("bar")) {
			chartlayout.add(barchart(obj));
		}

		individualChartlayout.add(titleLayout, chartlayout);
		return individualChartlayout;
	}

	private Component NightingaleRoseChart(JsonObject obj) {

		SOChart NightingaleRoseChart = new SOChart();
		NightingaleRoseChart.setSize("100%", "500px");
		String[] categoryNames = categoryNames(obj);
		CategoryData labels = new CategoryData(categoryNames);
		Integer[] values = new Integer[obj.size() - 2];
		AtomicInteger iterator = new AtomicInteger(0);
		obj.entrySet().forEach(entry -> {
			if (!entry.getKey().equals("title") && !entry.getKey().equals("Type")) {
				values[iterator.get()] = obj.get(entry.getKey()).getAsInt();
				iterator.getAndIncrement();
			}
		});
		Data data = new Data(values);
		NightingaleRoseChart nc = new NightingaleRoseChart(labels, data);
		nc.setHoleRadius(Size.percentage(20));
		Position p = new Position();
		p.setTop(Size.percentage(15));
		nc.setPosition(p);
		Toolbox toolbox = new Toolbox();
		toolbox.addButton(new Toolbox.Download(), new Toolbox.Zoom());
		NightingaleRoseChart.add(nc, toolbox);
		return NightingaleRoseChart;
	}

	private Component donutChart(JsonObject obj) {
		SOChart NightingaleRoseChart = new SOChart();
		NightingaleRoseChart.setSize("100%", "500px");
		String[] categoryNames = categoryNames(obj);
		CategoryData labels = new CategoryData(categoryNames);
		Integer[] values = new Integer[obj.size() - 2];
		AtomicInteger iterator = new AtomicInteger(0);
		obj.entrySet().forEach(entry -> {
			if (!entry.getKey().equals("title") && !entry.getKey().equals("Type")) {
				values[iterator.get()] = obj.get(entry.getKey()).getAsInt();
				iterator.getAndIncrement();
			}
		});
		Data data = new Data(values);

		DonutChart nc = new DonutChart(labels, data);
		nc.setHoleRadius(Size.percentage(20));
		Position p = new Position();
		p.setTop(Size.percentage(15));
		nc.setPosition(p);
		Toolbox toolbox = new Toolbox();
		toolbox.addButton(new Toolbox.Download(), new Toolbox.Zoom());
		NightingaleRoseChart.add(nc, toolbox);
		return NightingaleRoseChart;
	}

	private Component barchart(JsonObject obj) {
		SOChart barChart = new SOChart();
		barChart.setSize("100%", "500px");
		DataMatrix dataMatrix = new DataMatrix();
		String[] categoryNames = categoryNames(obj);
		String[] rowNames = rowDescriptionNames(obj);
		dataMatrix.setColumnNames(categoryNames);
		dataMatrix.setColumnDataName("Tables");
		dataMatrix.setRowNames(rowNames);
		dataMatrix.setRowDataName("Distributions");
		for (int i = 0; i < rowNames.length; i++) {
			Double[] values = extractValues(obj, i, categoryNames);
			dataMatrix.addRow(values);
		}
		XAxis xAxisProduct = new XAxis(DataType.CATEGORY);
		xAxisProduct.setName(dataMatrix.getColumnDataName());
		XAxis xAxisYear = new XAxis(DataType.CATEGORY);
		xAxisYear.setName(dataMatrix.getRowDataName());
		YAxis yAxis = new YAxis(DataType.NUMBER);
		yAxis.setName(dataMatrix.getName());
		RectangularCoordinate rc1 = new RectangularCoordinate();
		rc1.addAxis(xAxisProduct, yAxis);
		rc1.getPosition(true).setBottom(Size.percentage(25));
		BarChart bc = null;
		Border b = new Border();

		for (int i = 0; i < dataMatrix.getRowCount(); i++) {
			bc = new BarChart(dataMatrix.getColumnNames(), dataMatrix.getRow(i));
			bc.setName(dataMatrix.getRowName(i));
			bc.plotOn(rc1);
			// charts.add(bc);
		}
		Toolbox toolbox = new Toolbox();
		toolbox.addButton(new Toolbox.Download(), new Toolbox.Zoom());
		barChart.add(bc, toolbox);
		return barChart;
	}

	private String[] categoryNames(JsonObject obj) {
		String[] categoryNames = obj.keySet().stream()
				.filter(keys -> !keys.equals("title") && !keys.equals("Type") && !keys.equals("Description"))
				.toArray(String[]::new);
		return categoryNames;
	}

	private String[] rowDescriptionNames(JsonObject obj) {
		JsonArray arr = obj.get("Description").getAsJsonArray();
		String rowNames[] = new String[arr.size()];
		for (int i = 0; i < arr.size(); i++) {
			rowNames[i] = arr.get(i).getAsString();
		}
		return rowNames;
	}

	private Double[] extractValues(JsonObject obj, int arrayIndex, String[] keys) {

		Double[] values = new Double[obj.size() - 3];
		int valueItr = 0;
		for (int i = 0; i < keys.length; i++) {
			values[valueItr] = Double.valueOf(obj.get(keys[i]).getAsJsonArray().get(arrayIndex).getAsInt());
			valueItr++;
		}
		return values;
	}

}
