package ind.syu.entity;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Embeddable;

@Embeddable
public class ShowResultId implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 951294813532423934L;

	@Column(name = "ID", precision = 22, scale = 0)
	private Integer id;
	
	@Column(name = "COLUMNINDEX", precision = 22, scale = 0)
	private Integer columnindex;

	public ShowResultId(Integer id, Integer columnindex) {
		super();
		this.id = id;
		this.columnindex = columnindex;
	}

	public ShowResultId() {
		super();
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Integer getColumnindex() {
		return columnindex;
	}

	public void setColumnindex(Integer columnindex) {
		this.columnindex = columnindex;
	}

	@Override
	public String toString() {
		return "ShowResultId [id=" + id + ", columnindex=" + columnindex + "]";
	}
	
	
	
	

}
