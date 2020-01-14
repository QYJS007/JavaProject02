package cg.project;
import java.io.File;
import java.io.FileFilter;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.channels.FileChannel;
import java.text.ParseException;
import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * <code>{@link CopyUpdateFiles}</code>
 * 
 * 导出新修改文件，部署专用
 * 
 * @author Jason
 */
public class CopyUpdateFiles {
	static String START_DATE_STR = "2018-09-26-18-10-23"; //时间节点（获取该日期后修改的所有代码） 
	// 源目录
	//static final String ROOT_PATH = "H:/SVN/公安PAAS/source/01.trunk/bingo-paas/bingo-paas-web/"; // 项目所在路径
	static final String ROOT_PATH = "F:/A_temp/qtrip365/SRC/"; // 项目所在路径
	
	//F:\A_temp\qtrip365\SRC
	static String fromPath = ROOT_PATH + "qtrip_business"; // Web项目根路径
	static final String CLASS_PATH = ROOT_PATH + "WebRoot/WEB-INF/classes";  // CLASSPATH目录(.class文件所在目录)
	static final String[] ROOT_CLASS_PATHS = new String[] { ROOT_PATH + "src/main/java", ROOT_PATH + "src/main/resources" };  // Java文件所在目录

	// 导出目录
	static String path = "d:/updatefiles/"; 
	static String toPath = path + new SimpleDateFormat("yyyy-MM-dd-HH-mm-ss").format(new Date());

	static boolean VALID_FILE = true; // 是否对文件或文件夹进行验证
	FileFilter myFileFilter = new MyFileFilter(); // 文件过滤器
	static List<String> IGNORE_DIR_PATH = new ArrayList<String>();// 忽略的目录路径
	static List<String> IGNORE_FILE_PATH = new ArrayList<String>();// 忽略的文件路径
	static {
		// 忽略的文件
		IGNORE_FILE_PATH.add(CLASS_PATH + "/config/config.xml");
		IGNORE_FILE_PATH.add(CLASS_PATH + "/config/upload.xml");
		IGNORE_FILE_PATH.add(CLASS_PATH + "/config/jdbc.xml");
		IGNORE_FILE_PATH.add(CLASS_PATH + "/logback-test.xml");
		IGNORE_FILE_PATH.add(CLASS_PATH + "/spring/dataAccessContext.xml");
		IGNORE_FILE_PATH.add(CLASS_PATH + "/spring/applicationContext.xml");
		IGNORE_FILE_PATH.add(CLASS_PATH + "/bingo/util/update/CopyUpdateFiles.class");
		IGNORE_FILE_PATH.add(CLASS_PATH + "/bingo/util/update/MyFileFilter.class");
		IGNORE_FILE_PATH.add(ROOT_PATH + "WebRoot/WEB-INF/web.xml");

		// 忽略的目录
		// IGNORE_DIR_PATH.add(CLASS_PATH + "/config");
		// IGNORE_DIR_PATH.add(CLASS_PATH + "/spring");
	}

	public static void main(String[] args) throws IOException {
		File file = new File(toPath);
		if (!file.exists()) {
			file.mkdirs();
		}
		new CopyUpdateFiles().doCopy(new File(fromPath));
		System.out.println("备份完成....");
	}

	/**
	 * 拷贝文件
	 * 
	 * @throws IOException
	 */
	public void doCopy(File parent) throws IOException {
		File[] files = parent.listFiles(myFileFilter);
		for (File file : files) {
			// 将源路径转换为目标路径
			File toFile = this.getToFile(file);
			String path = this.tranPath(file.getPath());
			if (file.isDirectory()) {
				// 如果是文件夹则递归
				if (!VALID_FILE || !IGNORE_DIR_PATH.contains(path)) {
					doCopy(file);
				}
			} else {
				if (!VALID_FILE || !IGNORE_FILE_PATH.contains(path)) {
					toFile.getParentFile().mkdirs();
					copyFile(file, toFile);

					// 对于class文件，需要考虑内部类情况
					final String fileName = file.getName();
					if (fileName.endsWith(".class")) {
						File[] innerList = parent.listFiles(new FileFilter() {
							public boolean accept(File pathname) {
								return pathname.getName().startsWith(fileName.substring(0, fileName.indexOf('.')) + "$");
							}
						});
						for (File innerFile : innerList) {
							this.copyFile(innerFile, this.getToFile(innerFile));
						}
					}
				}
			}
		}
	}

	/**
	 * 获取目的文件对象
	 * 
	 * @param fromFile
	 * @return
	 * @throws IOException
	 */
	public File getToFile(File fromFile) throws IOException {
		String path = this.tranPath(fromFile.getPath()).replaceFirst(fromPath, toPath);
		return new File(path);
	}

	/**
	 * 转换路径, 将\转换为/
	 * 
	 * @return
	 */
	private String tranPath(String oldPath) {
		return oldPath.replace(File.separator, "/");
	}

