package com.sam.publications.client;

import java.util.ArrayList;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.DockPanel;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
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
		
		//setup a panel for each publication, this contains the info panel (including
		//the two buttons), then below it the abstract
        final VerticalPanel[] publicationPanel = new VerticalPanel[publicationOutput.size()];
        
        for (int i = 0; i < publicationOutput.size(); i++) {
        	publicationPanel[i] = new VerticalPanel();
	
        	final HorizontalPanel infoPanel = new HorizontalPanel();
        	
        	final HTML citation = new HTML(publicationOutput.get(i).citation);
        	final Button pdfButton = new Button("pdf");
        	final Button abstrButton = new Button("show abstract");   
        	final HorizontalPanel buttonWrapper = new HorizontalPanel();

        	publicationPanel[i].setWidth("100%");
        	infoPanel.setWidth("100%");
        	
        	infoPanel.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_LEFT);
        	infoPanel.add(citation);
        	
        	infoPanel.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_RIGHT);
        	buttonWrapper.add(pdfButton);
        	buttonWrapper.add(abstrButton);
        	
        	infoPanel.add(buttonWrapper);

        	if (publicationOutput.get(i).pdf.equals("null")) {
        		pdfButton.setEnabled(false);
        	}
        	
        	if (publicationOutput.get(i).abstr.equals("null")) {
        		abstrButton.setEnabled(false);
        	}
        	
        	//add CSS styles
        	citation.setStyleName("publication");
        	pdfButton.setStyleName("pdfButton");
        	abstrButton.setStyleName("abstButton");
        	
        	//add info panel
        	publicationPanel[i].add(infoPanel);
        	
        	//set up abstract panel, add it to the publication panel
        	//(below info panel) and add CSS style
        	final HTML abstr = new HTML("");
        	publicationPanel[i].add(abstr);
        	
        	abstr.setStyleName("abst");	
        	
        	final int finali = i;
        	
        	//set up pdf button
        	pdfButton.addClickHandler(new ClickHandler() {
        		public void onClick(ClickEvent event) {
        			PHP.Call("pageLoad.php?id=getPub: " + publicationOutput.get(finali).pdf, true);
        			
        			Window.open("getPub.php?id="+publicationOutput.get(finali).pdf,"publication","");
        		}
        	});
        	
        	//set up abstract button
        	abstrButton.addClickHandler(new ClickHandler() {
        		public void onClick(ClickEvent event) {
        			if (abstrButton.getText().equals("show abstract")) {
        				abstr.setHTML(publicationOutput.get(finali).abstr);
        				abstrButton.setText("hide abstract");
        			} else {
        				abstr.setHTML("");
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
        		if (publicationOutput.get(i).year==9999) {
        			header="pre-print";
        		} else if (publicationOutput.get(i).year==9998) {
        			header="in press";
        		} else {
        			header = header + publicationOutput.get(i).year;
        		}
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
