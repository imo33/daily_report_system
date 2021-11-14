package services;

import java.util.List;

import actions.views.CommentConverter;
import actions.views.CommentView;
import actions.views.EmployeeConverter;
import actions.views.EmployeeView;
import actions.views.ReportConverter;
import actions.views.ReportView;
import constants.JpaConst;
import models.Comment;
import models.Report;
import models.validators.CommentValidator;

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

    public long countAllMine(ReportView report) {

        long count = (long) em.createNamedQuery(JpaConst.Q_COM_COUNT_ALL_MINE, Long.class)
                .setParameter(JpaConst.JPQL_PARM_EMPLOYEE, ReportConverter.toModel(report))
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

    public CommentView findOne(int id) {
        return CommentConverter.toView(findOneInternal(id));
    }

    public CommentView findOneComment(int id) {
        return CommentConverter.toView(findOneInternalComment(id));
    }

    /**
     *
     * @param cv
     * @return
     */
    public List<String> create(CommentView cv) {
        List<String> errors = CommentValidator.validate(cv);
        if (errors.size() == 0) {

            createInternal(cv);

        }

        //バリデーションで発生したエラーを返却（エラーがなければ0件の空リスト）
        return errors;
    }

    public List<String> update(CommentView cv) {

        //バリデーションを行う
        List<String> errors = CommentValidator.validate(cv);

        if (errors.size() == 0) {

            updateInternal(cv);
        }

        //バリデーションで発生したエラーを返却（エラーがなければ0件の空リスト）
        return errors;
    }
    
 /**
  * 通常のを条件にデータを1件取得するメソッド
  * @param id
  * @return
  */
    private Comment findOneInternal(int id) {
        return em.find(Comment.class, id);
    }
/**
 * コメントを持ってくるメソッド
 * @param id
 * @return
 */
    private Comment findOneInternalComment(int id) {
        Report report = new Report();
        report.setId(id);
        List<Comment> comment = em.createNamedQuery(JpaConst.Q_COM_GET_REP, Comment.class)
                .setParameter(JpaConst.JPQL_PARM_REPORT, report)
                .getResultList();
        if (comment.isEmpty()) {
            return null;
        } else {
            return comment.get(0);
        }
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
