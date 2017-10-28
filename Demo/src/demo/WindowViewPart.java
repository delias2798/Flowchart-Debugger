package demo;

import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.part.ViewPart;
import org.eclipse.swt.events.DisposeListener;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.GridLayout;

import javax.swing.*;
import javax.swing.JPanel.*;
import javax.swing.border.EmptyBorder;
import javax.swing.JButton;
import javax.swing.JTextPane;

import java.awt.BorderLayout;
import java.awt.Toolkit;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.io.File;
import java.net.URL;

import javax.swing.ImageIcon;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JScrollBar;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JToolBar;
import javax.swing.JMenuBar;
import javax.swing.JMenu;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import org.eclipse.swt.widgets.Text;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.List;
import org.eclipse.swt.widgets.ToolItem;
import org.eclipse.swt.widgets.Canvas;
import org.eclipse.swt.custom.CLabel;

public class WindowViewPart extends ViewPart {
	

	public WindowViewPart() {
		// TODO Auto-generated constructor stub
	}

	@Override
	public void createPartControl(Composite parent) {
		parent.setLayout(new GridLayout(3, false));
		
		List list = new List(parent, SWT.BORDER);
		GridData gd_list = new GridData(SWT.LEFT, SWT.CENTER, false, false, 1, 1);
		gd_list.heightHint = 240;
		list.setLayoutData(gd_list);
		
		CLabel lblNewLabel = new CLabel(parent, SWT.NONE);
		GridData gd_lblNewLabel = new GridData(SWT.LEFT, SWT.CENTER, false, false, 1, 2);
		gd_lblNewLabel.heightHint = 377;
		gd_lblNewLabel.widthHint = 192;
		lblNewLabel.setLayoutData(gd_lblNewLabel);
		lblNewLabel.setText("New Label");
		
		List list_1 = new List(parent, SWT.BORDER);
		GridData gd_list_1 = new GridData(SWT.LEFT, SWT.TOP, false, false, 1, 1);
		gd_list_1.heightHint = 103;
		gd_list_1.widthHint = 143;
		list_1.setLayoutData(gd_list_1);
		
	//	Button btnPushMeeeeee = FlowChart.createButton(parent);

		
		List list_2 = new List(parent, SWT.BORDER);
		GridData gd_list_2 = new GridData(SWT.LEFT, SWT.CENTER, false, false, 1, 1);
		gd_list_2.heightHint = 101;
		gd_list_2.widthHint = 146;
		list_2.setLayoutData(gd_list_2);
		//JTextPane textPane = new JTextPane();
		//textPane.setBounds(10, 23, 107, 103);
		//parent.addDisposeListener((DisposeListener) textPane);
		// TODO Auto-generated method stub

	}

	@Override
	public void setFocus() {
		// TODO Auto-generated method stub

	}

}
