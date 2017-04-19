package ind.syu.entity;

import javax.persistence.Column;
import javax.persistence.Embeddable;

/**
 * ReportConfigsqlId entity. @author MyEclipse Persistence Tools
 */
@Embeddable
public class ReportConfigsqlId implements java.io.Serializable {

	// Fields

	/**
	 * 
	 */
	private static final long serialVersionUID = 4995357050879552352L;
	private String reportname;
	private String sqlname;

	// Constructors

	/** default constructor */
	public ReportConfigsqlId() {
	}

	/** full constructor */
	public ReportConfigsqlId(String reportname, String sqlname) {
		this.reportname = reportname;
		this.sqlname = sqlname;
	}

	// Property accessors

	@Column(name = "REPORTNAME", nullable = false, length = 40)
	public String getReportname() {
		return this.reportname;
	}

	public void setReportname(String reportname) {
		this.reportname = reportname;
	}

	@Column(name = "SQLNAME", nullable = false, length = 20)
	public String getSqlname() {
		return this.sqlname;
	}

	public void setSqlname(String sqlname) {
		this.sqlname = sqlname;
	}

	public boolean equals(Object other) {
		if ((this == other))
			return true;
		if ((other == null))
			return false;
		if (!(other instanceof ReportConfigsqlId))
			return false;
		ReportConfigsqlId castOther = (ReportConfigsqlId) other;

		return ((this.getReportname() == castOther.getReportname()) || (this
				.getReportname() != null && castOther.getReportname() != null && this
				.getReportname().equals(castOther.getReportname())))
				&& ((this.getSqlname() == castOther.getSqlname()) || (this
						.getSqlname() != null && castOther.getSqlname() != null && this
						.getSqlname().equals(castOther.getSqlname())));
	}

	public int hashCode() {
		int result = 17;

		result = 37
				* result
				+ (getReportname() == null ? 0 : this.getReportname()
						.hashCode());
		result = 37 * result
				+ (getSqlname() == null ? 0 : this.getSqlname().hashCode());
		return result;
	}

}