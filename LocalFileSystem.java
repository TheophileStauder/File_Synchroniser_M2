import java.io.File;
import java.util.ArrayList;

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
          } else{
            System.out.println("Votre système n'est pas pris en charge!");
            return "";
          }
    }


    public String getParent(String path) {
        File file = new File(path);
        if(file.exists()){
            if(file.isDirectory()){
                return file.getParentFile().getName();
            }else{
                return file.getParentFile().getName();
            }
        }
        return "";
    }

    public ArrayList<String> getChildren() {
        return null;
    }

    public ArrayList<String> getAncestors(String path) {
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

    public static void main(String[] args) {

        LocalFileSystem test = new LocalFileSystem();

        /* Test getRoot */
        String root = test.getRoot();
        System.out.println(root);

        /* Test getParent */
        String parent = test.getParent("C:\\Python27");
        System.out.println(parent);

        /* Test getChildren */

        /* Test getAncestrors */

        /*Test getAbsolutePath */


    }
    
}
