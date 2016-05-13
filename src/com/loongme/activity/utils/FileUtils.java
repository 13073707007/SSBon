package com.loongme.activity.utils;

import static com.loongme.activity.common.Configuration.FILE_SEPARATOR;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.StringTokenizer;

import android.content.Context;
import android.net.Uri;
import android.os.Environment;
import android.os.StatFs;
import android.text.TextUtils;
import android.util.Log;
import android.webkit.MimeTypeMap;

/**
 * 文件操作工具包
 */
public class FileUtils {

  /**
   * 生成短名称
   */
  public static String generateShortTitle(String title) {
    if (TextUtils.isEmpty(title)) {
      return "";
    }
    int maxcount = 7;
    if (title.length() <= maxcount) {
      return title;
    } else {
      return title.subSequence(0, maxcount) + ".." + title.substring(title.length() - 2);
    }
  }

  /**
   * 生成短名称
   */
  public static String generateShortTitle(String title, int maxcount) {
    if (TextUtils.isEmpty(title) || maxcount < 0) {
      return "";
    }
    if (title.length() <= maxcount) {
      return title;
    } else {
      return title.subSequence(0, maxcount) + "...";
    }
  }

  /**
   * 显示文件名，如果除去扩展名外的名称大于最大长度（第二个参数），则显示..
   * 
   * @param fileName
   * @param maxCount
   *          大于0
   * @return
   */
  public static String generateShortFileName(String fileName, int maxCount) {
    String result = "";
    if (TextUtils.isEmpty(fileName) || maxCount < 0) {
      return result;
    }
    String extName = FileUtils.getFileFormat(fileName);
    String title = FileUtils.getFileNameNoFormat(fileName);
    result = generateShortTitle(title, maxCount);

    return result + extName;
  }

  public enum PathStatus {
    ERROR, EXITS, SUCCESS
  }

  /**
   * 检查是否安装外置的SD卡
   * 
   * @return
   */
  public static boolean checkExternalSDExists() {

    Map<String, String> evn = System.getenv();
    return evn.containsKey("SECONDARY_STORAGE");
  }

  /**
   * 检查文件是否存在
   * 
   * @param name
   * @return
   */
  public static boolean checkFileExists(String name) {
    boolean status;
    if (!name.equals("")) {
      File path = Environment.getExternalStorageDirectory();
      File newPath = new File(path.toString() + name);
      status = newPath.exists();
    } else {
      status = false;
    }
    return status;
  }

  /**
   * 检查路径是否存在
   * 
   * @param path
   * @return
   */
  public static boolean checkFilePathExists(String path) {
    return new File(path).exists();
  }

  /**
   * 检查是否安装SD卡
   * 
   * @return
   */
  public static boolean checkSaveLocationExists() {
    String sDCardStatus = Environment.getExternalStorageState();
    boolean status;
    if (sDCardStatus.equals(Environment.MEDIA_MOUNTED)) {
      status = true;
    } else
      status = false;
    return status;
  }

  /**
   * 检查sdcard是否可用
   * 
   * @return
   */
  public static boolean checkSDCardAvailable() {
    String state = Environment.getExternalStorageState();
    if (!TextUtils.isEmpty(state)) {
      if (android.os.Environment.MEDIA_MOUNTED.equals(state)) {
        return true;
      } else {
        return false;
      }
    } else {
      return false;
    }
  }

  /**
   * 清空一个文件夹
   * 
   * @param files
   */
  public static void clearFileWithPath(String filePath) {
    List<File> files = FileUtils.listPathFiles(filePath);
    if (files.isEmpty()) {
      return;
    }
    for (File f : files) {
      if (f.isDirectory()) {
        clearFileWithPath(f.getAbsolutePath());
      } else {
        f.delete();
      }
    }
  }

  /**
   * 如果欲写入的文件所在目录不存在，需先创建
   * 
   * @param outputPath
   *          输出文件路径
   */
  public static void createDir(String outputPath) {
    File outputFile = new File(outputPath);
    File outputDir = outputFile.getParentFile();
    if (!outputDir.exists()) {
      outputDir.mkdirs();
    }
  }

  /**
   * 在SD卡上新建目录
   * 
   * @param directoryName
   * @return
   */
  public static boolean createDirectory(String directoryName) {
    boolean status;
    if (!directoryName.equals("")) {
      File path = Environment.getExternalStorageDirectory();
      File newPath = new File(path.toString() + directoryName);
      status = newPath.mkdir();
      status = true;
    } else
      status = false;
    return status;
  }

