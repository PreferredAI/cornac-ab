package ai.preferred.cornac.util;

import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

public class FileUtil {
    public static boolean isFileExtension(MultipartFile file, String fileExtension) {
        String fileName = file.getOriginalFilename();

        if (fileName == null) {
            return false;
        }

        int lastIndex = fileName.lastIndexOf('.');
        String filenameExt = fileName.substring(lastIndex).toLowerCase();
        return filenameExt.contains(fileExtension.toLowerCase());
    }

    // Reference: https://www.baeldung.com/java-compress-and-uncompress
    public static void unzipFile(String fileDir, String outputFolder, boolean ignoreFolders) throws IOException{
        File destDir = new File(outputFolder);
        byte[] buffer = new byte[1024];
        ZipInputStream zipInputStream = new ZipInputStream(new FileInputStream(fileDir));
        ZipEntry zipEntry = zipInputStream.getNextEntry();

        while(zipEntry != null) {
            File newFile = newFile(destDir, zipEntry);
            if (zipEntry.isDirectory()) {
                if (!ignoreFolders && !newFile.isDirectory() && !newFile.mkdirs()) {
                    throw new IOException("Failed to create directory " + newFile);
                }
            } else {
                // fix for Windows-created archives
                File parent = newFile.getParentFile();
                if (!parent.isDirectory() && !parent.mkdirs()) {
                    throw new IOException("Failed to create directory " + parent);
                }

                // write file content
                try (FileOutputStream fileOutputStream = new FileOutputStream(newFile)) {
                    int len;
                    while ((len = zipInputStream.read(buffer)) > 0) {
                        fileOutputStream.write(buffer, 0, len);
                    }
                }
            }
            zipEntry = zipInputStream.getNextEntry();
        }

        zipInputStream.closeEntry();
        zipInputStream.close();
    }

    // newFile method is used to guard against writing files to file system outside target folder
    // -> Zip slip vulnerability
    private static File newFile(File destinationDir, ZipEntry zipEntry) throws IOException {
        File destFile = new File(destinationDir, zipEntry.getName());

        String destDirPath = destinationDir.getCanonicalPath();
        String destFilePath = destFile.getCanonicalPath();

        if (!destFilePath.startsWith(destDirPath + File.separator)) {
            throw new IOException("Entry is outside of the target dir: " + zipEntry.getName());
        }

        return destFile;
    }
}
