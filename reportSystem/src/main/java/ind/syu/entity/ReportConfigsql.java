package ind.syu.entity;

import java.sql.Clob;
import javax.persistence.AttributeOverride;
import javax.persistence.AttributeOverrides;
import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * ReportConfigsql entity. @author MyEclipse Persistence Tools
 */
@Entity
@Table(name = "REPORT_CONFIGSQL", schema = "ALPHA")
public class ReportConfigsql implements java.io.Serializable {

	// Fields

	/**
	 * 
	 */
	private static final long serialVersionUID = -7097341956251993391L;
	private ReportConfigsqlId id;
	private Integer showid;
	private Clob sql;
	private String bak;
	private String name;

	// Constructors

	/** default constructor */
	public ReportConfigsql() {
	}

	/** minimal constructor */
	public ReportConfigsql(ReportConfigsqlId id) {
		this.id = id;
	}

	/** full constructor */
	public ReportConfigsql(ReportConfigsqlId id, Integer showid, Clob sql,
			String bak, String name) {
		this.id = id;
		this.showid = showid;
		this.sql = sql;
		this.bak = bak;
		this.name = name;
	}

	// Property accessors
	@EmbeddedId
	@AttributeOverrides({
			@AttributeOverride(name = "reportname", column = @Column(name = "REPORTNAME", nullable = false, length = 40)),
			@AttributeOverride(name = "sqlname", column = @Column(name = "SQLNAME", nullable = false, length = 20)) })
	public ReportConfigsqlId getId() {
		return this.id;
	}

	public void setId(ReportConfigsqlId id) {
		this.id = id;
	}

	@Column(name = "SHOWID", precision = 4, scale = 0)
	public Integer getShowid() {
		return this.showid;
	}

	public void setShowid(Integer showid) {
		this.showid = showid;
	}

	@Column(name = "SQL")
	public Clob getSql() {
		return this.sql;
	}

	public void setSql(Clob sql) {
		this.sql = sql;
	}

	@Column(name = "BAK", length = 20)
	public String getBak() {
		return this.bak;
	}

	public void setBak(String bak) {
		this.bak = bak;
	}

	@Column(name = "NAME", length = 20)
	public String getName() {
		return this.name;
	}

	public void setName(String name) {
		this.name = name;
	}

}