  public static File createFile(String folderPath, String fileName) {
    File destDir = new File(folderPath);
    if (!destDir.exists()) {
      destDir.mkdirs();
    }
    return new File(folderPath, fileName + fileName);
  }

  /**
   * 创建多级目录
   * 
   * @param path
   *          目录的绝对路径
   */
  public static void createMultilevelDir(String path) {
    createMultilevelDir(path, FILE_SEPARATOR);
  }

  /**
   * 创建多级目录
   * 
   * @param path
   *          目录的绝对路径
   */
  public static void createMultilevelDir(String path, String separator) {
    try {
      StringTokenizer st = new StringTokenizer(path, separator);
      String path1 = st.nextToken() + separator;
      String path2 = path1;
      while (st.hasMoreTokens()) {

        path1 = st.nextToken() + separator;
        path2 += path1;
        File inbox = new File(path2);
        if (!inbox.exists())
          inbox.mkdir();

      }
    } catch (Exception e) {
      System.out.println("目录创建失败" + e);
      e.printStackTrace();
    }
  }

  /**
   * 创建目录
   * 
   * @param path
   */
  public static PathStatus createPath(String newPath) {
    File path = new File(newPath);
    if (path.exists()) {
      return PathStatus.EXITS;
    }
    if (path.mkdir()) {
      return PathStatus.SUCCESS;
    } else {
      return PathStatus.ERROR;
    }
  }

  /**
   * 删除空目录
   * 
   * 返回 0代表成功 ,1 代表没有删除权限, 2代表不是空目录,3 代表未知错误
   * 
   * @return
   */
  public static int deleteBlankPath(String path) {
    File f = new File(path);
    if (!f.canWrite()) {
      return 1;
    }
    if (f.list() != null && f.list().length > 0) {
      return 2;
    }
    if (f.delete()) {
      return 0;
    }
    return 3;
  }

  /**
   * 删除目录(包括：目录里的所有文件)
   * 
   * @param fileName
   * @return
   */
  public static boolean deleteDirectory(String fileName) {
    boolean status;
    SecurityManager checker = new SecurityManager();

    if (!fileName.equals("")) {

      File path = Environment.getExternalStorageDirectory();
      File newPath = new File(path.toString() + fileName);
      checker.checkDelete(newPath.toString());
      if (newPath.isDirectory()) {
        String[] listfile = newPath.list();
        // delete all files within the specified directory and then
        // delete the directory
        try {
          for (int i = 0; i < listfile.length; i++) {
            File deletedFile = new File(newPath.toString() + FILE_SEPARATOR + listfile[i].toString());
            deletedFile.delete();
          }
          newPath.delete();
          Log.i("DirectoryManager deleteDirectory", fileName);
          status = true;
        } catch (Exception e) {
          e.printStackTrace();
          status = false;
        }

      } else
        status = false;
    } else
      status = false;
    return status;
  }

  /**
   * 根据文件路径删除文件
   * 
   * @param filePath
   */
  public static void delFile(String filePath) {
    File file = new File(filePath);
    if (file.exists()) {
      file.delete();
    }
  }

  /**
   * 删除文件
   * 
   * @param fileName
   * @return
   */
  public static boolean deleteFile(String fileName) {
    boolean status;
    SecurityManager checker = new SecurityManager();

    if (!fileName.equals("")) {

      File path = Environment.getExternalStorageDirectory();
      File newPath = new File(path.toString() + fileName);
      checker.checkDelete(newPath.toString());
      if (newPath.isFile()) {
        try {
          Log.i("DirectoryManager deleteFile", fileName);
          newPath.delete();
          status = true;
        } catch (SecurityException se) {
          se.printStackTrace();
          status = false;
        }
      } else
        status = false;
    } else
      status = false;
    return status;
  }

  /**
   * 删除文件
   * 
   * @param filePath
   */
  public static boolean deleteFileWithPath(String filePath) {
    SecurityManager checker = new SecurityManager();
    File f = new File(filePath);
    checker.checkDelete(filePath);
    if (f.isFile()) {
      Log.i("DirectoryManager deleteFile", filePath);
      f.delete();
      return true;
    }
    return false;
  }

  /**
   * 转换文件大小
   * 
   * @param fileS
   * @return B/KB/MB/GB
   */
  public static String formatFileSize(long fileS) {
    java.text.DecimalFormat df = new java.text.DecimalFormat("#.00");
    String fileSizeString = "";
    if (fileS < 1024) {
      fileSizeString = df.format((double) fileS) + "B";
    } else if (fileS < 1048576) {
      fileSizeString = df.format((double) fileS / 1024) + "KB";
    } else if (fileS < 1073741824) {
      fileSizeString = df.format((double) fileS / 1048576) + "MB";
    } else {
      fileSizeString = df.format((double) fileS / 1073741824) + "G";
    }
    return fileSizeString;
  }

