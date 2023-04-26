package com.yzx.party.service;

import com.yzx.party.dao.TaskRepository;
import com.yzx.party.myException.NotFoundException;
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
 * @since 2022/3/10 10:04
 */
@Service
public class TaskServiceImpl implements TaskService {

    @Autowired
    TaskRepository taskRepository;

    @Transactional
    @Override
    public Page<Tasks> listTasks(Pageable pageable, QueryTask queryTask) {
        return taskRepository.findAll(new Specification<Tasks>() {
            @Override
            public Predicate toPredicate(Root<Tasks> root, CriteriaQuery<?> cq, CriteriaBuilder cb) {
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
    public List<Tasks> listTask() {
        return taskRepository.findAll();
    }

    @Transactional
    @Override
    public Tasks saveTask(Tasks tasks) {
        return taskRepository.save(tasks);
    }

    @Transactional
    @Override
    public Tasks getTaskById(Integer id) {
        return taskRepository.findById(id).get();
    }

    @Transactional
    @Override
    public Tasks updateTasks(Integer id, Tasks tasks) {
        Tasks t = taskRepository.findById(id).get();
        if (t==null){
            throw new NotFoundException("不存在该类型");
        }
        BeanUtils.copyProperties(tasks,t);
        return taskRepository.save(t);
    }

    @Transactional
    @Override
    public void deleteTask(Integer id) {
        taskRepository.deleteById(id);
    }
}
