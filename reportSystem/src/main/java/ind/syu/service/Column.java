package ind.syu.service;

public class Column {
	private String header ;
	private String dataIndex; 
	private int width;
	private String  renderer ; 
	private boolean hidden;
	
	public Column(String dataIndex, String header, int width) {
		super();
		this.dataIndex = dataIndex;
		this.header = header;
		this.width = width;
	}
	
	
	
	public Column(String dataIndex, String header,  int width,String render) {
		super();
		this.dataIndex = dataIndex;
		this.header = header;
		this.renderer = render;
		this.width = width;
	}



	public Column() {
		super();
		// TODO Auto-generated constructor stub
	}
	public String getHeader() {
		return header;
	}
	public void setHeader(String header) {
		this.header = header;
	}
	public String getDataIndex() {
		return dataIndex;
	}
	public void setDataIndex(String dataIndex) {
		this.dataIndex = dataIndex;
	}
	public int getWidth() {
		return width;
	}
	public void setWidth(int width) {
		this.width = width;
	}


	public String getRenderer() {
		return renderer;
	}


	public void setRenderer(String renderer) {
		this.renderer = renderer;
	}



	public boolean isHidden() {
		return hidden;
	}



	public void setHidden(boolean hidden) {
		this.hidden = hidden;
	}
	
	
	
}