  /**
   * 获取应用程序缓存文件夹下的指定目录
   * 
   * @param context
   * @param dir
   * @return
   */
  public static String getAppCache(Context context, String dir) {
    String savePath = context.getCacheDir().getAbsolutePath() + FILE_SEPARATOR + dir + FILE_SEPARATOR;
    File savedir = new File(savePath);
    if (!savedir.exists()) {
      savedir.mkdirs();
    }
    savedir = null;
    return savePath;
  }

  /**
   * 截取文件所在的文件夹名
   * 
   * @return
   */
  public static String getDirFromFile(String filepath) {
    int end = filepath.lastIndexOf(File.separator) + 1;
    return filepath.substring(0, end);
  }

  /**
   * 获取目录文件大小
   * 
   * @param dir
   * @return
   */
  public static long getDirSize(File dir) {
    if (dir == null) {
      return 0;
    }
    if (!dir.isDirectory()) {
      return 0;
    }
    long dirSize = 0;
    File[] files = dir.listFiles();
    for (File file : files) {
      if (file.isFile()) {
        dirSize += file.length();
      } else if (file.isDirectory()) {
        dirSize += file.length();
        dirSize += getDirSize(file); // 递归调用继续统计
      }
    }
    return dirSize;
  }

  /**
   * 获取手机外置SD卡的根目录
   * 
   * @return
   */
  public static String getExternalSDRoot() {

    Map<String, String> evn = System.getenv();

    return evn.get("SECONDARY_STORAGE");
  }

  /**
   * 获取文件扩展名
   * 
   * @param fileName
   * @return
   */
  public static String getFileFormat(String fileName) {
    if (TextUtils.isEmpty(fileName))
      return "";

    int point = fileName.lastIndexOf('.');
    return fileName.substring(point + 1);
  }

  /**
   * 根据文件绝对路径获取文件名
   * 
   * @param filePath
   * @return
   */
  public static String getFileName(String filePath) {
    if (TextUtils.isEmpty(filePath))
      return "";
    return filePath.substring(filePath.lastIndexOf(FILE_SEPARATOR) + 1);
  }

  /**
   * 根据文件的绝对路径获取文件名但不包含扩展名
   * 
   * @param filePath
   * @return
   */
  public static String getFileNameNoFormat(String filePath) {
    if (TextUtils.isEmpty(filePath)) {
      return "";
    }
    int dot = filePath.lastIndexOf('.');
    return filePath.substring(filePath.lastIndexOf(FILE_SEPARATOR) + 1, dot);
  }

  /**
   * 获取文件大小
   * 
   * @param size
   *          字节
   * @return
   */
  public static String getFileSize(long size) {
    if (size <= 0)
      return "0";
    java.text.DecimalFormat df = new java.text.DecimalFormat("##.##");
    float temp = (float) size / 1024;
    if (temp >= 1024) {
      return df.format(temp / 1024) + "M";
    } else {
      return df.format(temp) + "K";
    }
  }

  /**
   * 获取文件大小
   * 
   * @param filePath
   * @return
   */
  public static long getFileSize(String filePath) {
    long size = 0;

    File file = new File(filePath);
    if (file != null && file.exists()) {
      size = file.length();
    }
    return size;
  }

  /**
   * 计算SD卡的剩余空间
   * 
   * @return 返回-1，说明没有安装sd卡
   */
  public static long getFreeDiskSpace() {
    String status = Environment.getExternalStorageState();
    long freeSpace = 0;
    if (status.equals(Environment.MEDIA_MOUNTED)) {
      try {
        File path = Environment.getExternalStorageDirectory();
        StatFs stat = new StatFs(path.getPath());
        long blockSize = stat.getBlockSize();
        long availableBlocks = stat.getAvailableBlocks();
        freeSpace = availableBlocks * blockSize / 1024;
      } catch (Exception e) {
        e.printStackTrace();
      }
    } else {
      return -1;
    }
    return (freeSpace);
  }

  /**
   * 截取路径名
   * 
   * @return
   */
  public static String getPathName(String absolutePath) {
    int start = absolutePath.lastIndexOf(File.separator) + 1;
    int end = absolutePath.length();
    return absolutePath.substring(start, end);
  }

  /**
   * 获取SD卡的根目录
   * 
   * @return
   */
  public static String getSDRoot() {

    return Environment.getExternalStorageDirectory().getAbsolutePath();
  }

