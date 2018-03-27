package pl.com.bottega.ecommerce.sales.domain.invoicing;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import pl.com.bottega.ecommerce.canonicalmodel.publishedlanguage.ClientData;
import pl.com.bottega.ecommerce.canonicalmodel.publishedlanguage.Id;
import pl.com.bottega.ecommerce.sales.domain.client.Client;

import static org.junit.Assert.*;

public class BookKeeperTest {

    BookKeeper bookKeeper = new BookKeeper(new InvoiceFactory());
    Id id = new Id("1");
    TaxPolicy taxPolicy = Mockito.mock(TaxPolicy.class);
    InvoiceRequest invoiceRequest = new InvoiceRequest(new ClientData(id,"Johny"));

    @Before public void setUp() throws Exception {
        invoiceRequest.add(Mockito.mock(RequestItem.class));
    }

    @Test public void issuanceShouldReturnInvoiceWithOneItem() {
        bookKeeper.issuance(invoiceRequest,taxPolicy);

    }
}
