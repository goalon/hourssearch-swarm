import javax.inject.Inject;
import javax.ws.rs.*;
import java.util.List;

@Path("/query")
public class Query {
    @Inject
    private OfferDAO offerDAO;

    @GET
    @Produces("application/json")
    public List<Offer> getAll() {
        return offerDAO.getAll();
    }

    @POST
    @Path("/{limit}")
    @Consumes("application/json")
    @Produces("application/json")
    public List<Offer> get(final QueryFind queryFind, @PathParam("limit") final int limit) {
        return offerDAO.get(queryFind, limit);
    }
}
