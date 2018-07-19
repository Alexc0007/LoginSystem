import java.awt.EventQueue;
import javax.swing.JFrame;
import javax.swing.JPanel;
import java.awt.BorderLayout;
import java.awt.Color;
import javax.swing.JLabel;
import java.awt.Font;
import javax.swing.JTextField;
import javax.swing.JRadioButton;
import javax.swing.ButtonGroup;
import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import javax.swing.JTextArea;

public class LoginScreen {

	private JFrame frame;
	private JTextField usernameTF;
	private JTextField passwordTF;
	private final ButtonGroup buttonGroup = new ButtonGroup();
	private JTextArea screenTA;
	private Hash hash;
	private Authenticate authenticator;
	private Logger logger;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					LoginScreen window = new LoginScreen();
					window.frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the application.
	 */
	public LoginScreen() {
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() 
	{
		//create the logger , authenticator and hash objects
		authenticator = new Authenticate();
		hash = new Hash();
		logger = new Logger();
		String screenInitialText = "Please insert a Username and Password\r\nPassword: length between 8 and 12 characters ,can use any numbers , should contain at least 1 uppercase letter\r\nand exactly 1 special character from the following characters:\r\n+, -, &, #, |, !, (, ), {, }, [, ], ^,~, *, ?, : , \" , \\\r\n\r\nUsername:  length between 3 and 10 characters , should contain lowercase / uppercase letters";
		
		frame = new JFrame();
		frame.setBounds(100, 100, 691, 487);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		JPanel panel = new JPanel();
		panel.setBackground(Color.BLUE);
		frame.getContentPane().add(panel, BorderLayout.CENTER);
		panel.setLayout(null);
		
		usernameTF = new JTextField();
		usernameTF.setBounds(291, 288, 149, 20);
		panel.add(usernameTF);
		usernameTF.setColumns(10);
		
		JLabel lblLogin = new JLabel("Login");
		lblLogin.setBounds(313, 11, 39, 20);
		lblLogin.setForeground(Color.YELLOW);
		lblLogin.setFont(new Font("Tahoma", Font.PLAIN, 16));
		panel.add(lblLogin);
		
		JLabel usernameLBL = new JLabel("User Name:");
		usernameLBL.setForeground(Color.YELLOW);
		usernameLBL.setBounds(214, 291, 67, 14);
		panel.add(usernameLBL);
		
		passwordTF = new JTextField();
		passwordTF.setBounds(291, 319, 149, 20);
		panel.add(passwordTF);
		passwordTF.setColumns(10);
		
		JLabel passwordLBL = new JLabel("Password:");
		passwordLBL.setForeground(Color.YELLOW);
		passwordLBL.setBounds(214, 322, 67, 14);
		panel.add(passwordLBL);
		
		JRadioButton rdbtnNewUser = new JRadioButton("New User");
		buttonGroup.add(rdbtnNewUser);
		rdbtnNewUser.setForeground(Color.ORANGE);
		rdbtnNewUser.setBackground(Color.BLUE);
		rdbtnNewUser.setBounds(313, 213, 102, 23);
		panel.add(rdbtnNewUser);
		
		
		JRadioButton rdbtnExistingUser = new JRadioButton("Existing User");
		rdbtnExistingUser.setSelected(true);
		buttonGroup.add(rdbtnExistingUser);
		rdbtnExistingUser.setForeground(Color.ORANGE);
		rdbtnExistingUser.setBackground(Color.BLUE);
		rdbtnExistingUser.setBounds(313, 239, 102, 23);
		panel.add(rdbtnExistingUser);
		
		JButton btnLogin = new JButton("Login");
		btnLogin.addActionListener(new ActionListener() 
		{
			public void actionPerformed(ActionEvent e) 
			{
				String username = usernameTF.getText();
				String password = passwordTF.getText();
				boolean verifier;
				//perform action depending on the radio buttons status
				if(rdbtnExistingUser.isSelected())
				{
					//need to check the inserted password and username and if they pass authentication - look for them in the passwords file
					verifier = authenticator.authenticateUsername(username);
					if(!verifier)
					{
						screenTA.setText("the username provided isnt compatible with the system's rules! \n" + screenInitialText);
						return;
					}
					verifier = authenticator.authenticatePassword(password);
					if(!verifier)
					{
						screenTA.setText("the password provided isnt compatible with the system's rules! \n" + screenInitialText);
						return;
					}
					String fetchedSalt = logger.getSalt(username);
					if(fetchedSalt.compareTo("NO") == 0) //means the username was not found on the passwords file
					{
						screenTA.setText("the username provided was not found on the system.");
						return;
					}
					//if got here - means the salt of the current user is fetched from the system
					String generatedCurrentHash = hash.getHash(password, fetchedSalt); //generate hash for the currently inserted password
					verifier = logger.confirmCredentials(username, generatedCurrentHash);
					if(verifier)
					{
						screenTA.setText("User " + username + " Successfully logged in!");
					}
					else
					{
						screenTA.setText("Incorrect Password! Login Failed!");
					}
					
				}
				else if(rdbtnNewUser.isSelected())
				{
					//need to check the inserted password and username and if they pass authentication - write it to the passwords file
					verifier = authenticator.authenticateUsername(username);
					if(!verifier)
					{
						screenTA.setText("the username provided isnt compatible with the system's rules! \n" + screenInitialText);
						return;
					}
					verifier = authenticator.authenticatePassword(password);
					if(!verifier)
					{
						screenTA.setText("the password provided isnt compatible with the system's rules! \n" + screenInitialText);
						return;
					}
					//before proceeding - need to make sure the username isnt already taken.
					String checkUser = logger.getSalt(username);
					if(checkUser.compareTo("NO") != 0) //means it found the exact same username on the system
					{
						screenTA.setText("the username picked is already in use , please pick another. \n" + screenInitialText);
						return;
					}
					//if both username and password passed authentication - need to make a hash out of the password and store it all to the file
					String salt = hash.generateSalt(); //get a salt value
					String hashValue = hash.getHash(password, salt);
					if(logger.storeCredentials(username, hashValue, salt))
					{
						screenTA.setText("Username " + username + " was successfully added to the system!");
					}
					else
					{
						screenTA.setText("Failure while adding " + username + " to the system!");
					}
					
				}
				
			}
		});
		btnLogin.setBounds(313, 414, 89, 23);
		panel.add(btnLogin);
		
		screenTA = new JTextArea();
		screenTA.setEditable(false);
		screenTA.setText(screenInitialText);
		screenTA.setBounds(10, 69, 655, 137);
		panel.add(screenTA);
		screenTA.setColumns(10);
		
		JLabel lblSystemMessage = new JLabel("System Message:");
		lblSystemMessage.setForeground(Color.RED);
		lblSystemMessage.setBounds(291, 47, 117, 14);
		panel.add(lblSystemMessage);
		
		rdbtnNewUser.addActionListener(new ActionListener() 
		{
			@Override
			public void actionPerformed(ActionEvent e) 
			{	
				btnLogin.setText("Submit"); //change the login button's text to submit
			}
		});
		rdbtnExistingUser.addActionListener(new ActionListener() 
		{
			@Override
			public void actionPerformed(ActionEvent e) 
			{	
				btnLogin.setText("Login"); //change the login button's text to submit
			}
		});
	}
}
