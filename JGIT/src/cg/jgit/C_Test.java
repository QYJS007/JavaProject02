package cg.jgit;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.ObjectInputStream.GetField;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.eclipse.jgit.dircache.DirCache;
import org.eclipse.jgit.events.ListenerList;
import org.eclipse.jgit.lib.AnyObjectId;
import org.eclipse.jgit.lib.FileMode;
import org.eclipse.jgit.lib.ObjectId;
import org.eclipse.jgit.lib.Ref;
import org.eclipse.jgit.lib.Repository;
import org.eclipse.jgit.lib.RepositoryBuilder;
import org.eclipse.jgit.revwalk.RevCommit;
import org.eclipse.jgit.revwalk.RevTree;
import org.eclipse.jgit.revwalk.RevWalk;
import org.eclipse.jgit.treewalk.TreeWalk;

public class C_Test {

	public static void main(String[] args) throws Exception {
		Repository repository = new RepositoryBuilder().setGitDir(new File("F:\\G_workspace\\qtrip365\\.git")).build();
		File directory = repository.getDirectory();
		System.out.println(directory);
		
		/*Map<String, Ref> allRefs = repository.getAllRefs();
		Set<String> keySet = allRefs.keySet();
		for (String string : keySet) {
			
			System.out.println( "key:"+ string + " sss:"+ allRefs.get(string));
		}*/
		
		/*
		Map<AnyObjectId, Set<Ref>> allRefsByPeeledObjectId = repository.getAllRefsByPeeledObjectId();
		Set<AnyObjectId> keySet = allRefsByPeeledObjectId.keySet();
		for (AnyObjectId anyObjectId : keySet) {
			
			System.out.println( "key:"+anyObjectId +"        value: "+ allRefsByPeeledObjectId.get(anyObjectId) );
			
		}*/
		// bdfe628a39c2ef429b42d62d2f53084b40df42a1
		
		String branch = repository.getBranch();
		System.out.println(" 分支: "+  branch);
		ListenerList list = repository.getListenerList();
		System.out.println(list.toString());
		// try (Repository repository = CookbookHelper.openJGitCookbookRepository()) {
		//LogCommit: commit 84a100d3589259748eb4655d504712af9fbf26dd 1444359761 ----sp
		//List<String> paths = readElementsAt(repository, "84a100d3589259748eb4655d504712af9fbf26dd", "src/porcelain");
		
		

		//System.out.println("Had paths for commit: " + paths);

		//final ObjectId testbranch = repository.resolve("testbranch");
		//paths = readElementsAt(repository, testbranch.getName(), "src/porcelain");

		//System.out.println("Had paths for tag: " + paths);
	}
	// }
	//
	private static List<String> readElementsAt(Repository repository, String commit, String path) throws IOException {
		RevCommit revCommit = buildRevCommit(repository, commit);

		// and using commit's tree find the path
		RevTree tree = revCommit.getTree();
		//System.out.println("Having tree: " + tree + " for commit " + commit);

		List<String> items = new ArrayList<>();

		// shortcut for root-path
		if(path.isEmpty()) {
			try (TreeWalk treeWalk = new TreeWalk(repository)) {
				treeWalk.addTree(tree);
				treeWalk.setRecursive(false);
				treeWalk.setPostOrderTraversal(false);

				while(treeWalk.next()) {
					items.add(treeWalk.getPathString());
				}
			}
		} else {
			// now try to find a specific file
			try (TreeWalk treeWalk = buildTreeWalk(repository, tree, path)) {
				if((treeWalk.getFileMode(0).getBits() & FileMode.TYPE_TREE) == 0) {
					throw new IllegalStateException("Tried to read the elements of a non-tree for commit '" + commit + "' and path '" + path + "', had filemode " + treeWalk.getFileMode(0).getBits());
				}

				try (TreeWalk dirWalk = new TreeWalk(repository)) {
					dirWalk.addTree(treeWalk.getObjectId(0));
					dirWalk.setRecursive(false);
					while(dirWalk.next()) {
						items.add(dirWalk.getPathString());
					}
				}
			}
		}

		return items;
	}

	private static RevCommit buildRevCommit(Repository repository, String commit) throws IOException {
		// a RevWalk allows to walk over commits based on some filtering that is defined
		try (RevWalk revWalk = new RevWalk(repository)) {
			return revWalk.parseCommit(ObjectId.fromString(commit));
		}
	}

	private static TreeWalk buildTreeWalk(Repository repository, RevTree tree, final String path) throws IOException {
		TreeWalk treeWalk = TreeWalk.forPath(repository, path, tree);

		if(treeWalk == null) {
			throw new FileNotFoundException("Did not find expected file '" + path + "' in tree '" + tree.getName() + "'");
		}

		return treeWalk;
	}


}
