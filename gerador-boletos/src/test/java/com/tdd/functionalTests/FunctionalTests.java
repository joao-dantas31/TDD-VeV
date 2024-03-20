package com.tdd.functionalTests;

import com.tdd.Boleto;
import com.tdd.Fatura;
import com.tdd.Pagamento;
import com.tdd.ProcessadorBoletos;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class FunctionalTests {
    private ProcessadorBoletos pb;
    private Fatura fatura;
    private ArrayList<Boleto> boletos;
    @BeforeEach
    void setUp() {
        pb = new ProcessadorBoletos();
        fatura = new Fatura("Gabriel", new Date(), 1500);
        boletos = new ArrayList<>();
    }

    @Test
    @DisplayName("Teste caso o pagamento ultrapasse o valor da fatura")
    void testCase01() {
        boletos.add(new Boleto("1234", new Date(), 2000));
        List<Pagamento> pagamentos = pb.processa(fatura, boletos);

        assertEquals(1, pagamentos.size());
        assertEquals(1, fatura.getPagamentos().size());
        assertEquals("PAGA", fatura.getEstado());
    }

    @Test
    @DisplayName("Teste caso o pagamento NAO ultrapasse o valor da fatura")
    void testCase02() {
        boletos.add(new Boleto("1234", new Date(), 1000));
        List<Pagamento> pagamentos = pb.processa(fatura, boletos);

        assertEquals(1, pagamentos.size());
        assertEquals(1, fatura.getPagamentos().size());
        assertEquals("PENDENTE", fatura.getEstado());
    }

    @Test
    @DisplayName("Teste caso pagamento possua um valor negativo negativo")
    void testCase03() {
        boletos.add(new Boleto("1234", new Date(), -1500));

        assertThrows(IllegalArgumentException.class, () -> pb.processa(fatura, boletos));
    }

    @Test
    @DisplayName("Teste caso pagamento NAO possua nenhum boleto")
    void testCase04() {
        assertThrows(IllegalArgumentException.class, () -> pb.processa(fatura, boletos));
    }

    @Test
    @DisplayName("Teste caso passemos uma fatura como null")
    void testCase05() {
        boletos.add(new Boleto("1234", new Date(), 1500));

        assertThrows(IllegalArgumentException.class, () -> pb.processa(null, boletos));
    }

    @Test
    @DisplayName("Teste caso passemos uma fatura como null e um array vazio")
    void testCase06() {
        assertThrows(IllegalArgumentException.class, () -> pb.processa(null, boletos));
    }

    @Test
    @DisplayName("Teste caso o pagamento possua valor igual ao valor da fatura")
    void testCase07() {
        boletos.add(new Boleto("1234", new Date(), 1500));
        List<Pagamento> pagamentos = pb.processa(fatura, boletos);

        assertEquals(1, pagamentos.size());
        assertEquals(1, fatura.getPagamentos().size());
        assertEquals("PAGA", fatura.getEstado());
    }

    @Test
    @DisplayName("Teste caso o pagamento possua valor igual ao valor da fatura + 1")
    void testCase08() {
        boletos.add(new Boleto("1234", new Date(), 1501));
        List<Pagamento> pagamentos = pb.processa(fatura, boletos);

        assertEquals(1, pagamentos.size());
        assertEquals(1, fatura.getPagamentos().size());
        assertEquals("PAGA", fatura.getEstado());
    }

    @Test
    @DisplayName("Teste caso o pagamento possua valor igual ao valor da fatura - 1")
    void testCase09() {
        boletos.add(new Boleto("1234", new Date(), 1499));
        List<Pagamento> pagamentos = pb.processa(fatura, boletos);

        assertEquals(1, pagamentos.size());
        assertEquals(1, fatura.getPagamentos().size());
        assertEquals("PENDENTE", fatura.getEstado());
    }
}
