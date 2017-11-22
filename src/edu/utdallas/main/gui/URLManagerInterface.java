package edu.utdallas.main.gui;

import java.awt.BorderLayout;
import java.awt.Button;
import java.awt.Dialog;
import java.awt.Label;
import java.awt.Panel;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;

import edu.utdallas.kwicsystem.gui.InputBox;
import edu.utdallas.kwicsystem.gui.Table;
import edu.utdallas.searchengine.db.Database;
import edu.utdallas.util.Triple;

public class URLManagerInterface extends Dialog implements ActionListener {
	private static final long serialVersionUID = 1L;
	private Label lblInfo;
	private Button btnDelete;
	private Button btnUpdateBalance;
	private Button btnClose;
	private Table tblURLsList;

	public URLManagerInterface(Dialog owner) {
		super(owner);
		setTitle("URL Manager");
		setLayout(new BorderLayout());
		
		lblInfo = new Label("URLs mentioned in the system:");
		add(lblInfo, BorderLayout.NORTH);
		tblURLsList = new Table("URL", "Balance");
		refreshTable();
		add(tblURLsList, BorderLayout.CENTER);
		Panel pnlSouth = new Panel();
		btnClose = new Button("Close");
		btnDelete = new Button("Delete...");
		btnUpdateBalance = new Button("Update Balance...");
		pnlSouth.add(btnDelete);
		pnlSouth.add(btnUpdateBalance);
		pnlSouth.add(btnClose);
		add(pnlSouth, BorderLayout.SOUTH);
		
		btnClose.addActionListener(this);
		btnDelete.addActionListener(this);
		btnUpdateBalance.addActionListener(this);
		
		setModalityType(ModalityType.APPLICATION_MODAL);
		
		pack();
		setSize(600, 800);
		setResizable(false);
	}
	
	private void refreshTable() {
		tblURLsList.clearContents();
		Database.v().all().stream().map(Triple::second).distinct().forEach(url -> {
			tblURLsList.addTuple(url, "$" + Database.v().getBalanceForURL(url));
		});
		tblURLsList.update();
	}
	
	public void showWindow() {
		setVisible(true);
	}
	
	@Override
	protected void processWindowEvent(WindowEvent evt) {
		if (evt.getID() == WindowEvent.WINDOW_CLOSING) {
			dispose();
		}
		super.processWindowEvent(evt);
	}

	@Override
	public void actionPerformed(ActionEvent event) {
		Object src = event.getSource();
		final String defURL = tblURLsList.getSelectedText().trim();
		if(src == btnClose) {
			processWindowEvent(new WindowEvent(this, WindowEvent.WINDOW_CLOSING));
		} else if(src == btnDelete) {
			if(!defURL.isEmpty() && Database.v().getBalanceForURL(defURL) != null) {
				InputBox urlBox = new InputBox(this, "Enter URL of the website to be deleted", defURL);
				urlBox.showWindow();
				final String url = urlBox.getResult().trim();
				if(!url.isEmpty()) {
					Database.v().deleteRowByURL(url);
					tblURLsList.deleteMatching(0, url);
					refreshTable();
				}
			}
		} else if(src == btnUpdateBalance) {
			if(!defURL.isEmpty()) {
				Double currentBalance = Database.v().getBalanceForURL(defURL);
				if(currentBalance != null) {
					InputBox balanceBox = new InputBox(this, "Current balance associated with URL \'" 
								+ defURL + "\' is $" + currentBalance + ".\nPlease enter new balance", currentBalance.toString());
					balanceBox.showWindow();
					final String rawBalance = balanceBox.getResult().trim();
					if(!rawBalance.isEmpty() && rawBalance.matches("[1-9][0-9]*(\\.(([0-9])|([0-9][0-9])))?")) {
						final double newBalance = Double.parseDouble(rawBalance);	
						Database.v().updateURLBalance(defURL, newBalance);
						tblURLsList.deleteMatching(0, defURL);
						tblURLsList.addTuple(defURL, "$" + newBalance);
						refreshTable();
					}
				}
			}
		}
	}

}
