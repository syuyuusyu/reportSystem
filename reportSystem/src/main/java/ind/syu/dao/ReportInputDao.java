package ind.syu.dao;

import java.util.List;

import ind.syu.entity.ReportInput;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface ReportInputDao extends JpaRepository<ReportInput, Integer>{
	
	@Query(value="select o from ReportInput  o where o.id.id in?1" )
	public List<ReportInput> getInputs(List<Integer> inputId);
	
	public List<ReportInput> somefind(String name);

}
