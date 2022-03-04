import java.io.File;
import java.io.IOException;
import java.nio.file.*;
import java.nio.file.StandardCopyOption;
import java.security.NoSuchAlgorithmException;
import java.sql.Array;
import java.util.*;

public class Synchronizer {

    private LocalFileSystem filesystemA;
    private LocalFileSystem filesystemB;

    public Synchronizer (){

    }

    public Synchronizer (LocalFileSystem lfs1,LocalFileSystem lfs2){
        filesystemA = lfs1;
        filesystemB = lfs2;
    }


    public ArrayList<String> computeDirty(FileSystem lastSync, FileSystem fs, String currentRelativePath) throws IOException, NoSuchAlgorithmException {
        ArrayList<String> l = new ArrayList<>();
        HashMap<String, String> lastState = lastSync.getHashes();
        HashMap<String, String> state = fs.getAllHash();

        /*lastState.forEach((k, v) -> {
            //System.out.format("last statekey: %s, value: %s%n", k, v);
        });

        state.forEach((k, v) -> {
            //System.out.format("state : key: %s, value: %s%n", k, v);
        });*/
        //System.out.println(lastState.toString());
        
        //System.out.println(state.toString());

        if(lastState.equals(state)){
            //System.out.println("Les deux fichiers sont equals"); // donc il n'y a rien à faire
            return l;
        }
        else{
            //System.out.println("Les deux fichiers ne sont pas equals"); // donc il faut les ajouter à la liste
            /*for (HashMap.Entry<String, String> entry : state.entrySet()) {
                l.add(entry.getKey());
            }*/

            //Chercher fichier ajouté
            state.forEach((k, v) -> {
                if(!lastState.containsValue(v)){
                    l.add(k);
                }

            });
            //Chercher fichier supprimé
            lastState.forEach((k, v) -> {
                if(!state.containsValue(v)){
                    l.add(k);
                }

            });

            Set<String> set = new HashSet<>(l);
            l.clear();
            l.addAll(set);
        }
        return l;
    }

    public void synchronize(FileSystem fs1, FileSystem fs2) throws CloneNotSupportedException, IOException, NoSuchAlgorithmException, InterruptedException {
        FileSystem refCopy1 = fs1.getReference();
        FileSystem refCopy2 = fs2.getReference();
        Thread.sleep(4000);
        ArrayList <String> dirtyPaths1 = computeDirty(refCopy1, fs1, "");
        ArrayList <String> dirtyPaths2 = computeDirty(refCopy2, fs2, "");
        System.out.println("Dirty 1 : "+ dirtyPaths1.toString());
        System.out.println("Dirty 2 : "+ dirtyPaths2.toString());

        if(!dirtyPaths1.isEmpty() || !dirtyPaths2.isEmpty()){
            // on ne fait rien, les fichiers et dossiers ne sont pas dirtys
            System.out.println("MODIFICATION");
            reconcile(fs1,dirtyPaths1,fs2,dirtyPaths2,"");
        }

    }
    public void reconcile(FileSystem fs1, List<String> dirtyPaths1, FileSystem fs2, List<String> dirtyPaths2, String currentRelativePath) throws IOException {


        if(!dirtyPaths1.isEmpty()){
            for(String path : dirtyPaths1){
                File source = new File(path);
                String pathDest = path;
                pathDest = pathDest.replace(fs1.getRoot(),fs2.getRoot());
                File dest = new File(pathDest);
                if(source.exists()){
                    if(source.isDirectory()){
                        dest.mkdir();
                        ArrayList<String> childs1 = fs1.getChildren(path);
                        reconcile(fs1,childs1,fs2,dirtyPaths2,"");
                    }else{
                        Files.copy(source.toPath(), dest.toPath(), StandardCopyOption.REPLACE_EXISTING );
                    }

                }else{
                    dest.delete();
                }
            }
        }
        else if(!dirtyPaths2.isEmpty()){
            for(String path : dirtyPaths2){
                File source = new File(path);
                String pathDest = path;
                pathDest = pathDest.replace(fs2.getRoot(),fs1.getRoot());
                File dest = new File(pathDest);
                if(source.exists()){
                    if(source.isDirectory()){
                        dest.mkdir();
                        ArrayList<String> childs2 = fs2.getChildren(path);
                        reconcile(fs1,dirtyPaths1,fs2,childs2,"");
                    }else{
                        Files.copy(source.toPath(), dest.toPath() ,StandardCopyOption.REPLACE_EXISTING );
                    }
                }else{
                    dest.delete();
                }
            }
        }
    }

    public LocalFileSystem getFilesystemA() {
        return filesystemA;
    }

    public LocalFileSystem getFilesystemB() {
        return filesystemB;
    }

    public void setFilesystemA(LocalFileSystem filesystemA) {
        this.filesystemA = filesystemA;
    }

    public void setFilesystemB(LocalFileSystem filesystemB) {
        this.filesystemB = filesystemB;
    }

    public void initialization(LocalFileSystem fs1, LocalFileSystem fs2) throws IOException, NoSuchAlgorithmException, CloneNotSupportedException {
        ArrayList<String> childfs1 = fs1.getChildren(fs1.getRoot());
        for(String child : childfs1){
            File src = new File(child);
            String pathDest = child;
            pathDest = pathDest.replace(fs1.getRoot(),fs2.getRoot());
            File dest = new File(pathDest);
            if(src.exists()){
                if(src.isDirectory()){
                    System.out.println("DIRECTORY");
                }else{
                    Files.copy(src.toPath(), dest.toPath(),StandardCopyOption.REPLACE_EXISTING);
                }
            }
        }

        ArrayList<String> childfs2 = fs2.getChildren(fs2.getRoot());
        for(String child : childfs2){
            File src = new File(child);
            String pathDest = child;
            pathDest = pathDest.replace(fs2.getRoot(),fs1.getRoot());
            File dest = new File(pathDest);
            if(src.exists()){
                if(src.isDirectory()){
                    System.out.println("DIRECTORY");
                    //FileUtils.copyDirectory(srcDir, destDir);
                }else{
                    System.out.println(src.toPath().toString());
                    System.out.println(dest.toPath().toString());
                    Files.copy(src.toPath(), dest.toPath(),StandardCopyOption.REPLACE_EXISTING);
                }
            }
        }


    }
}