  /**
   * 列出root目录下所有子目录
   * 
   * @param path
   * @return 绝对路径
   */
  public static List<String> listPath(String root) {
    List<String> allDir = new ArrayList<String>();
    SecurityManager checker = new SecurityManager();
    File path = new File(root);
    checker.checkRead(root);
    // 过滤掉以.开始的文件夹
    if (path.isDirectory()) {
      for (File f : path.listFiles()) {
        if (f.isDirectory() && !f.getName().startsWith(".")) {
          allDir.add(f.getAbsolutePath());
        }
      }
    }
    return allDir;
  }

  /**
   * 获取一个文件夹下的所有文件
   * 
   * @param root
   * @return
   */
  public static List<File> listPathFiles(String root) {
    List<File> allDir = new ArrayList<File>();
    SecurityManager checker = new SecurityManager();
    File path = new File(root);
    checker.checkRead(root);
    File[] files = path.listFiles();
    for (File f : files) {
      if (f.isFile())
        allDir.add(f);
      else
        listPath(f.getAbsolutePath());
    }
    return allDir;
  }

  /**
   * 读取文本文件
   * 
   * @param context
   * @param fileName
   * @return
   */
  public static String read(Context context, String fileName) {
    try {
      FileInputStream in = context.openFileInput(fileName);
      return readInStream(in);
    } catch (Exception e) {
      e.printStackTrace();
    }
    return "";
  }

  /**
   * 读取文件内容（使用UTF-8编码）
   * 
   * @param filePath
   *          输出文件路径
   * @return
   * @throws Exception
   */
  public static synchronized String readFileUTF8(String filePath) {
    String fileContent = "";
    try {
      File file = new File(filePath);
      if (file.exists()) {
        FileInputStream fis = new FileInputStream(file);
        BufferedReader br = new BufferedReader(new InputStreamReader(fis, "UTF-8"));

        String temp = "";
        while ((temp = br.readLine()) != null) {
          fileContent = fileContent + temp;
        }
        br.close();
        fis.close();
      } else {
        Logger.d("文件" + filePath + "不存在");
      }

    } catch (FileNotFoundException e) {
      e.printStackTrace();
    } catch (UnsupportedEncodingException e) {
      e.printStackTrace();
    } catch (IOException e) {
      e.printStackTrace();
    }
    return fileContent;
  }

  public static String readInStream(InputStream inStream) {
    try {
      ByteArrayOutputStream outStream = new ByteArrayOutputStream();
      byte[] buffer = new byte[512];
      int length = -1;
      while ((length = inStream.read(buffer)) != -1) {
        outStream.write(buffer, 0, length);
      }

      outStream.close();
      inStream.close();
      return outStream.toString();
    } catch (IOException e) {
      Log.i("FileTest", e.getMessage());
    }
    return null;
  }

  /**
   * 重命名
   * 
   * @param oldName
   * @param newName
   * @return
   */
  public static boolean reNamePath(String oldName, String newName) {
    File f = new File(oldName);
    return f.renameTo(new File(newName));
  }

  public static byte[] toBytes(InputStream in) throws IOException {
    ByteArrayOutputStream out = new ByteArrayOutputStream();
    int ch;
    while ((ch = in.read()) != -1) {
      out.write(ch);
    }
    byte buffer[] = out.toByteArray();
    out.close();
    return buffer;
  }

  /**
   * 写文本文件 在Android系统中，文件保存在 /data/data/PACKAGE_NAME/files 目录下
   * 
   * @param context
   * @param msg
   */
  public static void writeAppFile(Context context, String fileName, String content) {
    if (content == null) {
      content = "";
    }
    FileOutputStream fos = null;
    try {
      fos = context.openFileOutput(fileName, Context.MODE_PRIVATE);
      fos.write(content.getBytes());
    } catch (Exception e) {
      e.printStackTrace();
    } finally {
      if (null != fos) {
        try {
          fos.close();
        } catch (IOException e) {
          e.printStackTrace();
        }
      }
    }
  }

