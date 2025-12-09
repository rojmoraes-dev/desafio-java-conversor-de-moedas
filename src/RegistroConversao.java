import java.time.LocalDateTime;

public class RegistroConversao {
    public final String moedaOrigem;
    public final String moedaDestino;
    public final double valorOriginal;
    public final double valorConvertido;
    public final double taxaConversao;
    public final LocalDateTime dataHora;

    public RegistroConversao(String moedaOrigem, String moedaDestino,
                             double valorOriginal, double valorConvertido,
                             double taxaConversao, LocalDateTime dataHora) {
        this.moedaOrigem = moedaOrigem;
        this.moedaDestino = moedaDestino;
        this.valorOriginal = valorOriginal;
        this.valorConvertido = valorConvertido;
        this.taxaConversao = taxaConversao;
        this.dataHora = dataHora;
    }
}