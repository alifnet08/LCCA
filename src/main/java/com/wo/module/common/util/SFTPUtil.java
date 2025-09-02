package com.wo.module.common.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.apache.log4j.Logger;

import com.sshtools.net.SocketTransport;
//import com.sshtools.publickey.ConsoleKnownHostsKeyVerification;
import com.sshtools.publickey.SshPrivateKeyFile;
import com.sshtools.publickey.SshPrivateKeyFileFactory;
import com.sshtools.sftp.SftpClient;
import com.sshtools.sftp.SftpFile;
import com.sshtools.ssh.HostKeyVerification;
import com.sshtools.ssh.PasswordAuthentication;
import com.sshtools.ssh.PublicKeyAuthentication;
import com.sshtools.ssh.SshAuthentication;
import com.sshtools.ssh.SshClient;
import com.sshtools.ssh.SshConnector;
import com.sshtools.ssh.SshException;
import com.sshtools.ssh.components.SshKeyPair;
import com.sshtools.ssh.components.SshPublicKey;
import com.sshtools.ssh2.Ssh2Client;
import com.sshtools.ssh2.Ssh2Context;

public class SFTPUtil {
	private String host; // Remote SFTP hostname
	private SshClient ssh;
	private SftpClient sftp;
	// private static final String privateKeyPath =
	// "src/com/btpn/resources/sftp-key/PrivateKey-SF.ppk";
	private String privateKeyPath /* = "/sftp-key/PrivateKey-SF.ppk" */;

	final static Logger logger = Logger.getLogger(SFTPUtil.class);

	public SFTPUtil(String host, String privateKeyPath) {
		this.host = host;
		this.ssh = null;
		this.sftp = null;
		this.privateKeyPath = privateKeyPath;
	}

	public SFTPUtil(String host) {
		this.host = host;
		this.ssh = null;
		this.sftp = null;
	}

	public void connectByUsername(String user, String password) throws Exception {

		// Connect to SSH.
		// ssh = new SshClient();
		int idx = host.indexOf(':');
		int port = 22;
		if (idx > -1) {
			port = Integer.parseInt(host.substring(idx + 1));
			host = host.substring(0, idx);
		}
		try {
			// ssh.connect(host, new ConsoleKnownHostsKeyVerification());
			// ssh.connect(host, new IgnoreHostKeyVerification());
			/**
			 * Create an SshConnector instance
			 */
			SshConnector con = SshConnector.createInstance();

			/**
			 * Connect to the host
			 */

			// System.out.println("Connecting to " + host);

			SocketTransport transport = new SocketTransport(host, port);

			// System.out.println("Creating SSH client");

			ssh = con.connect(transport, user);
			Ssh2Client ssh2 = (Ssh2Client) ssh;
			/**
			 * Authenticate the user using password authentication
			 */
			PasswordAuthentication pwd = new PasswordAuthentication();
			do {
				pwd.setPassword(password);
			} while (ssh2.authenticate(pwd) != SshAuthentication.COMPLETE && ssh.isConnected());

			if (ssh.isAuthenticated()) {
				sftp = new SftpClient(ssh2);
			}
		} catch (Exception e) {
			throw new Exception("SFtp.connectByUsername Fail to open SFTP channel: " + e.getMessage());
		}
	}

	public void connectByKey(String username, String passphraseKey) throws Exception {

		// Connect to SSH.
		// ssh = new SshClient();
		int idx = host.indexOf(':');
		int port = 22;
		if (idx > -1) {
			port = Integer.parseInt(host.substring(idx + 1));
			host = host.substring(0, idx);
		}
		try {
			/**
			 * Create an SshConnector instance
			 */
			SshConnector con = SshConnector.createInstance();

			// Lets do some host key verification
			// Comment these 2 lines below cause it's not working on linux
			// con.getContext().setHostKeyVerification(
			// new ConsoleKnownHostsKeyVerification());
			con.getContext().setPreferredPublicKey(Ssh2Context.PUBLIC_KEY_SSHDSS);

			// con.setSupportedVersions(1);
			// Lets do some host key verification
			HostKeyVerification hkv = new HostKeyVerification() {
				public boolean verifyHost(String hostname, SshPublicKey key) {
					try {
						logger.debug("The connected host's key (" + key.getAlgorithm() + ") is");
						logger.debug(key.getFingerprint());
					} catch (SshException e) {
					}
					return true;
				}
			};

			con.getContext().setHostKeyVerification(hkv);

			/**
			 * Connect to the host
			 */
			ssh = con.connect(new SocketTransport(host, port), username);

			/**
			 * Authenticate the user using password authentication
			 */
			PublicKeyAuthentication pk = new PublicKeyAuthentication();

			do {
				// System.out.print("Private key file: ");
				SshPrivateKeyFile pkfile = SshPrivateKeyFileFactory.parse(new FileInputStream(privateKeyPath));

				SshKeyPair pair;
				if (pkfile.isPassphraseProtected()) {
					// System.out.print("Passphrase: ");
					pair = pkfile.toKeyPair(passphraseKey);
				} else
					pair = pkfile.toKeyPair(null);

				pk.setPrivateKey(pair.getPrivateKey());
				pk.setPublicKey(pair.getPublicKey());
			} while (ssh.authenticate(pk) != SshAuthentication.COMPLETE && ssh.isConnected());

			if (ssh.isAuthenticated()) {
				sftp = new SftpClient(ssh);
				sftp.setTransferMode(SftpClient.MODE_TEXT);
				sftp.setRemoteEOL(SftpClient.EOL_LF);
			}
		} catch (Exception e) {
			throw new Exception("SFtp.connectByKey Authentication Failure: " + e.getMessage());
		}
	}

