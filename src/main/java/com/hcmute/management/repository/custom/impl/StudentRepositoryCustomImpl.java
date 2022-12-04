package com.hcmute.management.repository.custom.impl;

import com.hcmute.management.common.OrderByEnum;
import com.hcmute.management.common.StudentSort;
import com.hcmute.management.model.entity.StudentEntity;
import com.hcmute.management.model.entity.UserEntity;
import com.hcmute.management.repository.custom.StudentRepositoryCustom;
import com.mysql.cj.log.Log;
import org.hibernate.Criteria;
import org.springframework.security.core.parameters.P;

import javax.persistence.PersistenceContext;

import javax.persistence.EntityManager;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.ArrayList;
import java.util.List;

public class StudentRepositoryCustomImpl implements StudentRepositoryCustom {
    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public List<StudentEntity> search(String searchText, OrderByEnum orderBy, StudentSort order, int pageindex, int pagesize) {
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();

        //Create filter query and count query
        CriteriaQuery<StudentEntity> query = cb.createQuery(StudentEntity.class);
        CriteriaQuery<Long> countQuery = cb.createQuery(Long.class);

        Root<StudentEntity> root = query.from(StudentEntity.class);
        Root<StudentEntity> countRoot = countQuery.from(StudentEntity.class);


        //Although filter query and count query have the same where clause, but we have to create 2 lists predicate separately
        List<Predicate> predicates = new ArrayList<>();
        List<Predicate> countPredicates = new ArrayList<>();

        // Filter by keyword, we have options here:  MSSV, education_program, chuyen nganh, class_id;
//        if (searchTextType != null) {
//            if (searchTextType.equals("id")) { // FIlter by id
//                predicates.add(cb.like(root.get("id"), "%" + searchText + "%"));
//            } else if (searchTextType.equals("education_program")) { //Filter by education program
//                predicates.add(cb.like(root.get("education_program"), "%" + searchText + "%"));
//            } else if (searchTextType.equals("class_id")) { //filter class _id
//                predicates.add(cb.like(root.get("class_id"), "%" + searchText + "%"));
//            } else if (searchTextType.equals("fullName")) {//filter by fullname
//                predicates.add(cb.like(
//                        cb.concat(root.get("user").get("fullName"), " "),"%" + searchText + "%"));
//            }
//        }
        Predicate predicatesId = cb.like(root.get("id"), "%" + searchText + "%");
        Predicate predicateedcation = cb.like(root.get("education_program"), "%" + searchText + "%");
        Predicate predicateclass_id = cb.like(cb.concat(root.get("classes").get("classname"), " "),"%" + searchText + "%");
        Predicate predicatename = cb.like(cb.concat(root.get("user").get("fullName"), " "),"%" + searchText + "%");
        predicates.add(cb.or(predicatesId,predicateedcation,predicateclass_id,predicatename));


        if (orderBy.equals("asc")) {
            query.orderBy(cb.asc(root.get(order.getName())));
        } else {
            query.orderBy(cb.desc(root.get(order.getName())));
        }

        query.select(root).where(predicates.toArray(new Predicate[predicates.size()]));
        List<StudentEntity> listStudents =
                entityManager.createQuery(query).setFirstResult((pageindex) * pagesize).setMaxResults(pagesize).getResultList();
        return listStudents;
    }
}
