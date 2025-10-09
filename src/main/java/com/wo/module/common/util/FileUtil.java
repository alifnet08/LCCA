package com.wo.module.common.util;

import java.awt.Image;
import java.awt.image.RenderedImage;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.Closeable;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FilenameFilter;
import java.io.IOException;
import java.io.InputStream;
import java.io.Reader;
import java.io.Serializable;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.nio.channels.FileChannel;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import javax.imageio.ImageIO;

import org.apache.log4j.Logger;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.primefaces.model.UploadedFile;

import com.wo.module.common.constant.Constants;

public class FileUtil implements Serializable {

	private static final long serialVersionUID = 8054786471963135015L;

	public static final String CHAR_COMMA = ",";

	static Logger logger = Logger.getLogger(FileUtil.class);

	@SuppressWarnings("unused")
	private static final int DEFAULT_COLUMNS_COUNT = 24;

	private static FileUtil me;

	public static FileUtil getInstance() {
		if (FileUtil.me == null) {
			FileUtil.me = new FileUtil();
		}

		return FileUtil.me;
	}

	private FileUtil() {

	}

	private void close(Closeable resource, File file) throws IOException {
		if (resource != null) {
			resource.close();
		}
	}

	public static String getExtention(String fileName) {
		String ext = "";

		// find "." position
		int dotPos = 0;
		if (fileName.contains("."))
			dotPos = fileName.lastIndexOf(".");
		if (dotPos > 0) {
			ext = fileName.substring(dotPos + 1);
		}

		return ext;
	}

	public static String getFileNameWithoutExtention(String fileName) {
		int dotPos = 0;
		if (fileName.contains(".")) {
			dotPos = fileName.lastIndexOf(".");
			return fileName.substring(0, dotPos);
		}
		return fileName;
	}

	

	

	@SuppressWarnings("unused")
	private Reader readReader(File file) throws IOException {
		return new BufferedReader(new FileReader(file));
	}

	

	public File uniqueFile(File filePath, String fileName) throws IOException {
		File file = new File(filePath, fileName);

		if (file.exists()) {
			// Split filename and add braces, e.g. "name.ext" --> "name[",
			// "].ext".
			String prefix;
			String suffix;
			int dotIndex = fileName.lastIndexOf(".");

			if (dotIndex > -1) {
				prefix = fileName.substring(0, dotIndex) + "[";
				suffix = "]" + fileName.substring(dotIndex);
			} else {
				prefix = fileName + "[";
				suffix = "]";
			}

			int count = 0;

			// Add counter to filename as long as file exists.
			while (file.exists()) {
				if (count < 0) { // int++ restarts at -2147483648 after
					// 2147483647.
					throw new IOException(
							"No unique filename available for " + fileName + " in path " + filePath.getPath() + ".");
				}

				// Glue counter between prefix and suffix, e.g. "name[" + count
				// + "].ext".
				file = new File(filePath, prefix + (count++) + suffix);
			}
		}

		return file;
	}

	public void write(File file, InputStream input, boolean append) throws IOException {
		BufferedOutputStream output = null;

		try {
			output = new BufferedOutputStream(new FileOutputStream(file, append));
			int data = -1;
			while ((data = input.read()) != -1) {
				output.write(data);
			}
		} finally {
			this.close(input, file);
			this.close(output, file);
		}

	}

	public void deleteFile(String filePath) throws IOException {
		if (filePath != null && !filePath.isEmpty()) {
			File temp = new File(filePath);
			if (temp.exists()) {
				boolean success = temp.delete();
				if (!success)
					throw new IOException("Fail to delete file");
			}
			temp = null;
		}
	}

	public String handleFileUpload(InputStream is, String oldFileName, String newFolderDir,
			String newFileNameWithoutExtension) throws IOException {
		String fullFileName = "";
		if (!newFolderDir.contains(Constants.FILE_SEPARATOR))
			newFolderDir = newFolderDir.concat(Constants.FILE_SEPARATOR);

		File folder = new File(newFolderDir);
		if (!folder.exists()) {
			folder.mkdirs();
		}
		folder = null;

		if (newFileNameWithoutExtension == null || newFileNameWithoutExtension.isEmpty())
			fullFileName = newFolderDir.concat(oldFileName);
		else
			fullFileName = newFolderDir
					.concat(newFileNameWithoutExtension.concat(".").concat(getExtention(oldFileName)));
		

		writeFile(is, fullFileName);
		
		if(newFileNameWithoutExtension!=null) {
			fullFileName = newFileNameWithoutExtension.concat(".").concat(getExtention(oldFileName));
		}
		return fullFileName;
	}

