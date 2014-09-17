package mir.gla.ac.uk.gms;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class RenameImage {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		File src = new File("/home/ripul/images/scotsman/");
		File dest = new File("/home/ripul/images/scotsman/");
		
		String files[] = src.list();
		try {
			for (String file : files) {
				
				String newFile = file;
				if(!newFile.contains("scot_")){
					newFile = "scot_" + file;
					System.out.println("File Name:" + file + ", New File:" + newFile);
					InputStream in;

					in = new FileInputStream(new File("/home/ripul/images/scotsman/" + file));

					OutputStream out = new FileOutputStream(new File ("/home/ripul/images/scotsman/" + newFile));

					byte[] buffer = new byte[1024];

					int length;
					// copy the file content in bytes
					while ((length = in.read(buffer)) > 0) {
						out.write(buffer, 0, length);
					}

					in.close();
					out.close();
				}
				File delFile = new File("/home/ripul/images/scotsman/" + file);
				delFile.delete();
			}
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
