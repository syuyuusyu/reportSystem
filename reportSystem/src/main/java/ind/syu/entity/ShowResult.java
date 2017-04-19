package ind.syu.entity;


import java.sql.Clob;

import ind.syu.util.ClobUtil;


import javax.persistence.Column;

import javax.persistence.AttributeOverride;
import javax.persistence.AttributeOverrides;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * ShowResultId entity. @author MyEclipse Persistence Tools
 */
@Entity
@Table(name = "SHOW_RESULT", schema = "alpha")
public class ShowResult implements java.io.Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 6294025811797846217L;

	@EmbeddedId
	@AttributeOverrides({
			@AttributeOverride(name = "id", column = @Column(name = "ID", precision = 22, scale = 0)),
			@AttributeOverride(name = "columnindex", column = @Column(name = "COLUMNINDEX", precision = 22, scale = 0)) })
	private ShowResultId showResultId;
	
	public ShowResultId getshowResultId() {
		return showResultId;
	}

	public void setId(ShowResultId showResultId) {
		this.showResultId = showResultId;
	}

	@Column(name = "CAPTION", length = 20)
	private String caption;
	
	@Column(name = "HEADER", length = 20)
	private String header;
	
	@Column(name = "DATAINDEX", length = 20)
	private String dataindex;
	
	@Column(name = "WIDTH", precision = 22, scale = 0)
	private Integer width;
	
	@Column(name = "ISRENDER", length = 1)
	private String isrender;
	
	@Column(name = "RENDERER")
	private Clob renderer;
	
	@Column(name = "HIDDEN", length = 1)
	private String hidden;
	
	@Column(name = "DATAFIELDTYPE", length = 20)
	private String datafieldtype;
	
	@Column(name = "SERIESHEADER", length = 1)
	private String seriesheader;
	
	@Column(name = "SERIESDATAINDEX", length = 1)
	private String seriesdataindex;
	
	@Column(name = "CATEDATAINDEX", length = 100)
	private String catedataindex;
	
	@Column(name = "GROUPDATAINDEX", length = 20)
	private String groupdataindex;
	
	@Column(name = "GROUPHEADER", length = 20)
	private String groupheader;
	


	// Constructors

	/** default constructor */
	public ShowResult() {
	}

	/** full constructor */


	// Property accessors

	


	
	public String getCaption() {
		return this.caption;
	}

	public void setCaption(String caption) {
		this.caption = caption;
	}

	
	public String getHeader() {
		return this.header;
	}

	public void setHeader(String header) {
		this.header = header;
	}

	
	public String getDataindex() {
		return this.dataindex;
	}

	public void setDataindex(String dataindex) {
		this.dataindex = dataindex;
	}

	
	public Integer getWidth() {
		return this.width;
	}

	public void setWidth(Integer width) {
		this.width = width;
	}

	
	public String getIsrender() {
		return this.isrender;
	}

	public void setIsrender(String isrender) {
		this.isrender = isrender;
	}

	
	public Clob getRenderer() {
		return this.renderer;
	}

	public void setRenderer(Clob renderer) {
		this.renderer = renderer;
	}

	
	public String getHidden() {
		return this.hidden;
	}

	public void setHidden(String hidden) {
		this.hidden = hidden;
	}

	
	public String getDatafieldtype() {
		return this.datafieldtype;
	}

	public void setDatafieldtype(String datafieldtype) {
		this.datafieldtype = datafieldtype;
	}

	
	public String getSeriesheader() {
		return this.seriesheader;
	}

	public void setSeriesheader(String seriesheader) {
		this.seriesheader = seriesheader;
	}

	
	public String getSeriesdataindex() {
		return this.seriesdataindex;
	}

	public void setSeriesdataindex(String seriesdataindex) {
		this.seriesdataindex = seriesdataindex;
	}

	
	public String getCatedataindex() {
		return this.catedataindex;
	}

	public void setCatedataindex(String catedataindex) {
		this.catedataindex = catedataindex;
	}

	
	public String getGroupdataindex() {
		return this.groupdataindex;
	}

	public void setGroupdataindex(String groupdataindex) {
		this.groupdataindex = groupdataindex;
	}

	
	public String getGroupheader() {
		return this.groupheader;
	}

	public void setGroupheader(String groupheader) {
		this.groupheader = groupheader;
	}

	@Override
	public String toString() {
		return "ShowResult [id=" + showResultId + ", caption=" + caption + ", header="
				+ header + ", dataindex=" + dataindex + ", width=" + width
				+ ", isrender=" + isrender + ", renderer=" + ClobUtil.clobToString(renderer)
				+ ", hidden=" + hidden + ", datafieldtype=" + datafieldtype
				+ ", seriesheader=" + seriesheader + ", seriesdataindex="
				+ seriesdataindex + ", catedataindex=" + catedataindex
				+ ", groupdataindex=" + groupdataindex + ", groupheader="
				+ groupheader + "]";
	}


	


}