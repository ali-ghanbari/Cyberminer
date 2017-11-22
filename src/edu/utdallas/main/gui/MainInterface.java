package edu.utdallas.main.gui;

import java.applet.Applet;
import java.awt.BorderLayout;
import java.awt.Button;
import java.awt.Checkbox;
import java.awt.CheckboxGroup;
import java.awt.Frame;
import java.awt.Label;
import java.awt.List;
import java.awt.Panel;
import java.awt.Point;
import java.awt.TextField;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.TextEvent;
import java.awt.event.TextListener;
import java.io.StringReader;
import java.util.Arrays;
import java.util.stream.Collectors;

import edu.utdallas.kwicsystem.gui.KWICInterface;
import edu.utdallas.main.Config;
import edu.utdallas.searchengine.SELexer;
import edu.utdallas.searchengine.SearchExpression;
import edu.utdallas.searchengine.parser;
import edu.utdallas.searchengine.db.Database;
import edu.utdallas.util.Triple;

public class MainInterface extends Applet implements ActionListener, ItemListener, TextListener, KeyListener {
	private static final long serialVersionUID = 1L;
	
	private Label lblSearchDescriptor;
	private TextField txtSearch;
	private Button btnSearch;
	private Button btnIndexMan;
	private Button btnURLMan;
	private PagedList lstResults;
	private Label lblSortBy;
	private CheckboxGroup chgSortBy;
	private Checkbox chkDictionary;
	private Checkbox chkBalance;
	private Checkbox chkVisitFrequency;
	private AutofillPopup autofillPopup;
		
	@Override
	public void init() {
		autofillPopup = null;
		setLayout(new BorderLayout());
		lblSearchDescriptor = new Label("Search Expression: ");
		txtSearch = new TextField(50);
		btnSearch = new Button("Search");
		Panel pnlSortOptions = new Panel();
		lblSortBy = new Label("Sort by: ");
		chgSortBy = new CheckboxGroup();
		chkDictionary = new Checkbox("Dictionary order", chgSortBy, true);
		chkBalance = new Checkbox("Balance value", chgSortBy, false);
		chkVisitFrequency = new Checkbox("Visit frequency", chgSortBy, false);
		pnlSortOptions.add(lblSortBy);
		pnlSortOptions.add(chkDictionary);
		pnlSortOptions.add(chkBalance);
		pnlSortOptions.add(chkVisitFrequency);
		lstResults = new PagedList(20, SortOptions.SORT_BY_DICT_ORDER, getAppletContext(), pnlSortOptions);
		Panel pnlNorth = new Panel();
		pnlNorth.add(lblSearchDescriptor);
		pnlNorth.add(txtSearch);
		pnlNorth.add(btnSearch);
		add(pnlNorth, BorderLayout.NORTH);
		add(lstResults, BorderLayout.CENTER);
		Panel pnlSouth = new Panel();
		btnIndexMan = new Button("Index Manager");
		btnURLMan = new Button("URL Manager");
		pnlSouth.add(btnIndexMan);
		pnlSouth.add(btnURLMan);
		add(pnlSouth, BorderLayout.SOUTH);
		btnIndexMan.addActionListener(this);
		btnSearch.addActionListener(this);
		btnURLMan.addActionListener(this);
		chkBalance.addItemListener(this);
		chkDictionary.addItemListener(this);
		chkVisitFrequency.addItemListener(this);
		txtSearch.addTextListener(this);
		txtSearch.addKeyListener(this);
	}
	
	private String sanitizeSearchPhrase(String s) {
		return s.replaceAll("\\p{Punct}", " ")
				.replaceAll("[\\s]+", " ")
				.trim();
	}

	@Override
	public void actionPerformed(ActionEvent event) {
		Object src = event.getSource();
		if(src == btnIndexMan) {
			KWICInterface kwicDialog = new KWICInterface(null); 
			kwicDialog.showWindow();
			Database.v().addBatch(kwicDialog.batch());
		} else if (src == btnSearch) {
			submitSearchPhrase(txtSearch.getText().trim());
		} else if (src == btnURLMan) {
			URLManagerInterface urlMan = new URLManagerInterface(null);
			urlMan.showWindow();
		}
	}
	
