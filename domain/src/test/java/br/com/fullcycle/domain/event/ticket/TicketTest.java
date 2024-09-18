package br.com.fullcycle.domain.event.ticket;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import br.com.fullcycle.domain.customer.CustomerId;
import br.com.fullcycle.domain.event.Event;
import br.com.fullcycle.domain.partner.Partner;

public class TicketTest {

    @Test
    @DisplayName("Deve criar um ticket")
    public void testReserveTicket() throws Exception {
        // given
        final var aPartner = Partner.newPartner("John Doe", "55.918.450/0001-50", "john.doe@gmail.com");
                
        final var expectedCustomerId = CustomerId.unique();
        final var expectedTicketStatus = TicketStatus.PENDING;
        
        final var anEvent = Event.newEvent("Disney on Ice", "2021-01-01", 100, aPartner);
        final var expectedEventId = anEvent.getEventId();

        // when
        
        final var actualTicket = Ticket.newTicket(expectedCustomerId, expectedEventId);

        // then

        Assertions.assertNotNull(actualTicket.getTicketId());
        Assertions.assertEquals(expectedCustomerId, actualTicket.getCustomerId());
        Assertions.assertEquals(expectedEventId, actualTicket.getEventId());
        Assertions.assertEquals(expectedTicketStatus, actualTicket.getTicketStatus());
        Assertions.assertNull(actualTicket.getPaidAt());
        Assertions.assertNotNull(actualTicket.getReservedAt());  
    }
}
