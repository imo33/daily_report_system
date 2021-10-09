package actions.views;

import java.util.ArrayList;
import java.util.List;

import models.Comment;

public class CommentConverter {
    public static Comment toModel(CommentView cv) {
        return new Comment(
                cv.getId(),
                EmployeeConverter.toModel(cv.getEmployee()),
                cv.getContent());
    }
    public static CommentView toView(Comment c) {

        if (c == null) {
            return null;
        }

        return new CommentView(
                c.getId(),
                EmployeeConverter.toView(c.getEmployee()),
                c.getContent());
    }
    public static List<CommentView> toViewList(List<Comment> list) {
        List<CommentView> evs = new ArrayList<>();

        for (Comment c : list) {
            evs.add(toView(c));
        }

        return evs;
    }
    public static void copyViewToModel(Comment c, CommentView cv) {
        c.setId(cv.getId());
        c.setEmployee(EmployeeConverter.toModel(cv.getEmployee()));
        c.setContent(cv.getContent());

    }
    public static void copyModelToView(Comment c, CommentView cv) {
        cv.setId(c.getId());
        cv.setEmployee(EmployeeConverter.toView(c.getEmployee()));

}
}
