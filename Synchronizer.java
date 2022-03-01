import java.io.File;
import java.sql.Array;
import java.util.ArrayList;

public class Synchronizer {


    public void reconcile(FileSystem fs1, ArrayList<String> dirtyPaths1, FileSystem fs2, ArrayList<String> dirtyPaths2,String currentRelativePath) {

    }

    public ArrayList<String> computeDirty(FileSystem lastSync, FileSystem fs, String currentRelativePath){
        ArrayList<String> l = new ArrayList<>();
        return l;
    }
    public void synchronize(FileSystem fs1, FileSystem fs2) throws CloneNotSupportedException{
        FileSystem refCopy1 = fs1.getReference();
        FileSystem refCopy2 = fs2.getReference();

        ArrayList<String> 
    }
}
