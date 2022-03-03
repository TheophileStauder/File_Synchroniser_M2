import jdk.swing.interop.SwingInterOpUtils;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.attribute.BasicFileAttributes;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;

public class LocalFileSystem implements FileSystem,Cloneable {

    private String root;
    private static Calendar cal = Calendar.getInstance();

    public LocalFileSystem(String racine){
        this.root = racine;
    }
    // Retourne la racine du système
    public String getRoot() {

        return this.root;
    }


    public String getParent(String path) {
        File file = new File(path);
        if(file.exists()){
                return file.getParentFile().getName();
        }
        return "";
    }

    public ArrayList<String> getChildren(String path) {
        ArrayList<String> lChildrens = new ArrayList<>();
        File dir  = new File(path);
        File[] liste = dir.listFiles();
        for(File item : liste){
            lChildrens.add(path+File.separator+item.getName());
        }
        return lChildrens;
    }

    public ArrayList<String> getAncestors(String path) {
        ArrayList<String> lAncestors = new ArrayList<>();
        File file = new File(path);
        while(!file.getAbsolutePath().equals(getRoot())){
            file = file.getParentFile();
            lAncestors.add(file.getAbsolutePath());
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
        FileSystem copy = (LocalFileSystem) this.clone();
        //FileSystem copy = new Rthis;
        return copy;
    }


    /**
     * Crée un répertoire
     * @param path
     * @return
     */
    public File createDirectory(String path) throws IOException {
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

    public String hashDirectory(String filePath) throws IOException, NoSuchAlgorithmException {
        //Create checksum for this file
        String checksum = "";
        File test;
        ArrayList<String> childs = getChildren(filePath);
        for (String c : childs ){
            test = new File(c);
            if(test.isFile()){
                checksum = checksum + hashFile(c);
            }else{
                checksum = checksum + test.getName();
            }
        }


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

    public HashMap<String, String> getAllHash() throws IOException, NoSuchAlgorithmException {
        HashMap<String, String> allHash = new HashMap<>();
        ArrayList<String> visited = new ArrayList<>();
        //Parcours DFS
        ArrayList<String> childs = getChildren(getRoot());
        for (String c : childs ){
            visited.add(c);
            explore(c,allHash,visited);
        }
        return allHash;
    }

    public void explore(String c, HashMap<String, String> hashs,ArrayList<String> visited) throws IOException, NoSuchAlgorithmException{
        File test = new File(c);


        if(test.isDirectory()){
            hashs.put(c,hashDirectory(c));
            System.out.println(c + " est un directory");
        }
        if(test.isFile()){
            System.out.println(c + " est un fichier ");
            hashs.put(c,hashFile(c));
        }
        ArrayList<String> childs = getChildren(getRoot());
        for (String child : childs ){
            if(!visited.contains(c)) {
                visited.add(c);
                explore(child, hashs,visited);
            }
        }
    }

    /**
     * Cette fonction permet d'obtenir la date de modification d'un fichier ou d'un dossier.
     * @param file le fichier ou le dossier dont il faut obtenir la date de modification.
     * @return La date de modification du fichier ou du dossier donné en paramètre de la méthode.
     */
    public static long getDateModification(File file){
        
        Path path = file.toPath();

        try {
            BasicFileAttributes basic = Files.readAttributes(path, BasicFileAttributes.class);

            cal.setTimeInMillis(basic.lastModifiedTime().toMillis());
        } catch (IOException e) {
            e.printStackTrace(System.out);
        }

        return cal.getTimeInMillis();
    }


    public static void main(String[] args) throws IOException, NoSuchAlgorithmException, CloneNotSupportedException {

        LocalFileSystem fileSystem = null ;
        String SE = System.getProperty("os.name").toLowerCase();
        if (SE.indexOf("win") >= 0) {
            //Pour WINDOWS
            fileSystem = new LocalFileSystem("C:\\UE-SYN");
              //A CHANGER J'ETAIS SUR LES ORDIS DE LA FAC CAR PLUS DE BATTERIE SUR MON PC : /home/UE-SYN
        } else if (SE.indexOf("nux") >= 0) {
            //Pour LINUX
            fileSystem = new LocalFileSystem("/home/profil/stauder11u/UE-SYN");
        }




        /* Test getRoot */
        String root = fileSystem.getRoot();
        System.out.println("Racine : " +root);
        System.out.println("**************** RACINE SYSTEM *****************");
        System.out.println("La racine est : " + fileSystem.getRoot());
        System.out.println("************************************************\n");




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


        /* Test getParent */
        System.out.println("**************** Test GET PARENT ****************");
        String testPath = fileSystem.getRoot()+File.separator+"testDirectory0"+File.separator+"testDirectory01";
        String parent2 = fileSystem.getParent(testPath);
        System.out.println("Parent de " + testPath + " : " + parent2);
        testPath = fileSystem.getRoot()+File.separator+"testDirectory0"+File.separator+"dog.txt";
        parent2 = fileSystem.getParent(testPath);
        System.out.println("Parent de " + testPath + " : " + parent2);
        System.out.println("************************************************\n");


        /* Test getAncestors */
        //String pathAncestors = fileSystem.getChildren("C:\\UE-SYN\\testDirectory1").get(0);
        System.out.println("**************** Test GET ANCETRES ****************");
        ArrayList<String> lAncestors = new ArrayList<String>();
        testPath = fileSystem.getRoot() +File.separator+"testDirectory0"+File.separator+"testDirectory01"+File.separator+"testDirectory010";
        lAncestors = fileSystem.getAncestors(testPath);
        System.out.println("Les ancêtres de " + testPath + " sont : " + lAncestors.toString());
        System.out.println("************************************************\n");



        /*Test getAbsolutePath */

        /* Test hash file */
        System.out.println("**************** Test HASH MD5 ****************");
        testPath = fileSystem.getRoot() +File.separator+"testDirectory0"+File.separator+"dog.txt";
        String hash = fileSystem.hashFile(testPath);
        System.out.println("Hash de " + testPath + " : " + hash);
        testPath = fileSystem.getRoot() +File.separator+"testDirectory0";
        hash = fileSystem.hashDirectory(testPath);
        System.out.println("Hash de " + testPath + " : " + hash);
        System.out.println("************************************************\n");

        /*Test getAbsolutePath */

        /*Test getDateModification */
        System.out.println("**************** Test getDateModifiction ****************");
        long lo;
        File ff = new File("C://testDirectory0//dog.txt");
        lo = fileSystem.getDateModification(ff);
        System.out.println("Date de modification en millisecondes pour le fichier "+ff+ " : "+lo);
        System.out.println("************************************************\n");

        /* Test hash file */
        System.out.println("**************** Test SYNCHRONISER ****************");
        Synchronizer synchronizer = new Synchronizer();
        LocalFileSystem fs1 = new LocalFileSystem("C:"+File.separator + "UE-SYN1");
        LocalFileSystem fs2 = new LocalFileSystem("C:"+File.separator + "UE-SYN2");


        while(true){
            ArrayList<String> paths = synchronizer.computeDirty(fs1.getReference(),fs1,"");
            if(paths.size() !=0){
                System.out.println(paths.toString());
            }

        }
        //System.out.println("************************************************\n");
    }
    
}
