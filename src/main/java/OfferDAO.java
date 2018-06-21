import javax.annotation.Resource;
import javax.enterprise.context.Dependent;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.UserTransaction;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;

@Dependent
public class OfferDAO {
    @PersistenceContext
    private EntityManager em;

    @Resource
    private UserTransaction utx;

    public List<Offer> getAll() {
        return em.createQuery("SELECT o FROM Offer o", Offer.class).getResultList();
    }

    public List<Offer> get(final QueryFind queryFind, final int limit) {
        return em.createQuery("SELECT o FROM Offer o WHERE " + queryFind.toString(), Offer.class)
                .setMaxResults(limit).getResultList();
    }

    public void removeOld() {
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        Calendar calendar = Calendar.getInstance();
        try {
            utx.begin();
            em.createQuery("DELETE FROM Offer o WHERE o.deadline<'" + dateFormat.format(calendar.getTime()) + "'").executeUpdate();
            utx.commit();
        } catch (Throwable e) {
            System.err.println(e.getMessage());
        }
    }

    public boolean insert(Offer offer) {
        try {
            utx.begin();
            em.persist(offer);
            utx.commit();
        } catch (Throwable e) {
            System.err.println(e.getMessage());
            return false;
        }
        return true;
    }
}
