package edu.utdallas.kwicsystem.gui;

import java.awt.Button;
import java.awt.Component;
import java.awt.ComponentOrientation;
import java.awt.Dialog;
import java.awt.FlowLayout;
import java.awt.Frame;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Label;
import java.awt.Panel;
import java.awt.TextField;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

import edu.utdallas.kwicsystem.Alphabetizer;
import edu.utdallas.kwicsystem.CircularShift;
import edu.utdallas.kwicsystem.Line;
import edu.utdallas.kwicsystem.OriginalLines;
import edu.utdallas.util.Triple;

public class KWICInterface extends Dialog implements ActionListener {
	private static final long serialVersionUID = 1L;
	private static final String defaultURLRegex;
	private String urlRegex;
	private Label descriptorLabel;
	private TextField descriptorText;
	private Label urlLabel;
	private TextField urlText;
	private Button submitButton;
	private Table circularShiftDB;
	private Button resetSettingsButton;
	private Button settingsButton;
	private Button deleteButton;
	private Button applyButton;
	private Button closeButton;
	private List<Triple<Integer, Line, String>> batch;
	
	static {
		defaultURLRegex = "(http(s)?://)?(www\\.)?[\\p{Alnum}\\-_~]+"
				+ "(\\.[\\p{Alnum}\\-_~]+)*\\.((com)|(org)|(edu))((/)|(/[\\p{Alnum}\\-_~]+)*"
				+ "(\\.((htm)|(html)|(asp)|(aspx)|(php)))?)?";
	}
	
	private Panel northPanel() {
		GridBagLayout gridBag = new GridBagLayout();
		GridBagConstraints c = new GridBagConstraints();
		Panel np = new Panel(gridBag);
		np.setComponentOrientation(ComponentOrientation.RIGHT_TO_LEFT);
		c.fill = GridBagConstraints.BOTH;
		c.weightx = 0.0;
		
		submitButton = new Button(">>");
		c.weighty = 1.0;
        c.gridwidth = 1;
        c.gridheight = 2;
        gridBag.setConstraints(submitButton, c);
        np.add(submitButton, c);
				
		c.gridheight = 1;
		c.weighty = 0.0;
		
		c.gridwidth = GridBagConstraints.CENTER;
		descriptorText = new TextField(100);
		gridBag.setConstraints(descriptorText, c);
		np.add(descriptorText);
		
		c.gridwidth = GridBagConstraints.REMAINDER;
		descriptorLabel = new Label("Descriptor: ");
		gridBag.setConstraints(descriptorLabel, c);
		np.add(descriptorLabel);
		
		c.gridwidth = GridBagConstraints.CENTER;
		urlText = new TextField(100);
		gridBag.setConstraints(urlText, c);
		np.add(urlText);
		
		c.gridwidth = GridBagConstraints.REMAINDER;
		urlLabel = new Label("URL: ");
		gridBag.setConstraints(urlLabel, c);
		np.add(urlLabel);
		
		urlRegex = defaultURLRegex;
		
		return np;
	}
	
	private Component centerPanel() {
		circularShiftDB = new Table("#", "Circular Shift", "URL");
		return circularShiftDB;
	}
	
	public List<Triple<Integer, Line, String>> batch() {
		return batch;
	}
	
	private Panel southPanel() {
		Panel sp = new Panel(new FlowLayout(FlowLayout.RIGHT));
		settingsButton = new Button("Settings...");
		sp.add(settingsButton);
		resetSettingsButton = new Button("Reset Settings");
		sp.add(resetSettingsButton);
		deleteButton = new Button("Delete...");
		sp.add(deleteButton);
		applyButton = new Button("Apply");
		sp.add(applyButton);
		closeButton = new Button("Close");
		sp.add(closeButton);
		return sp;
	}
	
	public KWICInterface(Frame owner) {
		super(owner);
		setTitle("KWIC System Interface");
		
		batch = new ArrayList<>();
		
		add(northPanel(), "North");
		add(centerPanel(), "Center");
		add(southPanel(), "South");
		
		submitButton.addActionListener(this);
		deleteButton.addActionListener(this);
		applyButton.addActionListener(this);
		closeButton.addActionListener(this);
		settingsButton.addActionListener(this);
		resetSettingsButton.addActionListener(this);
		
		setModalityType(ModalityType.APPLICATION_MODAL);
		
		pack();
		setSize(1000, 1000);
		setResizable(false);
	}
	