	public void writeFile(final InputStream is, final String fullFileName) throws FileNotFoundException, IOException {
		// check if file exist
		File fileExist = new File(fullFileName);
		if (!fileExist.exists()) {
			FileOutputStream fos = new FileOutputStream(fullFileName);
			int BUFFER_SIZE = 8192;
			byte[] buffer = new byte[BUFFER_SIZE];
			int a;
			while (true) {
				a = is.read(buffer);
				if (a < 0)
					break;
				fos.write(buffer, 0, a);
				fos.flush();
			}
			fos.close();
			is.close();
		}
		fileExist = null;
	}

	@SuppressWarnings("resource")
	public String copyFile(String oldFilePath, String newFolderPath, String newFileName, boolean moveFile)
			throws IOException {
		File destFile = new File(newFolderPath);
		FileChannel source = null;
		FileChannel destination = null;
		String fullFilePath = null;

		if (!newFolderPath.contains(Constants.FILE_SEPARATOR))
			newFolderPath = newFolderPath.concat(Constants.FILE_SEPARATOR);
		if (!destFile.exists())
			destFile.mkdirs();
		fullFilePath = newFolderPath.concat(newFileName);
		destFile = new File(fullFilePath);
		if (!destFile.exists())
			destFile.createNewFile();
		source = new FileInputStream(oldFilePath).getChannel();
		destination = new FileOutputStream(destFile).getChannel();
		destination.transferFrom(source, 0, source.size());

		if (source != null)
			source.close();
		source = null;
		if (destination != null)
			destination.close();
		destination = null;
		destFile = null;
		if (moveFile)
			deleteFile(oldFilePath);

		return fullFilePath;
	}

	public static String getFileNameFromPath(String fileFullPath) {
		String fileName;

		if (fileFullPath == null || fileFullPath.isEmpty())
			return null;

		if (!fileFullPath.contains(Constants.FILE_SEPARATOR))
			fileName = fileFullPath;
		else
			fileName = fileFullPath.substring(fileFullPath.lastIndexOf(Constants.FILE_SEPARATOR) + 1);

		return fileName;
	}

	public String getContentType(String filePath) throws MalformedURLException, IOException {
		return getContentType(filePath, null);
	}

	public String getContentType(String filePath, String defaultType) throws MalformedURLException, IOException {
		URL u = new URL("file:" + filePath);
		URLConnection uc = u.openConnection();
		String type = uc.getContentType();
		if (type.equals("content/unknown") && defaultType != null && !defaultType.isEmpty())
			type = defaultType;
		return type;
	}

	public String getDownloadedFileName(String newFileName, String oldFileName) {
		return newFileName + "." + getExtention(oldFileName);
	}

	public static String getFullPathForReport(String fileFullPath, String rawFileName) {
		String fileName;
		if (fileFullPath == null || fileFullPath.isEmpty())
			return fileFullPath;

		if (!fileFullPath.endsWith(Constants.FILE_SEPARATOR))
			fileName = fileFullPath + Constants.FILE_SEPARATOR;
		else
			fileName = fileFullPath;

		return fileName + rawFileName;
	}

	public static String getPathFromFullPath(String fileFullPath) {
		String fullPath;
		if (fileFullPath == null || fileFullPath.isEmpty())
			return fileFullPath;

		if (fileFullPath.contains(Constants.FILE_SEPARATOR)) {
			if (fileFullPath.endsWith(Constants.FILE_SEPARATOR))
				fullPath = fileFullPath;
			else
				fullPath = fileFullPath.substring(0, fileFullPath.lastIndexOf(Constants.FILE_SEPARATOR) + 1);
		} else {
			fullPath = fileFullPath + Constants.FILE_SEPARATOR;
		}

		return fullPath;
	}

	@SuppressWarnings("unused")
	public static String saveUploadedImageToLocalHarddrive(UploadedFile uploadedFile, String localFullPath)
			throws IOException {

		String todaysDate = "";
		SimpleDateFormat sdf = new SimpleDateFormat("ddMMyyyy_HHmmss");
		todaysDate = sdf.format(new java.util.Date());

		String folderDir = localFullPath;

		String filePath = folderDir;
		String fileName = uploadedFile.getFileName();

		File folder = new File(folderDir);
		if (!folder.exists()) {
			logger.debug("Create Folder:" + folder.mkdirs());
		}

		String uploadFileName = uploadedFile.getFileName();
		String writeFileName = getFileNameWithoutExtention(uploadFileName) + todaysDate + "."
				+ getExtention(uploadFileName);
		String fullPath = folderDir + writeFileName;

		File imgFile = new File(fullPath);

		Image image = null;
		image = ImageIO.read(uploadedFile.getInputstream());
		ImageIO.write((RenderedImage) image, "jpg", imgFile);

		return writeFileName;
	}

