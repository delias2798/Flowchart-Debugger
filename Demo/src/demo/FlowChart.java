package demo;
import java.awt.BorderLayout;

import javax.swing.JLabel;
import javax.swing.JPanel;

import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.SWT;

public final class FlowChart {
	/**
	 * @wbp.factory
	 */
	public static Button createButton(final Composite parent) {
		Button button = new Button(parent, SWT.NONE);
		button.setText("Push meeeeee");
		button.addListener(SWT.Selection, new Listener() {
		      public void handleEvent(Event e) {
		        switch (e.type) {
		        case SWT.Selection:
		        	final JPanel panel = new JPanel();
					panel.setBounds(127, 23, 200, 400);
					//parent.add(panel);
					JLabel statusbar;
					statusbar = new JLabel(" 0");
			        panel.add(statusbar, BorderLayout.SOUTH);
			        Tablero tablero = new Tablero(panel, statusbar);
			        panel.setVisible(true);
			        tablero.setVisible(true);
			        tablero.start();
			        //parent.add(tablero);
			        System.out.println(tablero.statusbar);
		          System.out.println("Button pressed");
		          break;
		        }
		      }
		    });
		return button;
	}
}