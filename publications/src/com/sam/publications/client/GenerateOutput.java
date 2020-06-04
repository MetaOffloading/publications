package com.sam.publications.client;

import java.util.ArrayList;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.VerticalPanel;

public class GenerateOutput {
	public static ArrayList<Publication> allPublications = new ArrayList<Publication>();
	
	public static VerticalPanel Run() {
		//select publications for output
		final ArrayList<Publication> publicationOutput = new ArrayList<Publication>();
		
		for (int i = 0; i < allPublications.size(); i++) {
        	boolean include = true;
        	
        	if (Publication.selectedTopic > 0) { //topic has been selected
        		if (allPublications.get(i).topics[Publication.selectedTopic]==false) {
        			include = false;
        		}
        	}
        	
        	if (Publication.selectedMethod > 0) {//method has been selected
        		if (allPublications.get(i).methods[Publication.selectedMethod]==false) {
        			include = false;
        		}
        	}
        	
        	if (include) {
        		publicationOutput.add(allPublications.get(i));
        	}
        }
		
		//setup a panel for each publication
        final VerticalPanel[] publicationPanel = new VerticalPanel[publicationOutput.size()];
        
        for (int i = 0; i < publicationOutput.size(); i++) {
        	publicationPanel[i] = new VerticalPanel();
	
        	final HorizontalPanel citationPanel = new HorizontalPanel();
        	
        	final HTML citation = new HTML(publicationOutput.get(i).citation);
        	final Button pdfButton = new Button("pdf");
        	final Button abstrButton = new Button("show abstract");   	
        	
        	citationPanel.add(citation);
        	
        	if (publicationOutput.get(i).pdf.length() > 0) {
        		citationPanel.add(pdfButton);
        	}
        	
        	if (publicationOutput.get(i).abstr.length() > 0) {
        		citationPanel.add(abstrButton);
        	}
        	
        	//add CSS styles
        	citation.setStyleName("publication");
        	pdfButton.setStyleName("pdfButton");
        	abstrButton.setStyleName("abstButton");
        	
        	//add citation panel
        	publicationPanel[i].add(citationPanel);
        	
        	//set up abstract panel and add CSS style
        	final Label abstr = new Label("");
        	publicationPanel[i].add(abstr);
        	
        	abstr.setStyleName("abstr");	
        	
        	final int finali = i;
        	
        	//set up abstract button
        	abstrButton.addClickHandler(new ClickHandler() {
        		public void onClick(ClickEvent event) {
        			if (abstrButton.getText().equals("show abstract")) {
        				abstr.setText(publicationOutput.get(finali).abstr);
        				abstrButton.setText("hide abstract");
        			} else {
        				abstr.setText("");
        				abstrButton.setText("show abstract");
        			}
        		}
        	});
        	
        }
        
        //start generating output
        final VerticalPanel outputPanel = new VerticalPanel();
        
        String lastPublicationHeader = "";
        
        for (int i = 0; i < publicationOutput.size(); i++) {
        	String header = "";
        	
        	if (Publication.sortBy == Publication.sortByYear) {
        		header = header + publicationOutput.get(i).year;
        	}
        	
        	if (Publication.sortBy == Publication.sortByTopic) {
        		header = header + Publication.topicString.get(publicationOutput.get(i).mainTopic);
        	}
        	
        	if (Publication.sortBy == Publication.sortByMethod) {
        		header = header + Publication.methodString.get(publicationOutput.get(i).mainMethod);
        	}
        	
        	if (header.equals(lastPublicationHeader) == false) {
        		lastPublicationHeader = header;
        		
        		final Label publicationHeader = new Label(header);
        		outputPanel.add(publicationHeader);
        		publicationHeader.setStyleName("publicationHeader");
        	}
        	
        	outputPanel.add(publicationPanel[i]);
        }
        
        return(outputPanel);
	}
}
