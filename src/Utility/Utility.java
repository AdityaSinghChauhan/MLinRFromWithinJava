package Utility;
import java.io.IOException;
import java.nio.file.FileVisitResult;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.HashMap;

public class Utility {
	public static boolean isNullOrBlank(String str)
    {
    	if (null == str || "".equals(str.trim()))
    	{
    		return true;
    	}
    	return false;
    }
	

	public static boolean deleteDir(String path){
    	Path directory = Paths.get(path);
        try {
			Files.walkFileTree(directory, new SimpleFileVisitor<Path>() {
			   @Override
			   public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {
			       Files.delete(file);
			       return FileVisitResult.CONTINUE;
			   }

			   @Override
			   public FileVisitResult postVisitDirectory(Path dir, IOException exc) throws IOException {
			       Files.delete(dir);
			       return FileVisitResult.CONTINUE;
			   }
			});
		} catch (IOException e) {
			return false;
		}
        return true;
    }
    
}