	@SuppressWarnings("resource")
	private void copyFile(File srcFile, File destFile) throws IOException {
		if (srcFile == null) {
			throw new NullPointerException("Source must not be null");
		}
		if (destFile == null) {
			throw new NullPointerException("Destination must not be null");
		}
		if (!srcFile.exists()) {
			throw new FileNotFoundException("Source '" + srcFile + "' does not exist");
		}
		if (srcFile.isDirectory()) {
			throw new IOException("Source '" + srcFile + "' exists but is a directory");
		}
		if (srcFile.getCanonicalPath().equals(destFile.getCanonicalPath())) {
			throw new IOException("Source '" + srcFile + "' and destination '" + destFile + "' are the same");
		}
		if ((destFile.getParentFile() != null) && (!destFile.getParentFile().exists()) && (!destFile.getParentFile().mkdirs())) {
			throw new IOException("Destination '" + destFile + "' directory cannot be created");
		}

		if ((destFile.exists()) && (!destFile.canWrite())) {
			throw new IOException("Destination '" + destFile + "' exists but is read-only");
		}

		if ((destFile.exists()) && (destFile.isDirectory())) {
			throw new IOException("Destination '" + destFile + "' exists but is a directory");
		}

		FileInputStream fis = null;
		FileOutputStream fos = null;
		FileChannel input = null;
		FileChannel output = null;
		try {
			fis = new FileInputStream(srcFile);
			fos = new FileOutputStream(destFile);
			input = fis.getChannel();
			output = fos.getChannel();
			long size = input.size();
			long pos = 0L;
			long count = 0L;
			while (pos < size) {
				count = size - pos > 52428800L ? 52428800L : size - pos;
				pos += output.transferFrom(input, pos, count);
			}
		} finally {
			if (output != null)
				output.close();
			if (fos != null)
				fos.close();
			if (input != null)
				input.close();
			if (fis != null)
				output.close();
		}

		if (srcFile.length() != destFile.length()) {
			throw new IOException("Failed to copy full contents from '" + srcFile + "' to '" + destFile + "'");
		}

		destFile.setLastModified(srcFile.lastModified());
	}

	/**
	 *
	 * <code>{@link MyFileFilter}</code>
	 *
	 * 文件过滤器
	 *
	 * @author Jason
	 */
	class MyFileFilter implements FileFilter {
		Date editDate = new Date();
		// 支持的日期格式
		String[] formatStrs = { "yyyyMMddHHmmss", "yyyy-MM-dd-HH-mm-ss", "yyyyMMddHHmm", "yyyyMMddHH", "yyyyMMdd" };

		/**
		 * @throws ParseException 
		 * 
		 */
		public MyFileFilter() {
			try {
				editDate = parseDate(CopyUpdateFiles.START_DATE_STR, formatStrs);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

		public boolean accept(File file) {
			return !file.isHidden() && (file.isDirectory() || this.validFile(file));
		}

		/**
		 * 判断class文件
		 * 
		 * @return
		 */
		public boolean validFile(File file) {
			if (file.isFile()) {
				String path = file.getPath().replace(File.separator, "/");
				// 判断修改时间
				if (path.startsWith(CopyUpdateFiles.CLASS_PATH)) {
					// 如果是class文件判断对应的java文件的修改时间
					for (String srcPath : CopyUpdateFiles.ROOT_CLASS_PATHS) {
						String tempPath = path.replaceFirst(CopyUpdateFiles.CLASS_PATH, srcPath);
						if (tempPath.endsWith(".class")) {
							tempPath = tempPath.replaceAll(".class$", ".java");
						}
						File classFile = new File(tempPath);
						if (classFile.exists()) {
							Date classLastEdit = new Date(classFile.lastModified());
							if (classLastEdit.after(editDate)) {
								return true;
							}
						}
					}
				} else {
					Date lastEdit = new Date(file.lastModified());
					if (lastEdit.after(editDate)) {
						return true;
					}
				}
			}
			return false;
		}

		/**
		 * 字符串转换为日期
		 * @param str
		 * @param parsePatterns
		 * @return
		 * @throws ParseException
		 */
		private Date parseDate(String str, String[] parsePatterns) throws ParseException {
			if ((str == null) || (parsePatterns == null)) {
				throw new IllegalArgumentException("Date and Patterns must not be null");
			}

			SimpleDateFormat parser = new SimpleDateFormat();
			parser.setLenient(true);
			ParsePosition pos = new ParsePosition(0);
			for (int i = 0; i < parsePatterns.length; i++) {
				String pattern = parsePatterns[i];

				if (parsePatterns[i].endsWith("ZZ")) {
					pattern = pattern.substring(0, pattern.length() - 1);
				}

				parser.applyPattern(pattern);
				pos.setIndex(0);

				String str2 = str;

				if (parsePatterns[i].endsWith("ZZ")) {
					int signIdx = indexOfSignChars(str2, 0);
					while (signIdx >= 0) {
						str2 = reformatTimezone(str2, signIdx);
						signIdx = indexOfSignChars(str2, ++signIdx);
					}
				}

				Date date = parser.parse(str2, pos);
				if ((date != null) && (pos.getIndex() == str2.length())) {
					return date;
				}
			}
			throw new ParseException("Unable to parse the date: " + str, -1);
		}

		private int indexOfSignChars(String str, int startPos) {
			int idx = str.indexOf('+', startPos);
			if (idx < 0) {
				idx = str.indexOf('-', startPos);
			}
			return idx;
		}

		private String reformatTimezone(String str, int signIdx) {
			String str2 = str;
			if ((signIdx >= 0) && (signIdx + 5 < str.length()) && (Character.isDigit(str.charAt(signIdx + 1)))
					&& (Character.isDigit(str.charAt(signIdx + 2))) && (str.charAt(signIdx + 3) == ':')
					&& (Character.isDigit(str.charAt(signIdx + 4))) && (Character.isDigit(str.charAt(signIdx + 5)))) {
				str2 = str.substring(0, signIdx + 3) + str.substring(signIdx + 4);
			}
			return str2;
		}
	}
}
