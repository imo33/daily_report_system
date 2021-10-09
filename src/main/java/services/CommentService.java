package services;

import java.util.List;

import actions.views.CommentConverter;
import actions.views.CommentView;
import actions.views.EmployeeConverter;
import actions.views.EmployeeView;
import constants.JpaConst;
import models.Comment;

public class CommentService extends ServiceBase {
    public List<CommentView> getMinePerPage(EmployeeView employee, int page) {

        List<Comment> comments = em.createNamedQuery(JpaConst.Q_COM_GET_ALL_MINE, Comment.class)
                .setParameter(JpaConst.JPQL_PARM_EMPLOYEE, EmployeeConverter.toModel(employee))
                .setFirstResult(JpaConst.ROW_PER_PAGE * (page - 1))
                .setMaxResults(JpaConst.ROW_PER_PAGE)
                .getResultList();
        return CommentConverter.toViewList(comments);
    }
    public long countAllMine(EmployeeView employee) {

        long count = (long) em.createNamedQuery(JpaConst.Q_COM_COUNT_ALL_MINE, Long.class)
                .setParameter(JpaConst.JPQL_PARM_EMPLOYEE, EmployeeConverter.toModel(employee))
                .getSingleResult();

        return count;
}
    public List<CommentView> getAllPerPage(int page) {

        List<Comment> comments = em.createNamedQuery(JpaConst.Q_COM_GET_ALL, Comment.class)
                .setFirstResult(JpaConst.ROW_PER_PAGE * (page - 1))
                .setMaxResults(JpaConst.ROW_PER_PAGE)
                .getResultList();
        return CommentConverter.toViewList(comments);
    }
    public long countAll() {
        long comments_count = (long) em.createNamedQuery(JpaConst.Q_COM_COUNT, Long.class)
                .getSingleResult();
        return comments_count;
    }

    private Comment findOneInternal(int id) {
        return em.find(Comment.class, id);
    }

    private void createInternal(CommentView cv) {

            em.getTransaction().begin();
            em.persist(CommentConverter.toModel(cv));
            em.getTransaction().commit();

}
    private void updateInternal(CommentView cv) {

        em.getTransaction().begin();
        Comment c = findOneInternal(cv.getId());
        CommentConverter.copyViewToModel(c, cv);
        em.getTransaction().commit();
    }
}
