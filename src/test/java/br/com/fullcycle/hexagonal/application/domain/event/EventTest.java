package br.com.fullcycle.hexagonal.application.domain.event;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullSource;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.Mockito;

import static org.mockito.Mockito.when;

import java.time.format.DateTimeFormatter;

import br.com.fullcycle.hexagonal.application.domain.customer.CustomerId;
import br.com.fullcycle.hexagonal.application.domain.event.ticket.TicketStatus;
import br.com.fullcycle.hexagonal.application.domain.partner.Partner;
import br.com.fullcycle.hexagonal.application.exceptions.ValidationException;

public class EventTest {
    
    @Test
    @DisplayName("Deve criar um evento")
    public void testCreateEvent() throws Exception {
        // given
        final var aPartner = Partner.newPartner("John Doe", "55.918.450/0001-50", "john.doe@gmail.com");
        final var expectedDate = "2021-01-01";
        final var expectedName = "Disney on Ice";
        final var expectedTotalSpots = 100;
        final var expectedPartnerId = aPartner.getPartnerId().value();
        final var expectedTickets = 0;

        // when
        final var actualEvent = Event.newEvent(expectedName, expectedDate, expectedTotalSpots, aPartner);

        // then
        Assertions.assertNotNull(actualEvent.getEventId());
        Assertions.assertEquals(expectedName, actualEvent.getName().value());
        Assertions.assertEquals(expectedDate, actualEvent.getDate().format(DateTimeFormatter.ISO_LOCAL_DATE));
        Assertions.assertEquals(expectedPartnerId, actualEvent.getPartnerId().value());
        Assertions.assertEquals(expectedTotalSpots, actualEvent.getTotalSpots());
        Assertions.assertEquals(expectedTickets, actualEvent.allTickets().size());
    }

    @Test
    @DisplayName("Deve criar um evento com nome inválido")
    public void testCreateEventWithInvalidName() throws Exception {
        // given
        final var aPartner = Partner.newPartner("John Doe", "55.918.450/0001-50", "john.doe@gmail.com");
        final var expectedMessage = "Invalid value for name";

        // when
        final var actualException = Assertions.assertThrows(
            ValidationException.class, 
            () -> Event.newEvent(null, "2021-01-01", 100, aPartner));
        // then

        Assertions.assertEquals(expectedMessage, actualException.getMessage());
    }

    @ParameterizedTest
    @NullSource
    @ValueSource(strings = {"20240101"})
    @DisplayName("Deve criar um evento com data inválida")
    public void testCreateEventWithInvalidDate(String date) throws Exception {
        // given
        final var aPartner = Partner.newPartner("John Doe", "55.918.450/0001-50", "john.doe@gmail.com");
        final var expectedMessage = "Invalid date for Event";

        // when
        final var actualException = Assertions.assertThrows(
            ValidationException.class, 
            () -> Event.newEvent("Disney on Ice", date, 100, aPartner));
        // then

        Assertions.assertEquals(expectedMessage, actualException.getMessage());
    }

    @Test
    @DisplayName("Deve criar um evento com parceiro inválido")
    public void testCreateEventWithInvalidPartner() throws Exception {
        // given
        final var aPartner = Mockito.mock(Partner.class);
        final var expectedMessage = "Invalid partnerId for Event";

        // when
        when(aPartner.getPartnerId()).thenReturn(null);

        final var actualException = Assertions.assertThrows(
            ValidationException.class, 
            () -> Event.newEvent("Disney on Ice", "2021-01-01", 100, aPartner));
        // then

        Assertions.assertEquals(expectedMessage, actualException.getMessage());
    }

    @Test
    @DisplayName("Deve criar um evento com número de assentos inválido")
    public void testCreateEventWithInvalidTotalSpots() throws Exception {
        // given
        final var aPartner = Partner.newPartner("John Doe", "55.918.450/0001-50", "john.doe@gmail.com");
        final var expectedMessage = "Invalid totalSpots for Event";

        // when
        final var actualException = Assertions.assertThrows(
            ValidationException.class, 
            () -> Event.newEvent("Disney on Ice", "2021-01-01", null, aPartner));
        // then

        Assertions.assertEquals(expectedMessage, actualException.getMessage());
    }

