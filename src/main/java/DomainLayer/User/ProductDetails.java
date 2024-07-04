package DomainLayer.User;
import jakarta.persistence.*;

import javax.persistence.Embeddable;

@Entity
public class ProductDetails {

    private int quantity;
    private int totalPrice;
    @Id
    //@GeneratedValue(strategy = GenerationType.IDENTITY)
    private String productName;

    // Constructors, getters, and setters
    public ProductDetails() {
    }

    public ProductDetails(int quantity, int totalPrice) {
        this.quantity = quantity;
        this.totalPrice = totalPrice;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public int getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(int totalPrice) {
        this.totalPrice = totalPrice;
    }

    //public void setId(Long id) {
    //    this.id = id;
    //}

    //public Long getId() {
    //    return id;
   // }
}
