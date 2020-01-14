package cg.jgit;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.Iterator;

import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.errors.JGitInternalException;
import org.eclipse.jgit.lib.ObjectId;
import org.eclipse.jgit.lib.ObjectLoader;
import org.eclipse.jgit.lib.Repository;
import org.eclipse.jgit.revwalk.RevCommit;
import org.eclipse.jgit.revwalk.RevTree;
import org.eclipse.jgit.revwalk.RevWalk;
import org.eclipse.jgit.treewalk.AbstractTreeIterator;
import org.eclipse.jgit.treewalk.CanonicalTreeParser;
import org.eclipse.jgit.treewalk.TreeWalk;

public class F_Test {

	public static void main(String [] args){
		try {
			//	            Git git=Git.cloneRepository()
			//	                    .setURI("git***")
			//	                    .setDirectory(new File("gitProject"))
			//	                    .call();

			//Git git=Git.open(new File("gitProject"));
			Git git=Git.open(new File("F:\\G_workspace\\qtrip365\\.git"));
			//	            DirCache index=git.add().addFilepattern("schemas/test.md").call();
			//	            RevCommit commit=git.commit().setMessage("addFile").call();
			//	            git.push().call();


			Iterable<RevCommit> iterable=git.log().call();
			//.addPath("schemas/test1.md").setMaxCount(2).call();
			Iterator<RevCommit> iter=iterable.iterator();
			while (iter.hasNext()){
				RevCommit commit=iter.next();
				String email=commit.getAuthorIdent().getEmailAddress();
				String name=commit.getAuthorIdent().getName();  //作者

				String commitEmail=commit.getCommitterIdent().getEmailAddress();//提交者
				String commitName=commit.getCommitterIdent().getName();

				int time=commit.getCommitTime();

				String fullMessage=commit.getFullMessage();
				String shortMessage=commit.getShortMessage();  //返回message的firstLine

				String commitID=commit.getName();  //这个应该就是提交的版本号

				System.out.println("authorEmail:"+email);
				System.out.println("authorName:"+name);
				System.out.println("commitEmail:"+commitEmail);
				System.out.println("commitName:"+commitName);
				System.out.println("time:"+time);
				System.out.println("fullMessage:"+fullMessage);
				System.out.println("shortMessage:"+shortMessage);
				System.out.println("commitID:"+commitID);
			}


			//	            Note note=git.notesShow().call();
			//	            System.out.println(note.getData().toString());


			// 接下来比如我们需要获得指定文件，最近两个版本之间的差异
			//	            Repository repository=git.getRepository();
			//	        List<RevCommit> list=new ArrayList<RevCommit>();
			//	        Iterable<RevCommit> iterable=git.log().addPath("schemas/test1.md").setMaxCount(2).call();
			//	        for(RevCommit revCommit:iterable){
			//	            list.add(revCommit);
			//	        }
			//	        if(list.size()==2){
			//	            AbstractTreeIterator newCommit=getAbstractTreeIterator(list.get(0),repository);
			//	            AbstractTreeIterator oldCommit=getAbstractTreeIterator(list.get(1),repository);
			//	            List<DiffEntry> diff=git.diff().setOldTree(oldCommit).setNewTree(newCommit).call();
			//	            ByteArrayOutputStream outputStream=new ByteArrayOutputStream();
			//	            DiffFormatter diffFormatter=new DiffFormatter(outputStream);
			//	            //设置比较器为忽略空白字符对比（Ignores all whitespace）
			//	            diffFormatter.setDiffComparator(RawTextComparator.WS_IGNORE_ALL);
			//	            diffFormatter.setRepository(repository); // 这里为什么还要设置它
			//	            System.out.println("-----------"+diff.size());
			//	            for(DiffEntry diffEntry:diff){
			//	                diffFormatter.format(diffEntry);
			//	                System.out.println("new Path:____"+diffEntry.getNewPath());
			//	                System.out.println("old path:____"+diffEntry.getOldPath());
			//	                System.out.println(outputStream.toString("UTF-8"));
			//	                outputStream.reset();
			//	            }
			//	        }



			ByteArrayOutputStream outputStream=read("3c9e4054b143af5e2bfed78efc30f518ea8abf1a",git);
			System.out.println(outputStream.toString("UTF-8"));
			git.close();
		} catch (Exception e) {
			e.printStackTrace();
		}

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



	public static ByteArrayOutputStream read(String revision, Git git) {
		ByteArrayOutputStream out = new ByteArrayOutputStream();
		Repository repository = null;
		try {
			//gitDir表示git库目录
			//  Git git = Git.open(new File("gitProject"));
			repository = git.getRepository();
			RevWalk walk = new RevWalk(repository);
			ObjectId objId = repository.resolve(revision);
			RevCommit revCommit = walk.parseCommit(objId);
			RevTree revTree = revCommit.getTree();

			//child表示相对git库的文件路径
			TreeWalk treeWalk = TreeWalk.forPath(repository, "schemas/smooth_platform.properties", revTree);
			ObjectId blobId = treeWalk.getObjectId(0);
			ObjectLoader loader = repository.open(blobId);
			loader.copyTo(out);
		} catch (IOException e) {
			e.printStackTrace();
		} catch (JGitInternalException e) {
			e.printStackTrace();
		} finally {
			if (repository != null)
				repository.close();
		}
		return out;
	}



}

