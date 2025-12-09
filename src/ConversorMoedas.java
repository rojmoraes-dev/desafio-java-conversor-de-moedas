import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class ConversorMoedas {
    private final String apiKey;
    private final HttpClient client;
    private final List<RegistroConversao> historico;
    private final GerenciadorLogs gerenciadorLogs;
    private final Gson gson;
    private static final String BASE_URL = "https://v6.exchangerate-api.com/v6/";

    public ConversorMoedas(String apiKey) {
        this.apiKey = apiKey;
        this.client = HttpClient.newHttpClient();
        this.historico = new ArrayList<>();
        this.gerenciadorLogs = new GerenciadorLogs();
        this.gson = new GsonBuilder()
                .setPrettyPrinting()
                .create();
    }

    public void converter(String moedaOrigem, String moedaDestino, double valor) {
        try {
            String url = BASE_URL + apiKey + "/pair/" + moedaOrigem + "/" + moedaDestino;

            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(url))
                    .GET()
                    .build();

            System.out.println("\nConsultando taxa de câmbio...");

            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

            if (response.statusCode() == 200) {
                RespostaAPI resposta = gson.fromJson(response.body(), RespostaAPI.class);

                if (resposta != null && "success".equals(resposta.result)) {
                    double valorConvertido = valor * resposta.conversion_rate;
                    LocalDateTime dataHora = LocalDateTime.now();

                    System.out.println("\n-------------------------------------------");
                    System.out.printf("Valor original: %.2f %s%n", valor, moedaOrigem);
                    System.out.printf("Taxa de conversão: %.6f%n", resposta.conversion_rate);
                    System.out.printf("Valor convertido: %.2f %s%n", valorConvertido, moedaDestino);
                    System.out.printf("Data/Hora: %s%n", dataHora.format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss")));
                    System.out.println("-------------------------------------------");

                    RegistroConversao registro = new RegistroConversao(
                            moedaOrigem,
                            moedaDestino,
                            valor,
                            valorConvertido,
                            resposta.conversion_rate,
                            dataHora
                    );

                    historico.add(registro);
                    gerenciadorLogs.registrarConversao(registro);

                    System.out.println("✓ Conversão registrada no histórico!");

                } else {
                    System.out.println("Erro na resposta da API: " +
                            (resposta != null ? resposta.error_type : "Resposta nula"));
                }
            } else {
                System.out.println("Erro HTTP: " + response.statusCode());
            }

        } catch (IOException | InterruptedException e) {
            System.out.println("Erro ao realizar a conversão: " + e.getMessage());
        }
    }

    public void exibirHistorico() {
        if (historico.isEmpty()) {
            System.out.println("\n⚠ Nenhuma conversão realizada ainda.");
            return;
        }

        System.out.println("\n===========================================");
        System.out.println("      HISTÓRICO DE CONVERSÕES");
        System.out.println("===========================================");

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss");

        for (int i = historico.size() - 1; i >= 0; i--) {
            RegistroConversao reg = historico.get(i);
            System.out.printf("\n[%d] %s%n", historico.size() - i, reg.dataHora.format(formatter));
            System.out.printf("    %s %.2f → %s %.2f%n",
                    reg.moedaOrigem, reg.valorOriginal,
                    reg.moedaDestino, reg.valorConvertido);
            System.out.printf("    Taxa: %.6f%n", reg.taxaConversao);
        }

        System.out.println("\n===========================================");
        System.out.printf("Total de conversões: %d%n", historico.size());
        System.out.println("===========================================");
    }

    public void exportarHistoricoJson() {
        if (historico.isEmpty()) {
            System.out.println("\n⚠ Nenhuma conversão para exportar.");
            return;
        }

        gerenciadorLogs.salvarHistoricoCompleto(historico);
    }
}