	public void cd(String remoteDir) throws Exception {

		if (sftp == null)
			throw new Exception("SFtp.cd SFTP channel is not initialized. remoteDir[" + remoteDir + "]");

		if (remoteDir == null || remoteDir.trim().length() == 0)
			throw new Exception("SFtp.cd Remote directory name is not provided. remoteDir[" + remoteDir + "]");

		try {
			sftp.cd(remoteDir);
		} catch (Exception e) {
			throw new Exception(
					"SFtp.cd Failed to change remote directory remoteDir[" + remoteDir + "]: " + e.getMessage());
		}
	}

	public void put(String fileName) throws Exception {

		if (sftp == null)
			throw new Exception("SFtp.put SFTP channel is not initialized. fileName[" + fileName + "]");

		if (fileName == null || fileName.trim().length() == 0)
			throw new Exception("SFtp.put File name is not provided. fileName[" + fileName + "]");

		// Send the file
		try {
			sftp.put(fileName);
			System.out.println("Success put fileName = " + fileName);
		} catch (Exception e) {
			throw new Exception(" SFtp.put Fail to upload file: fileName[" + fileName + "] = " + e.getMessage());
		}
	}

	public void get(String remoteFile, String localFile) throws Exception {

		if (sftp == null)
			throw new Exception("SFtp.get SFTP channel is not initialized. remoteFile[" + remoteFile + "] localFile["
					+ localFile + "]");

		if (remoteFile == null || remoteFile.trim().length() == 0)
			throw new Exception("SFtp.get Remote file name is not provided. remoteFile[" + remoteFile + "] localFile["
					+ localFile + "]");

		if (localFile == null || localFile.trim().length() == 0)
			throw new Exception("SFtp.get Local file name is not provided. remoteFile[" + remoteFile + "] localFile["
					+ localFile + "]");

		// Get the file
		try {
			File file = new File(localFile);
			sftp.get(remoteFile, new FileOutputStream(file));
		} catch (Exception e) {
			throw new Exception("SFtp.get Fail to retrieve file: remoteFile[" + remoteFile + "] localFile[" + localFile
					+ "] = " + e.getMessage());
		}
	}

	public List<SftpFile> getFiles(String remotePath) throws Exception {

		if (sftp == null)
			throw new Exception("SFtp.getFiles SFTP channel is not initialized.");
		// Get the file
		try {
			SftpFile[] files = sftp.ls(remotePath);
			if (files != null)
				return new ArrayList<SftpFile>(Arrays.asList(files));
			return new ArrayList<SftpFile>();
		} catch (Exception e) {
			throw new Exception("SFtp.getFiles Fail to retrieve files:" + e.getMessage());
		}
	}

	public void disconnect() throws Exception {

		if (sftp == null)
			throw new Exception("SFtp.disconnect SFTP channel is not initialized.");

		if (ssh == null)
			throw new Exception("SFtp.disconnect SSH session is not initialized.");

		try {
			sftp.quit();
		} catch (Exception e) {
			throw new Exception("SFtp.disconnect Fail to disconnect from the server: " + e.getMessage());
		}

		try {
			ssh.disconnect();
		} catch (Exception e) {
			throw new Exception("SFtp.disconnect Failed to disconnect from the server: " + e.getMessage());
		}
	}

	public void delete(String remoteFile) throws Exception {

		if (sftp == null)
			throw new Exception("SFtp.delete SFTP channel is not initialized. remoteFile[" + remoteFile + "]");

		if (remoteFile == null || remoteFile.trim().length() == 0)
			throw new Exception("SFtp.delete File name is not provided. remoteFile[" + remoteFile + "]");

		// Remove file
		try {
			sftp.rm(remoteFile);
		} catch (Exception e) {
			throw new Exception(" SFtp.delete Fail to delete file: remoteFile[" + remoteFile + "] = " + e.getMessage());
		}
	}
}