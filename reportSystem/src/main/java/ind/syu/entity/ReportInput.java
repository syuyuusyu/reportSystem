package ind.syu.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * ReportInput entity. @author MyEclipse Persistence Tools
 */
@Entity
@Table(name = "REPORT_INPUT", schema = "ALPHA")
public class ReportInput implements java.io.Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	// Fields
	@Id
	@Column(name = "ID", unique = true, nullable = false, precision = 22, scale = 0)
	private Integer id;
	
	@Column(name = "NAME", length = 20)
	private String name;
	
	@Column(name = "TYPE", length = 20)
	private String type;
	
	@Column(name = "LABLE", length = 20)
	private String lable;
	
	@Column(name = "MULTISELECT", length = 5)
	private String multiselect;
	
	@Column(name = "VALIDATEEXP", length = 100)
	private String validateexp;
	
	@Column(name = "VALIDATEMSG", length = 50)
	private String validatemsg;
	
	@Column(name = "ALLOWBLANK", length = 10)
	private String allowblank;
	
	@Column(name = "DEPENDCOLUMN", length = 50)
	private String dependcolumn;
	
	@Column(name = "DEPENDINPUT", length = 50)
	private String dependinput;
	
	@Column(name = "DISABLEMSG", length = 30)
	private String disablemsg;
	
	@Column(name = "DATASQL", length = 200)
	private String datasql;

	// Constructors

	/** default constructor */
	public ReportInput() {
	}

	/** minimal constructor */
	public ReportInput(Integer id) {
		this.id = id;
	}

	/** full constructor */
	public ReportInput(Integer id, String name, String type, String lable,
			String multiselect, String validateexp, String validatemsg,
			String allowblank, String dependcolumn, String dependinput,
			String disablemsg, String datasql) {
		this.id = id;
		this.name = name;
		this.type = type;
		this.lable = lable;
		this.multiselect = multiselect;
		this.validateexp = validateexp;
		this.validatemsg = validatemsg;
		this.allowblank = allowblank;
		this.dependcolumn = dependcolumn;
		this.dependinput = dependinput;
		this.disablemsg = disablemsg;
		this.datasql = datasql;
	}

	// Property accessors

	public Integer getId() {
		return this.id;
	}

	public void setId(Integer id) {
		this.id = id;
	}


	public String getName() {
		return this.name;
	}

	public void setName(String name) {
		this.name = name;
	}


	public String getType() {
		return this.type;
	}

	public void setType(String type) {
		this.type = type;
	}


	public String getLable() {
		return this.lable;
	}

	public void setLable(String lable) {
		this.lable = lable;
	}


	public String getMultiselect() {
		return this.multiselect;
	}

	public void setMultiselect(String multiselect) {
		this.multiselect = multiselect;
	}


	public String getValidateexp() {
		return this.validateexp;
	}

	public void setValidateexp(String validateexp) {
		this.validateexp = validateexp;
	}


	public String getValidatemsg() {
		return this.validatemsg;
	}

	public void setValidatemsg(String validatemsg) {
		this.validatemsg = validatemsg;
	}


	public String getAllowblank() {
		return this.allowblank;
	}

	public void setAllowblank(String allowblank) {
		this.allowblank = allowblank;
	}


	public String getDependcolumn() {
		return this.dependcolumn;
	}

	public void setDependcolumn(String dependcolumn) {
		this.dependcolumn = dependcolumn;
	}


	public String getDependinput() {
		return this.dependinput;
	}

	public void setDependinput(String dependinput) {
		this.dependinput = dependinput;
	}


	public String getDisablemsg() {
		return this.disablemsg;
	}

	public void setDisablemsg(String disablemsg) {
		this.disablemsg = disablemsg;
	}


	public String getDatasql() {
		return this.datasql;
	}

	public void setDatasql(String datasql) {
		this.datasql = datasql;
	}

}