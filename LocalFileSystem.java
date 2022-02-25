import jdk.swing.interop.SwingInterOpUtils;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;

public class LocalFileSystem implements FileSystem {


    private String root;

    public LocalFileSystem(String racine){
        this.root = racine;
    }
    // Retourne la racine du système
    public String getRoot() {
        /*String SE = System.getProperty("os.name").toLowerCase();
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
          }*/
        return this.root;
    }


    public String getParent(String path) {
        File file = new File(path);
        if(file.exists()){
            if(file.isDirectory()) {
                return file.getParentFile().getName();
            }
        }
        return "";
    }

    public ArrayList<String> getChildren(String path) {
        ArrayList<String> lChildrens = new ArrayList<>();
        File dir  = new File(path);
        File[] liste = dir.listFiles();
        for(File item : liste){
            lChildrens.add(item.getName());
        }
        return lChildrens;
    }

    public ArrayList<String> getAncestors(String path) {
        /*ArrayList<String> lChildrens = new ArrayList<>();
        ArrayList<String> copy = new ArrayList<>();

        while(!path.equals(getRoot()));{
            copy = getChildren(getParent(path));
            lChildrens.addAll(copy);
            copy.clear();
        }
        return lChildrens;*/
        ArrayList<String> lAncestors = new ArrayList<>();
        while(!path.equals(getRoot())){
            path = getParent(path);
            lAncestors.add(path);
            System.out.println("coucou");
        }

        return lAncestors;
    }

    public String getAbsolutePath(String relativePath) {
        return "/"+relativePath;
    }

    public String getRelativePath(String absolutePath) {
        return null;
    }

    public void replace(String absolutePathTargetFS, FileSystem fsSource, String absolutePathSourceFS) {
        
    }


    /**
     * Retourne une copie de l'objet sans référence
     */
    public FileSystem getReference() throws CloneNotSupportedException {
        FileSystem copy = (FileSystem) this.clone();
        return copy;
    }


    /**
     * Crée un répertoire
     * @param path
     * @return
     */
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
    public String hashFile(String filePath) throws IOException, NoSuchAlgorithmException {
        //Create checksum for this file
        File file = new File(filePath);

        //Use MD5 algorithm
        MessageDigest md5Digest = MessageDigest.getInstance("MD5");

        //Get the checksum
        String checksum = getFileChecksum(md5Digest, file);

        //see checksum
        return checksum;
    }

    private static String getFileChecksum(MessageDigest digest, File file) throws IOException
    {
        //Get file input stream for reading the file content
        FileInputStream fis = new FileInputStream(file);

        //Create byte array to read data in chunks
        byte[] byteArray = new byte[1024];
        int bytesCount = 0;

        //Read file data and update in message digest
        while ((bytesCount = fis.read(byteArray)) != -1) {
            digest.update(byteArray, 0, bytesCount);
        };

        //close the stream; We don't need it now.
        fis.close();

        //Get the hash's bytes
        byte[] bytes = digest.digest();

        //This bytes[] has bytes in decimal format;
        //Convert it to hexadecimal format
        StringBuilder sb = new StringBuilder();
        for(int i=0; i< bytes.length ;i++)
        {
            sb.append(Integer.toString((bytes[i] & 0xff) + 0x100, 16).substring(1));
        }

        //return complete hash
        return sb.toString();
    }
    public static void main(String[] args) throws IOException, NoSuchAlgorithmException {

        LocalFileSystem fileSystem = null ;
        String SE = System.getProperty("os.name").toLowerCase();
        if (SE.indexOf("win") >= 0) {
            //Pour WINDOWS
            fileSystem = new LocalFileSystem("/home/profil/stauder11u/UE-SYN");  //A CHANGER J'ETAIS SUR LES ORDIS DE LA FAC CAR PLUS DE BATTERIE SUR MON PC : /home/UE-SYN
        } else if (SE.indexOf("nux") >= 0) {
            //Pour LINUX
            fileSystem = new LocalFileSystem("C:\\UE-SYN");
        }




        /* Test getRoot */
        String root = fileSystem.getRoot();
        System.out.println("Racine : " +root);
        System.out.println("**************** RACINE SYSTEM *****************");
        System.out.println("La racine est : " + fileSystem.getRoot());
        System.out.println("************************************************\n");
        /* Test getParent */
        //String testPath = "C:\\Python27\\Python2.exe";
        //String parent = fileSystem.getParent(testPath);
        //System.out.println("Parent de " + testPath + " : " +parent);



        /* Test createDirectory */
        System.out.println("**************** CREATE DIRECTORY *****************");
        for(int i = 0 ; i < 5 ; i++){
            fileSystem.createDirectory(fileSystem.getRoot()+File.separator+"testDirectory"+i);
            System.out.println("Dossier créé : " + fileSystem.getRoot()+File.separator+"testDirectory"+i);
        }
        System.out.println("************************************************\n");

        /* Test getChildren */
        String parent = fileSystem.getRoot();
        System.out.println("**************** Test GETCHILDREN ****************");
        System.out.println("Les enfants de "+ parent +" sont : ");
        ArrayList<String> lChildrens = new ArrayList<>();
        lChildrens = fileSystem.getChildren(parent);
        for(String s : lChildrens){
            System.out.println(s);
        }
        System.out.println("************************************************\n");


        /* Test getAncestors */
        //String pathAncestors = fileSystem.getChildren("C:\\UE-SYN\\testDirectory1").get(0);
        System.out.println("**************** Test GET ANCETRES ****************");
        ArrayList<String> lAncestors = new ArrayList<String>();
        lAncestors = fileSystem.getAncestors(fileSystem.getRoot() +File.separator+"testDirectory0"+File.separator+"testDirectory01"+File.separator+"testDirectory010");
        System.out.println(lAncestors.toString());
        System.out.println("************************************************\n");



        /*Test getAbsolutePath */

        /* Test hash file */
        System.out.println("**************** Test HASH ****************");

        String hash = fileSystem.hashFile(fileSystem.getRoot() +File.separator+"testDirectory0"+File.separator+"dog.txt");
        System.out.println(hash);
        System.out.println("************************************************\n");
    }
    
}
