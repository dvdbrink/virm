package nl.clockwork.virm.server.view;

import java.awt.BorderLayout;
import java.util.Observable;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTable;
import javax.swing.SwingConstants;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import javax.swing.table.DefaultTableModel;

import nl.clockwork.virm.log.Log;
import nl.clockwork.virm.server.controller.Controller;
import nl.clockwork.virm.server.net.Connection;

public class BasicGUI implements View {
	private boolean loggerInitialized;
	private DefaultTableModel connections, log;
	public JButton startButton;

	public BasicGUI() {
		BasicGUILogger logger;
		if ((logger = (BasicGUILogger) Log.get("nl.clockwork.virm.server.view.BasicGUILogger")) != null) {
			logger.addObserver(this);
			loggerInitialized = true;
		} else {
			loggerInitialized = false;
		}

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

		initializeControlTab(tabbedPane);
		initializeConnectionsTab(tabbedPane);
		initializeLogTab(tabbedPane);
	}

	private void initializeControlTab(JTabbedPane tabbedPane) {
		JPanel controlPanel = new JPanel();
		tabbedPane.addTab("Control", null, controlPanel, null);
		controlPanel.setLayout(new BorderLayout());

		startButton = new JButton("Start");
		controlPanel.add(startButton);
	}

	private void initializeConnectionsTab(JTabbedPane tabbedPane) {
		JPanel connectionsPanel = new JPanel();
		tabbedPane.addTab("Connections", null, connectionsPanel, null);
		connectionsPanel.setLayout(new BorderLayout());

		connections = new DefaultTableModel();
		JTable connectionTable = new JTable(connections);
		connections.addColumn("SSID");
		connections.addColumn("IP");
		connections.addColumn("Port");
		connections.addColumn("Status");

		JScrollPane connectionScrollPane = new JScrollPane(connectionTable);
		connectionScrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
		connectionsPanel.add(connectionScrollPane);
	}

	private void initializeLogTab(JTabbedPane tabbedPane) {
		JPanel logPanel = new JPanel();
		tabbedPane.addTab("Log", null, logPanel, null);
		logPanel.setLayout(new BorderLayout());

		if (loggerInitialized) {
			log = new DefaultTableModel();
			JTable logTable = new JTable(log);
			log.addColumn("Timestamp");
			log.addColumn("Level");
			log.addColumn("SSID");
			log.addColumn("Message");

			logTable.getColumnModel().getColumn(0).setMaxWidth(200);
			logTable.getColumnModel().getColumn(1).setMaxWidth(100);
			logTable.getColumnModel().getColumn(2).setMaxWidth(100);

			JScrollPane logScrollPane = new JScrollPane(logTable);
			logScrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
			logPanel.add(logScrollPane);
		} else {
			JLabel missingLoggerMessage = new JLabel("Could not find logger.");
			missingLoggerMessage.setHorizontalAlignment(SwingConstants.CENTER);
			logPanel.add(missingLoggerMessage);
		}
	}

	public void addController(Controller controller) {
		startButton.addActionListener(controller);
	}

	private void addConnection(Connection c) {
		int duplicateRow = -1;
		if ((duplicateRow = connectionExists(c)) > -1) {
			connections.setValueAt(c.getStatus(), duplicateRow, 3);
		} else {
			connections.insertRow(0, new Object[] { c.getSSID(), c.getHostAddress(), c.getPort(), c.getStatus() });
		}
	}
	
	private int connectionExists(Connection c) {
		if (connections.getRowCount() > 0) {
			for (int row = 0; row <= connections.getRowCount(); row++) {
				if (c.getSSID() == (Long) connections.getValueAt(row, 0)) {
					return row;
				}
			}
		}
		return -1;
	}
	
	private void addLogMessage(String[] msg) {
		log.insertRow(0, msg);
	}

	@Override
	public void update(Observable source, Object arg) {
		if (arg instanceof Connection) {
			addConnection((Connection) arg);
		} else if (arg instanceof String[]) {
			addLogMessage((String[]) arg);
		}
	}
}
