package deliverable.utils;

import java.util.ArrayList;
import java.util.List;

import deliverable.model.Release;
import deliverable.model.Ticket;

public class TicketUtil {
	
	//This private constructor is meant to hide the public one: utility classes do not have to be instantiated.
	private TicketUtil() {
		throw new IllegalStateException("This class does not have to be instantiated.");
	}
	
	/*Callers:
	 * retrieveConsistentIssues (RetrieveJiraInfo)*/
	public static boolean isConsistentTicket(Ticket ticket) {
		
		boolean isOVaffected = false;
		
		//If AVs are not available then return false
		if(ticket.getAv() == null || ticket.getAv().isEmpty()) {
			return false;
		}
		
		for(int i=0; i<ticket.getAv().size(); i++) {
			//If there is AV>=FV then return false
			if(ticket.getAv().get(i).getId() >= ticket.getFv().getId()) {
				return false;
			}
			
			if(ticket.getAv().get(i).getId() == ticket.getOv().getId()) {
				isOVaffected = true;
			}
			
		}
		
		//If there not exists AV such that AV=OV then return false
		return isOVaffected;
		
	}
	
	/*Callers:
	 * retrieveConsistentIssues (RetrieveJiraInfo)
	 * adjustTicketsList (RetrieveJiraInfo)*/
	public static Ticket adjustTicket(Ticket ticket, List<Release> releasesList) {
		
		List<Release> newAV = new ArrayList<>();
		
		ticket.setIv(ticket.getAv().get(0));	//Obviously the injected version is the first affected version
		
		//Affected versions have to be from IV (=the earliest AV) to FV-1
		for(int i=ticket.getIv().getId(); i<ticket.getFv().getId(); i++) {
			for(Release rel : releasesList) {
				if(rel.getId() == i) {
					newAV.add(new Release(i, rel.getName(), rel.getDate()));
					
				}
				
			}
			
		}		
		ticket.setAv(newAV);
		return ticket;
		
	}
	
	/*Callers:
	 * adjustTicketsList (RetrieveJiraInfo)*/
	public static Ticket setInitialAV(Ticket ticket, List<Release> releasesList, Double p) {
		
		List<Release> initialAV = new ArrayList<>();
		
		//initialAVid = IV = max(1; FV-(FV-OV)*P)
		int initialAVid = (int) (ticket.getFv().getId() - (ticket.getFv().getId() - ticket.getOv().getId()) * p);
		if(initialAVid < 1) {
			initialAVid = 1;
		}
		
		for(Release rel : releasesList) {
			if(rel.getId() == initialAVid) {
				initialAV.add(new Release(initialAVid, rel.getName(), rel.getDate()));
				
			}
		
		}		
		ticket.setAv(initialAV);
		return ticket;
		
	}

}
