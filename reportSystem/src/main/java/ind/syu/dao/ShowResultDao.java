package ind.syu.dao;

import java.util.List;

import ind.syu.entity.ShowResult;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface ShowResultDao extends JpaRepository<ShowResult, Integer>{
	@Query(value="select o from ShowResult  o where o.showResultId.id =?1 and o.showResultId.columnindex in ?2 order by columnIndex" )
	public List<ShowResult> findById(Integer id,List<Integer> columnIndex);//DATAINDEX
	
	@Query(value="select o from ShowResult  o where o.showResultId.id =?1 order by columnIndex" )
	public List<ShowResult> findById(Integer id);
	
	public List<ShowResult> findByDataindex(String dataindex);//DATAINDEX
}
