package net.sf.jsignpdf;

import java.io.PrintWriter;

/**
 * Options for PDF signer.
 * @author Josef Cacek
 */
public class SignerOptions {

	protected volatile PrintWriter outWriter;
	protected volatile String ksType;
	protected volatile String ksFile;
	protected volatile char[] ksPasswd;
	protected volatile char[] keyPasswd;
	protected volatile String inFile;
	protected volatile String outFile;
	protected volatile String reason;
	protected volatile String location;
	protected volatile SignResultListener listener;


	public PrintWriter getOutWriter() {
		return outWriter;
	}
	public void setOutWriter(PrintWriter outWriter) {
		this.outWriter = outWriter;
	}
	public String getKsType() {
		return ksType;
	}
	public void setKsType(String ksType) {
		this.ksType = ksType;
	}
	public String getKsFile() {
		return ksFile;
	}
	public void setKsFile(String ksFile) {
		this.ksFile = ksFile;
	}
	public char[] getKsPasswd() {
		return ksPasswd;
	}
	public void setKsPasswd(char[] passwd) {
		this.ksPasswd = passwd;
	}
	public String getInFile() {
		return inFile;
	}
	public void setInFile(String inFile) {
		this.inFile = inFile;
	}
	public String getOutFile() {
		return outFile;
	}
	public void setOutFile(String outFile) {
		this.outFile = outFile;
	}
	public String getReason() {
		return reason;
	}
	public void setReason(String reason) {
		this.reason = reason;
	}
	public String getLocation() {
		return location;
	}
	public void setLocation(String location) {
		this.location = location;
	}
	public SignResultListener getListener() {
		return listener;
	}
	public void setListener(SignResultListener listener) {
		this.listener = listener;
	}
	public char[] getKeyPasswd() {
		return keyPasswd;
	}
	public void setKeyPasswd(char[] keyPasswd) {
		this.keyPasswd = keyPasswd;
	}

}