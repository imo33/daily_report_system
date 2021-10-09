package actions;

import java.io.IOException;

import javax.servlet.ServletException;

import constants.ForwardConst;
import services.CommentService;

public class CommentAction extends ActionBase {

    private CommentService service;

    @Override
    public void process() throws ServletException, IOException {

        service = new CommentService();

        invoke();
        service.close();

    }
// 一覧画面表示
    public void index() throws ServletException, IOException {
        forward(ForwardConst.FW_COM_INDEX);
    }

public void entryNew() throws ServletException, IOException {
    forward(ForwardConst.FW_COM_NEW);
}
}