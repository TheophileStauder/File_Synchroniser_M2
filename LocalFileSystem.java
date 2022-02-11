import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

public class LocalFileSystem implements FileSystem {

    // Retourne la racine du système
    public String getRoot() {
        return "/";
    }
    
    public String getParent() {
        return null;
    }

    public List<String> getChildren() {
        return null;
    }

    public List<String> getAncestors(String path) {
        return null;
    }

    public String getAbsolutePath(String relativePath) {
        return "/"+relativePath;
    }

    public String getRelativePath(String absolutePath) {
        return null;
    }

    public void replace(String absolutePathTargetFS, FileSystem fsSource, String absolutePathSourceFS) {
        
    }
    // référence vers le système de fichier 
    public FileSystem getReference() {
        return null;
    }

    public File createDirectory(String path) {
        return null;

    }

    public void fileCopy(File input, File output) throws Exception {
        
    }
    
}
