package ind.syu.controller;

import ind.syu.service.ReportService;
import ind.syu.service.ResultDto;
import ind.syu.service.ShowResultDaoImpl2;

import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.HashMap;


import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


import org.apache.commons.lang.StringUtils;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.commons.CommonsMultipartFile;

@Controller
@RequestMapping("/report")
public class ReportController {
	
	@Autowired
	ReportService reportService;
	
	@Autowired(required=true)
	JdbcTemplate jdbcTemplate;
	
	@Autowired
	ShowResultDaoImpl2 showResultDaoImpl;
	
	@RequestMapping(value ="/main.op")
	public String toWiew(HttpServletRequest request,HttpServletResponse response){
		@SuppressWarnings("unchecked")
		Map<String, Object[]> _param=request.getParameterMap();	
		request.getSession().setMaxInactiveInterval(60000);
		Map<String, String> param=conparam(_param);
		System.out.println("selectedId:"+param.get("selectedId"));
		request.removeAttribute("selectedId");
		System.out.println(request.getAttribute("selectedId"));
		request.setAttribute("selectedId", param.get("selectedId"));
		for (Iterator<Entry<String, String>> it=param.entrySet().iterator();it.hasNext();) {
			Entry<String, String> ss=it.next();
			request.getSession().setAttribute(ss.getKey(), ss.getValue());
		}
		return "report";
	}


	@RequestMapping(value ="/submit.op")
	@ResponseBody
	public ResultDto submit(HttpServletRequest request){
		@SuppressWarnings("unchecked")
		Map<String, Object[]> _param=request.getParameterMap();
		Map<String, String> param=new HashMap<String, String>();
		Set<Entry<String, Object[]>> keySet=_param.entrySet();
		for (Entry<String, Object[]> entry : keySet) {
			param.put(entry.getKey(), (String) entry.getValue()[0]);
		}
		String id=param.get("reportId");
		String columnIndex=param.get("columnIndex");
		String reportName=param.get("reportName");
		String sqlName=param.get("sqlName");		
		String queryLevel=param.get("queryLevel");
		if(StringUtils.isEmpty(queryLevel)){
			queryLevel="1";
		}
		ResultDto result=showResultDaoImpl.getResultDto(reportName,sqlName,columnIndex);
		//result.setData(showResultDaoImpl.getDate(param));
		result.setReportId(id);
		result.setReportName(reportName);
		result.setSqlName(sqlName);
		return result;
	}

	@RequestMapping(value ="/getData.op")
	@ResponseBody
	public Map<String, Object> getData(HttpServletRequest request){
		@SuppressWarnings("unchecked")
		Map<String, Object[]> _param=request.getParameterMap();
		Map<String, String> param=conparam(_param);
		String authorityParam=(String) request.getAttribute("authorityParam");
		
		if(!StringUtils.isEmpty(authorityParam)){
			for (String str : authorityParam.split(",")) {
				param.put(str, (String)request.getAttribute(str));
			}
		}

		return showResultDaoImpl.getData(param);
	}
	
	@RequestMapping(value ="/getInput.op")
	@ResponseBody
	public Map<String, Object> getInput(String sql){

		Map<String, Object> map=new HashMap<String, Object>();
		map.put("success", true);
		map.put("result", reportService.getInput(sql));
		return map;
	}

	@RequestMapping(value ="/loadTree.op")
	@ResponseBody
	public Map<String, Object> loadTree(String parentId){
		Map<String, Object> map=reportService.loadTree(parentId);
		return map;
	}

	@RequestMapping(value ="/excel.op")
	public void excel(HttpServletRequest request,HttpServletResponse response){
		@SuppressWarnings("unchecked")
		Map<String, Object[]> _param=request.getParameterMap();
		Map<String, String> param=conparam(_param);
		String fileName=param.get("fileName");
		String isIE=param.get("isIE");
		HSSFWorkbook wk=showResultDaoImpl.outputExcel(param);
	    OutputStream output=null;
		try {
			output = response.getOutputStream();
		} catch (IOException e) {
			e.printStackTrace();
		}  
	    response.reset();  
	    if("true".equals(isIE)){
	    	response.setHeader("Content-disposition", "attachment; filename="+encodingFileName(fileName)+".xls"); 
	    }else{
	    	try {
				response.setHeader("Content-disposition", "attachment; filename="+ new String( fileName.getBytes("utf-8"), "ISO8859-1" )+".xls");
			} catch (UnsupportedEncodingException e) {
				e.printStackTrace();
			}
	    }
		response.setContentType("application/msexcel");          
	    try {
			wk.write(output);
		} catch (IOException e) {
			e.printStackTrace();
		} finally{
			try {
				output.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		} 
	     
	}
	
    @RequestMapping("/fileUpload.op")
    @ResponseBody
    public Map<String, Object>  fileUpload(@RequestParam("file") CommonsMultipartFile file) throws IllegalStateException, IOException {
    	long  startTime=System.currentTimeMillis();
    	File newFile=new File("./"+startTime);
    	file.getOriginalFilename();
    	file.transferTo(newFile);
    	
    	newFile.delete();
    	Map<String, Object> map=new HashMap<>();
    	map.put("success", true);
    	map.put("fileName", file.getOriginalFilename());
    	return map;
    	
    }
	
	
	private Map<String, String> conparam(Map<String, Object[]> _param){
		Map<String, String> param=new HashMap<String, String>();
		Set<Entry<String, Object[]>> keySet=_param.entrySet();
		for (Entry<String, Object[]> entry : keySet) {
			Object[] objs=entry.getValue();
			String str="";
			if(objs.length>1){
				for(int i=0;i<objs.length;i++){
					if(objs.length-1==i){
						str+=(String) entry.getValue()[i];
					}else{
						str+=(String) entry.getValue()[i]+",";
					}
				}
				
			}else{
				str=(String) entry.getValue()[0];
			}
			param.put(entry.getKey(), str);
		}
		return param;
	}
	private  String encodingFileName(String fileName) {
        String returnFileName = "";
        try {
            returnFileName = URLEncoder.encode(fileName, "UTF-8");
            returnFileName = StringUtils.replace(returnFileName, "+", "%20");
            if (returnFileName.length() > 150) {
                returnFileName = new String(fileName.getBytes("GB2312"), "ISO8859-1");
                returnFileName = StringUtils.replace(returnFileName, " ", "%20");
            }
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            
        }
        return returnFileName;
    }
}
