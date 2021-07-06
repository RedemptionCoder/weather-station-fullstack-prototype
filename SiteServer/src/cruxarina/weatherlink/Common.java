/**
 * Contains a lot of common functionality used for Cruxarina projects
 * Included functionalities
 * <ul>
 * <li>Error Logging</li>
 * <li>Message Logging</li>
 * </ul>
 * 
 * @author Anthony Gibbons
 */


package cruxarina.weatherlink;

import java.util.ArrayList;
import java.util.Date;
import java.util.Properties;

import javax.activation.*;

import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.Authenticator;
import javax.mail.Message;
import javax.mail.PasswordAuthentication;
import javax.mail.internet.*;

import java.time.LocalDateTime;
import java.time.format.*;
import java.io.*;



public class Common {
	
	// CONSTRUCTORS
	
	Common() {
		
		cLog = new ArrayList<String>();
		cLastError = "";
		cLastMessage = "";
		cErrorTag = "ERROR: ";
		cLogFilePath = "";
		cLogFileAccessible = false;
		cDebugOutput = false;
		cSendingAccountUsernameEmail = "";
		cFromDisplayName = "";
		cPassword = "";
		cDestinationEmail = "";
		cSmtpServerFQDN = "smtp.gmail.com";	
		cSmtpPort = "587";
		cUseAuth = "true";
		cUseStartTls = "true";
		
	}
	
	// PUBLIC METHODS
	
	/**
	 * Sets the path to the configuration file
	 * @param Path	A path to the configuration file
	 */
	public void setConfigFilePath(String Path) {
		this.cConfig.setFilename(Path);
	}
	
	/**
	 * Gets the path to the config file
	 * @return	The path to the config file
	 */
	public String getConfigFilePath() {
		return this.cConfig.getFilename();
	}
	
	/**
	 * Gets the value of a property from the config file. Will return a zero length
	 * string if the config file was not found, accessible, or setting not found. 
	 * @param PropertyName	The name of the property
	 * @return				The value of the property
	 */
	public String getConfig(String PropertyName) {
		return this.cConfig.getConfig(PropertyName);
	}
	
	/**
	 * Sets the string that will be put in front of errors in the log file
	 * @param errorTag	The error tag to use.
	 */
	public void setErrorTag(String errorTag) {
		cErrorTag = errorTag;
	}
	
	/**
	 * Gets the string that will be put in front of errors in the log file
	 * @return	A String containing the error tag
	 */
	public String getErrorTag() {
		return cErrorTag;
	}
	
	/**
	 * Sets the path to the log file
	 * @param path
	 */
	public void setLogFilePath(String path) {
		cLogFilePath = path;
	}
	
	/**
	 * Gets the path to the log file
	 * @return	The path to the log file
	 */
	public String getLogFilePath() {
		return cLogFilePath;
	}
	
	/**
	 * Gets whether debug output is enabled
	 * @return 	True if enabled, false if not
	 */
	public boolean isDebugOutput() {
		return cDebugOutput;
	}

	/**
	 * Sets whether debug output should be shown
	 * @param debugOutput True if enabled, false if not
	 */
	public void setDebugOutput(boolean debugOutput) {
		cDebugOutput = debugOutput;
	}
	
	/**
	 * Gets whether the log file is accessible or not
	 * @return	True if log file is accessible, false if not.
	 */
	public boolean isLogFileAccessible() {
		return cLogFileAccessible;
	}
	
	/**
	 * The gets username usually in the form of the full email address to use 
	 * for the sending email account.
	 * @return The sending account username/email address
	 */
	public String getSendingAccountUsernameEmail() {
		return cSendingAccountUsernameEmail;
	}

	/**
	 *  Sets the username usually in the form of the full email address to use 
	 * for the sending email account.
	 * @param sendingAccountUsernameEmail The The sending account username/email address to set
	 */
	public void setSendingAccountUsernameEmail(String sendingAccountUsernameEmail) {
		cSendingAccountUsernameEmail = sendingAccountUsernameEmail;
	}

	/**
	 * Gets the actual person name that will come up as the sender
	 * @return The sender displayName
	 */
	public String getFromDisplayName() {
		return cFromDisplayName;
	}

	/**
	 * Sets the actual person name that will come up as the sender
	 * @param fromDisplayName The sender displayName
	 */
	public void setFromDisplayName(String fromDisplayName) {
		cFromDisplayName = fromDisplayName;
	}

	/**
	 * Gets the plain text password being used to authenticate with the smtp server
	 * @return The password
	 */
	public String getPassword() {
		return cPassword;
	}

	/**
	 * Sets the plain text password being used to authenticate with the smtp server
	 * @param The password
	 */
	public void setPassword(String password) {
		cPassword = password;
	}

	/**
	 * Gets the destination email address to which to send the alerts to
	 * @return The destination email address 
	 */
	public String getDestinationEmail() {
		return cDestinationEmail;
	}

	/**
	 * Sets the destination email address to which to send the alerts to
	 * @param destinationEmail The destination email address
	 */
	public void setDestinationEmail(String destinationEmail) {
		cDestinationEmail = destinationEmail;
	}

