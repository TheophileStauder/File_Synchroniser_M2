import java.io.File;
import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public interface FileSystem{
    public String getRoot();

    public String getParent(String path);

    public ArrayList<String> getChildren(String path);

    public ArrayList<String> getAncestors(String path);
    public String getAbsolutePath(String relativePath);
    public String getRelativePath(String absolutePath);
    public void replace(String absolutePathTargetFS, FileSystem fsSource, String absolutePathSourceFS);
    public FileSystem getReference() throws CloneNotSupportedException;
    public File createDirectory(String path);
    public void fileCopy(File input, File output) throws Exception;


    HashMap<String, String> getAllHash() throws IOException, NoSuchAlgorithmException;
}