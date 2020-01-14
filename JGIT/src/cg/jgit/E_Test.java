package cg.jgit;

import java.io.File;
import java.io.IOException;
import java.util.Iterator;

import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.lib.Repository;
import org.eclipse.jgit.lib.RepositoryBuilder;
import org.eclipse.jgit.revwalk.RevCommit;

public class E_Test {

	public static void main(String[] args) throws Exception {
		Repository repository = new RepositoryBuilder().setGitDir(new File("F:\\G_workspace\\qtrip365\\.git")).build();
		Git git = new Git(repository);
		//git.log().addPath(dir/filename.txt).setMaxCount(num).call();
		Iterable<RevCommit> iterable=git.log().call();
		
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

	}
}
