package org.senla_project.application.repository.impl;

import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import org.senla_project.application.entity.Collaboration;
import org.senla_project.application.entity.Collaboration_;
import org.senla_project.application.repository.AbstractDao;
import org.senla_project.application.repository.CollaborationRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public class CollaborationRepositoryImpl extends AbstractDao<UUID, Collaboration> implements CollaborationRepository {
    @Override
    protected Class<Collaboration> getEntityClass() {
        return Collaboration.class;
    }

    @Override
    public Optional<Collaboration> findCollabByName(String collabName) {
        CriteriaBuilder builder = entityManager.getCriteriaBuilder();
        CriteriaQuery<Collaboration> query = builder.createQuery(Collaboration.class);

        Root<Collaboration> root = query.from(Collaboration.class);

        Predicate equalsCollabName = builder.equal(root.get(Collaboration_.collabName), collabName);

        query.select(root).where(equalsCollabName);

        var results = entityManager.createQuery(query).getResultList();
        if (results.isEmpty()) return Optional.empty();
        return Optional.of(results.getFirst());
    }
}
