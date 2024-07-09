package DomainLayer.User;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

@Entity
public class Guest extends State{


    @Id
    private Long id;

    public Guest(){
        super();
    }
    @Override
    public void Logout() {
        throw new IllegalArgumentException("only member can log out");
    }

    @Override
    public void exitMarketSystem() {
    }

    @Override
    public void Login() {
        //do nothing
        return;
    }

    @Override
    public boolean isMember() {
        return false;
    }


    @Override
    public String getUsername() {
        return null;
    }


    @Override
    public void addAcquisition(String acquisitionId) {
        return;
    }

    @Override
    public List<String> getAcquisitionIds() {
        return null;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }
    @Override
    public int removeAcquisition(String acquisitionId)  {
        return -1;
    }
}
