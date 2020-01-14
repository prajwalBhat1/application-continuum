/**
 * 
 */
package com.app.continuum.generators;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import org.springframework.stereotype.Component;

/**
 * @author prajwbhat
 *
 */
@Component
public class ZipGenerator {
	public static void main(String[] args) {
		File dir = new File("C:\\Users\\prajwbhat\\Massage_Envy\\projectY");
		String zipDirName = "C:\\Users\\prajwbhat\\Massage_Envy\\projectY.zip";

		ZipGenerator zipFiles = new ZipGenerator();
		zipFiles.zipDirectory(dir, zipDirName, null);
	}

	/**
	 * This method zips the directory
	 * 
	 * @param dir
	 * @param zipDirName
	 * @return 
	 */
	public byte[] zipDirectory(File dir, String zipDirName, HashMap<String, List<String>> artifactMap) {
		try {
			List<String> filesListInDir = new ArrayList<String>();
			//String javaSource = "\\src\\main\\java\\";
			//String testSource = "\\src\\test\\java\\";
			/*
			 * artifactMap.get("L3-applications").stream().forEach(application -> {
			 * filesListInDir.add(new
			 * StringBuilder().append(dir.getAbsolutePath()).append("\\").append(
			 * application) .append(javaSource).toString()); filesListInDir.add(new
			 * StringBuilder().append(dir.getAbsolutePath()).append("\\").append(
			 * application) .append(testSource).toString()); });
			 * artifactMap.get("L3-components").stream().forEach(component -> {
			 * filesListInDir.add(new
			 * StringBuilder().append(dir.getAbsolutePath()).append("\\").append(component)
			 * .append(javaSource).toString()); filesListInDir.add(new
			 * StringBuilder().append(dir.getAbsolutePath()).append("\\").append(component)
			 * .append(testSource).toString()); });
			 */
			populateFilesList(dir , filesListInDir);
			// now zip files one by one
			// create ZipOutputStream to write to the zip file
			ByteArrayOutputStream baos = new ByteArrayOutputStream();
			FileOutputStream fos = new FileOutputStream(zipDirName);
			ZipOutputStream zos = new ZipOutputStream(baos);
			for (String filePath : filesListInDir) {
				System.out.println("Zipping " + filePath);
				readAndWriteToZipStream(zos, filePath, dir);
			}
			zos.close();
			fos.close();
			baos.close();
			return baos.toByteArray();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}

	private void readAndWriteToZipStream(ZipOutputStream zos, String filePath, File dir)
			throws FileNotFoundException, IOException {
		// read the file and write to ZipOutputStream
		ZipEntry ze = new ZipEntry(filePath.substring(dir.getAbsolutePath().length() + 1, filePath.length()));
		zos.putNextEntry(ze);
		FileInputStream fis = new FileInputStream(filePath);
		byte[] buffer = new byte[1024];
		int len;
		while ((len = fis.read(buffer)) > 0) {
			zos.write(buffer, 0, len);
		}
		zos.closeEntry();
		fis.close();
	}

	/**
	 * This method populates all the files in a directory to a List
	 * 
	 * @param dir
	 * @throws IOException
	 */
	private void populateFilesList(File dir, List<String> filesListInDir) throws IOException {
		File[] files = dir.listFiles();
		for (File file : files) {
			if (file.isFile()) {
				filesListInDir.add(file.getAbsolutePath());
			}
			else {
				populateFilesList(file, filesListInDir);
			}
		}
	}
}
