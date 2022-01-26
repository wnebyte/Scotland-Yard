package se.kau.cs.sy.util;

import java.io.IOException;
import java.io.InputStreamReader;

import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;

/**
 * Utility class for reading from a text file
 * @author Sebastian Herold
 *
 */
public class FileHandler {

		private static Logger log = LogManager.getLogger(FileHandler.class);
		
		InputStreamReader in = null;
		String filename = null;

		public FileHandler(String filename) {
			this.filename = filename;
			in = new InputStreamReader(this.getClass().getClassLoader().getResourceAsStream(filename));
		}
		
		public String readLine() {
			StringBuilder buffer = new StringBuilder();
			try {
				boolean newLine = false;
				while(!newLine) {
					char c[] = new char[1];
					if(in.ready()) {
						in.read(c);
						if(c[0]=='\n') {
							newLine = true;
						}
						else {
							buffer.append(c);
						}
					}
					else {
						if(buffer.length()==0) {
							return null;
						}
						else {
							newLine = true;
						}
					}
				}
				
			}
			catch (IOException e) {
				log.error("Error in reading file <"+filename+">", e);
				return null;
			}
			return buffer.toString();
		}
		
		public String getFileName() {
			return filename;
		}
		
		public void close() {
			try {
				in.close();
			}
			catch (IOException e) {
				log.error("Could not close file "+filename, e);
			}
		}
		
		public String readAll() {
			StringBuilder buffer = new StringBuilder();
			String line = readLine();
			while(line != null) {
				buffer.append(line).append("\n");
				line = readLine();
			}  
			close();
			return buffer.toString();
		}
}
