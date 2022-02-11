import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

public class LocalFileSystem implements FileSystem {

    public LocalFileSystem(){

    }
    // Retourne la racine du système
    public String getRoot() {
        String SE = System.getProperty("os.name").toLowerCase();
        System.out.println(SE);
        if (SE.indexOf("win") >= 0) {
            System.out.println("Votre système est Windows");
            return "C:\\";
          } else if (SE.indexOf("nux") >= 0) {
            System.out.println("Votre système est Unix ou Linux");
            return "/";
          } else if (SE.indexOf("sunos") >= 0) {
            System.out.println("Votre système est Solaris");
          } else {
            System.out.println("Votre système n'est pas pris en charge!");
            return null;
          }
          return null;
        }

    public String getParent() {
        return null;
    }

    // On re tourne une liste avec tous les dossiers et le fichiers 
    public ArrayList<String> getChildren(String path) {
        ArrayList<String> lChildrens = new ArrayList<>();
        File dir = new File(path);
        File[] liste = dir.listFiles();
        for(File item : liste){
            lChildrens.add(item.getName());
        }
        return lChildrens;
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
        Path p = Paths.get(path);
        try {
            Files.createDirectories(p);
        } catch (IOException e) {
            e.printStackTrace();
        }
        File f = new File(path);
        return f;

    }

    public void fileCopy(File input, File output) throws Exception {
        
    }

    public static void main(String[] args) {
        LocalFileSystem test = new LocalFileSystem();
        //String a = test.getRoot();
        //System.out.println(a);
        ArrayList<String> liste = new ArrayList<>();
        liste =  test.getChildren("C:\\");
        for(String s : liste){
            System.out.println(s);
        }
    }  
}