	public static List<String> getAllFilesInFolder(String folderPath, boolean fileOnly, String startWith,
			String contain, String endWith) {
		File folder = new File(folderPath);
		File[] listOfFiles = null;
		listOfFiles = folder.listFiles(new FilenameFilter() {
			public boolean accept(File dir, String name) {
				return ((startWith != null && !startWith.equals("")) ? name.toLowerCase().startsWith(startWith)
						: true && (contain != null && !contain.equals("")) ? name.toLowerCase().contains(contain)
								: true && (endWith != null && !endWith.equals(""))
										? name.toLowerCase().endsWith(endWith) : true);
			}
		});
		List<String> files = new ArrayList<String>();

		for (int i = 0; i < listOfFiles.length; i++) {
			if (listOfFiles[i].isFile()) {
				files.add(listOfFiles[i].getName());
			} else if (listOfFiles[i].isDirectory() && !fileOnly) {
				files.add(listOfFiles[i].getName());
			}
		}

		return files;
	}
	
	public static byte[] readBytesFromFile(String filePath) {

        FileInputStream fileInputStream = null;
        byte[] bytesArray = null;

        try {

            File file = new File(filePath);
            bytesArray = new byte[(int) file.length()];

            //read file into bytes[]
            fileInputStream = new FileInputStream(file);
            fileInputStream.read(bytesArray);

        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (fileInputStream != null) {
                try {
                    fileInputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

        }

        return bytesArray;

    }
	
	public static byte[] readBytesFromFile(File file) {

        FileInputStream fileInputStream = null;
        byte[] bytesArray = null;

        try {
            
            bytesArray = new byte[(int) file.length()];

            //read file into bytes[]
            fileInputStream = new FileInputStream(file);
            fileInputStream.read(bytesArray);

        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (fileInputStream != null) {
                try {
                    fileInputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

        }

        return bytesArray;

    }
	
	public static Row excelCopyRow(Sheet sheet, int rowSourceIdx, int rowDestIdx) {
		return excelCopyRow(sheet, rowSourceIdx, rowDestIdx, false);
	}
	
	public static Row excelCopyRow(Sheet sheet, int rowSourceIdx,
			int rowDestIdx, boolean isNotCopyValue) {
		Row rowSource = sheet.getRow(rowSourceIdx);
		Row rowDest = sheet.getRow(rowDestIdx);

		if (rowSource == null) {
			return null;
		}

		if (rowDest != null) {
			sheet.shiftRows(rowDestIdx, sheet.getLastRowNum(), 1);
		} else {
			rowDest = sheet.createRow(rowDestIdx);
		}

		if (rowSource != null) {
			Cell cellSource = null;
			Cell cellDest = null;
			for (int i = 0; i < rowSource.getLastCellNum(); i++) {
				cellSource = rowSource.getCell(i);
				cellDest = rowDest.createCell(i);

				// If the old cell is null jump to next cell
				if (cellSource == null) {
					cellDest = null;
					continue;
				}

				cellDest.setCellType(cellSource.getCellType());
				cellDest.setCellStyle(cellSource.getCellStyle());
				if (cellSource.getCellComment() != null) {
					cellDest.setCellComment(cellSource.getCellComment());
				}
				if (cellSource.getHyperlink() != null) {
					cellDest.setHyperlink(cellSource.getHyperlink());
				}

				if (!isNotCopyValue) {
					switch (cellSource.getCellType()) {
					case BLANK:
						cellDest.setCellValue(cellSource.getStringCellValue());
						break;
					case BOOLEAN:
						cellDest.setCellValue(cellSource.getBooleanCellValue());
						break;
					case ERROR:
						cellDest.setCellErrorValue(cellSource
								.getErrorCellValue());
						break;
					case FORMULA:
						cellDest.setCellFormula(cellSource.getCellFormula());
						break;
					case NUMERIC:
						cellDest.setCellValue(cellSource.getNumericCellValue());
						break;
					case STRING:
						cellDest.setCellValue(cellSource
								.getRichStringCellValue());
						break;
					}
				}
			}
		}

		return rowDest;
	}
}