package DomainLayer.Repositories;

import DomainLayer.User.Member;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.FluentQuery;

import java.util.List;
import java.util.Optional;
import java.util.function.Function;

public class MemberDBRepository implements MemberRepository, JpaRepository<Member,String> {
    @Override
    public Member get(String id) {
        return null;
    }

    @Override
    public void add(String memberId, Member to_add) {

    }

    @Override
    public void remove(String to_remove) {

    }

    @Override
    public List<Member> getAll() {
        return List.of();
    }

    @Override
    public boolean contain(String memberId) {
        return false;
    }

    @Override
    public void clear() {

    }

    @Override
    public Member getByUserName(String member) {
        return null;
    }

    @Override
    public void flush() {

    }

    @Override
    public <S extends Member> S saveAndFlush(S entity) {
        return null;
    }

    @Override
    public <S extends Member> List<S> saveAllAndFlush(Iterable<S> entities) {
        return List.of();
    }

    @Override
    public void deleteAllInBatch(Iterable<Member> entities) {

    }

    @Override
    public void deleteAllByIdInBatch(Iterable<String> strings) {

    }

    @Override
    public void deleteAllInBatch() {

    }

    @Override
    public Member getOne(String s) {
        return null;
    }

    @Override
    public Member getById(String s) {
        return null;
    }

    @Override
    public Member getReferenceById(String s) {
        return null;
    }

    @Override
    public <S extends Member> Optional<S> findOne(Example<S> example) {
        return Optional.empty();
    }

    @Override
    public <S extends Member> List<S> findAll(Example<S> example) {
        return List.of();
    }

    @Override
    public <S extends Member> List<S> findAll(Example<S> example, Sort sort) {
        return List.of();
    }

    @Override
    public <S extends Member> Page<S> findAll(Example<S> example, Pageable pageable) {
        return null;
    }

    @Override
    public <S extends Member> long count(Example<S> example) {
        return 0;
    }

    @Override
    public <S extends Member> boolean exists(Example<S> example) {
        return false;
    }

    @Override
    public <S extends Member, R> R findBy(Example<S> example, Function<FluentQuery.FetchableFluentQuery<S>, R> queryFunction) {
        return null;
    }

    @Override
    public <S extends Member> S save(S entity) {
        return null;
    }

    @Override
    public <S extends Member> List<S> saveAll(Iterable<S> entities) {
        return List.of();
    }

    @Override
    public Optional<Member> findById(String s) {
        return Optional.empty();
    }

    @Override
    public boolean existsById(String s) {
        return false;
    }

    @Override
    public List<Member> findAll() {
        return List.of();
    }

    @Override
    public List<Member> findAllById(Iterable<String> strings) {
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
    public void delete(Member entity) {

    }

    @Override
    public void deleteAllById(Iterable<? extends String> strings) {

    }

    @Override
    public void deleteAll(Iterable<? extends Member> entities) {

    }

    @Override
    public void deleteAll() {

    }

    @Override
    public List<Member> findAll(Sort sort) {
        return List.of();
    }

    @Override
    public Page<Member> findAll(Pageable pageable) {
        return null;
    }
}