	/**
	 * Gets the fully qualified domain name of the smtp server 
	 * that will send the email
	 * @return The FQDN of the sending smtp server
	 */
	public String getSmtpServerFQDN() {
		return cSmtpServerFQDN;
	}

	/**
	 * Sets the fully qualified domain name of the smtp server 
	 * that will send the email
	 * @param smtpServerFQDN The FQDN of the sending smtp server
	 */
	public void setSmtpServerFQDN(String smtpServerFQDN) {
		cSmtpServerFQDN = smtpServerFQDN;
	}

	/**
	 * Gets the port being used for the smtp server
	 * @return The smtp port
	 */
	public String getSmtpPort() {
		return cSmtpPort;
	}

	/**
	 * Gets the port being used for the smtp server
	 * @param The smtp port
	 */
	public void setSmtpPort(String smtpPort) {
		cSmtpPort = smtpPort;
	}

	/**
	 * Gets whether to use authentication with the smtp server
	 * @return A string either true or false
	 */
	public String getUseAuth() {
		return cUseAuth;
	}

	/**
	 * Sets whether to use authentication with the smtp server
	 * @param useAuth A string either true or false
	 */
	public void setUseAuth(String useAuth) {
		cUseAuth = useAuth.toLowerCase();
	}

	/**
	 * Gets whether to use STARTTLS or not
	 * @return A string either true or false
	 */
	public String getUseStartTls() {
		return cUseStartTls;
	}

	/**
	 * Sets whether to use STARTTLS or not
	 * @param A string either true or false
	 */
	public void setUseStartTls(String useStartTls) {
		cUseStartTls = useStartTls.toLowerCase();
	}

	/**
	 * Adds an error message to the class' error log
	 * @param Message			A string containing an error message
	 */
	public void logError(String Message) {
		
		// Write error to log with error tag
		logErrorOrMessage(cErrorTag + Message);
			
		// Set the last error to the most recent one
		cLastError = Message;
		
	}
	
	/**
	 * Adds an error message to the class' error log and sends an email
	 * alert with the supplied subject.
	 * @param Message			A string containing an error message
	 * @param Subject			The subject to include for the email
	 */
	public void logError(String Message, String Subject) {
		
		// Write error to log with error tag
		logErrorOrMessage(cErrorTag + Message);
		
		// Send the message as an email alert
		sendMailAlert(Message, Subject);
			
		// Set the last error to the most recent one
		cLastError = Message;
		
	}
	
	/**
	 * Adds a message to the class' message log
	 * @param Message			A string containing an message
	 */
	public void logMessage(String Message) {
		
		// Write message to log
		logErrorOrMessage(Message);
		
		// Set the last message to the most recent one
		cLastMessage = Message;
		
	}
	
	/**
	 * Adds a message to the class' message log and sends an email
	 * alert with the supplied subject.
	 * @param Message			A string containing an message
	 * @param Subject			The subject to include for the email
	 */
	public void logMessage(String Message, String Subject) {
		
		// Write message to log
		logErrorOrMessage(Message);
		
		// Send the message as an email alert
		sendMailAlert(Message, Subject);
		
		// Set the last message to the most recent one
		cLastMessage = Message;
		
	}
	
	/**
	 * Gets the last error that was logged
	 * @return The last error message
	 */
	public String lastError() {
		return cLastError;
	}
	
	/**
	 * Returns the last message that was logged
	 * @return The last message
	 */
	public String lastMessage() {
		return cLastMessage;
	}
	
	/**
	 * Attempts to save all messages and errors to the log file.
	 * @return	True if successful, false if not
	 */
	public boolean saveLogToFile() {
		
		
		if (cLog.size() > 0) {
			
			try {
			
				// Create or access the file
				File lLogFile = new File(cLogFilePath);
				
				// Create the file writer, and create the file if it doesn't exist
				FileWriter lLogWriter = new FileWriter(lLogFile);
				
				for (String lMessage : cLog) {
					
						// Write the message to the file
						lLogWriter.write(lMessage + "\n");
						
				}
				
				// Close the file
				lLogWriter.close();
					
			} catch (Exception e) {

				logError(e.getMessage());
				
				// File system issues, return false
				return false;
					
			}
				
		}
			
		// Message successfully written to the log file or there were no messages
		return true;
		
	}
	
	/**
	 * Sends to contents of the logs in memory to standard output
	 */
	public void printLogs() {
		
		if (cLog.size() > 0) {
				
			for (String lMessage : cLog) {
				
				// Write the message to the file
				System.out.println(lMessage);
					
			}
				
		}
		
	}
	
	
	// PRIVATE METHODS
	
