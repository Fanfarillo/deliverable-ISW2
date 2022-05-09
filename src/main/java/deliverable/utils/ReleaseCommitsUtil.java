package deliverable.utils;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.eclipse.jgit.revwalk.RevCommit;

import deliverable.model.Release;
import deliverable.model.ReleaseCommits;

public class ReleaseCommitsUtil {
	
	//This private constructor is meant to hide the public one: utility classes do not have to be instantiated.
	private ReleaseCommitsUtil() {
		throw new IllegalStateException("This class does not have to be instantiated.");
	}
	
	private static RevCommit getLastCommit(List<RevCommit> commitsList) {
		
		RevCommit lastCommit = commitsList.get(0);
		for(RevCommit commit : commitsList) {
			//if commitDate > lastCommitDate then refresh lastCommit
			if(commit.getCommitterIdent().getWhen().after(lastCommit.getCommitterIdent().getWhen())) {
				lastCommit = commit;
				
			}
			
		}
		return lastCommit;
		
	}
	
	public static ReleaseCommits getCommitsOfRelease(List<RevCommit> commitsList, Release release, Date firstDate) {
		
		List<RevCommit> matchingCommits = new ArrayList<>();
		Date lastDate = release.getDate();
		
		for(RevCommit commit : commitsList) {
			Date commitDate = commit.getCommitterIdent().getWhen();
			
			//if firstDate < commitDate <= lastDate then add the commit in matchingCommits list
			if(commitDate.after(firstDate) && (commitDate.before(lastDate) || commitDate.equals(lastDate))) {
				matchingCommits.add(commit);
				
			}
			
		}
		RevCommit lastCommit = getLastCommit(matchingCommits);
		
		return new ReleaseCommits(release, matchingCommits, lastCommit);
		
	}

}
