package com.sam.publications.client;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import com.google.gwt.event.dom.client.ChangeEvent;
import com.google.gwt.event.dom.client.ChangeHandler;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.json.client.JSONArray;
import com.google.gwt.json.client.JSONObject;
import com.google.gwt.json.client.JSONParser;
import com.google.gwt.json.client.JSONValue;
import com.google.gwt.user.client.Timer;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.ListBox;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.VerticalPanel;

public class GeneratePublicationList {
	public static void Run() {
		//get all publication data
		JSONObject json=JSONParser.parseLenient(PHP.phpOutput).isObject();
        
		//extract topics
        JSONValue topics = json.get("topics"); 
        JSONArray topicArray = topics.isArray();
        
        Publication.topicString.add("Show everything");
        Publication.topicPosition.add(0);
        
        for (int i = 0; i < topicArray.size(); i++) {
        	JSONObject item = JSONParser.parseLenient(topicArray.get(i).toString()).isObject();

        	Publication.topicString.add(item.get("topic").toString().replaceAll("\"", ""));
        	Publication.topicPosition.add(Integer.parseInt(item.get("position").toString().replaceAll("\"", "")));
        }

        //extract methods
        JSONValue methods = json.get("methods"); 
        JSONArray methodArray = methods.isArray();

        Publication.methodString.add("Show everything");
        Publication.methodPosition.add(0);
        
        for (int i = 0; i < methodArray.size(); i++) {
        	JSONObject item = JSONParser.parseLenient(methodArray.get(i).toString()).isObject();

        	Publication.methodString.add(item.get("method").toString().replaceAll("\"", ""));
        	Publication.methodPosition.add(Integer.parseInt(item.get("position").toString().replaceAll("\"", "")));
        }
        
        //extract publications
        JSONValue publicationsjson = json.get("publications");
        JSONArray publicationArray = publicationsjson.isArray();
        
        for (int i = 0; i < publicationArray.size(); i++) {
        	JSONObject item = JSONParser.parseLenient(publicationArray.get(i).toString()).isObject();
        	
        	Publication pub = new Publication();
        	pub.year = Integer.parseInt(item.get("year").toString().replaceAll("\"", ""));
        	pub.citation = item.get("citation").toString().replaceAll("\"", "");
        	pub.topic = item.get("topic").toString().replaceAll("\"", "");
        	pub.method = item.get("method").toString().replaceAll("\"", "");
        	pub.pdf = item.get("pdf").toString().replaceAll("\"", "");
        	pub.abstr = item.get("abstract").toString().replaceAll("\"", "");

        	//parse topic and method data
        	pub.parseData();
        	
        	//add to publications list
        	GenerateOutput.allPublications.add(pub);	
        }
        
        //set the booleans for the publications
        for (int i = 0; i < publicationArray.size(); i++) {
        	GenerateOutput.allPublications.get(i).setBooleans();
        }
        
        //sort publications (by year as default)
        Collections.sort(GenerateOutput.allPublications);
        
        //generate the output
        Publication.publicationList = GenerateOutput.Run();
        
        //set up the sort-by listbox
        final VerticalPanel sortByPanel = new VerticalPanel();
        final Label sortByPanelLabel = new Label("Sort by:");
        final ListBox sortBySelectionMenu = new ListBox();
        
        sortBySelectionMenu.addItem("Year");
        sortBySelectionMenu.addItem("Topic");
        sortBySelectionMenu.addItem("Method");
        
        sortByPanel.add(sortByPanelLabel);
        sortByPanel.add(sortBySelectionMenu);
        
        //set up the selection menus: 1. topics
        final VerticalPanel topicSelectionPanel = new VerticalPanel();
        final Label topicSelectionLabel = new Label("Filter by topic:");
        final ListBox topicSelectionMenu = new ListBox();

        for (int i = 0; i <= Publication.maxTopic; i++) {
        	topicSelectionMenu.addItem(Publication.topicString.get(Publication.topicPosition.indexOf(i)));
        }
        
        topicSelectionPanel.add(topicSelectionLabel);
        topicSelectionPanel.add(topicSelectionMenu);
        
        //2. methods
        
        final VerticalPanel methodSelectionPanel = new VerticalPanel();
        final Label methodSelectionLabel = new Label("Filter by method:");
        final ListBox methodSelectionMenu = new ListBox();

        for (int i = 0; i <= Publication.maxMethod; i++) {
        	methodSelectionMenu.addItem(Publication.methodString.get(Publication.methodPosition.indexOf(i)));
        }
        
        methodSelectionPanel.add(methodSelectionLabel);
        methodSelectionPanel.add(methodSelectionMenu);
        
        //put panels on screen     
        RootPanel.get().add(sortByPanel);
        RootPanel.get().add(topicSelectionPanel);
        RootPanel.get().add(methodSelectionPanel);
        RootPanel.get().add(Publication.publicationList);
        
        //setup handler for sort-by panel
        sortBySelectionMenu.addChangeHandler(new ChangeHandler() {
        	public void onChange(ChangeEvent event) {
        		Publication.sortBy = sortBySelectionMenu.getSelectedIndex();
        		
        		//sort the publications by the new sorter
        		Collections.sort(GenerateOutput.allPublications);
        		
        		Publication.publicationList = GenerateOutput.Run();
        		
        		RootPanel.get().clear();
        		RootPanel.get().add(sortByPanel);
        		RootPanel.get().add(topicSelectionPanel);
                RootPanel.get().add(methodSelectionPanel);
                RootPanel.get().add(Publication.publicationList);
        	}
        });
        
        //setup handlers for listboxes
        topicSelectionMenu.addChangeHandler(new ChangeHandler() {
        	public void onChange(ChangeEvent event) {
        		Publication.selectedTopic = topicSelectionMenu.getSelectedIndex();
        		
        		Publication.publicationList = GenerateOutput.Run();
        		
        		RootPanel.get().clear();
        		RootPanel.get().add(sortByPanel);
        		RootPanel.get().add(topicSelectionPanel);
                RootPanel.get().add(methodSelectionPanel);
                RootPanel.get().add(Publication.publicationList);
        		
        	}
        });

        methodSelectionMenu.addChangeHandler(new ChangeHandler() {
        	public void onChange(ChangeEvent event) {
        		Publication.selectedMethod = methodSelectionMenu.getSelectedIndex();
        		
        		Publication.publicationList = GenerateOutput.Run();
        		
        		RootPanel.get().clear();
        		RootPanel.get().add(sortByPanel);
        		RootPanel.get().add(topicSelectionPanel);
                RootPanel.get().add(methodSelectionPanel);
                RootPanel.get().add(Publication.publicationList);
        		
        	}
        });
	}
}
 