	/**
	 * Writes a message to the log file. If the log file cannot be created or is not accessible
	 * the function returns a false. Each message will have a date and time preceding the message
	 * @param message	The message to be written to the log file.
	 * @return			True if success, false if not.
	 */
	private boolean writeToLogFile(String message) {
		
		// Create or access the file
		File lLogFile = new File(cLogFilePath);

		try {
			
			// Check if the log file exists
			if (!lLogFile.canWrite()) {
				
				// Try and create the log file
				if (!lLogFile.createNewFile()) {
					throw new Exception("Log file could not be created");
				}
			}
			
			// Create the file writer, and create the file if it doesn't exist
			FileWriter lLogWriter = new FileWriter(lLogFile, true);
			
			// Write the message to the file
			lLogWriter.write(getTimeStamp() + " " + message + System.lineSeparator());
			
			// Close the file
			lLogWriter.close();
			
		} catch (Exception e) {
			
			// Debug output
			debugOut(e, "Could not create file");
			
			// File system issues, return false
			return false;
			
		}

		// Message successfully written to the log file
		return true;
		
	}

	/**
	 * Tries to write a message or error to the log file. If this fails, the error or message gets added
	 * to log in memory.
	 * @param Message	The message or error to be logged
	 */
	private void logErrorOrMessage(String Message) {
	
		if (!writeToLogFile(Message)) {
			
			// Could not write the message to the log file, so store it in memory
			cLog.add(getTimeStamp() + " " + Message);
			
			// Mark log file as not usable
			cLogFileAccessible = false;
		}
		
		// Mark log file as usable
		cLogFileAccessible = true;
		
	}
	
	/**
	 * Gets a timestamp to add to the log file
	 * @return	String containing the timestamp
	 */
	protected String getTimeStamp() {
		
		LocalDateTime lTimestamp = LocalDateTime.now();
		return lTimestamp.format(DateTimeFormatter.ofPattern("MMM d H:m:s"));
		
	}
	
	private void debugOut(Exception e, String message) {
		if (cDebugOutput) {
			System.out.println(message);
			debugOut(e);
		}
	}
	
	private void debugOut(Exception e) {
		if (cDebugOutput) {
			System.out.println(e.getMessage());
		}
	}
	
	/**
	 * Sends an email alert 
	 * @param message	The message to include
	 * @param subject	The subject to include
	 * @return			True if successful, false if not - errors logged
	 */
	private boolean sendMailAlert(String message, String subject) {
		
		// Create the properties object
		Properties lProperties = new Properties();
		
		// Set the properties for the session
		lProperties.put("mail.smtp.host", cSmtpServerFQDN);
		lProperties.put("mail.smtp.port", cSmtpPort);
		lProperties.put("mail.smtp.auth", cUseAuth);
		lProperties.put("mail.smtp.starttls.enable", cUseStartTls);
		
		// Set up the authentication
		Authenticator lAuth = new Authenticator() {
			protected PasswordAuthentication getPasswordAuthentication() {
				return new PasswordAuthentication(cSendingAccountUsernameEmail, cPassword);
			}
		};
		
		// Create the session
		Session lSession = Session.getDefaultInstance(lProperties, lAuth);
		
		try {
		
			// Set up the message
			MimeMessage lMessage = new MimeMessage(lSession);
						
			lMessage.addHeader("Content-type", "text/HTML; charset=UTF-8");
			lMessage.addHeader("format","flowed");
			lMessage.addHeader("Content-Transfer-Encoding","8bit");
			lMessage.setFrom(new InternetAddress(cSendingAccountUsernameEmail, cFromDisplayName));
			lMessage.setReplyTo(InternetAddress.parse(cSendingAccountUsernameEmail, false));
			lMessage.setSubject(subject, "UTF-8");
			lMessage.setText(message, "UTF-8");
			lMessage.setSentDate(new Date());
			lMessage.setRecipients(Message.RecipientType.TO, InternetAddress.parse(cDestinationEmail, false));
			
			// Try and send the message
			Transport.send(lMessage);
			
		} catch (Exception e) {
			
			// Log the issue in the log file
			logError(e.getMessage());
			
			// Sending failed, return false
			return false;
		}
		
		// Sending successful, return true
		return true;
		
	}
	
	
	// PRIVATE ATTRIBUTES

	// The list of errors that may have occurred
	private ArrayList<String> cLog;
	
	// The last error logged
	private String cLastError;
	
	// The last message that was logged
	private String cLastMessage;
	
	// A configuration file
	private ConfigFile cConfig;
	
	// The string to put in front of errors
	private String cErrorTag;
	
	// The path to the logfile
	private String cLogFilePath;
	
	// Whether the log file is accessible
	private boolean cLogFileAccessible;
	
	// Used for debugging
	private boolean cDebugOutput;
	
	// email related attributes --------------------------------------------
	
	// The email account email address/username used for sending alerts
	private String cSendingAccountUsernameEmail;
	
	// The name to display for sending account user.
	private String cFromDisplayName;
	
	// The password for the sending account
	private String cPassword;
	
	// The destination recipient email address
	private String cDestinationEmail;
	
	// The fully qualified domain name of the smtp server. 
	private String cSmtpServerFQDN;	
	
	// The port for the smtp server to use
	private String cSmtpPort;
	
	// Whether to use authentication or not
	private String cUseAuth;
	
	// Whether or not to use STARTTLS
	private String cUseStartTls;
	
	
	
	
	
	
	
	
	
	
	
	
	
	
}
