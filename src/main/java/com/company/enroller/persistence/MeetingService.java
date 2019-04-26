package com.company.enroller.persistence;

import java.util.Collection;

import com.company.enroller.model.Participant;
import org.hibernate.Query;
import org.hibernate.Transaction;
import org.springframework.stereotype.Component;

import com.company.enroller.model.Meeting;

@Component("meetingService")
public class MeetingService {

	DatabaseConnector connector;

	public MeetingService() {
		connector = DatabaseConnector.getInstance();
	}

	public Collection<Meeting> getAll() {
		String hql = "FROM Meeting";
		Query query = connector.getSession().createQuery(hql);
		return query.list();
	}

	public Meeting findById(String Id) {
		String hql = "FROM Meeting AS M WHERE M.id = " + Id;
		Query query = connector.getSession().createQuery(hql);
		if(query.list().isEmpty()) {
			return null;
		} else {
			return (Meeting) query.list().get(0);
		}
	}

	public void add(Meeting meeting){
		org.hibernate.Transaction transaction = connector.getSession().beginTransaction();
		connector.getSession().save(meeting);

		transaction.commit();
	}

	public void edit(Meeting meeting) {
		org.hibernate.Transaction transaction = connector.getSession().beginTransaction();
		connector.getSession().update(meeting);

		transaction.commit();
	}

}
