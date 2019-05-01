package panda.repository;

import panda.domain.entities.Package;
import panda.domain.entities.Status;

import javax.inject.Inject;
import javax.persistence.EntityManager;
import java.util.List;

public class PackageRepositoryImpl implements PackageRepository {

    private final EntityManager entityManager;
    private static final String FIND_PACKAGE_BY_ID_QUERY = "SELECT p FROM packages p WHERE p.id=:id";
    private static final String FIND_ALL_PACKAGE_QUERY = "SELECT p FROM packages p";
    private static final String COUNT_ALL_PACKAGE_QUERY = "SELECT count(p) FROM packages p";
    private static final String FIND_ALL_PACKAGE_BY_STATUS_QUERY = "SELECT p FROM Package p WHERE p.status = :status";

    @Inject
    public PackageRepositoryImpl(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    @Override
    public Package save(Package entity) {
        this.entityManager.getTransaction().begin();
        this.entityManager.persist(entity);
        this.entityManager.getTransaction().commit();

        return entity;
    }


    @Override
    public Package findById(String id) {
        return this.entityManager
                .createQuery(FIND_PACKAGE_BY_ID_QUERY, Package.class)
                .setParameter("id", id)
                .getSingleResult();
    }

    @Override
    public List<Package> findAll() {
        return this.entityManager
                .createQuery(FIND_ALL_PACKAGE_QUERY, Package.class)
                .getResultList();
    }

    @Override
    public Long size() {
        return this.entityManager
                .createQuery(COUNT_ALL_PACKAGE_QUERY, Long.class)
                .getSingleResult();
    }

    @Override
    public List<Package> findAllPackagesByStatus(Status status) {
        this.entityManager.getTransaction().begin();
        List<Package> packages = this.entityManager
                .createQuery(FIND_ALL_PACKAGE_BY_STATUS_QUERY, Package.class)
                .setParameter("status", status)
                .getResultList();
        this.entityManager.getTransaction().commit();

        return packages;
    }

    @Override
    public Package updatePackage(Package aPackage) {
        this.entityManager.getTransaction().begin();
        Package updated = this.entityManager.merge(aPackage);
        this.entityManager.getTransaction().commit();

        return updated;
    }
}
