package com.mtmd.infrastructure.persistence;

import com.mtmd.application.IceAlreadyInStockException;
import com.mtmd.application.TechnicalException;
import com.mtmd.domain.Ice;
import com.mtmd.domain.IceRepository;
import com.mtmd.domain.category.Category;
import com.mtmd.domain.category.Cream;
import com.mtmd.domain.category.Sorbet;
import com.mtmd.domain.category.Water;

import javax.enterprise.context.ApplicationScoped;
import javax.persistence.EntityExistsException;
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
    @SuppressWarnings("unchecked")
    public Ice add(Ice ice) {
        try {
            // persist the category before
            // FIXME this looks wrong
            boolean exists = em.createQuery("select c from Category c", Category.class).getResultList().stream()
                    .anyMatch(entity -> compareCategory(ice.getCategory(), entity));
            if(exists){
                ice.setCategory(em.merge(ice.getCategory()));
            }else{
                em.persist(ice.getCategory());
            }
            em.persist(ice);
        } catch(EntityExistsException e){
            throw new IceAlreadyInStockException(String.format("Ice with name '%s' is already in stock!", ice.getName()));
        }catch (Throwable cause){
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

    private boolean compareCategory(Category unmanaged, Category entity){
        if(unmanaged.getClass().getName().equals(entity.getClass().getName())){
            // both are of same type, continue
            boolean result;
            if(unmanaged instanceof Water){
                result = ((Water)unmanaged).getName().equals(((Water)entity).getName());
            }else if (unmanaged instanceof Sorbet){
                result = ((Sorbet)unmanaged).getName().equals(((Sorbet)entity).getName());
            }else if(unmanaged instanceof Cream){
                result = ((Cream)unmanaged).getName().equals(((Cream)entity).getName());
            }else{
                throw new IllegalArgumentException("Unmapped Type");
            }
            return result;
        }
        return false;
    }
}
