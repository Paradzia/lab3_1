package pl.com.bottega.ecommerce.sales.domain.invoicing;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import pl.com.bottega.ecommerce.canonicalmodel.publishedlanguage.ClientData;
import pl.com.bottega.ecommerce.canonicalmodel.publishedlanguage.Id;
import pl.com.bottega.ecommerce.sales.domain.productscatalog.Product;
import pl.com.bottega.ecommerce.sales.domain.productscatalog.ProductData;
import pl.com.bottega.ecommerce.sales.domain.productscatalog.ProductType;
import pl.com.bottega.ecommerce.sharedkernel.Money;

import java.util.Date;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

public class BookKeeperTest {

    private BookKeeper bookKeeper;

    private TaxPolicy mockedTaxPolicy = mock(TaxPolicy.class);
    private Id id = new Id("123");

    ProductType standard = ProductType.STANDARD;
    Money zero = Money.ZERO;

    ClientData client;
    InvoiceRequest invoiceRequest;
    ProductData product;


    @Before public void setUp() throws Exception {
        client = new ClientData(id, "test");
        invoiceRequest = new InvoiceRequest(client);
        product = new ProductData(id,zero,"klawiatura",standard,new Date());
    }

    @Test public void invoiceWithOnePosition() throws Exception {


        InvoiceFactory mockedInvoiceFactory = mock(InvoiceFactory.class);
        bookKeeper = new BookKeeper(mockedInvoiceFactory);


        RequestItem item = new RequestItem(product,1,zero);
        invoiceRequest.add(item);

        when(mockedInvoiceFactory.create(client)).thenReturn(new Invoice(id,client));
        when(mockedTaxPolicy.calculateTax(standard,zero)).thenReturn(new Tax(zero,"tax"));

        Invoice invoiceResult = bookKeeper.issuance(invoiceRequest,mockedTaxPolicy);

        assertThat(invoiceResult.getItems().size(),is(1));
    }

    @Test public void invoiceWithTwoPositionsShouldCallCalculateTaxMethodTwice() throws Exception{

        InvoiceFactory mockedInvoiceFactory = mock(InvoiceFactory.class);
        bookKeeper = new BookKeeper(mockedInvoiceFactory);

        RequestItem item = new RequestItem(product,1,zero);
        invoiceRequest.add(item);
        invoiceRequest.add(item);

        when(mockedInvoiceFactory.create(client)).thenReturn(new Invoice(id,client));
        when(mockedTaxPolicy.calculateTax(standard,zero)).thenReturn(new Tax(zero,"tax"));

        Invoice invoiceResult = bookKeeper.issuance(invoiceRequest,mockedTaxPolicy);

        verify(mockedTaxPolicy,times(2)).calculateTax(standard,zero);
    }


    @Test public void invoiceWithoutPositionsShouldReturnEmptyInvoice() throws Exception{


        InvoiceFactory mockedInvoiceFactory = mock(InvoiceFactory.class);
        bookKeeper = new BookKeeper(mockedInvoiceFactory);


        when(mockedInvoiceFactory.create(client)).thenReturn(new Invoice(id,client));
        when(mockedTaxPolicy.calculateTax(standard,zero)).thenReturn(new Tax(zero,"tax"));

        Invoice invoiceResult = bookKeeper.issuance(invoiceRequest,mockedTaxPolicy);

        assertThat(invoiceResult.getItems().size(),is(0));

    }

    @Test public void InvoiceWithoudPositionsShouldNotCall_calculateTax() throws Exception{

        InvoiceFactory mockedInvoiceFactory = mock(InvoiceFactory.class);
        bookKeeper = new BookKeeper(mockedInvoiceFactory);


        when(mockedInvoiceFactory.create(client)).thenReturn(new Invoice(id,client));
        when(mockedTaxPolicy.calculateTax(standard,zero)).thenReturn(new Tax(zero,"tax"));

        Invoice invoiceResult = bookKeeper.issuance(invoiceRequest,mockedTaxPolicy);

        verify(mockedTaxPolicy,times(0)).calculateTax(standard,zero);

    }

}
