import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        System.out.println("===========================================");
        System.out.println("   BEM-VINDO AO CONVERSOR DE MOEDAS");
        System.out.println("===========================================\n");

        System.out.print("Por favor, insira sua chave da API ExchangeRate: ");
        String apiKey = scanner.nextLine().trim();

        if (apiKey.isEmpty()) {
            System.out.println("Erro: Chave da API não pode estar vazia!");
            scanner.close();
            return;
        }

        ConversorMoedas conversor = new ConversorMoedas(apiKey);

        while (true) {
            exibirMenu();
            System.out.print("Escolha uma opção: ");

            int opcao;
            try {
                opcao = Integer.parseInt(scanner.nextLine());
            } catch (NumberFormatException e) {
                System.out.println("Opção inválida! Digite um número.\n");
                continue;
            }

            if (opcao == 9) {
                System.out.println("\nObrigado por usar o Conversor de Moedas!");
                break;
            }

            if (opcao == 7) {
                conversor.exibirHistorico();
                continue;
            }

            if (opcao == 8) {
                conversor.exportarHistoricoJson();
                continue;
            }

            if (opcao < 1 || opcao > 6) {
                System.out.println("Opção inválida! Escolha entre 1 e 9.\n");
                continue;
            }

            if (opcao >= 1 && opcao <= 6) {
                System.out.print("Digite o valor a converter: ");
                double valor;
                try {
                    valor = Double.parseDouble(scanner.nextLine());
                    if (valor < 0) {
                        System.out.println("Valor não pode ser negativo!\n");
                        continue;
                    }
                } catch (NumberFormatException e) {
                    System.out.println("Valor inválido!\n");
                    continue;
                }

                realizarConversao(conversor, opcao, valor);
            }
        }

        scanner.close();
    }

    private static void exibirMenu() {
        System.out.println("\n===========================================");
        System.out.println("           MENU DE CONVERSÕES");
        System.out.println("===========================================");
        System.out.println("1. Dólar (USD) => Real Brasileiro (BRL)");
        System.out.println("2. Real Brasileiro (BRL) => Dólar (USD)");
        System.out.println("3. Euro (EUR) => Real Brasileiro (BRL)");
        System.out.println("4. Real Brasileiro (BRL) => Euro (EUR)");
        System.out.println("5. Dólar (USD) => Peso Argentino (ARS)");
        System.out.println("6. Peso Argentino (ARS) => Dólar (USD)");
        System.out.println("-------------------------------------------");
        System.out.println("7. Ver Histórico de Conversões");
        System.out.println("8. Exportar Histórico para JSON");
        System.out.println("9. Sair");
        System.out.println("===========================================");
    }

    private static void realizarConversao(ConversorMoedas conversor, int opcao, double valor) {
        String moedaOrigem = "";
        String moedaDestino = "";

        switch (opcao) {
            case 1:
                moedaOrigem = "USD";
                moedaDestino = "BRL";
                break;
            case 2:
                moedaOrigem = "BRL";
                moedaDestino = "USD";
                break;
            case 3:
                moedaOrigem = "EUR";
                moedaDestino = "BRL";
                break;
            case 4:
                moedaOrigem = "BRL";
                moedaDestino = "EUR";
                break;
            case 5:
                moedaOrigem = "USD";
                moedaDestino = "ARS";
                break;
            case 6:
                moedaOrigem = "ARS";
                moedaDestino = "USD";
                break;
        }

        conversor.converter(moedaOrigem, moedaDestino, valor);
    }
}