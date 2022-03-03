import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
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
        Thread.sleep(1000);
        ArrayList <String> dirtyPaths1 = computeDirty(refCopy1, fs1, "");
        ArrayList <String> dirtyPaths2 = computeDirty(refCopy2, fs2, "");
        reconcile(fs1, dirtyPaths1, fs2, dirtyPaths2, "");
    }

    /*
    * A est le fileSystem 
    * B est le fileSystem 
    * p est le path 
    recon(A,B,p) = 
    1) if !dirty_A(p) && !dirty_B(p) Si les fichiers ne sont pas dirty on ne fait rien 
            then (A,B)
    2) else if isdir_A,B(p) 
            then let {p1,p2,...,pn} = children_A,B(p) (in lexicographic order)
            in let (A0,B0) = (A,B)
                let (Ai+1, Bi+1) = recon(Ai,Bi,pi+1)
                    for 0 <= i <= n
                in (An,Bn)
    3) else if !dirty_A(p) Si A n'est pas dirty mais B est dirty alors A prends B
            then (A <-- B, B)
    4) else if !dirty_B(p) Si B n'est pas dirty mais A est dirty alors B prends A
            then (A,B <-- A)
    5) else
            (A,B)

    Suppositions : 
        Si la liste des dirtypaths est vide alors on ne fait rien vu que aucun fichier est modifié ou supprimé
    */
    public void reconcile(FileSystem fs1, ArrayList<String> dirtyPaths1, FileSystem fs2, ArrayList<String> dirtyPaths2,String currentRelativePath) throws IOException {
        if(dirtyPaths1.isEmpty() && dirtyPaths2.isEmpty()){
            // on ne fait rien, les fichiers et dossiers ne sont pas dirtys
            System.out.println("AUCUNE MODIFICATION");
        }
        else if(!dirtyPaths1.isEmpty() && !dirtyPaths2.isEmpty()){
            System.out.println(" MODIFICATION PARTOUT");
        }
        else if(!dirtyPaths1.isEmpty()){
            for(String path : dirtyPaths1){
                File source = new File(path);
                String pathDest = path;
                pathDest = pathDest.replace(fs1.getRoot(),fs2.getRoot());
                File dest = new File(pathDest);
                if(source.exists()){
                    Files.copy(source.toPath(), dest.toPath() );
                }else{
                    dest.delete();
                }
            }
        }
        else if(!dirtyPaths2.isEmpty()){
            System.out.println("modif UE SYN2");
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
}