  /**
   * 将字符串写到文件内
   * 
   * @param outputPath
   *          输出文件路径
   * @param msg
   *          字符串
   * @param isApend
   *          是否追加
   * @throws IOException
   */
  public static synchronized void writeContent(String msg, String outputPath, boolean isApend) {
    try {
      BufferedWriter bw = new BufferedWriter(new FileWriter(outputPath, isApend));
      bw.write(msg);
      bw.flush();
      bw.close();
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  /**
   * copy文件
   * 
   * @param is
   *          输入流
   * @param outputPath
   *          输出文件路径
   * @throws Exception
   */
  public static void writeFile(InputStream is, String outputPath) {
    try {
      InputStream bis = null;
      OutputStream bos = null;
      createDir(outputPath);
      bis = new BufferedInputStream(is);
      bos = new BufferedOutputStream(new FileOutputStream(outputPath));
      byte[] bs = new byte[1024 * 10];
      int len = -1;
      while ((len = bis.read(bs)) != -1) {
        bos.write(bs, 0, len);
      }
      bos.flush();
      bis.close();
      bos.close();
    } catch (FileNotFoundException e) {
      e.printStackTrace();
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  /**
   * 写文件
   * 
   * @param outputPath
   *          输出文件路径
   * @param is
   *          输入流
   * @param isApend
   *          是否追加
   * @throws IOException
   */
  public static void writeFile(InputStream is, String outputPath, boolean isApend) throws IOException {
    FileInputStream fis = (FileInputStream) is;
    createDir(outputPath);
    FileOutputStream fos = new FileOutputStream(outputPath, isApend);
    byte[] bs = new byte[1024 * 16];
    int len = -1;
    while ((len = fis.read(bs)) != -1) {
      fos.write(bs, 0, len);
    }
    fos.close();
    fis.close();
  }

  /**
   * 写文件
   * 
   * @param outputPath
   *          输出文件路径
   * @param inPath
   *          输入文件路径
   * @throws IOException
   */
  public static void writeFile(String inPath, String outputPath, boolean isApend) throws IOException {
    if (new File(inPath).exists()) {
      FileInputStream fis = new FileInputStream(inPath);
      writeFile(fis, outputPath, isApend);
    } else {
      System.out.println("文件copy失败，由于源文件不存在!");
    }
  }

  /**
   * 向手机SDcard下写文件
   * 
   * @param buffer
   * @param folderName
   * @param fileName
   * @return
   */
  public static boolean writeFileInFolder(byte[] buffer, String folderPath, String fileName) {
    if (TextUtils.isEmpty(folderPath) || TextUtils.isEmpty(fileName) || buffer == null || buffer.length == 0) {
      return false;
    }
    // 检查传来的参数是不是目录
    File folderFile = new File(folderPath);
    if (!folderFile.isDirectory()) {
      return false;
    }
    boolean writeSucc = false;
    String _folderPath = "";
    if (!folderPath.endsWith(FILE_SEPARATOR)) {
      _folderPath = folderPath + FILE_SEPARATOR;
    } else {
      _folderPath = folderPath;
    }
    createMultilevelDir(_folderPath);
    File file = new File(_folderPath + fileName);
    FileOutputStream out = null;
    try {
      out = new FileOutputStream(file);
      out.write(buffer);
      writeSucc = true;
    } catch (Exception e) {
      e.printStackTrace();
    } finally {
      try {
        out.close();
      } catch (IOException e) {
        e.printStackTrace();
      }
    }

    return writeSucc;
  }

  /**
   * 向手机SDcard下写文件
   * 
   * @param buffer
   * @param folderName
   * @param fileName
   * @return
   */
  public static boolean writeFileOnSDCard(byte[] buffer, String folderName, String fileName) {
    boolean writeSucc = false;

    boolean sdCardExist = FileUtils.checkSDCardAvailable();

    String folderPath = "";
    if (sdCardExist) {
      folderPath = Environment.getExternalStorageDirectory() + File.separator + folderName + File.separator;
    } else {
      writeSucc = false;
    }

    File fileDir = new File(folderPath);
    if (!fileDir.exists()) {
      fileDir.mkdirs();
    }

    File file = new File(folderPath + fileName);
    FileOutputStream out = null;
    try {
      out = new FileOutputStream(file);
      out.write(buffer);
      writeSucc = true;
    } catch (Exception e) {
      e.printStackTrace();
    } finally {
      try {
        out.close();
      } catch (IOException e) {
        e.printStackTrace();
      }
    }

    return writeSucc;
  }

  /**
   * 将文件内容写入文件（使用UTF-8编码）
   * 
   * @param content
   *          文件内容
   * @param filePath
   *          输出文件路径
   * @throws Exception
   */
  public static void writeFileUTF8(String content, String filePath) {
    try {
      createDir(filePath);
      File file = new File(filePath);
      FileOutputStream fos = new FileOutputStream(file);
      BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(fos, "UTF-8"));
      bw.write(content);
      bw.flush();
      bw.close();
      fos.close();
    } catch (FileNotFoundException e) {
      e.printStackTrace();
    } catch (UnsupportedEncodingException e) {
      e.printStackTrace();
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  /**
   * 获取目录文件个数
   * 
   * @param f
   * @return
   */
  public long getFileList(File dir) {
    long count = 0;
    File[] files = dir.listFiles();
    count = files.length;
    for (File file : files) {
      if (file.isDirectory()) {
        count = count + getFileList(file);// 递归
        count--;
      }
    }
    return count;
  }

  /**
   * 检查MIME类型
   * 
   * @param url
   * @return
   */
  public static String getMimeType(String url) {
    String type = null;
    String extension = MimeTypeMap.getFileExtensionFromUrl(url);
    if (extension != null) {
      MimeTypeMap mime = MimeTypeMap.getSingleton();
      type = mime.getMimeTypeFromExtension(extension);
    }
    return type;
  }

  // copy from oplayer
  // http://www.fileinfo.com/filetypes/video , "dat" , "bin" , "rms"
  public static final String[] VIDEO_EXTENSIONS = { "264", "3g2", "3gp", "3gp2", "3gpp", "3gpp2", "3mm", "3p2", "60d", "aep",
      "ajp", "amv", "amx", "arf", "asf", "asx", "avb", "avd", "avi", "avs", "avs", "axm", "bdm", "bdmv", "bik", "bix", "bmk",
      "box", "bs4", "bsf", "byu", "camre", "clpi", "cpi", "cvc", "d2v", "d3v", "dav", "dce", "dck", "ddat", "dif", "dir", "divx",
      "dlx", "dmb", "dmsm", "dmss", "dnc", "dpg", "dream", "dsy", "dv", "dv-avi", "dv4", "dvdmedia", "dvr-ms", "dvx", "dxr",
      "dzm", "dzp", "dzt", "evo", "eye", "f4p", "f4v", "fbr", "fbr", "fbz", "fcp", "flc", "flh", "fli", "flv", "flx", "gl",
      "grasp", "gts", "gvi", "gvp", "hdmov", "hkm", "ifo", "imovi", "imovi", "iva", "ivf", "ivr", "ivs", "izz", "izzy", "jts",
      "lsf", "lsx", "m15", "m1pg", "m1v", "m21", "m21", "m2a", "m2p", "m2t", "m2ts", "m2v", "m4e", "m4u", "m4v", "m75", "meta",
      "mgv", "mj2", "mjp", "mjpg", "mkv", "mmv", "mnv", "mod", "modd", "moff", "moi", "moov", "mov", "movie", "mp21", "mp21",
      "mp2v", "mp4", "mp4v", "mpe", "mpeg", "mpeg4", "mpf", "mpg", "mpg2", "mpgin", "mpl", "mpls", "mpv", "mpv2", "mqv", "msdvd",
      "msh", "mswmm", "mts", "mtv", "mvb", "mvc", "mvd", "mve", "mvp", "mxf", "mys", "ncor", "nsv", "nvc", "ogm", "ogv", "ogx",
      "osp", "par", "pds", "pgi", "piv", "playlist", "pmf", "prel", "pro", "prproj", "psh", "pva", "pvr", "pxv", "qt", "qtch",
      "qtl", "qtm", "qtz", "rcproject", "rdb", "rec", "rm", "rmd", "rmp", "rmvb", "roq", "rp", "rts", "rts", "rum", "rv", "sbk",
      "sbt", "scm", "scm", "scn", "sec", "seq", "sfvidcap", "smil", "smk", "sml", "smv", "spl", "ssm", "str", "stx", "svi",
      "swf", "swi", "swt", "tda3mt", "tivo", "tix", "tod", "tp", "tp0", "tpd", "tpr", "trp", "ts", "tvs", "vc1", "vcr", "vcv",
      "vdo", "vdr", "veg", "vem", "vf", "vfw", "vfz", "vgz", "vid", "viewlet", "viv", "vivo", "vlab", "vob", "vp3", "vp6", "vp7",
      "vpj", "vro", "vsp", "w32", "wcp", "webm", "wm", "wmd", "wmmp", "wmv", "wmx", "wp3", "wpl", "wtv", "wvx", "xfl", "xvid",
      "yuv", "zm1", "zm2", "zm3", "zmv" };
  // http://www.fileinfo.com/filetypes/audio , "spx" , "mid" , "sf"
  public static final String[] AUDIO_EXTENSIONS = { "4mp", "669", "6cm", "8cm", "8med", "8svx", "a2m", "aa", "aa3", "aac", "aax",
      "abc", "abm", "ac3", "acd", "acd-bak", "acd-zip", "acm", "act", "adg", "afc", "agm", "ahx", "aif", "aifc", "aiff", "ais",
      "akp", "al", "alaw", "all", "amf", "amr", "ams", "ams", "aob", "ape", "apf", "apl", "ase", "at3", "atrac", "au", "aud",
      "aup", "avr", "awb", "band", "bap", "bdd", "box", "bun", "bwf", "c01", "caf", "cda", "cdda", "cdr", "cel", "cfa", "cidb",
      "cmf", "copy", "cpr", "cpt", "csh", "cwp", "d00", "d01", "dcf", "dcm", "dct", "ddt", "dewf", "df2", "dfc", "dig", "dig",
      "dls", "dm", "dmf", "dmsa", "dmse", "drg", "dsf", "dsm", "dsp", "dss", "dtm", "dts", "dtshd", "dvf", "dwd", "ear", "efa",
      "efe", "efk", "efq", "efs", "efv", "emd", "emp", "emx", "esps", "f2r", "f32", "f3r", "f4a", "f64", "far", "fff", "flac",
      "flp", "fls", "frg", "fsm", "fzb", "fzf", "fzv", "g721", "g723", "g726", "gig", "gp5", "gpk", "gsm", "gsm", "h0", "hdp",
      "hma", "hsb", "ics", "iff", "imf", "imp", "ins", "ins", "it", "iti", "its", "jam", "k25", "k26", "kar", "kin", "kit",
      "kmp", "koz", "koz", "kpl", "krz", "ksc", "ksf", "kt2", "kt3", "ktp", "l", "la", "lqt", "lso", "lvp", "lwv", "m1a", "m3u",
      "m4a", "m4b", "m4p", "m4r", "ma1", "mdl", "med", "mgv", "midi", "miniusf", "mka", "mlp", "mmf", "mmm", "mmp", "mo3", "mod",
      "mp1", "mp2", "mp3", "mpa", "mpc", "mpga", "mpu", "mp_", "mscx", "mscz", "msv", "mt2", "mt9", "mte", "mti", "mtm", "mtp",
      "mts", "mus", "mws", "mxl", "mzp", "nap", "nki", "nra", "nrt", "nsa", "nsf", "nst", "ntn", "nvf", "nwc", "odm", "oga",
      "ogg", "okt", "oma", "omf", "omg", "omx", "ots", "ove", "ovw", "pac", "pat", "pbf", "pca", "pcast", "pcg", "pcm", "peak",
      "phy", "pk", "pla", "pls", "pna", "ppc", "ppcx", "prg", "prg", "psf", "psm", "ptf", "ptm", "pts", "pvc", "qcp", "r", "r1m",
      "ra", "ram", "raw", "rax", "rbs", "rcy", "rex", "rfl", "rmf", "rmi", "rmj", "rmm", "rmx", "rng", "rns", "rol", "rsn",
      "rso", "rti", "rtm", "rts", "rvx", "rx2", "s3i", "s3m", "s3z", "saf", "sam", "sb", "sbg", "sbi", "sbk", "sc2", "sd", "sd",
      "sd2", "sd2f", "sdat", "sdii", "sds", "sdt", "sdx", "seg", "seq", "ses", "sf2", "sfk", "sfl", "shn", "sib", "sid", "sid",
      "smf", "smp", "snd", "snd", "snd", "sng", "sng", "sou", "sppack", "sprg", "sseq", "sseq", "ssnd", "stm", "stx", "sty",
      "svx", "sw", "swa", "syh", "syw", "syx", "td0", "tfmx", "thx", "toc", "tsp", "txw", "u", "ub", "ulaw", "ult", "ulw", "uni",
      "usf", "usflib", "uw", "uwf", "vag", "val", "vc3", "vmd", "vmf", "vmf", "voc", "voi", "vox", "vpm", "vqf", "vrf", "vyf",
      "w01", "wav", "wav", "wave", "wax", "wfb", "wfd", "wfp", "wma", "wow", "wpk", "wproj", "wrk", "wus", "wut", "wv", "wvc",
      "wve", "wwu", "xa", "xa", "xfs", "xi", "xm", "xmf", "xmi", "xmz", "xp", "xrns", "xsb", "xspf", "xt", "xwb", "ym", "zvd",
      "zvr" };

  private static final HashSet<String> mHashVideo;
  private static final HashSet<String> mHashAudio;
  private static final double KB = 1024.0;
  private static final double MB = KB * KB;
  private static final double GB = KB * KB * KB;

  static {
    mHashVideo = new HashSet<String>(Arrays.asList(VIDEO_EXTENSIONS));
    mHashAudio = new HashSet<String>(Arrays.asList(AUDIO_EXTENSIONS));
  }

  /** 是否是音频或者视频 */
  public static boolean isVideoOrAudio(File f) {
    final String ext = getFileExtension(f);
    return mHashVideo.contains(ext) || mHashAudio.contains(ext);
  }

  public static boolean isVideoOrAudio(String f) {
    final String ext = getUrlExtension(f);
    return mHashVideo.contains(ext) || mHashAudio.contains(ext);
  }

  public static boolean isVideo(File f) {
    final String ext = getFileExtension(f);
    return mHashVideo.contains(ext);
  }

  /** 获取文件后缀 */
  public static String getFileExtension(File f) {
    if (f != null) {
      String filename = f.getName();
      int i = filename.lastIndexOf('.');
      if (i > 0 && i < filename.length() - 1) {
        return filename.substring(i + 1).toLowerCase();
      }
    }
    return null;
  }

  public static String getUrlFileName(String url) {
    int slashIndex = url.lastIndexOf('/');
    int dotIndex = url.lastIndexOf('.');
    String filenameWithoutExtension;
    if (dotIndex == -1) {
      filenameWithoutExtension = url.substring(slashIndex + 1);
    } else {
      filenameWithoutExtension = url.substring(slashIndex + 1, dotIndex);
    }
    return filenameWithoutExtension;
  }

  public static String getUrlExtension(String url) {
    if (!StringUtils.isEmpty(url)) {
      int i = url.lastIndexOf('.');
      if (i > 0 && i < url.length() - 1) {
        return url.substring(i + 1).toLowerCase();
      }
    }
    return "";
  }

  public static String getFileNameNoEx(String filename) {
    if ((filename != null) && (filename.length() > 0)) {
      int dot = filename.lastIndexOf('.');
      if ((dot > -1) && (dot < (filename.length()))) {
        return filename.substring(0, dot);
      }
    }
    return filename;
  }

  public static String showFileSize(long size) {
    String fileSize;
    if (size < KB)
      fileSize = size + "B";
    else if (size < MB)
      fileSize = String.format("%.1f", size / KB) + "KB";
    else if (size < GB)
      fileSize = String.format("%.1f", size / MB) + "MB";
    else
      fileSize = String.format("%.1f", size / GB) + "GB";

    return fileSize;
  }

  /** 显示SD卡剩余空间 */
  public static String showFileAvailable() {
    String result = "";
    if (Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState())) {
      StatFs sf = new StatFs(Environment.getExternalStorageDirectory().getPath());
      long blockSize = sf.getBlockSize();
      long blockCount = sf.getBlockCount();
      long availCount = sf.getAvailableBlocks();
      return showFileSize(availCount * blockSize) + " / " + showFileSize(blockSize * blockCount);
    }
    return result;
  }

  /** 如果不存在就创建 */
  public static boolean createIfNoExists(String path) {
    File file = new File(path);
    boolean mk = false;
    if (!file.exists()) {
      mk = file.mkdirs();
    }
    return mk;
  }

  /** 多个SD卡时 取外置SD卡 */
  public static String getExternalStorageDirectory() {
    // 参考文章
    // http://blog.csdn.net/bbmiku/article/details/7937745
    Map<String, String> map = System.getenv();
    String[] values = new String[map.values().size()];
    map.values().toArray(values);
    String path = values[values.length - 1];
    Log.e("nmbb", "FileUtils.getExternalStorageDirectory : " + path);
    if (path.startsWith("/mnt/") && !Environment.getExternalStorageDirectory().getAbsolutePath().equals(path))
      return path;
    else
      return null;
  }

  public static boolean sdAvailable() {
    return Environment.MEDIA_MOUNTED_READ_ONLY.equals(Environment.getExternalStorageState())
        || Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState());
  }

  public static String getCanonical(File f) {
    if (f == null)
      return null;

    try {
      return f.getCanonicalPath();
    } catch (IOException e) {
      return f.getAbsolutePath();
    }
  }

  /**
   * Get the path for the file:/// only
   * 
   * @param uri
   * @return
   */
  public static String getPath(String uri) {
    Log.i("FileUtils#getPath(%s)", uri);
    if (TextUtils.isEmpty(uri))
      return null;
    if (uri.startsWith("file://") && uri.length() > 7)
      return Uri.decode(uri.substring(7));
    return Uri.decode(uri);
  }

  public static String getName(String uri) {
    String path = getPath(uri);
    if (path != null)
      return new File(path).getName();
    return null;
  }

  public static void deleteDir(File f) {
    if (f.exists() && f.isDirectory()) {
      for (File file : f.listFiles()) {
        if (file.isDirectory())
          deleteDir(file);
        file.delete();
      }
      f.delete();
    }
  }

}