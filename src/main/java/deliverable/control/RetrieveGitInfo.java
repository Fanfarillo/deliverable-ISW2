package deliverable.control;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.ListBranchCommand.ListMode;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.eclipse.jgit.errors.AmbiguousObjectException;
import org.eclipse.jgit.errors.CorruptObjectException;
import org.eclipse.jgit.errors.IncorrectObjectTypeException;
import org.eclipse.jgit.errors.MissingObjectException;
import org.eclipse.jgit.errors.RevisionSyntaxException;
import org.eclipse.jgit.internal.storage.file.FileRepository;
import org.eclipse.jgit.lib.Ref;
import org.eclipse.jgit.lib.Repository;
import org.eclipse.jgit.revwalk.RevCommit;
import org.eclipse.jgit.revwalk.RevTree;
import org.eclipse.jgit.treewalk.TreeWalk;

import deliverable.model.Release;
import deliverable.model.ReleaseCommits;
import deliverable.model.Ticket;
import deliverable.utils.ReleaseCommitsUtil;

public class RetrieveGitInfo {
	
	private Repository repo;
	private Git git;
	private List<Ticket> ticketsWithAV;
	private List<Release> releases;
	
	public RetrieveGitInfo(String path, List<Ticket> ticketsList, List<Release> releasesList) throws IOException {
		this.repo = new FileRepository(path + "/.git");
		this.git = new Git(this.repo);
		this.releases = releasesList;
		
		//We are removing issues without AV (i.e. with FV=IV) because they are not influent on classes buggyness
		List<Ticket> ticketsWithAV = new ArrayList<>();
		for(Ticket ticket : ticketsList) {
			if(ticket.getAv() != null && !ticket.getAv().isEmpty()) {
				ticketsWithAV.add(ticket);
			}
		}
		this.ticketsWithAV = ticketsWithAV;
		
	}
	
	public List<RevCommit> retrieveAllCommits() throws GitAPIException, RevisionSyntaxException, MissingObjectException, IncorrectObjectTypeException, AmbiguousObjectException, IOException {
		
		List<RevCommit> allCommitsList = new ArrayList<>();		
		List<Ref> branchesList = this.git.branchList().setListMode(ListMode.ALL).call();
		
		//Branches loop
		for(Ref branch : branchesList) {
			Iterable<RevCommit> commitsList = this.git.log().add(this.repo.resolve(branch.getName())).call();
			
			for(RevCommit commit : commitsList) {
				if(!allCommitsList.contains(commit)) {
					allCommitsList.add(commit);				
				}
				
			}
			
		}
		return allCommitsList;
		
	}
	
	public List<ReleaseCommits> getRelCommAssociations(List<RevCommit> allCommitsList) throws ParseException {
		
		List<ReleaseCommits> relCommAssociations = new ArrayList<>();
		
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
		Date firstDate = formatter.parse("1900-00-01");	//firstDate is the date of the previous release; for the first release we take 01/01/1900 as lower bound
		
		for(Release rel : this.releases) {
			relCommAssociations.add(ReleaseCommitsUtil.getCommitsOfRelease(allCommitsList, rel, firstDate));
			firstDate = rel.getDate();
				
		}
		return relCommAssociations;
		
	}
	
	private List<String> getClasses(RevCommit commit) throws MissingObjectException, IncorrectObjectTypeException, CorruptObjectException, IOException {
		
		List<String> javaClasses = new ArrayList<>();
		
		RevTree tree = commit.getTree();	//We get the tree of the files and the directories that were belonging to the repository when commit was pushed
		TreeWalk treeWalk = new TreeWalk(this.repo);	//We use a TreeWalk to iterate over all files in the Tree recursively
		treeWalk.addTree(tree);
		treeWalk.setRecursive(true);
		
		while(treeWalk.next()) {
			//We are keeping only Java classes that are not involved in tests
			if(treeWalk.getPathString().contains(".java") && !treeWalk.getPathString().contains("/test/")) {
				javaClasses.add(treeWalk.getPathString());
			}
		}		
		treeWalk.close();
		
		return javaClasses;
		
	}
	
	public List<ReleaseCommits> getRelClassesAssociations(List<ReleaseCommits> relCommAssociations) throws MissingObjectException, IncorrectObjectTypeException, CorruptObjectException, IOException {
		
		for(ReleaseCommits relComm : relCommAssociations) {
			List<String> javaClasses = getClasses(relComm.getLastCommit());
			relComm.setJavaClasses(javaClasses);
			
		}
		return relCommAssociations;
		
	}

}
