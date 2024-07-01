package DomainLayer.Repositories;

import DomainLayer.User.Member;

import java.util.List;

public interface MemberRepository {
    public Member get(String id);
    public void add(String memberId, Member to_add);
    public void remove(String to_remove);
    public List<Member> getAll();
    public boolean contain(String memberId);
    public void clear();
    public Member getByUserName(String member);

}
