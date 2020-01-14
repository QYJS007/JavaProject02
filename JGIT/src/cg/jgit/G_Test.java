package cg.jgit;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.diff.DiffEntry;
import org.eclipse.jgit.diff.DiffFormatter;
import org.eclipse.jgit.diff.RawTextComparator;
import org.eclipse.jgit.lib.Repository;
import org.eclipse.jgit.revwalk.RevCommit;
import org.eclipse.jgit.revwalk.RevTree;
import org.eclipse.jgit.revwalk.RevWalk;
import org.eclipse.jgit.treewalk.AbstractTreeIterator;
import org.eclipse.jgit.treewalk.CanonicalTreeParser;

public class G_Test {
	public static void main(String[] args) throws Exception {

//		Git git=Git.open(new File("F:\\G_workspace\\qtrip365\\.git"));
		/*Git git= new Git();
		Repository  repository=git.getRepository();
		List<RevCommit> list=new ArrayList<RevCommit>();
		Iterable<RevCommit> iterable=git.log().setMaxCount(2).call();

		for(RevCommit revCommit:iterable){
			list.add(revCommit);
		}
		if(list.size()==2){
			AbstractTreeIterator newCommit=getAbstractTreeIterator(list.get(0),repository);
			AbstractTreeIterator oldCommit=getAbstractTreeIterator(list.get(1),repository);
			List<DiffEntry> diff=git.diff().setOldTree(oldCommit).setNewTree(newCommit).call();
			
			ByteArrayOutputStream outputStream=new ByteArrayOutputStream();
			DiffFormatter diffFormatter=new DiffFormatter(outputStream);
			
			//设置比较器为忽略空白字符对比（Ignores all whitespace）
			diffFormatter.setDiffComparator(RawTextComparator.WS_IGNORE_ALL);
			diffFormatter.setRepository(repository); // 这里为什么还要设置它
			for(DiffEntry diffEntry:diff){
				diffFormatter.format(diffEntry);
				System.out.println(outputStream.toString("UTF-8"));
				outputStream.reset();
			}
		}

		git.close();*/

		//git.log().all()
		/*StatusCommand status = git.status();
		List<String> paths = status.getPaths();
		for (String string : paths) {

			System.out.println(string);
		}*/

	}
	public static AbstractTreeIterator getAbstractTreeIterator(RevCommit commit, Repository repository ){
		RevWalk revWalk=new RevWalk(repository);
		CanonicalTreeParser treeParser=null;
		try {
			RevTree revTree=revWalk.parseTree(commit.getTree().getId());
			treeParser=new CanonicalTreeParser();
			treeParser.reset(repository.newObjectReader(),revTree.getId());
			revWalk.dispose();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return treeParser;
	}
}