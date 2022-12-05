package com.hcmute.management.repository.custom.impl;

import com.hcmute.management.common.LecturerSort;
import com.hcmute.management.common.OrderByEnum;
import com.hcmute.management.model.entity.LecturerEntity;
import com.hcmute.management.repository.custom.LecturerRepositoryCustom;
import org.springframework.data.domain.Page;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.ArrayList;
import java.util.List;

public class LecturerRepositoryCustomImpl implements LecturerRepositoryCustom {
    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public List<LecturerEntity> search(String searchText, OrderByEnum orderBy, LecturerSort order, int pageindex, int pagesize) {
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();

        //Create filter query and count query
        CriteriaQuery<LecturerEntity> query = cb.createQuery(LecturerEntity.class);
        CriteriaQuery<Long> countQuery = cb.createQuery(Long.class);

        Root<LecturerEntity> root = query.from(LecturerEntity.class);
        Root<LecturerEntity> countRoot = countQuery.from(LecturerEntity.class);


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
        Predicate predicateposition = cb.like(root.get("position"), "%" + searchText + "%");
        Predicate predicatequalification = cb.like(cb.concat(root.get("qualification"), " "),"%" + searchText + "%");
        Predicate predicatename = cb.like(cb.concat(root.get("user").get("fullName"), " "),"%" + searchText + "%");
        predicates.add(cb.or(predicatesId,predicateposition,predicatequalification,predicatename));


        if (orderBy.equals("asc")) {
            query.orderBy(cb.asc(root.get(order.getName())));
        } else {
            query.orderBy(cb.desc(root.get(order.getName())));
        }

        query.select(root).where(predicates.toArray(new Predicate[predicates.size()]));
        List<LecturerEntity> listLecturers =
                entityManager.createQuery(query).setFirstResult((pageindex) * pagesize).setMaxResults(pagesize).getResultList();
        return listLecturers;
    }
}
