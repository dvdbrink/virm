package nl.clockwork.virm.server;

import java.awt.BorderLayout;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Observable;
import java.util.Observer;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTable;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import javax.swing.table.DefaultTableModel;

public class ServerView implements Observer {
	private static final String DATE_FORMAT_NOW = "HH:mm:ss";

	private DefaultTableModel connectionsTableModel, logTableModel;

	public ServerView() {
		try {
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (InstantiationException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (UnsupportedLookAndFeelException e) {
			e.printStackTrace();
		}

		initializeGUI();
	}

	private void initializeGUI() {
		JFrame frame = new JFrame();
		frame.setResizable(false);
		frame.setTitle("virm-server");
		frame.setBounds(500, 500, 700, 300);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setVisible(true);

		JTabbedPane tabbedPane = new JTabbedPane(JTabbedPane.TOP);
		frame.add(tabbedPane, BorderLayout.CENTER);

		initializeConnectionsTab(tabbedPane);
		initializeLogTab(tabbedPane);
	}

	private void initializeConnectionsTab(JTabbedPane tabbedPane) {
		JPanel connectionsPanel = new JPanel();
		tabbedPane.addTab("Connections", null, connectionsPanel, null);
		connectionsPanel.setLayout(new BorderLayout());

		connectionsTableModel = new DefaultTableModel();
		JTable connectionTable = new JTable(connectionsTableModel);
		connectionsTableModel.addColumn("SSID");
		connectionsTableModel.addColumn("IP");
		connectionsTableModel.addColumn("Port");
		connectionsTableModel.addColumn("Status");

		JScrollPane connectionScrollPane = new JScrollPane(connectionTable);
		connectionScrollPane
				.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
		connectionsPanel.add(connectionScrollPane);
	}

	private void initializeLogTab(JTabbedPane tabbedPane) {
		JPanel logPanel = new JPanel();
		tabbedPane.addTab("Log", null, logPanel, null);
		logPanel.setLayout(new BorderLayout());

		logTableModel = new DefaultTableModel();
		JTable logTable = new JTable(logTableModel);
		logTableModel.addColumn("Timestamp");
		logTableModel.addColumn("Level");
		logTableModel.addColumn("SSID");
		logTableModel.addColumn("Message");

		logTable.getColumnModel().getColumn(0).setMaxWidth(200);
		logTable.getColumnModel().getColumn(1).setMaxWidth(100);
		logTable.getColumnModel().getColumn(2).setMaxWidth(100);

		JScrollPane logScrollPane = new JScrollPane(logTable);
		logScrollPane
				.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
		logPanel.add(logScrollPane);
	}

	private String now() {
		Calendar cal = Calendar.getInstance();
		SimpleDateFormat sdf = new SimpleDateFormat(DATE_FORMAT_NOW);
		return sdf.format(cal.getTime());
	}

	public void addLogText(long ssid, String message) {
		if (!message.isEmpty()) {
			logTableModel.insertRow(0, new Object[] { now(), "Debug", ssid,
					message });
		}
	}

	public void addConnection(long ssid, String ip, String port, String status) {
		if (!connectionExists(ssid)) {
			connectionsTableModel.insertRow(0, new Object[] { ssid, ip, port,
					status });
		} else {
			setConnectionStatus(ssid, status);
		}
	}

	public boolean connectionExists(long ssid) {
		for (int row = 0; row < connectionsTableModel.getRowCount(); row++) {
			if (ssid == (Long) connectionsTableModel.getValueAt(row, 0)) {
				return true;
			}
		}
		return false;
	}

	public void setConnectionStatus(long ssid, String status) {
		for (int row = 0; row < connectionsTableModel.getRowCount(); row++) {
			if (ssid == (Long) connectionsTableModel.getValueAt(row, 0)) {
				connectionsTableModel.setValueAt(status, row, 3);
			}
		}
	}

	@Override
	public void update(Observable arg0, Object arg1) {
		// TODO Auto-generated method stub
		
	}
}
