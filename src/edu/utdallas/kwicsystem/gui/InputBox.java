package edu.utdallas.kwicsystem.gui;

import java.awt.Button;
import java.awt.Dialog;
import java.awt.FlowLayout;
import java.awt.Label;
import java.awt.TextField;
import java.awt.Window;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;

public class InputBox extends Dialog implements ActionListener {
	private static final long serialVersionUID = 1L;
	private Label message;
	private TextField text;
	private Button okButton;
	private Button cancelButton;
	private String result;
	
	public InputBox(Window owner, String message) {
		super(owner, "Input");
		this.message = new Label(message + ": ");
		text = new TextField(50);
		okButton = new Button("OK");
		cancelButton = new Button("Cancel");
		result = null;
			
		setLayout(new FlowLayout(FlowLayout.LEFT, 10, 10));
		add(this.message);
		add(text);
		add(okButton);
		add(cancelButton);
		
		okButton.addActionListener(this);
		cancelButton.addActionListener(this);
		
		setModalityType(ModalityType.APPLICATION_MODAL);
		setResizable(false);
		pack();
	}
	
	public InputBox(Window owner, String message, String defaultResult) {
		this(owner, message);
		this.result = defaultResult;
		if(defaultResult != null) {
			text.setText(defaultResult);
		}
	}
	
	public void showWindow() {
		setVisible(true);
	}
	
	private void closeWindow() {
		processWindowEvent(new WindowEvent(this, WindowEvent.WINDOW_CLOSING));
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		Object src = e.getSource();
		if(src == okButton) {
			result = text.getText();
			closeWindow();
		} else if (src == cancelButton) {
			closeWindow();
		}
	}
	
	@Override
	protected void processWindowEvent(WindowEvent evt) {
		if (evt.getID() == WindowEvent.WINDOW_CLOSING) {
			dispose();
		}
		super.processWindowEvent(evt);
	}
	
	public String getResult() {
		return result;
	}
}
