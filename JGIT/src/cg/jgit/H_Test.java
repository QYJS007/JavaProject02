package cg.jgit;

import java.io.File;
import java.util.Iterator;
import java.util.List;

import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.diff.DiffEntry;
import org.eclipse.jgit.diff.RenameDetector;
import org.eclipse.jgit.lib.ObjectId;
import org.eclipse.jgit.lib.Repository;
import org.eclipse.jgit.revwalk.RevCommit;
import org.eclipse.jgit.treewalk.TreeWalk;

public class H_Test {

	public static void main(String[] args) throws Exception {
		System.out.println("asdasfdsafdz");
		// Git git=Git.open(new File("F:\\G_workspace\\qtrip365\\.git"));
		//List<DiffEntry> log = getLog("F:\\G_workspace\\qtrip365\\.git", "80d2392a8d598b6d7e42857d6af4b5f44dbedf79");
		//List<DiffEntry> log = getLog("F:\\G_workspace\\qtrip365\\.git", "80d2392a8d598b6d7e42857d6af4b5f44dbedf79");
		//List<DiffEntry> log = getLog("F:\\G_workspace\\qtrip365\\.git", "80d2392a8d598b6d7e42857d6af4b5f44dbedf79");
		List<DiffEntry> log = getLog("F:\\G_workspace\\qtrip365\\.git", "3c9e4054b143af5e2bfed78efc30f518ea8abf1a");
		
		for (DiffEntry diffEntry : log) {
			String newPath = diffEntry.getNewPath();
			String oldpath = diffEntry.getOldPath();
			//diffEntry.getPath(null)
			//System.out.println(" new ::: "+  newPath);
			System.out.println( " old :" + oldpath);
			
		}
	}
	/** 
	 * 查询本次提交的日志 
	 * @param gitRoot git仓库 
	 * @param revision  版本号 
	 * @return  
	 * @throws Exception 
	 */  
	public static List<DiffEntry> getLog(String gitRoot, String revision) throws Exception {  
//		Git git = Git.open(new File(gitRoot));  
//		Repository repository = git.getRepository();  

//		ObjectId objId = repository.resolve(revision);  
//		Iterable<RevCommit> allCommitsLater = git.log().add(objId).call();  
//		Iterator<RevCommit> iter = allCommitsLater.iterator();  
		/*RevCommit commit = iter.next();  
		TreeWalk tw = new TreeWalk(repository);  
		tw.addTree(commit.getTree());  

		commit = iter.next();  
		if (commit != null)  
			tw.addTree(commit.getTree());  
		else  
			return null;  

		tw.setRecursive(true);  
		RenameDetector rd = new RenameDetector(repository);  
		rd.addAll(DiffEntry.scan(tw));  

		return rd.compute();  */
	}  


}
