package com.ibm.sg.idaastesting;

import org.eclipse.swt.widgets.Display;

import com.ibm.sg.idaastesting.view.AppWindow;

public class Main {
	/**
	 * Launch the application.
	 * 
	 * @param args
	 */
	public static void main(String args[]) {
		try {
			AppWindow window = new AppWindow();
			window.setBlockOnOpen(true);
			window.open();
			Display.getCurrent().dispose();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
