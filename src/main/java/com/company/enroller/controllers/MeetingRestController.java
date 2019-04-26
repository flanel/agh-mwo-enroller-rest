package com.company.enroller.controllers;

import com.company.enroller.model.Participant;
import com.company.enroller.persistence.ParticipantService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import java.util.Collection;

import com.company.enroller.model.Meeting;
import com.company.enroller.persistence.MeetingService;

import static org.apache.logging.log4j.util.LambdaUtil.getAll;


@RestController
@RequestMapping("/meetings")
public class MeetingRestController {
	@Autowired
	ParticipantService participantService;

	@Autowired
	MeetingService meetingService;

	@RequestMapping(value = "", method = RequestMethod.GET)
	public ResponseEntity<?> getMeetings() {
		Collection<Meeting> meetings = meetingService.getAll();
		return new ResponseEntity<Collection<Meeting>>(meetings, HttpStatus.OK);
	}



	@RequestMapping(value = "/{id}", method = RequestMethod.GET)
	public ResponseEntity<?> getMeetingId(@PathVariable("id") String id) {
		Meeting meeting = meetingService.findById(id);
		if (meeting == null) {
			return new ResponseEntity(HttpStatus.NOT_FOUND);
		}
		return new ResponseEntity<Meeting>(meeting, HttpStatus.OK);
	}

	@RequestMapping(value = "", method = RequestMethod.POST)
	public ResponseEntity<?> registerMeeting(@RequestBody Meeting meeting) {

		Meeting tmpMeeting = meetingService.findById(Long.toString(meeting.getId()));

		if (tmpMeeting !=  null) {
			return new ResponseEntity("Unable to create. A meeting with id " + meeting.getId() + " already exist.", HttpStatus.CONFLICT);
		} else {
			meetingService.add(meeting);
			return new ResponseEntity<Meeting>(meeting, HttpStatus.OK);
		}
	}

     @RequestMapping(value = "/{id}/participants/{participantLogin}", method = RequestMethod.POST)
     public ResponseEntity<?> addParticipantToMeeting(@PathVariable("id") int id,
													  @PathVariable("participantLogin") String participantLogin) {

		 Meeting tmpMeeting = meetingService.findById(Integer.toString(id));
		 Participant tmpParticipant = participantService.findByLogin(participantLogin);
		 if (tmpMeeting !=  null && tmpParticipant != null) {
			 tmpMeeting.addParticipant(tmpParticipant);
			 meetingService.edit(tmpMeeting);
			 return new ResponseEntity<Participant>(tmpParticipant, HttpStatus.OK);
		 } else if ( tmpMeeting == null ) {
			 return new ResponseEntity("Unable to add participant. A meeting with id " + id + " doesn't exist.", HttpStatus.CONFLICT);
		 } else if ( tmpParticipant == null ) {
			 return new ResponseEntity("Unable to add participant. A participant " + participantLogin + " doesn't exist.", HttpStatus.CONFLICT);
		 }
		 return new ResponseEntity<Participant>(tmpParticipant, HttpStatus.OK);
     }



//
//
//	@RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
//	public ResponseEntity<?> deleteParticipant(@PathVariable("id") String login) {
//
//		Participant participant = participantService.findByLogin(login);
//
//		if (participant  ==  null) {
//			return new ResponseEntity("Unable to delete. A participant with login " + participant.getLogin() + " already removed.", HttpStatus.CONFLICT);
//		} else {
//			participantService.delete(participant);
//			return new ResponseEntity<Participant>(participant, HttpStatus.OK);
//		}
//	}
}