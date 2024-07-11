package DomainLayer.Role;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "system_manager")
public class SystemManager {

    @Id
    @Column(name = "member_id", nullable = false, unique = true)
    private String member_ID;

    SystemManager(String member_ID)
    {
        this.member_ID = member_ID;
    }

    public SystemManager() {

    }

    public String getMember_ID() {
        return this.member_ID;
    }

}