	public void showWindow() {
		setVisible(true);
	}


	@Override
	public void actionPerformed(ActionEvent event) {
		Object src = event.getSource();
		if(src == submitButton) {
			submitLine(descriptorText.getText().trim(), urlText.getText().trim());
		} else if(src == deleteButton) {
			final String defID = circularShiftDB.getSelectedText().trim();
			InputBox idBox = new InputBox(this, "Enter ID of the row(s) to be deleted", defID);
			idBox.showWindow();
			final String idStr = idBox.getResult().trim();
			if(idStr.matches("[1-9][0-9]*")) {
				circularShiftDB.deleteMatching(0, idStr);
				final int id = Integer.parseInt(idStr);
				batch = batch.parallelStream().filter(t -> t.first() != id).collect(Collectors.toList());
				circularShiftDB.update();
			}
		} else if(src == settingsButton) {
			InputBox userRegexBox = new InputBox(this, "Enter Java regular expression recognizing URLs", urlRegex);
			userRegexBox.showWindow();
			final String newURLRegex = userRegexBox.getResult().trim();
			if(!newURLRegex.isEmpty()) {
				urlRegex = newURLRegex;
			}
		} else if(src == resetSettingsButton) {
			urlRegex = defaultURLRegex;
		} else if(src == applyButton) {
			// batch is ready
//			batch = circularShiftDB.tuples()
//					.parallelStream()
//					.map(tuple -> new Triple<>(Integer.parseInt(tuple[0]), new Line(tuple[1]), tuple[2]))
//					.collect(Collectors.toList());
			processWindowEvent(new WindowEvent(this, WindowEvent.WINDOW_CLOSING));
		} else if(src == closeButton) {
			batch.clear();
			processWindowEvent(new WindowEvent(this, WindowEvent.WINDOW_CLOSING));
		}
	}

	@Override
	protected void processWindowEvent(WindowEvent evt) {
		if (evt.getID() == WindowEvent.WINDOW_CLOSING) {
			dispose();
		}
		super.processWindowEvent(evt);
	}
	
	private void submitLine(String descriptor, String url) {
		if(descriptor.isEmpty() || url.isEmpty()) {
			return;
		}
		if(!url.matches(urlRegex)) {
			urlText.setText(urlText.getText() + "\t<< Bad URL");
			return;
		}
		OriginalLines originals = new OriginalLines(descriptor);
		CircularShift cs = new CircularShift(originals);
		Alphabetizer alpha = new Alphabetizer(cs);
		List<Triple<Integer, Line, String>> left = batch.stream().collect(Collectors.toList());
		final int id = left.size() + 1;
		List<Triple<Integer, Line, String>> right = alpha.items()
				.stream()
				.map(ln -> new Triple<>(id, ln, url))
				.collect(Collectors.toList());
		batch.clear();
		circularShiftDB.clearContents();
		stringBasedMerge(left, right, Triple::second).forEach(triple -> {
			batch.add(triple);
			circularShiftDB.addTuple(triple.first().toString(), triple.second().body(), triple.third());
		});
		circularShiftDB.update();
	}
	
	private List<Triple<Integer, Line, String>> stringBasedMerge(List<Triple<Integer, Line, String>> left,
			List<Triple<Integer, Line, String>> right, Function<Triple<Integer, Line, String>, Object> projection) {
		if (left.isEmpty()) {
			return right;
		}
		if (right.isEmpty()) {
			return left;
		}
		List<Triple<Integer, Line, String>> out = new ArrayList<>();
		int l = 0;
		int r = 0;
		while (l < left.size() && r < right.size()) {
			Triple<Integer, Line, String> a = left.get(l);
			Triple<Integer, Line, String> b = right.get(r);
			int c = projection.apply(a).toString().compareToIgnoreCase(projection.apply(b).toString());
			if (c < 0) { // left[l][sort_by] < right[r][sort_by]
				out.add(a);
				l++;
			} else if (c > 0) { // left[l][sort_by] > right[r][sort_by]
				out.add(b);
				r++;
			} else { // left[l][sort_by] = right[r][sort_by]
				out.add(a);
				out.add(b);
				l++;
				r++;
			}
		}
		while (l < left.size()) {
			out.add(left.get(l++));
		}
		while (r < right.size()) {
			out.add(right.get(r++));
		}
		return out;
	}
}
