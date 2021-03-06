package models;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

import constants.JpaConst;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * コメントデータのDTOモデル
 *
 */
@Table(name = JpaConst.TABLE_COM)
@NamedQueries({
    @NamedQuery(
            name = JpaConst.Q_COM_GET_ALL,
            query = JpaConst.Q_COM_GET_ALL_DEF),
    @NamedQuery(
            name = JpaConst.Q_COM_COUNT,
            query = JpaConst.Q_COM_COUNT_DEF),
    @NamedQuery(
            name = JpaConst.Q_COM_GET_ALL_MINE,
            query = JpaConst.Q_COM_GET_ALL_MINE_DEF),
    @NamedQuery(
            name = JpaConst.Q_COM_COUNT_ALL_MINE,
            query = JpaConst.Q_COM_COUNT_ALL_MINE_DEF),
    @NamedQuery(
            name = JpaConst.Q_COM_GET_REP,
            query = JpaConst.Q_COM_GET_REP_DEF)
})

@Getter //全てのクラスフィールドについてgetterを自動生成する(Lombok)
@Setter //全てのクラスフィールドについてsetterを自動生成する(Lombok)
@NoArgsConstructor //引数なしコンストラクタを自動生成する(Lombok)
@AllArgsConstructor //全てのクラスフィールドを引数にもつ引数ありコンストラクタを自動生成する(Lombok)
@Entity
public class Comment {

    /**
     * id
     */
    @Id
    @Column(name = JpaConst.COM_COL_ID)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    /**
     * コメントを登録した従業員
     */
    @ManyToOne
    @JoinColumn(name = JpaConst.COM_COL_EMP, nullable = false)
    private Employee employee;

    /**
     * レポートID
     */
    @ManyToOne
    @JoinColumn(name = JpaConst.COM_COL_REP, nullable = false)
    private Report report;


    /**
     * コメントの内容
     */
    @Lob
    @Column(name = JpaConst.COM_COL_CONTENT, nullable = false)
    private String content;


}