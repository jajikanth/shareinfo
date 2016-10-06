/**
 * 
 */
package com.jitworks.shareinfo.dao.jpa;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.persistence.TypedQuery;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;

import com.jitworks.shareinfo.dao.QuizDAO;
import com.jitworks.shareinfo.data.Quiz;
import com.jitworks.shareinfo.data.QuizFile;
import com.jitworks.shareinfo.data.QuizRecent;
import com.jitworks.shareinfo.data.QuizTopic;

/**
 * @author j.paidimarla
 * 
 */

@Repository
public class QuizDAOImpl implements QuizDAO {

	private static Logger logger = LoggerFactory.getLogger(QuizDAOImpl.class);

	@PersistenceContext
	private EntityManager em;

	public void cerateQuizTopic(QuizTopic quizToipc) {
		logger.debug("Create Quiz Topic.");

		em.persist(quizToipc);

	}

	public void updateQuizTopic(QuizTopic quizTopic, List<Quiz> newValidQuizs, QuizFile newQuizFile) {
		logger.debug("Update Quiz Topic.");

		em.merge(quizTopic);

		if (!newValidQuizs.isEmpty()) {
			for (Quiz quiz : newValidQuizs) {
				em.persist(quiz);
			}
		}

		if (newQuizFile != null) {
			em.persist(newQuizFile);
		}

		// delete empty quiz
		for (Quiz quiz : quizTopic.getQuizs()) {
			if (quiz.getQuestion().isEmpty()) {
				logger.debug("Delete Empty Quiz for Quiz ID :" + quiz.getId());
				Query query2 = em.createQuery("UPDATE Quiz q SET q.deleted = true WHERE q.id = :quizId");
				query2.setParameter("quizId", quiz.getId());
				query2.executeUpdate();
			}
		}

	}

	public List<QuizTopic> getQuizTopics() {

		logger.debug("get Quiz Topics");
		TypedQuery<QuizTopic> query;
		query = this.em.createQuery("SELECT q FROM QuizTopic q WHERE q.deleted = false ORDER BY q.id DESC", QuizTopic.class);

		List<QuizTopic> topics = null;
		try {
			topics = query.getResultList();
		} catch (NoResultException e) {

			logger.error("No Quiz Topics found. ");
		}

		return topics;
	}

	public List<Quiz> getQuizs(int quizTopicId) {
		TypedQuery<Quiz> query = em.createQuery("SELECT q FROM Quiz q WHERE q.deleted = false AND q.quizTopic.id = :quizTopicId ORDER BY q.id", Quiz.class);
		query.setParameter("quizTopicId", quizTopicId);
		return query.getResultList();
	}

	public QuizTopic getQuizTopic(int quizTopicId) {
		TypedQuery<QuizTopic> query;
		query = em.createQuery("SELECT q FROM QuizTopic q WHERE q.id = :quizTopicId AND q.deleted = false", QuizTopic.class);
		query.setParameter("quizTopicId", quizTopicId);

		QuizTopic quizTopic = null;
		try {
			quizTopic = query.getSingleResult();
		} catch (NoResultException e) {
			logger.error("no Quiz Topic with id :  " + quizTopicId);
		}

		return quizTopic;
	}

	public void deleteQuiz(int quizTopicId) {
		logger.debug("Delete QuizTopic ID :" + quizTopicId);
		Query query = em.createQuery("UPDATE QuizTopic q SET q.deleted = true WHERE q.id = :quizTopicId");
		query.setParameter("quizTopicId", quizTopicId);
		query.executeUpdate();
		logger.debug("Delete Quiz for QuizTopic ID :" + quizTopicId);
		Query query2 = em.createQuery("UPDATE Quiz q SET q.deleted = true WHERE q.quizTopic.id = :quizTopicId");
		query2.setParameter("quizTopicId", quizTopicId);
		query2.executeUpdate();
		Query query3 = em.createQuery("UPDATE QuizFile f SET f.deleted = true WHERE f.quizTopic.id = :quizTopicId");
		query3.setParameter("quizTopicId", quizTopicId);
		query3.executeUpdate();

	}

	public List<QuizRecent> getRecentQuizs() {
		TypedQuery<QuizRecent> query = em.createQuery("SELECT q FROM QuizRecent q WHERE q.deleted = false ORDER BY q.id DESC ", QuizRecent.class);
		return query.setMaxResults(10).getResultList();
	}

	public List<QuizFile> getQuizFiles(int quizTopicId) {
		TypedQuery<QuizFile> query = em.createQuery("SELECT q FROM QuizFile q WHERE q.deleted = false AND q.quizTopic.id = :quizTopicId ORDER BY q.id", QuizFile.class);
		query.setParameter("quizTopicId", quizTopicId);
		return query.getResultList();
	}

	/* (non-Javadoc)
	 * @see com.jitworks.shareinfo.dao.QuizDAO#getQuizFile(int)
	 */
	public QuizFile getQuizFile(int fileId) {
		// TODO Auto-generated method stub
		TypedQuery<QuizFile> query = em.createQuery("SELECT f FROM QuizFile f WHERE f.deleted = false AND f.id = :fileId ", QuizFile.class);
		query.setParameter("fileId", fileId);
		return query.getSingleResult();
	}

	/* (non-Javadoc)
	 * @see com.jitworks.shareinfo.dao.QuizDAO#deleteQuizFile(int)
	 */
	public void deleteQuizFile(int fileId) {
		// TODO Auto-generated method stub
		Query query = em.createQuery("UPDATE QuizFile f SET f.deleted = true WHERE f.id = :fileId");
		query.setParameter("fileId", fileId);
		query.executeUpdate();
		
	}

	
	
}
