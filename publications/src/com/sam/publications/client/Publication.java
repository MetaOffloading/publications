package com.sam.publications.client;

import java.util.ArrayList;

import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.VerticalPanel;

public class Publication implements java.lang.Comparable<Publication> {
	//static variables
	static int maxTopic=0, maxMethod=0, selectedTopic=0, selectedMethod=0;
	static int sortByYear = 0, sortByTopic = 1, sortByMethod = 2;
	
	//store topics here
	static ArrayList<String> topicString = new ArrayList<String>();
    static ArrayList<Integer> topicPosition = new ArrayList<Integer>(); 
    
    //store methods here
    static ArrayList<String> methodString = new ArrayList<String>();
    static ArrayList<Integer> methodPosition = new ArrayList<Integer>(); 
	
	//sort by year as default
	static int sortBy = sortByYear;
	
	//use this to contain the list of publications
	static VerticalPanel publicationList = new VerticalPanel();
	
	//information about each individual publication
	int year;
	String citation, topic, method, pdf, abstr; 
	int mainMethod, mainTopic;
	String[] splitTopic, splitMethod;
	boolean[] topics, methods;

	//use this for sorting
	public int compareTo(Publication comparepub) {
		int c = 0;
		
		if (sortBy == sortByYear) {
			c = comparepub.year - this.year;
		}
		
		if (sortBy == sortByTopic) {
			c = topicPosition.get(this.mainTopic) - topicPosition.get(comparepub.mainTopic);
		}
		
		if (sortBy == sortByMethod) {
			c = methodPosition.get(this.mainMethod) - methodPosition.get(comparepub.mainMethod); 
		}
		
	
		return(c);
	}
	
	//parse methods and topics for this publication
	public void parseData() {
		splitTopic = topic.split(",");
		splitMethod = method.split(",");
		
		//we assume that the first topic and method in the sql entry is the main one
		mainTopic=Integer.parseInt(splitTopic[0]);
		mainMethod=Integer.parseInt(splitMethod[0]);
		
		//increment maxTopic
		for (int i=0; i<splitTopic.length; i++) {
			int topic=Integer.parseInt(splitTopic[i]);
			
			if (topic>maxTopic) {
				maxTopic=topic;
			}
		}
		
		//increment maxMethod
		for (int i=0; i<splitMethod.length; i++) {
			int method=Integer.parseInt(splitMethod[i]);
			
			if (method>maxMethod) {
				maxMethod=method;
			}
		}
	}
	
	//set booleans for this publication
	public void setBooleans() {
		topics = new boolean[maxTopic+1];
		methods = new boolean[maxMethod+1];

		for (int i=1; i<(maxTopic+1); i++) {
			methods[i]=false;
		}

		for (int i=0; i<(splitTopic.length); i++) {
			topics[Integer.parseInt(splitTopic[i])]=true;
		} 

		for (int i=1; i<(maxMethod+1); i++) {
			methods[i]=false;
		}
		
		for (int i=0; i<(splitMethod.length); i++) {
			methods[Integer.parseInt(splitMethod[i])]=true;
		}
	}
}
