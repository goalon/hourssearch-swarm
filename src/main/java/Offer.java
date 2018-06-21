import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.util.Date;

@Entity
@Table(name = "offer")
public class Offer {
    @Id
    private String title;
    private String level;
    private String kind;
    private String name;
    private String locality;
    private String street;
    private String number;
    private String postal_code;
    private String phone_number;
    private String email;
    private String subject;
    private short hours;
    private String means;
    private Date added;
    private Date deadline;
    private String description;

    public Offer() {
    }

    public Offer(String title, String level, String kind, String name, String locality, String street, String number,
                 String postal_code, String phone_number, String email, String subject, short hours, String means,
                 Date added, Date deadline, String description) {
        this.title = title;
        this.level = level;
        this.kind = kind;
        this.name = name;
        this.locality = locality;
        this.street = street;
        this.number = number;
        this.postal_code = postal_code;
        this.phone_number = phone_number;
        this.email = email;
        this.subject = subject;
        this.hours = hours;
        this.means = means;
        this.added = added;
        this.deadline = deadline;
        this.description = description;
    }

    public String getTitle() {
        return title;
    }

    public String getLevel() {
        return level;
    }

    public String getKind() {
        return kind;
    }

    public String getName() {
        return name;
    }

    public String getLocality() {
        return locality;
    }

    public String getStreet() {
        return street;
    }

    public String getNumber() {
        return number;
    }

    public String getPostal_code() {
        return postal_code;
    }

    public String getPhone_number() {
        return phone_number;
    }

    public String getEmail() {
        return email;
    }

    public String getSubject() {
        return subject;
    }

    public short getHours() {
        return hours;
    }

    public String getMeans() {
        return means;
    }

    public Date getAdded() {
        return added;
    }

    public Date getDeadline() {
        return deadline;
    }

    public String getDescription() {
        return description;
    }
}
