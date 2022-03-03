import java.io.File;
import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.sql.Array;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

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
        HashMap<String, String> lastState = lastSync.getAllHash();
        HashMap<String, String> state = fs.getAllHash();

        if(lastState.equals(state)){
            return l;
        }

        lastState.forEach((k, v) -> {
            System.out.format("key: %s, value: %d%n", k, v);
        });

        return l;
    }

    public void synchronize(FileSystem fs1, FileSystem fs2) throws CloneNotSupportedException, IOException, NoSuchAlgorithmException {
        FileSystem refCopy1 = fs1.getReference();
        FileSystem refCopy2 = fs2.getReference();

        ArrayList <String> dirtyPaths1 = computeDirty(refCopy1, fs1, "");
        ArrayList <String> dirtyPaths2 = computeDirty(refCopy2, fs2, "");
        reconcile(fs1, dirtyPaths1, fs2, dirtyPaths2, "");
    }

    public void reconcile(FileSystem fs1, ArrayList<String> dirtyPaths1, FileSystem fs2, ArrayList<String> dirtyPaths2,String currentRelativePath) {

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
