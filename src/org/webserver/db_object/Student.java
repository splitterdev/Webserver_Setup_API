package org.webserver.db_object;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import org.webserver.db.DataEntity;

@Entity
@Table(name = "student")
public class Student implements DataEntity {

	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	private Long id;

	@Column(name="studentName", length=50, nullable=true, unique=false)
	private String studentName;

	@Column(name="studentCardId", nullable=true, unique=false)
	private Long studentCardId;

	@Column(name="studentJoinDate", nullable=true, unique=false)
	private Date studentJoinDate;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getStudentName() {
		return studentName;
	}

	public void setStudentName(String studentName) {
		this.studentName = studentName;
	}

	public Long getStudentCardId() {
		return studentCardId;
	}

	public void setStudentCardId(Long studentCardId) {
		this.studentCardId = studentCardId;
	}

	public Date getStudentJoinDate() {
		return studentJoinDate;
	}

	public void setStudentJoinDate(Date studentJoinDate) {
		this.studentJoinDate = studentJoinDate;
	}
}
