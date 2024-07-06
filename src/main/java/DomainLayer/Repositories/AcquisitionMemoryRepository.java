package DomainLayer.Repositories;

import DomainLayer.PaymentServices.Acquisition;
import org.springframework.context.annotation.Profile;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.repository.query.FluentQuery;
import org.springframework.stereotype.Repository;

import java.util.*;
import java.util.function.Function;

@Repository
@Profile("memory")
public class AcquisitionMemoryRepository implements AcquisitionRepository{

    private Map<String, Acquisition> IdAndAcquisition = new HashMap<>();
    private final Object acquisitionLock;

    public AcquisitionMemoryRepository(){
        acquisitionLock = new Object();
    }


    @Override
    public void flush() {

    }

    @Override
    public <S extends Acquisition> S saveAndFlush(S entity) {
        return null;
    }

    @Override
    public <S extends Acquisition> List<S> saveAllAndFlush(Iterable<S> entities) {
        return List.of();
    }

    @Override
    public void deleteAllInBatch(Iterable<Acquisition> entities) {

    }

    @Override
    public void deleteAllByIdInBatch(Iterable<String> strings) {

    }

    @Override
    public void deleteAllInBatch() {

    }

    @Override
    public Acquisition getOne(String s) {
        return null;
    }

    @Override
    public Acquisition getById(String s) {
        return null;
    }

    @Override
    public Acquisition getReferenceById(String s) {
        return null;
    }

    @Override
    public <S extends Acquisition> Optional<S> findOne(Example<S> example) {
        return Optional.empty();
    }

    @Override
    public <S extends Acquisition> List<S> findAll(Example<S> example) {
        return List.of();
    }

    @Override
    public <S extends Acquisition> List<S> findAll(Example<S> example, Sort sort) {
        return List.of();
    }

    @Override
    public <S extends Acquisition> Page<S> findAll(Example<S> example, Pageable pageable) {
        return null;
    }

    @Override
    public <S extends Acquisition> long count(Example<S> example) {
        return 0;
    }

    @Override
    public <S extends Acquisition> boolean exists(Example<S> example) {
        return false;
    }

    @Override
    public <S extends Acquisition, R> R findBy(Example<S> example, Function<FluentQuery.FetchableFluentQuery<S>, R> queryFunction) {
        return null;
    }

    @Override
    public <S extends Acquisition> S save(S entity) {
        synchronized (acquisitionLock) {
            IdAndAcquisition.put(entity.getAcquisitionId(), entity);
            return entity;
        }
    }


    @Override
    public <S extends Acquisition> List<S> saveAll(Iterable<S> entities) {
        return List.of();
    }

    @Override
    public Optional<Acquisition> findById(String s) {
        synchronized (acquisitionLock) {
            return Optional.ofNullable(IdAndAcquisition.get(s));
        }
    }

    @Override
    public boolean existsById(String s) {
        return false;
    }

    @Override
    public List<Acquisition> findAll() {
        synchronized (acquisitionLock) {
            return new ArrayList<>(IdAndAcquisition.values());
        }
    }

    @Override
    public List<Acquisition> findAllById(Iterable<String> strings) {
        return List.of();
    }

    @Override
    public long count() {
        return 0;
    }

    @Override
    public void deleteById(String s) {

    }

    @Override
    public void delete(Acquisition entity) {

    }

    @Override
    public void deleteAllById(Iterable<? extends String> strings) {

    }

    @Override
    public void deleteAll(Iterable<? extends Acquisition> entities) {

    }

    @Override
    public void deleteAll() {

    }

    @Override
    public List<Acquisition> findAll(Sort sort) {
        return List.of();
    }

    @Override
    public Page<Acquisition> findAll(Pageable pageable) {
        return null;
    }
}
