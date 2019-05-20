import javax.swing.*;
import javax.swing.event.MenuEvent;
import javax.swing.event.MenuListener;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

public class UI extends JFrame implements ActionListener, MenuListener {
	Dimension windowsSize = new Dimension(250, 200);
	private JLabel keyLabel, filePickLabel;
	private JButton encryptButton, decryptButton, filePickButton;
	private JTextField filePathTextArea;
	private JPasswordField keyTextArea;
	private JMenuBar menuBar;
	private JMenu menu;
	private File fileToEncrypt;

	public UI(String title) {
		super(title);
		this.setLayout(new GridLayout());

		initWindowOption();
		initMembers();
		initMembersOptions();
		setListeners();
		addMembers();
		pack();
	}

	private void addMembers() {
		add(filePickLabel);
		add(filePathTextArea);
		add(filePickButton);
		add(keyLabel);
		add(keyTextArea);
		add(encryptButton);
		add(decryptButton);
		this.setJMenuBar(menuBar);
		menu.addActionListener(this);
	}

	private void setListeners() {
		filePickButton.addActionListener(this);
		encryptButton.addActionListener(this);
		decryptButton.addActionListener(this);
	}

	private void initMembersOptions() {
		filePathTextArea.setText("Scieżka do pliku...");
		filePathTextArea.setEditable(false);
	}

	private void initMembers() {
		keyLabel = new JLabel("Podaj klucz: ");
		filePickLabel = new JLabel("Wybierz lokalizację pliku");
		encryptButton = new JButton("Zaszyfruj");
		decryptButton = new JButton("Odszyfruj");
		filePickButton = new JButton("Wybierz");
		filePathTextArea = new JTextField();
		keyTextArea = new JPasswordField();
		menuBar = new JMenuBar();
		menu = new JMenu();
		menuBar.add(menu);
	}

	private void initWindowOption() {
		setResizable(false);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setVisible(true);
	}

	public static void main(String args[]) {
		try {
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		} catch (Exception e) {

		}
		new UI("Szyfrator");
	}


	@Override
	public void actionPerformed(ActionEvent e) {
		if (e.getSource().equals(filePickButton)) {
			encryptButton.setText("Wybierz lokalizacje pliku");
			JFileChooser chooser = new JFileChooser();
			chooser.setCurrentDirectory(new File(System.getProperty("user.dir")));
			int returnVal = chooser.showOpenDialog(this);
			if (returnVal == JFileChooser.APPROVE_OPTION) {
				fileToEncrypt = chooser.getSelectedFile();
				filePathTextArea.setText(fileToEncrypt.getAbsolutePath());
				encryptButton.setText("Zaszyfruj");
			}
		} else if (e.getSource().equals(encryptButton)) {
			if (
					fileToEncrypt != null &&
							fileToEncrypt.getAbsolutePath().endsWith(".txt") &&
							keyTextArea.getText().length() <= 10 &&
							keyTextArea.getText().length() > 0
			) {
				encryptButton.setText("Zaszyfruj");
				Encrypt.encrypt(fileToEncrypt, keyTextArea.getText(), this);
			} else if (fileToEncrypt == null) {
				encryptButton.setText("Plik nie został wybrany/błędny typ pliku.");
			} else if (keyTextArea.getText().length() == 0 || keyTextArea.getText().length() > 10) {
				encryptButton.setText("Niepoprawny klucz");
			}
		} else if (e.getSource().equals(decryptButton)) {
			if (
					fileToEncrypt != null &&
							fileToEncrypt.getAbsolutePath().endsWith(".enc") &&
							keyTextArea.getText().length() <= 10 &&
							keyTextArea.getText().length() > 0
			) {
				encryptButton.setText("Zaszyfruj");
				Encrypt.decrypt(fileToEncrypt, keyTextArea.getText(), this);
			} else if (fileToEncrypt == null) {
				encryptButton.setText("Plik nie został wybrany/błędny typ pliku.");
			} else if (
					keyTextArea.getText().length() == 0 ||
							keyTextArea.getText().length() > 10
			) {
				encryptButton.setText("Klucz nieprawidłowy");
			}
		}
	}


	@Override
	public void menuSelected(MenuEvent e) {
		menu.setSelected(false);
		JOptionPane.showMessageDialog(
				this, "Wybierz plik i wprowadź klucz.\n" +
						"Plik powinien mieć rozszerzenie .txt\n" +
						"Klucz nie może być dłuższy niż 10 zanków.",
				"Pomoc",
				JOptionPane.INFORMATION_MESSAGE
		);
	}

	@Override
	public void menuDeselected(MenuEvent e) {

	}

	@Override
	public void menuCanceled(MenuEvent e) {

	}

	public void notifyFileEncrypted() {
		JOptionPane.showMessageDialog(
				this,
				"Plik został zaszyfrowany!",
				"Informacja",
				JOptionPane.INFORMATION_MESSAGE
		);

		fileToEncrypt = null;
		filePathTextArea.setText("Scieżka do pliku...");
		keyTextArea.setText("");
	}

	public void notifyFileDecrypted() {
		JOptionPane.showMessageDialog(
				this,
				"Plik został odszyfrowany!",
				"Informacja",
				JOptionPane.INFORMATION_MESSAGE
		);

		fileToEncrypt = null;
		filePathTextArea.setText("Scieżka do pliku...");
		keyTextArea.setText("");
	}
}
