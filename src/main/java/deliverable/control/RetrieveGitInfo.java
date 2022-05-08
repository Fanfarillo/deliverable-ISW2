package deliverable.control;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.ListBranchCommand.ListMode;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.eclipse.jgit.errors.AmbiguousObjectException;
import org.eclipse.jgit.errors.IncorrectObjectTypeException;
import org.eclipse.jgit.errors.MissingObjectException;
import org.eclipse.jgit.errors.RevisionSyntaxException;
import org.eclipse.jgit.internal.storage.file.FileRepository;
import org.eclipse.jgit.lib.Ref;
import org.eclipse.jgit.lib.Repository;
import org.eclipse.jgit.revwalk.RevCommit;

import deliverable.model.Ticket;

public class RetrieveGitInfo {
	
	private Repository repo;
	private Git git;
	private List<Ticket> ticketsWithAV;
	
	public RetrieveGitInfo(String path, List<Ticket> ticketsList) throws IOException {
		this.repo = new FileRepository(path + "/.git");
		this.git = new Git(this.repo);
		
		//We are removing issues without AV (i.e. with FV=IV) because they are not influent on classes buggyness
		for(Ticket ticket : ticketsList) {
			if(ticket.getAv() == null || ticket.getAv().isEmpty()) {
				ticketsList.remove(ticket);
			}
		}
		this.ticketsWithAV = ticketsList;
		
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

}