	private void submitSearchPhrase(String searchPhrase) {
		if(!searchPhrase.isEmpty()) {
			boolean parsed;
			do {
				try {
					SearchExpression se = (new parser(new SELexer(new StringReader(searchPhrase)))).getTree();
					lstResults.clean();
					se.evaluate(Database.v(), String::startsWith, Config.CASE_SENSITIVE).stream()
						.map(Triple::second)
						.distinct()
						.forEach(t -> {
							lstResults.add(t.toString());
						});
					parsed = true;
				} catch (Exception e) {
					searchPhrase = sanitizeSearchPhrase(searchPhrase);
					parsed = false;
				}
			} while(!parsed);
			lstResults.display();
			lstResults.repaint();
		}
	}

	@Override
	public void itemStateChanged(ItemEvent event) {
		if(chkDictionary.getState()) {
			lstResults.setSortOptions(SortOptions.SORT_BY_DICT_ORDER);
		}
		if(chkBalance.getState()) {
			lstResults.setSortOptions(SortOptions.SORT_BY_BALANCE_VAL);
		}
		if(chkVisitFrequency.getState()) {
			lstResults.setSortOptions(SortOptions.SORT_BY_VISIT_FREQ);
		}
	}
	
	private class AutofillPopup extends Frame implements ItemListener {
		private static final long serialVersionUID = 1L;
		List autofillOptions;
		
		AutofillPopup(int width) {
			setLayout(new BorderLayout());
			autofillOptions = new List(10);
			add(autofillOptions);
			setFocusableWindowState(false);
			setUndecorated(true);
			autofillOptions.addItemListener(this);
			pack();
			setSize(width, getHeight());
		}
		
		void popUp() {
			setVisible(true);
		}
		
		void update(String[] words) {
			autofillOptions.removeAll();
			final String searchPhrase = Arrays.stream(words).collect(Collectors.joining(" ")).trim();
			if(!searchPhrase.isEmpty()) {
				try {
					SearchExpression se = (new parser(new SELexer(new StringReader(searchPhrase)))).getTree();
					se.evaluate(Database.v(), String::contains, false)
						.stream()
						.map(t -> t.first().originalLine())
						.distinct()
						.limit(10)
						.forEach(ln -> {
							autofillOptions.add(ln.originalLine().body());
						});
				} catch (Exception e) {
					//swallow the exception
				}
			}			
		}

		@Override
		public void itemStateChanged(ItemEvent event) {
			Object src = event.getSource();
			if(src == autofillOptions) {
				MainInterface.this.txtSearch.setText(autofillOptions.getSelectedItem());
			}
		}
	}

	@Override
	public void textValueChanged(TextEvent event) {
		Object src = event.getSource();
		if(src == txtSearch) {
			final String theSearchText = txtSearch.getText().trim();
			if(!theSearchText.isEmpty()) {
				if(autofillPopup == null) {
					autofillPopup = new AutofillPopup(txtSearch.getWidth());
				}
				Point appletPos = getParent().getLocationOnScreen();
				autofillPopup.setLocation(appletPos.x + txtSearch.getX(),
						appletPos.y + txtSearch.getY() + txtSearch.getHeight() + 1);
				autofillPopup.popUp();
				autofillPopup.setFocusable(false);
				autofillPopup.update(theSearchText.split("[\\p{Punct}\\s]+"));			
			}
		}
	}

	@Override
	public void keyPressed(KeyEvent e) {
		
	}

	@Override
	public void keyReleased(KeyEvent e) {
		Object src = e.getSource();
		if(src == txtSearch) {
			if(e.getKeyCode() == KeyEvent.VK_ESCAPE) {
				autofillPopup.setVisible(false);
			} else if(e.getKeyCode() == KeyEvent.VK_ENTER) {
				submitSearchPhrase(txtSearch.getText().trim());
			}
		}
	}

	@Override
	public void keyTyped(KeyEvent e) {
		
	}	
}
