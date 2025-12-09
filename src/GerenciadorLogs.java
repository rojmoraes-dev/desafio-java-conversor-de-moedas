import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import java.io.FileWriter;
import java.io.IOException;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class GerenciadorLogs {
    private static final String ARQUIVO_LOG = "conversoes_log.json";
    private static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss");
    private final Gson gson;

    public GerenciadorLogs() {
        this.gson = new GsonBuilder()
                .setPrettyPrinting()
                .create();
    }

    public void registrarConversao(RegistroConversao registro) {
        System.out.println("✓ Log registrado internamente");
    }

    public void salvarHistoricoCompleto(List<RegistroConversao> historico) {
        try (FileWriter writer = new FileWriter(ARQUIVO_LOG)) {
            JsonObject jsonPrincipal = new JsonObject();
            JsonArray arrayConversoes = new JsonArray();

            for (RegistroConversao reg : historico) {
                JsonObject conversao = new JsonObject();
                conversao.addProperty("data_hora", reg.dataHora.format(formatter));
                conversao.addProperty("moeda_origem", reg.moedaOrigem);
                conversao.addProperty("moeda_destino", reg.moedaDestino);
                conversao.addProperty("valor_original", reg.valorOriginal);
                conversao.addProperty("valor_convertido", reg.valorConvertido);
                conversao.addProperty("taxa_conversao", reg.taxaConversao);

                arrayConversoes.add(conversao);
            }

            jsonPrincipal.add("historico_conversoes", arrayConversoes);
            jsonPrincipal.addProperty("total_conversoes", historico.size());
            jsonPrincipal.addProperty("gerado_em", java.time.LocalDateTime.now().format(formatter));

            String jsonFormatado = gson.toJson(jsonPrincipal);
            writer.write(jsonFormatado);

            System.out.println("\n✓ Histórico exportado com sucesso para: " + ARQUIVO_LOG);
            System.out.println("  Total de conversões exportadas: " + historico.size());

        } catch (IOException e) {
            System.out.println("✗ Erro ao exportar histórico: " + e.getMessage());
        }
    }
}