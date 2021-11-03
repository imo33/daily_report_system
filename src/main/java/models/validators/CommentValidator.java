package models.validators;

import java.util.ArrayList;
import java.util.List;

import actions.views.CommentView;
import constants.MessageConst;

public class CommentValidator {
    public static List<String> validate(CommentView cv) {
        List<String> errors = new ArrayList<String>();

      //内容のチェック
        String contentError = validateContent(cv.getContent());
        if (!contentError.equals("")) {
            errors.add(contentError);
        }

        return errors;
    }
   private static String validateContent(String content) {
        if (content == null || content.equals("")) {
            return MessageConst.E_NOCONTENT.getMessage();
       }

        //入力値がある場合は空文字を返却
      return "";
   }
}
