import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.TimeUnit;
import java.util.regex.Pattern;

@Path("/update")
public class Update {
    @Inject
    private OfferDAO offerDAO;

    @GET
    @Produces("text/plain")
    public String update() throws ParseException {
        offerDAO.removeOld();

        final String response = "Dodano %d rekordy.";
        final String url = "http://mbopn.kuratorium.waw.pl/";
        final String[] labels = {" Typ: ", " Rodzaj placówki: ", "nazwa szkoły/placówki:", " miejscowość:",
                "ulica:", "nr domu:", "kod pocztowy:", "telefon:", "email:", "Przedmiot:",
                "Liczba godzin w tygodniu:", "Wymiar zatrudnienia:", "Data dodania:", "Termin składania dokumentów:"};

        ChromeOptions options = new ChromeOptions();
        final String GOOGLE_CHROME_SHIM = System.getenv("GOOGLE_CHROME_SHIM");
        if (GOOGLE_CHROME_SHIM != null) {
            options.setBinary(GOOGLE_CHROME_SHIM);
        } else {
            options.addArguments("headless");
        }

        WebDriver offersDriver = new ChromeDriver(options);
        WebDriver detailsDriver = new ChromeDriver(options);

        offersDriver.manage().timeouts().implicitlyWait(5, TimeUnit.SECONDS);

        Set<String> extracted_details_urls = new HashSet<>();

        for (int i = 1, records = 0; true; ++i) {
            offersDriver.get(url + "#/page/" + Integer.toString(i));
            List<WebElement> offers = offersDriver.findElement(By.id("all")).findElements(By.xpath("//td[a=\"szczegóły \"]/a"));
            if (offers.isEmpty()) {
                detailsDriver.close();
                offersDriver.close();
                return String.format(response, records);
            }

            for (WebElement offer : offers) {
                final String details_url = offer.getAttribute("href");
                if (!extracted_details_urls.add(details_url)) {
                    continue; // Prevents errors when a new offer was added on the site.
                }

                detailsDriver.get(details_url);
                WebElement details = detailsDriver.findElement(By.id("detalis"));
                WebDriverWait wait = new WebDriverWait(detailsDriver, 5);
                wait.until(ExpectedConditions.textMatches(By.xpath("//fieldset/p"), Pattern.compile("(.+)\\(numer oferty: (\\d{5,})\\)")));

                List<String> attrs = new ArrayList<>(16);
                attrs.add(details.findElement(By.xpath("//fieldset/p")).getText());

                for (String label : labels) {
                    String data = details.findElement(By.xpath("//tr[td=\"" + label + "\"]/td[@class=\"ng-binding\"]")).getText();
                    attrs.add(data);
                }

                String data = details.findElement(By.xpath("//td/p")).getText();
                attrs.add(data);

                Offer newOffer = new Offer(attrs.get(0), attrs.get(1), attrs.get(2), attrs.get(3), attrs.get(4), attrs.get(5),
                        attrs.get(6), attrs.get(7), attrs.get(8), attrs.get(9), attrs.get(10), Short.parseShort(attrs.get(11)),
                        attrs.get(12), new SimpleDateFormat("yyyy-MM-dd").parse(attrs.get(13)),
                        new SimpleDateFormat("yyyy-MM-dd").parse(attrs.get(14)), attrs.get(15));

                if (!offerDAO.insert(newOffer)) {
                    detailsDriver.close();
                    offersDriver.close();
                    return String.format(response, records);
                }
                ++records;
            }
        }
    }
}
