package edu.utdallas.main.gui;

import java.applet.AppletContext;
import java.awt.BorderLayout;
import java.awt.Button;
import java.awt.Component;
import java.awt.Label;
import java.awt.Panel;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

import edu.utdallas.searchengine.db.Database;

public class PagedList extends Panel {
	private static final long serialVersionUID = 1L;
	public static final int DEF_PAGE_SZ = 5;
	private int pageSize = DEF_PAGE_SZ;
	private List<String> allData;
	private List<String[]> pages;
	private java.awt.List lstOutput;
	private Button btnBack;
	private Label lblInfo;
	private Button btnForward;
	private Label lblViewing;
	private Button btnItemsPerPage;
	private int currentPage;
	private int numPages;
	private PagedListEventListener eventListener;
	private SortOptions sortOptions;
	
	public PagedList(int defRows, SortOptions defSortOptions, AppletContext ac, Component... top) {
		this.sortOptions = defSortOptions;
		allData = new ArrayList<>();
		setLayout(new BorderLayout());
		if(top != null) {
			Panel northPanel = new Panel();
			for(Component comp : top) {
				northPanel.add(comp);
			}
			add(northPanel, BorderLayout.NORTH);
		}
		lstOutput = new java.awt.List(defRows);
		add(lstOutput, BorderLayout.CENTER);
		btnBack = new Button("<");
		lblInfo = new Label("          ");
		btnForward = new Button(">");
		Panel southPanel = new Panel();
		southPanel.add(btnBack);
		southPanel.add(lblInfo);
		southPanel.add(btnForward);
		lblViewing = new Label("     Viewing");
		southPanel.add(lblViewing);
		btnItemsPerPage = new Button(DEF_PAGE_SZ + " item(s) per page");
		southPanel.add(btnItemsPerPage);
		add(southPanel, BorderLayout.SOUTH);
		this.currentPage = -2;
		eventListener = new PagedListEventListener(this, ac, btnBack, btnForward, btnItemsPerPage);
		btnBack.addActionListener(eventListener);
		btnForward.addActionListener(eventListener);
		lstOutput.addItemListener(eventListener);
		lstOutput.addMouseListener(eventListener);
		btnItemsPerPage.addActionListener(eventListener);
	}
	
	public void setSortOptions(SortOptions so) {
		this.sortOptions = so;
		display();
	}
	
	private void inform(String info) {
		lblInfo.setText(center(info, 10));
	}
	
	private static String center(String s, int size) {
        return center(s, size, ' ');
    }

    private static String center(String s, int size, char pad) {
        if (s == null || size <= s.length())
            return s;
        StringBuilder sb = new StringBuilder(size);
        for (int i = 0; i < (size - s.length()) / 2; i++) {
            sb.append(pad);
        }
        sb.append(s);
        while (sb.length() < size) {
            sb.append(pad);
        }
        return sb.toString();
    }

	public boolean add(String e) {
		return allData.add(e);
	}

	public void add(int index, String element) {
		allData.add(index, element);
	}

	public boolean addAll(Collection<? extends String> c) {
		return allData.addAll(c);
	}
	
	public boolean addAll(int index, Collection<? extends String> c) {
		return allData.addAll(c);
	}
	
	public void clear() {
		allData.clear();
	}
	
	public boolean contains(Object o) {
		return allData.contains(o);
	}
	
	public boolean containsAll(Collection<?> c) {
		return allData.containsAll(c);
	}
	
	public String get(int index) {
		return allData.get(index);
	}

	public int indexOf(Object o) {
		return allData.indexOf(o);
	}

	public boolean isEmpty() {
		return allData.isEmpty();
	}

	public Iterator<String> iterator() {
		return allData.iterator();
	}

	public int lastIndexOf(Object o) {
		return allData.lastIndexOf(o);
	}

	public ListIterator<String> listIterator() {
		return allData.listIterator();
	}

	public ListIterator<String> listIterator(int index) {
		return allData.listIterator(index);
	}

	public boolean remove(Object o) {
		return allData.remove(o);
	}

	public String _remove(int index) {
		return allData.remove(index);
	}

	public boolean removeAll(Collection<?> c) {
		return allData.removeAll(c);
	}

	public boolean retainAll(Collection<?> c) {
		return allData.retainAll(c);
	}

	public String set(int index, String element) {
		return allData.set(index, element);
	}

	public int _size() {
		return allData.size();
	}

	public List<String> subList(int fromIndex, int toIndex) {
		return allData.subList(fromIndex, toIndex);
	}

	public Object[] toArray() {
		return allData.toArray();
	}

	public <T> T[] toArray(T[] a) {
		return allData.toArray(a);
	}
	
	public void clean() {
		allData.clear();
		lstOutput.removeAll();
		inform(" ");
		currentPage = -1;
		eventListener.nullifySelection();
	}
	
	public void moveBack() {
		if(currentPage > 0) {
			inform(currentPage + "/" + numPages);
			currentPage--;
			lstOutput.removeAll();
			for(String s : pages.get(currentPage)) {
				if(s != null) {
					lstOutput.add(s);
				}
			}
			eventListener.nullifySelection();
		}
	}
	
	void moveForward() {
		if(currentPage != -2 && currentPage < numPages - 1) {
			currentPage++;
			inform((currentPage + 1) + "/" + numPages);
			lstOutput.removeAll();
			for(String s : pages.get(currentPage)) {
				if(s != null) {
					lstOutput.add(s);
				}
			}
			eventListener.nullifySelection();
		}
	}
	
	private void doSort() {
		final Database db = Database.v();
		final Comparator<String> comp;
		switch(sortOptions) {
		case SORT_BY_BALANCE_VAL:
			comp = (url1, url2) -> {
				final double v1 = db.getBalanceForURL(url1);
				final double v2 = db.getBalanceForURL(url2);
				if(v1 == v2) {
					return 0;
				} else if(v1 < v2) {
					return 1; //a URL with more balance value appears first
				} else {
					return -1;
				}
			};
			break;
		case SORT_BY_DICT_ORDER:
			comp = (url1, url2) -> url1.compareToIgnoreCase(url2);
			break;
		case SORT_BY_VISIT_FREQ:
			//a URL with more frequency value appears first
			comp = (url1, url2) -> db.getVisitFrequencyForURL(url2) - db.getVisitFrequencyForURL(url1);
			break;
		default: //never happens
			comp = null;
		}
		allData.sort(comp);
	}
	
	void display() {
		eventListener.nullifySelection();
		lstOutput.removeAll();
		doSort();
		numPages = (int) Math.ceil((double) allData.size() / (double) pageSize);
		pages = new ArrayList<>(numPages);
		String[] page = null;
		int inPageIndex = -1;
		for(int i = 0; i < allData.size(); i++) {
			if((i % pageSize) == 0) {
				page = new String[pageSize];
				pages.add(page);
				inPageIndex = 0;
			}
			page[inPageIndex++] = allData.get(i);
		}
		currentPage = -1;
		moveForward();
	}
	
	int getPageSize() {
		return pageSize;
	}
	
	void setPageSize(int pageSize) {
		this.pageSize = pageSize;
	}
}
