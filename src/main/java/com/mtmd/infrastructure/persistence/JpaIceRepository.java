package com.mtmd.infrastructure.persistence;

import com.mtmd.application.TechnicalException;
import com.mtmd.domain.Ice;
import com.mtmd.domain.IceRepository;

import javax.enterprise.context.ApplicationScoped;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

@ApplicationScoped
public class JpaIceRepository implements IceRepository {

    @PersistenceContext
    EntityManager em;

    @Override
    public Ice add(Ice ice) {
        try {
            // persist the category before
            em.persist(ice.getCategory());
            em.persist(ice);
        } catch (Throwable cause){
            throw new TechnicalException(cause);
        }
        return ice;
    }

    @Override
    public Optional<Ice> findById(String name) {
        try {
            return Optional.ofNullable(em.find(Ice.class, name));
        } catch (Throwable cause){
            throw new TechnicalException(cause);
        }
    }

    @Override
    @SuppressWarnings("unchecked")
    public List<Ice> findAll() {
        try {
            return Collections.unmodifiableList(em.createQuery("select i from Ice i").getResultList());
        } catch (Throwable cause){
            throw new TechnicalException(cause);
        }
    }
}
