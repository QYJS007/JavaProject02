package cg.jgit;

import java.io.File;
import java.io.IOException;
import java.util.List;

import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.lib.ObjectId;
import org.eclipse.jgit.lib.Repository;
import org.eclipse.jgit.lib.RepositoryBuilder;
import org.eclipse.jgit.revwalk.FooterLine;
import org.eclipse.jgit.revwalk.RevCommit;
import org.eclipse.jgit.revwalk.RevWalk;
import org.eclipse.jgit.transport.FetchResult;

public class D_Test {

	public static void main(String[] args) throws Exception {
		/*Git git = Git.open(new File("F:\\G_workspace\\qtrip365\\.git"));
		ogs = git.log()
                .add(repository.resolve("remotes/origin/testbranch"))
                .call();
        count = 0;
        for (RevCommit rev : logs) {    
            System.out.println("Commit: " + rev  + ", name: " + rev.getName() + ", id: " + rev.getId().getName() );
            count++;
        }*/
		Repository repository = new RepositoryBuilder().setGitDir(new File("F:\\G_workspace\\qtrip365\\.git")).build();
		//Repository repository = git.getRepository() ; 
		RevWalk revWalk = new RevWalk( repository );
		ObjectId commitId = repository.resolve( "refs/heads/kx_cdssoverify" );  
		
		revWalk.markStart( revWalk.parseCommit( commitId ) ); 
		for( RevCommit commit : revWalk ) {  
			/*List<FooterLine> footerLines = commit.getFooterLines();
			for (FooterLine footerLine : footerLines) {2
				System.out.println(footerLine.getValue());
			}*/
			
			
			System.out.println( "文件内容:		"+commit.getFullMessage() ); 
			System.out.println( commit.getFirstByte());
			System.out.println(" name::: " + commit.name());//
		}
	}
}
