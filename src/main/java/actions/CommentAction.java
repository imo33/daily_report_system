package actions;

import java.io.IOException;
import java.util.List;

import javax.servlet.ServletException;

import actions.views.CommentView;
import actions.views.EmployeeView;
import actions.views.ReportView;
import constants.AttributeConst;
import constants.ForwardConst;
import constants.JpaConst;
import constants.MessageConst;
import services.CommentService;

public class CommentAction extends ActionBase {

    private CommentService service;

    @Override
    public void process() throws ServletException, IOException {

        service = new CommentService();

        invoke();
        service.close();

    }

    /**
     * 一覧画面表示
     * @throws ServletException
     * @throws IOException
     */
    public void index() throws ServletException, IOException {
      //指定されたページ数の一覧画面に表示するコメントデータを取得
        int page = getPage();
        List<CommentView> comments = service.getAllPerPage(page);

        //全日報データの件数を取得
        long commentsCount = service.countAll();

        putRequestScope(AttributeConst.COMMENTS, comments); //取得したコメントデータ
        putRequestScope(AttributeConst.COM_COUNT,commentsCount); //全てのコメントデータの件数
        putRequestScope(AttributeConst.PAGE, page); //ページ数
        putRequestScope(AttributeConst.MAX_ROW, JpaConst.ROW_PER_PAGE); //1ページに表示するレコードに数

        //セッションにフラッシュメッセージが設定されている場合はリクエストスコープに移し替え、セッションからは削除する
        String flush = getSessionScope(AttributeConst.FLUSH);
        if (flush != null) {
            putRequestScope(AttributeConst.FLUSH, flush);
            removeSessionScope(AttributeConst.FLUSH);
        }

        //一覧画面を表示
        forward(ForwardConst.FW_COM_INDEX);
    }


    /**
     * 新規登録画面表示
     * @throws ServletException
     * @throws IOException
     */
    public void entryNew() throws ServletException, IOException {

        putRequestScope(AttributeConst.TOKEN, getTokenId());
        forward(ForwardConst.FW_COM_NEW);
    }

    public void create() throws ServletException, IOException {
        //CSRF対策 tokenのチェック
        if (checkToken()) {

            //セッションからログイン中の従業員情報を取得
            EmployeeView ev = (EmployeeView) getSessionScope(AttributeConst.LOGIN_EMP);

            // レポートのIDを取得したい
            ReportView rv = (ReportView)getSessionScope(AttributeConst.REP_ID);


            //パラメータの値をもとにコメント情報のインスタンスを作成する
            CommentView cv = new CommentView(
                    null,
                    ev, //ログインしている従業員を、コメント作成者として登録する
                    rv,
                    getRequestParam(AttributeConst.COM_CONTENT));

            // コメントID取得
            CommentView comment =(CommentView)getSessionScope(AttributeConst.COM_VIEW);


            //コメント情報登録
            List<String> errors = service.create(cv);
             if (errors.size() > 0 ) {
                //登録中にエラーがあった場合

                putRequestScope(AttributeConst.TOKEN, getTokenId()); //CSRF対策用トークン
                putRequestScope(AttributeConst.COMMENT, cv); //入力されたコメント情報
                putRequestScope(AttributeConst.ERR, errors); //エラーのリスト

                //新規登録画面を再表示
                forward(ForwardConst.FW_COM_NEW);

            } else if (comment != null){
                forward(ForwardConst.FW_COM_NEW);

              //登録中にエラーがなかった場合
            }else {

                //セッションに登録完了のフラッシュメッセージを設定
                putSessionScope(AttributeConst.FLUSH, MessageConst.I_REGISTERED.getMessage());

                //詳細画面
                redirect(ForwardConst.ACT_REP, ForwardConst.CMD_SHOW, rv.getId());

            }
        }
    }

//    public void show() throws ServletException, IOException {
//
//        //idを条件にコメントデータを取得する
//        CommentView cv = service.findOne(toNumber(getRequestParam(AttributeConst.COM_ID)));
//
//        if (cv == null) {
//            //該当のコメントデータが存在しない場合はエラー画面を表示
//            forward(ForwardConst.FW_ERR_UNKNOWN);
//
//        } else {
//
//            putRequestScope(AttributeConst.COMMENT, cv); //取得したコメントデータ
//
//            //詳細画面を表示
//            forward(ForwardConst.FW_COM_SHOW);
//        }
//    }
    /**
     * 編集画面を表示する
     * @throws ServletException
     * @throws IOException
     */
    public void edit() throws ServletException, IOException {

        //idを条件にコメントデータを取得する
        CommentView cv = service.findOne(toNumber(getRequestParam(AttributeConst.COM_ID)));

        //セッションからログイン中の従業員情報を取得
        EmployeeView ev = (EmployeeView) getSessionScope(AttributeConst.LOGIN_EMP);

        if (cv == null || ev.getId() != cv.getEmployee().getId()) {
            //該当のコメントデータが存在しない、または
            //ログインしている従業員がコメントの作成者でない場合はエラー画面を表示
            forward(ForwardConst.FW_ERR_UNKNOWN);

        } else {

            putRequestScope(AttributeConst.TOKEN, getTokenId()); //CSRF対策用トークン
            putRequestScope(AttributeConst.COMMENT, cv); //取得したコメントデータ

            //編集画面を表示
            forward(ForwardConst.FW_COM_EDIT);
        }

    }
    /**
     * 更新を行う
     * @throws ServletException
     * @throws IOException
     */
    public void update() throws ServletException, IOException {

        //CSRF対策 tokenのチェック
        if (checkToken()) {

            //idを条件にコメントデータを取得する
            CommentView cv = service.findOne(toNumber(getRequestParam(AttributeConst.COM_ID)));

            //idを条件にレポートデータを取得
            ReportView rv = (ReportView)getSessionScope(AttributeConst.REP_ID);

            //入力されたコメント内容を設定する
            cv.setContent(getRequestParam(AttributeConst.COM_CONTENT));

            //コメントデータを更新する
            List<String> errors = service.update(cv);

            if (errors.size() > 0) {
                //更新中にエラーが発生した場合

                putRequestScope(AttributeConst.TOKEN, getTokenId()); //CSRF対策用トークン
                putRequestScope(AttributeConst.COMMENT, cv); //入力されたコメント情報
                putRequestScope(AttributeConst.ERR, errors); //エラーのリスト

                //編集画面を再表示
                forward(ForwardConst.FW_COM_EDIT);
            } else {
                //更新中にエラーがなかった場合

                //セッションに更新完了のフラッシュメッセージを設定
                putSessionScope(AttributeConst.FLUSH, MessageConst.I_UPDATED.getMessage());

                //一覧画面にリダイレクト
                redirect(ForwardConst.ACT_REP, ForwardConst.CMD_SHOW, rv.getId());


            }
        }
    }
}

