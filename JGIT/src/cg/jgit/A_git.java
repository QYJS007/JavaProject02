package cg.jgit;

import java.io.File;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.List;

import org.eclipse.jgit.lib.PersonIdent;
import org.eclipse.jgit.lib.Ref;
import org.eclipse.jgit.lib.Repository;
import org.eclipse.jgit.lib.RepositoryBuilder;
import org.eclipse.jgit.revwalk.FooterLine;
import org.eclipse.jgit.revwalk.RevCommit;
import org.eclipse.jgit.revwalk.RevWalk;

public class A_git {
	public static void main(String[] args) throws IOException {
		/*
		 * 读取本地仓库的提交记录; F:\G_workspace\qtrip365\.git
		 */
		
		 DateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	       // Repository repository = new RepositoryBuilder().setGitDir(new File("F:\\workspaces\\qtrip1820\\qtrip365\\.git")).build();
	        Repository repository = new RepositoryBuilder().setGitDir(new File("F:\\G_workspace\\qtrip365\\.git")).build();
	        try (RevWalk walk = new RevWalk(repository)) {
	            Ref head = repository.findRef("HEAD");
	            //SymbolicRef[HEAD -> refs/heads/qtrip_1820=717c3aea7bdcc03a96a790968fac7a94cef17ac7]
	            walk.markStart(walk.parseCommit(head.getObjectId())); // 从HEAD开始遍历，AnyObjectId[717c3aea7bdcc03a96a790968fac7a94cef17ac7]
	            for (RevCommit commit : walk) {
	                /*RevTree tree = commit.getTree();

	                TreeWalk treeWalk = new TreeWalk(repository, repository.newObjectReader());
	                PathFilter f = PathFilter.create("pom.xml");
	                treeWalk.setFilter(f);
	                treeWalk.reset(tree);
	                treeWalk.setRecursive(false);*/
	                //while (treeWalk.next()) {
	                    PersonIdent authoIdent = commit.getAuthorIdent();
	                    System.out.println("提交人： " + authoIdent.getName() + " <" + authoIdent.getEmailAddress() + ">");
	                    System.out.println("提交SHA1： " + commit.getId().name());
	                    System.out.println("提交信息： " + commit.getShortMessage());
	                    System.out.println("提交的文件: "+ commit.getFullMessage());
	                     PersonIdent committerIdent = commit.getCommitterIdent();
	                     List<FooterLine> footerLines = commit.getFooterLines();
	                     for (FooterLine footerLine : footerLines) {
							System.out.println(footerLine.getValue());
						}
	                     
	                    System.out.println("提交的文件: ");
	                    System.out.println("提交时间： " + format.format(authoIdent.getWhen()));

	                   // ObjectId objectId = treeWalk.getObjectId(0);
	                   // ObjectLoader loader = repository.open(objectId);
	                   // loader.copyTo(System.out);              //提取blob对象的内容
	               // }
	            }
	        }
		/*Git git = null ;
		try {
			git = Git.open(new File("F:\\G_workspace\\qtrip365\\.git"));
			LogCommand log = git.log();
			Repository repository = log.getRepository();
			
			Set<ObjectId> additionalHaves = repository.getAdditionalHaves();
			
			//Status status = git.status().call(); 
			
			//Set<String> added = status.getModified();
			for (ObjectId string : additionalHaves) {
				System.out.println(string);
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		}*/
		
		/*Git git = null;
		try {
			git = Git.open(new File("F:\\G_workspace\\qtrip365\\.git\\.git"));
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		DateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		Iterable<RevCommit> results = null;
		try {
			results = git.log().setRevFilter(new RevFilter() {
			    @Override
			    public boolean include(RevWalk walker, RevCommit cmit)
			       throws StopWalkException, MissingObjectException, IncorrectObjectTypeException, IOException {
			        return cmit.getAuthorIdent().getName().equals("xxxxx dsd");
			    }

			    @Override
			    public RevFilter clone() {
			    return this;
			            }
			        }).call();
		} catch (NoHeadException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (GitAPIException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		results.forEach(commit -> {
		    PersonIdent authoIdent = commit.getAuthorIdent();
		    System.out.println("提交人：  " + authoIdent.getName() + "     <" + authoIdent.getEmailAddress() + ">");
		    System.out.println("提交SHA1：  " + commit.getId().name());
		    System.out.println("提交信息：  " + commit.getShortMessage());
		    System.out.println("提交时间：  " + format.format(authoIdent.getWhen()));
		});*/
	}

}
