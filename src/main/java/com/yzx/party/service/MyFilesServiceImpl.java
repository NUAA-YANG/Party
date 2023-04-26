package com.yzx.party.service;

import com.yzx.party.dao.MyFileRepository;
import com.yzx.party.pojo.MyFiles;
import com.yzx.party.vo.QueryFile;
import com.yzx.party.vo.QueryNew;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;

/**
 * @author yzx
 * @version JDK 1.8.0_131
 * @since 2022/1/27 11:01
 */
@Service
public class MyFilesServiceImpl implements MyFilesService {

    @Autowired
    MyFileRepository myFileRepository;

    @Transactional
    @Override
    public Page<MyFiles> listFiles(Pageable pageable, QueryFile queryFile) {
        return myFileRepository.findAll(new Specification<MyFiles>() {

            List<Predicate> predicates = new ArrayList<>();
            @Override
            public Predicate toPredicate(Root<MyFiles> root, CriteriaQuery<?> cq, CriteriaBuilder cb) {
                //标题的模糊查询
                if (!"".equals(queryFile.getTitle()) && queryFile.getTitle()!=null ){
                    predicates.add(cb.like(root.<String>get("title"),"%"+queryFile.getTitle()+"%"));
                }
                //姓名的模糊查询
                if (!"".equals(queryFile.getPerson_name()) && queryFile.getPerson_name()!=null ){
                    predicates.add(cb.like(root.<String>get("person_name"),"%"+queryFile.getPerson_name()+"%"));
                }
                //学院的模糊查询
                if (queryFile.getCollege_id()!=null){
                    predicates.add(cb.equal(root.<Integer>get("college_id"),queryFile.getCollege_id()));
                }
                //专业的模糊查询
                if (queryFile.getMajor_id()!=null){
                    predicates.add(cb.equal(root.<Integer>get("major_id"),queryFile.getMajor_id()));
                }
                cq.where(predicates.toArray(new Predicate[predicates.size()]));
                return null;
            }
        },pageable);
    }

    @Transactional
    @Override
    public MyFiles saveFiles(MyFiles files) {
        return myFileRepository.save(files);
    }

    @Override
    public MyFiles getFileById(Integer id) {
        return myFileRepository.findById(id).get();
    }

    @Override
    public MyFiles getFileByTitle(String title) {
        return myFileRepository.findByTitle(title);
    }

    @Transactional
    @Override
    public void deleteFiles(Integer id) {
        myFileRepository.deleteById(id);
    }
}
