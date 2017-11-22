package edu.utdallas.main.gui;

import java.applet.AppletContext;
import java.awt.Button;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.net.URL;

import edu.utdallas.kwicsystem.gui.InputBox;
import edu.utdallas.searchengine.db.Database;

final class PagedListEventListener extends MouseAdapter implements ItemListener, ActionListener {
	private String selectedItem;
	private final AppletContext ac;
	private final Button backButton;
	private final Button forwardButton;
	private final Button itemsPerPageButton;
	private final PagedList theList;
	
	PagedListEventListener(PagedList theList,
			AppletContext ac,
			Button backButton,
			Button forwardButton,
			Button itemsPerPageButton) {
		this.ac = ac;
		this.selectedItem = null;
		this.backButton = backButton;
		this.forwardButton = forwardButton;
		this.itemsPerPageButton = itemsPerPageButton;
		this.theList = theList;
	}
	
	void nullifySelection() {
		selectedItem = null;
	}

	@Override
	public void itemStateChanged(ItemEvent event) {
		selectedItem = (String) event.getItemSelectable().getSelectedObjects()[0];
	}
	
	@Override
	public void mouseClicked(MouseEvent event) {
        if(event.getClickCount() == 2) {
        	if(selectedItem != null) {
    			try {
    				//increasing visit frequency
    				Database.v().incrementURLVisitFrequenct(selectedItem);
    				final String rawURL = selectedItem;
    				final URL url = new URL(rawURL.toLowerCase().startsWith("http") ? rawURL : "http://" + rawURL);
    				ac.showDocument(url, "_self");
    			} catch (Exception e) {
    				//swallow exception
    			}
        	}
        }
	}

	@Override
	public void actionPerformed(ActionEvent event) {
		final Object src = event.getSource();
		if(src == backButton) {
			theList.moveBack();
		} else if(src == forwardButton) {
			theList.moveForward();
		} else if(src == itemsPerPageButton) {
			InputBox pgSzBox = new InputBox(null, "Enter number of items per page", theList.getPageSize() + "");
			pgSzBox.showWindow();
			final String rawPageSize = pgSzBox.getResult().trim();
			if(rawPageSize.matches("[1-9][0-9]*")) {
				final int newPageSize = Integer.parseInt(rawPageSize);
				if(newPageSize > 0) {
					theList.setPageSize(newPageSize);
					theList.display();
				}
			}
		}
	}
}
