package com.yzx.party.service;

import com.yzx.party.dao.TaskFileRepository;
import com.yzx.party.myException.NotFoundException;
import com.yzx.party.pojo.TaskFile;
import com.yzx.party.pojo.Tasks;
import com.yzx.party.vo.QueryTask;
import org.springframework.beans.BeanUtils;
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
 * @since 2022/3/11 10:16
 */
@Service
public class TaskFileServiceImpl implements TaskFileService {

    @Autowired
    TaskFileRepository taskFileRepository;

    @Transactional
    @Override
    public Page<TaskFile> listTaskFiles(Pageable pageable, QueryTask queryTask) {
        return taskFileRepository.findAll(new Specification<TaskFile>() {
            @Override
            public Predicate toPredicate(Root<TaskFile> root, CriteriaQuery<?> cq, CriteriaBuilder cb) {
                List<Predicate> predicates = new ArrayList<>();
                //学院模糊查询
                if (queryTask.getCollege_id()!=null){
                    predicates.add(cb.equal(root.<Integer>get("college_id"),queryTask.getCollege_id()));
                }
                //任务名称模糊查询
                if (!"".equals(queryTask.getTitle()) && queryTask.getTitle()!=null ){
                    predicates.add(cb.like(root.<String>get("title"),"%"+queryTask.getTitle()+"%"));
                }
                cq.where(predicates.toArray(new Predicate[predicates.size()]));
                return null;
            }
        },pageable);
    }

    @Override
    public List<TaskFile> allListTaskFiles() {
        return taskFileRepository.findAll();
    }

    @Transactional
    @Override
    public TaskFile saveTaskFile(TaskFile taskFile) {
        return taskFileRepository.save(taskFile);
    }

    @Transactional
    @Override
    public TaskFile getTaskFileById(Integer id) {
        return taskFileRepository.findById(id).get();
    }

    @Override
    public TaskFile getTaskFileByFileName(String fileName) {
        return taskFileRepository.findByFile(fileName);
    }

    @Transactional
    @Override
    public TaskFile updateTaskFile(Integer id, TaskFile taskFile) {
        TaskFile t = taskFileRepository.findById(id).get();
        if (t==null){
            throw new NotFoundException("不存在该类型");
        }
        BeanUtils.copyProperties(taskFile,t);
        return taskFileRepository.save(t);
    }

    @Transactional
    @Override
    public void deleteTaskFile(Integer id) {
        taskFileRepository.deleteById(id);
    }
}
