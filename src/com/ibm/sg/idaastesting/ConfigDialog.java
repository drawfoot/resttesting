package com.ibm.sg.idaastesting;

import java.io.IOException;
import java.util.Properties;

import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Text;
import org.eclipse.swt.custom.SashForm;
import com.ibm.sg.idaastesting.model.IdaasTestingConfig;

public class ConfigDialog extends Dialog {
	private Text textHost;
	private Text textTenant1;
	private Text textTenant2;
	private Text textAppId;
	private Button btnRadioRemote;
	private Button btnRadioLocal;

	public ConfigDialog(Shell parentShell) {
		super(parentShell);
	}

	@Override
	protected Control createDialogArea(Composite parent) {

		Composite container = (Composite) super.createDialogArea(parent);
		GridLayout gl_container = new GridLayout(3, true);
		gl_container.marginLeft = 30;
		gl_container.marginRight = 30;
		gl_container.marginWidth = 0;
		gl_container.marginHeight = 0;
		gl_container.marginBottom = 30;
		gl_container.marginTop = 30;
		gl_container.verticalSpacing = 50;
		container.setLayout(gl_container);

		Label lblHost = new Label(container, SWT.HORIZONTAL);
		lblHost.setSize(46, 15);
		lblHost.setText("Host");

		textHost = new Text(container, SWT.BORDER);
		textHost.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, false, false,
				1, 1));

		Composite composite = new Composite(container, SWT.NONE);
		composite.setLayoutData(new GridData(SWT.FILL, SWT.FILL, false, false,
				1, 1));
		composite.setLayout(new GridLayout(2, false));

		btnRadioRemote = new Button(composite, SWT.RADIO);
		btnRadioRemote.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				textHost.setEnabled(true);
			}
		});
		btnRadioRemote.setText("Remote");

		btnRadioLocal = new Button(composite, SWT.RADIO);
		btnRadioLocal.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				textHost.setEnabled(false);
			}
		});
		btnRadioLocal.setText("Local Host");

		Label lblNewLabel = new Label(container, SWT.NONE);
		lblNewLabel.setSize(46, 15);
		lblNewLabel.setText("Tenant 1");

		textTenant1 = new Text(container, SWT.BORDER);
		GridData gd_textTenant1 = new GridData(SWT.FILL, SWT.CENTER, false,
				false, 1, 1);
		gd_textTenant1.widthHint = 62;
		textTenant1.setLayoutData(gd_textTenant1);
		new Label(container, SWT.NONE);

		Label lblTenant = new Label(container, SWT.NONE);
		lblTenant.setSize(46, 15);
		lblTenant.setText("Tenant 2");

		textTenant2 = new Text(container, SWT.BORDER);
		textTenant2.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, false,
				false, 1, 1));
		new Label(container, SWT.NONE);

		Label lblAppId = new Label(container, SWT.NONE);
		lblAppId.setSize(46, 15);
		lblAppId.setText("App ID");

		textAppId = new Text(container, SWT.BORDER);
		textAppId.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, false,
				false, 1, 1));
		new Label(container, SWT.NONE);

		// init dialogs
		readConfig();

		return container;
	}

	@Override
	protected void okPressed() {
		saveConfig();
		super.okPressed();
	}

	// overriding this methods allows you to set the
	// title of the custom dialog
	@Override
	protected void configureShell(Shell newShell) {
		super.configureShell(newShell);
		newShell.setText("Configuration dialog");
	}

	private void saveConfig() {
		try {
			IdaasTestingConfig config = IdaasTestingConfig.getInstance();
			Properties prop = config.getProp();
			if (btnRadioLocal.getSelection()) {
				prop.setProperty(IdaasTestingConfig.CFG_HOST, "localhost");
				prop.setProperty(IdaasTestingConfig.CFG_HOSTMODE, "local");
			} else {
				prop.setProperty(IdaasTestingConfig.CFG_HOST, textHost
						.getText().trim());
				prop.setProperty(IdaasTestingConfig.CFG_HOSTMODE, "remote");
			}
			prop.setProperty(IdaasTestingConfig.CFG_TENANT1, textTenant1
					.getText().trim());
			prop.setProperty(IdaasTestingConfig.CFG_TENANT2, textTenant2
					.getText().trim());
			prop.setProperty(IdaasTestingConfig.CFG_APPID, textAppId.getText()
					.trim());
			config.save();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private void readConfig() {
		try {
			IdaasTestingConfig config = IdaasTestingConfig.getInstance();
			Properties prop = config.getProp();
			// host
			if (prop.getProperty(IdaasTestingConfig.CFG_HOSTMODE) != null
					&& prop.getProperty(IdaasTestingConfig.CFG_HOSTMODE)
							.equals("remote")) {
				btnRadioRemote.setSelection(true);
				textHost.setEnabled(true);
				textHost.setText(prop.getProperty(IdaasTestingConfig.CFG_HOST));
			} else {
				btnRadioLocal.setSelection(true);
				textHost.setEnabled(false);
			}

			// tenants
			String tenant1 = prop.getProperty(IdaasTestingConfig.CFG_TENANT1);
			if (tenant1 != null)
				textTenant1.setText(tenant1);
			String tenant2 = prop.getProperty(IdaasTestingConfig.CFG_TENANT2);
			if (tenant2 != null)
				textTenant2.setText(tenant2);

			// appid
			String appid = prop.getProperty(IdaasTestingConfig.CFG_APPID);
			if (appid != null)
				textAppId.setText(appid);

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