    @Test
    @DisplayName("Deve reservar um ticket quando for possivel")
    public void testReserveTicket() throws Exception {
        // given
        final var aPartner = Partner.newPartner("John Doe", "55.918.450/0001-50", "john.doe@gmail.com");
        
        final var expectedDate = "2021-01-01";
        final var expectedName = "Disney on Ice";
        final var expectedTotalSpots = 100;
        final var expectedPartnerId = aPartner.getPartnerId().value();
        
        final var expectedCustomerId = CustomerId.unique();
        final var expectedTicketStatus = TicketStatus.PENDING;
        final var expectedTickets = 1;
        final var expectedTicketOrder = 1;
        
        final var actualEvent = Event.newEvent(expectedName, expectedDate, expectedTotalSpots, aPartner);
        final var expectedEventId = actualEvent.getEventId();
        
        // when

        final var actualTicket = actualEvent.reserveTicket(expectedCustomerId);

        // then

        Assertions.assertNotNull(expectedEventId);
        Assertions.assertEquals(expectedName, actualEvent.getName().value());
        Assertions.assertEquals(expectedDate, actualEvent.getDate().format(DateTimeFormatter.ISO_LOCAL_DATE));
        Assertions.assertEquals(expectedPartnerId, actualEvent.getPartnerId().value());
        Assertions.assertEquals(expectedTotalSpots, actualEvent.getTotalSpots());
        Assertions.assertEquals(expectedTickets, actualEvent.allTickets().size());

        Assertions.assertNotNull(actualTicket.getTicketId());
        Assertions.assertEquals(expectedCustomerId, actualTicket.getCustomerId());
        Assertions.assertEquals(expectedEventId, actualTicket.getEventId());
        Assertions.assertEquals(expectedTicketStatus, actualTicket.getTicketStatus());
        Assertions.assertNull(actualTicket.getPaidAt());
        Assertions.assertNotNull(actualTicket.getReservedAt());

        final var actualEventTicket = actualEvent.allTickets().iterator().next();
        Assertions.assertEquals(expectedTicketOrder, actualEventTicket.getOrdering());
        Assertions.assertEquals(expectedCustomerId, actualEventTicket.getCustomerId());
        Assertions.assertEquals(expectedEventId, actualEventTicket.getEventId());
        Assertions.assertEquals(actualTicket.getTicketId(), actualEventTicket.getTicketId());        
    }

    @Test
    @DisplayName("Nao deve reservar um ticket quando o evento está esgotado")
    public void testReserveTicketWhenEventIsSoldOut() throws Exception {
        // given
        final var aPartner = Partner.newPartner("John Doe", "55.918.450/0001-50", "john.doe@gmail.com");        
        final var anEvent = Event.newEvent("Disney on Ice", "2021-01-01", 1, aPartner);
        
        final var aCustomer = CustomerId.unique();
        anEvent.reserveTicket(CustomerId.unique());

        final var expectedErrorMessage = "Event sold out";

        // when

        final var actualException = Assertions.assertThrows(
            ValidationException.class, 
            () -> anEvent.reserveTicket(aCustomer));

        // then
        Assertions.assertEquals(expectedErrorMessage, actualException.getMessage());
    }

    @Test
    @DisplayName("Nao deve reservar dois tickets para o mesmo cliente")
    public void testReserveTwoTicketForTheSameCustomer() throws Exception {
        // given
        final var aPartner = Partner.newPartner("John Doe", "55.918.450/0001-50", "john.doe@gmail.com");        
        final var anEvent = Event.newEvent("Disney on Ice", "2021-01-01", 100, aPartner);
        
        final var aCustomer = CustomerId.unique();
        anEvent.reserveTicket(aCustomer);

        final var expectedErrorMessage = "Email already subscribed";

        // when

        final var actualException = Assertions.assertThrows(
            ValidationException.class, 
            () -> anEvent.reserveTicket(aCustomer));

        // then
        Assertions.assertEquals(expectedErrorMessage, actualException.getMessage());
    }
}
