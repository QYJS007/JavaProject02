package cg.jgit;

import java.io.File;
import java.util.List;

import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.lib.PersonIdent;
import org.eclipse.jgit.lib.Repository;
import org.eclipse.jgit.lib.RepositoryBuilder;
import org.eclipse.jgit.revwalk.FooterLine;
import org.eclipse.jgit.revwalk.RevCommit;

public class B_test {

	@SuppressWarnings("unused")
	public static void main(String[] args) throws Exception {
		//Repository repository = CookbookHelper.openJGitCookbookRepository()
		 Repository repository = new RepositoryBuilder().setGitDir(new File("F:\\G_workspace\\qtrip365\\.git")).build();
		try (Git git = new Git(repository)) {
            Iterable<RevCommit> commits = git.log().all().call();
            int count = 0;
            for (RevCommit commit : commits) {
              commit.getCommitTime();
              PersonIdent committerIdent = commit.getCommitterIdent();
              
              System.out.println( committerIdent.toExternalString());
              System.out.println( committerIdent.getTimeZone());
              
              String encodingName = commit.getEncodingName();
              System.out.println(encodingName);
              //commit.get
               // System.out.println("LogCommit: " + commit.getFooterLines());
               // List<FooterLine> footerLines = commit.getFooterLines();
                /*for (FooterLine footerLine : footerLines) {
                	
					System.out.println(footerLine.getKey()+":"+footerLine.getValue());
					if(footerLine.getValue()!=null){
						
						return;
					}
//				}*/
                count++;
            }
            System.out.println(count);
        }
	}
}
