package ind.syu.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * ReportTree entity. @author MyEclipse Persistence Tools
 */
@Entity
@Table(name = "REPORT_TREE", schema = "ALPHA")
public class ReportTree implements java.io.Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	// Fields
	@Id
	@Column(name = "ID", unique = true, nullable = false, precision = 22, scale = 0)
	private Integer id;
	
	@Column(name = "PARENTID", precision = 22, scale = 0)
	private Integer parentid;
	
	@Column(name = "TEXT", length = 20)
	private String text;
	
	@Column(name = "LEAF", length = 5)
	private String leaf;
	
	@Column(name = "INPUTS", length = 20)
	private String inputs;
	
	
	@Column(name = "REPORTNAME", length = 40)
	private String reportname;
	
	@Column(name = "SQLNAME", length = 20)
	private String sqlname;
	
	@Column(name = "COLUMNINDEX", length = 50)
	private String columnindex;

	// Constructors

	/** default constructor */
	public ReportTree() {
	}

	/** minimal constructor */
	public ReportTree(Integer id) {
		this.id = id;
	}

	/** full constructor */
	public ReportTree(Integer id, Integer parentid, String text,
			String leaf, String inputs, String reportname, String sqlname,
			String columnindex) {
		this.id = id;
		this.parentid = parentid;
		this.text = text;
		this.leaf = leaf;
		this.inputs = inputs;
		this.reportname = reportname;
		this.sqlname = sqlname;
		this.columnindex = columnindex;
	}

	// Property accessors

	public Integer getId() {
		return this.id;
	}

	public void setId(Integer id) {
		this.id = id;
	}


	public Integer getParentid() {
		return this.parentid;
	}

	public void setParentid(Integer parentid) {
		this.parentid = parentid;
	}


	public String getText() {
		return this.text;
	}

	public void setText(String text) {
		this.text = text;
	}


	public String getLeaf() {
		return this.leaf;
	}

	public void setLeaf(String leaf) {
		this.leaf = leaf;
	}


	public String getInputs() {
		return this.inputs;
	}

	public void setInputs(String inputs) {
		this.inputs = inputs;
	}


	public String getReportname() {
		return this.reportname;
	}

	public void setReportname(String reportname) {
		this.reportname = reportname;
	}

	
	public String getSqlname() {
		return this.sqlname;
	}

	public void setSqlname(String sqlname) {
		this.sqlname = sqlname;
	}

	
	public String getColumnindex() {
		return this.columnindex;
	}

	public void setColumnindex(String columnindex) {
		this.columnindex = columnindex;
	}

}