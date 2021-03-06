package pl.com.bottega.ecommerce.sales.application.api.handler;

import org.hamcrest.generator.SugarGenerator;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.internal.util.reflection.Whitebox;
import pl.com.bottega.ecommerce.canonicalmodel.publishedlanguage.Id;
import pl.com.bottega.ecommerce.sales.application.api.command.AddProductCommand;
import pl.com.bottega.ecommerce.sales.domain.client.Client;
import pl.com.bottega.ecommerce.sales.domain.equivalent.SuggestionService;
import pl.com.bottega.ecommerce.sales.domain.productscatalog.Product;
import pl.com.bottega.ecommerce.sales.domain.productscatalog.ProductRepository;
import pl.com.bottega.ecommerce.sales.domain.reservation.Reservation;
import pl.com.bottega.ecommerce.sales.domain.reservation.ReservationRepository;

import java.lang.reflect.Field;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

public class AddProductCommandHandlerTest {

    private Id id;

    private AddProductCommandHandler addProductCommandHandler;
  //  private AddProductCommand addProductCommand;

    private ReservationRepository reservationRepository;
    private ProductRepository productRepository;
    private SuggestionService suggestionService;

    private Product product;
    private Reservation reservation;

    Field field;

    @Before public void setUp() {
        id = new Id("123");
        reservationRepository = mock(ReservationRepository.class);
        productRepository = mock(ProductRepository.class);
        suggestionService = mock(SuggestionService.class);
        product = mock(Product.class);
        reservation = mock(Reservation.class);

        addProductCommandHandler = new AddProductCommandHandler();

        try {
            initializeReflection();

        } catch (Exception e) {
            e.printStackTrace();
        }
        when(reservationRepository.load(any(Id.class))).thenReturn(reservation);
        when(productRepository.load(any(Id.class))).thenReturn(product);
        when(suggestionService.suggestEquivalent(any(Product.class), any(Client.class))).thenReturn(product);

    }

    private void initializeReflection() throws NoSuchFieldException, IllegalAccessException {
        field = AddProductCommandHandler.class.getDeclaredField("reservationRepository");
        field.setAccessible(true);
        field.set(addProductCommandHandler, reservationRepository);

        field = AddProductCommandHandler.class.getDeclaredField("productRepository");
        field.setAccessible(true);
        field.set(addProductCommandHandler, productRepository);

        field = AddProductCommandHandler.class.getDeclaredField("suggestionService");
        field.setAccessible(true);
        field.set(addProductCommandHandler, suggestionService);
    }

    @Test public void methodSaveIsCalled() throws Exception {
        AddProductCommand addProductCommand = new AddProductCommand(id,id,1);
        when(product.isAvailable()).thenReturn(true);

        addProductCommandHandler.handle(addProductCommand);
        verify(reservationRepository,times(1)).save(any(Reservation.class));
    }

}
