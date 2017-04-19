package ind.syu.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;


/**
 * ReportAuthorityConfig entity. @author MyEclipse Persistence Tools
 */
@Entity
@Table(name="REPORT_AUTHORITY_CONFIG"
    ,schema="ALPHA"
)

public class ReportAuthorityConfig  implements java.io.Serializable {


    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	// Fields    
    @Id    
    @Column(name="ID", unique=true, nullable=false, precision=22, scale=0)
     private Integer id;
    
    @Column(name="KEY_WORD", length=30)
     private String keyWord;
     
    @Column(name="PARAM_WORD", length=30)
     private String paramWord;
    
    @Column(name="TYPE", length=1)
     private String type;
    
    @Column(name="SQL", length=500)
     private String sql;


    // Constructors

    /** default constructor */
    public ReportAuthorityConfig() {
    }

	/** minimal constructor */
    public ReportAuthorityConfig(Integer id) {
        this.id = id;
    }
    
    /** full constructor */
    public ReportAuthorityConfig(Integer id, String keyWord, String paramWord, String type, String sql) {
        this.id = id;
        this.keyWord = keyWord;
        this.paramWord = paramWord;
        this.type = type;
        this.sql = sql;
    }

   
    // Property accessors


    public Integer getId() {
        return this.id;
    }
    
    public void setId(Integer id) {
        this.id = id;
    }
    
    

    public String getKeyWord() {
        return this.keyWord;
    }
    
    public void setKeyWord(String keyWord) {
        this.keyWord = keyWord;
    }
    
    

    public String getParamWord() {
        return this.paramWord;
    }
    
    public void setParamWord(String paramWord) {
        this.paramWord = paramWord;
    }
    
   

    public String getType() {
        return this.type;
    }
    
    public void setType(String type) {
        this.type = type;
    }
    
    

    public String getSql() {
        return this.sql;
    }
    
    public void setSql(String sql) {
        this.sql = sql;
    }
   








}