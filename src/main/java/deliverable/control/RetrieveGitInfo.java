package deliverable.control;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.ListBranchCommand.ListMode;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.eclipse.jgit.diff.DiffEntry;
import org.eclipse.jgit.diff.DiffFormatter;
import org.eclipse.jgit.diff.Edit;
import org.eclipse.jgit.diff.RawTextComparator;
import org.eclipse.jgit.diff.DiffEntry.ChangeType;
import org.eclipse.jgit.errors.CorruptObjectException;
import org.eclipse.jgit.errors.MissingObjectException;
import org.eclipse.jgit.errors.RevisionSyntaxException;
import org.eclipse.jgit.internal.storage.file.FileRepository;
import org.eclipse.jgit.lib.ObjectId;
import org.eclipse.jgit.lib.ObjectReader;
import org.eclipse.jgit.lib.Ref;
import org.eclipse.jgit.lib.Repository;
import org.eclipse.jgit.revwalk.RevCommit;
import org.eclipse.jgit.revwalk.RevTree;
import org.eclipse.jgit.treewalk.CanonicalTreeParser;
import org.eclipse.jgit.treewalk.TreeWalk;
import org.eclipse.jgit.util.io.DisabledOutputStream;

import deliverable.model.JavaClass;
import deliverable.model.Release;
import deliverable.model.ReleaseCommits;
import deliverable.model.Ticket;
import deliverable.utils.JavaClassUtil;
import deliverable.utils.ReleaseCommitsUtil;
import deliverable.utils.ReleaseUtil;

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
		List<Ticket> ticketsAV = new ArrayList<>();
		for(Ticket ticket : ticketsList) {
			if(ticket.getAv() != null && !ticket.getAv().isEmpty()) {
				ticketsAV.add(ticket);
			}
		}
		this.ticketsWithAV = ticketsAV;
		
	}
	
	public List<RevCommit> retrieveAllCommits() throws GitAPIException, RevisionSyntaxException, IOException {
		
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
	
	private Map<String, String> getClasses(RevCommit commit) throws IOException {
		
		Map<String, String> javaClasses = new HashMap<>();
		
		RevTree tree = commit.getTree();	//We get the tree of the files and the directories that were belonging to the repository when commit was pushed
		TreeWalk treeWalk = new TreeWalk(this.repo);	//We use a TreeWalk to iterate over all files in the Tree recursively
		treeWalk.addTree(tree);
		treeWalk.setRecursive(true);
		
		while(treeWalk.next()) {
			//We are keeping only Java classes that are not involved in tests
			if(treeWalk.getPathString().contains(".java") && !treeWalk.getPathString().contains("/test/")) {
				//We are retrieving (name class, content class) couples
				javaClasses.put(treeWalk.getPathString(), new String(this.repo.open(treeWalk.getObjectId(0)).getBytes(), StandardCharsets.UTF_8));
			}
		}		
		treeWalk.close();
		
		return javaClasses;
		
	}
	
	public void getRelClassesAssociations(List<ReleaseCommits> relCommAssociations) throws IOException {
		
		for(ReleaseCommits relComm : relCommAssociations) {
			Map<String, String> javaClasses = getClasses(relComm.getLastCommit());
			relComm.setJavaClasses(javaClasses);
			
		}
		
	}
	
	private List<RevCommit> getTicketCommits(Ticket ticket) throws GitAPIException, IOException {
		
		//Here there will be the commits related to the tickets involving the affected versions of ticket
		//Commits have a ticket ID included in their comments (full messages)
		List<RevCommit> associatedCommits = new ArrayList<>();
		List<Ref> branchesList = this.git.branchList().setListMode(ListMode.ALL).call();

		//Branches loop
		for(Ref branch : branchesList) {
			Iterable<RevCommit> commitsList = this.git.log().add(repo.resolve(branch.getName())).call();

			//Commits loop within a specific branch
			for(RevCommit commit : commitsList) {
				String comment = commit.getFullMessage();	
				
				//We are keeping only commits related to Jira tickets previously found
				if(comment.contains(ticket.getKey()) && !associatedCommits.contains(commit)) {	
					associatedCommits.add(commit);
				}				
							
			}
		
		}
		return associatedCommits;		
		
	}
	
	private List<String> getModifiedClasses(RevCommit commit) throws IOException {
		
		List<String> modifiedClasses = new ArrayList<>();	//Here there will be the names of the classes that have been modified by the commit
		
		try(DiffFormatter diffFormatter = new DiffFormatter(DisabledOutputStream.INSTANCE);
			ObjectReader reader = this.repo.newObjectReader()) {			
						
			CanonicalTreeParser newTreeIter = new CanonicalTreeParser();
			ObjectId newTree = commit.getTree();
			newTreeIter.reset(reader, newTree);
		
			RevCommit commitParent = commit.getParent(0);	//It's the previous commit of the commit we are considering
			CanonicalTreeParser oldTreeIter = new CanonicalTreeParser();
			ObjectId oldTree = commitParent.getTree();
			oldTreeIter.reset(reader, oldTree);
	
			diffFormatter.setRepository(this.repo);
			List<DiffEntry> entries = diffFormatter.scan(oldTreeIter, newTreeIter);
		
			//Every entry contains info for each file involved in the commit (old path name, new path name, change type (that could be MODIFY, ADD, RENAME, etc.))
			for(DiffEntry entry : entries) {
				//We are keeping only Java classes that are not involved in tests
				if(entry.getChangeType().equals(ChangeType.MODIFY) && entry.getNewPath().contains(".java") && !entry.getNewPath().contains("/test/")) {
					modifiedClasses.add(entry.getNewPath());
				}
			
			}
		
		} catch(ArrayIndexOutOfBoundsException e) {
			//commit has no parents: skip this commit, return an empty list and go on
			
		}
		
		return modifiedClasses;
		
	}
	
	private void doLabeling(List<JavaClass> javaClasses, Ticket ticket, List<ReleaseCommits> relCommAssociations) throws GitAPIException, IOException {
		
		List<RevCommit> commitsAssociatedWIssue = getTicketCommits(ticket);
		
		for(RevCommit commit : commitsAssociatedWIssue) {
			Release associatedRelease = ReleaseCommitsUtil.getReleaseOfCommit(commit, relCommAssociations);
			List<String> modifiedClasses = getModifiedClasses(commit);
			
			for(String modifClass : modifiedClasses) {
				JavaClassUtil.updateJavaClassBuggyness(javaClasses, modifClass, ticket.getIv(), associatedRelease);
				
			}
			
		}
		
	}
	
	public List<JavaClass> labelClasses(List<ReleaseCommits> relCommAssociations) throws GitAPIException, IOException {
		
		List<JavaClass> javaClasses = JavaClassUtil.buildAllJavaClasses(relCommAssociations);
		
		for(Ticket ticket : this.ticketsWithAV) {
			doLabeling(javaClasses, ticket, relCommAssociations);
			
		}
		return javaClasses;
		
	}
	
	public void assignCommitsToClasses(List<JavaClass> javaClasses, List<RevCommit> commits, List<ReleaseCommits> relCommAssociations) throws IOException {	
		
		for(RevCommit commit : commits) {
			Release associatedRelease = ReleaseCommitsUtil.getReleaseOfCommit(commit, relCommAssociations);
			
			if(associatedRelease != null) {		//There are also commits with no associatedRelease because their date is latter than last release date			
				List<String> modifiedClasses = getModifiedClasses(commit);
			
				for(String modifClass : modifiedClasses) {
					JavaClassUtil.updateJavaClassCommits(javaClasses, modifClass, associatedRelease, commit);
				
				}
			
			}	
				
		}
		
	}
	
	public List<JavaClass> getCurrentClasses(List<RevCommit> allCommits) throws IOException {
		//allCommits is a useful parameter: it allows to get the commits AFTER the last release without doing new computations with Jgit
		
		/*We need to call getCommitsOfRelease method of ReleaseCommitsUtil class in order to get:
		 * The commits after the last release
		 * Current commit
		 * To do this, we need to retrieve the date of the last release and to create a new Release instance (that will be related to the next release) */
		
		Release lastRelease = ReleaseUtil.getLastRelease(this.releases);
		Date lastReleaseDate = lastRelease.getDate();
		Release futureRelease = new Release(lastRelease.getId()+1, null, Calendar.getInstance().getTime());		//Last param is TODAY
		
		ReleaseCommits currentRelComm = ReleaseCommitsUtil.getCommitsOfRelease(allCommits, futureRelease, lastReleaseDate);
		Map<String, String> currentJavaClasses = getClasses(currentRelComm.getLastCommit());
		currentRelComm.setJavaClasses(currentJavaClasses);
		//Now currentRelComm has all the attributes setted
		//(future release, commits associated to future release, very last commit and current Java classes, that will be associated to future release too)
		
		List<ReleaseCommits> currentRelCommList = new ArrayList<>();	//We need a list just for compatibility with method buildAllJavaClasses reason
		currentRelCommList.add(currentRelComm);
		List<JavaClass> javaClassInstances = JavaClassUtil.buildAllJavaClasses(currentRelCommList);
		
		for(RevCommit commit : currentRelComm.getCommits()) {
			List<String> modifiedClasses = getModifiedClasses(commit);
			
			for(String modifClass : modifiedClasses) {
				JavaClassUtil.updateJavaClassCommits(javaClassInstances, modifClass, futureRelease, commit);
			
			}
			
		}
		return javaClassInstances;
	
	}
	
	private int getAddedLines(DiffFormatter diffFormatter, DiffEntry entry) throws IOException {
		
		int addedLines = 0;
		for(Edit edit : diffFormatter.toFileHeader(entry).toEditList()) {
			addedLines += edit.getEndA() - edit.getBeginA();
			
		}
		return addedLines;
		
	}
	
	private int getDeletedLines(DiffFormatter diffFormatter, DiffEntry entry) throws IOException {
		
		int deletedLines = 0;
		for(Edit edit : diffFormatter.toFileHeader(entry).toEditList()) {
			deletedLines += edit.getEndB() - edit.getBeginB();
			
		}
		return deletedLines;
		
	}
	
	public void computeAddedAndDeletedLinesList(JavaClass javaClass) throws IOException {
		
		List<Integer> addedLinesList = new ArrayList<>();	
		List<Integer> deletedLinesList = new ArrayList<>();
		
		for(RevCommit comm : javaClass.getCommits()) {		
			try(DiffFormatter diffFormatter = new DiffFormatter(DisabledOutputStream.INSTANCE)) {
				
				RevCommit parentComm = comm.getParent(0);
				
				diffFormatter.setRepository(this.repo);
				diffFormatter.setDiffComparator(RawTextComparator.DEFAULT);
				
				List<DiffEntry> diffs = diffFormatter.scan(parentComm.getTree(), comm.getTree());
				for(DiffEntry entry : diffs) {
					if(entry.getNewPath().equals(javaClass.getName())) {
						getAddedLines(diffFormatter, entry);
						getDeletedLines(diffFormatter, entry);
						//ADD THESE VALUES IN LISTS BUT BE CAREFUL IN HOW
						
					}
					
				}
			
			} catch(ArrayIndexOutOfBoundsException e) {
			//commit has no parents: skip this commit, return an empty list and go on
			
			}
			
		}
		
	}

}
