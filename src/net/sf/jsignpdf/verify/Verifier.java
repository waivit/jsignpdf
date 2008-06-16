package net.sf.jsignpdf.verify;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

import net.sf.jsignpdf.IOUtils;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.apache.commons.cli.PosixParser;

/**
 * Entry point (i.e. main class) to PDF signature verifications.
 * @author Josef Cacek
 * @author $Author: kwart $
 * @version $Revision: 1.1 $
 * @created $Date: 2008/06/16 13:07:14 $
 */
public class Verifier {

	/**
	 * @param args
	 */
	public static void main(String[] args) {


		// create the Options
		Option help = new Option("h", "help", false, "print this message");
//		Option version = new Option("v", "version", false, "print version info");
		Option certificates = new Option("c", "cert", true, "use external colon-separated X.509 certificate files"); 
		certificates.setArgName("certificates");
		Option password = new Option("p", "password", true, "sets password for opening PDF"); 
		password.setArgName("password");
		Option export = new Option("e", "extract", true, "extracts signed PDF revisions to given folder");
		export.setArgName("folder");
		
		final Options options = new Options();
		options.addOption(help);
//		options.addOption(version);
		options.addOption(certificates);
		options.addOption(password);
		options.addOption(export);

		CommandLine line = null;
		try {
			// create the command line parser
			CommandLineParser parser = new PosixParser();
			// parse the command line arguments
			line = parser.parse(options, args);
		} catch (ParseException exp) {
			System.out.println("Unexpected exception:" + exp.getMessage());
			System.exit(-1);
		}

		final String[] tmpArgs = line.getArgs();
		if (line.hasOption("h") || tmpArgs == null || tmpArgs.length==0) {
			// automatically generate the help statement
			HelpFormatter formatter = new HelpFormatter();
			formatter.printHelp(70, "java -jar verify.jar [-c <certificates>] [-h] [-v] <PDF_file(s)>", "//TODO header", options, "//TODO footer"); 
		} else {
			final VerifierLogic tmpLogic = new VerifierLogic();
			
			if (line.hasOption("c")) {
				String tmpCertFiles = line.getOptionValue("c");
				for (String tmpCFile : tmpCertFiles.split("[:;]")) {
					tmpLogic.addX509CertFile(tmpCFile);
				}
			}
			byte[] tmpPasswd = null; 
			if (line.hasOption("p")) {
				tmpPasswd = line.getOptionValue("p").getBytes();
			}
			String tmpExtractDir = null;
			if (line.hasOption("e")) {
				tmpExtractDir = new File(line.getOptionValue("e")).getPath();
			}

			for (String tmpFilePath : tmpArgs) {
				
				System.out.println("Verifying " + tmpFilePath);
				final File tmpFile = new File(tmpFilePath);
				if (!tmpFile.canRead()) {
					System.err.println("Couln't read the file. Check the path and permissions.");
					continue;
				}
				final VerificationResult tmpResult = tmpLogic.verify(tmpFilePath, tmpPasswd);				
				if (tmpResult.getException()!=null) {
					tmpResult.getException().printStackTrace();
				} else {
					System.out.println("Total revisions: " + tmpResult.getTotalRevisions());
					for (SignatureVerification tmpSigVer : tmpResult.getVerifications()) {
						System.out.println(tmpSigVer.toString());
						if (tmpExtractDir!=null) {
							try {
								File tmpExFile = new File(tmpExtractDir + "/" + tmpFile.getName() + "_" + tmpSigVer.getRevision() + ".pdf");
								System.out.println("Extracting to " + tmpExFile.getCanonicalPath());
								FileOutputStream tmpFOS = new FileOutputStream(tmpExFile.getCanonicalPath());
								
								InputStream tmpIS = tmpLogic.extractRevision(
									tmpFilePath,
									tmpPasswd,
									tmpSigVer.getName());
								IOUtils.copy(tmpIS, tmpFOS);
								tmpIS.close();
								tmpFOS.close();
							} catch (IOException ioe) {
								ioe.printStackTrace();
							}
						}
					}
				}
			}
		}
		
	}

}